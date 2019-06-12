package course.train.sematicparams.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import course.dal.bean.SematicParams;
import course.train.sematicparams.ExportMarkedFile;
import course.train.sematicparams.SvmTrain;
import course.train.sematicparams.Train;
import course.util.ArrayUtil;
import course.util.IOUtil;

public class FindBetterParams implements Train {
	private SematicParams sematicParams;
	private double BEST_SBV = 0.8;
	private double BEST_VOB = 0.4;
	private double BEST_IOB = 0.4;
	private double BEST_FOB = 1.0;
	private double BEST_DBL = 0.1;
	private double BEST_ATT = 0.9;
	private double BEST_ADV = 0.1;
	private double BEST_CMP = 0.1;
	private double max_f1 = 0;
	private int nearest_rate_index = 1;
	private final int arraySize = 8;
	private boolean changed = false;
	private double[] bestList = new double[arraySize];
	private List<String> bestNames = Arrays.asList("SBV", "VOB", "IOB", "FOB", "DBL", "ATT", "ADV", "CMP");
	private List<Double>[] USE_list = (List<Double>[]) new List[arraySize];
	private List<Double>[] Cache_list = (List<Double>[]) new List[arraySize];

	{
		for (int index = 0; index < USE_list.length; index++) {
			switch (index) {
			case 7:
			case 6:
			case 5:
				USE_list[index] = Arrays.asList(0.0);
				break;
			case 4:
				USE_list[index] = Arrays.asList(0.5);
				break;
			case 3:
				USE_list[index] = Arrays.asList(0.7);
				break;
			case 2:
				USE_list[index] = Arrays.asList(0.3);
				break;
			case 1:
				USE_list[index] = Arrays.asList(0.5, 0.6, 0.7, 0.8, 0.9, 1.0);
				break;
			case 0:
				USE_list[index] = Arrays.asList(0.8);
				break;
			default:
				USE_list[index] = Arrays.asList(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0);
				break;
			}
		}
		for (int index = 0; index < Cache_list.length; index++) {
			Cache_list[index] = new ArrayList<>();
		}

	}

	private SvmTrain svmTrain;
	private ExportMarkedFile exportMarkedFile;
	public static final String trainFileName = "src/test/resources/FindBetterParams/AnalysisSematicTrain.txt";
	public static final String testFileName = "src/test/resources/FindBetterParams/AnalysisSematicTest.txt";
	public static final String processFileName = "src/test/resources/FindBetterParams/precess.txt";
	public static final String processLoopFileName = "src/test/resources/FindBetterParams/precessLoop.txt";
	public static final String resultFileName = "src/test/resources/FindBetterParams/result.txt";

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		FindBetterParams findBetterParams = new FindBetterParams();
		findBetterParams.exportMarkedFile = new ExportMarkedFile();
		findBetterParams.exportMarkedFile.setUseNearest(false);
		findBetterParams.exportMarkedFile.setUseTranslate(true);
		findBetterParams.exportMarkedFile.setLoopFile(true);
		findBetterParams.exportMarkedFile.setTrain_File_Name(trainFileName);
		findBetterParams.exportMarkedFile.setTest_File_Name(testFileName);

		findBetterParams.svmTrain = new SvmTrain();
		findBetterParams.svmTrain.setC(2);
		findBetterParams.svmTrain.setGamma(0.5);
		findBetterParams.svmTrain.setTrain_File_Name(trainFileName);
		findBetterParams.svmTrain.setTest_File_Name(testFileName);
		findBetterParams.svmTrain.setResult_File_Name(processFileName);

