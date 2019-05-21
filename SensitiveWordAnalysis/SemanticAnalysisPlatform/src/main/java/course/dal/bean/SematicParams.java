package course.dal.bean;

public class SematicParams {
	private double S_SBV;
	private double S_VOB;
	private double S_IOB;
	private double S_FOB;
	private double S_DBL;
	private double S_ATT;
	private double S_ADV;
	private double S_CMP;

	public SematicParams() {
	}

	public SematicParams(double s_SBV, double s_VOB, double s_IOB, double s_FOB, double s_DBL, double s_ATT,
			double s_ADV, double s_CMP) {
		super();
		S_SBV = s_SBV;
		S_VOB = s_VOB;
		S_IOB = s_IOB;
		S_FOB = s_FOB;
		S_DBL = s_DBL;
		S_ATT = s_ATT;
		S_ADV = s_ADV;
		S_CMP = s_CMP;
	}

	public double getS_SBV() {
		return S_SBV;
	}

	public void setS_SBV(double s_SBV) {
		S_SBV = s_SBV;
	}

	public double getS_VOB() {
		return S_VOB;
	}

	public void setS_VOB(double s_VOB) {
		S_VOB = s_VOB;
	}

	public double getS_IOB() {
		return S_IOB;
	}

	public void setS_IOB(double s_IOB) {
		S_IOB = s_IOB;
	}

	public double getS_FOB() {
		return S_FOB;
	}

	public void setS_FOB(double s_FOB) {
		S_FOB = s_FOB;
	}

	public double getS_DBL() {
		return S_DBL;
	}

	public void setS_DBL(double s_DBL) {
		S_DBL = s_DBL;
	}

	public double getS_ATT() {
		return S_ATT;
	}

	public void setS_ATT(double s_ATT) {
		S_ATT = s_ATT;
	}

	public double getS_ADV() {
		return S_ADV;
	}

	public void setS_ADV(double s_ADV) {
		S_ADV = s_ADV;
	}

	public double getS_CMP() {
		return S_CMP;
	}

	public void setS_CMP(double s_CMP) {
		S_CMP = s_CMP;
	}

}
