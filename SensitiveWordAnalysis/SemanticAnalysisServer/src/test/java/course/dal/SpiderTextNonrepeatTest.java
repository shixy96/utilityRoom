package course.dal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import course.dal.bean.SpiderTextData;

public class SpiderTextNonrepeatTest {
	ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private SpiderTextNonrepeatDal spiderTextNonrepeatDal = (SpiderTextNonrepeatDal) context.getBean("spiderTextNonrepeatDal");
	private static final String testString = "测试数据";
	private static final String changeString = "update数据";
	private static final Integer isSensitive = 0;

	@Test
	public void test() {
		Integer id = null;
		String text = null;
		spiderTextNonrepeatDal.insert(testString, isSensitive, null, null);
		List<SpiderTextData> spiderTextDatas = spiderTextNonrepeatDal.completeSearch(null, testString, isSensitive, null, null);
		if (!spiderTextDatas.isEmpty()) {
			id = spiderTextDatas.get(0).getId();
			text = spiderTextDatas.get(0).getText();
		}
		assertFalse(id == null);
		assertEquals(testString, text);

		spiderTextNonrepeatDal.update(id, changeString, isSensitive, null, null);
		spiderTextDatas = spiderTextNonrepeatDal.completeSearch(id, changeString, isSensitive, null, null);
		if (!spiderTextDatas.isEmpty()) {
			text = spiderTextDatas.get(0).getText();
		}
		assertEquals(changeString, text);

		spiderTextNonrepeatDal.delete(id);
		spiderTextDatas = spiderTextNonrepeatDal.completeSearch(null, testString, isSensitive, null, null);
		assertTrue(spiderTextDatas.isEmpty());
	}
}
