package jpcap.media.packet;

import jpcap.packet.IPPacket;

public class RTPPacket extends IPPacket{
	private static final long serialVersionUID = -5148364932237290120L;
	private int version;
	private int padding;
	private int extension;
	private int cc;
	private int marker;
	private RTPPayloadType payloadType;
	private int sequenceNumber;
	private long timestamp;
	private int ssrc;

	public RTPPacket(Builder builder) {
		this.version = builder.version;
		this.padding = builder.padding;
		this.extension = builder.extension;
		this.cc = builder.cc;
	}
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	public int getPadding() {
		return padding;
	}
	public void setPadding(int padding) {
		this.padding = padding;
	}
	public int getExtension() {
		return extension;
	}
	public void setExtension(int extension) {
		this.extension = extension;
	}
	public int getCc() {
		return cc;
	}
	public void setCc(int cc) {
		this.cc = cc;
	}
	public int getMarker() {
		return marker;
	}
	public void setMarker(int marker) {
		this.marker = marker;
	}
	public RTPPayloadType getPayloadType() {
		return payloadType;
	}
	public void setPayloadType(RTPPayloadType payloadType) {
		this.payloadType = payloadType;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getSsrc() {
		return ssrc;
	}
	public void setSsrc(int ssrc) {
		this.ssrc = ssrc;
	}

	class Builder{
		public int version;
		public int padding;
		public int extension;
		public int cc;
		public int marker;
		public RTPPayloadType payloadType;
		public int sequenceNumber;
		public long timestamp;
		public int ssrc;
		public int csrc;
		
		public Builder version(int v){
			this.version  = v;
			return this;
		}
		public Builder padding(int p){
			this.padding  = p;
			return this;
		}
		public Builder extension(int x){
			this.extension  = x;
			return this;
		}
		public Builder cc(int cc){
			this.cc  = cc;
			return this;
		}
		
		public Builder marker(int m){
			this.marker  = m;
			return this;
		}
		public Builder payloadType(RTPPayloadType pt){
			this.payloadType  = pt;
			return this;
		}
		public Builder sequeceNumber(int sn){
			this.sequenceNumber  = sn;
			return this;
		}
		public Builder timestamp(long timestamp){
			this.timestamp  = timestamp;
			return this;
		}		
		public Builder ssrc(int ssrc){
			this.ssrc = ssrc;
			return this;
		}
		public Builder csrc(int csrc){
			this.csrc = csrc;
			return this;
		}
		
		public RTPPacket build(){
			return new RTPPacket(this);
		}
	}
	
	enum RTPPayloadType{
		
	}
}
