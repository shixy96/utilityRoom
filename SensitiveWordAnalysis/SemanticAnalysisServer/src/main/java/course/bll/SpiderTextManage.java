package course.bll;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import course.dal.SpiderTextDal;
import course.dal.bean.SpiderTextData;

@Component
public class SpiderTextManage {
	@Autowired
	private SpiderTextDal spiderTextDal;
	private Logger logger = LoggerFactory.getLogger(SpiderTextManage.class);

	public void insert(String text, Integer isSensitive, Integer segmentNum, Double sensitiveLevel) {
		try {
			spiderTextDal.insert(text, isSensitive, segmentNum, sensitiveLevel);
		} catch (Exception e) {
			logger.error("SpiderTextManage insert error", e);
		}
	}

	public List<SpiderTextData> search(Integer id, String text, Integer isSensitive, Integer segmentNum,
			Double sensitiveLevel) {
		List<SpiderTextData> result = new ArrayList<SpiderTextData>();
		try {
			result = spiderTextDal.completeSearch(id, text, isSensitive, segmentNum, sensitiveLevel);
		} catch (Exception e) {
			logger.error("SpiderTextManage search error", e);
		}
		return result;
	}

	public boolean exist(String content) {
		List<SpiderTextData> result = search(null, content, null, null, null);
		if (!CollectionUtils.isEmpty(result)) {
			return true;
		}
		return false;
	}

	public void delete(Integer id) {
		try {
			spiderTextDal.delete(id);
		} catch (Exception e) {
			logger.error("SpiderTextManage delete error", e);
		}
	}
}
