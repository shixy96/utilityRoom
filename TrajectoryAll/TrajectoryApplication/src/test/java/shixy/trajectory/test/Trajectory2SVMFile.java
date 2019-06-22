package shixy.trajectory.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import shixy.trajectory.bean.TrajectoryData;
import shixy.trajectory.bll.TrajectoryManage;

public class Trajectory2SVMFile {
	public static final String trainFilename = "src/test/resources/trainFile.txt";
	public static final String testFilename = "src/test/resources/testFile.txt";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("springmvc-config.xml");
		TrajectoryManage trajectoryManage = (TrajectoryManage) context.getBean("trajectoryManage");

		int offset = 0, limit = 200;
		Double maxLat = trajectoryManage.getMaxLat(0);
		Double minLat = trajectoryManage.getMinLat(0);
		Double maxLng = trajectoryManage.getMaxLng(0);
		Double minLng = trajectoryManage.getMinLng(0);
		Integer allNum = trajectoryManage.getNumByLocation(0, maxLat, maxLng, minLat, minLng);
		try {
			BufferedWriter train_save = new BufferedWriter(new FileWriter(trainFilename, true));
			BufferedWriter test_save = new BufferedWriter(new FileWriter(testFilename, true));
			while (offset < allNum) {
				List<TrajectoryData> trajectoryDatas = trajectoryManage.getListLimit(0, offset, limit);
				for (TrajectoryData trajectoryData : trajectoryDatas) {
					int timeNum = 0;// 0凌晨,1早上,2下午,3晚上
					if (trajectoryData.getTime().getHours() >= 18) {
						timeNum = 3;
					} else if (trajectoryData.getTime().getHours() >= 12) {
						timeNum = 2;
					} else if (trajectoryData.getTime().getHours() >= 6) {
						timeNum = 1;
					}
					String content = timeNum + " " + "1:" + trajectoryData.getLat() + " 2:" + trajectoryData.getLng()
							+ "\n";
					if (offset * 1.0 / allNum >= 0.7) {
						test_save.write(content);
					} else {
						train_save.write(content);
					}
				}
				offset += trajectoryDatas.size();
			}
			train_save.close();
			test_save.close();
		} catch (IOException e) {
			System.err.println("can't open file ");
			System.exit(1);
		}
		((ConfigurableApplicationContext) context).close();
	}
}
