
public class ReversiBoard implements BoardInterface {
    private int[][] boardArray = null;

    public ReversiBoard(){
        boardArray = new int[8][8];
        this.setupBoard();
        this.setInitialPieces();
    }

    private void setupBoard(){
        for(int i = 0; i < boardArray.length; i++){
            for(int j = 0; j < boardArray[0].length; j++){
                boardArray[i][j] = 0;
            }
        }
    }

    private void setInitialPieces(){
        boardArray[3][3] = 1;
        boardArray[3][4] = 2;
        boardArray[4][3] = 1;
        boardArray[4][4] = 2;
    }

    public boolean makeMove(int i, int j, int player){
        if(i < 0 || i >= boardArray.length){return false;}
        if(j < 0 || j >= boardArray[0].length){return false;}

        if(boardArray[i][j] != 0){
            boardArray[i][j] = player;
            return true;
        }
        else return false;
    }

    public int[][] getBoard(){
        return boardArray;
    }

    public int checkGameState(){
        //TODO: check if there is a winner, tie, or continue_game
        return 0;
    }
}
