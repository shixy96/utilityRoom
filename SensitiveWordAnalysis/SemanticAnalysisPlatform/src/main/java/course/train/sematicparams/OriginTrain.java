package course.train.sematicparams;

import java.io.IOException;

public class OriginTrain implements Train {
	private int nearest_rate_index = 1;
	private ExportMarkedFile exportMarkedFile;
	private SvmTrain svmTrain;
	public static final String trainFileName = "src/test/resources/origin/originTrainFile.txt";
	public static final String testFileName = "src/test/resources/origin/originTestFile.txt";
	public static final String resultFileName = "src/test/resources/origin/originResultFile.txt";

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		OriginTrain originTrain = new OriginTrain();
		originTrain.train();
	}

	@Override
	public void train() throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		exportMarkedFile = new ExportMarkedFile();
		exportMarkedFile.setUseNearest(false);
		exportMarkedFile.setUseTranslate(false);
		exportMarkedFile.setTrain_File_Name(trainFileName);
		exportMarkedFile.setTest_File_Name(testFileName);
		exportMarkedFile.markExport(null, nearest_rate_index);
		svmTrain = new SvmTrain();
		svmTrain.setC(2);
		svmTrain.setGamma(0.5);
		svmTrain.setTrain_File_Name(trainFileName);
		svmTrain.setTest_File_Name(testFileName);
		svmTrain.setResult_File_Name(resultFileName);
		svmTrain.training();
	}

}