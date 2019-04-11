package course.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import course.dal.common.DalBaseTemplate;

@Component
public class SensitiveWordDal extends DalBaseTemplate {
	public void insert(String content) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_content", content);
		Execute("sensitive_word_insert", parameters);
	}

	public List<String> search(String content) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_content", content);
		return ExecuteReturnList("sensitive_word_search", parameters, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("content");
			}
		});
	}

	public void delete(String content) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_content", content);
		Execute("sensitive_word_delete", parameters);
	}
}
