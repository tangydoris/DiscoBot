package application;

import java.net.URL;
import java.util.ResourceBundle;

import application.util.ImageLoader;
import application.util.SerialPortUtil;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * This class is the controller class of the DiscoBot GUI specified by
 * applicaton.view.RootLayout.fxml. Communicates at 9600 baud.
 * 
 * @author Doris Tang
 *
 */

public class RootLayoutController extends AnchorPane implements Initializable {

	private Stage stage;

	@FXML
	private Button upBtn;
	@FXML
	private Button downBtn;
	@FXML
	private Button leftBtn;
	@FXML
	private Button rightBtn;

	private BooleanProperty upPressed;
	private BooleanProperty downPressed;
	private BooleanProperty leftPressed;
	private BooleanProperty rightPressed;

	private SerialPortUtil portUtil;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// insert icon images
		attachButtonGraphics();

		portUtil = new SerialPortUtil();
	}

	/**
	 * Sets the main application stage.
	 * 
	 * @param applicationStage
	 *            The main application {@link Stage}.
	 */
	public void setStage(Stage applicationStage) {
		stage = applicationStage;

		upPressed = new SimpleBooleanProperty(false);
		downPressed = new SimpleBooleanProperty(false);
		leftPressed = new SimpleBooleanProperty(false);
		rightPressed = new SimpleBooleanProperty(false);

		setButtonActions();
		bindKeyEventsToScene();
		addDirectionalBooleanListeners();
	}

	public void openPort() {
		portUtil.printSerialPortNames();
		portUtil.openPort();
	}

	public void testSerialPortWrite() {
		// change this for hardcoded testing/ connection
		portUtil.writeByteToPort(FORWARD);
	}

	public void closePort() {
		portUtil.closePort();
	}

	
	private void addDirectionalBooleanListeners() {
		upPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				boolean down, left, right;
				down = isDownPressed();
				left = isLeftPressed();
				right = isRightPressed();

				if (newValue) {
					upBtn.setGraphic(ImageLoader.getUpInv());

					if (!down && !left && !right) {
						System.out.println("0: forward-up");
					} else {
						if (down)
							upPressed.set(false);
						else if (left)
							System.out.println("2: forward left-up");
						else if (right)
							System.out.println("3: forward right-up");
					}
				} else {
					upBtn.setGraphic(ImageLoader.getUp());

					if (!down && !left && !right) {
						System.out.println("8: stop-up");
					} else if (!down) {
						if (left)
							System.out.println("7: spin left-up");
						else if (right)
							System.out.println("6: spin right-up");
					}
				}
			}
		});

		downPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				boolean up, left, right;
				up = isUpPressed();
				left = isLeftPressed();
				right = isRightPressed();

				if (newValue) {
					downBtn.setGraphic(ImageLoader.getDownInv());

					if (!up && !left && !right)
						System.out.println("1: backward-d");
					else {
						if (up)
							downPressed.set(false);
						else if (left)
							System.out.println("4: backward left-d");
						else if (right)
							System.out.println("5: backward right-d");
					}

				} else {
					downBtn.setGraphic(ImageLoader.getDown());

					if (!up && !left && !right)
						System.out.println("8: stop-d");
					else if (!up) {
						if (left)
							System.out.println("7: spin left-d");
						else if (right)
							System.out.println("6: spin right-d");
					}
				}
			}
		});

		leftPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				boolean up, down, right;
				up = isUpPressed();
				down = isDownPressed();
				right = isRightPressed();

				if (newValue) {
					leftBtn.setGraphic(ImageLoader.getLeftInv());

					if (!up && !down && !right)
						System.out.println("7: spin left-l");
					else {
						if (right)
							leftPressed.set(false);
						else if (up)
							System.out.println("2: forward left-l");
						else if (down)
							System.out.println("4: backward left-l");
					}

				} else {
					leftBtn.setGraphic(ImageLoader.getLeft());

					if (!up && !down && !right)
						System.out.println("8: stop");
					else if (!right) {
						if (up)
							System.out.println("0: forward-l");
						else if (down)
							System.out.println("1: backward-l");
					}
				}
			}
		});

		rightPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				boolean up, down, left;
				up = isUpPressed();
				down = isDownPressed();
				left = isLeftPressed();

				if (newValue) {
					rightBtn.setGraphic(ImageLoader.getRightInv());

					if (!up && !down && !left)
						System.out.println("6: spin right-r");
					else {
						if (left)
							rightPressed.set(false);
						else if (up)
							System.out.println("3: forward right-r");
						else if (down)
							System.out.println("5: backward right-r");
					}

				} else {
					rightBtn.setGraphic(ImageLoader.getRight());

					if (!up && !down && !left)
						System.out.println("8: stop-r");
					else if (!left) {
						if (up)
							System.out.println("0: forward-r");
						else if (down)
							System.out.println("1: backward-r");
					}
				}
			}
		});
	}

	/**
	 * Sends the UP command to the serial port.
	 */
	private void doUpAction() {
		upPressed.set(!upPressed.get());
	}

	/**
	 * Sends the DOWN command to the serial port.
	 */
	private void doDownAction() {
		downPressed.set(!downPressed.get());
	}

	/**
	 * Sends the LEFT command to the serial port.
	 */
	private void doLeftAction() {
		leftPressed.set(!leftPressed.get());
	}

	/**
	 * Sends the RIGHT command to the serial port.
	 */
	private void doRightAction() {
		rightPressed.set(!rightPressed.get());
	}

	/**
	 * Binds a {@link KeyEvent} to its appropriate arrow button.
	 */
	private void bindKeyEventsToScene() {
		stage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					upBtn.fire();
					break;
				case DOWN:
					downBtn.fire();
					break;
				case LEFT:
					leftBtn.fire();
					break;
				case RIGHT:
					rightBtn.fire();
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * Sets the graphic of the buttons to the appropriate {@link ImageView}.
	 */
	private void attachButtonGraphics() {
		ImageLoader.loadImages();
		upBtn.setGraphic(ImageLoader.getUp());
		downBtn.setGraphic(ImageLoader.getDown());
		leftBtn.setGraphic(ImageLoader.getLeft());
		rightBtn.setGraphic(ImageLoader.getRight());
	}

	/**
	 * Sets the onAction property of arrow buttons.
	 */
	private void setButtonActions() {
		upBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doUpAction();
			}
		});

		downBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doDownAction();
			}
		});

		leftBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doLeftAction();
			}
		});

		rightBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doRightAction();
			}
		});
	}

	private boolean isUpPressed() {
		return upPressed.get();
	}

	private boolean isDownPressed() {
		return downPressed.get();
	}

	private boolean isLeftPressed() {
		return leftPressed.get();
	}

	private boolean isRightPressed() {
		return rightPressed.get();
	}

	private final byte FORWARD = 0, BACKWARD = 1, FORWARD_LEFT = 2, FORWARD_RIGHT = 3, BACK_LEFT = 4, BACK_RIGHT = 5,
			SPIN_RIGHT = 6, SPIN_LEFT = 7, STOP = 8;

}
