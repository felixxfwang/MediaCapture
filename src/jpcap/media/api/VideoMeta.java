package jpcap.media.api;

public class VideoMeta {
	private String id;
	private String title;
	private String description;
	private int duration;
	private String playUrl;

	public VideoMeta(String id, String title, String description, int duration,
			String playUrl) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.duration = duration;
		this.playUrl = playUrl;
	}

	public String toString() {
		return "id:" + id + " duration:" + duration + " playUrl:" + playUrl
				+ " title:" + title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
}
