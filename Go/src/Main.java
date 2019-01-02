import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;

/**
 * This is the Main class, it is where the application starts from
 * @author David Lee
 * @author Carson Mathews
 * @version completed
 */
public class Main extends Application
{

    /**
     * The mainMenu is the VBox of buttons from which the player can choose what mode he/she would like to play
     */
    VBox mainMenu;

    /**
     * This controller is the reference to the Controller class of the game
     */
    static Controller controller;

    /**
     * This method runs whenever the program starts up
     * @param primaryStage This is the main stage for the game where everything is located
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Go.fxml"));
        Parent root = loader.load();

        controller = (Controller)loader.getController();

        mainMenu = new VBox(20);
        mainMenu.setAlignment(Pos.CENTER);

        Label title = new Label("Go\nChoose a Board Size");


        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Goudy Old Style", 36));

        Button nineBoard = new Button("9x9");
        nineBoard.setOnAction(e -> {
            primaryStage.setScene(new Scene(root, 1500, 1000));
            controller.size0.setSelected(true);
            controller.setSize();
            Piece.setStaticReferences(controller.getBoard());
        });
        Button thirteenBoard = new Button("13x13");
        thirteenBoard.setOnAction(e -> {
            primaryStage.setScene(new Scene(root, 1500, 1000));
            controller.size1.setSelected(true);
            controller.setSize();
            Piece.setStaticReferences(controller.getBoard());
        });
        Button nineteenBoard = new Button("19x19");
        nineteenBoard.setOnAction(e -> {
            primaryStage.setScene(new Scene(root, 1500, 1000));
            controller.size2.setSelected(true);
            controller.setSize();
            Piece.setStaticReferences(controller.getBoard());
        });

        Button tutorialButton = new Button("Tutorial");
        tutorialButton.setOnAction(e -> {
            primaryStage.setScene(new Scene(root, 1500, 1000));
            controller.size0.setSelected(true);
            controller.setSize();
            Tile.canPress = false;
            Tile.saveSnapshots = false;
            Piece.setStaticReferences(controller.getBoard());
            controller.tutorial();
        });

        primaryStage.setTitle("Go");
        mainMenu.getChildren().addAll(title, nineBoard, thirteenBoard, nineteenBoard, tutorialButton);
        primaryStage.setScene(new Scene(mainMenu, 500, 500));
        primaryStage.setResizable(false);

        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            File folder = new File(".");
            for(File f : folder.listFiles())
            {
                if(f.getName().contains("snapshot"))
                {
                    f.delete();
                    controller.imgNum = 0;
                }
            }
        });
    }


    /**
     * This is the main method, necessary for the game to run properly
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}




