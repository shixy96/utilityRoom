package course.bll;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import course.dal.SensitiveWordDal;

@Component
public class SensitiveWordManager {
	@Autowired
	private SensitiveWordDal sensitiveWordDal;
	private Logger logger = LoggerFactory.getLogger(SensitiveWordManager.class);

	public void insert(String content) {
		try {
			sensitiveWordDal.insert(content);
		} catch (Exception e) {
			logger.error("SensitiveWordManager insert error", e);
		}
	}

	public List<String> search(String content) {
		List<String> result = new ArrayList<String>();
		try {
			result = sensitiveWordDal.search(content);
		} catch (Exception e) {
			logger.error("SensitiveWordManager search error", e);
		}
		return result;
	}

	public boolean exist(String content) {
		List<String> result = search(content);
		if (result.size() != 0) {
			return true;
		}
		return false;
	}

	public void delete(String content) {
		try {
			sensitiveWordDal.delete(content);
		} catch (Exception e) {
			logger.error("SensitiveWordManager delete error", e);
		}
	}

}
