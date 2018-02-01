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
                view.setButtonIcon(row, column,model.getPlayer1Name());
                model.setP1Turn(false);
            }
        }
        
    }
    
    private void saveGame()
    {
        
    }
       
    class ButtonListener implements ActionListener{
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
            
            // If its one of the game board buttons


        }
        
    }
}
