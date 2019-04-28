package course.dal.bean;

public enum SensitiveNatureLevel {
	source(3, "源敏感词"), secondaryExpansion(2, "HOWNET完全拓展"), threeLevelExpansion(1, "HOWNET模糊拓展");
	
	private int level;
	private String desc;
	
	private SensitiveNatureLevel(int id, String desc) {
		this.level = id;
		this.desc = desc;
	}
	
	public int level() {
		return level;
	}
	
	public String desc() {
		return desc;
	}
}