package shixy.trajectory.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据类
 * 
 * @author sxy
 * @date 2018年5月30日
 */
public class TrajectoryData {
	private double Lng;
	private double Lat;
	private double Height;
	private double Retime; // 相对时间戳
	private Date Daytime;

	public double getLng() {
		return this.Lng;
	}

	public void setLng(double lng) {
		this.Lng = lng;
	}

	public double getLat() {
		return this.Lat;
	}

	public void setLat(double lat) {
		this.Lat = lat;
	}

	public double getHeight() {
		return this.Height;
	}

	public void setHeight(double height) {
		this.Height = height;
	}

	public String getDaytime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Daytime);
	}
	
	public Date getTime() {
		return Daytime;
	}

	public void setDaytime(Date daytime) {
		this.Daytime = daytime;
	}

	public double getRetday() {
		return this.Retime;
	}

	public void setRetday(double day) {
		this.Retime = day;
	}
}
