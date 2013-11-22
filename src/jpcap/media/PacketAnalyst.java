package jpcap.media;

import jpcap.media.net.IPPortPair;
import jpcap.media.packet.TimePacket;

public abstract class PacketAnalyst implements Runnable {
	protected PacketQueue q = new PacketQueue();
	protected IPPortPair ipport;
	
	protected ResultProcesser processer;

	protected PacketAnalyst(IPPortPair ipport) {
		this.ipport = ipport;
	}

	public void addPacket(TimePacket packet) throws InterruptedException {
		q.put(packet);
	}

	public boolean compareWith(IPPortPair pair) {
		return ipport.ip.equals(pair.ip) && pair.port == ipport.port;
	}

	@Override
	public abstract void run();

	public void setProcesser(ResultProcesser processer) {
		this.processer = processer;
	}
}