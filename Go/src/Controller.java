import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.File;

/**
 * This is the Controller class, which handles most of the general operations and capturing in the game, and is what connects to the fxml
 * @author David Lee
 * @author Carson Mathews
 * @version FINALLY
 */
public class Controller
{

    /**
     * This GridPane is the grid object that stores the board array.
     */
    @FXML
    private GridPane grid;  //9x9: Image size: width: 889 height: 756 grid size width: 1000 height: 850

    /**
     * These RadioMenuItems are the items in the MenuBar that represent each size of board (9x9, 13x13, 19x19)
     */
    @FXML
    public RadioMenuItem size0, size1, size2;

    /**
     * This BorderPane is the overall layout that contains all of the objects in the game.
     */
    @FXML
    private BorderPane layout;

    /**
     * The boardImage is the background image of the board that has the lines and dots on it.
     */
    @FXML
    private ImageView boardImage;

    /**
     * The StackPane we have is the Pane that holds 4 objects: the board, the two VBoxes for each player, and the bottom HBox with the previous move list
     */
    @FXML
    public StackPane centerPane;

    /**
     * These labels are all of the extra labels that display the information such as each player's points.
     */
    @FXML
    private Label turnNum, capture1, stones1, points1, capture2, stones2, points2;

    /**
     * This ListView of Moves is the ListView that we put so it is possible to look at all of the previous Moves in the game
     */
    @FXML
    public ListView<Move> turnView;

    /**
     * The bottom box is the HBox that stores the ListView and the label that shows the turn number
     */
    @FXML
    private HBox bottomBox;

    /**
     * This array of Tiles is the board of Go where all of the pieces are placed
     */
    private Tile[][] board;

    /**
     * This imgNum is used to determine which snapshot will be displayed when a past move is viewed.
     */
    public int imgNum;

    /**
     * Determines whether the game should end or not based on how many consecutive passes have occurred.
     */
    private int endGame = 0;

    /**
     * This int is used to determine what part of the tutorial the player is in.
     */
    private int tutorialStep = 0;

    /**
     * This is the constructor method for the class
     */
    public Controller()
    {
        File folder = new File(".");
        for(File f : folder.listFiles())
        {
            if(f.getName().contains("snapshot"))
            {
                f.delete();
                imgNum = 0;
            }
        }
    }

    /**
     * This method is simply called to close the game
     */
    public void leaveThisPlace()
    {
        System.exit(0);
    }

    /**
     * The setSize method is used to change board sizes and reset the game board
     */
    public void setSize()   //0 - 2 0 = 9x9, 1 = 13x13, 2 = 19x19
    {
        layout.setBottom(bottomBox);
        int size = size0.isSelected() ? 9 : size1.isSelected() ? 13 : size2.isSelected() ? 19 : 9;
        grid.getChildren().clear();
        Image newImage = new Image((size == 9 ? "9x9" : size == 13 ? "13x13" : size == 19 ? "19x19" : "9x9") +".png");
        boardImage.setImage(newImage);
        boardImage.setFitHeight((size == 9 ? 711.11 : size == 13 ? 736.46 : size == 19 ? 757.89 : 711.11));
        boardImage.setFitWidth((size == 9 ? 844.44 : size == 13 ? 876.92 : size == 19 ? 900 : 844.44));

        grid.getChildren().clear();
        grid = new GridPane();
        grid.setPrefSize(950, 800);
        grid.setMaxHeight(Region.USE_PREF_SIZE);
        grid.setMaxWidth(Region.USE_PREF_SIZE);
        grid.setMinHeight(Region.USE_PREF_SIZE);
        grid.setMinWidth(Region.USE_PREF_SIZE);

        board = new Tile[size][size];
        for(int x = 0; x < size; x++)
        {
            for(int y = 0; y < size; y++)
            {
                board[x][y] = new Tile(x, y, size);
                GridPane.setConstraints(board[x][y], x, y);
                grid.getChildren().add(board[x][y]);
            }
        }
        centerPane.getChildren().add(grid);
        Piece.setStaticReferences(board);

        turnNum.setText("Turn 0");
        points1.setText("Points: 0");
        points2.setText("Points: 6.5");
        stones1.setText("Stones: 0");
        stones2.setText("Stones: 0");
        capture1.setText("Captures: 0");
        capture2.setText("Captures: 0");

        Tile.turn = Side.BLACK;
        Tile.turnNumber = 0;
        for(int x = 0; x < 2; x++)
        {
            Tile.captures[x] = 0;
            Tile.points[x] = x == 1 ? 6.5 : 0;
            Tile.stones[x] = 0;
        }

        File folder = new File(".");
        for(File f : folder.listFiles())
        {
            if(f.getName().contains("snapshot"))
            {
                f.delete();
                imgNum = 0;
            }
        }

        turnView.getItems().clear();
        Tile.canPress = true;
        Tile.saveSnapshots = true;
    }

    /**
     * When called, this method returns the array of Tiles, a.k.a. the board
     * @return board is the array of Tiles where the game is played
     */
    public Tile[][] getBoard()
    {
        return board;
    }

