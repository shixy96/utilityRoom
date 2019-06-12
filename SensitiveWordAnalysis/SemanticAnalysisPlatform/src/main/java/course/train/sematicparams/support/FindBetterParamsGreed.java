package course.train.sematicparams.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import course.dal.bean.SematicParams;
import course.train.sematicparams.ExportMarkedFile;
import course.train.sematicparams.SvmTrain;
import course.util.IOUtil;

public class FindBetterParamsGreed {
	private int nearest_rate_index = 1;
	private final int arraySize = 8;
	private boolean changed = false;
	private List<String> bestNames = Arrays.asList("SBV", "VOB", "IOB", "FOB", "DBL", "ATT", "ADV", "CMP");
	private List<Double>[] USE_list = (List<Double>[]) new List[arraySize];
	private List<Double>[] Cache_list = (List<Double>[]) new List[arraySize];

	{
		for (int index = 0; index < USE_list.length; index++) {
			switch (index) {
			case 7:
			case 6:
			case 5:
			case 4:
			case 3:
			case 2:
				USE_list[index] = Arrays.asList(0.0);
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
	public static final String trainFileName = "src/test/resources/FindBetterParams/FindBetterParamsGreed/AnalysisSematicTrain.txt";
	public static final String testFileName = "src/test/resources/FindBetterParams/FindBetterParamsGreed/AnalysisSematicTest.txt";
	public static final String processFileName = "src/test/resources/FindBetterParams/FindBetterParamsGreed/precess.txt";
	public static final String processLoopFileName = "src/test/resources/FindBetterParams/FindBetterParamsGreed/precessLoop.txt";
	public static final String resultFileName = "src/test/resources/FindBetterParams/FindBetterParamsGreed/result.txt";

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		FindBetterParamsGreed findBetterParams = new FindBetterParamsGreed();
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

		findBetterParams.betterTraining();
	}

	/**
	 * 使用队列存储
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void betterTraining() throws ClassNotFoundException, IOException {
		double currentF1 = 0;
		changed = false;
		IOUtil.output_to_file(printParamsName(), resultFileName, true);
		for (int cmpIndex = 0; cmpIndex < USE_list[7].size(); cmpIndex++) {
			// CMP
			for (int advIndex = 0; advIndex < USE_list[6].size(); advIndex++) {
				// ADV
				for (int attIndex = 0; attIndex < USE_list[5].size(); attIndex++) {
					// ATT
					for (int dblIndex = 0; dblIndex < USE_list[4].size(); dblIndex++) {
						// DBL
						for (int fobIndex = 0; fobIndex < USE_list[3].size(); fobIndex++) {
							// FOB
							for (int iobIndex = 0; iobIndex < USE_list[2].size(); iobIndex++) {
								// IOB
								for (int vobIndex = 0; vobIndex < USE_list[1].size(); vobIndex++) {
									// VOB
									for (int sbvIndex = 0; sbvIndex < USE_list[0].size(); sbvIndex++) {
										// SBV
										SematicParams sParams = new SematicParams(USE_list[0].get(sbvIndex),
												USE_list[1].get(vobIndex), USE_list[2].get(iobIndex),
												USE_list[3].get(fobIndex), USE_list[4].get(dblIndex),
												USE_list[5].get(attIndex), USE_list[6].get(advIndex),
												USE_list[7].get(cmpIndex));
										exportMarkedFile.markExport(sParams, nearest_rate_index);
										currentF1 = svmTrain.training();
										IOUtil.output_to_file(printParams(
												Arrays.asList(USE_list[0].get(sbvIndex), USE_list[1].get(vobIndex),
														USE_list[2].get(iobIndex), USE_list[3].get(fobIndex),
														USE_list[4].get(dblIndex), USE_list[5].get(attIndex),
														USE_list[6].get(advIndex), USE_list[7].get(cmpIndex)),
												currentF1), resultFileName, true);
									}
								}
							}
						}
					}
				}
			}
		}
		String reString = printList();
		IOUtil.output_to_file(reString, resultFileName, true);
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

	private String printParams(List<Double> list, double f1) {
		StringBuffer sBuffer = new StringBuffer();
		for (int index = 0; index < arraySize && index < list.size(); index++) {
			sBuffer.append(list.get(index));
			sBuffer.append("\t");
		}
		sBuffer.append(f1 + "\r\n");
		System.out.print(sBuffer.toString());
		return sBuffer.toString();
	}

	private String printParamsName() {
		StringBuffer sBuffer = new StringBuffer();
		for (int index = 0; index < arraySize; index++) {
			sBuffer.append(bestNames.get(index));
			sBuffer.append("\t");
		}
		sBuffer.append("F1\r\n");
		System.out.print(sBuffer.toString());
		return sBuffer.toString();
	}

}