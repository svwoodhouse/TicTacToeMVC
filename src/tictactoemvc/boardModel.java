//--------------------------------------------------------------------
// Author : Sydnee Woodhouse
// Date: 1/22/2018
// This is the model of the game board
// It conatins all the data
//---------------------------------------------------------------------
package tictactoemvc;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class boardModel 
{
    private String player1Name;
    private String player2Name;
    private boolean p1Turn;
    private boolean AIEnabled;
    private String[][] gameBoard;
    
    public void setBoard() {
        gameBoard = new String[3][3];
    }
    
    // Checks to see if the game board is full
    public boolean isBoardFull(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(gameBoard[i][j] == null)
                    return false;
            }
        }
        return true;
    }
    
    // Checks to see if spot is full
    public boolean isSpotFull(int r, int c)
    {
        if(gameBoard[r][c] == null || gameBoard[r][c] == "")
            return false;
        else
            return true;
    }
    public void setMove(int r, int c)
    {
        if(p1Turn)
            gameBoard[r][c] = "X";
        
        else
            gameBoard[r][c] = "O";
    }
    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }
    
    public boolean getAIEnabled() {
        return AIEnabled;
    }
    
    public void setAIEnabled(boolean AIEnabled) {
        this.AIEnabled = AIEnabled;
    }
    
    public String[][] getGameBoard(){
        return gameBoard;
    }
    
    public boolean getP1Turn() {
        return p1Turn;
    }
    
    public void setP1Turn(boolean p1Turn) {
        this.p1Turn = p1Turn;
    }
    
}
