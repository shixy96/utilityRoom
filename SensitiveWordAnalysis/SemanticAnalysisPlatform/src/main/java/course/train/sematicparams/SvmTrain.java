package course.train.sematicparams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import course.libsvm.lib.svm;
import course.libsvm.lib.svm_model;
import course.libsvm.lib.svm_node;
import course.libsvm.lib.svm_parameter;
import course.libsvm.lib.svm_problem;
import course.util.IOUtil;

public class SvmTrain {
	/**
	 * 训练数据文件名
	 */
	private String Train_File_Name = "src/test/resources/SvmTrain.txt";
	
	/**
	 * 测试数据文件名
	 */
	private String Test_File_Name = "src/test/resources/SvmText.txt";
	
	/**
	 * 敏感语句分析模型文件名
	 */
	private String Model_File_Name = "src/test/resources/Svmmodel.txt";
	
	/**
	 * 训练结果文件名
	 */
	private String Result_File_Name = "src/test/resources/SvmResult.txt";
	
	/**
	 * libsvm相关配置
	 */
	private int svm_type = svm_parameter.C_SVC;
	private int kernel_type = svm_parameter.RBF;
	private double C = 2;
	private double gamma = 2;
	private double cache_size = 100;
	private double eps = 0.00001;
	
	/**
	 * 敏感向量训练接口
	 * @return 训练结果F1值
	 */
	public double training() {
		List<Double> label = new ArrayList<Double>();
		List<svm_node[]> nodeSet = new ArrayList<svm_node[]>();
		SvmTrain.getData(nodeSet, label, Train_File_Name);

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
		param.svm_type = svm_type;
		param.kernel_type = kernel_type;
		param.cache_size = cache_size;
		param.eps = eps;
		param.C = C;
		param.gamma = gamma;
		System.out.println(svm.svm_check_parameter(problem, param));
		svm_model model = svm.svm_train(problem, param);

		try {
			svm.svm_save_model(Model_File_Name, model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Double> testlabel = new ArrayList<Double>();
		List<svm_node[]> testnodeSet = new ArrayList<svm_node[]>();
		SvmTrain.getData(testnodeSet, testlabel, Test_File_Name);
		dataRange = testnodeSet.get(0).length;

		svm_node[][] testdatas = new svm_node[testnodeSet.size()][dataRange];
		for (int i = 0; i < testdatas.length; i++) {
			for (int j = 0; j < dataRange; j++) {
				testdatas[i][j] = testnodeSet.get(i)[j];
			}
		}
		double[] testlables = new double[testlabel.size()];
		for (int i = 0; i < testlables.length; i++) {
			testlables[i] = testlabel.get(i);
		}

		return predict(model, testlables, testdatas);
	}

	public String getModel_File_Name() {
		return Model_File_Name;
	}

	public void setModel_File_Name(String model_File_Name) {
		Model_File_Name = model_File_Name;
	}

	private double predict(svm_model model, double[] lables, svm_node[][] datas) {
		double accuary = 0.0, precision = 0.0, recall = 0.0, f1 = 0.0;
		int TP_Num = 0, FP_Num = 0, TN_Num = 0, FN_Num = 0;
		for (int i = 0; i < datas.length; i++) {
			double truevalue = lables[i];
			double predictValue = svm.svm_predict(model, datas[i]);
			boolean predictTure = predictValue == truevalue;
			if (truevalue == 0) {
				if (predictTure) {
					System.out.println(i);
					TN_Num++;
				} else {
					FN_Num++;
				}
			} else {
				if (predictTure) {
					System.out.println(i);
					TP_Num++;
				} else {
					FP_Num++;
				}
			}
		}
		System.out.println("TP_Num=" + TP_Num + "\tFP_Num=" + FP_Num + "\tTN_Num=" + TN_Num + "\tFN_Num=" + FN_Num);
		accuary = (TP_Num + TN_Num) * 1.0 / (TP_Num + TN_Num + FP_Num + FN_Num);
		precision = TP_Num * 1.0 / (TP_Num + FP_Num);
		recall = TP_Num * 1.0 / (TP_Num + FN_Num);
		f1 = 2.0 * TP_Num / (2.0 * TP_Num + FP_Num + FN_Num);
		outProcess(accuary, precision, recall, f1);
		return f1;
	}

	private void outProcess(double accuary, double precision, double recall, double f1) {
		String process = "TestFile" + ": accuary=" + accuary + "\tprecision=" + precision + "\trecall=" + recall
				+ "\tf1=" + f1 + "\n";
		IOUtil.output_to_file(process, Result_File_Name, true);
		System.err.println(process);
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

	public String getTrain_File_Name() {
		return Train_File_Name;
	}

	public void setTrain_File_Name(String train_File_Name) {
		Train_File_Name = train_File_Name;
	}

	public String getTest_File_Name() {
		return Test_File_Name;
	}

	public void setTest_File_Name(String test_File_Name) {
		Test_File_Name = test_File_Name;
	}

	public int getSvm_type() {
		return svm_type;
	}

	public void setSvm_type(int svm_type) {
		this.svm_type = svm_type;
	}

	public int getKernel_type() {
		return kernel_type;
	}

	public void setKernel_type(int kernel_type) {
		this.kernel_type = kernel_type;
	}

	public double getC() {
		return C;
	}

	public void setC(double c) {
		C = c;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public String getResult_File_Name() {
		return Result_File_Name;
	}

	public void setResult_File_Name(String result_File_Name) {
		Result_File_Name = result_File_Name;
	}

	public double getCache_size() {
		return cache_size;
	}

	public void setCache_size(double cache_size) {
		this.cache_size = cache_size;
	}

	public double getEps() {
		return eps;
	}

	public void setEps(double eps) {
		this.eps = eps;
	}
}
