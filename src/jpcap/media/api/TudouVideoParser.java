package jpcap.media.api;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class TudouVideoParser implements VideoParser {

	@Override
	public VideoMeta parseVideo(String jsonString) throws ParseException {
		JSONObject json = new JSONObject(jsonString);
		String id = json.getString("itemCode");
		String title = json.getString("title");
		String description = json.getString("description");
		int duration = json.getInt("totalTime");
		String playUrl = json.getString("playUrl");
		VideoMeta video = new VideoMeta(id, title, description, duration, playUrl);
		return video;
	}

	@Override
	public ArrayList<VideoMeta> parseVideoList(String jsonString) throws ParseException {
		ArrayList<VideoMeta> videoList = new ArrayList<VideoMeta>();
		JSONObject json = new JSONObject(jsonString);
		JSONArray jArray = json.getJSONArray("results");
		for (int i = 0; i < jArray.length(); i++) {
			String j = jArray.getString(i);
			VideoMeta video = parseVideo(j);
			videoList.add(video);
		}
		return videoList;
	}

	@Override
	public ArrayList<VideoMeta> parseVideoList(String jsonString,
			int minDuration) throws ParseException {
		ArrayList<VideoMeta> videoList = new ArrayList<VideoMeta>();
		JSONObject json = new JSONObject(jsonString);
		JSONArray jArray = json.getJSONArray("results");
		for (int i = 0; i < jArray.length(); i++) {
			String j = jArray.getString(i);
			VideoMeta video = parseVideo(j);
			if(video.getDuration() > minDuration)
				videoList.add(video);
		}
		return videoList;
	}
}
