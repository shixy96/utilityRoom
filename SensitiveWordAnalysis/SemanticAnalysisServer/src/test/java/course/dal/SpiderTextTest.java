package course.dal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import course.dal.bean.SpiderTextData;

public class SpiderTextTest {
	ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private SpiderTextDal spiderTextDal = (SpiderTextDal) context.getBean("spiderTextDal");
	private static final String testString = "测试数据";
	private static final String changeString = "update数据";

	@Test
	public void test() {
		Integer id = null;
		String text = null;
		spiderTextDal.insert(testString, null, null);
		List<SpiderTextData> spiderTextDatas = spiderTextDal.completeSearch(null, testString, null, null);
		if (!spiderTextDatas.isEmpty()) {
			id = spiderTextDatas.get(0).getId();
			text = spiderTextDatas.get(0).getText();
		}
		assertFalse(id == null);
		assertEquals(testString, text);

		spiderTextDal.update(id, changeString, null, null);
		spiderTextDatas = spiderTextDal.completeSearch(id, changeString, null, null);
		if (!spiderTextDatas.isEmpty()) {
			text = spiderTextDatas.get(0).getText();
		}
		assertEquals(changeString, text);

		spiderTextDal.delete(id);
		spiderTextDatas = spiderTextDal.completeSearch(null, testString, null, null);
		assertTrue(spiderTextDatas.isEmpty());
	}
}
