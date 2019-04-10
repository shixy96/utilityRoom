package course.dal.bean;

public class WeiboResponse {
	WeiboResponseData data;
	Integer ok;

	public WeiboResponseData getData() {
		return data;
	}

	public void setData(WeiboResponseData data) {
		this.data = data;
	}

	public Integer getOk() {
		return ok;
	}

	public void setOk(Integer ok) {
		this.ok = ok;
	}
}
