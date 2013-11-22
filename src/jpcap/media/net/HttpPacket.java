package jpcap.media.net;

public abstract class HttpPacket {
	public static final String[] VIDEO_EXTS = { ".mp4", ".f4v","" };
	public static final String[] VIDEO_MIMES = { "video/mp4", "video/x-flv" };	

	public String ip;
	public int port;

	public byte[] data;	

	public abstract String toString();
}
