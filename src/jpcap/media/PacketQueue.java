package jpcap.media;

import java.util.concurrent.LinkedBlockingQueue;

import jpcap.media.packet.TimePacket;

public class PacketQueue extends LinkedBlockingQueue<TimePacket> {
	private static final long serialVersionUID = 6889499096350509994L;
}
