package course.spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

public class spider {
	private final String Connection = "Keep-Alive";
	private final String UserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";
	private final String Host = "m.weibo.cn";
	private final String Accept = "application/json, text/plain, */*";
	private final String AcceptLanguage = "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3";
	private final String AcceptEncoding = "gzip, deflate, br";
	private final String Referer = "https://m.weibo.cn/status/4160547165300149";
	private final String Cookie = "_T_WM=e25a28bec35b27c72d37ae2104433873; WEIBOCN_WM=3349; H5_wentry=H5; backURL=http%3A%2F%2Fm.weibo.cn%2F; SUB=_2A250zXayDeThGeVJ7VYV8SnJyTuIHXVUThr6rDV6PUJbkdBeLRDzkW1FrGCo75fsx_qRR822fcI2HoErRQ..; SUHB=0sqRDiYRHXFJdM; SCF=Ag4UgBbd7u4DMdyvdAjGRMgi7lfo6vB4Or8nQI4-9HQ4cLYm_RgdaeTdAH_68X4EbewMK-X4JMj5IQeuQUymxxc.; SSOLoginState=1506346722; M_WEIBOCN_PARAMS=featurecode%3D20000320%26oid%3D3638527344076162%26luicode%3D10000011%26lfid%3D1076031239246050; H5_INDEX=3; H5_INDEX_TITLE=%E8%8A%82cao%E9%85%B1";
	private final String DNT = "1";
	private final String commentId = "4160547165300149";
	private final String url = "https://m.weibo.cn/api/comments/show";

	public static void main(String[] args) throws Exception {
		spider http = new spider();
		http.sendGet();
	}

	public String sendGet() {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?id=" + commentId;
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("User-agent", UserAgent);
			connection.setRequestProperty("Host", Host);
			connection.setRequestProperty("Accept", Accept);
			connection.setRequestProperty("Accept-Language", AcceptLanguage);
			connection.setRequestProperty("Accept-Encoding", AcceptEncoding);
			connection.setRequestProperty("Referer", Referer);
			connection.setRequestProperty("Cookie", Cookie);
			connection.setRequestProperty("DNT", DNT);
			connection.setRequestProperty("Connection", Connection);
			connection.connect();
			in = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
			String line;
			while ((line = in.readLine()) != null) {
				for (String retval : SpiderUtil.unicodeToString(line).split(",")) {
					if (retval.substring(1, 5).equals("text")) {
						String[] text = retval.split(":", 2);
						System.out.println(SpiderUtil.htmlRemoveTag(text[1]));
					}
				}
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
}