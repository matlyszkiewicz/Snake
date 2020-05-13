/** 
 * The class is game controller (game initialize and loop).
 *
 * @author  Mateusz Łyszkiewicz
 */

package ui;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import snake.SnakeController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static ui.MainController.SCREEN_HEIGHT;
import static ui.MainController.SCREEN_WIDTH;

public class GameController {

    private MainController mainController;
    private SnakeController snakeController;
    private AnimationTimer animationTimer;
    private Timeline timeline;
    private GraphicsContext graphicsContext;

    private static int speed;
    private static boolean speedChanging = false;

    @FXML
    private AnchorPane gamePane;

    @FXML
    private Label timeLabel;

    @FXML
    private Label actualLevelLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Text gameOver;

    @FXML
    private void initialize() {
        snakeController = new SnakeController();
        gamePane.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT + 50);
        Canvas canvas = new Canvas(SCREEN_WIDTH + 100, SCREEN_HEIGHT + 100);
        graphicsContext = canvas.getGraphicsContext2D();
        gamePane.getChildren().add(0, canvas);
        snakeController.drawUpdate(graphicsContext);

        gameListeners();
        timelineUpdater();
        gameLoop();
    }

    @FXML
    private void backToMenu() {
        animationTimer.stop();
        mainController.loadMenuScreen();
    }

    private void gameListeners() {
        gamePane.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    snakeController.setActualDirection(SnakeController.Direction.LEFT);
                    break;
                case RIGHT:
                    snakeController.setActualDirection(SnakeController.Direction.RIGHT);
                    break;
                case UP:
                    speedChanging = true;
                    speed = 10;
                    break;
            }
        });
        gamePane.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                case RIGHT:
                    snakeController.setActualDirection(SnakeController.Direction.NONE);
                    break;
                case UP:
                    speedChanging = false;
                    break;
            }
        });
    }

    private void timelineUpdater() {
        DateFormat timeFormat = new SimpleDateFormat("mm:ss");
        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(100),
                        event -> {
                            timeLabel.setText(timeFormat.format(snakeController.getGameTime()));
                            scoreLabel.setText(String.valueOf(snakeController.getScore()));
                            if (speed < 10)
                                actualLevelLabel.setText(String.valueOf(speed - 1));
                            else
                                actualLevelLabel.setText("MAX");
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void gameLoop() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!speedChanging)
                    speed = snakeController.getSize() / 10 + 2;

                if (!snakeController.moveUpdate(speed))
                    gameOver();
                snakeController.drawUpdate(graphicsContext);
            }
        };
        animationTimer.start();
    }

    private void gameOver() {
        gameOver.setVisible(true);
        timeline.stop();
        animationTimer.stop();
    }

    void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}