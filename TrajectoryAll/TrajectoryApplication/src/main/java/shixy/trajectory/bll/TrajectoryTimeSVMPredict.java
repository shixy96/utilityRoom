package shixy.trajectory.bll;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import shixy.trajectory.libsvm.lib.svm;
import shixy.trajectory.libsvm.lib.svm_model;
import shixy.trajectory.libsvm.lib.svm_node;
import shixy.trajectory.libsvm.lib.svm_parameter;
import shixy.trajectory.libsvm.lib.svm_problem;

@Component
public class TrajectoryTimeSVMPredict {
	public static final Boolean hasScaleFile = false;
	public static final String resourceTrainFile = "src/test/resources/trainFile.txt";
	public static final String resourceTestFile = "src/test/resources/testFile.txt";
	public static final String resourceModelFile = "src/test/resources/modelFile.txt";

	public void print(String n) {
		System.out.println(n);
	}

	public String predictByTime(Double lat, Double lng) {
		final String time[] = {"早上", "下午", "晚上", "凌晨"};
		return time[(int) Math.round(Math.random()*100/25)];
	}
	
	public String predict(Double lat, Double lng) {
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
		svm_node[] dataTest = new svm_node[2];
		dataTest[0] = new svm_node(0, lat);
		dataTest[1] = new svm_node(1, lng);
		double predictValue = svm.svm_predict(model, dataTest);
		if (predictValue == 0) {
			return "凌晨";
		} else if (predictValue == 1) {
			return "早上";
		} else if (predictValue == 2) {
			return "下午";
		} else if (predictValue == 3) {
			return "晚上";
		} else {
			return "未知";
		}
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