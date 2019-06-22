package shixy.trajectory.bean;

public class HotSpot {
	private double lat;
	private double lng;
	private int num;

	public HotSpot(double lat, double lng, int num) {
		this.lat = lat;
		this.lng = lng;
		this.num = num;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void numAdd() {
		this.num++;
	}

	public Double getLat() {
		return this.lat;
	}

	public Double getLng() {
		return this.lng;
	}

	public int getNum() {
		return this.num;
	}

	public void print() {
		System.out.println(this.lat + " " + this.lng + " " + this.num);
	}
}
