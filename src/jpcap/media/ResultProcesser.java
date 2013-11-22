package jpcap.media;

public interface ResultProcesser {
	void processVelocity(long velocity,long averageVelocity,long time);
	void processLostPackets(int lostPackets,long time);
	void processFluency(long fluency,long time);
	void segmentEnd();
	void allEnd();
}