    /**
     * This method updates all of the labels on the side of the interface when changes are made
     * @param turns this is to update what turn # the game is at
     * @param stones This array is both numbers of stones for each player
     * @param captures This array is both numbers of captures for each player
     * @param points This array is both values for points for each player
     */
    public void setLabels(int turns, int[] stones, int[] captures, double[] points)
    {
        turnNum.setText("Turn: " + turns);
        points1.setText("Points: " + points[0]);
        points2.setText("Points: " + points[1]);

        stones1.setText("Stones: " + stones[0]);
        stones2.setText("Stones: " + stones[1]);

        capture1.setText("Captures: " + captures[0]);
        capture2.setText("Captures: " + captures[1]);
    }

    /**
     * Every time a move is made, this method adds that move to the list in the ListView
     * @param nextMove This Move is the latest move being passed in
     */
    public void addMoveTurnView(Move nextMove)
    {
        turnView.getItems().add(nextMove);
    }

    /**
     * This method is how each piece calculates each of its liberties
     */
    public void setAllLiberties()
    {
        endGame = 0;
        for(Tile[] row : board)
        {
            for(Tile tile : row)
            {
                try
                {
                    tile.getPiece().shouldDelete = false;
                    tile.getPiece().setInitialLiberties();
                }
                catch(Exception NullPointerException)
                {

                }
            }
        }
        for(Tile[] row : board)
        {
            for(Tile tile : row)
            {
                try
                {
                    if(!tile.getPiece().checked)
                    {
                        if (!tile.getPiece().hasOpenLiberties())
                        {
                            if (!Piece.group.isEmpty())
                            {
                                for (Piece part : Piece.group)
                                {
                                    part.shouldDelete = true;
                                }
                            }
                            else
                                tile.getPiece().shouldDelete = true;
                        }
                        else
                        {
                            if (!Piece.group.isEmpty())
                            {
                                for (Piece part : Piece.group)
                                {
                                    part.shouldDelete = false;
                                }
                            }
                            else
                                tile.getPiece().shouldDelete = false;
                        }
                    }
                    resetBoard();
                }
                catch(Exception NullPointerException)
                {

                }
            }
        }

        for(Tile[] row : board)
        {
            for(Tile tile : row)
            {
                try
                {
                    tile.getPiece().checked = false;
                }
                catch(NullPointerException exception)
                {

                }
            }
        }
        removePieces();
    }

    /**
     * This method is what detects which pieces should be captured and removes these pieces.
     * These values are then updated in Tile under the setOnAction() method
     */
    public void removePieces()
    {
        for(Tile[] row : board)
        {
            for(Tile tile : row)
            {
                try
                {
                    if (tile.getPiece().shouldDelete && !tile.getPiece().justPlaced)
                    {
                        tile.justDeleted = true;
                        for(Liberty liberty : tile.getPiece().liberties)
                        {
                            if(liberty.getLiberty().equals(LibertyValues.TEAM) || liberty.getLiberty().equals(LibertyValues.EMPTY))
                            {
                                tile.justDeleted = false;
                            }
                        }
                        tile.deletePiece();
                    }
                    else if(tile.getPiece().shouldDelete && tile.getPiece().justPlaced)
                    {
                        int libertyCount = 0;
                        for(Liberty liberty : tile.getPiece().liberties)
                        {
                            if(liberty.getLiberty().equals(LibertyValues.ENEMY) && !liberty.getLocated().getPiece().shouldDelete || liberty.getLiberty().equals(LibertyValues.TEAM) || liberty.getLiberty().equals(LibertyValues.EMPTY))
                            {
                                libertyCount++;
                            }
                        }

                        if(libertyCount == 4)
                        {
                            tile.justDeleted = true;
                            tile.deletePiece();
                        }
                        else
                            tile.getPiece().shouldDelete = false;
                    }
                }
                catch(Exception NullPointerException)
                {

                }
            }
        }
    }

    /**
     * This method is called when the layout for the game is needed
     * @return layout This is the layout of the entire game
     */
    public BorderPane getLayout()
    {
        return layout;
    }

    /**
     * This method clears the goneTo value for the tiles and clears the recursive check groups
     */
    public void resetBoard()
    {
        Piece.group.clear();
        for(Tile[] row : board)
        {
            for (Tile tile : row)
            {
                try
                {
                    tile.getPiece().goneTo = false;
                }
                catch(NullPointerException exception)
                {

                }
            }
        }
    }

    /**
     * This method is called when the player chooses to pass and doesn't want to place a piece.
     * This includes the code to end the game once both players pass their turns
     */
    public void passTurn()
    {
        if(endGame == 0)
        {
            Tile.turn = Tile.turn.equals(Side.WHITE) ? Side.BLACK : Side.WHITE;
            Tile.turnNumber++;
            endGame++;
            turnNum.setText("Turn: " + Tile.turnNumber);
        }
        else if(endGame == 1)
        {
            Tile.points[0] = Tile.captures[0] - Tile.captures[1];
            Tile.points[1] = Tile.captures[1] - Tile.captures[0] + 6.5;

            setLabels(Tile.turnNumber, Tile.stones, Tile.captures, Tile.points);
            turnNum.setFont(new Font("Goudy Old Style", 20));

            turnNum.setText(Tile.points[0] > Tile.points[1] ? "BLACK WINS" : "WHITE WINS");
            endGame++;
            Tile.canPress = false;
        }

    }

