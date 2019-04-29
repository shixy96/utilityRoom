package course.dal.datas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import course.bll.SensitiveWordManager;
import course.dal.bean.SensitiveWordData;
import course.util.StringUtil;

public class SensitiveWordExport {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context
			.getBean("sensitiveWordManager");

	private static final String fileName = "src/test/resources/SensitiveWord.txt";
	private static final String nature = "sen";

	private static final int limit_once = 500;
	private static Logger logger = LoggerFactory.getLogger(SensitiveWordExport.class);

	public static void main(String args[]) {
		int offset = 0;
		List<SensitiveWordData> datas = sensitiveWordManager.search(null, null, offset++ * limit_once, limit_once);
		StringBuffer sBuffer;
		while (datas != null && datas.size() > 0) {
			for (SensitiveWordData sensitiveWordData : datas) {
				sBuffer = new StringBuffer(StringUtil.replaceBlank(sensitiveWordData.getWord()));
				sBuffer.append(
						"	" + nature + sensitiveWordData.getLevel() + "	" + sensitiveWordData.getLevel() + "\n");
				output_to_file(sBuffer.toString(), fileName);
			}
			System.out.println(offset * limit_once);
			datas = sensitiveWordManager.search(null, null, offset++ * limit_once, limit_once);
		}
	}

	private static void output_to_file(String content, String filename) {
		if (content != null) {
			BufferedWriter fp_save = null;
			try {
				fp_save = new BufferedWriter(new FileWriter(filename, true));
				fp_save.write(content.toString());
				fp_save.close();
			} catch (IOException e) {
				logger.error("can't open file " + filename, e);
				System.exit(1);
			} finally {
				if (fp_save != null) {
					try {
						fp_save.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
