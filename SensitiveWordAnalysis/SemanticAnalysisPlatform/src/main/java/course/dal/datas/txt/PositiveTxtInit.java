package course.dal.datas.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hankcs.hanlp.HanLP;

import course.bll.TxtCollectionManager;
import course.dal.datas.DataInit;

public class PositiveTxtInit implements DataInit {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static TxtCollectionManager txtCollectionManager = (TxtCollectionManager) context
			.getBean("txtCollectionManager");
	private static final String fileName = "src/test/resources/weibo_senti_100k.csv";
	private static final int maxNum = 15000;
	private static Logger logger = LoggerFactory.getLogger(PositiveTxtInit.class);

	public static void main(String[] args) {
		PositiveTxtInit positiveTxtInit = new PositiveTxtInit();
		positiveTxtInit.init(fileName);
	}

	@Override
	public void init(String resourceFileName) {
		int maxWordNum = 0, num = 0;
		File fp = new File(fileName);
		if (!fp.isFile()) {
			logger.error("文件" + fileName + "不存在！");
			return;
		}
		try {
			FileReader fr = new FileReader(fp);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null && num < maxNum) {
				if (line.length() > 0 && line.substring(0, 1).equals("1")) {
					int wordNum = HanLP.segment(line).size();
					if (wordNum > maxWordNum) {
						maxWordNum = wordNum;
					}
					if (wordNum <= 20) {
						line = line.substring(2);
						if (!txtCollectionManager.exist(line)) {
							num++;
							txtCollectionManager.insert(line, false, null);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("maxWordNum = " + maxWordNum);
		System.out.println("插入" + num + "条正向文本");
	}

}
