public interface BoardInterface {

    public int[] getBoard();
    public boolean makeMove(int move, int player);
    public boolean checkGameState();
    public void printBoard();

}
