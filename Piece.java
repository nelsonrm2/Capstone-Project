public class Piece
{
    private char color;
    private String type;
    private boolean hasMoved;
    private boolean canCapture;

    public Piece()
    {
        color = ' ';
        type = "\t";
        hasMoved = false;
        canCapture = false;
    }

    public Piece(char newColor, String newType, boolean newHasMoved, boolean newCanCapture)
    {
        color = newColor;
        type = newType;
        hasMoved = newHasMoved;
        canCapture = newCanCapture;
    }

    public char getColor()
    {
        return color;
    }
    public String getType()
    {
        return type;
    }
    public boolean getHasMoved()
    {
        return hasMoved;
    }
    public void setCanCapture(boolean newCanCapture)
    {
        canCapture = newCanCapture;
    }
    public boolean getCanCapture()
    {
        return canCapture;
    }

    public String toString()
    {
        String output = "";
        output += getColor() + " " + getType();
        return output;
    }
}
