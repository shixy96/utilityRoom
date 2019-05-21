package course.bll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import course.libsvm.lib.svm;
import course.libsvm.lib.svm_model;
import course.libsvm.lib.svm_node;

public class SematicAnalysisManager {
	private static svm_model model;
	private static String resourceModelFile = "senaticAnalysis.model";
	private static SematicAnalysisManager sematicAnalysisManager;

	static {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = SematicAnalysisManager.class.getClassLoader();
		}
		try {
			String path = loader.getResource(resourceModelFile).getPath();
			model = svm.svm_load_model(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static SematicAnalysisManager instance() {
		if (sematicAnalysisManager == null) {
			sematicAnalysisManager = new SematicAnalysisManager();
		}
		return sematicAnalysisManager;
	}

	public boolean predict(String sematicVec) {
		List<svm_node[]> testnodeSet = new ArrayList<svm_node[]>();
		getData(testnodeSet, sematicVec);
		double predictValue = svm.svm_predict(model, testnodeSet.get(0));
		return predictValue == 1.0;
	}

	public void getData(List<svm_node[]> nodeSet, String sematicVec) {
		String[] datas = sematicVec.split(" ");
		svm_node[] vector = new svm_node[datas.length];
		for (int i = 0; i < datas.length; i++) {
			svm_node node = new svm_node();
			String[] feature = datas[i].split(":");
			node.index = Integer.parseInt(feature[0]);
			node.value = Double.parseDouble(feature[1]);
			vector[i] = node;
		}
		nodeSet.add(vector);
	}
}
