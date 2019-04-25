package course.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import course.dal.bean.HowNetData;
import course.dal.common.DalBaseTemplate;
import course.util.StringUtil;

@Component
public class HowNetWordDal extends DalBaseTemplate {
	public void insert(String W_C, String G_C, String S_C, String W_E, String G_E, String S_E, String DEF) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_W_C", W_C);
		parameters.put("in_G_C", G_C);
		parameters.put("in_S_C", S_C);
		parameters.put("in_W_E", W_E);
		parameters.put("in_G_E", G_E);
		parameters.put("in_S_E", S_E);
		parameters.put("in_DEF", DEF);
		Execute("how_net_word_insert", parameters);
	}

	public List<HowNetData> search(Integer id, String W_C, String G_C, String S_C, String W_E, String G_E, String S_E,
			String DEF, int offset, int limit) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_id", id);
		parameters.put("in_W_C", StringUtil.replaceWildcard(W_C));
		parameters.put("in_G_C", StringUtil.replaceWildcard(G_C));
		parameters.put("in_S_C", StringUtil.replaceWildcard(S_C));
		parameters.put("in_W_E", StringUtil.replaceWildcard(W_E));
		parameters.put("in_G_E", StringUtil.replaceWildcard(G_E));
		parameters.put("in_S_E", StringUtil.replaceWildcard(S_E));
		parameters.put("in_DEF", StringUtil.replaceWildcard(DEF));
		parameters.put("in_offset", offset);
		parameters.put("in_count", limit);
		return ExecuteReturnList("how_net_word_search", parameters, new HowNetMap());
	}

	public List<HowNetData> searchContent(String W_C, String W_E, int offset, int limit) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_W_C", StringUtil.replaceWildcard(W_C));
		parameters.put("in_W_E", StringUtil.replaceWildcard(W_E));
		parameters.put("in_offset", offset);
		parameters.put("in_count", limit);
		return ExecuteReturnList("how_net_word_content_search", parameters, new HowNetMap());
	}

}

class HowNetMap implements RowMapper<HowNetData> {
	public HowNetData mapRow(ResultSet rs, int rowNum) throws SQLException {
		HowNetData howNetData = new HowNetData();
		howNetData.setNO(rs.getInt("id"));
		howNetData.setW_C(rs.getString("W_C"));
		howNetData.setG_C(rs.getString("G_C"));
		howNetData.setS_C(rs.getString("S_C"));
		howNetData.setW_E(rs.getString("W_E"));
		howNetData.setG_E(rs.getString("G_E"));
		howNetData.setS_E(rs.getString("S_E"));
		howNetData.setDEF(rs.getString("DEF"));
		return howNetData;
	}
}
