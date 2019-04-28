package course.bll;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import course.dal.HowNetWordDal;
import course.dal.bean.HowNetData;

@Component
public class HowNetWordManager {
	@Autowired
	private HowNetWordDal howNetWordDal;
	private Logger logger = LoggerFactory.getLogger(HowNetWordManager.class);

	public void insert(String W_C, String G_C, String S_C, String W_E, String G_E, String S_E, String DEF) {
		try {
			howNetWordDal.insert(W_C, G_C, S_C, W_E, G_E, S_E, DEF);
			;
		} catch (Exception e) {
			logger.error("HowNetWordManager insert error", e);
		}
	}

	public List<HowNetData> search(Integer id, String W_C, String G_C, String S_C, String W_E, String G_E, String S_E,
			String DEF, int offset, int limit) {
		List<HowNetData> result = new ArrayList<HowNetData>();
		try {
			result = howNetWordDal.search(id, W_C, G_C, S_C, W_E, G_E, S_E, DEF, offset, limit);
		} catch (Exception e) {
			logger.error("HowNetWordManager search error", e);
		}
		return result;
	}

	public List<HowNetData> searchComplete(Integer id, String W_C, String G_C, String S_C, String W_E, String G_E,
			String S_E, String DEF, int offset, int limit) {
		List<HowNetData> result = new ArrayList<HowNetData>();
		try {
			result = howNetWordDal.searchComplete(id, W_C, G_C, S_C, W_E, G_E, S_E, DEF, offset, limit);
		} catch (Exception e) {
			logger.error("HowNetWordManager search error", e);
		}
		return result;
	}

	public boolean searchContentLikeExit(String W_C) {
		List<HowNetData> result = new ArrayList<HowNetData>();
		try {
			result = howNetWordDal.searchContentLikeExit(W_C, W_C, 0, 1);
		} catch (Exception e) {
			logger.error("HowNetWordManager searchContent error", e);
		}
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}

}
