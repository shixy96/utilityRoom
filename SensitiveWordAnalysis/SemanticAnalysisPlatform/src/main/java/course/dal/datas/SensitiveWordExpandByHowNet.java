package course.dal.datas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import course.bll.HowNetWordManager;
import course.bll.SensitiveWordManager;
import course.dal.bean.HowNetData;
import course.dal.bean.SensitiveNatureLevel;
import course.dal.bean.SensitiveWordData;
import course.util.StringUtil;

public class SensitiveWordExpandByHowNet {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context
			.getBean("sensitiveWordManager");
	private static HowNetWordManager howNetWordManager = (HowNetWordManager) context.getBean("howNetWordManager");
	private static final int limit_once = 500;
	private static final String fileName = "src/test/resources/敏感词拓展(HowNet).txt";
	private static Logger logger = LoggerFactory.getLogger(SensitiveWordExpandByHowNet.class);

	public static void main(String args[]) {
		int offset = 0;
		List<SensitiveWordData> datas = sensitiveWordManager.search(null, null, offset++ * limit_once, limit_once);
		while (datas != null && datas.size() > 0) {
			for (int i = 0; i < datas.size(); i++) {
				ExpandSensitive(datas.get(i).getWord());
			}
			System.out.println(offset * limit_once);
			datas = sensitiveWordManager.search(null, null, offset++ * limit_once, limit_once);
		}
	}

	private static void ExpandSensitive(String text) {
		List<Term> textSegment = HanLP.segment(text);
		List<String> words = new ArrayList<>();
		for (Term term : textSegment) {
			if (!StringUtil.isEmpty(term.getWord())) {
				words.add(term.getWord());
			}
		}
		List<String> expands = new ArrayList<>();
		for (int i = 1, l = words.size(); i <= l; i++) {
			for (int j = 0; j < i; j++) {
				StringBuffer sBuffer = new StringBuffer();
				for (int k = j; k < i; k++) {
					String current = words.get(k);
					sBuffer.append(current);
					if (!expands.contains(sBuffer.toString()) && sBuffer.length() >= 2) {
						expands.add(sBuffer.toString());
					}
				}
			}
		}
		for (String string : expands) {
			Expand(text, string);
		}
	}

	private static void Expand(String originWord, String word) {
		if (StringUtil.isEmpty(word)) {
			return;
		}
		List<HowNetData> datas = howNetWordManager.search(null, word, null, null, word, null, null, word, 0,
				Integer.MAX_VALUE);
		Set<String> expandExp = new HashSet<>();
		for (HowNetData data : datas) {
			List<String> words = new ArrayList<>();
			words.add(data.getW_C());
			String[] defs = data.getDEF();
			if (defs != null && defs.length > 0) {
				for (String def : defs) {
					words.add(def);
				}
			}
			for (String expandWord : words) {
				if (!expandExp.contains(expandWord) && howNetWordManager.exist(expandWord)) {
					expandExp.add(expandWord);
				}
			}
		}
		for (String expand : expandExp) {
			if (!sensitiveWordManager.exist(expand)) {
				String msg = originWord + "/" + word + "---拓展---" + expand + "\n";
				System.out.print(msg);
				output_to_file(msg, fileName);
				sensitiveWordManager.insert(expand, SensitiveNatureLevel.secondaryExpansion.level());
			}
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
			}
		}
	}
}