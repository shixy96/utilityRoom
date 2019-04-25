package course.bll;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import course.dal.SensitiveTxtDal;

@Component
public class SensitiveTxtManager {
	@Autowired
	private SensitiveTxtDal sensitiveTxtDal;
	private Logger logger = LoggerFactory.getLogger(SensitiveTxtManager.class);

	public void insert(String content) {
		try {
			sensitiveTxtDal.insert(content);
		} catch (Exception e) {
			logger.error("SensitiveTxtManager insert error", e);
		}
	}

	public List<String> search(String content, int offset, int limit) {
		List<String> result = new ArrayList<String>();
		try {
			result = sensitiveTxtDal.search(content, offset, limit);
		} catch (Exception e) {
			logger.error("SensitiveTxtManager search error", e);
		}
		return result;
	}

	public boolean exist(String content) {
		List<String> result = search(content, 0, 1);
		if (result.size() != 0) {
			return true;
		}
		return false;
	}

	public void delete(String content) {
		try {
			sensitiveTxtDal.delete(content);
		} catch (Exception e) {
			logger.error("SensitiveTxtManager delete error", e);
		}
	}

}
