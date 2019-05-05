package course.dal.bean;

public class TxtCollectionData {
	private Integer id;
	private String content;
	private Boolean isSensitive;
	private String vectors;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getIsSensitive() {
		return isSensitive;
	}

	public void setIsSensitive(Boolean isSensitive) {
		this.isSensitive = isSensitive;
	}

	public void setIsSensitiveByInteger(Integer isSensitive) {
		this.isSensitive = isSensitive == null ? null : isSensitive == 0 ? false : true;
	}

	public String getVectors() {
		return vectors;
	}

	public void setVectors(String vectors) {
		this.vectors = vectors;
	}

}
