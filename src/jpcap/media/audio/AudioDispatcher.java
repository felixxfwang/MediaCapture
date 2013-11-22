package jpcap.media.audio;

import java.util.concurrent.ExecutorService;

import jpcap.media.CaptureLauncher;
import jpcap.media.Dispatcher;
import jpcap.media.PacketQueue;
import jpcap.media.ResultProcesser;
import jpcap.media.net.AudioJudger;
import jpcap.media.net.IPPortPair;
import jpcap.media.packet.TimePacket;
import jpcap.packet.TCPPacket;

public class AudioDispatcher extends Dispatcher {
	private Audio audio;

	public AudioDispatcher(PacketQueue queue, Audio audio, ExecutorService executor,
			ResultProcesser processer) {
		super(queue,executor,processer);
		this.audio = audio;
	}

	@Override
	public void run() {
		try {
			while (!CaptureLauncher.oneTimeCaptureEnd) {
				TimePacket tp = q.take();
				if (audio != null){
					audio.addPacket(tp);
				}
				TCPPacket tcp = tp.getPacket();
				if (!mediaGet) {
					mediaGet = AudioJudger.audioGet(tcp);
					continue;
				}
				
				if (!mediaRes) {
					IPPortPair ipport = AudioJudger.audioResponse(tcp);
					if (mediaRes = ipport != null) {						
						audio.setIpport(ipport);
						audio.startAnalyst(exec, processer);
						audio.addPacket(tp);
						
						mediaGet = false;
						mediaRes = false;
					}
				}				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
