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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
        view.getServerButton().addActionListener(listener);
        view.getClientButton().addActionListener(listener);
        
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
    
    public void startSender() 
    {
        try {
            model.setSocket(new Socket("localhost", 60010));
            model.setOutput(new DataOutputStream((model.getSocket().getOutputStream())));
            model.getOutput().writeChars("Hello World");
            model.getOutput().flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendClick() throws IOException
    {
        model.getOutput().writeInt(0);
        model.getOutput().flush();
        model.getOutput().writeInt(1);
        model.getOutput().flush();
        model.getOutput().writeBoolean(!model.getP1Turn());
        model.getOutput().flush();
    }
    
    public void recieveClick() throws IOException
    {
        int row = model.getInput().readInt();
        int column = model.getInput().readInt();
        boolean playerTurn = model.getInput().readBoolean();
        
        System.out.println(row);
        System.out.println(column);
        System.out.println(playerTurn);
    }
    
    public void startServer()
    {
        try {
                model.setServerSocket(new ServerSocket(60010));
                model.setSocket(model.getServerSocket().accept());
                model.setInput(new DataInputStream((model.getSocket().getInputStream())));
                String line = model.getInput().toString();
                System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
        
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            // Checks to see if online mode is on and disables the buttons for the player
            // waiting for their turn
            /*
            if(model.isOnlineMode())
            {
                // If its the server/ player 1 turn, disables the buttons for player 2/client
                if(model.getP1Turn() && model.isClient())
                {
                    for(int i = 0; i < 3; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            view.getGameboardButtons()[i][j].setEnabled(false);
                        }
                    }
                }
                
                // If its the clients turn enables the button
                else if(!model.getP1Turn() && model.isClient())
                {
                    for(int i = 0; i < 3; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            view.getGameboardButtons()[i][j].setEnabled(true);
                        }
                    }
                }
                
                // if its the player 1 turn, enables the buttons
                else if(model.getP1Turn() && model.isServer())
                {
                    for(int i = 0; i < 3; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            view.getGameboardButtons()[i][j].setEnabled(true);
                        }
                    }
                }
                
                else
                {
                    for(int i = 0; i < 3; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            view.getGameboardButtons()[i][j].setEnabled(false);
                        }
                    }
                }
            }
*/
            
            if(e.getSource() == view.getSaveButtons()[0])
            {
                saveGame();
                System.exit(0);
            }
            
            else if(e.getSource() == view.getSaveButtons()[1])
            {
                System.exit(0);
            }
            
            else if(e.getSource() == view.getServerButton())
            {
                startServer();
                model.setAIEnabled(false);
                model.setP1Turn(true);
                model.setAccepted(true);
                model.setServer(true);
                model.setClient(false);
                model.setOnlineMode(true);
                model.setPlayer1Name("Player 1 - Server");
                model.setPlayer2Name("Player 2 - Client");
                view.displayMessage("Client Accepted Challlenge");
                model.setBoard();
                view.setGameBoardView();
                try {
                    sendClick();
                } catch (IOException ex) {
                    Logger.getLogger(boardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            else if(e.getSource() == view.getClientButton()) {
                startSender();
                model.setAIEnabled(false);
                model.setP1Turn(true);
                model.setAccepted(true);
                model.setServer(false);
                model.setClient(true);
                model.setOnlineMode(true);
                model.setPlayer1Name("Player 1 - Server");
                model.setPlayer2Name("Player 2 - Client");
                view.displayMessage("Server Accepted Challlenge");
                model.setBoard();
                view.setGameBoardView();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(boardController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    recieveClick();
                } catch (IOException ex) {
                    Logger.getLogger(boardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
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
                view.displayMessage("Started playing against computer");
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
            
            //if the online feature is enabled
          //  else if(model.isOnlineMode())
         //   {
                
         //   }
            else if(e.getSource() == view.getPlayOnlineButton()){
                view.onlinePlayView();
            }
            
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
