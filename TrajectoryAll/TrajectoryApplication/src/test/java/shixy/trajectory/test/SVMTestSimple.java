package shixy.trajectory.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shixy.trajectory.libsvm.lib.svm;
import shixy.trajectory.libsvm.lib.svm_model;
import shixy.trajectory.libsvm.lib.svm_node;
import shixy.trajectory.libsvm.lib.svm_parameter;
import shixy.trajectory.libsvm.lib.svm_problem;

public class SVMTestSimple {
	public static final Boolean hasScaleFile = false;
	public static final String resourceTrainFile = "src/test/resources/trainFile.txt";
	public static final String resourceTestFile = "src/test/resources/testFile.txt";
	public static final String resourceModelFile = "src/test/resources/modelFile.txt";

	public static void main(String[] args) {
		File modelFile = new File(resourceModelFile);
		int dataRange;
		svm_model model = null;
		if (!modelFile.isFile()) {
			List<Double> label = new ArrayList<Double>();
			List<svm_node[]> nodeSet = new ArrayList<svm_node[]>();
			getData(nodeSet, label, resourceTrainFile);
			dataRange = nodeSet.get(0).length;// 特征数量
			svm_node[][] datas = new svm_node[nodeSet.size()][dataRange]; // 训练集的向量表
			for (int i = 0; i < datas.length; i++) {
				for (int j = 0; j < dataRange; j++) {
					datas[i][j] = nodeSet.get(i)[j];
				}
			}
			double[] lables = new double[label.size()]; // a,b 对应的lable
			for (int i = 0; i < lables.length; i++) {
				lables[i] = label.get(i);
			}

			// 定义svm_problem对象
			svm_problem problem = new svm_problem();
			problem.l = nodeSet.size(); // 向量个数
			problem.x = datas; // 训练集向量表
			problem.y = lables; // 对应的lable数组

			// 定义svm_parameter对象
			svm_parameter param = new svm_parameter();
			param.svm_type = svm_parameter.C_SVC;
			param.kernel_type = svm_parameter.RBF;
			param.cache_size = 100;
			param.eps = 2;
			param.C = 2;
			// 训练SVM分类模型
			System.out.println(svm.svm_check_parameter(problem, param));
			// 如果参数没有问题，则svm.svm_check_parameter()函数返回null,否则返回error描述。
			model = svm.svm_train(problem, param);
			try {
				svm.svm_save_model(resourceModelFile, model);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				model = svm.svm_load_model(resourceModelFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 获取测试数据
		List<Double> testlabel = new ArrayList<Double>();
		List<svm_node[]> testnodeSet = new ArrayList<svm_node[]>();
		getData(testnodeSet, testlabel, resourceTestFile);
		dataRange = testnodeSet.get(0).length;

		svm_node[][] testdatas = new svm_node[testnodeSet.size()][dataRange]; // 训练集的向量表
		for (int i = 0; i < testdatas.length; i++) {
			for (int j = 0; j < dataRange; j++) {
				testdatas[i][j] = testnodeSet.get(i)[j];
			}
		}
		double[] testlables = new double[testlabel.size()]; // a,b 对应的lable
		for (int i = 0; i < testlables.length; i++) {
			testlables[i] = testlabel.get(i);
		}

		// 预测测试数据的lable
		double err = 0.0;
		for (int i = 0; i < testdatas.length; i++) {
			double truevalue = testlables[i];
			double predictValue = svm.svm_predict(model, testdatas[i]);
			if (predictValue != truevalue) {
				err += 1;
			}
		}
		System.out.println("err=" + err / (testdatas.length / 0.3 * 0.7));
	}

	@SuppressWarnings("resource")
	public static void getData(List<svm_node[]> nodeSet, List<Double> label, String filename) {
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
}