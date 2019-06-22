package shixy.trajectory;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shixy.trajectory.controller.GroupInPutController;

/**
 * TrajectoryApp入口
 * 
 * @author sxy
 * @date 2018年5月30日
 */
@SuppressWarnings("restriction")
public class MainApp extends Application {
	private static Stage primaryStage;
	private AnchorPane TrajOverview;

	@Override
	public void start(Stage pStage) {
		try {
			primaryStage = pStage;
			primaryStage.setTitle("trajectory");
			primaryStage.getIcons().add(new Image("file:src\\main\\resources\\images\\trajectory_app.jpg"));
			initLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TrajOverview.fxml"));
			TrajOverview = (AnchorPane) loader.load();

			Scene scene = new Scene(TrajOverview);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean showGroupEditDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/GroupInPut.fxml"));
			Parent page = loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("编辑群组成员");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			GroupInPutController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
