package shixy.trajectory.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shixy.trajectory.bean.TrajectoryEdge;
import shixy.trajectory.dal.TrajectoryEdgeDal;

@Component
public class TrajectoryEdgeManage {
	@Autowired
	private TrajectoryEdgeDal trajectoryEdgeDal;

	public void insert(int tableNum, Double maxLat, Double maxLng, Double minLat, Double minLng) {
		try {
			trajectoryEdgeDal.insert(tableNum, maxLat, maxLng, minLat, minLng);
		} catch (Exception e) {
			System.err.println("TrajectoryEdgeManage insert error");
			System.err.println(e.getMessage());
		}
	}

	public TrajectoryEdge search(int tableName) {
		TrajectoryEdge result = null;
		try {
			result = trajectoryEdgeDal.search(tableName);
		} catch (Exception e) {
			System.err.println("TrajectoryEdgeManage search error");
			System.err.println(e.getMessage());
		}
		return result;
	}
}
