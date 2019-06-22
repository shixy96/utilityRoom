package shixy.trajectory.bean;

import java.util.HashMap;

/**
 * 同一个人的不同时间点的轨迹
* @author sxy  
* @date 2018年5月30日
 */
public class Person {
	private int TimeDis[] = new int[25]; //统计个人出行时间的数组
	private String start; 				 //个人出行最早时间点
	private String end;					 //个人出行最晚时间点
	private HashMap<Integer, TrajectoryData> no2 = new HashMap<>();

	public int getTimeNum(int n) {
		return this.TimeDis[n];
	}

	public void addTimedis(int i) {
		this.TimeDis[i]++;
	}

	public void setTimedis(int i, int n) {
		this.TimeDis[i] = n;
	}

	public String getStart() {
		return this.start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return this.end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public HashMap<Integer, TrajectoryData> getNo2() {
		return this.no2;
	}
}
