import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**This is the Piece class for our Go project
 * @author David Lee
 * @author Carson Mathews
 * @version WEDIDIT
 */
public class Piece
{
    /**This is the Side variable for which team the piece is on
     */
    private Side team;
    /**This is the image for the piece
     */
    private ImageView icon;
    /**This array of Liberties is the list of all
     * liberties for the piece
     */
    public Liberty liberties[] = new Liberty[4];
    /**This is the white image for the piece
     */
    private final ImageView WHITE = new ImageView("white.png");
    /**This is the black image for the piece
     */
    private final ImageView BLACK = new ImageView("black.png");
    /**This is the static reference to the double array of Tiles that is the board to be used
     */
    public static Tile[][] board;
    /**These 3 variables tell if:
     * if we should delete the piece
     * if the piece has already been checked when we've recursively checked
     * and if the piece was just placed onto the board
     */
    public boolean shouldDelete, checked, justPlaced = false;
    /**These 2 constant integers define the row and column of the piece
     */
    private final int ROW, COL;
    /**This static ArrayList of pieces is used to find the
     * groups of like team pieces that share liberties
     */
    public static ArrayList<Piece> group = new ArrayList<>();
    /**This boolean says if the piece has been gone to
     * when we check recursively or not
     */
    public boolean goneTo = false;

    /**This is the default constructor of Piece that is never used
     */
    public Piece()
    {
        this.team = Side.BLACK;
        ROW = 0;
        COL = 0;
        icon = BLACK;
    }

    /**This is the constructor used for the Piece class
     * @param side Piece's team
     * @param size The size of the board in terms of numbers of lines
     * @param r The row of the piece
     * @param c The column of the piece
     */
    public Piece(Side side, int size, int r, int c)
    {

            for(Tile[] row : board)
            {
                for(Tile tile : row)
                {
                    tile.justDeleted = false;
                    try
                    {
                        tile.getPiece().justPlaced = false;
                    }
                    catch(NullPointerException exception)
                    {

                    }
                }
            }
        team = side;
        icon = side.equals(Side.WHITE) ? WHITE : BLACK;

        ROW = r;
        COL = c;

        this.justPlaced = true;

        icon.setFitWidth(950/size);
        icon.setFitHeight(800/size);
    }

    /**This sets the static board reference for the Piece class
     * @param tiles The board of the game
     */
    public static void setStaticReferences(Tile[][] tiles)
    {
        board = tiles;
    }

    /**This returns the team the piece is on
     * @return The team the piece is on
     */
    public Side getTeam()
    {
        return team;
    }

    /**This returns the image of the piece
     * @return The piece's image
     */
    public ImageView getIcon()
    {
        return icon;
    }

    /**This method sets all of the liberties for the piece
     * to then use to find if there are open liberties or not
     */
    public void setInitialLiberties()
    {
        //0-3 clockwise from the top (up = 0, left = 3, etc.)
        for(int x = -1; x <= 1; x++)
        {
            for(int y = -1; y <= 1; y++)
            {
                try
                {
                    try
                    {
                        if (Math.abs(x) != Math.abs(y))  //Checks for diagonals
                        {
                            if (board[ROW + x][COL + y].isOccupied() && board[ROW + x][COL + y].getPiece().team != this.team)
                            {
                                if (x == 0)
                                {
                                    liberties[(y == 1 ? 0 : y == -1 ? 2 : 0)] = new Liberty(LibertyValues.ENEMY, board[ROW + x][COL + y]);
                                }
                                else if (y == 0)
                                {
                                    liberties[(x == 1 ? 1 : x == -1 ? 3 : 1)] = new Liberty(LibertyValues.ENEMY, board[ROW + x][COL + y]);
                                }
                            }
                            else if (!(board[ROW + x][COL + y].isOccupied()))
                            {
                                if (x == 0)
                                {
                                    liberties[(y == 1 ? 0 : y == -1 ? 2 : 0)] = new Liberty(LibertyValues.EMPTY, board[ROW + x][COL + y]);
                                }
                                else if (y == 0)
                                {
                                    liberties[(x == 1 ? 1 : x == -1 ? 3 : 1)] = new Liberty(LibertyValues.EMPTY, board[ROW + x][COL + y]);
                                }
                            }
                            else if (board[ROW + x][COL + y].isOccupied() && board[ROW + x][COL + y].getPiece().team == this.team)
                            {
                                if (x == 0)
                                {
                                    liberties[(y == 1 ? 0 : y == -1 ? 2 : 0)] = new Liberty(LibertyValues.TEAM, board[ROW + x][COL + y]);
                                }
                                else if (y == 0)
                                {
                                    liberties[(x == 1 ? 1 : x == -1 ? 3 : 1)] = new Liberty(LibertyValues.TEAM, board[ROW + x][COL + y]);
                                }
                            }
                        }
                    }
                    catch(Exception IndexOutOfBoundsException)
                    {
                        if (x == 0)
                        {
                            liberties[(y == 1 ? 0 : y == -1 ? 2 : 0)] = new Liberty(LibertyValues.OFFBOARD);
                        }
                        else if (y == 0)
                        {
                            liberties[(x == 1 ? 1 : x == -1 ? 3 : 1)] = new Liberty(LibertyValues.OFFBOARD);
                        }
                    }
                }
                catch(Exception NullPointerException)
                {

                }
            }
        }
    }

    /**This method checks if there are any open liberties
     * and recursively check through pieces of the same team
     * to see if these pieces should be deleted or not
     * @return This is if the pieces should be deleted or not
     */
    public boolean hasOpenLiberties()
    {
   /*
   Check through every piece to find if there's an empty square or not
    */
        boolean temp = false;
        if(!shouldDelete)
        {
            this.checked = true;
            for (Liberty liberty : liberties)
            {
                switch (liberty.getLiberty())
                {
                    case TEAM:
                        group.add(liberty.getLocated().getPiece());
                        this.goneTo = true;
                        if(!liberty.getLocated().getPiece().goneTo)
                            temp = temp ? true : liberty.getLocated().getPiece().hasOpenLiberties();
                        break;
                    case EMPTY:
                        temp = true;
                        break;
                    case ENEMY:
                        temp = temp ? true : false;
                        break;
                    case OFFBOARD:
                        temp = temp ? true : false;
                        break;
                }
            }
        }
        if(justPlaced)
        {
            temp = temp ? true : false;
        }
        return temp;
    }
}
