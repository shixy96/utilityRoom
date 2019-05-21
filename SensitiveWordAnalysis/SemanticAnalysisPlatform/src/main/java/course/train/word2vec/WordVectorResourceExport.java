package course.train.word2vec;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import course.bll.TxtCollectionManager;
import course.dal.bean.TxtCollectionData;
import course.util.IOUtil;

public class WordVectorResourceExport {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static TxtCollectionManager txtCollectionManager = (TxtCollectionManager) context
			.getBean("txtCollectionManager");

	private static final int limit_once = 1000;
	public final String fileName = "src/test/resources/搜狗文本分类语料库已分词.txt";

	public static void main(String[] args) {
		WordVectorResourceExport wExport = new WordVectorResourceExport();
		wExport.export();
	}

	private void export() {
		int offset = 0;
		List<TxtCollectionData> datas = txtCollectionManager.search(null, null, offset, limit_once);
		while (datas != null && datas.size() > 0) {
			for (int i = 0; i < datas.size(); i++) {
				offset++;
				List<Term> terms = HanLP.segment(datas.get(i).getContent());
				StringBuffer sBuffer = new StringBuffer("\n");
				for (Term term : terms) {
					sBuffer.append(term.getWord() + " ");
				}
				IOUtil.output_to_file(sBuffer.toString(), fileName, true);
			}
			System.out.println(offset);
			datas = txtCollectionManager.search(null, null, offset, limit_once);
		}
	}
}
