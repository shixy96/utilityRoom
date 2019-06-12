package course.train.sematicparams.support;

import java.io.IOException;

import course.train.sematicparams.ExportMarkedFile;
import course.train.sematicparams.SvmTrain;
import course.train.sematicparams.Train;

public class TransformLuTrain implements Train {
	private int nearest_rate_index = 1;

	private SvmTrain svmTrain;
	private ExportMarkedFile exportMarkedFile;
	public static final String trainFileName = "src/test/resources/transformLu/transformLuTrain.txt";
	public static final String testFileName = "src/test/resources/transformLu/transformLuTest.txt";
	public static final String modelFileName = "src/test/resources/transformLu/transformLuModel.txt";
	public static final String resultFileName = "src/test/resources/transformLu/transformLuResult.txt";

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		TransformLuTrain transformLuTrain = new TransformLuTrain();
		transformLuTrain.train();
	}

	@Override
	public void train() throws ClassNotFoundException, IOException {
		exportMarkedFile = new ExportMarkedFile();
		exportMarkedFile.setUseLuMethod(true);
		exportMarkedFile.setTrain_File_Name(trainFileName);
		exportMarkedFile.setTest_File_Name(testFileName);

		svmTrain = new SvmTrain();
		svmTrain.setC(2);
		svmTrain.setGamma(0.5);
		svmTrain.setTrain_File_Name(trainFileName);
		svmTrain.setTest_File_Name(testFileName);
		svmTrain.setModel_File_Name(modelFileName);
		svmTrain.setResult_File_Name(resultFileName);

		exportMarkedFile.markExport(null, nearest_rate_index);
		svmTrain.training();
	}

}