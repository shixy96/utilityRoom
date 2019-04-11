package course.dal.bean;

public class SpiderTextData {
	private Integer id;
	private String text;
	private Integer isSensitive;
	private Integer segmentNum;
	private Double sensitiveLevel;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getSegmentNum() {
		return segmentNum;
	}

	public void setSegmentNum(Integer segmentNum) {
		this.segmentNum = segmentNum;
	}

	public Double getSensitiveLevel() {
		return sensitiveLevel;
	}

	public void setSensitiveLevel(Double sensitiveLevel) {
		this.sensitiveLevel = sensitiveLevel;
	}

	public Integer getIsSensitive() {
		return isSensitive;
	}

	public void setIsSensitive(Integer isSensitive) {
		this.isSensitive = isSensitive;
	}
}
