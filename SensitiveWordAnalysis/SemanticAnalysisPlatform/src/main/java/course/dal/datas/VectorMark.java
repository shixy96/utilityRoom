package course.dal.datas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import course.bll.SensitiveWordManager;
import course.bll.TxtCollectionManager;
import course.dal.bean.SensitiveWordData;
import course.dal.bean.TxtCollectionData;

public class VectorMark {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context
			.getBean("sensitiveWordManager");
	private static TxtCollectionManager txtCollectionManager = (TxtCollectionManager) context
			.getBean("txtCollectionManager");
	private static final String fileName = "src/test/resources/transformSem2/AnalysisTransformSemantic2";

	private static final int limit_once = 500;
	private static final int max_word = 21;
	private static final double min_rate = 0.05;
	private static final double max_rate = 0.85;
	private static final double[] nearest_rates = { .55, .65, .75, .85, .95 };
	private static final boolean useNearest = true;
	private static WordVectorModel wordVectorModel;
	private static final String MODEL_FILE_NAME = "I:/CS224n_NLP/data/test/word2vec.txt";
	private static Logger logger = LoggerFactory.getLogger(SenstiveWordInit.class);

	static {
		try {
			wordVectorModel = new WordVectorModel(MODEL_FILE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		for (int i = 0; i < nearest_rates.length; i++) {
			System.out.println("nearest_rates: " + nearest_rates[i]);
			mark(i);
		}
	}

	private static void mark(int nearest_rate_index) {
		int maxWordNum = 0, offset = 0;
		String maxWordTxt = "";
		int allNum = txtCollectionManager.getAllNum();
		List<TxtCollectionData> datas = txtCollectionManager.search(null, null, offset, limit_once);
		while (datas != null && datas.size() > 0) {
			for (int i = 0; i < datas.size(); i++) {
				offset++;
				StringBuffer sBuffer = new StringBuffer(datas.get(i).getIsSensitive() ? "1" : "0");
				CoNLLSentence sentence = HanLP.parseDependency(datas.get(i).getContent());
				for (CoNLLWord coNLLWord : sentence.word) {
					List<String> wordList = new ArrayList<String>();
					wordList.add(coNLLWord.LEMMA);
					if (useNearest) {
						for (Map.Entry<String, Float> entry : wordVectorModel.nearest(coNLLWord.LEMMA, 3)) {
							if (entry.getValue() > nearest_rates[nearest_rate_index]) {
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
						// if (coNLLWord.CPOSTAG.equals("sen1")) {
						// coNLLWord.SENSITIVE_LEVEL = 1.0;
						// } else if (coNLLWord.CPOSTAG.equals("sen2")) {
						// coNLLWord.SENSITIVE_LEVEL = 2.0;
						// } else if (coNLLWord.CPOSTAG.equals("sen3")) {
						// coNLLWord.SENSITIVE_LEVEL = 3.0;
						// } else {
						// coNLLWord.SENSITIVE_LEVEL = 0.0;
						// }
					}
				}
				int index = 0, wordNum = sentence.word.length;
				if (wordNum > maxWordNum) {
					maxWordNum = wordNum;
					maxWordTxt = datas.get(i).getContent();
				}
				SensitiveTranslate(sentence);
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
					output_to_file(sBuffer.toString(), fileName + nearest_rates[nearest_rate_index]
							+ (offset <= allNum * max_rate && offset >= allNum * min_rate ? "Train.txt" : "Test.txt"));
				}
			}
			System.out.print(offset);
//			System.out.print("\t" + "最大词数: " + maxWordNum + "\t" + maxWordTxt);
			System.out.print("\n");
			datas = txtCollectionManager.search(null, null, offset, limit_once);
		}
	}

	private static void SensitiveTranslate(CoNLLSentence sentence) {
		for (CoNLLWord coNLLWord : sentence) {
//			SensitiveLevelMarkLu(sentence, coNLLWord);
			SensitiveLevelMark(sentence, coNLLWord);
		}
	}

	private static void SensitiveLevelMarkLu(CoNLLSentence sentence, CoNLLWord word) {
		List<CoNLLWord> list = sentence.findChildren(word);
		if (list.isEmpty()) {
			return;
		}
		for (CoNLLWord child : list) {
			SensitiveLevelMarkLu(sentence, child);
			if (child.DEPREL.equals("定中关系") || child.DEPREL.equals("动宾关系")) {
				if (child.SENSITIVE_LEVEL > word.SENSITIVE_LEVEL) {
					word.SENSITIVE_LEVEL = child.SENSITIVE_LEVEL;
				}
			} else if (child.CPOSTAG.equals("m")) {
				word.SENSITIVE_LEVEL *= child.SENSITIVE_LEVEL > 0 ? child.SENSITIVE_LEVEL : 1;
			} else {
				word.SENSITIVE_LEVEL += child.SENSITIVE_LEVEL * 1.0 / 2;
			}
		}
	}

	private static void SensitiveLevelMark(CoNLLSentence sentence, CoNLLWord word) {
		List<CoNLLWord> list = sentence.findChildren(word);
		if (list.isEmpty()) {
			return;
		}
		for (CoNLLWord child : list) {
			SensitiveLevelMarkLu(sentence, child);
			if (child.DEPREL.equals("左附加关系") || child.DEPREL.equals("右附加关系")) {
				word.SENSITIVE_LEVEL += child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("主谓关系") || child.DEPREL.equals("动宾关系")) {
				word.SENSITIVE_LEVEL += 0.8 * child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("前置宾语") || child.DEPREL.equals("间宾关系") || child.DEPREL.equals("间接宾语")
					|| child.DEPREL.equals("兼语")) {
				word.SENSITIVE_LEVEL += 0.5 * child.SENSITIVE_LEVEL;
			} else if (child.DEPREL.equals("定中关系") || child.DEPREL.equals("并列关系") || child.DEPREL.equals("状中结构")
					|| child.DEPREL.equals("动补结构")) {
				if (child.SENSITIVE_LEVEL > word.SENSITIVE_LEVEL) {
					word.SENSITIVE_LEVEL = child.SENSITIVE_LEVEL;
				}
			} else if (child.CPOSTAG.equals("m")) {
				word.SENSITIVE_LEVEL *= child.SENSITIVE_LEVEL > 0 ? child.SENSITIVE_LEVEL : 1;
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
