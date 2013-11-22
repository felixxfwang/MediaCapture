package jpcap.media.video;

import java.util.concurrent.ExecutorService;

import jpcap.media.ResultProcesser;
import jpcap.media.net.IPPortPair;
import jpcap.media.packet.TimePacket;

public class VideoSegment {
	private String type;
	private int no;
	private int segs;
	private long size;
	private double seconds;
	private String k;
	private String k2;
	private String downloadUrl;
	
	private IPPortPair ipport;
	private boolean analyzing = false;
	VideoPacketAnalyst analyst;
	
	public VideoSegment(String type, int no, long size, double seconds,
			String k, String k2) {
		this.type = type;
		this.no = no;
		this.size = size;
		this.seconds = seconds;
		this.k = k;
		this.k2 = k2;
	}
	
	/**
	 * 获得该视频片段的播放速率
	 * @return
	 */
	public long playVelocity(){
		return (long) (size/(1000*seconds));
	}
	
	public boolean endSegment(){
		return segs - no == 1;
	}
	
	public IPPortPair getIpport() {
		return ipport;
	}

	public void setIpport(IPPortPair ipport) {
		this.ipport = ipport;
	}
	
	public boolean startAnalyst(ExecutorService excutor,ResultProcesser processer){
		if(analyzing){
			return false;
		}
		if(ipport == null){
			return false;
		}else{
			analyst = new VideoPacketAnalyst(ipport,this);			
			analyst.setProcesser(processer);
			excutor.execute(analyst);
			analyzing = true;
			return true;
		}
	}
	
	/**
	 * 该视频片段是否正在进行包分析
	 * @return
	 */
	public boolean analyzing(){
		return this.analyzing;
	}
	
	/**
	 * 该视频片段正在进行包分析的情况下，添加新的分析包
	 * @param packet
	 * @return
	 * @throws InterruptedException
	 */
	public boolean addPacket(TimePacket packet) throws InterruptedException{
		if(analyzing){
			analyst.addPacket(packet);
			return true;
		}
		return false;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public double getSeconds() {
		return seconds;
	}

	public void setSeconds(double seconds) {
		this.seconds = seconds;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getK2() {
		return k2;
	}

	public void setK2(String k2) {
		this.k2 = k2;
	}

	public int getSegs() {
		return segs;
	}

	public void setSegs(int segs) {
		this.segs = segs;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
