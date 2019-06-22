package shixy.trajectory.controller;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWComplexity;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import shixy.trajectory.bean.TrajectoryData;
import shixy.trajectory.bean.TrajectoryEdge;
import shixy.trajectory.bll.TrajectoryEdgeManage;
import shixy.trajectory.bll.TrajectoryManage;
import threeb.Class1;

class CreatPlot3 {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("springmvc-config.xml");
		TrajectoryManage trajectoryManage = (TrajectoryManage) context.getBean("trajectoryManage");
		TrajectoryEdgeManage trajectoryEdgeManage = (TrajectoryEdgeManage) context.getBean("trajectoryEdgeManage");
		TrajectoryEdge trajectoryEdge = trajectoryEdgeManage.search(0);
		List<TrajectoryData> trajectoryDatas = trajectoryManage.getListByLocation(0, trajectoryEdge.getMaxLat(),
				trajectoryEdge.getMaxLng(), trajectoryEdge.getMinLat(), trajectoryEdge.getMinLat());
		MWNumericArray x = null; // 存放lat值的数组
		MWNumericArray y = null; // 存放lng值的数组
		MWNumericArray z = null; // 存放time值的数组
		Class1 thePlot = null; // plotter类的实例
		int n = trajectoryDatas.size() > 10000 ? 10000 : trajectoryDatas.size();
		try {
			int[] dims = { 1, n };
			x = MWNumericArray.newInstance(dims, MWClassID.DOUBLE, MWComplexity.REAL);
			y = MWNumericArray.newInstance(dims, MWClassID.DOUBLE, MWComplexity.REAL);
			z = MWNumericArray.newInstance(dims, MWClassID.DOUBLE, MWComplexity.REAL);

			for (int i = 1; i <= n; i++) {
				x.set(i, trajectoryDatas.get(i).getLat());
				y.set(i, trajectoryDatas.get(i).getLng());
				z.set(i, trajectoryDatas.get(i).getRetday());
			}

			thePlot = new Class1();
			thePlot.threeb(x, y, z);
			thePlot.waitForFigures();
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		} finally {
			((ConfigurableApplicationContext) context).close();
			MWArray.disposeArray(x);
			MWArray.disposeArray(y);
			if (thePlot != null)
				thePlot.dispose();
		}
	}
}