package shixy.trajectory.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import shixy.trajectory.bean.TrajectoryEdge;
import shixy.trajectory.dal.common.DalBaseTemplate;

@Component
public class TrajectoryEdgeDal extends DalBaseTemplate {
	public void insert(int tableNum, Double maxLat, Double maxLng, Double minLat, Double minLng) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_name", tableNum);
		parameters.put("in_mat_lat", maxLat);
		parameters.put("in_max_lng", maxLng);
		parameters.put("in_min_lat", minLat);
		parameters.put("in_min_lng", minLng);
		Execute("trajectory_edge_insert", parameters);
	}

	public TrajectoryEdge search(int tableName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_name", tableName);
		return (TrajectoryEdge) ExecuteReturnObject("trajectory_edge_search", parameters, new TrajectoryEdgeMapper());
	}
}

class TrajectoryEdgeMapper implements RowMapper<TrajectoryEdge> {
	public TrajectoryEdge mapRow(ResultSet rs, int rowNum) throws SQLException {
		TrajectoryEdge trajectoryEdge = new TrajectoryEdge();
		trajectoryEdge.setMaxLat(rs.getDouble("max_lat"));
		trajectoryEdge.setMaxLng(rs.getDouble("max_lng"));
		trajectoryEdge.setMinLat(rs.getDouble("min_lat"));
		trajectoryEdge.setMinLng(rs.getDouble("min_lng"));
		return trajectoryEdge;
	}
}