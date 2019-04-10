package course.dal.bean;

import java.util.List;

public class WeiboResponseData {
	List<WeiboCommentData> data;
	Integer max;
	String max_id;
	String max_id_type;
	Object status;
	String total_number;

	public List<WeiboCommentData> getData() {
		return data;
	}

	public void setData(List<WeiboCommentData> data) {
		this.data = data;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String getMax_id() {
		return max_id;
	}

	public void setMax_id(String max_id) {
		this.max_id = max_id;
	}

	public String getMax_id_type() {
		return max_id_type;
	}

	public void setMax_id_type(String max_id_type) {
		this.max_id_type = max_id_type;
	}

	public Object getStatus() {
		return status;
	}

	public void setStatus(Object status) {
		this.status = status;
	}

	public String getTotal_number() {
		return total_number;
	}

	public void setTotal_number(String total_number) {
		this.total_number = total_number;
	}
}
