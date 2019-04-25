package course.bll;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import course.dal.TxtCollectionDal;
import course.dal.bean.TxtCollectionData;

@Component
public class TxtCollectionManager {
	@Autowired
	private TxtCollectionDal txtCollectionDal;
	private Logger logger = LoggerFactory.getLogger(TxtCollectionManager.class);

	public void insert(String content, Boolean isSensitive) {
		try {
			txtCollectionDal.insert(content, isSensitive);
			;
		} catch (Exception e) {
			logger.error("TxtCollectionManager insert error", e);
		}
	}

	public List<TxtCollectionData> search(String content, Boolean isSensitive, int offset, int limit) {
		List<TxtCollectionData> result = new ArrayList<TxtCollectionData>();
		try {
			result = txtCollectionDal.search(content, isSensitive, offset, limit);
		} catch (Exception e) {
			logger.error("TxtCollectionManager search error", e);
		}
		return result;
	}

	public boolean exist(String content) {
		List<TxtCollectionData> result = search(content, null, 0, 1);
		if (result.size() != 0) {
			return true;
		}
		return false;
	}

	public void delete(String content) {
		try {
			txtCollectionDal.delete(content);
		} catch (Exception e) {
			logger.error("TxtCollectionManager delete error", e);
		}
	}

}
