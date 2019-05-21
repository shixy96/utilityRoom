package course.train.sematicparams;

import java.io.IOException;

public class NearestTrain implements Train {
	private int nearest_rate_index = 1;
	private ExportMarkedFile exportMarkedFile;
	private SvmTrain svmTrain;
	private static double[] nearest_rates = { .55, .65, .75, .85, .95 };
	public static final String outputFold = "src/test/resources/nearest/";
	public static final String trainFileName = "nearestTrainFile.txt";
	public static final String testFileName = "nearestTestFile.txt";
	public static final String resultFileName = "nearestResultFile.txt";

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		NearestTrain nearestTrain = new NearestTrain();
		nearestTrain.train();
	}

	@Override
	public void train() throws ClassNotFoundException, IOException {
		NearestTrain originTrain = new NearestTrain();
		originTrain.exportMarkedFile = new ExportMarkedFile();
		originTrain.exportMarkedFile.setUseNearest(true);
		originTrain.exportMarkedFile.setUseTranslate(false);
		originTrain.exportMarkedFile.setNearest_rates(nearest_rates);
		originTrain.svmTrain = new SvmTrain();
		originTrain.svmTrain.setC(2);
		originTrain.svmTrain.setGamma(0.5);
		for (originTrain.nearest_rate_index = 0; originTrain.nearest_rate_index < nearest_rates.length; originTrain.nearest_rate_index++) {
			originTrain.exportMarkedFile
					.setTrain_File_Name(outputFold + nearest_rates[originTrain.nearest_rate_index] + trainFileName);
			originTrain.exportMarkedFile
					.setTest_File_Name(outputFold + nearest_rates[originTrain.nearest_rate_index] + testFileName);
			originTrain.svmTrain
					.setTrain_File_Name(outputFold + nearest_rates[originTrain.nearest_rate_index] + trainFileName);
			originTrain.svmTrain
					.setTest_File_Name(outputFold + nearest_rates[originTrain.nearest_rate_index] + testFileName);
			originTrain.svmTrain
					.setResult_File_Name(outputFold + nearest_rates[originTrain.nearest_rate_index] + resultFileName);

			originTrain.exportMarkedFile.markExport(null, originTrain.nearest_rate_index);
			originTrain.svmTrain.training();
		}
	}

}