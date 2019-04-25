package course.bll;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import course.dal.SensitiveWordDal;
import course.dal.bean.SensitiveWordData;

@Component
public class SensitiveWordManager {
	@Autowired
	private SensitiveWordDal sensitiveWordDal;
	private Logger logger = LoggerFactory.getLogger(SensitiveWordManager.class);

	public void insert(String content, Integer natureLevel) {
		try {
			sensitiveWordDal.insert(content, natureLevel);
		} catch (Exception e) {
			logger.error("SensitiveWordManager insert error", e);
		}
	}

	public List<SensitiveWordData> search(String content, Integer natureLevel, int offset, int limit) {
		List<SensitiveWordData> result = new ArrayList<SensitiveWordData>();
		try {
			result = sensitiveWordDal.search(content, natureLevel, offset, limit);
		} catch (Exception e) {
			logger.error("SensitiveWordManager search error", e);
		}
		return result;
	}

	public boolean exist(String content) {
		List<SensitiveWordData> result = search(content, null, 0, 1);
		if (result.size() != 0) {
			return true;
		}
		return false;
	}

	public void delete(Integer id, String content, Integer natureLevel) {
		try {
			sensitiveWordDal.delete(id, content, natureLevel);
		} catch (Exception e) {
			logger.error("SensitiveWordManager delete error", e);
		}
	}

}
