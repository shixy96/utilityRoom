package course.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import course.web.common.GenericJsonResult;
import course.web.common.HResult;

@Controller
@RequestMapping(value = "/hello/")
public class HelloController {
	@RequestMapping(value = "first")
	public ModelAndView first(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		List<Term> termList = HanLP.segment("今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。");
		map.put("termList", termList);
		return new ModelAndView("hello", "model", map);
	}

	@RequestMapping(value = "segment", method = RequestMethod.GET)
	public @ResponseBody GenericJsonResult<List<Term>> segment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "text") String text) {
		GenericJsonResult<List<Term>> result = new GenericJsonResult<>(HResult.S_OK);
		List<Term> textSegment = HanLP.segment(text);
		result.setData(textSegment);
		return result;
	}

}
