package shixy.trajectory.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import shixy.trajectory.dal.common.DalBaseTemplate;

@Component
public class GlobalDal extends DalBaseTemplate {

	public Integer getAllTableNum() {
		Map<String, Object> resultMap = Execute("guide_table_num_get_all", null);
		return (Integer) resultMap.get("out_num");
	}

	public List<Integer> listMember() {
		return ExecuteReturnList("guide_table_list_get_all", null, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("num");
			}
		});
	}

	public void insert(Integer num, String tableName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_num", num);
		parameters.put("in_table_name", tableName);
		Execute("guide_insert", parameters);
	}

	public void delete(Integer num) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_num", num);
		Execute("guide_delete", parameters);
	}

	public void update(Integer num, String tableName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_num", num);
		parameters.put("in_table_name", tableName);
		Execute("guide_update", parameters);
	}
}
