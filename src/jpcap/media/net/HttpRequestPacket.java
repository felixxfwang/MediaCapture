package jpcap.media.net;

public class HttpRequestPacket extends HttpPacket {
	public String method;
	public String uri;
	public String version;
	public String host;
	public String userAgent;
	public String accept;
	public String acceptLanguage;
	public String acceptEncoding;
	public String referer;
	public String cookie;
	public String connection;

	public HttpRequestPacket(String method, String uri, String version,
			String host, byte[] data,String ip,int port) {
		this.method = method.trim();
		this.uri = uri.trim();
		this.version = version.trim();
		this.host = host.trim();
		this.ip = ip;
		this.port = port;
		this.data = data;
	}

	@Override
	public String toString() {
		return method + " " + uri + " " + version + " host: " + host;
	}

	public IPPortPair playListGet() {
		IPPortPair ipport = null;
		if(method != null && method.contains("GET") && host != null
				&& host.contains("v.youku.com") && uri != null
				&& uri.startsWith("/player/getPlayList/VideoIDS/")){
			ipport = new IPPortPair(ip, port);
		}
		return ipport;
	}

	public boolean videoGet() {
		boolean video = method != null && method.contains("GET")
				&& host != null && uri.startsWith("/youku/");
		boolean validExt = false;
		for (String ext : VIDEO_EXTS) {
			validExt = validExt || uri.contains(ext);
		}
		video = video && validExt;
		return video;
	}
	
	public boolean audioGet(){
		boolean audio = method != null && method.contains("GET")
				&& host != null && uri.startsWith("/anti.s?") && host.contains("antiserver.kuwo.cn");
		return audio;
	}

}
