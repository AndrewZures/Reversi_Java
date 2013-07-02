public interface BoardInterface {

    public int[][] getBoard();
    public boolean makeMove(int i, int j, int player);
    public int checkGameState();

}
