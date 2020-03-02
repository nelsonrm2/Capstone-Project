import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Scanner;

/*
 * This will be the main class that handles the fundamentals of the game
 * Ryan Nelson
 * Created 2/10/2020
 * Last updated 2/26/2020
 */
public class Game
{
    public Piece[][] board;     // Handles the board and all the pieces on it
    public int[][] capturable;      // Handles a list of coordinates to pieces that can be captured
    public int captureCount;        // Handles the number of pieces that can be captured
    public boolean whiteMove;       // Handles which player's turn it is
    public boolean kingInCheck;     // Handles whether or not the king is in danger
    public int[][] checkers;        // Handles a list of coordinates to pieces that can attack the king
    public int checkCount;      // Handles the number of pieces that can attack the king
    public Piece[] capturePile;     // Handles the pieces that have been removed from the game
    public int killCount;       // Handles the number of pieces that have been removed
    
    /*
     * Initialize the components of the board
     * Creates a unique object that handles each piece
     * White takes the bottom two rows, black takes the top two
     */
    public void startGame()
    {
        board = new Piece[8][8];
        capturable = new int[100][2];
        whiteMove = true;
        kingInCheck = false;
        checkCount = 0;
        checkers = new int[1000][2];
        capturePile = new Piece[32];
        killCount = 0;

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
        board[3][0] = new Piece('B', "Queen", false, false);

        // Spawn in the kings
        board[4][7] = new Piece('W', "King", false, false);
        board[4][0] = new Piece('B', "King", false, false);
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
        System.out.print("\n");
    }

    /*
     * Checks the given coordinates to see whether a piece occupies it
     * Returns true if there is a piece, false if there is not
     */
    public boolean isOccupied(int column, int row)
    {
        return (!isOutOfBounds(column, row) && board[column][row].getColor() != ' ');
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
    }

    /*
     * Clears any values from the captureble array
     * Sets the captureCount integer back to zero
     */
    public void resetCaptureValues()
    {
        for(int index = 0; index < captureCount; index++)
        {
            capturable[index][0] = 0;
            capturable[index][1] = 0;
        }
        captureCount = 0;
    }

    /*
     * Clears any values from the checkers array
     * Sets the checkCount integer back to zero
     */
    public void resetCheckValues()
    {
        for(int index = 0; index < checkCount; index++)
        {
            checkers[index][0] = 0;
            checkers[index][1] = 0;
        }
        checkCount = 0;
    }

    /*
     * Scans through each piece to determine if the king is in danger
     * If check is possible, the kingInCheck flag is set to true
     * Any pieces that can attack the king are added to the checkers array
     * Returns true if the king is in danger, false if the king is safe
     */
    public boolean checkForCheck()
    {
        for(int column = 0; column < 8; column++)
        {
            for(int row = 0; row < 8; row++)
            {
                Piece current = board[column][row];
                if(current.getColor() != ' ')
                {
                    checkMoves(column, row);
                    if(captureCount != 0)
                    {
                        System.out.println("There is a " + current.getType() + " at " + column + "," + row +
                                           " that can capture " + captureCount + " pieces");
                    }
                    for(int index = 0; index < captureCount; index++)
                    {
                        int currColumn = capturable[index][0];
                        int currRow = capturable[index][1];
                        if(board[currColumn][currRow].getType() == "King")
                        {
                            checkCount++;
                            kingInCheck = true;
                            String kingColor;
                            if(board[currColumn][currRow].getColor() == 'B')
                                kingColor = "black";
                            else
                                kingColor = "white";
                            System.out.println("The " + kingColor + " king at " + currColumn + "," + currRow + " is in danger from "
                                               + current.getType() + " at " + column + "," + row);
                            checkers[checkCount - 1][0] = column;
                            checkers[checkCount - 1][1] = row;
                        }
                    }
                resetCaptureValues();
                }
            }
            clearBoard();
        }
        System.out.println("There are " + checkCount + " pieces that can check the king");
        return kingInCheck;
    }
    
