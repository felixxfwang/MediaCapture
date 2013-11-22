package jpcap.media.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jpcap.packet.TCPPacket;

public class AudioJudger {

	private static Logger logger = LogManager.getLogger(AudioJudger.class);

	public static boolean audioGet(TCPPacket tcp) {
		HttpParser httpParser = new HttpParser();
		HttpPacket httpPacket;
		if ((httpPacket = httpParser.parse(tcp)) != null
				&& (httpPacket instanceof HttpRequestPacket)) {
			HttpRequestPacket request = (HttpRequestPacket) httpPacket;
			boolean flag = request.audioGet();
			if (flag)
				logger.info(httpPacket);
			return flag;
		}
		return false;
	}

	public static IPPortPair audioResponse(TCPPacket tcp) {
		HttpParser httpParser = new HttpParser();
		IPPortPair ipport = null;
		HttpPacket httpPacket;
		if ((httpPacket = httpParser.parse(tcp)) != null
				&& (httpPacket instanceof HttpResponsePacket)) {
			HttpResponsePacket response = (HttpResponsePacket) httpPacket;
			ipport = response.audioResponse();
			if (ipport != null)
				logger.info(httpPacket);
		}
		return ipport;
	}

}
