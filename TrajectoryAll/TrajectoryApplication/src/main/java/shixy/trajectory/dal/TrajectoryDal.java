package shixy.trajectory.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import shixy.trajectory.bean.TrajectoryData;
import shixy.trajectory.dal.common.DalBaseTemplate;

@Component
public class TrajectoryDal extends DalBaseTemplate {

	public TrajectoryData getFirst(int tableNum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		return (TrajectoryData) ExecuteReturnObject("trajectory_get_first", parameters, new TrajectoryDataMapper());
	}

	public Integer getNumByTime(int tableNum, int time) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		parameters.put("in_time", time);
		return (Integer) ExecuteReturnObject("trajectory_num_get_by_time", parameters, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("num");
			}
		});
	}

	public Integer getNumByLocation(int tableNum, Double maxLat, Double maxLng, Double minLat, Double minLng) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		parameters.put("in_max_lat", maxLat);
		parameters.put("in_max_lng", maxLng);
		parameters.put("in_min_lat", minLat);
		parameters.put("in_min_lng", minLng);
		Map<String, Object> resultMap = Execute("trajectory_num_get_by_location", parameters);
		return (Integer) resultMap.get("out_num");
	}

	public List<TrajectoryData> getListByLocation(int tableNum, Double maxLat, Double maxLng, Double minLat,
			Double minLng) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		parameters.put("in_max_lat", maxLat);
		parameters.put("in_max_lng", maxLng);
		parameters.put("in_min_lat", minLat);
		parameters.put("in_min_lng", minLng);
		return (List<TrajectoryData>) ExecuteReturnList("trajectory_list_get_by_location", parameters,
				new TrajectoryDataMapper());
	}
	
	public List<TrajectoryData> getListLimit(int tableNum, int offset, int limit) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		parameters.put("in_offset", offset);
		parameters.put("in_limit", limit);
		return (List<TrajectoryData>) ExecuteReturnList("trajectory_list_get_limit", parameters,
				new TrajectoryDataMapper());
	}

	public Date getMaxTime(int tableNum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		return (Date) ExecuteReturnObject("trajectory_time_get_max", parameters, new RowMapper<Date>() {
			public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getDate("max(time)");
			}
		});
	}

	public Date getMinTime(int tableNum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		return (Date) ExecuteReturnObject("trajectory_time_get_min", parameters, new RowMapper<Date>() {
			public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getDate("min(time)");
			}
		});
	}

	public Double getMaxLat(int tableNum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		return (Double) ExecuteReturnObject("trajectory_get_max_lat", parameters, new RowMapper<Double>() {
			public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getDouble("max(lat)");
			}
		});
	}

	public Double getMinLat(int tableNum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		return (Double) ExecuteReturnObject("trajectory_get_min_lat", parameters, new RowMapper<Double>() {
			public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getDouble("min(lat)");
			}
		});
	}

	public Double getMaxLng(int tableNum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		return (Double) ExecuteReturnObject("trajectory_get_max_lng", parameters, new RowMapper<Double>() {
			public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getDouble("max(lng)");
			}
		});
	}

	public Double getMinLng(int tableNum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		return (Double) ExecuteReturnObject("trajectory_get_min_lng", parameters, new RowMapper<Double>() {
			public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getDouble("min(lng)");
			}
		});
	}

	public void insert(int tableNum, Integer num, Double lat, Double lng, Double height, Double rel_time, Date time) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		parameters.put("in_num", num);
		parameters.put("in_lat", lat);
		parameters.put("in_lng", lng);
		parameters.put("in_height", height);
		parameters.put("in_rel_time", rel_time);
		parameters.put("in_time", time);
		Execute("trajectory_insert", parameters);
	}

	public void delete(int tableNum, Integer num, Date time) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		parameters.put("in_num", num);
		parameters.put("in_time", time);
		Execute("trajectory_insert", parameters);
	}

	public void update(int tableNum, Integer num, Double lat, Double lng, Double height, Double rel_time, Date time) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("in_table_num", tableNum);
		parameters.put("in_num", num);
		parameters.put("in_lat", lat);
		parameters.put("in_lng", lng);
		parameters.put("in_height", height);
		parameters.put("in_rel_time", rel_time);
		parameters.put("in_time", time);
		Execute("trajectory_update", parameters);
	}

}

class TrajectoryDataMapper implements RowMapper<TrajectoryData> {
	public TrajectoryData mapRow(ResultSet rs, int rowNum) throws SQLException {
		TrajectoryData trajectoryData = new TrajectoryData();
		trajectoryData.setLat(rs.getDouble("lat"));
		trajectoryData.setLng(rs.getDouble("lng"));
		trajectoryData.setHeight(rs.getDouble("height"));
		trajectoryData.setRetday(rs.getDouble("rel_time"));
		trajectoryData.setDaytime(rs.getTime("time"));
		return trajectoryData;
	}
}