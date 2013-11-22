package jpcap.media.api;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

public class TudouVideoRequest extends VideoRequest{

	@Override
	public ArrayList<VideoMeta> getVideo() throws ClientProtocolException, IOException, ParseException {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(TUDOU_APP_KEY_NAME,TUDOU_APP_KEY_VALUE));
		ArrayList<VideoMeta> videos = getVideo(TUDOU_VIDEO_LIST_URL, params,TUDOU_MIN_DURATION);
		return videos;
	}	
}
