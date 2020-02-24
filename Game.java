import java.util.Scanner;

/*
 * This will be the main class that handles the fundamentals of the game
 * Ryan Nelson
 * Created 2/10/2020
 * Last updated 2/24/2020
 */
public class Game
{
    public Piece[][] board;	// Handles the board and all the pieces on it
    public int[][] capturable;	// Handles a list of coordinates to pieces that can be captured
    public int captureCount;	// Handles the number of pieces that can be captured
    public boolean whiteMove;	// Handles which player's turn it is
    public boolean kingInCheck;	// Handles whether or not the king is in danger
    public int[][] checkers;	// Handles a list of coordinates to pieces that can attack the king
    public int checkCount;	// Handles the number of pieces that can attack the king
    
    /*
     * Initialize the components of the board
     * Creates a unique object that handles each piece
     * White takes the bottom two rows, black takes the top two
     */
    public void startGame()
    {
        board = new Piece[8][8];
        capturable = new int[10][2];
        whiteMove = true;
        kingInCheck = false;

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
     * Serves as a placeholder for testing until the GUI is complete
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
     * Wipes away any leftovers from checkMoves()
     * Sets the canCapture attribute to false for all pieces on the board
     * Clears the '-' from any highlighted spaces
     * Deletes any coordinates from the capturable array
     */
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
        for(int index = 0; index < captureCount; index++)
        {
            capturable[index][0] = 0;
            capturable[index][1] = 0;
        }
        for(int index = 0; index < checkCount; index++)
        {
            checkers[index][0] = 0;
            checkers[index][1] = 0;
        }
        captureCount = 0;
        checkCount = 0;
    }

    /*
     * Scans through each piece to determine if the king is in danger
     */
    public void checkForCheck()
    {
        for(int column = 0; column < 8; column++)
        {
            for(int row = 0; row < 8; row++)
            {
                Piece current = board[column][row];
                if(current.getType() != "\t")
                {
                    checkMoves(column, row);
                    for(int index = 0; index < captureCount; index++)
                    {
                        int currColumn = capturable[index][0];
                        int currRow = capturable[index][1];
                        if(board[currColumn][currRow].getType() == "King")
                        {
                            kingInCheck = true;
                            checkers[checkCount][0] = column;
                            checkers[checkCount][1] = row;
                            checkCount++;
                        }
                    }
                    clearBoard();
                }
            }
        }
    }
    
