package course.dal.datas.word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hankcs.hanlp.HanLP;

import course.bll.TxtCollectionManager;
import course.bll.SensitiveWordManager;
import course.dal.bean.SensitiveNatureLevel;
import course.dal.datas.DataInit;

public class SenstiveWordInit implements DataInit {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context
			.getBean("sensitiveWordManager");
	private static TxtCollectionManager txtCollectionManager = (TxtCollectionManager) context
			.getBean("txtCollectionManager");
	private static final String fileName1 = "src/test/resources/敏感词.txt";
	private static final String fileName2 = "src/test/resources/CensorWords.txt";
	private static Logger logger = LoggerFactory.getLogger(SenstiveWordInit.class);

	public static void main(String args[]) {
		SenstiveWordInit senstiveWordInit = new SenstiveWordInit();
		senstiveWordInit.init(fileName1);
		senstiveWordInit.init(fileName2);
	}

	@Override
	public void init(String resourceFileName) {
		FileReader(resourceFileName);
	}

	@SuppressWarnings("resource")
	private static void FileReader(String fileName) {
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
				int wordNum = HanLP.segment(line).size();
				if (line.length() > 0) {
					if ((line.length() <= 5 || wordNum == 1)) {
						if (!sensitiveWordManager.exist(line)) {
							sensitiveWordManager.insert(line, SensitiveNatureLevel.source.level());
						}
					} else if (!txtCollectionManager.exist(line)) {
						txtCollectionManager.insert(line, true, null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}