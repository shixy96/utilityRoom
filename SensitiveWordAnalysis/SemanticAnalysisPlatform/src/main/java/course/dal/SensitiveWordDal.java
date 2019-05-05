package course.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import course.dal.bean.SensitiveWordData;
import course.dal.common.DalBaseTemplate;

@Component
public class SensitiveWordDal extends DalBaseTemplate {
	public void insert(String content, Integer natureLevel) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_content", content);
		parameters.put("in_nature_level", natureLevel);
		Execute("sensitive_word_insert", parameters);
	}

	public List<SensitiveWordData> search(String content, Integer natureLevel, int offset, int limit) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_content", content);
		parameters.put("in_nature_level", natureLevel);
		parameters.put("in_offset", offset);
		parameters.put("in_count", limit);
		return ExecuteReturnList("sensitive_word_search", parameters, new SensitiveWordMap());
	}

	public void delete(Integer id, String content, Integer natureLevel) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_id", id);
		parameters.put("in_content", content);
		parameters.put("in_nature_level", natureLevel);
		Execute("sensitive_word_delete", parameters);
	}
}

class SensitiveWordMap implements RowMapper<SensitiveWordData> {
	public SensitiveWordData mapRow(ResultSet rs, int rowNum) throws SQLException {
		SensitiveWordData sensitiveWordData = new SensitiveWordData();
		sensitiveWordData.setId(rs.getInt("id"));
		sensitiveWordData.setWord(rs.getString("content"));
		sensitiveWordData.setLevel(rs.getDouble("nature_level"));
		return sensitiveWordData;
	}
}
