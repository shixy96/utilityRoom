package course.spider;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;

import course.bll.SpiderTextManage;
import course.dal.bean.SpiderTextData;

public class Comment2File {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SpiderTextManage spiderTextManage = (SpiderTextManage) context.getBean("spiderTextManage");
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	private static final String FilePath = "src/main/resources/file/comment";
	private static final String FileExtension = ".txt";
	private static final Integer count = 500;

	public static void main(String[] args) {
		int page = -1;
		List<SpiderTextData> spiderTextDatas = spiderTextManage.search(null, null, null, null, null, ++page, count);
		while (!CollectionUtils.isEmpty(spiderTextDatas)) {
			StringBuffer sBuffer = new StringBuffer();
			System.out.println("正在转换第" + page + "页, " + "文件：" + FilePath + page/10 + FileExtension + ", "
					+ df.format(new java.util.Date()));
			for (SpiderTextData spiderData : spiderTextDatas) {
				sBuffer.append(spiderData.getText() + "\n");
			}
			output_to_file(sBuffer.toString(), FilePath + page / 10 + FileExtension);
			spiderTextDatas = spiderTextManage.search(null, null, null, null, null, ++page, count);
		}
	}

	private static void output_to_file(String content, String filename) {
		if (content != null) {
			BufferedWriter fp_save = null;
			try {
				fp_save = new BufferedWriter(new FileWriter(filename, true));
				fp_save.write(content);
				fp_save.close();
			} catch (IOException e) {
				System.err.println("can't open file " + filename);
				System.exit(1);
			}
		}
	}

}
