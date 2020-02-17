public class Piece
{
    private char color;
    private String type;
    private boolean hasMoved;

    public Piece()
    {
        color = 'A';
        type = "None";
        hasMoved = false;
    }

    public Piece(char newColor, String newType, boolean newHasMoved)
    {
        color = newColor;
        type = newType;
        hasMoved = newHasMoved;
    }

    public void setHasMoved(boolean newHasMoved)
    {
        hasMoved = newHasMoved;
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

    public String toString()
    {
        String output = "";
        output += getColor() + " " + getType();
        return output;
    }
}
