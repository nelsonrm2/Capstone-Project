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

    public Piece(char color, String type, boolean hasMoved, boolean canCapture)
    {
        this.color = color;
        this.type = type;
        this.hasMoved = hasMoved;
        this.canCapture = canCapture;
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
    public void setHasMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }
    public boolean getCanCapture()
    {
        return canCapture;
    }
    public void setCanCapture(boolean canCapture)
    {
        this.canCapture = canCapture;
    }

    public String toString()
    {
        String output = "";
        output += getColor() + " " + getType();
        return output;
    }
}
