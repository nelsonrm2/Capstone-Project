import java.util.Scanner;

/*
 * This will be the main class that handles the fundamentals of the game
 * Ryan Nelson
 * Created 2/10/2020
 * Updated 2/17/2020
 */
public class Game
{
    public Piece[][] board;
    
    /*
     * Initialize the components of the board
     */
    public void startGame()
    {
        board = new Piece[8][8];

        // Spawn in the pawns and the empty squares
        for(int index = 0; index < 8; index++)
        {
            board[index][6] = new Piece('W', "Pawn", false, false);
            board[index][5] = new Piece();
            board[index][4] = new Piece();
            board[index][3] = new Piece();
            board[index][2] = new Piece();
            board[index][1] = new Piece('B', "Pawn", false, false);
        }
        
        // Spawn in the rooks
        board[0][7] = new Piece('W', "Rook", false, false);
        board[7][7] = new Piece('W', "Rook", false, false);
        board[0][0] = new Piece('B', "Rook", false, false);
        board[7][0] = new Piece('B', "Rook", false, false);

	// Spawn in the knights
        board[1][7] = new Piece('W', "Knight", false, false);
        board[6][7] = new Piece('W', "Knight", false, false);
        board[1][0] = new Piece('B', "Knight", false, false);
        board[6][0] = new Piece('B', "Knight", false, false);

        // Spawn in the bishops
        board[2][7] = new Piece('W', "Bishop", false, false);
        board[5][7] = new Piece('W', "Bishop", false, false);
        board[2][0] = new Piece('B', "Bishop", false, false);
        board[5][0] = new Piece('B', "Bishop", false, false);

        // Spawn in the queens
        board[3][7] = new Piece('W', "Queen", false, false);
        board[4][0] = new Piece('B', "Queen", false, false);

	// Spawn in the kings
        board[4][7] = new Piece('W', "King", false, false);
        board[3][0] = new Piece('B', "King", false, false);
    }
    
    /*
     * A simple drawing of the board for testing
     * Will be removed from the project once the GUI is complete
     */
    public void printBoard()
    {
        for(int row = 0; row < 8; row++)
        {
            for(int column = 0; column < 8; column++)
            {
                System.out.print(board[column][row].toString() + "   \t");
            }
            System.out.print("    " + row + "\n");
        }
        System.out.print("\n");
        for(int index = 0; index < 8; index++)
        {
            System.out.print("\t" + index + "\t");
        }
    }

    /*
     * Checks the given coordinates to see whether a piece occupies it
     * Returns true if there is a piece, false if there is not
     */
    public boolean isOccupied(int column, int row)
    {
        return (!isOutOfBounds(column, row) && board[column][row].getType() != "\t");
    }
    
    /*
     * Determines whether a set of coordinates is out of bounds
     * Returns true if it is not on the board, false if it is
     */
    public boolean isOutOfBounds(int column, int row)
    {
        return(column > 7 || column < 0 || row > 7 || row < 0);
    }
    
