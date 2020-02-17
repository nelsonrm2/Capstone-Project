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
        Piece empty = new Piece('O', "Empty", false);
        System.out.print(empty.toString() + "\n\n\n");

        Piece blank = new Piece();
        System.out.print(blank.toString() + "\n\n\n");

        // Spawn in the pawns and the empty squares
        for(int index = 0; index < 8; index++)
        {
            board[index][6] = new Piece('W', "Pawn", false);
            board[index][5] = empty;
            board[index][4] = empty;
            board[index][3] = empty;
            board[index][2] = empty;
            board[index][1] = new Piece('B', "Pawn", false);
        }
        
        // Spawn in the rooks
        board[0][7] = new Piece('W', "Rook", false);
        board[7][7] = new Piece('W', "Rook", false);
        board[0][0] = new Piece('B', "Rook", false);
        board[7][0] = new Piece('B', "Rook", false);

	// Spawn in the knights
        board[1][7] = new Piece('W', "Knight", false);
        board[6][7] = new Piece('W', "Knight", false);
        board[1][0] = new Piece('B', "Knight", false);
        board[6][0] = new Piece('B', "Knight", false);

        // Spawn in the bishops
        board[2][7] = new Piece('W', "Bishop", false);
        board[5][7] = new Piece('W', "Bishop", false);
        board[2][0] = new Piece('B', "Bishop", false);
        board[5][0] = new Piece('B', "Bishop", false);

        // Spawn in the queens
        board[3][7] = new Piece('W', "Queen", false);
        board[4][0] = new Piece('B', "Queen", false);

	// Spawn in the kings
        board[4][7] = new Piece('W', "King", false);
        board[3][0] = new Piece('B', "King", false);

        // Spawn in a test piece
        board[5][3] = new Piece('W', "King", false);
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
        return board[column][row].getType() != "Empty";
    }
    
    /*
     * Determines whether a set of coordinates is out of bounds
     * Returns true if it is not on the board, false if it is
     */
    public boolean outOfBounds(int column, int row)
    {
        return(column > 7 || column < 0 || row > 7 || row < 0);
    }
    
    /*
     * Prints out the available moves for a piece at a given coordinate
     * Only handles unoccupied spaces at the moment
     */
    public void checkMoves(int column, int row)
    {
        Piece piece = board[column][row];
        Piece open = new Piece('X', "Available", false);

        if(piece.getType() == "Pawn" && piece.getColor() == 'W')
        {
            if(!isOccupied(column, row - 1))
            {
                board[column][row - 1] = open;
            }
        }
        if(piece.getType() == "Pawn" && piece.getColor() == 'B')
        {
            if(!isOccupied(column, row + 1))
            {
                board[column][row + 1] = open;
            }
        }
        
        if(piece.getType() == "Rook")
        {
            int tempColumn = column;
            int tempRow = row;
            while(!outOfBounds(tempColumn, tempRow - 1) && !isOccupied(tempColumn, tempRow - 1))
            {
                board[tempColumn][tempRow - 1] = open;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn, tempRow + 1) && !isOccupied(tempColumn, tempRow + 1))
            {
                board[tempColumn][tempRow + 1] = open;
                tempRow++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow) && !isOccupied(tempColumn - 1, tempRow))
            {
                board[tempColumn - 1][tempRow] = open;
                tempColumn--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow) && !isOccupied(tempColumn + 1, tempRow))
            {
                board[tempColumn + 1][tempRow] = open;
                tempColumn++;
            }
        }
        
        if(piece.getType() == "Bishop")
        {
            int tempColumn = column;
            int tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow - 1) && !isOccupied(tempColumn - 1, tempRow - 1))
            {
                board[tempColumn - 1][tempRow - 1] = open;
                tempColumn--;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow - 1) && !isOccupied(tempColumn + 1, tempRow - 1))
            {
                board[tempColumn + 1][tempRow - 1] = open;
                tempColumn++;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow + 1) && !isOccupied(tempColumn - 1, tempRow + 1))
            {
                board[tempColumn - 1][tempRow + 1] = open;
                tempColumn--;
                tempRow++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow + 1) && !isOccupied(tempColumn + 1, tempRow + 1))
            {
                board[tempColumn + 1][tempRow + 1] = open;
                tempColumn++;
                tempRow++;
            }
        }
            
        if(piece.getType() == "Knight")
        {
            int tempColumn = column;
            int tempRow = row;
            if(!outOfBounds(tempColumn - 2, tempRow - 1) && !isOccupied(tempColumn - 2, tempRow - 1))
            {
                board[tempColumn - 2][tempRow - 1] = open;
            }
            if(!outOfBounds(tempColumn - 1, tempRow - 2) && !isOccupied(tempColumn - 1, tempRow - 2))
            {
                board[tempColumn - 1][tempRow - 2] = open;
            }
            if(!outOfBounds(tempColumn + 2, tempRow - 1) && !isOccupied(tempColumn + 2, tempRow - 1))
            {
                board[tempColumn + 2][tempRow - 1] = open;
            }
            if(!outOfBounds(tempColumn + 1, tempRow - 2) && !isOccupied(tempColumn + 1, tempRow - 2))
            {
                board[tempColumn + 1][tempRow - 2] = open;
            }
            if(!outOfBounds(tempColumn + 2, tempRow + 1) && !isOccupied(tempColumn + 2, tempRow + 1))
            {
                board[tempColumn + 2][tempRow + 1] = open;
            }
            if(!outOfBounds(tempColumn + 1, tempRow + 2) && !isOccupied(tempColumn + 1, tempRow + 2))
            {
                board[tempColumn + 1][tempRow + 2] = open;
            }
            if(!outOfBounds(tempColumn - 2, tempRow + 1) && !isOccupied(tempColumn - 2, tempRow + 1))
            {
                board[tempColumn - 2][tempRow + 1] = open;
            }
            if(!outOfBounds(tempColumn - 1, tempRow + 2) && !isOccupied(tempColumn - 1, tempRow + 2))
            {
                board[tempColumn - 1][tempRow + 2] = open;
            }
        }
        
        if(piece.getType() == "Queen")
        {
            int tempColumn = column;
            int tempRow = row;
            while(!outOfBounds(tempColumn, tempRow - 1) && !isOccupied(tempColumn, tempRow - 1))
            {
                board[tempColumn][tempRow - 1] = open;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn, tempRow + 1) && !isOccupied(tempColumn, tempRow + 1))
            {
                board[tempColumn][tempRow + 1] = open;
                tempRow++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow) && !isOccupied(tempColumn - 1, tempRow))
            {
                board[tempColumn - 1][tempRow] = open;
                tempColumn--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow) && !isOccupied(tempColumn + 1, tempRow))
            {
                board[tempColumn + 1][tempRow] = open;
                tempColumn++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow - 1) && !isOccupied(tempColumn - 1, tempRow - 1))
            {
                board[tempColumn - 1][tempRow - 1] = open;
                tempColumn--;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow - 1) && !isOccupied(tempColumn + 1, tempRow - 1))
            {
                board[tempColumn + 1][tempRow - 1] = open;
                tempColumn++;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow + 1) && !isOccupied(tempColumn - 1, tempRow + 1))
            {
                board[tempColumn - 1][tempRow + 1] = open;
                tempColumn--;
                tempRow++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow + 1) && !isOccupied(tempColumn + 1, tempRow + 1))
            {
                board[tempColumn + 1][tempRow + 1] = open;
                tempColumn++;
                tempRow++;
            }
        }
        
        if(piece.getType() == "King")
        {
            if(!isOccupied(column, row - 1))
            {
                board[column][row - 1] = open;
            }
            if(!isOccupied(column, row + 1))
            {
                board[column][row + 1] = open;
            }
            if(!isOccupied(column - 1, row))
            {
                board[column - 1][row] = open;
            }
            if(!isOccupied(column + 1, row))
            {
                board[column + 1][row] = open;
            }
            if(!isOccupied(column - 1, row - 1))
            {
                board[column - 1][row - 1] = open;
            }
            if(!isOccupied(column + 1, row - 1))
            {
                board[column + 1][row - 1] = open;
            }
            if(!isOccupied(column - 1, row + 1))
            {
                board[column - 1][row + 1] = open;
            }
            if(!isOccupied(column + 1, row + 1))
            {
                board[column + 1][row + 1] = open;
            }
        }
        
        // Highlights the current piece
        // For testing purposes only
        piece = new Piece('X', piece.getType(), piece.getHasMoved());
    }
    
    public static void main(String[] args)
    {
        Game g = new Game();
        g.startGame();
        g.printBoard();
        Scanner keyboard = new Scanner(System.in);
        System.out.println("\nPlease enter a coordinate point");
        int column = keyboard.nextInt();
        int row = keyboard.nextInt();
        g.checkMoves(column, row);
        g.printBoard();
    }
}
