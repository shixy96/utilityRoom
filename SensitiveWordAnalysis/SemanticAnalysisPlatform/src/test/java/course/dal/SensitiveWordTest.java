package course.dal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import course.bll.SensitiveWordManager;
import course.dal.bean.SensitiveNatureLevel;
import course.dal.bean.SensitiveWordData;

public class SensitiveWordTest {
	ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context.getBean("sensitiveWordManager");
	private static final String testString = "test_content";

	@Test
	public void test() {
		sensitiveWordManager.insert(testString, SensitiveNatureLevel.source.level());
		assertTrue(sensitiveWordManager.exist(testString));

		SensitiveWordData testResult = sensitiveWordManager.search(testString, null, 0, 1).get(0);
		assertEquals(testResult.getWord(), testString);

		sensitiveWordManager.delete(testResult.getId(), testString, null);
		assertFalse(sensitiveWordManager.exist(testString));
	}
}
