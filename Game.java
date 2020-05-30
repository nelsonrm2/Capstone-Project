import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/*
 * This will be the main class that handles the fundamentals of the game
 * Ryan Nelson
 * Created 2/10/2020
 * Last updated 4/19/2020
 */
public class Game extends Frame implements MouseListener
{
    public Piece[][] board;           // Handles the board and all the pieces on it
    public int[][] capturable;        // Handles a list of coordinates to pieces that can be captured
    public int captureCount;          // Handles the number of pieces that can be captured
    public boolean whiteMove;         // Handles which player's turn it is
    public boolean whiteKingInCheck;  // Handles whether or not the king is in danger
    public boolean blackKingInCheck;  // Handles whether or not the king is in danger
    public int[][] checkers;          // Handles a list of coordinates to pieces that can attack the king
    public int checkCount;            // Handles the number of pieces that can attack the king
    public Piece[] capturePile;       // Handles the pieces that have been removed from the game
    public int killCount;             // Handles the number of pieces that have been removed
    public boolean singleplayer;      // Handles whether or not the game should use an AI
    public String difficulty;         // Handles what strategies the AI would use against the player
    
    public int mouseXCoord;           // Handles the horizontal location of the mouse
    public int mouseYCoord;           // Handles the vertical location of the mouse
    public boolean mouseClick;        // Handles whether or not the mouse has clicked
    
