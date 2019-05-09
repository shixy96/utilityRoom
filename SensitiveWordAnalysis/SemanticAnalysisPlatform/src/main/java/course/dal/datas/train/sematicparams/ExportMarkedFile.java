package course.dal.datas.train.sematicparams;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;

import course.bll.TxtCollectionManager;
import course.dal.bean.SematicParams;
import course.dal.bean.TxtCollectionData;
import course.util.IOUtil;
import course.util.TimeUtil;

public class ExportMarkedFile {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static TxtCollectionManager txtCollectionManager = (TxtCollectionManager) context
			.getBean("txtCollectionManager");

	public static final String fileName = "src/test/resources/FindBetterParams/AnalysisSematic";
	private static final int limit_once = 1000;
	private static final int max_word = 21;

	public static void markExport(SematicParams sematicParams) {
		final long timeStart = System.currentTimeMillis();
		int offset = 0;
		int maxNum = txtCollectionManager.getAllNum();
		List<TxtCollectionData> datas = txtCollectionManager.search(null, null, offset, limit_once);
		while (datas != null && datas.size() > 0) {
			for (int i = 0; i < datas.size(); i++) {
				offset++;
				StringBuffer sBuffer = new StringBuffer(datas.get(i).getIsSensitive() ? "1" : "0");
				CoNLLSentence sentence = HanLP.parseDependency(datas.get(i).getContent());
				for (CoNLLWord coNLLWord : sentence.word) {
					if (coNLLWord.CPOSTAG.equals("sen1")) {
						coNLLWord.SENSITIVE_LEVEL = 1.0;
					} else if (coNLLWord.CPOSTAG.equals("sen2")) {
						coNLLWord.SENSITIVE_LEVEL = 2.0;
					} else if (coNLLWord.CPOSTAG.equals("sen3")) {
						coNLLWord.SENSITIVE_LEVEL = 3.0;
					} else {
						coNLLWord.SENSITIVE_LEVEL = 0.0;
					}
				}
				int index = 0, wordNum = sentence.word.length;
				SensitiveTranslate(sentence, sematicParams);
				while (index < wordNum) {
					sBuffer.append(" ");
					sBuffer.append(index + 1 + ":" + sentence.word[index].SENSITIVE_LEVEL);
					index++;
				}
				while (index < max_word) {
					sBuffer.append(" ");
					sBuffer.append(index + 1 + ":" + 0);
					index++;
				}
				sBuffer.append("\n");
				if (index == max_word) {
					IOUtil.output_to_file(sBuffer.toString(), fileName, offset != 1);
				}
			}
			long timeNow = System.currentTimeMillis();
			long costTime = timeNow - timeStart + 1;
			String etd = TimeUtil.humanTime((long) (costTime * 1.0 / offset * (maxNum - offset)));
			System.out.printf("\r生成训练数据：%.4f%%", offset * 1.0 / maxNum);
			if (etd.length() > 0)
				System.out.printf("\t剩余时间：%s", etd);
			System.out.flush();
			datas = txtCollectionManager.search(null, null, offset, limit_once);
		}
	}

	private static void SensitiveTranslate(CoNLLSentence sentence, SematicParams sematicParams) {
		for (CoNLLWord coNLLWord : sentence) {
			SensitiveLevelMark(sentence, coNLLWord, sematicParams);
		}
	}

	private static void SensitiveLevelMark(CoNLLSentence sentence, CoNLLWord word, SematicParams sematicParams) {
		List<CoNLLWord> list = sentence.findChildren(word);
		if (list.isEmpty()) {
			return;
		}
		for (CoNLLWord child : list) {
			SensitiveLevelMark(sentence, child, sematicParams);
			if (child.DEPREL.equals("左附加关系") || child.DEPREL.equals("右附加关系")) {
				word.SENSITIVE_LEVEL += child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("并列关系")) {
				if (child.SENSITIVE_LEVEL > word.SENSITIVE_LEVEL) {
					word.SENSITIVE_LEVEL = child.SENSITIVE_LEVEL;
				}
			} else if (child.DEPREL.equals("主谓关系")) {
				word.SENSITIVE_LEVEL += sematicParams.getS_SBV() * child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("动宾关系")) {
				word.SENSITIVE_LEVEL += sematicParams.getS_VOB() * child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("间宾关系") || child.DEPREL.equals("间接宾语")) {
				word.SENSITIVE_LEVEL += sematicParams.getS_IOB() * child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("前置宾语")) {
				word.SENSITIVE_LEVEL += sematicParams.getS_FOB() * child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("兼语")) {
				word.SENSITIVE_LEVEL += sematicParams.getS_DBL() * child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("定中关系")) {
				word.SENSITIVE_LEVEL += sematicParams.getS_ATT() * child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("状中结构")) {
				word.SENSITIVE_LEVEL += sematicParams.getS_ADV() * child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("动补结构")) {
				word.SENSITIVE_LEVEL += sematicParams.getS_CMP() * child.SENSITIVE_LEVEL;
			}
		}
	}

}
