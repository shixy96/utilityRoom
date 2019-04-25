package course.dal.bean;

public class HowNetData {
	Integer NO;
	String W_C;
	String G_C;
	String S_C;
	String W_E;
	String G_E;
	String S_E;
	String DEF;

	public String toString() {
		String result = "NO: " + NO + ", W_C: " + W_C + ", G_C: " + G_C + ", S_C: " + S_C + ", W_E: " + W_E + ", G_E: "
				+ G_E + ", DEF: " + DEF;
		return result;
	}

	public Integer getNO() {
		return NO;
	}

	public void setNO(Integer nO) {
		NO = nO;
	}

	public String getW_C() {
		return W_C;
	}

	public void setW_C(String w_C) {
		W_C = w_C;
	}

	public String getG_C() {
		return G_C;
	}

	public void setG_C(String g_C) {
		G_C = g_C;
	}

	public String getS_C() {
		return S_C;
	}

	public void setS_C(String s_C) {
		S_C = s_C;
	}

	public String getW_E() {
		return W_E;
	}

	public void setW_E(String w_E) {
		W_E = w_E;
	}

	public String getG_E() {
		return G_E;
	}

	public void setG_E(String g_E) {
		G_E = g_E;
	}

	public String getS_E() {
		return S_E;
	}

	public void setS_E(String s_E) {
		S_E = s_E;
	}

	public String[] getDEF() {
		if (DEF != null) {
			return DEF.split(",");
		}
		return null;
	}
	
	public String getDEFStr() {
		return DEF;
	}

	public void setDEF(String dEF) {
		DEF = dEF;
	}

}
