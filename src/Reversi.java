import java.util.Scanner;

public class Reversi {
    BoardInterface board = null;

    public static void main(String[] args){
        BoardInterface board1 = new ReversiBoard();
        Scanner sc = new Scanner(System.in);

        for(int j = 0; j < 10; j++){
            board1.printBoard();
            board1.makeMove(sc.nextInt(), 1);
            board1.printBoard();
            board1.makeMove(sc.nextInt(), 2);
        }
        board1.printBoard();

    }

    public Reversi(){
        board = new ReversiBoard();
    }



}
