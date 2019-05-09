package course.dal.datas.train.sematicparams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import course.dal.bean.SematicParams;
import course.libsvm.lib.svm;
import course.libsvm.lib.svm_model;
import course.libsvm.lib.svm_node;
import course.libsvm.lib.svm_parameter;
import course.libsvm.lib.svm_problem;
import course.util.IOUtil;

public class FindBetterParams {
	private SematicParams sematicParams = new SematicParams();
	private double BEST_SBV = 0;
	private double BEST_VOB = 0;
	private double BEST_IOB = 0;
	private double BEST_FOB = 0;
	private double BEST_DBL = 0;
	private double BEST_ATT = 0;
	private double BEST_ADV = 0;
	private double BEST_CMP = 0;
	private double min_err = 1;
	public static final String resultFileName = "src/test/resources/FindBetterParams/result";
	public static final String processFileName = "src/test/resources/FindBetterParams/process";

	public static void main(String[] args) {
		FindBetterParams findBetterParams = new FindBetterParams();
		findBetterParams.betterTrainingSBV();
		findBetterParams.betterTrainingDBL();
		findBetterParams.betterTrainingOB();
		findBetterParams.betterTrainingXIU();
		findBetterParams.outProcess(findBetterParams.min_err, true);
	}

	private void betterTrainingSBV() {
		setParamsToBest();
		for (double SBV = 0; SBV <= 10; SBV++) {
			// 主谓关系
			sematicParams.setS_SBV(SBV / 10);
			ExportMarkedFile.markExport(sematicParams);
			double currentErr = training();
			if (currentErr < min_err) {
				min_err = currentErr;
				BEST_SBV = SBV / 10;
			}
		}
	}

	private void betterTrainingDBL() {
		setParamsToBest();
		for (double DBL = 0; DBL <= 10; DBL++) {
			// 兼词
			sematicParams.setS_DBL(DBL / 10);
			ExportMarkedFile.markExport(sematicParams);
			double currentErr = training();
			if (currentErr < min_err) {
				min_err = currentErr;
				BEST_DBL = DBL / 10;
			}
		}
	}

	private void betterTrainingOB() {
		setParamsToBest();
		for (double OB = 0; OB <= 10; OB++) {
			// 宾语
			sematicParams.setS_VOB(OB / 10);
			sematicParams.setS_IOB(OB / 10);
			sematicParams.setS_FOB(OB / 10);
			ExportMarkedFile.markExport(sematicParams);
			double currentErr = training();
			if (currentErr < min_err) {
				min_err = currentErr;
				BEST_VOB = OB / 10;
				BEST_IOB = OB / 10;
				BEST_FOB = OB / 10;
			}
		}
	}

	private void betterTrainingXIU() {
		setParamsToBest();
		for (double XIU = 0; XIU <= 10; XIU++) {
			// 修饰语(状语、定语、补语)
			sematicParams.setS_ATT(XIU / 10);
			sematicParams.setS_ADV(XIU / 10);
			sematicParams.setS_CMP(XIU / 10);
			ExportMarkedFile.markExport(sematicParams);
			double currentErr = training();
			if (currentErr < min_err) {
				min_err = currentErr;
				BEST_ATT = XIU / 10;
				BEST_ADV = XIU / 10;
				BEST_CMP = XIU / 10;
			}
		}
	}

	private void setParamsToBest() {
		sematicParams.setS_SBV(BEST_SBV);
		sematicParams.setS_VOB(BEST_VOB);
		sematicParams.setS_IOB(BEST_IOB);
		sematicParams.setS_FOB(BEST_FOB);
		sematicParams.setS_DBL(BEST_DBL);
		sematicParams.setS_ATT(BEST_ATT);
		sematicParams.setS_ADV(BEST_ADV);
		sematicParams.setS_CMP(BEST_CMP);
	}

