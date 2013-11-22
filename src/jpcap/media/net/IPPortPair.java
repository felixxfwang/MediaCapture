package jpcap.media.net;

public class IPPortPair {
	public String ip;
	public int port;
	public IPPortPair(String ip,int port){
		this.ip = ip;
		this.port = port;
	}
	public IPPortPair(IPPortPair pair){
		this.ip = pair.ip;
		this.port = pair.port;
	}
}
