/** 
 * The class manages the game menu.
 *
 * @author  Mateusz Łyszkiewicz
 */

package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MenuController {

    private MainController mainController;

    @FXML
    public void initialize() {
    }

    @FXML
    private void newGame() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/GameScreen.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameController gameController = loader.getController();
        gameController.setMainController(mainController);
        mainController.setScreen(anchorPane);
    }

    @FXML
    private void exit() {
        Platform.exit();
        System.exit(0);
    }

    void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}
