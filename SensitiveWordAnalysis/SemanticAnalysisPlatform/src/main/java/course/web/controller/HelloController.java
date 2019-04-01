package course.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.seg.common.Term;

import course.web.common.GenericJsonResult;
import course.web.common.HResult;

@Controller
@RequestMapping(value = "/hello/")
public class HelloController {
	private static final String MODEL_FILE_NAME = "D:/CS224n_NLP/data/test/word2vec.txt";
	private static WordVectorModel wordVectorModel;
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
		List<Term> termList = HanLP.segment("今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。");
		map.put("termList", termList);
		return new ModelAndView("hello", "model", map);
	}

	@RequestMapping(value = "segment", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<List<Term>> segment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "text") String text) {
		GenericJsonResult<List<Term>> result = new GenericJsonResult<List<Term>>(HResult.S_OK);
		List<Term> textSegment = HanLP.segment(text);
		result.setData(textSegment);
		return result;
	}

	@RequestMapping(value = "nearest", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<Map<String, Float>> word2vec(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "word") String word) throws IOException {
		GenericJsonResult<Map<String, Float>> result = new GenericJsonResult<Map<String, Float>>(HResult.S_OK);
		Map<String, Float> wordResult = new HashMap<String, Float>();
		for (Map.Entry<String, Float> entry : wordVectorModel.nearest(word)) {
			wordResult.put(entry.getKey(), entry.getValue());
		}
		result.setData(wordResult);
		return result;
	}

}
