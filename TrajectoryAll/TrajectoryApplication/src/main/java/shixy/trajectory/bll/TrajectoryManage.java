package shixy.trajectory.bll;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shixy.trajectory.bean.TrajectoryData;
import shixy.trajectory.dal.TrajectoryDal;

@Component
public class TrajectoryManage {
	@Autowired
	private TrajectoryDal trajectoryDal;

	public TrajectoryData getFirst(int tableNum) {
		TrajectoryData result = null;
		try {
			result = trajectoryDal.getFirst(tableNum);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getFirst error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public Integer getNumByTime(int tableNum, int time) {
		Integer result = null;
		try {
			result = trajectoryDal.getNumByTime(tableNum, time);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getNumByTime error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public Integer getNumByLocation(Integer tableNum, Double maxLat, Double maxLng, Double minLat, Double minLng) {
		Integer result = null;
		try {
			result = trajectoryDal.getNumByLocation(tableNum, maxLat, maxLng, minLat, minLng);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getNumByLocation error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public List<TrajectoryData> getListByLocation(Integer tableNum, Double maxLat, Double maxLng, Double minLat,
			Double minLng) {
		List<TrajectoryData> result = null;
		try {
			result = trajectoryDal.getListByLocation(tableNum, maxLat, maxLng, minLat, minLng);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getListByLocation error");
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	public List<TrajectoryData> getListLimit(int tableNum, int offset, int limit) {
		List<TrajectoryData> result = null;
		try {
			result = trajectoryDal.getListLimit(tableNum, offset, limit);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getListLimit error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public Date getMaxTime(int tableNum) {
		Date result = null;
		try {
			result = trajectoryDal.getMaxTime(tableNum);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getMaxTime error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public Date getMinTime(int tableNum) {
		Date result = null;
		try {
			result = trajectoryDal.getMinTime(tableNum);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getMinTime error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public Double getMaxLat(int tableNum) {
		Double result = null;
		try {
			result = trajectoryDal.getMaxLat(tableNum);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getMaxLat error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public Double getMinLat(int tableNum) {
		Double result = null;
		try {
			result = trajectoryDal.getMinLat(tableNum);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getMinLat error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public Double getMaxLng(int tableNum) {
		Double result = null;
		try {
			result = trajectoryDal.getMaxLng(tableNum);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getMaxLng error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public Double getMinLng(int tableNum) {
		Double result = null;
		try {
			result = trajectoryDal.getMinLng(tableNum);
		} catch (Exception e) {
			System.err.println("TrajectoryManage getMinLng error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public void insert(Integer tableNum, Integer num, Double lat, Double lng, Double height, Double rel_time,
			Date time) {
		try {
			trajectoryDal.insert(tableNum, num, lat, lng, height, rel_time, time);
		} catch (Exception e) {
			System.err.println("GuideManage insert error");
			System.err.println(e.getMessage());
		}
	}

	public void update(Integer tableNum, Integer num, Double lat, Double lng, Double height, Double rel_time,
			Date time) {
		try {
			trajectoryDal.update(tableNum, num, lat, lng, height, rel_time, time);
		} catch (Exception e) {
			System.err.println("GuideManage update error");
			System.err.println(e.getMessage());
		}
	}

	public void delete(Integer tableNum, Integer num, Date time) {
		try {
			trajectoryDal.delete(tableNum, num, time);
		} catch (Exception e) {
			System.err.println("GuideManage delete error");
			System.err.println(e.getMessage());
		}
	}
}
