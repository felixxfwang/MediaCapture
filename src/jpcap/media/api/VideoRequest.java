package jpcap.media.api;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import jpcap.media.net.HttpRequest;
import jpcap.media.net.HttpRequest.HttpResult;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;

public abstract class VideoRequest {

	protected final String TUDOU_VIDEO_LIST_URL = "http://api.tudou.com/v6/video/top_list";
	protected final String TUDOU_APP_KEY_NAME = "app_key";
	protected final String TUDOU_APP_KEY_VALUE = "31cf5c4915b945ea";
	protected final int TUDOU_MIN_DURATION = 600000;

	public abstract ArrayList<VideoMeta> getVideo()
			throws ClientProtocolException, IOException, ParseException;

	protected ArrayList<VideoMeta> getVideo(String url,
			ArrayList<NameValuePair> params, int minDuration)
			throws ClientProtocolException, IOException, ParseException {
		HttpRequest request = new HttpRequest();
		HttpResult result = request.get(url, params);
		if (result.code == 200) {
			VideoParser parser = new TudouVideoParser();
			return parser.parseVideoList(result.jsonResult, minDuration);
		}
		return new ArrayList<VideoMeta>();
	}
}
