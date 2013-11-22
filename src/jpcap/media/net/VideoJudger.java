package jpcap.media.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jpcap.packet.TCPPacket;

public class VideoJudger {
	private static Logger logger = LogManager.getLogger(VideoJudger.class);
	
	public static IPPortPair playListGet(TCPPacket tcp) {
		HttpParser httpParser = new HttpParser();
		IPPortPair ipport = null;
		HttpPacket httpPacket;
		if ((httpPacket = httpParser.parse(tcp)) != null
				&& (httpPacket instanceof HttpRequestPacket)) {
			HttpRequestPacket request = (HttpRequestPacket) httpPacket;
			ipport = request.playListGet();
			if (ipport != null)
				logger.info(httpPacket);
		}
		return ipport;
	}

	public static HttpResponsePacket playListResponse(TCPPacket tcp,
			IPPortPair ipport) {
		HttpParser httpParser = new HttpParser();
		HttpPacket httpPacket;
		if ((httpPacket = httpParser.parse(tcp)) != null
				&& (httpPacket instanceof HttpResponsePacket)) {
			HttpResponsePacket response = (HttpResponsePacket) httpPacket;
			int length = response.playListResponse(ipport);
			if (length > 0) {
				logger.info(httpPacket);
				return response;
			}
		}
		return null;
	}

	public static boolean videoGet(TCPPacket tcp) {
		HttpParser httpParser = new HttpParser();
		HttpPacket httpPacket;
		if ((httpPacket = httpParser.parse(tcp)) != null
				&& (httpPacket instanceof HttpRequestPacket)) {
			HttpRequestPacket request = (HttpRequestPacket) httpPacket;
			boolean flag = request.videoGet();
			if (flag)
				logger.info(httpPacket);
			return flag;
		}
		return false;
	}

	public static IPPortPair videoResponse(TCPPacket tcp) {
		HttpParser httpParser = new HttpParser();
		IPPortPair ipport = null;
		HttpPacket httpPacket;
		if ((httpPacket = httpParser.parse(tcp)) != null
				&& (httpPacket instanceof HttpResponsePacket)) {
			HttpResponsePacket response = (HttpResponsePacket) httpPacket;
			ipport = response.videoResponse();
			if (ipport != null)
				logger.info(httpPacket);
		}
		return ipport;
	}
}
