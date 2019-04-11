package course.dal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import course.bll.SensitiveWordManager;

public class SensitiveWordTest {
	ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private SensitiveWordManager sensitiveWordManager = (SensitiveWordManager) context.getBean("sensitiveWordManager");
	private static final String testString = "test_content";

	@Test
	public void test() {
		sensitiveWordManager.insert(testString);
		assertTrue(sensitiveWordManager.exist(testString));

		String testResult = sensitiveWordManager.search(testString).get(0);
		assertEquals(testResult, testString);

		sensitiveWordManager.delete(testString);
		assertFalse(sensitiveWordManager.exist(testString));
	}
}
