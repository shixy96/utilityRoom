package course.train.sematicparams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import course.bll.SensitiveWordManager;
import course.bll.TxtCollectionManager;
import course.dal.bean.SematicParams;
import course.dal.bean.SensitiveWordData;
import course.dal.bean.TxtCollectionData;
import course.util.IOUtil;
import course.util.TimeUtil;

public class ExportMarkedFile {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context
			.getBean("sensitiveWordManager");
	private static TxtCollectionManager txtCollectionManager = (TxtCollectionManager) context
			.getBean("txtCollectionManager");

	private int limit_once = 1000;
	private int max_word = 21;
	/**
	 * 训练比例
	 */
	public static double train_rate = 0.80;
	/**
	 * 使用词语相关性拓展
	 */
	public static boolean useNearest = false;
	/**
	 * 使用词语按句法敏感性传递
	 */
	public static boolean useTranslate = true;
	/**
	 * 词语相关性比例
	 */
	public static double nearest_rate = 0.95;
	/**
	 * 训练数据保存文件
	 */
	public static String Train_File_Name = "src/test/resources/ExportMarkedFileTrain.txt";
	/**
	 * 测试数据保存文件
	 */
	public static String Test_File_Name = "src/test/resources/ExportMarkedFileText.txt";
	public static String MODEL_FILE_NAME = "I:/CS224n_NLP/data/test/word2vec.txt";
	public static WordVectorModel wordVectorModel;
	public static double[] nearest_rates = { .55, .65, .75, .85, .95 };
	public static boolean useLuMethod = false;
	public static boolean loopFile = false;

	{
		try {
			wordVectorModel = new WordVectorModel(MODEL_FILE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param sematicParams 词语敏感性按句法传递参数
	 * @param nearest_rate_index 词语相关性拓展比例
	 */
	public void markExport(SematicParams sematicParams, int nearest_rate_index) {
		boolean trainStart = false, testStart = false;
		if (!loopFile) {
			trainStart = true;
			testStart = true;
		}
		final long timeStart = System.currentTimeMillis();
		int offset = 0;
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
					}
				}
				int index = 0, wordNum = sentence.word.length;
				if (useTranslate) {
					SensitiveTranslate(sentence, sematicParams);
				}
				while (index < wordNum) {
					sBuffer.append(" ");
					sBuffer.append(index + 1 + ":" + sentence.word[index].SENSITIVE_LEVEL);
					index++;
				}
				while (index < max_word) {
					sBuffer.append(" ");
					sBuffer.append(index + 1 + ":" + 0.0);
					index++;
				}
				sBuffer.append("\n");
				if (index == max_word) {
					if (offset <= allNum * (1 - (1 - train_rate) * train_rate)
							&& offset >= allNum * (1 - train_rate) * (1 - train_rate)) {
						IOUtil.output_to_file(sBuffer.toString(), Train_File_Name, trainStart);
						trainStart = true;
					} else {
						IOUtil.output_to_file(sBuffer.toString(), Test_File_Name, testStart);
						testStart = true;
					}
				}
			}
			long timeNow = System.currentTimeMillis();
			long costTime = timeNow - timeStart + 1;
			String etd = TimeUtil.humanTime((long) (costTime * 1.0 / offset * (allNum - offset)));
			System.out.printf("\r生成训练数据：%.4f%%", offset * 1.0 / allNum * 100);
			if (etd.length() > 0)
				System.out.printf("\t剩余时间：%s", etd);
			System.out.flush();
			datas = txtCollectionManager.search(null, null, offset, limit_once);
		}
	}

	private void SensitiveTranslate(CoNLLSentence sentence, SematicParams sematicParams) {
		for (CoNLLWord coNLLWord : sentence) {
			if (!useLuMethod) {
				SensitiveLevelMark(sentence, coNLLWord, sematicParams);
			} else {
				SensitiveLevelMarkLu(sentence, coNLLWord);
			}
		}
	}

	private void SensitiveLevelMark(CoNLLSentence sentence, CoNLLWord word, SematicParams sematicParams) {
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

	private void SensitiveLevelMarkLu(CoNLLSentence sentence, CoNLLWord word) {
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

	public int getLimit_once() {
		return limit_once;
	}

	public void setLimit_once(int limit_once) {
		this.limit_once = limit_once;
	}

	public int getMax_word() {
		return max_word;
	}

	public void setMax_word(int max_word) {
		this.max_word = max_word;
	}

	public double getNearest_rates(int index) {
		return index >= 0 && index < nearest_rates.length ? nearest_rates[index] : 0;
	}

	public void setNearest_rates(double[] nearest_rates) {
		this.nearest_rates = nearest_rates;
	}

	public boolean isUseNearest() {
		return useNearest;
	}

	public void setUseNearest(boolean useNearest) {
		this.useNearest = useNearest;
	}

	public boolean isUseTranslate() {
		return useTranslate;
	}

	public void setUseTranslate(boolean useTranslate) {
		this.useTranslate = useTranslate;
	}

	public String getMODEL_FILE_NAME() {
		return MODEL_FILE_NAME;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		ExportMarkedFile.context = context;
	}

	public static SensitiveWordManager getSensitiveWordManager() {
		return sensitiveWordManager;
	}

	public static void setSensitiveWordManager(SensitiveWordManager sensitiveWordManager) {
		ExportMarkedFile.sensitiveWordManager = sensitiveWordManager;
	}

	public static TxtCollectionManager getTxtCollectionManager() {
		return txtCollectionManager;
	}

	public static void setTxtCollectionManager(TxtCollectionManager txtCollectionManager) {
		ExportMarkedFile.txtCollectionManager = txtCollectionManager;
	}

	public double getTrain_rate() {
		return train_rate;
	}

	public void setTrain_rate(double train_rate) {
		this.train_rate = train_rate;
	}

	public String getTrain_File_Name() {
		return Train_File_Name;
	}

	public void setTrain_File_Name(String train_File_Name) {
		Train_File_Name = train_File_Name;
	}

	public String getTest_File_Name() {
		return Test_File_Name;
	}

	public void setTest_File_Name(String test_File_Name) {
		Test_File_Name = test_File_Name;
	}

	public WordVectorModel getWordVectorModel() {
		return wordVectorModel;
	}

	public void setWordVectorModel(WordVectorModel wordVectorModel) {
		this.wordVectorModel = wordVectorModel;
	}

	public boolean isLoopFile() {
		return loopFile;
	}

	public void setLoopFile(boolean loopFile) {
		this.loopFile = loopFile;
	}

	public boolean isUseLuMethod() {
		return useLuMethod;
	}

	public void setUseLuMethod(boolean useLuMethod) {
		this.useLuMethod = useLuMethod;
	}

	public double[] getNearest_rates() {
		return nearest_rates;
	}

}
