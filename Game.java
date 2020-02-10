/*
 * This will be the main class that handles the fundamentals of the game
 * Ryan Nelson
 */
public class Game
{
    public int[][] board;
    
    public void startGame()
    {
        board = new int[8][8];
        for(int index = 0; index < 8; index++)
        {
            board[index][7] = 8;
            board[index][6] = 7;
            board[index][5] = 6;
            board[index][4] = 5;
            board[index][3] = 4;
            board[index][2] = 3;
            board[index][1] = 2;            
            board[index][0] = 1;
        }
    }
    
    public void printBoard()
    {
        for(int row = 0; row < 8; row++)
        {
            for(int column = 0; column < 8; column++)
            {
                System.out.print(board[column][row] + "\t");
            }
            System.out.print("\n\n");
        }
    }
    
    public static void main(String[] args)
    {
        Game g = new Game();
        g.startGame();
        g.printBoard();
    }
}
