package course.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;

import course.dal.bean.SematicParams;
import course.dal.bean.SensitiveWordData;
import course.tool.AuxiliaryTool;

public class SensitiveVecManager {
	private static SematicParams sematicParams = new SematicParams(0.8, 0.0, 0.2, 0.7, 0.5, 0.0, 0.0, 0.0);

	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context
			.getBean("sensitiveWordManager");

	private static int max_word = 21;
	private static boolean useNearest = false;
	private static boolean useTranslate = true;
	private static double nearest_rate = .95;
	private static SensitiveVecManager sensitiveVecManager;

	public static SensitiveVecManager instance() {
		if (sensitiveVecManager == null) {
			sensitiveVecManager = new SensitiveVecManager();
		}
		return sensitiveVecManager;
	}

	public String mark(String txt) {
		CoNLLSentence sentence = AuxiliaryTool.instance().parseDependency(txt);
		for (CoNLLWord coNLLWord : sentence.word) {
			List<String> wordList = new ArrayList<String>();
			wordList.add(coNLLWord.LEMMA);
			if (useNearest) {
				for (Map.Entry<String, Float> entry : AuxiliaryTool.instance().nearest(coNLLWord.LEMMA, 3)) {
					if (entry.getValue() > nearest_rate) {
						wordList.add(entry.getKey());
					}
				}
			}
			for (String word : wordList) {
				List<SensitiveWordData> sendata = sensitiveWordManager.search(word, null, 0, 1);
				if (sendata != null && sendata.size() > 0 && (coNLLWord.SENSITIVE_LEVEL == null
						|| sendata.get(0).getLevel() > coNLLWord.SENSITIVE_LEVEL)) {
					coNLLWord.SENSITIVE_LEVEL = sendata.get(0).getLevel();
				} else {
					coNLLWord.SENSITIVE_LEVEL = 0.0;
				}
			}
		}
		int index = 0, wordNum = sentence.word.length;
		if (useTranslate) {
			SensitiveTranslate(sentence);
		}
		StringBuffer sBuffer = new StringBuffer();
		while (index < wordNum) {
			sBuffer.append(index + 1 + ":" + sentence.word[index].SENSITIVE_LEVEL);
			sBuffer.append(" ");
			index++;
		}
		while (index < max_word) {
			sBuffer.append(index + 1 + ":" + 0.0);
			sBuffer.append(" ");
			index++;
		}
		return sBuffer.toString();
	}

	private void SensitiveTranslate(CoNLLSentence sentence) {
		for (CoNLLWord coNLLWord : sentence) {
			SensitiveLevelMark(sentence, coNLLWord);
		}
	}

	private void SensitiveLevelMark(CoNLLSentence sentence, CoNLLWord word) {
		List<CoNLLWord> list = sentence.findChildren(word);
		if (list.isEmpty()) {
			return;
		}
		for (CoNLLWord child : list) {
			SensitiveLevelMark(sentence, child);
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
