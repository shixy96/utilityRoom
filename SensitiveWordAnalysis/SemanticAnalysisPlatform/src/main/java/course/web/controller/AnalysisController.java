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

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;

import course.bll.SematicAnalysisManager;
import course.bll.SensitiveVecManager;
import course.bll.TxtCollectionManager;
import course.tool.AuxiliaryTool;
import course.web.common.GenericJsonResult;
import course.web.common.HResult;

@Controller
@RequestMapping(value = "/analysis/")
public class AnalysisController {
	private AuxiliaryTool auxiliaryTool = AuxiliaryTool.instance();
	private SematicAnalysisManager sematicAnalysisManager = SematicAnalysisManager.instance();
	private SensitiveVecManager sensitiveVecManager = SensitiveVecManager.instance();
	@Autowired
	private TxtCollectionManager txtCollectionManager;

	@RequestMapping(value = "page")
	public ModelAndView analysisPage(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		return new ModelAndView("hello", "model", map);
	}

	@RequestMapping(value = "getText", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<String> getTxt(HttpServletRequest request, HttpServletResponse response) {
		GenericJsonResult<String> result = new GenericJsonResult<String>(HResult.S_OK);
		int index = (int) Math.floor(Math.random() * 20400);
		result.setData(txtCollectionManager.search(null, null, index, 1).get(0).getContent());
		return result;
	}

	@RequestMapping(value = "getTextSegmentResult", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<List<Term>> segment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "text") String text) {
		GenericJsonResult<List<Term>> result = new GenericJsonResult<List<Term>>(HResult.S_OK);
		List<Term> textSegment = auxiliaryTool.segment(text);
		result.setData(textSegment);
		return result;
	}

	@RequestMapping(value = "getTextNearestResult", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<List<NearestUIData>> nearest(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "text") String text) throws IOException {
		GenericJsonResult<List<NearestUIData>> result = new GenericJsonResult<List<NearestUIData>>(HResult.S_OK);
		List<NearestUIData> nearestUIData = new ArrayList<NearestUIData>();
		List<Term> textSegment = auxiliaryTool.segment(text);
		for (Term term : textSegment) {
			Map<String, Float> wordResult = new HashMap<String, Float>();
			for (Map.Entry<String, Float> entry : auxiliaryTool.nearest(term.getWord(), 3)) {
				wordResult.put(entry.getKey(), entry.getValue());
			}
			nearestUIData.add(new NearestUIData(term.getWord(), wordResult));
		}
		result.setData(nearestUIData);
		return result;
	}

	@RequestMapping(value = "getTextDependenciesResult", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<CoNLLSentenceUIData> dependency(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "text") String text) throws IOException {
		GenericJsonResult<CoNLLSentenceUIData> result = new GenericJsonResult<CoNLLSentenceUIData>(HResult.S_OK);
		CoNLLSentence sentence = auxiliaryTool.parseDependency(text);
		result.setData(new CoNLLSentenceUIData(sentence));
		return result;
	}

	@RequestMapping(value = "getTextAnalysisResult", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<Boolean> analysis(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "text") String text) throws IOException {
		GenericJsonResult<Boolean> result = new GenericJsonResult<Boolean>(HResult.S_OK);
		String sematicVec = sensitiveVecManager.mark(text);
		boolean res = sematicAnalysisManager.predict(sematicVec);
		result.setData(res);
		return result;
	}
}