    /*
     * Prints out the available moves for a piece at a given coordinate
     * Valid unoccupied spaces are highlighted with a '-'
     * If an enemy piece is in a valid space, it is signified by the canCapture attribute
     */
    public void checkMoves(int column, int row)
    {
        Piece piece = board[column][row];
        Piece open = new Piece(' ', "-\t", false, false);

        // Handle moves for black and white pawns
        if(piece.getType() == "Pawn" && piece.getColor() == 'W')
        {
            if(!piece.getHasMoved() && !isOccupied(column, row - 2))
                board[column][row - 2] = open;
            if(!isOccupied(column, row - 1))
                board[column][row - 1] = open;
            if(isOccupied(column - 1, row - 1) && board[column - 1][row - 1].getColor() == 'B')
            {
                board[column - 1][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(isOccupied(column + 1, row - 1) && board[column + 1][row - 1].getColor() == 'B')
            {
                board[column + 1][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
        }
        if(piece.getType() == "Pawn" && piece.getColor() == 'B')
        {
            if(!piece.getHasMoved() && !isOccupied(column, row + 2))
                board[column][row + 2] = open;
            if(!isOccupied(column, row + 1))
                board[column][row + 1] = open;
            if(isOccupied(column - 1, row + 1) && board[column - 1][row + 1].getColor() == 'W')
            {
                board[column - 1][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
            if(isOccupied(column + 1, row + 1) && board[column + 1][row + 1].getColor() == 'W')
            {
                board[column + 1][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
        }

        // Handle moves for rooks
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
            {
                board[tempColumn][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn, tempRow + 1) && !isOccupied(tempColumn, tempRow + 1))
            {
                board[tempColumn][tempRow + 1] = open;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn, tempRow + 1) && board[tempColumn][tempRow + 1].getColor() != piece.getColor())
            {
                board[tempColumn][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn;
                capturable[captureCount][1] = tempRow + 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow) && !isOccupied(tempColumn - 1, tempRow))
            {
                board[tempColumn - 1][tempRow] = open;
                tempColumn--;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow) && board[tempColumn - 1][tempRow].getColor() != piece.getColor())
            {
                board[tempColumn - 1][tempRow].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 1;
                capturable[captureCount][1] = tempRow ;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow) && !isOccupied(tempColumn + 1, tempRow))
            {
                board[tempColumn + 1][tempRow] = open;
                tempColumn++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow) && board[tempColumn + 1][tempRow].getColor() != piece.getColor())
            {
                board[tempColumn + 1][tempRow].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow;
                captureCount++;
            }
        }
        
        // Handle moves for bishops
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
            {
                board[tempColumn - 1][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 1;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow - 1) && !isOccupied(tempColumn + 1, tempRow - 1))
            {
                board[tempColumn + 1][tempRow - 1] = open;
                tempColumn++;
                tempRow--;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow - 1) && board[tempColumn + 1][tempRow - 1].getColor() != piece.getColor())
            {
                board[tempColumn + 1][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow + 1) && !isOccupied(tempColumn - 1, tempRow + 1))
            {
                board[tempColumn - 1][tempRow + 1] = open;
                tempColumn--;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow + 1) && board[tempColumn - 1][tempRow + 1].getColor() != piece.getColor())
            {
                board[tempColumn - 1][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 1;
                capturable[captureCount][1] = tempRow + 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow + 1) && !isOccupied(tempColumn + 1, tempRow + 1))
            {
                board[tempColumn + 1][tempRow + 1] = open;
                tempColumn++;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow + 1) && board[tempColumn + 1][tempRow + 1].getColor() != piece.getColor())
            {
                board[tempColumn + 1][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow + 1;
                captureCount++;
            }
        }
            
        // Handle moves for knights
        if(piece.getType() == "Knight")
        {
            int tempColumn = column;
            int tempRow = row;
            if(!isOutOfBounds(tempColumn - 2, tempRow - 1) && !isOccupied(tempColumn - 2, tempRow - 1))
                board[tempColumn - 2][tempRow - 1] = open;
            if(!isOutOfBounds(tempColumn - 2, tempRow - 1) && board[tempColumn - 2][tempRow - 1].getColor() != piece.getColor())
            {
                board[tempColumn - 2][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 2;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow - 2) && !isOccupied(tempColumn - 1, tempRow - 2))
                board[tempColumn - 1][tempRow - 2] = open;
            if(!isOutOfBounds(tempColumn - 1, tempRow - 2) && board[tempColumn - 1][tempRow - 2].getColor() != piece.getColor())
            {
                board[tempColumn - 1][tempRow - 2].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 1;
                capturable[captureCount][1] = tempRow - 2;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn + 2, tempRow - 1) && !isOccupied(tempColumn + 2, tempRow - 1))
                board[tempColumn + 2][tempRow - 1] = open;
            if(!isOutOfBounds(tempColumn + 2, tempRow - 1) && board[tempColumn + 2][tempRow - 1].getColor() != piece.getColor())
            {
                board[tempColumn + 2][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 2;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow - 2) && !isOccupied(tempColumn + 1, tempRow - 2))
                board[tempColumn + 1][tempRow - 2] = open;
            if(!isOutOfBounds(tempColumn + 1, tempRow - 2) && board[tempColumn + 1][tempRow - 2].getColor() != piece.getColor())
            {
                board[tempColumn + 1][tempRow - 2].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow - 2;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn + 2, tempRow + 1) && !isOccupied(tempColumn + 2, tempRow + 1))
                board[tempColumn + 2][tempRow + 1] = open;
            if(!isOutOfBounds(tempColumn + 2, tempRow + 1) && board[tempColumn + 2][tempRow + 1].getColor() != piece.getColor())
            {
                board[tempColumn + 2][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 2;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow + 2) && !isOccupied(tempColumn + 1, tempRow + 2))
                board[tempColumn + 1][tempRow + 2] = open;
            if(!isOutOfBounds(tempColumn + 1, tempRow + 2) && board[tempColumn + 1][tempRow + 2].getColor() != piece.getColor())
            {
                board[tempColumn + 1][tempRow + 2].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow + 2;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn - 2, tempRow + 1) && !isOccupied(tempColumn - 2, tempRow + 1))
                board[tempColumn - 2][tempRow + 1] = open;
            if(!isOutOfBounds(tempColumn - 2, tempRow + 1) && board[tempColumn - 2][tempRow + 1].getColor() != piece.getColor())
            {
                board[tempColumn - 2][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 2;
                capturable[captureCount][1] = tempRow + 1;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow + 2) && !isOccupied(tempColumn - 1, tempRow + 2))
                board[tempColumn - 1][tempRow + 2] = open;
            if(!isOutOfBounds(tempColumn - 1, tempRow + 2) && board[tempColumn - 1][tempRow + 2].getColor() != piece.getColor())
            {
                board[tempColumn - 1][tempRow + 2].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 1;
                capturable[captureCount][1] = tempRow + 2;
                captureCount++;
            }
        }
        
        // Handle moves for queens
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
            {
                board[tempColumn][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn, tempRow + 1) && !isOccupied(tempColumn, tempRow + 1))
            {
                board[tempColumn][tempRow + 1] = open;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn, tempRow + 1) && board[tempColumn][tempRow + 1].getColor() != piece.getColor())
            {
                board[tempColumn][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn;
                capturable[captureCount][1] = tempRow + 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow) && !isOccupied(tempColumn - 1, tempRow))
            {
                board[tempColumn - 1][tempRow] = open;
                tempColumn--;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow) && board[tempColumn - 1][tempRow].getColor() != piece.getColor())
            {
                board[tempColumn - 1][tempRow].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 1;
                capturable[captureCount][1] = tempRow;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow) && !isOccupied(tempColumn + 1, tempRow))
            {
                board[tempColumn + 1][tempRow] = open;
                tempColumn++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow) && board[tempColumn + 1][tempRow].getColor() != piece.getColor())
            {
                board[tempColumn + 1][tempRow].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow - 1) && !isOccupied(tempColumn - 1, tempRow - 1))
            {
                board[tempColumn - 1][tempRow - 1] = open;
                tempColumn--;
                tempRow--;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow - 1) && board[tempColumn - 1][tempRow -1].getColor() != piece.getColor())
            {
                board[tempColumn - 1][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 1;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow - 1) && !isOccupied(tempColumn + 1, tempRow - 1))
            {
                board[tempColumn + 1][tempRow - 1] = open;
                tempColumn++;
                tempRow--;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow - 1) && board[tempColumn + 1][tempRow - 1].getColor() != piece.getColor())
            {
                board[tempColumn + 1][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn - 1, tempRow + 1) && !isOccupied(tempColumn - 1, tempRow + 1))
            {
                board[tempColumn - 1][tempRow + 1] = open;
                tempColumn--;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow + 1) && board[tempColumn - 1][tempRow + 1].getColor() != piece.getColor())
            {
                board[tempColumn - 1][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 1;
                capturable[captureCount][1] = tempRow + 1;
                captureCount++;
            }
            tempColumn = column;
            tempRow = row;
            while(!isOutOfBounds(tempColumn + 1, tempRow + 1) && !isOccupied(tempColumn + 1, tempRow + 1))
            {
                board[tempColumn + 1][tempRow + 1] = open;
                tempColumn++;
                tempRow++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow + 1) && board[tempColumn + 1][tempRow + 1].getColor() != piece.getColor())
            {
                board[tempColumn + 1][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow + 1;
                captureCount++;
            }
        }
        
        // Handle moves for kings
        if(piece.getType() == "King")
        {
            if(!isOutOfBounds(column, row - 1) && board[column][row - 1].getColor() != piece.getColor())
            {
                board[column][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(!isOutOfBounds(column, row - 1) && !isOccupied(column, row - 1))
                board[column][row - 1] = open;
            if(!isOutOfBounds(column, row + 1) && board[column][row + 1].getColor() != piece.getColor())
            {
                board[column][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
            if(!isOutOfBounds(column, row + 1) && !isOccupied(column, row + 1))
                board[column][row + 1] = open;
            if(!isOutOfBounds(column - 1, row) && board[column - 1][row].getColor() != piece.getColor())
            {
                board[column - 1][row].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row;
                captureCount++;
            }
            if(!isOutOfBounds(column - 1, row) && !isOccupied(column - 1, row))
                board[column - 1][row] = open;
            if(!isOutOfBounds(column + 1, row) && board[column + 1][row].getColor() != piece.getColor())
            {
                board[column + 1][row].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row;
                captureCount++;
            }
            if(!isOutOfBounds(column + 1, row) && !isOccupied(column + 1, row))
                board[column + 1][row] = open;
            if(!isOutOfBounds(column - 1, row - 1) && board[column - 1][row - 1].getColor() != piece.getColor())
            {
                board[column - 1][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(!isOutOfBounds(column - 1, row - 1) && !isOccupied(column - 1, row - 1))
                board[column - 1][row - 1] = open;
            if(!isOutOfBounds(column + 1, row - 1) && board[column + 1][row - 1].getColor() != piece.getColor())
            {
                board[column + 1][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(!isOutOfBounds(column + 1, row - 1) && !isOccupied(column + 1, row - 1))
                board[column + 1][row - 1] = open;
            if(!isOutOfBounds(column - 1, row + 1) && board[column - 1][row + 1].getColor() != piece.getColor())
            {
                board[column - 1][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
            if(!isOutOfBounds(column - 1, row + 1) && !isOccupied(column - 1, row + 1))
                board[column - 1][row + 1] = open;
            if(!isOutOfBounds(column + 1, row + 1) && board[column + 1][row + 1].getColor() != piece.getColor())
            {
                board[column + 1][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
            if(!isOutOfBounds(column + 1, row + 1) && !isOccupied(column + 1, row + 1))
                board[column + 1][row + 1] = open;
        }
    }

    /*
     * Handles the movement for pieces
     * Can only move to spaces that are validated by checkMoves()
     * Returns a -1 if the destination is invalid
     */
    public int move(int currColumn, int currRow, int newColumn, int newRow)
    {
        checkMoves(currColumn, currRow);
        Piece current = board[currColumn][currRow];
        Piece destination = board[newColumn][newRow];

        if(destination.getType()== "-\t" || destination.getCanCapture())
        {
            // Promote pawns that have crossed the board to queens
            if(current.getType() == "Pawn" && (newRow == 0 || newRow == 7))
            {
                char color = current.getColor();
                board[newColumn][newRow] = new Piece(color, "Queen", true, false);
            }
            else
            {
                current.setHasMoved(true);
                board[newColumn][newRow] = current;
            }
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

    /*
     * Handles the creation of the game object and takes input from the user
     * Input is currently handled through a scanner object
     * GUI will eventually replace this feature
     */
    public static void main(String[] args)
    {
        Game g = new Game();
        g.startGame();
        g.printBoard();
        Scanner keyboard = new Scanner(System.in);
        // TODO: handle endgame conditions for checkmate and stalemate
        boolean finished = false;
        while(!finished)
        {
            System.out.println("\nSelect a piece");
            int currColumn = keyboard.nextInt();
            int currRow = keyboard.nextInt();
            if(g.whiteMove && g.board[currColumn][currRow].getColor() == 'B' ||
                !g.whiteMove && g.board[currColumn][currRow].getColor() == 'W')
            {
                if(g.whiteMove)
                    System.out.print("Only white pieces may move this turn");
                if(!g.whiteMove)
                    System.out.print("Only black pieces may move this turn");
            }
            else
            {
                System.out.println("\nWhere will the " + g.board[currColumn][currRow].getType() + " move?");
                g.checkMoves(currColumn, currRow);
                g.printBoard();
                System.out.println("\nThere are " + g.captureCount + " pieces that can be captured");
                if(g.captureCount > 0)
                {
                    for(int index = 0; index < g.captureCount; index++)
                    {
                        int column = g.capturable[index][0];
                        int row = g.capturable[index][1];
                        Piece current = g.board[column][row];
                        System.out.println("There is a " + current.getType() + " at " + column + "," + row);
                    }
                }
                System.out.println("\nThere are " + g.checkCount + " pieces that can check the king");
                if(g.checkCount > 0)
                {
                    for(int index = 0; index < g.checkCount; index++)
                    {
                        int column = g.checkers[index][0];
                        int row = g.checkers[index][1];
                        Piece current = g.board[column][row];
                        System.out.println("There is a " + current.getType() + " at " + column + "," + row);
                    }
                }
                System.out.print("\n");
                int newColumn = keyboard.nextInt();
                int newRow = keyboard.nextInt();
                if(g.move(currColumn, currRow, newColumn, newRow) == -1)
                    System.out.println("That move cannot be made");
                else
                {
                    g.printBoard();
                    if(g.whiteMove)
                        g.whiteMove = false;
                    else
                        g.whiteMove = true;
                }
            }
        }
    }
}
