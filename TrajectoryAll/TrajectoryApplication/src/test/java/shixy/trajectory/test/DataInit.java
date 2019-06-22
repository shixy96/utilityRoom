package shixy.trajectory.test;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import shixy.trajectory.bean.TrajectoryData;
import shixy.trajectory.bean.TrajectoryEdge;
import shixy.trajectory.bll.GuideManage;
import shixy.trajectory.bll.TrajectoryEdgeManage;
import shixy.trajectory.bll.TrajectoryManage;
import shixy.trajectory.bll.TrajectoryTimeSVMPredict;
import shixy.trajectory.dal.sqlInit.FileToSql;
import shixy.trajectory.dal.sqlInit.MaxNumAreaInit;

/**
 * 初始化数据库
 * 
 * @author sxy
 * @date 2018年5月30日
 */
public class DataInit {
	private static final boolean initDataBase = false;

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("springmvc-config.xml");
		GuideManage guideManage = (GuideManage) context.getBean("guideManage");
		guideManageValid(guideManage);
		TrajectoryManage trajectoryManage = (TrajectoryManage) context.getBean("trajectoryManage");
		trajectoryManageValid(trajectoryManage);
		TrajectoryEdgeManage trajectoryEdgeManage = (TrajectoryEdgeManage) context.getBean("trajectoryEdgeManage");
		trajectoryEdgeManageValid(trajectoryEdgeManage);
		TrajectoryTimeSVMPredict trajectoryTimeSVMPredict = new TrajectoryTimeSVMPredict();
		System.out.println(trajectoryTimeSVMPredict.predict(39.991744430044555, 116.3311732276635));
		if (initDataBase) {
			MaxNumAreaInit maxNumAreaInit = (MaxNumAreaInit) context.getBean("maxNumAreaInit");
			maxNumAreaInitValid(maxNumAreaInit);
			initAllTable();
		}
		((ConfigurableApplicationContext) context).close();
	}

	private static void initAllTable() {
		for (int i = 0; i < 182; i++) {
			FileToSql.InitTable(i);
		}
	}

	private static void guideManageValid(GuideManage guideManage) {
		guideManage.insert(1000, "1000");
		System.out.println(guideManage.getTablelistMember());
		guideManage.delete(1000);
		System.out.println(guideManage.getTablelistMember());
		System.out.println();
	}

	private static void trajectoryManageValid(TrajectoryManage trajectoryManage) {
		System.out.println(trajectoryManage.getNumByLocation(0, 39.99174443, 116.3311732, 39.0, 116.0));
		List<TrajectoryData> trajectoryDatas = trajectoryManage.getListByLocation(0, 39.99174443, 116.3311732, 39.0,
				116.0);
		System.out.println(trajectoryDatas.size());
		System.out.println(trajectoryManage.getMaxTime(0));
		System.out.println(trajectoryManage.getMinTime(0));
		System.out.println(trajectoryManage.getMaxLat(0));
		System.out.println(trajectoryManage.getMinLat(0));
		System.out.println(trajectoryManage.getMaxLng(0));
		System.out.println(trajectoryManage.getMinLng(0));
		System.out.println(trajectoryManage.getNumByTime(0, 12));
		System.out.println();
	}

	private static void trajectoryEdgeManageValid(TrajectoryEdgeManage trajectoryEdgeManage) {
		TrajectoryEdge trajectoryEdge = trajectoryEdgeManage.search(0);
		System.out.println(trajectoryEdge.getMaxLat());
		System.out.println(trajectoryEdge.getMaxLng());
		System.out.println(trajectoryEdge.getMinLat());
		System.out.println(trajectoryEdge.getMinLng());
	}

	private static void maxNumAreaInitValid(MaxNumAreaInit maxNumAreaInit) {
		/*
		 * TrajectoryEdge trajectoryEdge = maxNumAreaInit.findMaxNumEdge(0);
		 * System.out.println(trajectoryEdge.getMaxLat());
		 * System.out.println(trajectoryEdge.getMinLat());
		 * System.out.println(trajectoryEdge.getMaxLng());
		 * System.out.println(trajectoryEdge.getMinLng());
		 */
		maxNumAreaInit.initMaxNumAreaEdge();
	}
}