    /*
     * Prints out the available moves for a piece at a given coordinate
     * Valid unoccupied spaces are highlighted with a '-'
     * If an enemy piece is in a valid space, it is signified by the canCapture attribute
     * For pieces that move in a straight line, spaces are continuously scanned and set as valid until
     *   it comes across an occupied space or it reaches the end of the board
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
            if(!isOutOfBounds(tempColumn - 2, tempRow - 1) && board[tempColumn - 2][tempRow - 1].getColor() != piece.getColor()
               && board[tempColumn - 2][tempRow - 1].getColor() != ' ')
            {
                board[tempColumn - 2][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 2;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow - 2) && !isOccupied(tempColumn - 1, tempRow - 2))
                board[tempColumn - 1][tempRow - 2] = open;
            if(!isOutOfBounds(tempColumn - 1, tempRow - 2) && board[tempColumn - 1][tempRow - 2].getColor() != piece.getColor()
               && board[tempColumn - 1][tempRow - 2].getColor() != ' ')
            {
                board[tempColumn - 1][tempRow - 2].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 1;
                capturable[captureCount][1] = tempRow - 2;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn + 2, tempRow - 1) && !isOccupied(tempColumn + 2, tempRow - 1))
                board[tempColumn + 2][tempRow - 1] = open;
            if(!isOutOfBounds(tempColumn + 2, tempRow - 1) && board[tempColumn + 2][tempRow - 1].getColor() != piece.getColor()
               && board[tempColumn + 2][tempRow - 1].getColor() != ' ')
            {
                board[tempColumn + 2][tempRow - 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 2;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow - 2) && !isOccupied(tempColumn + 1, tempRow - 2))
                board[tempColumn + 1][tempRow - 2] = open;
            if(!isOutOfBounds(tempColumn + 1, tempRow - 2) && board[tempColumn + 1][tempRow - 2].getColor() != piece.getColor()
               && board[tempColumn + 1][tempRow - 2].getColor() != ' ')
            {
                board[tempColumn + 1][tempRow - 2].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow - 2;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn + 2, tempRow + 1) && !isOccupied(tempColumn + 2, tempRow + 1))
                board[tempColumn + 2][tempRow + 1] = open;
            if(!isOutOfBounds(tempColumn + 2, tempRow + 1) && board[tempColumn + 2][tempRow + 1].getColor() != piece.getColor()
               && board[tempColumn + 2][tempRow + 1].getColor() != ' ')
            {
                board[tempColumn + 2][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 2;
                capturable[captureCount][1] = tempRow - 1;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn + 1, tempRow + 2) && !isOccupied(tempColumn + 1, tempRow + 2))
                board[tempColumn + 1][tempRow + 2] = open;
            if(!isOutOfBounds(tempColumn + 1, tempRow + 2) && board[tempColumn + 1][tempRow + 2].getColor() != piece.getColor()
               && board[tempColumn + 1][tempRow + 2].getColor() != ' ')
            {
                board[tempColumn + 1][tempRow + 2].setCanCapture(true);
                capturable[captureCount][0] = tempColumn + 1;
                capturable[captureCount][1] = tempRow + 2;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn - 2, tempRow + 1) && !isOccupied(tempColumn - 2, tempRow + 1))
                board[tempColumn - 2][tempRow + 1] = open;
            if(!isOutOfBounds(tempColumn - 2, tempRow + 1) && board[tempColumn - 2][tempRow + 1].getColor() != piece.getColor()
               && board[tempColumn - 2][tempRow + 1].getColor() != ' ')
            {
                board[tempColumn - 2][tempRow + 1].setCanCapture(true);
                capturable[captureCount][0] = tempColumn - 2;
                capturable[captureCount][1] = tempRow + 1;
                captureCount++;
            }
            if(!isOutOfBounds(tempColumn - 1, tempRow + 2) && !isOccupied(tempColumn - 1, tempRow + 2))
                board[tempColumn - 1][tempRow + 2] = open;
            if(!isOutOfBounds(tempColumn - 1, tempRow + 2) && board[tempColumn - 1][tempRow + 2].getColor() != piece.getColor()
               && board[tempColumn - 1][tempRow + 2].getColor() != ' ')
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
            if(!isOutOfBounds(column, row - 1) && board[column][row - 1].getColor() != piece.getColor()
               && board[column][row - 1].getColor() != ' ')
            {
                board[column][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(!isOutOfBounds(column, row - 1) && !isOccupied(column, row - 1))
                board[column][row - 1] = open;
            if(!isOutOfBounds(column, row + 1) && board[column][row + 1].getColor() != piece.getColor()
               && board[column][row + 1].getColor() != ' ')
            {
                board[column][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
            if(!isOutOfBounds(column, row + 1) && !isOccupied(column, row + 1))
                board[column][row + 1] = open;
            if(!isOutOfBounds(column - 1, row) && board[column - 1][row].getColor() != piece.getColor()
               && board[column - 1][row].getColor() != ' ')
            {
                board[column - 1][row].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row;
                captureCount++;
            }
            if(!isOutOfBounds(column - 1, row) && !isOccupied(column - 1, row))
                board[column - 1][row] = open;
            if(!isOutOfBounds(column + 1, row) && board[column + 1][row].getColor() != piece.getColor()
               && board[column + 1][row].getColor() != ' ')
            {
                board[column + 1][row].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row;
                captureCount++;
            }
            if(!isOutOfBounds(column + 1, row) && !isOccupied(column + 1, row))
                board[column + 1][row] = open;
            if(!isOutOfBounds(column - 1, row - 1) && board[column - 1][row - 1].getColor() != piece.getColor()
               && board[column - 1][row - 1].getColor() != ' ')
            {
                board[column - 1][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(!isOutOfBounds(column - 1, row - 1) && !isOccupied(column - 1, row - 1))
                board[column - 1][row - 1] = open;
            if(!isOutOfBounds(column + 1, row - 1) && board[column + 1][row - 1].getColor() != piece.getColor()
               && board[column + 1][row - 1].getColor() != ' ')
            {
                board[column + 1][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(!isOutOfBounds(column + 1, row - 1) && !isOccupied(column + 1, row - 1))
                board[column + 1][row - 1] = open;
            if(!isOutOfBounds(column - 1, row + 1) && board[column - 1][row + 1].getColor() != piece.getColor()
               && board[column - 1][row + 1].getColor() != ' ')
            {
                board[column - 1][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
            if(!isOutOfBounds(column - 1, row + 1) && !isOccupied(column - 1, row + 1))
                board[column - 1][row + 1] = open;
            if(!isOutOfBounds(column + 1, row + 1) && board[column + 1][row + 1].getColor() != piece.getColor()
               && board[column + 1][row + 1].getColor() != ' ')
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
            if(isOccupied(newColumn, newRow) && destination.getColor() != ' ')
            {
                capturePile[killCount] = destination;
                killCount++;
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

    public void runGame(Scanner keyboard)
    {
        boolean finished = false;
        while(!finished)
        {
            System.out.println("Select a piece");
            int currColumn = keyboard.nextInt();
            int currRow = keyboard.nextInt();
            if(whiteMove && board[currColumn][currRow].getColor() == 'B' ||
                !whiteMove && board[currColumn][currRow].getColor() == 'W')
            {
                if(whiteMove)
                    System.out.println("Only white pieces may move this turn");
                if(!whiteMove)
                    System.out.println("Only black pieces may move this turn");
            }
            else if(board[currColumn][currRow].getColor() == ' ')
                System.out.println("That space is empty");
            else
            {
                System.out.println("Where will the " + board[currColumn][currRow].getType() + " move?");
                checkMoves(currColumn, currRow);
                printBoard();
                System.out.println("There are " + captureCount + " pieces that can be captured");
                if(captureCount > 0)
                {
                    for(int index = 0; index < captureCount; index++)
                    {
                        int column = capturable[index][0];
                        int row = capturable[index][1];
                        Piece current = board[column][row];
                        System.out.println("\t" + (index + 1) + ": " + current.getType() + " " + column + "," + row);
                    }
                }
                int newColumn = keyboard.nextInt();
                int newRow = keyboard.nextInt();
                if(move(currColumn, currRow, newColumn, newRow) == -1)
                    System.out.println("That move cannot be made");
                else
                {
                    printBoard();
                    if(whiteMove)
                        whiteMove = false;
                    else
                        whiteMove = true;
                }
                if(checkForCheck())
                {
                    System.out.println("Check");
                }
                if(checkCount > 0)
                {
                    for(int index = 0; index < checkCount; index++)
                    {
                        int column = checkers[index][0];
                        int row = checkers[index][1];
                        Piece current = board[column][row];
                    }
                }
            }
            System.out.println("So far, there have been " + killCount + " casualties");
            for(int index = 0; index < killCount; index++)
            {
                System.out.println("\t" + (index + 1) + ": " + capturePile[index].getColor() + " " + capturePile[index].getType());
            }
            resetCheckValues();
            resetCaptureValues();
        }
    }
    
    public Game()
    {
        JFrame frame = new JFrame();
        Panel boardPanel = new Panel(new GridLayout(8, 8));
        JButton[][] board = new JButton[8][8];
        boolean whiteTile = true;
        for(int column = 0; column < 8; column++)
        {
            for(int row = 0; row < 8; row++)
            {
                String title = column + "," + row;
                if(whiteTile)
                {
                    board[column][row] = new JButton(new ImageIcon("C:\\Users\\ryanm\\Desktop\\whiteTile.png"));
                    whiteTile = false;
                }
                else
                {
                    board[column][row] = new JButton(new ImageIcon("C:\\Users\\ryanm\\Desktop\\blackTile.png"));
                    whiteTile = true;
                }
                //board[column][row] = new JFrame(new ImageIcon("C:\\Users\\ryanm\\Desktop\\whiteTile.png"));
                boardPanel.add(board[column][row]);
            }
            if(whiteTile)
                whiteTile = false;
            else
                whiteTile = true;
        }
        
        frame.add(boardPanel);
  
        frame.setTitle("Chess Board"); // "super" Frame sets title
        frame.setSize(550, 550);            // "super" Frame sets initial size
        frame.setVisible(true);             // "super" Frame shows
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
        g.runGame(keyboard);
    }
}