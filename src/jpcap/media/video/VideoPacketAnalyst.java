package jpcap.media.video;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jpcap.media.CaptureLauncher;
import jpcap.media.PacketAnalyst;
import jpcap.media.net.IPPortPair;
import jpcap.media.packet.TimePacket;
import jpcap.packet.TCPPacket;

public class VideoPacketAnalyst extends PacketAnalyst {
	private Logger logger = LogManager.getLogger(PacketAnalyst.class);
	private VideoSegment segment;

	private long currentBytes = 0;
	private long lastBytes = 0;
	private long initTime = -1;
	private long lastTime = -1;
	private static long totalDeltaTime = 0;
	private static long totalBytes = 0;
	
	//for ������
	private static long startPlayTime = -1;
	private long currentPlayTime = 0;
	private long currentReceiveTime = 0;
	
	private static int currentLostPackets = 0;
	private long initSequeceNumber = -1;
	private long lastSequeceNumber = -1;
	private final long VIDEO_PACKET_LENGTH = 1506;
	
	public VideoPacketAnalyst(IPPortPair ipport,VideoSegment segment) {
		super(ipport);
		this.segment = segment;
	}

	@Override
	public void run() {
		try {
			while (!CaptureLauncher.oneTimeCaptureEnd) {
				TimePacket tp = q.take();
				TCPPacket tcp = tp.getPacket();
				if ((tcp.src_ip.getHostAddress().equals(ipport.ip) && tcp.dst_port == ipport.port)
						|| (tcp.dst_ip.getHostAddress().equals(ipport.ip) && tcp.src_port == ipport.port)) {
					if (tcp.data.length > 0) {
						currentBytes += tp.getPacket().data.length;
						totalBytes += tp.getPacket().data.length;
						if (initTime == -1) {
							initTime = tp.getTime();
							lastTime = initTime;
							lastBytes = currentBytes;
						} else {							
							long currentTime = tp.getTime();
							/* ��˲ʱ���� */
							long deltaBytes = currentBytes - lastBytes;
							long deltaTime = currentTime - lastTime;
							if (deltaTime <= 100)
								continue;
							lastTime = currentTime;
							lastBytes = currentBytes;
							long velocity = deltaTime == 0 ? -1 : deltaBytes
									/ deltaTime;
							
							totalDeltaTime += deltaTime;
							
							/* ��ƽ������ */
							long wholeDeltaTime = currentTime - initTime;
							long averageVelocity = wholeDeltaTime == 0 ? -1
									: currentBytes / wholeDeltaTime;
							if(processer != null){
								processer.processVelocity(velocity, averageVelocity, totalDeltaTime);
							}
						}
						
						//���㶪����
						if(initSequeceNumber == -1){
							initSequeceNumber = tcp.sequence;
							lastSequeceNumber = initSequeceNumber;
						}else{
							long deltaSequeceNumber = tcp.sequence - lastSequeceNumber;
							lastSequeceNumber = tcp.sequence;
							if(deltaSequeceNumber > VIDEO_PACKET_LENGTH){
								int count = (int)(deltaSequeceNumber / VIDEO_PACKET_LENGTH);
								currentLostPackets += count;
							}
							if(processer != null){
								processer.processLostPackets(currentLostPackets, totalDeltaTime);
							}
						}
						
						//����������						
						if(startPlayTime == -1){
							startPlayTime = tp.getTime();//��ʼ����ʼʱ��
						}else{
							currentPlayTime = tp.getTime() - startPlayTime;						
							currentReceiveTime = totalBytes / segment.playVelocity();
							long fluency = currentReceiveTime - currentPlayTime;
							if(processer != null){
								processer.processFluency(fluency, totalDeltaTime);
							}
						}
					}
					if(tcp.fin){
						if(processer != null){
							if(segment.endSegment()){
								processer.allEnd();
							}else{
								processer.segmentEnd();
							}
						}
						break;
					}
				}
			}
			logger.info("Thread " + Thread.currentThread().getId() + "-" + Thread.currentThread().getName() + " End");
			System.out.println("PacketAnalyst Interrupt");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			System.out.println("PacketAnalyst InterruptException");
		}
	}
}