		findBetterParams.train();
	}
	

	/**
	 * 使用队列存储
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void train() throws ClassNotFoundException, IOException {
		double currentF1 = 0;
		do {
			changed = false;
			for (int sbvIndex = 0; sbvIndex < USE_list[0].size(); sbvIndex++) {
				// SBV
				for (int vobIndex = 0; vobIndex < USE_list[1].size(); vobIndex++) {
					// VOB
					for (int iobIndex = 0; iobIndex < USE_list[2].size(); iobIndex++) {
						// IOB
						for (int fobIndex = 0; fobIndex < USE_list[3].size(); fobIndex++) {
							// FOB
							for (int dblIndex = 0; dblIndex < USE_list[4].size(); dblIndex++) {
								// DBL
								for (int attIndex = 0; attIndex < USE_list[5].size(); attIndex++) {
									// ATT
									for (int advIndex = 0; advIndex < USE_list[6].size(); advIndex++) {
										// ADV
										for (int cmpIndex = 0; cmpIndex < USE_list[7].size(); cmpIndex++) {
											// CMP
											SematicParams sParams = new SematicParams(USE_list[0].get(sbvIndex),
													USE_list[1].get(vobIndex), USE_list[2].get(iobIndex),
													USE_list[3].get(fobIndex), USE_list[4].get(dblIndex),
													USE_list[5].get(attIndex), USE_list[6].get(advIndex),
													USE_list[7].get(cmpIndex));
											exportMarkedFile.markExport(sParams, nearest_rate_index);
											IOUtil.output_to_file(printParams(
													Arrays.asList(USE_list[0].get(sbvIndex), USE_list[1].get(vobIndex),
															USE_list[2].get(iobIndex), USE_list[3].get(fobIndex),
															USE_list[4].get(dblIndex), USE_list[5].get(attIndex),
															USE_list[6].get(advIndex), USE_list[7].get(cmpIndex))),
													processFileName, true);
											currentF1 = svmTrain.training();
											afterProcess(7, currentF1, USE_list[7].get(cmpIndex), false);
											afterProcess(6, currentF1, USE_list[6].get(advIndex), false);
											afterProcess(5, currentF1, USE_list[5].get(attIndex), false);
											afterProcess(4, currentF1, USE_list[4].get(dblIndex), false);
											afterProcess(3, currentF1, USE_list[3].get(fobIndex), false);
											afterProcess(2, currentF1, USE_list[2].get(iobIndex), false);
											afterProcess(1, currentF1, USE_list[1].get(vobIndex), false);
											afterProcess(0, currentF1, USE_list[0].get(sbvIndex), false);
										}
										printLine("CMP");
										afterProcess(6, currentF1, USE_list[6].get(advIndex), true);
									}
									printLine("ADV");
									afterProcess(5, currentF1, USE_list[5].get(attIndex), true);
								}
								printLine("ATT");
								afterProcess(4, currentF1, USE_list[4].get(dblIndex), true);
							}
							printLine("DBL");
							afterProcess(3, currentF1, USE_list[3].get(fobIndex), true);
						}
						printLine("FOB");
						afterProcess(2, currentF1, USE_list[2].get(iobIndex), true);
					}
					printLine("IOB");
					afterProcess(1, currentF1, USE_list[1].get(vobIndex), true);
				}
				printLine("VOB");
				afterProcess(0, currentF1, USE_list[0].get(sbvIndex), true);
			}
			if (Cache_list[0] != null && !Cache_list[0].isEmpty()) {
				USE_list[0] = ArrayUtil.deepCopy(Cache_list[0]);
				Cache_list[0].clear();
			}
			printLine("SBV");
		} while (changed);
		String reString = printList();
		IOUtil.output_to_file(reString, resultFileName, true);
	}

	private void printLine(String string) {
		StringBuffer sb = new StringBuffer("--------------------" + string + "--------------------\r\n");
		sb.append(printList());
		sb.append("--------------------" + string + "--------------------\r\n");
		System.out.println(sb.toString());
		IOUtil.output_to_file(sb.toString(), processLoopFileName, true);
	}

	private void afterProcess(int index, double currentF1, double currentParam, boolean replace)
			throws ClassNotFoundException, IOException {
		if (replace && index + 1 < arraySize && Cache_list[index + 1] != null && !Cache_list[index + 1].isEmpty()) {
			USE_list[index + 1] = ArrayUtil.deepCopy(Cache_list[index + 1]);
			Cache_list[index + 1].clear();
		}
		if (currentF1 == max_f1 && Cache_list[index].indexOf(currentParam) == -1) {
			Cache_list[index].add(currentParam);
		} else if (currentF1 > max_f1) {
			changed = true;
			max_f1 = currentF1;
			bestList[index] = currentParam;
			Cache_list[index] = new ArrayList<>(Arrays.asList(currentParam));
		}
	}

	private String printList() {
		StringBuffer sBuffer = new StringBuffer();
		for (int index = 0; index < arraySize; index++) {
			sBuffer.append(bestNames.get(index) + ": ");
			for (double sub : USE_list[index]) {
				sBuffer.append(sub + "\t");
			}
			sBuffer.append("\r\n");
		}
		System.out.print(sBuffer.toString());
		return sBuffer.toString();
	}

	private String printParams(List<Double> list) {
		StringBuffer sBuffer = new StringBuffer();
		for (int index = 0; index < arraySize && index < list.size(); index++) {
			sBuffer.append(bestNames.get(index) + ": " + list.get(index));
			sBuffer.append("\t");
		}
		sBuffer.append("\r\n");
		System.out.print(sBuffer.toString());
		return sBuffer.toString();
	}

	/**
	 * 隔离处理
	 */
	private void betterTrain() {
		betterTrainingSBV();
		betterTrainingDBL();
		betterTrainingOB();
		betterTrainingXIU();
		bestTrainingADV();
		bestTrainingATT();
		bestTrainingCMP();
		bestTrainingIOB();
		bestTrainingVOB();
		bestTrainingFOB();
	}

	private void betterTrainingSBV() {
		setParamsToBest();
		for (double SBV = 1; SBV <= 10; SBV++) {
			// 主谓关系
			sematicParams.setS_SBV(SBV / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_SBV = SBV / 10;
			}
		}
	}

	private void betterTrainingDBL() {
		setParamsToBest();
		for (double DBL = 1; DBL <= 10; DBL++) {
			// 兼词
			sematicParams.setS_DBL(DBL / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_DBL = DBL / 10;
			}
		}
	}

	private void betterTrainingOB() {
		setParamsToBest();
		for (double OB = 1; OB <= 10; OB++) {
			// 宾语
			sematicParams.setS_VOB(OB / 10);
			sematicParams.setS_IOB(OB / 10);
			sematicParams.setS_FOB(OB / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_VOB = OB / 10;
				BEST_IOB = OB / 10;
				BEST_FOB = OB / 10;
			}
		}
	}

	private void bestTrainingFOB() {
		setParamsToBest();
		for (double OB = 1; OB <= 10; OB++) {
			// 宾语
			sematicParams.setS_FOB(OB / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_FOB = OB / 10;
			}
		}
	}

	private void bestTrainingIOB() {
		setParamsToBest();
		for (double OB = 1; OB <= 10; OB++) {
			// 宾语
			sematicParams.setS_IOB(OB / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_IOB = OB / 10;
			}
		}
	}

	private void bestTrainingVOB() {
		setParamsToBest();
		for (double OB = 1; OB <= 10; OB++) {
			// 宾语
			sematicParams.setS_VOB(OB / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_VOB = OB / 10;
			}
		}
	}

	private void betterTrainingXIU() {
		setParamsToBest();
		for (double XIU = 1; XIU <= 10; XIU++) {
			// 修饰语(状语、定语、补语)
			sematicParams.setS_ATT(XIU / 10);
			sematicParams.setS_ADV(XIU / 10);
			sematicParams.setS_CMP(XIU / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_ATT = XIU / 10;
				BEST_ADV = XIU / 10;
				BEST_CMP = XIU / 10;
			}
		}
	}

	private void bestTrainingCMP() {
		setParamsToBest();
		for (double CMP = 1; CMP <= 10; CMP++) {
			// 修饰语(状语、定语、补语)
			sematicParams.setS_CMP(CMP / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_CMP = CMP / 10;
			}
		}
	}

	private void bestTrainingADV() {
		setParamsToBest();
		for (double ADV = 1; ADV <= 10; ADV++) {
			// 修饰语(定语)
			sematicParams.setS_ADV(ADV / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_ADV = ADV / 10;
			}
		}
	}

	private void bestTrainingATT() {
		setParamsToBest();
		for (double ATT = 1; ATT <= 10; ATT++) {
			// 修饰语(状语)
			sematicParams.setS_ATT(ATT / 10);
			exportMarkedFile.markExport(sematicParams, nearest_rate_index);
			double currentF1 = svmTrain.training();
			if (currentF1 > max_f1) {
				max_f1 = currentF1;
				BEST_ATT = ATT / 10;
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

}