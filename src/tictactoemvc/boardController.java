//--------------------------------------------------------------------
// Author : Sydnee Woodhouse
// Date: 1/31/2018
// This is the controller for the board
// It controls the data coming from the user or going to the user
// from the model
//---------------------------------------------------------------------
package tictactoemvc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class boardController 
{
    private boardModel model;
    private boardView view;
    
    public boardController(boardModel m, boardView v)
    {
        model = m;
        view = v;
    }
    
    public void initController()
    {
        ButtonListener listener = new ButtonListener();
        
        // Main Menu Buttons Action 
        view.getPlayAIButton().addActionListener(listener);
        view.getPlayFriendButton().addActionListener(listener);
        view.getPlayOnlineButton().addActionListener(listener);
        view.getHowToPlayButton().addActionListener(listener);
        view.getLoadGameButton().addActionListener(listener);
        view.getExitButton().addActionListener(listener);
        
        // Game Panel buttons
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                view.getGameboardButtons()[i][j].addActionListener(listener);
            }
        }
        
        view.getSaveButtons()[0].addActionListener(listener);
        view.getSaveButtons()[1].addActionListener(listener);
    }   
    
    private void computerTurn()
    {
        outerloop:
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if(model.getGameBoard()[i][j] == "" || model.getGameBoard()[i][j] == null)
                {
                    view.getGameboardButtons()[i][j].doClick();
                    model.setP1Turn(true);
                    break outerloop;
                }
            }
        }
    }
    
    private void playerTurn(int row, int column)
    {
        // Checks to see if the spot chosen is available
        // If no spot is available, sends out an error message
        if(model.isSpotFull(row, column))
            view.errorSpotFull();
        
        else
        {
            // Checks to see if its Player 1 Turn
            if(model.getP1Turn())
            {
                model.setMove(row, column);
                view.setButtonIcon(row, column,model.getP1Turn());
                model.setP1Turn(false);
                
                //Checks to see if player 1 won the game
                if(model.playerWon("X"))
                {
                    if(view.playerWonView(model.getPlayer1Name()) == JOptionPane.YES_OPTION)
                    {
                        for(int k = 0; k < 3; k++)
                        {
                            for(int l = 0; l < 3; l++)
                            {
                                view.getGameboardButtons()[k][l].setText("");
                                view.getGameboardButtons()[k][l].setIcon(null);
                                model.getGameBoard()[k][l] = null;
                            }
                        }
                        model.setP1Turn(true);
                    }
                }
                
                // Checks to see if the board is full
                else if(model.isBoardFull())
                {
                    if(view.boardFull() == JOptionPane.YES_OPTION)
                    {
                        for(int k = 0; k < 3; k++)
                        {
                            for(int l = 0; l < 3; l++)
                            {
                                view.getGameboardButtons()[k][l].setText("");
                                view.getGameboardButtons()[k][l].setIcon(null);
                                model.getGameBoard()[k][l] = null;
                            }
                        }
                        model.setP1Turn(true);
                    }
                }
                
                
                // Checks to see if player 1 is playing against the computer
                else if(model.getAIEnabled())
                {
                    computerTurn();
                }
            }
            
            // If its the second player's turn
            else
            {
                model.setMove(row, column);
                view.setButtonIcon(row, column,model.getP1Turn());
                model.setP1Turn(true);
                
                //Checks to see if player 1 won the game
                if(model.playerWon("O"))
                {
                    if(view.playerWonView(model.getPlayer2Name()) == JOptionPane.YES_OPTION)
                    {
                        for(int k = 0; k < 3; k++)
                        {
                            for(int l = 0; l < 3; l++)
                            {
                                view.getGameboardButtons()[k][l].setText("");
                                view.getGameboardButtons()[k][l].setIcon(null);
                                model.getGameBoard()[k][l] = null;
                            }
                        }
                        model.setP1Turn(true);
                    }
                }
                
                // Checks to see if the board is full
                else if(model.isBoardFull())
                {
                    if(view.boardFull() == JOptionPane.YES_OPTION)
                    {
                        for(int k = 0; k < 3; k++)
                        {
                            for(int l = 0; l < 3; l++)
                            {
                                view.getGameboardButtons()[k][l].setText("");
                                view.getGameboardButtons()[k][l].setIcon(null);
                                model.getGameBoard()[k][l] = null;
                            }
                        }
                        model.setP1Turn(true);
                    }
                }
            }
        }
        
    }
    
    private void saveGame()
    {
        
    }

    private void closeConnection() throws IOException
    {
        view.displayMessage("Closing Connection....");
        try{
            model.getOutput().close();
            model.getInput().close();
            model.getSocket().close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
   
    // Connects to the server
    public boolean connectToServer(){
        try {
            model.setSocket(new Socket(model.getIp(), model.getPort()));
            model.setOutput(new ObjectOutputStream(model.getSocket().getOutputStream()));
            model.setInput(new ObjectInputStream(model.getSocket().getInputStream()));
            model.setAccepted(true);
            model.setBoard();
            view.setGameBoardView();
            
        } catch (Exception e) {
           // view.displayMessage("Unable to connect to the address: " + model.getIp() + ":" + model.getPort() + " | Starting a server");
            return false;
        }
        view.displayMessage("Successfully connected to the server");
        return true;
    }
    
    // Waits until a socket is connected to the server socket
    public void runServer() throws IOException
    {
        model.setSocket(null);
        model.setServerSocket(new ServerSocket(model.getPort()));
        try{
            model.setSocket(model.getServerSocket().accept());
            model.setOutput(new ObjectOutputStream(model.getSocket().getOutputStream()));
            model.setInput(new ObjectInputStream(model.getSocket().getInputStream()));
            view.displayMessage("Accepted Client Request");
            model.setBoard();
            view.setGameBoardView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == view.getSaveButtons()[0])
            {
                saveGame();
                System.exit(0);
            }
            
            else if(e.getSource() == view.getSaveButtons()[1])
                System.exit(0);
            
            else if(e.getSource() == view.getExitButton())
                System.exit(0);
            
            else if(e.getSource() == view.getHowToPlayButton())
                view.howToPlayView();
            
            else if(e.getSource() == view.getPlayAIButton())
            {
                model.setPlayer1Name("Player 1");
                model.setPlayer2Name("Computer");
                model.setP1Turn(true);
                model.setAIEnabled(true);
                view.displayMessage("Started playign comp");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(boardController.class.getName()).log(Level.SEVERE, null, ex);
                }
                model.setBoard();
                view.setGameBoardView();
            }
            
            else if(e.getSource() == view.getPlayFriendButton())
            {
                model.setPlayer1Name("Player 1");
                model.setPlayer2Name("Player 2");
                model.setAIEnabled(false);
                model.setP1Turn(true);
                model.setBoard();
                view.setGameBoardView();
            }
            
            else if(e.getSource() == view.getPlayOnlineButton())
            {
                model.setPlayer1Name("Player 1");
                model.setPlayer2Name("Player 2");
                model.setAIEnabled(false);
                model.setOnlineMode(true);
                model.setP1Turn(true);
                model.setIp(view.getIPAddressView().toLowerCase());
                model.setPort(view.getPortNumber());
                
                if(!connectToServer())
                {
                    try {
                        runServer();
                    } catch (IOException ex) {
                        Logger.getLogger(boardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                else
                {
                    connectToServer();
                    model.setP1Turn(true);
                    model.setAccepted(true);
                    model.setBoard();
                    view.setGameBoardView();
                }
            }
            
            // If its one of the game board buttons
            
            else if(e.getSource() == view.getGameboardButtons()[0][0]) {
                playerTurn(0, 0);
            }
            else if(e.getSource() == view.getGameboardButtons()[0][1]) {
                playerTurn(0, 1);
            }
            else if(e.getSource() == view.getGameboardButtons()[0][2]) {
                playerTurn(0, 2);
            }
            else if(e.getSource() == view.getGameboardButtons()[1][0]) {
                playerTurn(1, 0);
            }
            else if(e.getSource() == view.getGameboardButtons()[1][1]) {
                playerTurn(1, 1);
            }
            else if(e.getSource() == view.getGameboardButtons()[1][2]) {
                playerTurn(1, 2);
            }
            else if(e.getSource() == view.getGameboardButtons()[2][0]) {
                playerTurn(2, 0);
            }
            else if(e.getSource() == view.getGameboardButtons()[2][1]) {
                playerTurn(2, 1);
            }
            else if(e.getSource() == view.getGameboardButtons()[2][2]) {
                playerTurn(2, 2);
            }
        }
        
    }
}
