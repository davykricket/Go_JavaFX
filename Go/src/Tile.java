import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**This is the Tile class for Go
 * @author David Lee
 * @author Carson Mathews
 * @version WEDIDIT
 */
public class Tile extends Label
{
    /**These 2 constant integers define the row and column
     * of the tile
     */
    public final int ROW, COL;
    /**This static integer is the turn number for the game
     */
    public static int turnNumber = 0;
    /**This static array of integers is the number of stones played by each player
     */
    public static int[] stones = {0, 0};
    /**This static array of doubles is the score for each player
     */
    public static double[] points = {0, 6.5};
    /**This static array of integers is the number of captures made by each player
     */
    public static int[] captures = {0, 0};
    /**This is the size of the board to scale the tile size to
     * The size is equal to the number of lines on the axis for the board
     */
    private int labelSize;
    /**This boolean states if a piece has just been
     * deleted or not from this tile
     */
    public boolean justDeleted = false;
    /**This is the piece in the Tile
     */
    private Piece piece;
    /**These 2 static booleans state if the tiles can be pressed
     * and if the game should save snapshots or not
     */
    public static boolean canPress, saveSnapshots = false;
    /**This boolean states if the tile is occupied by a piece
     * or not
     */
    private boolean isOccupied = false;
    /**This static Side object tells us the turns in the game
     */
    public static Side turn;

    /**This is the default constructor for the Tile class
     */
    public Tile()
    {
        this.ROW = 0;
        this.COL = 0;
        piece = null;
    }

    /**This is the constructor for the Tile class with parameters
     * @param row Row of the tile
     * @param column Column of the tile
     * @param size This is the size of the board based off the number of lines
     */
    public Tile(int row, int column, int size)
    {
        ROW = row;
        COL = column;
        piece = null;
        labelSize = size;

        setPrefSize(950/size, 800/size);

        turn = Side.BLACK;

        setOnMousePressed(e -> {
            if(!isOccupied && !justDeleted && canPress)
            {
                setPiece();
                Main.controller.setLabels(turnNumber, stones, captures, points);
                Main.controller.addMoveTurnView(new Move(turnNumber, captures, stones, points, ROW, COL, turn.equals(Side.WHITE) ? Side.BLACK : Side.WHITE));
            }
        });
    }

    /**This sets the piece in the Tile
     */
    public void setPiece()
    {
        piece = new Piece(turn, labelSize, ROW, COL);
        this.setGraphic(piece.getIcon());
        isOccupied = true;
        stones[(turn.equals(Side.WHITE) ? 1 : 0)]++;
        turnNumber++;
        Main.controller.setAllLiberties();
        turn = turn.equals(Side.WHITE) ? Side.BLACK : Side.WHITE;
        if(saveSnapshots)
        {
            WritableImage image = Main.controller.getLayout().getCenter().snapshot(new SnapshotParameters(), null);
            File file = new File("snapshot" + Main.controller.imgNum + ".png");
            try
            {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                Main.controller.imgNum++;
            }
            catch (IOException exception)
            {

            }
        }
    }

    /**This returns if the tile is occupied or not
     * @return returns if the tile is occupied or not
     */
    public boolean isOccupied()
    {
        return isOccupied;
    }

    /**This returns the piece in the tile
     * @return returns the piece in the tile
     */
    public Piece getPiece()
    {
        return piece;
    }

    /**This removes the piece in the tile
     */
    public void deletePiece()
    {
        Tile.captures[(piece.getTeam().equals(Side.WHITE) ? 0 : 1)]++;
        piece = null;
        setGraphic(null);
        for(int x = 0; x < 2; x++)
        {
            points[x] = captures[x] - captures[(x == 0) ? 1 : 0] + (x == 1 ? 6.5 : 0);
        }
        isOccupied = false;
    }
}
