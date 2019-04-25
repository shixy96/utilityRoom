package course.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import course.dal.bean.TxtCollectionData;
import course.dal.common.DalBaseTemplate;

@Component
public class TxtCollectionDal extends DalBaseTemplate {
	public void insert(String content, Boolean isSensitive) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_content", content);
		parameters.put("in_is_sensitive", isSensitive);
		Execute("txt_collection_insert", parameters);
	}

	public List<TxtCollectionData> search(String content, Boolean isSensitive, int offset, int limit) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_content", content);
		parameters.put("in_is_sensitive", isSensitive);
		parameters.put("in_offset", offset);
		parameters.put("in_count", limit);
		return ExecuteReturnList("txt_collection_search", parameters, new TxtMap());
	}

	public void delete(String content) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_content", content);
		Execute("txt_collection_delete", parameters);
	}
}

class TxtMap implements RowMapper<TxtCollectionData> {
	public TxtCollectionData mapRow(ResultSet rs, int rowNum) throws SQLException {
		TxtCollectionData txtCollectionData = new TxtCollectionData();
		txtCollectionData.setId(rs.getInt("id"));
		txtCollectionData.setContent(rs.getString("content"));
		txtCollectionData.setIsSensitiveByInteger(rs.getInt("is_sensitive"));
		return txtCollectionData;
	}
}
