package course.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import course.dal.bean.SpiderTextData;
import course.dal.common.DalBaseTemplate;

@Component
public class SpiderTextDal extends DalBaseTemplate {
	public void insert(String text, Integer segmentNum, Double sensitiveLevel) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_text", text);
		parameters.put("in_segment_num", segmentNum);
		parameters.put("in_sensitive_level", sensitiveLevel);
		Execute("spider_text_insert", parameters);
	}

	public void update(Integer id, String text, Integer segmentNum, Double sensitiveLevel) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_id", id);
		parameters.put("in_text", text);
		parameters.put("in_segment_num", segmentNum);
		parameters.put("in_sensitive_level", sensitiveLevel);
		Execute("spider_text_update", parameters);
	}

	public List<SpiderTextData> completeSearch(Integer id, String text, Integer segmentNum, Double sensitiveLevel) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_id", id);
		parameters.put("in_text", text);
		parameters.put("in_segment_num", segmentNum);
		parameters.put("in_sensitive_level", sensitiveLevel);
		return ExecuteReturnList("spider_text_complete_search", parameters, new SpiderTextDataMapper());
	}

	public void delete(Integer id) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_id", id);
		Execute("spider_text_delete", parameters);
	}
}

class SpiderTextDataMapper implements RowMapper<SpiderTextData> {
	public SpiderTextData mapRow(ResultSet rs, int rowNum) throws SQLException {
		SpiderTextData spiderTextData = new SpiderTextData();
		spiderTextData.setId(rs.getInt("id"));
		spiderTextData.setText(rs.getString("text"));
		spiderTextData.setSegmentNum(rs.getInt("segment_num"));
		spiderTextData.setSensitiveLevel(rs.getDouble("sensitive_level"));
		return spiderTextData;
	}
}