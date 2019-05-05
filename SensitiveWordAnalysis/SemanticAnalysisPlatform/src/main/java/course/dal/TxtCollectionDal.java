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
	public void insert(String content, Boolean isSensitive, String vectors) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_content", content);
		parameters.put("in_is_sensitive", isSensitive);
		parameters.put("in_vectors", vectors);
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

	public Integer getAllNum() {
		return (Integer) ExecuteReturnObject("txt_collection_get_all_num", null, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("count(*)");
			}
		});
	}

	public void update(Integer id, String vectors) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_id", id);
		parameters.put("in_vectors", vectors);
		Execute("txt_collection_update", parameters);
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
