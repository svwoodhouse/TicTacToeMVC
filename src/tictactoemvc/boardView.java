//-------------------------------
// Author: Sydnee Woodhouse
// Date : 1/19/2018
// BoardView: View represents the visual of the data 
// stored in the model
//---------
package tictactoemvc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class boardView extends JFrame 
{
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel mainMenuPanel;
    private JPanel saveGamePanel;
    private JPanel gameGridPanel;
    private JPanel messageDisplayPanel;
    private JButton playAIButton;
    private JButton playFriendButton;
    private JButton playOnlineButton;
    private JButton loadGameButton;
    private JButton howToPlayButton;
    private JButton exitButton;
    private JButton saveGameButton;
    private JButton[][] gameboardButtons;
    private JButton[] saveButtons;
    private JLabel logoLabel;
    private JLabel waitLabel;
    private ImageIcon redX;
    private ImageIcon blueO;
    
    public boardView()
    {
        // Setup for the main Game Frame and Game Panel
        mainFrame = new JFrame("Tic Tac Toe");
        mainFrame.setSize(600, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainPanel = new JPanel();
        
        //Panels for the game and button array containing the game buttons
        saveGamePanel = new JPanel();
        saveGamePanel.setLayout(new GridLayout(1,2));
        gameGridPanel = new JPanel();
        gameGridPanel.setLayout(new GridLayout(3,3));
        gameboardButtons = new JButton[3][3];
        saveButtons = new JButton[2];
        

        // Panel that conatins the Main Menu buttons
        mainMenuPanel = new JPanel();
        
        messageDisplayPanel = new JPanel();
        messageDisplayPanel.setLayout(new FlowLayout());
        
        // Tic Tac Toe game logo
        logoLabel = new JLabel(new ImageIcon(getClass().getResource("tictactoe_logo.png")));
        waitLabel = new JLabel("Label" ,JLabel.CENTER);
        
        // Buttons for the main menu
        playAIButton= new JButton("Play Against AI");
        playFriendButton = new JButton("Play Against a Friend");
        playOnlineButton = new JButton("Play Online");
        loadGameButton = new JButton("Load Game");
        howToPlayButton = new JButton("How To Play");
        exitButton = new JButton("Exit");

        // Fills the array to contain the game buttons
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                gameboardButtons[i][j] = new JButton();
                gameboardButtons[i][j].setBackground(Color.white);
                gameboardButtons[i][j].setOpaque(true);
            }
        }
        saveButtons[0] = new JButton("Save");
        saveButtons[1] = new JButton("Exit");
        
        playAIButton.setPreferredSize(new Dimension(100, 100));
        playFriendButton.setPreferredSize(new Dimension(100, 100));
        playOnlineButton.setPreferredSize(new Dimension(100, 100));
        loadGameButton.setPreferredSize(new Dimension(100, 100));
        howToPlayButton.setPreferredSize(new Dimension(100, 100));
        exitButton.setPreferredSize(new Dimension(100, 100));
        
        // Creates a GridLayout to contain the buttons in the JPanel
        // Adds the buttons to the buttonPanel
        mainMenuPanel.setLayout(new GridLayout(2,3));
        mainMenuPanel.add(playAIButton);
        mainMenuPanel.add(playFriendButton);
        mainMenuPanel.add(playOnlineButton);
        mainMenuPanel.add(loadGameButton);
        mainMenuPanel.add(howToPlayButton);
        mainMenuPanel.add(exitButton);
        
        messageDisplayPanel.add(waitLabel);
        
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                gameGridPanel.add(gameboardButtons[i][j]);
            }
        }
        saveGamePanel.add(saveButtons[0]);
        saveGamePanel.add(saveButtons[1]);
        
        // Sets the main panel to the mainMenuPanel
        // Adds it to the main frame
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(mainMenuPanel, BorderLayout.SOUTH);
        mainPanel.add(logoLabel, BorderLayout.NORTH);
        mainFrame.getContentPane().add(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        
        redX = new ImageIcon(getClass().getResource("red_X.png"));
        blueO = new ImageIcon(getClass().getResource("o_icon.png"));
    }
    
    public void howToPlayView()
    {
        JOptionPane.showMessageDialog(null, "RULES FOR TIC-TAC-TOE\n" + "\n" + "1. The game is played on a grid that's 3 squares by 3 squares.\n" +
        "\n" + "2. You are X, your friend (or the computer in this case) is O. Players take turns putting their marks in empty squares.\n" +
        "\n" + "3. The first player to get 3 of her marks in a row (up, down, across, or diagonally) is the winner.\n" +
        "\n" + "4. When all 9 squares are full, the game is over. If no player has 3 marks in a row, the game ends in a tie.");
    }
    
    public void setGameBoardView()
    {
        mainMenuPanel.setVisible(false);
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(gameGridPanel,BorderLayout.CENTER);
        mainPanel.add(saveGamePanel, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
        mainFrame.getContentPane().add(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    public void errorSpotFull(){
        JOptionPane.showMessageDialog(mainFrame, "Error. Choose another spot"); 
    }
    
    public void displayMessage(String message){
        //JOptionPane.showMessageDialog(mainFrame, message);
        mainMenuPanel.setVisible(false);
        waitLabel.removeAll();;
        waitLabel.setText(message);
        waitLabel.revalidate();
        waitLabel.repaint();
        messageDisplayPanel.removeAll();
        messageDisplayPanel.setLayout(new FlowLayout());
        messageDisplayPanel.add(waitLabel);
        messageDisplayPanel.revalidate();
        messageDisplayPanel.repaint();
        mainPanel.removeAll();
        mainPanel.add(messageDisplayPanel,BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
        mainFrame.getContentPane().add(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
        
    }
    public String getIPAddressView()
    {
        return (String)JOptionPane.showInputDialog("Please input IP Address");
    }
    public void serverConnectionEndedView() {
        JOptionPane.showMessageDialog(mainFrame, "Server Ended Connection!");
    }
    public void acceptedClientView() {
        JOptionPane.showMessageDialog(mainFrame, "Accepted Client Request");
    }
    public int getPortNumber()
    {
        return Integer.parseInt((String)JOptionPane.showInputDialog("Please input Port Number"));
    }
    
    public int boardFull()
    {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Game Board Full. Would you like to restart?");
        return dialogResult;
    } 
    
    public int playerWonView(String playerName)
    {
        int dialogResult = JOptionPane.showConfirmDialog(null, playerName + " Won! Would you like to replay?");
        return dialogResult;
    }
    
    public static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight)
    {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
    
    public void setButtonIcon(int row, int column, boolean p1Turn)
    {
        if(p1Turn)
        {
            gameboardButtons[row][column].setText("X");
            gameboardButtons[row][column].setIcon(resizeIcon(redX,180,160));
        }
        
        else
        {
            gameboardButtons[row][column].setText("O");
            gameboardButtons[row][column].setIcon(resizeIcon(blueO,180,160));
        }
    }
    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JPanel getMainMenuPanel() {
        return mainMenuPanel;
    }

    public void setMainMenuPanel(JPanel mainMenuPanel) {
        this.mainMenuPanel = mainMenuPanel;
    }

    public JPanel getSaveGamePanel() {
        return saveGamePanel;
    }

    public void setSaveGamePanel(JPanel saveGamePanel) {
        this.saveGamePanel = saveGamePanel;
    }

    public JPanel getGameGridPanel() {
        return gameGridPanel;
    }

    public void setGameGridPanel(JPanel gameGridPanel) {
        this.gameGridPanel = gameGridPanel;
    }

    public JButton getPlayAIButton() {
        return playAIButton;
    }

    public void setPlayAIButton(JButton playAIButton) {
        this.playAIButton = playAIButton;
    }

    public JButton getPlayFriendButton() {
        return playFriendButton;
    }

    public void setPlayFriendButton(JButton playFriendButton) {
        this.playFriendButton = playFriendButton;
    }

    public JButton getPlayOnlineButton() {
        return playOnlineButton;
    }

    public void setPlayOnlineButton(JButton playOnlineButton) {
        this.playOnlineButton = playOnlineButton;
    }

    public JButton getLoadGameButton() {
        return loadGameButton;
    }

    public void setLoadGameButton(JButton loadGameButton) {
        this.loadGameButton = loadGameButton;
    }

    public JButton getHowToPlayButton() {
        return howToPlayButton;
    }

    public void setHowToPlayButton(JButton howToPlayButton) {
        this.howToPlayButton = howToPlayButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public void setExitButton(JButton exitButton) {
        this.exitButton = exitButton;
    }

    public JButton getSaveGameButton() {
        return saveGameButton;
    }

    public void setSaveGameButton(JButton saveGameButton) {
        this.saveGameButton = saveGameButton;
    }

    public JButton[][] getGameboardButtons() {
        return gameboardButtons;
    }

    public void setGameboardButtons(JButton[][] gameboardButtons) {
        this.gameboardButtons = gameboardButtons;
    }

    public JButton[] getSaveButtons() {
        return saveButtons;
    }

    public void setSaveButtons(JButton[] saveButtons) {
        this.saveButtons = saveButtons;
    }

    public JLabel getLogoLabel() {
        return logoLabel;
    }

    public void setLogoLabel(JLabel logoLabel) {
        this.logoLabel = logoLabel;
    }
    
    public ImageIcon getRedX() {
        return redX;
    }

    public void setRedX(ImageIcon redX) {
        this.redX = redX;
    }

    public ImageIcon getBlueO() {
        return blueO;
    }

    public void setBlueO(ImageIcon blueO) {
        this.blueO = blueO;
    }

    public JLabel getWaitLabel() {
        return waitLabel;
    }

    public void setWaitLabel(JLabel waitLabel) {
        this.waitLabel = waitLabel;
    }
   
}
