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
import static java.lang.Integer.max;
import static java.lang.Integer.min;
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
    
    private int minimax(String gameBoard[][], int depth, boolean isMax)
    {      
        if(model.playerWon("O"))
            return 10+depth;
        
        else if(model.playerWon("X"))
            return -10-depth;
        
        else if(model.isBoardFull())
            return 0-depth;
        
        // If it the maximizer's turn aka the computers turn
        else if(isMax)
        {
            model.setP1Turn(!isMax);
            int best = -1000;
            for(int i = 0; i < 3; i++)
            {
                for(int j = 0; j < 3; j++)
                {
                    // Checks for an empty cell
                    if(model.getGameBoard()[i][j] == "" || model.getGameBoard()[i][j] == null)
                    {
                        model.setMove(i, j);
                        best = max(best,minimax(model.getGameBoard(), depth+1, !isMax));                       
                        model.getGameBoard()[i][j] = null;
                    }
                }
            }
            return best;
        }
        
        // If its the minimizer's turn 
        else
        {
            int best = 1000;
            model.setP1Turn(!isMax);
            for(int i = 0; i < 3; i++)
            {
                for(int j = 0; j < 3; j++)
                {
                    if(model.getGameBoard()[i][j] == "" || model.getGameBoard()[i][j] == null)
                    {
                        model.setMove(i,j);
                        best = min(best, minimax(model.getGameBoard(), depth+1, !isMax));
                        model.getGameBoard()[i][j] = null;
                    }
                }
            }
            return best;
        }  
    }
   
    private void findComputerMove()
    {
        int bestVal = -1000;
        int row = -1;
        int column = -1;
        
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if(model.getGameBoard()[i][j] == "" || model.getGameBoard()[i][j] == null)
                {
                    //model.setP1Turn(false);
                    model.setMove(i, j);
                    int moveVal = minimax(model.getGameBoard(), 0, false);
                    model.getGameBoard()[i][j] = null;

                    if(moveVal >= bestVal)
                    {
                        row = i;
                        column = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        model.setP1Turn(false);
        view.getGameboardButtons()[row][column].doClick();
        model.setP1Turn(true);
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
                    //computerTurn();
                    findComputerMove();
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
    
    // Client code
    public void startSender() 
    {
        try {
            /**/
            model.setSocket(new Socket("localhost", 60010));
            model.setOutput(new ObjectOutputStream((model.getSocket().getOutputStream())));
           // model.getOutput().writeBytes("Hello World");
           // model.getOutput().flush();
            model.getOutput().writeObject(89);
            model.getOutput().flush();
            model.getOutput().writeObject(89768);
            model.getOutput().flush();
            model.getOutput().writeObject(!model.getP1Turn());
            model.getOutput().flush();
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Sends the online user moves to the other player
    public void sendClick(int row, int column, boolean playerTurn) throws IOException
    {
        model.getOutput().writeObject(row);
        model.getOutput().flush();
        model.getOutput().writeObject(column);
        model.getOutput().flush();
        model.getOutput().writeObject(playerTurn);
        model.getOutput().flush();
    }
    
    // Waits for the user to input a click and then sets it accordingl
    public void recieveClick() throws IOException
    {
        do{
            try {
                    int row = (int) model.getInput().readObject();
                    int column = (int) model.getInput().readObject();
                    boolean playerTurn =(boolean)model.getInput().readObject(); 
                    model.setP1Turn(playerTurn);
                    view.setButtonIcon(row, column,model.getP1Turn());
                    model.setMove(row, row);                
                    } catch (ClassNotFoundException classNotFound) {
                        System.out.println("\n idk wtf that user sent!");
                    }    
        }while (model.getP1Turn() != true);   
    }
    
    public void startServer()
    {
        // Sets up the server
        try {
                // Opens a new Server Socket and waits for a client to connect
                model.setServerSocket(new ServerSocket(60010));
                
                while(model.isOnlineMode())
                {
                    try 
                    {
                        // Client accepts the Server socket
                        model.setSocket(model.getServerSocket().accept());    
                        // Initializing the stream sockets for input and output
                        model.setInput(new ObjectInputStream((model.getSocket().getInputStream())));
                        model.setOutput(new ObjectOutputStream((model.getSocket().getOutputStream())));
                        model.getOutput().flush();
                        // FOR TESTING ONLY
                        // Reads the dummy data sent from the client and prints it to the screen
                         try {
                                int row = (int) model.getInput().readObject();
                                int column = (int) model.getInput().readObject();
                                boolean playerTurn =(boolean)model.getInput().readObject();
                                System.out.println(row);
                                System.out.println(column);
                                System.out.println(playerTurn);
                                
                                model.getOutput().writeObject(32);
                                model.getOutput().flush();
                                model.getOutput().writeObject(453);
                                model.getOutput().flush();
                                model.getOutput().writeObject(!model.getP1Turn());
                                model.getOutput().flush();
                            } 
                        catch (ClassNotFoundException classNotFound) {
                                System.out.println("\n idk wtf that user sent!");
                            }
                         
                            //whileChatting();
                    } catch (EOFException ex) {
                        System.out.println("\n Server Ended the connection!");
                    }finally{
                        System.out.println("\n Closing connections...\n");
                            try {
                                    model.getOutput().close();
                                    model.getInput().close();
                                    model.getSocket().close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                                            }
                                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
        
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            // Saves the game
            if(e.getSource() == view.getSaveButtons()[0])
            {
                saveGame();
                System.exit(0);
            }
            
            // Exit out the game
            else if(e.getSource() == view.getSaveButtons()[1])
            {
                System.exit(0);
            }
            
            // If user chooses online play and chooses to be the host/server
            else if(e.getSource() == view.getServerButton())
            {
                model.setOnlineMode(true);
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
            }
            
            //If user chooses online play and chooses to be the client
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
                    recieveClick();
                } catch (IOException ex) {
                    Logger.getLogger(boardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            // If users chooses to exit the game
            else if(e.getSource() == view.getExitButton())
                System.exit(0);
            
            //If user wants to see the How-To-Play option
            else if(e.getSource() == view.getHowToPlayButton())
                view.howToPlayView();
            
            // If user chooses to play against the computer
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
            
            //If user chooses to play against a local friend
            else if(e.getSource() == view.getPlayFriendButton())
            {
                model.setPlayer1Name("Player 1");
                model.setPlayer2Name("Player 2");
                model.setAIEnabled(false);
                model.setP1Turn(true);
                model.setBoard();
                view.setGameBoardView();
            }
            
            //If user chooses to play online, displays the screen selection
            // for either server or client
            else if(e.getSource() == view.getPlayOnlineButton()){
                view.onlinePlayView();
            }
            
            else if(e.getSource() == view.getGameboardButtons()[0][0]) 
            {
                if(model.isOnlineMode())
                {
                    // If its the server/player 1 turn
                    if(model.getP1Turn())
                        try {
                            sendClick(0,0,true);
                    } catch (IOException ex) {
                        Logger.getLogger(boardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    else
                        try {
                            sendClick(0,0,false);
                    } catch (IOException ex) {
                        Logger.getLogger(boardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        recieveClick();
                    } catch (IOException ex) {
                        Logger.getLogger(boardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
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
