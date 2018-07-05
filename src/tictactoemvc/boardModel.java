//--------------------------------------------------------------------
// Author : Sydnee Woodhouse
// Date: 1/22/2018
// This is the model of the game board
// It conatins all the data
//---------------------------------------------------------------------
package tictactoemvc;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class boardModel 
{
    private String player1Name;
    private String player2Name;
    private boolean p1Turn;
    private boolean onlineMode;
    private boolean AIEnabled;
    private String[][] gameBoard;
    
    // For multiplayer online
    private String ip = "localhost";
    private int port;
    private Socket socket;
    private ObjectOutputStream  output;
    private ObjectInputStream input;
    private ServerSocket serverSocket;
    private boolean connectionEnded;
    private boolean accepted;
    private boolean client;
    private boolean server;
    
    public void setBoard() {
        gameBoard = new String[3][3];
    }
 
    // Checks to see if the game board is full
    public boolean isBoardFull(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(gameBoard[i][j] == null || gameBoard[i][j] == "")
                    return false;
            }
        }
        return true;
    }
    
    public boolean playerWon(String playerIcon)
    {
        // For loop to check if the palyer won by columns    
        for(int j = 0; j < 3; j++)
            {
                if((gameBoard[0][j] == playerIcon) && (gameBoard[1][j] == playerIcon) && (gameBoard[2][j] == playerIcon))
                    return true;
                
            }
        
        // For loop to check if the player won by rows
        for(int i = 0; i < 3; i++)
        {
            if((gameBoard[i][0] == playerIcon) && (gameBoard[i][1] == playerIcon) && (gameBoard[i][2] == playerIcon))
                return true;
        }
        
        // For loop to check if the palyer won diagonal
        if((gameBoard[1][1] == playerIcon) &&(gameBoard[0][0] == playerIcon) && (gameBoard[2][2] == playerIcon))
            return true;
        
        //Checks to see if the user won in the reverse diagonal direction
         if((gameBoard[1][1] == playerIcon) &&(gameBoard[0][2] == playerIcon) && (gameBoard[2][0] == playerIcon))
            return true;
         
            return false;
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
    
    public void setGameBoard(String[][] gameBoard) {
        this.gameBoard = gameBoard;
    }
    
    public boolean getP1Turn() {
        return p1Turn;
    }
    
    public void setP1Turn(boolean p1Turn) {
        this.p1Turn = p1Turn;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public void setInput(ObjectInputStream input) {
        this.input = input;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public boolean isConnectionEnded() {
        return connectionEnded;
    }

    public void setConnectionEnded(boolean connectionEnded) {
        this.connectionEnded = connectionEnded;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }
    
    
    
}
