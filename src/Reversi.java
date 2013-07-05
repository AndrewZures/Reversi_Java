
public class Reversi {
    BoardInterface board = null;

    public static void main(String[] args){
        BoardInterface board1 = new ReversiBoard();
        board1.printBoard();
        board1.makeMove(29, 1);
        board1.printBoard();
        board1.makeMove(21, 2);
        board1.printBoard();

    }

    public Reversi(){
        board = new ReversiBoard();
    }



}
