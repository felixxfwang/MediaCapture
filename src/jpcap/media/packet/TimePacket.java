package jpcap.media.packet;

import jpcap.packet.TCPPacket;

public class TimePacket {
	private TCPPacket packet;
    private	long time;

	public TimePacket(TCPPacket p) {
		time = System.currentTimeMillis();
		packet = p;
	}

	public TCPPacket getPacket() {
		return this.packet;
	}

	public long getTime() {
		return this.time;
	}
}
