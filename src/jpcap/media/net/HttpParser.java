package jpcap.media.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jpcap.packet.TCPPacket;

public class HttpParser {
	public static final byte CR = '\r';
	public static final byte LF = '\n';
	public static final byte[] CRLF = { CR, LF };
	public static final byte[] CRLF2 = { CR, LF, CR, LF };
	public static final String VERSION = "HTTP/1.1";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String LOCATION = "Location";
	public static final String HOST = "Host";

	ByteArrayInputStream input;

	private byte[] contact(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public HttpPacket parse(TCPPacket tcp) {
		if (tcp.data.length != 0) {
			byte[] data = contact(tcp.data, CRLF2);
			input = new ByteArrayInputStream(data);
			HttpPacket packet;
			try {
				String statusLine = readStatusLine();
				if (isHttpPacket(statusLine)) {
					Map<String, String> headers = readHeaders();
					byte[] payLoadData = readResponseBody();
					String[] status = statusLine.split(" ");
					if (status[0].contains(VERSION)) {
						String version = status[0];
						int code = Integer.parseInt(status[1]);
						int length = Integer.parseInt(headers
								.containsKey(CONTENT_LENGTH) ? headers
								.get(CONTENT_LENGTH).trim() : "-1");
						String contentType = headers.get(CONTENT_TYPE);
						String location = headers.get(LOCATION);
						packet = new HttpResponsePacket(version, code,
								contentType, length, location, payLoadData,
								tcp.src_ip.getHostAddress(), tcp.dst_port);

					} else {
						String method = status[0];
						String uri = status[1];
						String version = status[2];
						String host = headers.get(HOST);
						packet = new HttpRequestPacket(method, uri, version,
								host, payLoadData, tcp.dst_ip.getHostAddress(), tcp.src_port);
					}
					return packet;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
		return null;
	}

	private boolean isHttpPacket(String line) {
		return line.contains(VERSION);
	}

	private Map<String, String> readHeaders() throws IOException {
		Map<String, String> headers = new HashMap<String, String>();

		String line;

		while (!("".equals(line = readLine()))) {
			String[] nv = line.split(": "); // 头部字段的名值都是以(冒号+空格)分隔的
			if (nv.length == 2) {
				headers.put(nv[0], nv[1]);
			}
		}

		return headers;
	}

	private String readStatusLine() throws IOException {
		return readLine();
	}

	/**
	 * 读取以CRLF分隔的一行，返回结果不包含CRLF
	 */
	private String readLine() throws IOException {
		int b;
		ByteArrayOutputStream buff = new ByteArrayOutputStream();
		while ((b = input.read()) != CR) {
			buff.write(b);
		}
		input.read(); // 读取 LF
		String line = buff.toString();
		return line;
	}
	private byte[] readResponseBody() throws IOException {          
        ByteArrayOutputStream buff = new ByteArrayOutputStream();            
        int b;  
        while((b = input.read()) != -1 ) { 
        	if(b == CR || b==LF) continue;
            buff.write(b);  
        }            
        return buff.toByteArray();  
    }  
}
