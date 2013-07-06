import java.util.ArrayList;

public interface BoardInterface {

    public int[] getBoard();
    public int getScore(int player);
    public boolean makeMove(int move, int player);
    public boolean checkGameState();
    public ArrayList<Integer> getValidMoves(int player);
    public void printBoard();

}
