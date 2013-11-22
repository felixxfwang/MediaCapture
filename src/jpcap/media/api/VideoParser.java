package jpcap.media.api;

import java.text.ParseException;
import java.util.ArrayList;

public interface VideoParser {

	VideoMeta parseVideo(String jsonString) throws ParseException;

	ArrayList<VideoMeta> parseVideoList(String jsonString)
			throws ParseException;

	ArrayList<VideoMeta> parseVideoList(String jsonString, int minDuration)
			throws ParseException;

}