    public JLabel[][] tiles = new JLabel[8][8];
    public JPanel boardPanel = new JPanel(new GridLayout(8, 8));

    
    /*
     * Initialize the components of the board
     * Creates a unique object that handles each piece
     * White takes the bottom two rows, black takes the top two
     */
    public void startGame()
    {
        board = new Piece[8][8];
        capturable = new int[300][2];
        whiteMove = true;
        whiteKingInCheck = false;
        blackKingInCheck = false;
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
     * A functional GUI for the user to interact with
     */
    public void printBoard()
    {
        boardPanel.removeAll();
        boolean whiteTile = true;
        for(int row = 0; row < 8; row++)
        {
            for(int column = 0; column < 8; column++)
            {
                ImageIcon icon = null;
                tiles[column][row] = new JLabel();
                if(board[column][row].getType()== "-\t" || board[column][row].getCanCapture())
                {
                    icon = printTile(column, row, "red");
                    tiles[column][row].setIcon(icon);
                    if(whiteTile)
                        whiteTile = false;
                    else
                        whiteTile = true;

                }
                else if(whiteTile)
                {
                    icon = printTile(column, row, "white");
                    tiles[column][row].setIcon(icon);
                    whiteTile = false;
                }
                else
                {
                    icon = printTile(column, row, "black");
                    tiles[column][row].setIcon(icon);
                    whiteTile = true;
                }
                tiles[column][row].setSize(80, 80);
                boardPanel.add(tiles[column][row]);
            }
            if(whiteTile)
                whiteTile = false;
            else
                whiteTile = true;
        }
        boardPanel.updateUI();
    }
    
    /*
     * Looks at the current tile and pulls the correct png file from the folder
     * Returns an ImageIcon representing the piece and the board
     */
    public ImageIcon printTile(int column, int row, String color)
    {
        ImageIcon icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RE.png");
        if(board[column][row].getType() == "King")
        {
            if(board[column][row].getColor() == 'W')
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WWK.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BWK.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RWK.png");
            }
            else
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WBK.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BBK.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RBK.png");
            }

        }
        else if(board[column][row].getType() == "Queen")
        {
            if(board[column][row].getColor() == 'W')
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WWQ.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BWQ.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RWQ.png");
            }
            else
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WBQ.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BBQ.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RBQ.png");
            }
        }
        else if(board[column][row].getType() == "Bishop")
        {
            if(board[column][row].getColor() == 'W')
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WWB.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BWB.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RWB.png");
            }
            else
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WBB.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BBB.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RBB.png");
            }
        }
        else if(board[column][row].getType() == "Knight")
        {
            if(board[column][row].getColor() == 'W')
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WWN.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BWN.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RWN.png");
            }
            else
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WBN.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BBN.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RBN.png");
            }
        }
        else if(board[column][row].getType() == "Rook")
        {
            if(board[column][row].getColor() == 'W')
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WWR.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BWR.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RWR.png");
            }
            else
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WBR.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BBR.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RBR.png");
            }
        }
        else if(board[column][row].getType() == "Pawn")
        {
            if(board[column][row].getColor() == 'W')
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WWP.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BWP.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RWP.png");
            }
            else
            {
                if(color == "white")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WBP.png");
                else if(color == "black")
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BBP.png");
                else
                    icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\RBP.png");
            }
        }
        else if(board[column][row].getType() == "\t")
        {
            if(color == "white")
                icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\WE.png");
            else if(color == "black")
                icon = new ImageIcon("C:\\Users\\ryanm\\Desktop\\Chess\\BE.png");
        }
        return icon;
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
     * Looks at the position of a king and determines if it can be attacked
     * Moves that would place the king in danger return true
     * Moves that have no effect on the king return false
     */
    public boolean isDangerousMove(char color, int column, int row, int oldColumn, int oldRow)
    {
        boolean danger = false;
        if(column < 8 && column >= 0 && row < 8 && row >= 0)
        {
            Piece original = board[oldColumn][oldRow];
            board[oldColumn][oldRow] = new Piece(' ', "\t", false, false);
            Piece tempPiece = board[column][row];
            Piece testPiece = new Piece(color, "Test", false, false);
            board[column][row] = testPiece;
            if(checkForCheck())
                danger = true;
            board[column][row] = tempPiece;
            board[oldColumn][oldRow] = original;
        }
        return danger;
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
     * Clears any values from the capturable array
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
        whiteKingInCheck = false;
        blackKingInCheck = false;
    }

    /*
     * Scans through each piece to determine if the king is in danger
     * If check is possible, the kingInCheck flag is set to true
     * Any pieces that can attack the king are added to the checkers array
     */
    public boolean checkForCheck()
    {
        resetCaptureValues();
        boolean dangerousMove = false;
        for(int column = 0; column < 8; column++)
        {
            for(int row = 0; row < 8; row++)
            {
                Piece current = board[column][row];
                if(current.getColor() != ' ' && current.getType() != "King")
                {
                    checkMoves(column, row);
                    for(int index = 0; index < captureCount; index++)
                    {
                        int currColumn = capturable[index][0];
                        int currRow = capturable[index][1];
                        if(currColumn >= 0 && currRow >= 0 && board[currColumn][currRow].getType() == "King")
                        {
                            checkCount++;
                            if(board[currColumn][currRow].getColor() == 'B')
                                blackKingInCheck = true;
                            else
                                whiteKingInCheck = true;
                            System.out.println("The king at " + currColumn + "," + currRow + " is in danger from "
                                               + current.getType() + " at " + column + "," + row);
                            checkers[checkCount - 1][0] = column;
                            checkers[checkCount - 1][1] = row;
                        }
                        if(currColumn >= 0 && currRow >= 0 && board[currColumn][currRow].getType() == "Test")
                        {
                            dangerousMove = true;
                            //System.out.println("A move to " + currColumn + "," + currRow + " would be dangerous");
                        }
                    }
                }
                resetCaptureValues();
            }
            clearBoard();
        }
        // System.out.println(checkCount + " piece(s) can check the king");
        return dangerousMove;
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
            if(!piece.getHasMoved() && !isOccupied(column, row - 2) && !isOccupied(column, row - 1))
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
            if(!piece.getHasMoved() && (row + 2 < 8) && !isOccupied(column, row + 2) && !isOccupied(column, row + 1))
                board[column][row + 2] = open;
            if((row + 1 < 8) && !isOccupied(column, row + 1))
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
            // These need to be handled first because checkMoves automatically calls clearboard()
            boolean up = !isDangerousMove(piece.getColor(), column, row - 1, column, row);
            boolean down = !isDangerousMove(piece.getColor(), column, row + 1, column, row);
            boolean left = !isDangerousMove(piece.getColor(), column - 1, row, column, row);
            boolean right = !isDangerousMove(piece.getColor(), column + 1, row, column, row);
            boolean upleft = !isDangerousMove(piece.getColor(), column - 1, row - 1, column, row);
            boolean upright = !isDangerousMove(piece.getColor(), column + 1, row - 1, column, row);
            boolean downleft = !isDangerousMove(piece.getColor(), column - 1, row + 1, column, row);
            boolean downright = !isDangerousMove(piece.getColor(), column + 1, row + 1, column, row);
            
            if(!isOutOfBounds(column, row - 1) && board[column][row - 1].getColor() != piece.getColor()
               && board[column][row - 1].getColor() != ' ' && up)
            {
                board[column][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(!isOutOfBounds(column, row - 1) && !isOccupied(column, row - 1) && up)
                board[column][row - 1] = open;
            if(!isOutOfBounds(column, row + 1) && board[column][row + 1].getColor() != piece.getColor()
               && board[column][row + 1].getColor() != ' ' && down)
            {
                board[column][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
            if(!isOutOfBounds(column, row + 1) && !isOccupied(column, row + 1) && down)
                board[column][row + 1] = open;
            if(!isOutOfBounds(column - 1, row) && board[column - 1][row].getColor() != piece.getColor()
               && board[column - 1][row].getColor() != ' ' && left)
            {
                board[column - 1][row].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row;
                captureCount++;
            }
            if(!isOutOfBounds(column - 1, row) && !isOccupied(column - 1, row) && left)
                board[column - 1][row] = open;
            if(!isOutOfBounds(column + 1, row) && board[column + 1][row].getColor() != piece.getColor()
               && board[column + 1][row].getColor() != ' ' && right)
            {
                board[column + 1][row].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row;
                captureCount++;
            }
            if(!isOutOfBounds(column + 1, row) && !isOccupied(column + 1, row) && right)
                board[column + 1][row] = open;
            if(!isOutOfBounds(column - 1, row - 1) && board[column - 1][row - 1].getColor() != piece.getColor()
               && board[column - 1][row - 1].getColor() != ' ' && upleft)
            {
                board[column - 1][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(!isOutOfBounds(column - 1, row - 1) && !isOccupied(column - 1, row - 1) && upleft)
                board[column - 1][row - 1] = open;
            if(!isOutOfBounds(column + 1, row - 1) && board[column + 1][row - 1].getColor() != piece.getColor()
               && board[column + 1][row - 1].getColor() != ' ' && upright)
            {
                board[column + 1][row - 1].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row - 1;
                captureCount++;
            }
            if(!isOutOfBounds(column + 1, row - 1) && !isOccupied(column + 1, row - 1) && upright)
                board[column + 1][row - 1] = open;
            if(!isOutOfBounds(column - 1, row + 1) && board[column - 1][row + 1].getColor() != piece.getColor()
               && board[column - 1][row + 1].getColor() != ' ' && downleft)
            {
                board[column - 1][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column - 1;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
            if(!isOutOfBounds(column - 1, row + 1) && !isOccupied(column - 1, row + 1) && downleft)
                board[column - 1][row + 1] = open;
            if(!isOutOfBounds(column + 1, row + 1) && board[column + 1][row + 1].getColor() != piece.getColor()
               && board[column + 1][row + 1].getColor() != ' ' && downright)
            {
                board[column + 1][row + 1].setCanCapture(true);
                capturable[captureCount][0] = column + 1;
                capturable[captureCount][1] = row + 1;
                captureCount++;
            }
            if(!isOutOfBounds(column + 1, row + 1) && !isOccupied(column + 1, row + 1) && downright)
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
    
    /*
     * Look at a given piece and check to see if there is a valuable target at that position
     * Offense rating is assigned by checking the value of the piece it could capture
     * Returns an int representing how advantageous the move is
     */
    public int checkOffenseLevel(Piece target)
    {
        int value = 0;
        if(target.getType() == "King")
            value = 10000;
        else if(target.getType() == "Queen")
            value = 100;
        else if(target.getType() == "Rook")
            value = 50;
        else if(target.getType() == "Bishop")
            value = 35;
        else if(target.getType() == "Knight")
            value = 30;
        else if(target.getType() == "Pawn")
            value = 10;
        Random rand = new Random();
        value += rand.nextInt(10);
        return value;
    }
    
    /*
     * Look at a given piece and check how many pieces it could capture from that position
     * Offense rating is assigned by checking the value of each piece it could capture
     * Returns an int representing how offensive the move is
     */
    public int checkDangerLevel(int column, int row, Piece attacker)
    {
        int danger = 0;
        Piece original = board[column][row];
        board[column][row] = attacker;
        for(int tempColumn = 0; tempColumn < 8; tempColumn++)
        {
            for(int tempRow = 0; tempRow < 8; tempRow++)
            {
                if(board[tempColumn][tempRow].getColor() == 'W')
                    checkMoves(tempColumn, tempRow);
                for(int currColumn = 0; currColumn < 8; currColumn++)
                {
                    for(int currRow = 0; currRow < 8; currRow++)
                    {
                        if(currColumn == column && currRow == row && board[currColumn][currRow].getCanCapture())
                        {
                            if(attacker.getType() == "King")
                                danger = 10000;
                            else if(attacker.getType() == "Queen")
                                danger = 100;
                            else if(attacker.getType() == "Rook")
                                danger = 50;
                            else if(attacker.getType() == "Bishop")
                                danger = 35;
                            else if(attacker.getType() == "Knight")
                                danger = 30;
                            else if(attacker.getType() == "Pawn")
                                danger = 10;
                            Random rand = new Random();
                            danger += rand.nextInt(10);
                        }
                    }
                }
            }
        }
        board[column][row] = original;
        resetCaptureValues();
        return danger;
    }    
    
    /*
     * Handles the move for AI in single player games
     * The difficulty is determined by the player at main menu
     * Automatically feeds its choice into move() and returns control to player
     */
    public void computerMove()
    {
        if(difficulty.equals("Random"))
        {
            Random rand = new Random();
            int[][] availableMoves = new int[300][4];
            int moveCount = 0;
            // Iterate through the board and look for pieces that can move
            for(int column = 0; column < 8; column++)
            {
                for(int row = 0; row < 8; row++)
                {
                    Piece current = board[column][row];
                    if(current.getColor() == 'B')
                    {
                        checkMoves(column, row);
                        for(int newColumn = 0; newColumn < 8; newColumn++)
                        {
                            for(int newRow = 0; newRow < 8; newRow++)
                            {
                                Piece destination = board[newColumn][newRow];
                                if(destination.getType() == "-\t" || destination.getCanCapture())
                                {
                                    // If a valid move is detected, add it to the array
                                    availableMoves[moveCount][0] = column;
                                    availableMoves[moveCount][1] = row;
                                    availableMoves[moveCount][2] = newColumn;
                                    availableMoves[moveCount][3] = newRow;
                                    moveCount++;
                                }
                            }
                        }
                    }
                    resetCaptureValues();
                }
                clearBoard();
            }
            
            // Choose a move from the list at random
            int pieceColumn;
            int pieceRow;
            int moveColumn;
            int moveRow;
            do
            {
                int selectMove = rand.nextInt(moveCount);
                pieceColumn = availableMoves[selectMove][0];
                pieceRow = availableMoves[selectMove][1];
                moveColumn = availableMoves[selectMove][2];
                moveRow = availableMoves[selectMove][3];
                
                System.out.println("The computer will move the " + board[pieceColumn][pieceRow].getType() +
                    " from " + pieceColumn + "," + pieceRow + " to " + moveColumn + "," + moveRow);
            }
            while(move(pieceColumn, pieceRow, moveColumn, moveRow) == -1);
        }
        else if(difficulty == "Thoughtful")
        {
            int[][] availableMoves = new int[300][5];
            int moveCount = 0;
            // Iterate through the board and look for pieces that can move
            for(int column = 0; column < 8; column++)
            {
                for(int row = 0; row < 8; row++)
                {
                    Piece current = board[column][row];
                    if(current.getColor() == 'B')
                    {
                        checkMoves(column, row);
                        for(int newColumn = 0; newColumn < 8; newColumn++)
                        {
                            for(int newRow = 0; newRow < 8; newRow++)
                            {
                                Piece destination = board[newColumn][newRow];
                                if(destination.getType() == "-\t" || destination.getCanCapture() && destination.getColor() != 'B')
                                {
                                    availableMoves[moveCount][0] = column;
                                    availableMoves[moveCount][1] = row;
                                    availableMoves[moveCount][2] = newColumn;
                                    availableMoves[moveCount][3] = newRow;
                                    int offense = checkOffenseLevel(destination);
                                    int danger = checkDangerLevel(newColumn, newRow, current);
                                    //System.out.println("The offense is: " + offense + " The danger is: " + danger);
                                    availableMoves[moveCount][4] = offense; // - danger;
                                    moveCount++;
                                }
                            }
                        }
                    }
                    resetCaptureValues();
                }
                clearBoard();
            }
            
            int bestRating = -1000;
            int pieceColumn = 0;
            int pieceRow = 0;
            int moveColumn = 0;
            int moveRow = 0;
            for(int index = 0; index < moveCount; index++)
            {
                int currRating = availableMoves[index][4];
                if(currRating > bestRating)
                {
                    bestRating = currRating;
                    pieceColumn = availableMoves[index][0];
                    pieceRow = availableMoves[index][1];
                    moveColumn = availableMoves[index][2];
                    moveRow = availableMoves[index][3];
                }
            }
            System.out.println("The best rating is: " + bestRating);
            System.out.println("The computer will move the " + board[pieceColumn][pieceRow].getType() +
                " from " + pieceColumn + "," + pieceRow + " to " + moveColumn + "," + moveRow);
            if(move(pieceColumn, pieceRow, moveColumn, moveRow) == -1)
            {
            //    System.out.println("That didn't work");
                computerMove();
            }
        }
    }

    public void runGame()
    {
        boolean finished = false;
        while(!finished)
        {
            printBoard();
            // Check to see if it is the player's turn
            if(!singleplayer || whiteMove)
            {
                while(!mouseClick)
                {
                    // Do nothing
                    // Wait for the user to click something
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println("Something went wrong: \n" + e);
                    }
                }
                int currColumn = mouseXCoord;
                int currRow = mouseYCoord;
                mouseClick = false;
                // Check if the input is valid
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
                    checkMoves(currColumn, currRow);
                    /*System.out.println("There are " + captureCount + " pieces that can be captured");
                    if(captureCount > 0)
                    {
                        for(int index = 0; index < captureCount; index++)
                        {
                            int column = capturable[index][0];
                            int row = capturable[index][1];
                            if(column >= 0 && row >= 0)
                            {
                                Piece current = board[column][row];
                                System.out.println("\t" + (index + 1) + ": " + current.getType() + " " + column + "," + row);
                            }
                        }
                    }*/
                    printBoard();
                    while(!mouseClick)
                    {
                        // Do nothing
                        // Wait for the user to click something
                        try
                        {
                            Thread.sleep(10);
                        }
                        catch (InterruptedException e)
                        {
                            System.out.println("Something went wrong: \n" + e);
                        }
                    }
                    mouseClick = false;
                    int newColumn = mouseXCoord;
                    int newRow = mouseYCoord;
                    if(move(currColumn, currRow, newColumn, newRow) == -1)
                        System.out.println("That move cannot be made");
                    else
                    {
                        if(whiteMove)
                            whiteMove = false;
                        else
                            whiteMove = true;
                    }
                    checkForCheck();
                    if(checkCount > 0)
                    {
                        // System.out.println("Check");
                        for(int index = 0; index < checkCount; index++)
                        {
                            int column = checkers[index][0];
                            int row = checkers[index][1];
                            Piece current = board[column][row];
                            System.out.println("\t" + (index + 1) + ": " + current.getColor() + " " + current.getType());
                        }
                    }
                }
                
                //System.out.println("So far, there have been " + killCount + " casualties");
                for(int index = 0; index < killCount; index++)
                {
                    if(capturePile[index].getType() == "King")
                    {
                        finished = true;
                        printBoard();
                        System.out.println("The king is dead, long live the king");
                    }
                    //System.out.println("\t" + (index + 1) + ": " + capturePile[index].getColor() + " " + capturePile[index].getType());
                }
                resetCheckValues();
                resetCaptureValues();
            }
            else
            {
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                    System.out.println("Something went wrong: \n" + e);
                }
                computerMove();
                whiteMove = true;
            }
        }
    }
    
    public Game()
    {
        JFrame menuFrame = new JFrame();
        JFrame boardFrame = new JFrame();
        boardFrame.setTitle("Chess Board");
        boardFrame.setSize(564, 587);
        boardFrame.add(boardPanel);
        boardPanel.addMouseListener(this);
        
        Panel menuPanel = new Panel(new BorderLayout(10, 10));
        menuPanel.setSize(500, 500);
        menuFrame.add(menuPanel);
        JLabel textBox = new JLabel("Main Menu");
        menuPanel.add(textBox, BorderLayout.PAGE_START);
        JLabel textBox2 = new JLabel("Welcome to Shallow Blue");
        menuPanel.add(textBox2, BorderLayout.PAGE_END);
        JButton startsp1 = new JButton("Single Player - Random");
        startsp1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                singleplayer = true;
                boardFrame.setVisible(true);
                difficulty = "Random";
            }
        });
        menuPanel.add(startsp1, BorderLayout.LINE_START);
        JButton startsp2 = new JButton("Single Player - Thoughtful");
        startsp2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                singleplayer = true;
                boardFrame.setVisible(true);
                difficulty = "Thoughtful";
            }
        });
        menuPanel.add(startsp2, BorderLayout.CENTER);
        JButton startmp = new JButton("Start Multi Player Game");
        startmp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                singleplayer = false;
                boardFrame.setVisible(true);
            }
        });
        menuPanel.add(startmp, BorderLayout.LINE_END);
        menuFrame.setTitle("Main Menu");
        menuFrame.setSize(550, 250);
        menuFrame.setVisible(true);
    }
    
    /*
     * Handles the creation of the game object and begins a new game
     */
    public static void main(String[] args)
    {
        Game g = new Game();
        g.startGame();
        g.runGame();
    }
    
    /*
     * Handles the input from the user to detect where the mouse is on the screen
     * From this, we can calculate which tile the user is pointing on the board
     * The tile is then passed to the game
     */
    public void mouseClicked(MouseEvent e)
    {
        int x = e.getX() / 70;
        int y = e.getY() / 70;
        mouseXCoord = x;
        mouseYCoord = y;
        mouseClick = true;
    }
    
    /*
     * These methods all need to be overridden after we implement MouseListener
     * Most of them we don't need, so they'll be left blank
     */
    public void mouseDragged(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    
}