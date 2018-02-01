package tictactoemvc;

public class TicTacToeMVC 
{
    public static void main(String[] args) 
    {
       boardModel model = new boardModel();
       boardView view = new boardView();
       boardController controller = new boardController(model, view);
       controller.initController();
    }
}
