import java.util.Scanner;

/*
 * This will be the main class that handles the fundamentals of the game
 * Ryan Nelson
 */
public class Game
{
    public char[][] board;
    
    /*
     * Initialize the components of the board
     */
    public void startGame()
    {
        board = new char[8][8];
        // Spawn in the pawns and the empty squares
        for(int index = 0; index < 8; index++)
        {
            board[index][6] = 'p';
            board[index][5] = ' ';
            board[index][4] = ' ';
            board[index][3] = ' ';
            board[index][2] = ' ';
            board[index][1] = 'P';            
        }
        
        // Spawn in the rooks
        board[0][7] = 'r';
        board[7][7] = 'r';
        board[0][0] = 'R';
        board[7][0] = 'R';

	// Spawn in the knights
        board[1][7] = 'n';
        board[6][7] = 'n';
        board[1][0] = 'N';
        board[6][0] = 'N';

        // Spawn in the bishops
        board[2][7] = 'b';
        board[5][7] = 'b';
        board[2][0] = 'B';
        board[5][0] = 'B';

        // Spawn in the queens
        board[3][7] = 'q';
        board[4][0] = 'Q';

	// Spawn in the kings
        board[4][7] = 'k';
        board[3][0] = 'K';

        // Spawn in a test piece
        board[5][3] = 'k';
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
                System.out.print(board[column][row]);
            }
            System.out.print("    " + row + "\n");
        }
        System.out.print("\n");
        for(int index = 0; index < 8; index++)
        {
            System.out.print(index);
        }
    }

    /*
     * Checks the given coordinates to see whether a piece occupies it
     * Returns true if there is a piece, false if there is not
     */
    public boolean isOccupied(int column, int row)
    {
        return board[column][row] != ' ';
    }
    
    /*
     * Checks a piece at a given coordinate and determines what color it belongs to
     * Returns 1 if black, 0 if white, -1 if empty
     */
    public int checkTeam(int column, int row)
    {
        return 1;
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
        // Pawns
        if(board[column][row] == 'p')
        {
            if(!isOccupied(column, row - 1))
            {
                board[column][row - 1] = '*';
            }
        }
        if(board[column][row] == 'P')
        {
            if(!isOccupied(column, row + 1))
            {
                board[column][row + 1] = '*';
            }
        }
        
        // Rooks
        if(board[column][row] == 'r' || board[column][row] == 'R')
        {
            int tempColumn = column;
            int tempRow = row;
            while(!outOfBounds(tempColumn, tempRow - 1) && !isOccupied(tempColumn, tempRow - 1))
            {
                board[tempColumn][tempRow - 1] = '*';
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn, tempRow + 1) && !isOccupied(tempColumn, tempRow + 1))
            {
                board[tempColumn][tempRow + 1] = '*';
                tempRow++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow) && !isOccupied(tempColumn - 1, tempRow))
            {
                board[tempColumn - 1][tempRow] = '*';
                tempColumn--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow) && !isOccupied(tempColumn + 1, tempRow))
            {
                board[tempColumn + 1][tempRow] = '*';
                tempColumn++;
            }
        }
        
        // Bishops
        if(board[column][row] == 'b' || board[column][row] == 'B')
        {
            int tempColumn = column;
            int tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow - 1) && !isOccupied(tempColumn - 1, tempRow - 1))
            {
                board[tempColumn - 1][tempRow - 1] = '*';
                tempColumn--;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow - 1) && !isOccupied(tempColumn + 1, tempRow - 1))
            {
                board[tempColumn + 1][tempRow - 1] = '*';
                tempColumn++;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow + 1) && !isOccupied(tempColumn - 1, tempRow + 1))
            {
                board[tempColumn - 1][tempRow + 1] = '*';
                tempColumn--;
                tempRow++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow + 1) && !isOccupied(tempColumn + 1, tempRow + 1))
            {
                board[tempColumn + 1][tempRow + 1] = '*';
                tempColumn++;
                tempRow++;
            }
        }
            
        // Knights
        if(board[column][row] == 'n' || board[column][row] == 'N')
        {
            int tempColumn = column;
            int tempRow = row;
            if(!outOfBounds(tempColumn - 2, tempRow - 1) && !isOccupied(tempColumn - 2, tempRow - 1))
            {
                board[tempColumn - 2][tempRow - 1] = '*';
            }
            if(!outOfBounds(tempColumn - 1, tempRow - 2) && !isOccupied(tempColumn - 1, tempRow - 2))
            {
                board[tempColumn - 1][tempRow - 2] = '*';
            }
            if(!outOfBounds(tempColumn + 2, tempRow - 1) && !isOccupied(tempColumn + 2, tempRow - 1))
            {
                board[tempColumn + 2][tempRow - 1] = '*';
            }
            if(!outOfBounds(tempColumn + 1, tempRow - 2) && !isOccupied(tempColumn + 1, tempRow - 2))
            {
                board[tempColumn + 1][tempRow - 2] = '*';
            }
            if(!outOfBounds(tempColumn + 2, tempRow + 1) && !isOccupied(tempColumn + 2, tempRow + 1))
            {
                board[tempColumn + 2][tempRow + 1] = '*';
            }
            if(!outOfBounds(tempColumn + 1, tempRow + 2) && !isOccupied(tempColumn + 1, tempRow + 2))
            {
                board[tempColumn + 1][tempRow + 2] = '*';
            }
            if(!outOfBounds(tempColumn - 2, tempRow + 1) && !isOccupied(tempColumn - 2, tempRow + 1))
            {
                board[tempColumn - 2][tempRow + 1] = '*';
            }
            if(!outOfBounds(tempColumn - 1, tempRow + 2) && !isOccupied(tempColumn - 1, tempRow + 2))
            {
                board[tempColumn - 1][tempRow + 2] = '*';
            }
        }
        
        // Queens
        if(board[column][row] == 'q' || board[column][row] == 'Q')
        {
            int tempColumn = column;
            int tempRow = row;
            while(!outOfBounds(tempColumn, tempRow - 1) && !isOccupied(tempColumn, tempRow - 1))
            {
                board[tempColumn][tempRow - 1] = '*';
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn, tempRow + 1) && !isOccupied(tempColumn, tempRow + 1))
            {
                board[tempColumn][tempRow + 1] = '*';
                tempRow++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow) && !isOccupied(tempColumn - 1, tempRow))
            {
                board[tempColumn - 1][tempRow] = '*';
                tempColumn--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow) && !isOccupied(tempColumn + 1, tempRow))
            {
                board[tempColumn + 1][tempRow] = '*';
                tempColumn++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow - 1) && !isOccupied(tempColumn - 1, tempRow - 1))
            {
                board[tempColumn - 1][tempRow - 1] = '*';
                tempColumn--;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow - 1) && !isOccupied(tempColumn + 1, tempRow - 1))
            {
                board[tempColumn + 1][tempRow - 1] = '*';
                tempColumn++;
                tempRow--;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn - 1, tempRow + 1) && !isOccupied(tempColumn - 1, tempRow + 1))
            {
                board[tempColumn - 1][tempRow + 1] = '*';
                tempColumn--;
                tempRow++;
            }
            tempColumn = column;
            tempRow = row;
            while(!outOfBounds(tempColumn + 1, tempRow + 1) && !isOccupied(tempColumn + 1, tempRow + 1))
            {
                board[tempColumn + 1][tempRow + 1] = '*';
                tempColumn++;
                tempRow++;
            }
        }
        
        // Kings
        if(board[column][row] == 'k' || board[column][row] == 'K')
        {
            if(!isOccupied(column, row - 1))
            {
                board[column][row - 1] = '*';
            }
            if(!isOccupied(column, row + 1))
            {
                board[column][row + 1] = '*';
            }
            if(!isOccupied(column - 1, row))
            {
                board[column - 1][row] = '*';
            }
            if(!isOccupied(column + 1, row))
            {
                board[column + 1][row] = '*';
            }
            if(!isOccupied(column - 1, row - 1))
            {
                board[column - 1][row - 1] = '*';
            }
            if(!isOccupied(column + 1, row - 1))
            {
                board[column + 1][row - 1] = '*';
            }
            if(!isOccupied(column - 1, row + 1))
            {
                board[column - 1][row + 1] = '*';
            }
            if(!isOccupied(column + 1, row + 1))
            {
                board[column + 1][row + 1] = '*';
            }
        }
        
        // Highlights the current piece
        // For testing purposes only
        board[column][row] = 'X';
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
