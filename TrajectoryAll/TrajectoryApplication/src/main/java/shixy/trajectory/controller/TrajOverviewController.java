package shixy.trajectory.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWComplexity;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import shixy.trajectory.MainApp;
import shixy.trajectory.bean.TrajectoryData;
import shixy.trajectory.bean.TrajectoryEdge;
import shixy.trajectory.bll.TrajectoryEdgeManage;
import shixy.trajectory.bll.TrajectoryManage;
import shixy.trajectory.bll.TrajectoryTimeSVMPredict;
import shixy.trajectory.dal.server.SqlServer;
import threeb.Class1;

@SuppressWarnings("restriction")
public class TrajOverviewController {
	private ApplicationContext context = new ClassPathXmlApplicationContext("springmvc-config.xml");
	private SqlServer sqlServer = (SqlServer) context.getBean("sqlServer");
	private TrajectoryTimeSVMPredict trajectoryTimeSVMPredict = new TrajectoryTimeSVMPredict();
	@FXML
	private MenuBar menubar;
	@FXML
	private Menu file;
	@FXML
	private Menu insert;
	@FXML
	private MenuItem ins_floder;
	@FXML
	private MenuItem ins_file;
	@FXML
	private MenuItem exit;
	@FXML
	private Menu edit;
	@FXML
	private Menu del;
	@FXML
	private MenuItem del_flder;
	@FXML
	private MenuItem del_file;
	@FXML
	private Menu analysis;
	@FXML
	private RadioMenuItem ana_single;
	@FXML
	private ToggleGroup Ana_Group;
	@FXML
	private RadioMenuItem ana_group;
	@FXML
	private MenuItem groupEdit;
	@FXML
	private Menu map;
	@FXML
	private MenuItem map_trajectory;
	@FXML
	private MenuItem map_hotspot;
	@FXML
	private MenuItem map_three;
	@FXML
	private MenuItem time_predict;
	@FXML
	private MenuItem map_clear;
	@FXML
	private Menu help;
	@FXML
	private MenuItem help_format;
	@FXML
	private MenuItem help_about;
	@FXML
	private SplitPane split_big;
	@FXML
	private TreeView<String> treeview = new TreeView<>();
	@FXML
	private SplitPane split_small;
	@FXML
	private TabPane tabpane_Map;
	@FXML
	private Tab tab_Map;
	@FXML
	private WebView webview = new WebView();
	@FXML
	private Tab tab_Predict;
	@FXML
	private TabPane tabpane_bar;
	@FXML
	private Tab tab_bar;
	final NumberAxis xAxis = new NumberAxis();
	final NumberAxis yAxis = new NumberAxis();
	final LineChart<Number, Number> time_bar = new LineChart<Number, Number>(xAxis, yAxis);
	@FXML
	private Label startime;
	@FXML
	private DatePicker startDatePicker = new DatePicker(LocalDate.of(2000, 1, 1));
	@FXML
	private Label endtime;
	@FXML
	private DatePicker endDatePicker = new DatePicker(LocalDate.now());

	/**
	 * 设置日期格式
	 */
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static int trajectoryNum = 0; // 数据人数
	private WebEngine eng = new WebEngine();
	private JSObject jsobj = (JSObject) eng.executeScript("window");
	private static int CurrentItem = 0;
	private static boolean isOrigin = true;

	public static int getTrajectoryNum() {
		return trajectoryNum;
	}

	public void createTree() {
		TreeItem<String> rootItem = new TreeItem<>("Geolife Member List");
		rootItem.setExpanded(true);
		List<Integer> memberList = sqlServer.listMember();
		if (memberList.isEmpty()) {
			return;
		}
		for (int member : memberList) {
			TreeItem<String> item = new TreeItem<>("" + member);
			item.setExpanded(false);
			rootItem.getChildren().add(item);
		}
		trajectoryNum = memberList.size();
		treeview.setRoot(rootItem);
	}

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public TrajOverviewController() {
	}

	/**
	 * 初始化函数
	 */
	@FXML
	private void initialize() {
		createTree();
		eng = webview.getEngine();
		eng.load(getClass().getResource("../view/MapView.html").toExternalForm());
		tab_bar.setContent(time_bar);
		treeview.getSelectionModel().selectedItemProperty().addListener((obv, ov, nv) -> showItermDetails(nv));
	}

