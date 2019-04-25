package course.dal.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import course.bll.SensitiveWordManager;
import course.dal.bean.SensitiveNatureLevel;

public class SenstiveWordInit {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context
			.getBean("sensitiveWordManager");
	private static final String fileName = "src/test/resources/敏感词.txt";
	private static Logger logger = LoggerFactory.getLogger(SenstiveWordInit.class);

	public static void main(String args[]) {
		FileReader();
	}

	@SuppressWarnings("resource")
	private static void FileReader() {
		File fp = new File(fileName);
		if (!fp.isFile()) {
			logger.error("文件" + fileName + "不存在！");
			return;
		}
		try {
			FileReader fr = new FileReader(fp);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0 && line.length() <= 5 && !sensitiveWordManager.exist(line)) {
					sensitiveWordManager.insert(line, SensitiveNatureLevel.source.level());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}