    /**
     * This method is called when the player enters the tutorial mode, which teaches the player the basics of the game.
     */
    public void tutorial()
    {
        HBox tutorial = new HBox(10);
        tutorial.setPadding(new Insets(10, 10, 10, 10));
        Button next = new Button("Next");
        Label text = new Label("Hi! I heard you wanted to learn how to play Go. \n\n Click Next to learn more.");
        text.setFont(new Font("Goudy Old Style", 24));
        next.setFont(new Font("Goudy Old Style", 18));
        tutorial.setMinHeight(120);

        layout.setBottom(tutorial);



        next.setOnAction(e -> {
            switch (tutorialStep)
            {
                case 0:
                    tutorialStep++;
                    board[4][4].setPiece();
                    text.setText("Okay!\nThis is a piece.\nEach piece has 4 open sides (called liberties) that you must protect. They are on the top, bottom, left, and right sides.");
                    break;
                case 1:
                    tutorialStep++;
                    for(Liberty openLiberty : board[4][4].getPiece().liberties)
                    {
                        openLiberty.getLocated().setPiece();
                        Tile.turn = Side.WHITE;
                    }
                    text.setText("When all of a piece's liberties are taken by the other team, the piece is captured by the enemy.");
                    Tile.turn = Side.WHITE;
                    break;
                case 2:
                    tutorialStep++;
                    board[4][3].deletePiece();
                    for(int x = 3; x <= 4; x++)
                    {
                        try
                        {
                            Tile.turn = Side.BLACK;
                            board[4][x].setPiece();
                        }
                        catch(NullPointerException exception)
                        {

                        }
                    }
                    text.setText("But when 2 identical pieces are next to each other, their liberties are added together like a chain.\nNow it requires six white pieces to surround the 2 black pieces.");
                    break;
                case 3:
                    tutorialStep++;
                    for(int x = 2; x < 6; x++)
                    {
                        for(int y = 2; y < 7; y++)
                        {
                            board[x][y].setPiece();
                            Tile.turn = Side.WHITE;
                        }
                    }
                    text.setText("This group's liberties are located on all 4 edges of the rectangular shape.");
                    Tile.turn = Side.BLACK;
                    break;
                case 4:
                    tutorialStep++;
                    for(int x = 1; x < 7; x++)
                    {
                        for(int y = 1; y < 8; y++)
                        {
                            if(x == 1 || x == 6 || y == 1 || y == 7)
                            {
                                board[x][y].setPiece();
                                Tile.turn = Side.BLACK;
                            }
                        }
                    }
                    board[1][1].deletePiece();
                    board[1][7].deletePiece();
                    board[6][1].deletePiece();
                    board[6][7].deletePiece();
                    text.setText("When the perimeter is taken by enemy pieces like this, the entire block of white pieces is captured.\nKeep in mind that the turns switch every piece placed so this situation is highly unlikely.");
                    break;
                case 5:
                    tutorialStep++;
                    for(Tile[] row : board)
                    {
                        for (Tile tile : row)
                        {
                            try
                            {
                                tile.deletePiece();
                            }
                            catch(NullPointerException exception)
                            {

                            }
                        }
                    }
                    board[0][0].setPiece();
                    Tile.turn = Side.BLACK;
                    text.setText("If a piece is put on the edge of the board, the piece loses a liberty for each side that is adjacent to the edge.\nIn this example, the black piece only has 2 open liberties.");
                    break;
                case 6:
                    tutorialStep++;
                    board[0][0].deletePiece();
                    board[4][4].setPiece();
                    board[5][4].setPiece();
                    Tile.turn = Side.WHITE;

                    for(Liberty openLiberty : board[4][4].getPiece().liberties)
                    {
                        openLiberty.getLocated().setPiece();
                        Tile.turn = Side.WHITE;
                    }
                    Tile.turn = Side.BLACK;
                    for(Liberty openLiberty : board[5][4].getPiece().liberties)
                    {
                        openLiberty.getLocated().setPiece();
                        Tile.turn = Side.BLACK;
                    }
                    text.setText("One final rule is Ko. Ko states that you cannot perform an action that would return the game to what the last turn looked like." +
                            "\nIn this case, if white puts a piece in the one open liberty of black, black could do the same thing, and this could go on infinitely." +
                            "\nThis is the purpose of Ko: to make sure this event of infinite movements without going anywhere doesn't occur");
                    break;
                case 7:
                    tutorialStep++;
                    text.setText("The game ends once both players have passed on a turn consecutively. Points are given by the equation: Captures - Captured = Score" +
                            "\nNote this is a simplified way of the actual scoring, however it is not completely inaccurate." +
                            "\nThat's all there is to know about our version of Go! Hit the \"Next\" button to open up a 9x9 board to play.");
                    break;
                case 8:
                    Tile.canPress = true;
                    setSize();
                    break;
            }
        });
        tutorial.getChildren().addAll(text, next);
    }
}