	public LocalDate Date2LocalDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		LocalDate localDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
		return localDate;
	}

	private void showItermDetails(TreeItem<String> treeitem) {
		CurrentItem = Integer.valueOf(treeitem.getValue());
		try {
			List<Integer> memberList = new ArrayList<>();
			if (GroupInPutController.getGroupset().length == 0) {
				memberList.add(CurrentItem);
			}

			jsobj = (JSObject) eng.executeScript("window");
			jsobj.setMember("sqlTest", sqlServer);
			jsobj.setMember("isOrigin", isOrigin);
			jsobj.setMember("memberList", memberList);
			jsobj.setMember("predictServer", trajectoryTimeSVMPredict);
			// 更新时间区域
			startDatePicker.setValue(Date2LocalDate(sqlServer.findMinTime(CurrentItem)));
			endDatePicker.setValue(Date2LocalDate(sqlServer.findMaxTime(CurrentItem)));

			// 更新时间统计图
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName("member " + CurrentItem);
			int j;
			int allNum = sqlServer.getTimeNum(CurrentItem, 24);
			for (j = 0; j < 24; j++) {
				series.getData().add(new Data<Number, Number>(j, sqlServer.getTimeNum(CurrentItem, j) * 100 / allNum));
			}
			if (ana_single.isSelected()) {
				time_bar.getData().clear();
			}
			time_bar.getData().add(series);

			eng = webview.getEngine();
			eng.executeScript("firstLoad()");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	// Event Listener on MenuItem[#ins_floder].onAction
	@FXML
	public void INS_Floder(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#ins_file].onAction
	@FXML
	public void INS_File(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#exit].onAction
	@FXML
	public void Exit(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#del_flder].onAction
	@FXML
	public void Del_Folder(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#del_file].onAction
	@FXML
	public void Del_File(ActionEvent event) {
		// TODO Autogenerated
	}

	@FXML
	public void Ana_Single() {
		groupEdit.setDisable(true);
		TrajOverviewController.isOrigin = true;
		GroupInPutController.clearGroup();
		System.out.println("Ana_Single");
		List<Integer> memberList = new ArrayList<>();
		memberList.add(CurrentItem);
		jsobj = (JSObject) eng.executeScript("window");
		jsobj.setMember("sqlTest", sqlServer);
		jsobj.setMember("isOrigin", isOrigin);
		jsobj.setMember("memberList", memberList);
		jsobj.setMember("predictServer", trajectoryTimeSVMPredict);
		eng = webview.getEngine();
		eng.load(getClass().getResource("../view/MapView.html").toExternalForm());
	}

	@FXML
	public void Ana_Group() {
		map_hotspot.setDisable(false);
		groupEdit.setDisable(false);
	}

	/**
	 * 编辑群组分析的成员
	 * 
	 * @return (void)
	 * @author sxy
	 */
	@FXML
	public void Group_Edit() {
		boolean okClicked = MainApp.showGroupEditDialog();
		if (okClicked) {
			// System.out.println(GroupInPutController.getGroupset());
		}
	}

	@FXML
	public void Map_Trajectory(ActionEvent event) {
		TrajOverviewController.isOrigin = true;
		System.out.println("Map_Trajectory isOrigin");

		List<Integer> memberList = new ArrayList<>();
		if (GroupInPutController.getGroupset().length == 0) {
			if (CurrentItem == -1) {
				String errMessage = "请选择成员！";
				// Show the error message.
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Look, there is some error!");
				alert.setContentText(errMessage);
				alert.showAndWait();
				return;
			}
			memberList.add(CurrentItem);
		} else {
			for (int item : GroupInPutController.getGroupset()) {
				memberList.add(item);
			}
		}
		jsobj = (JSObject) eng.executeScript("window");
		jsobj.setMember("sqlTest", sqlServer);
		jsobj.setMember("isOrigin", isOrigin);
		jsobj.setMember("memberList", memberList);
		jsobj.setMember("predictServer", trajectoryTimeSVMPredict);
	}

	@FXML
	public void Map_Hotspot(ActionEvent event) {
		isOrigin = false;
		System.out.println("Map_Hotspot isOrigin");
		List<Integer> memberList = new ArrayList<>();
		if (GroupInPutController.getGroupset().length == 0) {
			if (CurrentItem == -1) {
				String errMessage = "请选择成员！";
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Look, there is some error!");
				alert.setContentText(errMessage);
				alert.showAndWait();
				return;
			}
			memberList.add(CurrentItem);
		} else {
			for (int item : GroupInPutController.getGroupset()) {
				memberList.add(item);
			}
		}
		jsobj = (JSObject) eng.executeScript("window");
		jsobj.setMember("sqlTest", sqlServer);
		jsobj.setMember("isOrigin", isOrigin);
		jsobj.setMember("memberList", memberList);
		jsobj.setMember("predictServer", trajectoryTimeSVMPredict);
		eng = webview.getEngine();
		eng.executeScript("showHotPoint()");
	}

	
	
	// Event Listener on MenuItem[#map_three].onAction
	@FXML
	public void Map_Three(ActionEvent event) {
		TrajectoryManage trajectoryManage = (TrajectoryManage) context.getBean("trajectoryManage");
		TrajectoryEdgeManage trajectoryEdgeManage = (TrajectoryEdgeManage) context.getBean("trajectoryEdgeManage");
		TrajectoryEdge trajectoryEdge = trajectoryEdgeManage.search(CurrentItem);
		List<TrajectoryData> trajectoryDatas = trajectoryManage.getListByLocation(CurrentItem,
				trajectoryEdge.getMaxLat(), trajectoryEdge.getMaxLng(), trajectoryEdge.getMinLat(),
				trajectoryEdge.getMinLat());
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

	@FXML
	public void Map_Clear() {
		eng = webview.getEngine();
		eng.load(getClass().getResource("../view/MapView.html").toExternalForm());
	}

	// Event Listener on MenuItem[#help_format].onAction
	@FXML
	public void Help_Formation(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#help_about].onAction
	@FXML
	public void Help_About(ActionEvent event) {
		// TODO Autogenerated
	}
}
