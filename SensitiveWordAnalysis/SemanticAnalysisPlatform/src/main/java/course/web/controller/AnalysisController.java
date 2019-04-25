package course.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.seg.common.Term;

import course.bll.SensitiveWordManager;
import course.web.common.GenericJsonResult;
import course.web.common.HResult;

@Controller
@RequestMapping(value = "/hello/")
public class AnalysisController {
	private static final String MODEL_FILE_NAME = "I:/CS224n_NLP/data/test/word2vec.txt";
	private static final float rate = (float) 0.5;
	private static WordVectorModel wordVectorModel;
	@Autowired
	private SensitiveWordManager sensitiveWordManager;

	static {
		try {
			wordVectorModel = new WordVectorModel(MODEL_FILE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "first")
	public ModelAndView first(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		return new ModelAndView("hello", "model", map);
	}

	@RequestMapping(value = "getTextSegmentResult", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<List<Term>> segment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "text") String text) {
		GenericJsonResult<List<Term>> result = new GenericJsonResult<List<Term>>(HResult.S_OK);
		List<Term> textSegment = HanLP.segment(text);
		result.setData(textSegment);
		return result;
	}

	@RequestMapping(value = "getTextNearestResult", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<List<NearestUIData>> nearest(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "text") String text) throws IOException {
		GenericJsonResult<List<NearestUIData>> result = new GenericJsonResult<List<NearestUIData>>(HResult.S_OK);
		List<NearestUIData> nearestUIData = new ArrayList<NearestUIData>();
		List<Term> textSegment = HanLP.segment(text);
		for (Term term : textSegment) {
			Map<String, Float> wordResult = new HashMap<String, Float>();
			for (Map.Entry<String, Float> entry : wordVectorModel.nearest(term.getWord(), 3)) {
				wordResult.put(entry.getKey(), entry.getValue());
			}
			nearestUIData.add(new NearestUIData(term.getWord(), wordResult));
		}
		result.setData(nearestUIData);
		return result;
	}

	@RequestMapping(value = "getTextDependenciesResult", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<CoNLLSentenceUIData> sentence(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "text") String text) throws IOException {
		GenericJsonResult<CoNLLSentenceUIData> result = new GenericJsonResult<CoNLLSentenceUIData>(HResult.S_OK);
		CoNLLSentence sentence = HanLP.parseDependency(text);
		result.setData(new CoNLLSentenceUIData(sentence));
		return result;
	}

	@RequestMapping(value = "getTextAnalysisResult", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<String> textNearest(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "text") String text) throws IOException {
		GenericJsonResult<String> result = new GenericJsonResult<String>(HResult.S_OK);
		String textResult = "";
		List<Term> textSegment = HanLP.segment(text);
		for (Term term : textSegment) {
			boolean sensitiveFlag = sensitiveWordManager.exist(term.word);
			for (Map.Entry<String, Float> entry : wordVectorModel.nearest(term.word)) {
				if (entry.getValue() > rate && sensitiveWordManager.exist(entry.getKey())) {
					sensitiveFlag = true;
				}
				if (sensitiveFlag) {
					break;
				}
			}
			if (sensitiveFlag) {
				textResult += "*";
			} else {
				textResult += term.word;
			}
		}
		result.setData(textResult);
		return result;
	}
}
