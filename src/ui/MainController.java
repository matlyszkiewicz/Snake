/** 
 * The class manages game window and main scene.
 *
 * @author  Mateusz Łyszkiewicz
 */

package ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainController extends Application {

    static final int SCREEN_WIDTH = (int) Screen.getPrimary().getVisualBounds().getWidth();
    static final int SCREEN_HEIGHT = (int) Screen.getPrimary().getVisualBounds().getHeight();

    private static final ImageView background = new ImageView();
    private static final double BLUR_AMOUNT = 4;

    @FXML
    private StackPane mainStackPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/MainStack.fxml"));
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT, false, SceneAntialiasing.BALANCED);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setTitle("Snake");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setMaximized(true);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.show();

        background.setImage(copyBackground(primaryStage));
        background.setEffect(new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 3));
    }

    @FXML
    private void initialize() {
        loadMenuScreen();
    }

    private Image copyBackground(Stage stage) {
        final int X = (int) stage.getX();
        final int Y = (int) stage.getY();
        final int W = (int) stage.getWidth();
        final int H = (int) stage.getHeight();

        WritableImage image = new WritableImage(W, H);

        return new Robot().getScreenCapture(image, X, Y, W, H);
    }

    void loadMenuScreen() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/ui/MenuScreen.fxml"));
        Pane pane = null;

        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MenuController menuController = loader.getController();
        menuController.setMainController(this);
        setScreen(pane);
    }

    private void backgroudRefresh() {
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(background);
    }

    void setScreen(Region region) {
        backgroudRefresh();
        mainStackPane.getChildren().add(region);
    }
}
