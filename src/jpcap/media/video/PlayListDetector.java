package jpcap.media.video;

import java.text.ParseException;

import jpcap.media.PacketQueue;
import jpcap.media.net.HttpResponsePacket;
import jpcap.media.net.IPPortPair;
import jpcap.media.net.VideoJudger;
import jpcap.media.packet.TimePacket;
import jpcap.packet.TCPPacket;

public class PlayListDetector{
	private PacketQueue q;

	public PlayListDetector(PacketQueue queue){
		this.q = queue;
	}

	public Video findPlayList() throws ParseException, InterruptedException {
		boolean playListGet = false;
		IPPortPair ipport = null;
		while(!playListGet){
			TimePacket tp = q.take();
			TCPPacket tcp = tp.getPacket();
			ipport = VideoJudger.playListGet(tcp);
			playListGet = ipport != null;	
		}
		
		int jsonLength = 0;
		int currentDataLength = 0;
		String json = "";
		while(jsonLength <= 0){
			TimePacket tp = q.take();
			TCPPacket tcp = tp.getPacket();
			HttpResponsePacket packet;
			if((packet = VideoJudger.playListResponse(tcp, ipport)) != null){
				jsonLength = packet.contentLength;
				json += new String(packet.data);
				currentDataLength += packet.data.length;
			}
		}
		while(currentDataLength < jsonLength){
			TimePacket tp = q.take();
			TCPPacket tcp = tp.getPacket();
			if(tcp.src_ip.getHostAddress().equals(ipport.ip) && tcp.dst_port == ipport.port){
				json += new String(tcp.data);
				currentDataLength += tcp.data.length;
			}
		}
		Video video = Video.getVideo(json);
		return video;
	}
}
