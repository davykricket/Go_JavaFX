import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * This is the Move class, it is the object stored in each spot in the ListView where the player can view each past Move
 * @author David Lee
 * @author Carson Mathews
 * @version WEDIDIT
 */
public class Move extends VBox
{

    /**
     * This int represents what turn the game is on currently
     */
    private int turnNum;

    /**
     * This int array stores the number of captures for both players
     */
    private int[] captures = {0, 0};

    /**
     * This double array stores the number of points for both players
     */
    private double[] points = {0, 0};

    /**
     * This int array stores the number of stones for each player
     */
    private int[] stones = {0, 0};

    /**
     * These 2 ints represent where the last piece was placed in each move
     */
    private int moveX, moveY;

    /**
     * This represents what side was the side during a certain turn
     */
    private Side side;

    /**
     * These labels are the parts of the Move that display the values for each past move
     */
    private Label stoneDisplay, turnDisplay, pointsDisplay, capturesDisplay, moveDisplay;

    /**
     * This boolean determines if a certain move in the ListView is selected or not.
     */
    private boolean isPressed = false;

    /**
     * This ImageView is the snapshot created by each move
     */
    private ImageView image;

    /**
     * This is the constructor method for the class
     * @param turnNumber This int is the turn value stored in the move
     * @param caps This int array stores the number of caps for each player in the Move
     * @param stons This int array stores the number of stones for each player in the Move
     * @param pnts This double array stores the number of points for each player in the Move
     * @param x This int is the x-value of the last piece placed on the board
     * @param y This int is the y-value of the last piece placed on the board
     * @param currentSide This Side represents the current side for the respective Move
     */
    public Move(int turnNumber, int[] caps, int[] stons, double[] pnts, int x, int y, Side currentSide)
    {
        turnNum = turnNumber;
        captures = caps;
        stones = stons;
        points = pnts;
        moveX = x;
        moveY = y;
        side = currentSide;

        setPadding(new Insets(10, 10, 10, 10));

        turnDisplay = new Label("Turn " + turnNum + "\tSide: " + side);
        turnDisplay.setFont(Font.font("Goudy Old Style", 14));

        stoneDisplay = new Label("Stones:\tB: " + stones[0] + " W: " + stones[1]);
        stoneDisplay.setFont(Font.font("Goudy Old Style", 14));

        capturesDisplay = new Label("Captures:\tB: " + captures[0] + " W: " + captures[1]);
        capturesDisplay.setFont(Font.font("Goudy Old Style", 14));

        pointsDisplay = new Label("Points:\tB: " + points[0] + " W: " + points[1]);
        pointsDisplay.setFont(Font.font("Goudy Old Style", 14));

        moveDisplay = new Label("Move to: (" + moveX + ", " + moveY + ")");
        moveDisplay.setFont(Font.font("Goudy Old Style", 14));

        getChildren().addAll(turnDisplay, stoneDisplay, capturesDisplay, pointsDisplay, moveDisplay);

        setOnMouseClicked(e -> {
            this.isPressed = true;
            if(Main.controller.turnView.getSelectionModel().getSelectedItem().isPressed)
            {
                String url = "file:snapshot" + (turnNum - 1) + ".png";
                image = new ImageView(url);
                image.setFitHeight(800);
                image.setFitWidth(950);
                Main.controller.getLayout().setCenter(image);
                this.isPressed = false;

                Main.controller.setLabels(turnNum, stones, captures, points);

                if(Main.controller.turnView.getItems().get(Tile.turnNumber - 1).equals(Main.controller.turnView.getSelectionModel().getSelectedItem()))
                {
                    image.setImage(null);
                    Main.controller.getLayout().setCenter(Main.controller.centerPane);
                }
            }
        });


    }
}




