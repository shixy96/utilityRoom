package course.dal.datas.train.word2vec;

import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

public class WordVectorResource {
	public static void main(String[] args) {
		Word2VecTrainer trainerBuilder = new Word2VecTrainer();
		WordVectorModel wordVectorModel = trainerBuilder.train("src/test/resources/Word2VecTrain/msr_training.txt",
				"src/test/resources/Word2VecTrain/msr_vectors.txt");
	}
}
