package course.tool;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.seg.common.Term;

public class AuxiliaryTool {
	private static final String MODEL_FILE_NAME = "word2vec.txt";
	private static WordVectorModel wordVectorModel;
	private static AuxiliaryTool auxiliaryTool;

	static {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = AuxiliaryTool.class.getClassLoader();
		}
		try {
			String path = loader.getResource(MODEL_FILE_NAME).getPath();
			wordVectorModel = new WordVectorModel(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static AuxiliaryTool instance() {
		if (auxiliaryTool == null) {
			auxiliaryTool = new AuxiliaryTool();
		}
		return auxiliaryTool;
	}

	public List<Term> segment(String text) {
		return HanLP.segment(text);
	}

	public List<Map.Entry<String, Float>> nearest(String key, int size) {
		return wordVectorModel.nearest(key, size);
	}

	public List<Map.Entry<String, Float>> nearest(String key) {
		return wordVectorModel.nearest(key);
	}

	public CoNLLSentence parseDependency(String sentence) {
		return HanLP.parseDependency(sentence);
	}
}
