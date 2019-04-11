package course.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;

import course.bll.SpiderTextManage;
import course.bll.SpiderTextNonrepeatManage;
import course.dal.bean.SpiderTextData;

public class SpiderTextNonrepeatInit {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SpiderTextManage spiderTextManage = (SpiderTextManage) context.getBean("spiderTextManage");
	private static SpiderTextNonrepeatManage spiderTextNonrepeatManage = (SpiderTextNonrepeatManage) context
			.getBean("spiderTextNonrepeatManage");
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	
	public static void main(String args[]) {
		int page = 0;
		List<SpiderTextData> datas = new ArrayList<SpiderTextData>();
		datas = spiderTextManage.search(null, null, null, null, null, page++, 100);
		while (!CollectionUtils.isEmpty(datas)) {
			System.out.println("正在转换第" + page + "页, " + df.format(new java.util.Date()));
			for (SpiderTextData spiderData : datas) {
				if (!spiderTextNonrepeatManage.exist(spiderData.getText())) {
					spiderTextNonrepeatManage.insert(spiderData.getText(), null, null, null);
				}
			}
			datas = spiderTextManage.search(null, null, null, null, null, page++, 100);
		}
	}
}
