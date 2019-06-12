package course.train.sematicparams.support;

import java.io.IOException;

import course.dal.bean.SematicParams;
import course.train.sematicparams.ExportMarkedFile;
import course.train.sematicparams.SvmTrain;
import course.train.sematicparams.Train;

public class TransformNearestTrain implements Train {
	public static double BEST_SBV = 0.8;
	public static double BEST_VOB = 0.0;
	public static double BEST_IOB = 0.2;
	public static double BEST_FOB = 0.7;
	public static double BEST_DBL = 0.5;
	public static double BEST_ATT = 0.0;
	public static double BEST_ADV = 0.0;
	public static double BEST_CMP = 0.0;
	private static double[] nearest_rates = { 0.55, 0.65, 0.75, 0.85, .95 };
	private int nearest_rate_index = 1;

	private SvmTrain svmTrain;
	private ExportMarkedFile exportMarkedFile;
	public static final String outputFold = "src/test/resources/transformNearest/";
	public static final String trainFileName = "transformTrain.txt";
	public static final String testFileName = "transformTest.txt";
	public static final String modelFileName = "transformModel.txt";
	public static final String resultFileName = "transformResult.txt";

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		TransformNearestTrain transformTrain = new TransformNearestTrain();
		transformTrain.train();
	}

	private SematicParams getBestParams() {
		SematicParams sematicParams = new SematicParams();
		sematicParams.setS_SBV(BEST_SBV);
		sematicParams.setS_VOB(BEST_VOB);
		sematicParams.setS_IOB(BEST_IOB);
		sematicParams.setS_FOB(BEST_FOB);
		sematicParams.setS_DBL(BEST_DBL);
		sematicParams.setS_ATT(BEST_ATT);
		sematicParams.setS_ADV(BEST_ADV);
		sematicParams.setS_CMP(BEST_CMP);
		return sematicParams;
	}

	@Override
	public void train() throws ClassNotFoundException, IOException {
		exportMarkedFile = new ExportMarkedFile();
		exportMarkedFile.setUseNearest(true);
		exportMarkedFile.setUseTranslate(true);
		exportMarkedFile.setTrain_File_Name(trainFileName);
		exportMarkedFile.setTest_File_Name(testFileName);

		svmTrain = new SvmTrain();
		svmTrain.setC(8);
		svmTrain.setGamma(9);
		svmTrain.setTrain_File_Name(trainFileName);
		svmTrain.setTest_File_Name(testFileName);
		svmTrain.setResult_File_Name(resultFileName);

		for (nearest_rate_index = 0; nearest_rate_index < nearest_rates.length; nearest_rate_index++) {
			exportMarkedFile.setTrain_File_Name(outputFold + nearest_rates[nearest_rate_index] + trainFileName);
			exportMarkedFile.setTest_File_Name(outputFold + nearest_rates[nearest_rate_index] + testFileName);
			svmTrain.setTrain_File_Name(outputFold + nearest_rates[nearest_rate_index] + trainFileName);
			svmTrain.setTest_File_Name(outputFold + nearest_rates[nearest_rate_index] + testFileName);
			svmTrain.setModel_File_Name(outputFold + nearest_rates[nearest_rate_index] + modelFileName);
			svmTrain.setResult_File_Name(outputFold + nearest_rates[nearest_rate_index] + resultFileName);

			exportMarkedFile.markExport(getBestParams(), nearest_rate_index);
			svmTrain.training();
		}
	}

}