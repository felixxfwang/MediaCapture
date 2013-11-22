package jpcap.media.net;

public class HttpResponsePacket extends HttpPacket {

	public String version;
	public int statusCode;
	public String location;
	public String contentType;
	public int contentLength;

	public HttpResponsePacket(String version, int code, String contentType,
			int contentLength, String location, byte[] data, String ip, int port) {
		this.version = version;
		this.statusCode = code;
		this.contentType = contentType;
		this.contentLength = contentLength;
		this.location = location;
		this.ip = ip;
		this.port = port;
		this.data = data;
	}

	@Override
	public String toString() {
		return version + " " + statusCode + " " + " Content-Type: "
				+ contentType + " Content-Length: " + contentLength
				+ " Location: " + location;
	}

	public IPPortPair videoResponse() {
		if (contentType != null
				&& ((contentType.contains("video/mp4")
						|| (contentType.contains("video/x-flv"))) && contentLength > 1024000)) {
			return new IPPortPair(ip, port);
		}
		return null;
	}
	
	public IPPortPair audioResponse() {
		if (contentType != null
				&& ((contentType.contains("text/plain")
						|| (contentType.contains("audio/mpeg"))))) {
			return new IPPortPair(ip, port);
		}
		return null;
	}

	public int playListResponse(IPPortPair ipport) {
		if (ipport.ip.equals(ip) && ipport.port == port) {
			return this.contentLength;
		}
		return -1;
	}
}