	private double training() {
		List<Double> label = new ArrayList<Double>();
		List<svm_node[]> nodeSet = new ArrayList<svm_node[]>();
		getData(nodeSet, label, ExportMarkedFile.fileName);

		int dataRange = nodeSet.get(0).length;
		svm_node[][] datas = new svm_node[nodeSet.size()][dataRange];
		for (int i = 0; i < datas.length; i++) {
			for (int j = 0; j < dataRange; j++) {
				datas[i][j] = nodeSet.get(i)[j];
			}
		}
		double[] lables = new double[label.size()];
		for (int i = 0; i < lables.length; i++) {
			lables[i] = label.get(i);
		}

		svm_problem problem = new svm_problem();
		problem.l = nodeSet.size();
		problem.x = datas;
		problem.y = lables;

		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.cache_size = 100;
		param.eps = 0.00001;
		param.C = 8;
		param.gamma = 8;
		System.out.println(svm.svm_check_parameter(problem, param));
		svm_model model = svm.svm_train(problem, param);

		double err = 0.0;
		for (int i = 0; i < datas.length; i++) {
			double truevalue = lables[i];
			double predictValue = svm.svm_predict(model, datas[i]);
//			err += Math.abs(predictValue - truevalue);
			err += predictValue != truevalue ? 1 : 0;
		}
		double errate = err / datas.length;
		outProcess(errate, false);
		return errate;
	}

	@SuppressWarnings("resource")
	public void getData(List<svm_node[]> nodeSet, List<Double> label, String filename) {
		try {
			FileReader fr = new FileReader(new File(filename));
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] datas = line.split(" ");
				svm_node[] vector = new svm_node[datas.length - 1];
				for (int i = 1; i < datas.length; i++) {
					svm_node node = new svm_node();
					String[] feature = datas[i].split(":");
					node.index = Integer.parseInt(feature[0]);
					node.value = Double.parseDouble(feature[1]);
					vector[i - 1] = node;
				}
				nodeSet.add(vector);
				double lablevalue = Double.parseDouble(datas[0]);
				label.add(lablevalue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void outProcess(double errate, boolean isResult) {
		String process = "S_SBV() = " + sematicParams.getS_SBV() + "\t\t" + "S_VOB = " + sematicParams.getS_VOB() + "\t"
				+ "S_IOB = " + sematicParams.getS_IOB() + "\t" + "S_FOB = " + sematicParams.getS_FOB() + "\t\t"
				+ "S_DBL = " + sematicParams.getS_DBL() + "\t\t" + "S_ATT = " + sematicParams.getS_ATT() + "\t"
				+ "S_ADV = " + sematicParams.getS_ADV() + "\t" + "S_CMP = " + sematicParams.getS_CMP() + "\t\t" + "err="
				+ errate + "\n";
		IOUtil.output_to_file(process, isResult ? resultFileName : processFileName, true);
		if (isResult) {
			System.err.println(process);
		} else {
			System.out.println(process);
		}
	}

	/*
	 * private void betterTrainingOrigin() { for (double XIU = 0; XIU <= 10; XIU++)
	 * { // 修饰语(状语、定语、补语) sematicParams.setS_ATT(XIU / 10);
	 * sematicParams.setS_ADV(XIU / 10); sematicParams.setS_CMP(XIU / 10); for
	 * (double OB = 0; OB <= 10; OB++) { // 宾语 sematicParams.setS_VOB(OB / 10);
	 * sematicParams.setS_IOB(OB / 10); sematicParams.setS_FOB(OB / 10); for (double
	 * DBL = 0; DBL <= 10; DBL++) { // 兼词 sematicParams.setS_DBL(DBL / 10); for
	 * (double SBV = 0; SBV <= 10; SBV++) { // 主谓关系 sematicParams.setS_SBV(SBV /
	 * 10); ExportMarkedFile.mark(sematicParams); double currentErr = training(); if
	 * (currentErr < min_err) { min_err = currentErr; setBestParams(); } } } } } }
	 * 
	 * private void setBestParams() { BEST_SBV = sematicParams.getS_SBV(); BEST_VOB
	 * = sematicParams.getS_VOB(); BEST_IOB = sematicParams.getS_IOB(); BEST_FOB =
	 * sematicParams.getS_FOB(); BEST_DBL = sematicParams.getS_DBL(); BEST_ATT =
	 * sematicParams.getS_ATT(); BEST_ADV = sematicParams.getS_ADV(); BEST_CMP =
	 * sematicParams.getS_CMP(); }
	 */
}