package jpcap.media.net;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpRequest {

	public HttpResult get(String url) throws ClientProtocolException,
			IOException {
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(httpGet);
		int code = response.getStatusLine().getStatusCode();
		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		return new HttpResult(code, result);
	}

	public HttpResult get(String url, ArrayList<NameValuePair> params)
			throws ClientProtocolException, IOException {
		url += "?";
		for (NameValuePair param : params) {
			url += param.getName() + "=" + param.getValue() + "&";
		}
		url = url.substring(0, url.length() - 1);
		return get(url);
	}

	public class HttpResult {
		public int code;
		public String jsonResult;

		public HttpResult(int code, String result) {
			this.code = code;
			this.jsonResult = result;
		}
	}
}
