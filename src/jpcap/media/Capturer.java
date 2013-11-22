package jpcap.media;

import jpcap.JpcapCaptor;
import jpcap.PacketReceiver;
import jpcap.media.packet.TimePacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

public class Capturer implements Runnable {
	private JpcapCaptor jc;
	private PacketQueue q;

	public Capturer(JpcapCaptor jpcapturer, PacketQueue queue) {
		jc = jpcapturer;
		q = queue;
	}

	@Override
	public void run() {
		jc.loopPacket(-1, new PacketReceiver() {

			@Override
			public void receivePacket(Packet packet) {
				try {
					if(CaptureLauncher.oneTimeCaptureEnd){
						jc.breakLoop();
						return;
					}
					if (packet instanceof TCPPacket) {
						TCPPacket tcp = (TCPPacket) packet;
						if (tcp.src_port == 80 || tcp.dst_port == 80) {
							q.put(new TimePacket(tcp));
						}
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
		System.out.println("Capturer Interrupt");
	}
}