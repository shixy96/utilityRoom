package shixy.trajectory.dal.sqlInit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shixy.trajectory.bean.TrajectoryEdge;
import shixy.trajectory.bll.TrajectoryEdgeManage;
import shixy.trajectory.bll.TrajectoryManage;

@Component
public class MaxNumAreaInit {
	@Autowired
	private TrajectoryManage trajectoryManage;
	@Autowired
	private TrajectoryEdgeManage trajectoryEdgeManage;

	public void initMaxNumAreaEdge() {
		for (int i = 0; i < 182; i++) {
			System.out.println("初始化表" + i);
			TrajectoryEdge trajectoryEdge = findMaxNumEdge(i);
			trajectoryEdgeManage.insert(i, trajectoryEdge.getMaxLat(), trajectoryEdge.getMaxLng(),
					trajectoryEdge.getMinLat(), trajectoryEdge.getMinLng());
		}
	}

	public TrajectoryEdge findMaxNumEdge(int tableNum) {
		TrajectoryEdge trajectoryEdge = new TrajectoryEdge();
		Double maxLat = trajectoryManage.getMaxLat(tableNum);
		Double maxLng = trajectoryManage.getMaxLng(tableNum);
		Double minLat = trajectoryManage.getMinLat(tableNum);
		Double minLng = trajectoryManage.getMinLng(tableNum);
		Double maxLatX = maxLat, maxLngX = maxLng, minLatX = minLat, minLngX = minLng;
		Double tempLat, tempLng;
		int peaceNum1, peaceNum2;
		while ((maxLat - minLat) > 0.4) {
			tempLat = (maxLat + minLat) / 2;
			peaceNum1 = trajectoryManage.getNumByLocation(tableNum, tempLat, maxLngX, minLat, minLngX);
			peaceNum2 = trajectoryManage.getNumByLocation(tableNum, maxLat, maxLngX, tempLat, minLngX);
			if (peaceNum1 > peaceNum2) {
				maxLat = tempLat;
			} else {
				minLat = tempLat;
			}
			tempLng = (maxLng + minLng) / 2;
			peaceNum1 = trajectoryManage.getNumByLocation(tableNum, maxLatX, tempLng, minLatX, minLng);
			peaceNum2 = trajectoryManage.getNumByLocation(tableNum, maxLatX, maxLng, minLatX, tempLng);
			if (peaceNum1 > peaceNum2) {
				maxLng = tempLng;
			} else {
				minLng = tempLng;
			}
		}
		trajectoryEdge.setMaxLat(maxLat);
		trajectoryEdge.setMaxLng(maxLng);
		trajectoryEdge.setMinLat(minLat);
		trajectoryEdge.setMinLng(minLng);
		return trajectoryEdge;
	}
}
