package course.spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPInputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import course.dal.SpiderTextDal;
import course.dal.bean.WeiboCommentData;
import course.dal.bean.WeiboResponse;
import course.dal.bean.WeiboResponseData;

public class Spider {
	private final String UserAgent = "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
	private final String Host = "m.weibo.cn";
	private final String Accept = "application/json, text/plain, */*";
	private final String AcceptLanguage = "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3";
	private final String AcceptEncoding = "gzip, deflate, br";
	private final String Referer = "https://m.weibo.cn/detail/4154417035431509";
	private final String Cookie = "_T_WM=d5206f133ba763406c49b7de48ed0400;SUB=_2A25xqtQjDeRhGeNI7FYQ8yfEzDSIHXVTVPxrrDV6PUJbkdAKLWv1kW1NSCOhzR2Mn4fx7k7pvLMuLgarD3bCsN-a; SUHB=0xnCi892NssGAb;  M_WEIBOCN_PARAMS=luicode%3D10000011%26lfid%3D102803%26uicode%3D20000174; MLOGIN=1; WEIBOCN_FROM=1110006030; XSRF-TOKEN=71765d";
	private final String DNT = "1";
	private final String commentId = "4154417035431509";
	private final String url = "https://m.weibo.cn/comments/hotflow";
	private final String XSRF = "71765d";
	private ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private SpiderTextDal spiderTextDal = (SpiderTextDal) context.getBean("spiderTextDal");
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	public static void main(String[] args) throws Exception {
		Spider http = new Spider();
		http.sendGet();
	}

	public void sendGet() {
		int pageNum = 1;
		BufferedReader in = null;
		String max_id = "";
		String max_id_type = "";
		while (true) {
			System.out.println("正在爬取第" + pageNum + "条, " + df.format(new java.util.Date()));
			try {
				String urlNameString = url + "?id=" + commentId + "&mid=4346865082030790";
				if (!max_id.isEmpty()) {
					urlNameString += "&max_id=" + max_id + "&max_id_type=" + max_id_type;
					max_id = "";
				}
				urlNameString += "&max_id_type=" + (max_id_type.equals("") ? 0 : max_id_type);
				max_id_type = "";
				URL realUrl = new URL(urlNameString);
				HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
				connection.setRequestProperty("User-agent", UserAgent);
				connection.setRequestProperty("Host", Host);
				connection.setRequestProperty("Accept", Accept);
				connection.setRequestProperty("Accept-Language", AcceptLanguage);
				connection.setRequestProperty("Accept-Encoding", AcceptEncoding);
				connection.setRequestProperty("Referer", Referer);
				connection.setRequestProperty("Cookie", Cookie);
				connection.setRequestProperty("MWeibo-Pwa", DNT);
				connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
				connection.setRequestProperty("X-XSRF-TOKEN", XSRF);

				connection.connect();
				if (connection.getResponseCode() != 200) {
					break;
				}
				String encode = connection.getContentEncoding();
				if (encode == null) {
					encode = "";
				}
				if (encode.equals("gzip")) {
					in = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
				} else {
					in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				}
				String line;
				while ((line = in.readLine()) != null) {
					String responseStr = SpiderUtil.unicodeToString(line);
					WeiboResponse response = JSON.parseObject(responseStr, new TypeReference<WeiboResponse>() {
					});
					if (response.getOk() != 1) {
						System.err.println("返回标志ok != 1");
						continue;
					}
					WeiboResponseData weiboResponseData = response.getData();
					if (weiboResponseData == null) {
						System.out.println("第" + pageNum + "条为空");
						break;
					}
					max_id = weiboResponseData.getMax_id();
					max_id_type = weiboResponseData.getMax_id_type();
					for (WeiboCommentData retval : response.getData().getData()) {
						if (!retval.getText().isEmpty()) {
							String insertText = SpiderUtil.htmlRemoveTag(retval.getText());
							spiderTextDal.insert(insertText, 0, null, null);
						}
					}
				}
				pageNum++;
			} catch (Exception e) {
				System.out.println("发送GET请求出现异常！" + e);
				e.printStackTrace();
				break;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			Long time = Math.round(Math.random() * 10000);
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}