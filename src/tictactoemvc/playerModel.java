//-------------------------------------------------------------------------
// Author: Sydnee Woodhouse
// Date : 1/19/2018
// BoardModel: Model represents an object or JAVA POJO carrying data. 
// It can also have logic to update controller if its data changes.
//-------------------------------------------------------------------------
package tictactoemvc;

public class playerModel
{
    private boolean p1Turn;
    public boolean AIEnabled;
    private String[][] gameBoard;
    
    // Checks to see if user wins the game 
    boolean playerWon(String playerIcon)
    {   
        for(int j = 0; j < 3; j++)
            {
                if((gameBoard[0][j] == playerIcon) && (gameBoard[1][j] == playerIcon) && (gameBoard[2][j] == playerIcon))
                    return true;
            }

        for(int i = 0; i < 3; i++)
        {
            if((gameBoard[i][0] == playerIcon) && (gameBoard[i][1] == playerIcon) && (gameBoard[i][2] == playerIcon))
                return true;
        }

        if((gameBoard[1][1] == playerIcon) &&(gameBoard[0][0] == playerIcon) && (gameBoard[2][2] == playerIcon))
            return true;
        
         if((gameBoard[1][1] == playerIcon) &&(gameBoard[0][2] == playerIcon) && (gameBoard[2][0] == playerIcon))
            return true;
         
            return false;
    }
}
