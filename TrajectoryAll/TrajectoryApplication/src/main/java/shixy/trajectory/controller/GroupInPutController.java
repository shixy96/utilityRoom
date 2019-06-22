package shixy.trajectory.controller;

import javafx.fxml.FXML;
import java.util.HashSet;
import java.util.Iterator;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author sxy
 * @date 2018/5/30
 */
@SuppressWarnings("restriction")
public class GroupInPutController {
	@FXML
	private VBox text_Vbox;
	private Stage dialogStage;
	private boolean okClicked = false;
	private static HashSet<Integer> groupSet = new HashSet<>();
	public static HashSet<Integer> getGroup() {
		return groupSet;
	}
	public static void clearGroup() {
		groupSet.clear();
	}

	/**
	 * 初始化函数，还原groupSet集中的成员
	 */
	@FXML
	private void initialize() {
		if (!groupSet.isEmpty()) {
			Iterator<Integer> interator = groupSet.iterator();
			for (int i = 0; i < groupSet.size(); i++) {
				if (i < text_Vbox.getChildren().size()) {
					((TextField) text_Vbox.getChildren().get(i)).setText(interator.next().toString());
				} else {
					AddText(interator.next().toString());
				}
			}
		}
	}

	/**
	 * Sets the stage of this dialog.
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		this.dialogStage.getIcons().add(new Image("file:src\\main\\resources\\images\\user_group.png"));
	}
	/**
	* @return 由Groupset集组成的数组(int[])
	* @author sxy
	 */
	public static int[] getGroupset() {
		Iterator<Integer> interator = groupSet.iterator();
		int a[] = new int[groupSet.size()];
		for (int i = 0; i < groupSet.size(); i++) {
			a[i] = interator.next();
		}
		return a;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * 增加一个空白的textfield
	 * 
	 * @return (void)
	 * @author sxy
	 */
	@FXML
	public void AddText() {
		TextField text = new TextField();
		text_Vbox.getChildren().add(text);
	}

	/**
	 * 增加一个内容为成员编号memberNum的textfield，在重新编辑群组成员时用到
	 * 
	 * @return (void)
	 * @author sxy
	 */
	public void AddText(String memberNum) {
		TextField text = new TextField(memberNum);
		text_Vbox.getChildren().add(text);
	}

	/**
	 * 删除一个textfield
	 * 
	 * @return (void)
	 * @author sxy
	 */
	@FXML
	public void DelText() {
		if (text_Vbox.getChildren().size() > 0) {
			int num = text_Vbox.getChildren().size();
			String a = ((TextField) text_Vbox.getChildren().get(num - 1)).getText();
			try {
				if (!groupSet.isEmpty()) {
					if (a != "" && groupSet.contains(Integer.parseInt(a))) {
						groupSet.remove(Integer.parseInt(a));
					}
				}

			} catch (Exception e) {
			} finally {
				text_Vbox.getChildren().remove(num - 1);
			}
		}
	}

	/**
	 * Called when the user clicks OK.
	 */
	@FXML
	public void handleOk() {
		if (isInputValid()) {
			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * 验证在 textfields中的输入是否合法.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		TextField textField;
		String errMessage = "", nunTxt;
		int num;
		for (int i = 0; i < text_Vbox.getChildren().size(); i++) {
			textField = (TextField) text_Vbox.getChildren().get(i);
			nunTxt = textField.getText();
			if (nunTxt != "") {
				try {
					num = Integer.parseInt(nunTxt);
					if (num >= 0 && num < TrajOverviewController.getTrajectoryNum())
						groupSet.add(num);
					else {
						errMessage += "第" + i + "个输入" + num + "不在成员范围内\n";
					}
				} catch (Exception e) {
					errMessage += "第" + i + "个输入不是数字\n";
				}
			} else {
				errMessage += "第" + i + "个输入为空\n";
			}
		}
		if (errMessage.length() != 0) {
			// Show the error message.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Look, there is some error!");
			alert.setContentText(errMessage);
			alert.showAndWait();
			groupSet.clear();
			return false;
		} else {
			return true;
		}
	}
}
