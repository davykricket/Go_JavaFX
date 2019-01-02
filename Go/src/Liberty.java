/**This is the class for the liberties to the pieces
 * @author David Lee
 * @author Carson Mathews
 * @version complete
 */
public class Liberty
{
    /**This is a variable that determines the values for
     * the liberty whether it is open or not and such
     */
    private LibertyValues liberty;

    /**This provides the location on the board for the liberty
     */
    private Tile located;

    /**This is the default constructor for the game that is never used
     */
    public Liberty()
    {
        this.liberty = LibertyValues.EMPTY;
        this.located = null;
    }

    /**This is the constructor used to create the liberties
     *
     * @param value this is the value for the liberty
     * @param location this is the location on the board
     */
    public Liberty(LibertyValues value, Tile location)
    {
        liberty = value;
        located = location;
    }

    /**This is another constructor if the liberty is off the board
     * @param offBoard this is the offboard liberty value given
     */
    public Liberty(LibertyValues offBoard)
    {
        liberty = offBoard;
    }

    /**This returns the liberty value
     * @return the liberty value of the liberty object
     */
    public LibertyValues getLiberty()
    {
        return liberty;
    }

    /**This returns where the liberty is located on the board
     * @return The location of the liberty on the board
     */
    public Tile getLocated()
    {
        return located;
    }
}
