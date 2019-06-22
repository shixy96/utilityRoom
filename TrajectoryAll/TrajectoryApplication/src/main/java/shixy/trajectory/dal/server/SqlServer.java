package shixy.trajectory.dal.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import shixy.trajectory.bean.HotSpot;
import shixy.trajectory.bean.TrajectoryData;
import shixy.trajectory.dal.GlobalDal;
import shixy.trajectory.dal.TrajectoryDal;

@Component
public class SqlServer {
	@Autowired
	private TrajectoryDal trajectoryDal;
	@Autowired
	private GlobalDal globalDal;
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	public void print(String n) {
		System.out.println(n + df.format(new java.util.Date()));
	}

	public List<TrajectoryData> searchTrajectoryData(Integer tableNum, Double maxLat, Double maxLng, Double minLat,
			Double minLng) {
		List<TrajectoryData> trajectoryDatas = trajectoryDal.getListByLocation(tableNum, maxLat, maxLng, minLat,
				minLng);
		if (CollectionUtils.isEmpty(trajectoryDatas)) {
			TrajectoryData trajectoryData = trajectoryDal.getFirst(tableNum);
			trajectoryDatas.add(trajectoryData);
		}
		return trajectoryDatas;
	}

	public List<HotSpot> searchTrajectoryDatas(List<Integer> tableNums, Double maxLat, Double maxLng, Double minLat,
			Double minLng) {
//		System.out.println(tableNums.size());
		List<HotSpot> hotSpots = new ArrayList<>();
		Map<String, HotSpot> kMap = new HashMap<>();
		double diet = 1 / Math.abs(maxLng - minLng) * 10;
		System.out.println("diet: " + diet);
		for (int index : tableNums) {
//			System.out.println(index + "查询开始" + df.format(new java.util.Date()));
			List<TrajectoryData> trajectoryDatas = trajectoryDal.getListByLocation(index, maxLat, maxLng, minLat,
					minLng);
//			System.out.println("数据库查询结束");
			for (TrajectoryData trajectoryData : trajectoryDatas) {
				String key = "" + trajectoryData.getLat() + "," + trajectoryData.getLng();
				if (kMap.containsKey(key)) {
					kMap.get(key).numAdd();
				} else {
					kMap.put(key, new HotSpot(Math.floor(trajectoryData.getLat() * diet) / diet,
							Math.floor(trajectoryData.getLng() * diet) / diet, 1));
				}
			}
		}
		for (HotSpot hotSpot : kMap.values()) {
			hotSpots.add(hotSpot);
		}
//		System.out.println("查询结束，共" + hotSpots.size() + " " + df.format(new java.util.Date()));
		return hotSpots;
	}

	public Date findMaxTime(int tableNum) {
		return trajectoryDal.getMaxTime(tableNum);
	}

	public Date findMinTime(int tableNum) {
		return trajectoryDal.getMinTime(tableNum);
	}

	public int getTimeNum(int tableNum, int time) {
		int result = trajectoryDal.getNumByTime(tableNum, time);
		System.err.println(tableNum + " " + time + ": " + result);
		return result;
	}

	public int getTableNumber() {
		int tableNum = globalDal.getAllTableNum();
		return tableNum;
	}

	public List<Integer> listMember() {
		List<Integer> memberList = globalDal.listMember();
		return memberList;
	}

	public void insert(Integer num, String tableName) {
		globalDal.insert(num, tableName);
	}
}