package course.train.sematicparams.support;

import java.io.IOException;

import course.dal.bean.SematicParams;
import course.train.sematicparams.ExportMarkedFile;
import course.train.sematicparams.SvmTrain;
import course.train.sematicparams.Train;

public class TransformTrain implements Train {
//	SBV: 0.8	VOB: 0.0	IOB: 0.2	FOB: 0.7	DBL: 0.5	ATT: 0.0	ADV: 0.0	CMP: 0.0	
	public static double BEST_SBV = 0.8;
	public static double BEST_VOB = 0.0;
	public static double BEST_IOB = 0.2;
	public static double BEST_FOB = 0.7;
	public static double BEST_DBL = 0.5;
	public static double BEST_ATT = 0.0;
	public static double BEST_ADV = 0.0;
	public static double BEST_CMP = 0.0;
	private int nearest_rate_index = 1;

	private SvmTrain svmTrain;
	private ExportMarkedFile exportMarkedFile;
	public static final String trainFileName = "src/test/resources/transform/transformTrain.txt";
	public static final String testFileName = "src/test/resources/transform/transformTest.txt";
	public static final String modelFileName = "src/test/resources/transform/transformModel.txt";
	public static final String resultFileName = "src/test/resources/transform/transformResult.txt";

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		TransformTrain transformTrain = new TransformTrain();
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
		exportMarkedFile.setUseNearest(false);
		exportMarkedFile.setUseTranslate(true);
		exportMarkedFile.setTrain_File_Name(trainFileName);
		exportMarkedFile.setTest_File_Name(testFileName);

		svmTrain = new SvmTrain();
		svmTrain.setC(2);
		svmTrain.setGamma(0.5);
		svmTrain.setTrain_File_Name(trainFileName);
		svmTrain.setTest_File_Name(testFileName);
		svmTrain.setModel_File_Name(modelFileName);
		svmTrain.setResult_File_Name(resultFileName);

		exportMarkedFile.markExport(getBestParams(), nearest_rate_index);
		svmTrain.training();
	}

}