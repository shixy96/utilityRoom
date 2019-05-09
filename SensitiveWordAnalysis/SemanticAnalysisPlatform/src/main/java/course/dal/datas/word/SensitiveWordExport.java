package course.dal.datas.word;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import course.bll.SensitiveWordManager;
import course.dal.bean.SensitiveWordData;
import course.util.IOUtil;
import course.util.StringUtil;

public class SensitiveWordExport {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context
			.getBean("sensitiveWordManager");

	private static final String fileName = "src/test/resources/SensitiveWord.txt";
	private static final String nature = "sen";

	private static final int limit_once = 500;

	public static void main(String args[]) {
		int offset = 0;
		List<SensitiveWordData> datas = sensitiveWordManager.search(null, null, offset++ * limit_once, limit_once);
		StringBuffer sBuffer;
		while (datas != null && datas.size() > 0) {
			for (SensitiveWordData sensitiveWordData : datas) {
				sBuffer = new StringBuffer(StringUtil.replaceBlank(sensitiveWordData.getWord()));
				sBuffer.append(
						"	" + nature + sensitiveWordData.getLevel() + "	" + sensitiveWordData.getLevel() + "\n");
				IOUtil.output_to_file(sBuffer.toString(), fileName, true);
			}
			System.out.println(offset * limit_once);
			datas = sensitiveWordManager.search(null, null, offset++ * limit_once, limit_once);
		}
	}

}