    /*
     * Prints out the available moves for a piece at a given coordinate
     * Only handles unoccupied spaces at the moment
     * TODO: Check for pieces that can be captured
     */
    public void checkMoves(int column, int row)
    {
        Piece piece = board[column][row];
        Piece open = new Piece(' ', "-\t", false, false);

        if(piece.getType() == "Pawn" && piece.getColor() == 'W')
        {
            if(!isOccupied(column, row - 1))
                board[column][row - 1] = open;
            if(isOccupied(column - 1, row - 1) && board[column - 1][row - 1].getColor() == 'B')
                board[column - 1][row - 1].setCanCapture(true);
            if(isOccupied(column + 1, row - 1) && board[column + 1][row - 1].getColor() == 'B')
                board[column + 1][row - 1].setCanCapture(true);
        }
        if(piece.getType() == "Pawn" && piece.getColor() == 'B')
        {
            if(!isOccupied(column, row + 1))
                board[column][row + 1] = open;
            if(isOccupied(column - 1, row + 1) && board[column - 1][row + 1].getColor() == 'W')
                board[column - 1][row + 1].setCanCapture(true);
            if(isOccupied(column + 1, row + 1) && board[column + 1][row + 1].getColor() == 'W')
                board[column + 1][row + 1].setCanCapture(true);
        }
        
        if(piece.getType() == "Rook")
        {
            int tempColumn = column;
            int tempRow = row;
            while(!isOutOfBounds(tempColumn, tempRow - 1) && !isOccupied(tempColumn, tempRow - 1))
            {
                board[tempColumn][tempRow - 1] = open;
                tempRow--;
            }
            if(!isOutOfBounds(tempColumn, tempRow - 1) && board[tempColumn][tempRow - 1].getColor() != piece.getColor())
                board[tempColumn][tempRow - 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn, tempRow + 1) && !isOccupied(tempColumn, tempRow + 1))
            {
                board[tempColumn][tempRow + 1] = open;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn, tempRow + 1) && board[tempColumn][tempRow + 1].getColor() != piece.getColor())
                board[tempColumn][tempRow + 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow) && !isOccupied(tempColumn - 1, tempRow))
            {
                board[tempColumn - 1][tempRow] = open;
                tempColumn--;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow) && board[tempColumn - 1][tempRow].getColor() != piece.getColor())
                board[tempColumn - 1][tempRow].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow) && !isOccupied(tempColumn + 1, tempRow))
            {
                board[tempColumn + 1][tempRow] = open;
                tempColumn++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow) && board[tempColumn + 1][tempRow].getColor() != piece.getColor())
                board[tempColumn + 1][tempRow].setCanCapture(true);
        }
        
        if(piece.getType() == "Bishop")
        {
            int tempColumn = column;
            int tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow - 1) && !isOccupied(tempColumn - 1, tempRow - 1))
            {
                board[tempColumn - 1][tempRow - 1] = open;
                tempColumn--;
                tempRow--;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow - 1) && board[tempColumn - 1][tempRow - 1].getColor() != piece.getColor())
                board[tempColumn - 1][tempRow - 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow - 1) && !isOccupied(tempColumn + 1, tempRow - 1))
            {
                board[tempColumn + 1][tempRow - 1] = open;
                tempColumn++;
                tempRow--;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow - 1) && board[tempColumn + 1][tempRow - 1].getColor() != piece.getColor())
                board[tempColumn + 1][tempRow - 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow + 1) && !isOccupied(tempColumn - 1, tempRow + 1))
            {
                board[tempColumn - 1][tempRow + 1] = open;
                tempColumn--;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow + 1) && board[tempColumn - 1][tempRow + 1].getColor() != piece.getColor())
                board[tempColumn - 1][tempRow + 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow + 1) && !isOccupied(tempColumn + 1, tempRow + 1))
            {
                board[tempColumn + 1][tempRow + 1] = open;
                tempColumn++;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow + 1) && board[tempColumn + 1][tempRow + 1].getColor() != piece.getColor())
                board[tempColumn + 1][tempRow + 1].setCanCapture(true);
        }
            
        if(piece.getType() == "Knight")
        {
            int tempColumn = column;
            int tempRow = row;
            if(!isOutOfBounds(tempColumn - 2, tempRow - 1) && !isOccupied(tempColumn - 2, tempRow - 1))
                board[tempColumn - 2][tempRow - 1] = open;
            if(!isOutOfBounds(tempColumn - 2, tempRow - 1) && board[tempColumn - 2][tempRow - 1].getColor() != piece.getColor())
                board[tempColumn - 2][tempRow - 1].setCanCapture(true);
            if(!isOutOfBounds(tempColumn - 1, tempRow - 2) && !isOccupied(tempColumn - 1, tempRow - 2))
                board[tempColumn - 1][tempRow - 2] = open;
            if(!isOutOfBounds(tempColumn - 1, tempRow - 2) && board[tempColumn - 1][tempRow - 2].getColor() != piece.getColor())
                board[tempColumn - 1][tempRow - 2].setCanCapture(true);
            if(!isOutOfBounds(tempColumn + 2, tempRow - 1) && !isOccupied(tempColumn + 2, tempRow - 1))
                board[tempColumn + 2][tempRow - 1] = open;
            if(!isOutOfBounds(tempColumn + 2, tempRow - 1) && board[tempColumn + 2][tempRow - 1].getColor() != piece.getColor())
                board[tempColumn + 2][tempRow - 1].setCanCapture(true);
            if(!isOutOfBounds(tempColumn + 1, tempRow - 2) && !isOccupied(tempColumn + 1, tempRow - 2))
                board[tempColumn + 1][tempRow - 2] = open;
            if(!isOutOfBounds(tempColumn + 1, tempRow - 2) && board[tempColumn + 1][tempRow - 2].getColor() != piece.getColor())
                board[tempColumn + 1][tempRow - 2].setCanCapture(true);
            if(!isOutOfBounds(tempColumn + 2, tempRow + 1) && !isOccupied(tempColumn + 2, tempRow + 1))
                board[tempColumn + 2][tempRow + 1] = open;
            if(!isOutOfBounds(tempColumn + 2, tempRow + 1) && board[tempColumn + 2][tempRow + 1].getColor() != piece.getColor())
                board[tempColumn + 2][tempRow + 1].setCanCapture(true);
            if(!isOutOfBounds(tempColumn + 1, tempRow + 2) && !isOccupied(tempColumn + 1, tempRow + 2))
                board[tempColumn + 1][tempRow + 2] = open;
            if(!isOutOfBounds(tempColumn + 1, tempRow + 2) && board[tempColumn + 1][tempRow + 2].getColor() != piece.getColor())
                board[tempColumn + 1][tempRow + 2].setCanCapture(true);
            if(!isOutOfBounds(tempColumn - 2, tempRow + 1) && !isOccupied(tempColumn - 2, tempRow + 1))
                board[tempColumn - 2][tempRow + 1] = open;
            if(!isOutOfBounds(tempColumn - 2, tempRow + 1) && board[tempColumn - 2][tempRow + 1].getColor() != piece.getColor())
                board[tempColumn - 2][tempRow + 1].setCanCapture(true);
            if(!isOutOfBounds(tempColumn - 1, tempRow + 2) && !isOccupied(tempColumn - 1, tempRow + 2))
                board[tempColumn - 1][tempRow + 2] = open;
            if(!isOutOfBounds(tempColumn - 1, tempRow + 2) && board[tempColumn - 1][tempRow + 2].getColor() != piece.getColor())
                board[tempColumn - 1][tempRow + 2].setCanCapture(true);
        }
        
        if(piece.getType() == "Queen")
        {
            int tempColumn = column;
            int tempRow = row;
            while(!isOutOfBounds(tempColumn, tempRow - 1) && !isOccupied(tempColumn, tempRow - 1))
            {
                board[tempColumn][tempRow - 1] = open;
                tempRow--;
            }
            if(!isOutOfBounds(tempColumn, tempRow - 1) && board[tempColumn][tempRow - 1].getColor() != piece.getColor())
                board[tempColumn][tempRow - 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn, tempRow + 1) && !isOccupied(tempColumn, tempRow + 1))
            {
                board[tempColumn][tempRow + 1] = open;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn, tempRow + 1) && board[tempColumn][tempRow + 1].getColor() != piece.getColor())
                board[tempColumn][tempRow + 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow) && !isOccupied(tempColumn - 1, tempRow))
            {
                board[tempColumn - 1][tempRow] = open;
                tempColumn--;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow) && board[tempColumn - 1][tempRow].getColor() != piece.getColor())
                board[tempColumn - 1][tempRow].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow) && !isOccupied(tempColumn + 1, tempRow))
            {
                board[tempColumn + 1][tempRow] = open;
                tempColumn++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow) && board[tempColumn + 1][tempRow].getColor() != piece.getColor())
                board[tempColumn + 1][tempRow].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow - 1) && !isOccupied(tempColumn - 1, tempRow - 1))
            {
                board[tempColumn - 1][tempRow - 1] = open;
                tempColumn--;
                tempRow--;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow - 1) && board[tempColumn - 1][tempRow -1].getColor() != piece.getColor())
                board[tempColumn - 1][tempRow - 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow - 1) && !isOccupied(tempColumn + 1, tempRow - 1))
            {
                board[tempColumn + 1][tempRow - 1] = open;
                tempColumn++;
                tempRow--;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow - 1) && board[tempColumn + 1][tempRow - 1].getColor() != piece.getColor())
                board[tempColumn + 1][tempRow - 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow + 1) && !isOccupied(tempColumn - 1, tempRow + 1))
            {
                board[tempColumn - 1][tempRow + 1] = open;
                tempColumn--;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow + 1) && board[tempColumn - 1][tempRow + 1].getColor() != piece.getColor())
                board[tempColumn - 1][tempRow + 1].setCanCapture(true);
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow + 1) && !isOccupied(tempColumn + 1, tempRow + 1))
            {
                board[tempColumn + 1][tempRow + 1] = open;
                tempColumn++;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow + 1) && board[tempColumn + 1][tempRow + 1].getColor() != piece.getColor())
                board[tempColumn + 1][tempRow + 1].setCanCapture(true);
        }
        
        if(piece.getType() == "King")
        {
            if(!isOutOfBounds(column, row - 1) && !isOccupied(column, row - 1))
                board[column][row - 1] = open;
            if(!isOutOfBounds(column, row - 1) && board[column][row - 1].getColor() != piece.getColor())
                board[column][row - 1].setCanCapture(true);
            if(!isOutOfBounds(column, row + 1) && !isOccupied(column, row + 1))
                board[column][row + 1] = open;
            if(!isOutOfBounds(column, row + 1) && board[column][row + 1].getColor() != piece.getColor())
                board[column][row + 1].setCanCapture(true);
            if(!isOutOfBounds(column - 1, row) && !isOccupied(column - 1, row))
                board[column - 1][row] = open;
            if(!isOutOfBounds(column - 1, row) && board[column - 1][row].getColor() != piece.getColor())
                board[column - 1][row].setCanCapture(true);
            if(!isOutOfBounds(column + 1, row) && !isOccupied(column + 1, row))
                board[column + 1][row] = open;
            if(!isOutOfBounds(column + 1, row) && board[column + 1][row].getColor() != piece.getColor())
                board[column + 1][row].setCanCapture(true);
            if(!isOutOfBounds(column - 1, row - 1) && !isOccupied(column - 1, row - 1))
                board[column - 1][row - 1] = open;
            if(!isOutOfBounds(column - 1, row - 1) && board[column - 1][row - 1].getColor() != piece.getColor())
                board[column - 1][row - 1].setCanCapture(true);
            if(!isOutOfBounds(column + 1, row - 1) && !isOccupied(column + 1, row - 1))
                board[column + 1][row - 1] = open;
            if(!isOutOfBounds(column + 1, row - 1) && board[column + 1][row - 1].getColor() != piece.getColor())
                board[column + 1][row - 1].setCanCapture(true);
            if(!isOutOfBounds(column - 1, row + 1) && !isOccupied(column - 1, row + 1))
                board[column - 1][row + 1] = open;
            if(!isOutOfBounds(column - 1, row + 1) && board[column - 1][row + 1].getColor() != piece.getColor())
                board[column - 1][row + 1].setCanCapture(true);
            if(!isOutOfBounds(column + 1, row + 1) && !isOccupied(column + 1, row + 1))
                board[column + 1][row + 1] = open;
            if(!isOutOfBounds(column + 1, row + 1) && board[column + 1][row + 1].getColor() != piece.getColor())
                board[column + 1][row + 1].setCanCapture(true);
        }
        
        // Highlights the current piece
        // For testing purposes only
        piece = new Piece('X', piece.getType(), piece.getHasMoved(), piece.getCanCapture());
    }

    public void clearBoard()
    {
        for(int column = 0; column < 8; column++)
        {
            for(int row = 0; row < 8; row++)
            {
                if(board[column][row].getType()== "-\t")
                    board[column][row] = new Piece(' ', "\t", false, false);
                board[column][row].setCanCapture(false);
            }
        }
    }

    public int move(int currColumn, int currRow, int newColumn, int newRow)
    {
        checkMoves(currColumn, currRow);
        Piece current = board[currColumn][currRow];
        Piece destination = board[newColumn][newRow];

        if(destination.getType()== "-\t" || destination.getCanCapture())
        {
            board[newColumn][newRow] = current;
            board[currColumn][currRow] = new Piece(' ', "\t", true, false);
            clearBoard();
            return 0;
        }
        else
        {
            clearBoard();
            return -1;
        }
    }
    
    public static void main(String[] args)
    {
        Game g = new Game();
        g.startGame();
        g.printBoard();
        Scanner keyboard = new Scanner(System.in);
        while(true)
        {
            System.out.println("\nSelect a piece");
            int currColumn = keyboard.nextInt();
            int currRow = keyboard.nextInt();
            System.out.println("\nWhere will the " + g.board[currColumn][currRow].getType() + " move?");
            g.checkMoves(currColumn, currRow);
            g.printBoard();
            System.out.print("\n");
            int newColumn = keyboard.nextInt();
            int newRow = keyboard.nextInt();
            if(g.move(currColumn, currRow, newColumn, newRow) == -1)
            {
                System.out.println("That move cannot be made");
                g.printBoard();
            }
            else
            {
                g.printBoard();
            }
        }
    }
}
