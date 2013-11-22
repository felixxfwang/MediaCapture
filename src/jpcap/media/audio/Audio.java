package jpcap.media.audio;

import java.util.concurrent.ExecutorService;

import jpcap.media.ResultProcesser;
import jpcap.media.net.IPPortPair;
import jpcap.media.packet.TimePacket;

public class Audio {
	private long size;
	private double seconds;
	
	private IPPortPair ipport;
	private boolean analyzing = false;
	AudioPacketAnalyst analyst;
	
	/**
	 * ��ø���ƵƬ�εĲ�������,Ԥ��6 byte/ms
	 * @return
	 */
	public long playVelocity(){
		return 6;
	}
	
	public IPPortPair getIpport() {
		return ipport;
	}

	public void setIpport(IPPortPair ipport) {
		this.ipport = ipport;
	}
	
	public boolean startAnalyst(ExecutorService excutor,ResultProcesser processer){
		if(analyzing){
			return false;
		}
		if(ipport == null){
			return false;
		}else{
			analyst = new AudioPacketAnalyst(ipport,this);
			analyst.setProcesser(processer);
			excutor.execute(analyst);
			analyzing = true;
			return true;
		}
	}
	
	/**
	 * ����ƵƬ���Ƿ����ڽ��а�����
	 * @return
	 */
	public boolean analyzing(){
		return this.analyzing;
	}
	
	/**
	 * ����ƵƬ�����ڽ��а�����������£�����µķ�����
	 * @param packet
	 * @return
	 * @throws InterruptedException
	 */
	public boolean addPacket(TimePacket packet) throws InterruptedException{
		if(analyzing){
			analyst.addPacket(packet);
			return true;
		}
		return false;
	}

	
	public double getSeconds() {
		return seconds;
	}

	public void setSeconds(double seconds) {
		this.seconds = seconds;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}	
}
