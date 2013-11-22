package jpcap.media.video;

import java.util.concurrent.ExecutorService;

import jpcap.media.CaptureLauncher;
import jpcap.media.Dispatcher;
import jpcap.media.PacketQueue;
import jpcap.media.ResultProcesser;
import jpcap.media.net.IPPortPair;
import jpcap.media.net.VideoJudger;
import jpcap.media.packet.TimePacket;
import jpcap.packet.TCPPacket;

public class VideoDispatcher extends Dispatcher {
	//private Logger logger = LogManager.getLogger(Dispatcher.class);
	private Video video;
	private String type;

	public VideoDispatcher(PacketQueue queue,String type, Video video, ExecutorService executor,
			ResultProcesser processer) {
		super(queue,executor,processer);
		this.video = video;
		this.type = type;
	}

	@Override
	public void run() {
		try {
			while (!CaptureLauncher.oneTimeCaptureEnd) {
				TimePacket tp = q.take();
				if (video != null){
					video.addPacket(type, tp);
				}
				TCPPacket tcp = tp.getPacket();
				if (!mediaGet) {
					mediaGet = VideoJudger.videoGet(tcp);
					continue;
				}
				
				if (!mediaRes) {
					IPPortPair ipport = VideoJudger.videoResponse(tcp);
					if (mediaRes = ipport != null) {						
						VideoSegment segment = video.takeVideoSegment(type);
						if(segment != null){
							segment.setIpport(ipport);
							segment.addPacket(tp);
							segment.startAnalyst(exec, processer);
						}
						
						mediaGet = false;
						mediaRes = false;
					}
				}				
			}
			System.out.println("Dispatcher Interrupted");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Dispatcher InterruptException");
		}
	}
}
