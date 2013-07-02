
public class ReversiBoard implements BoardInterface {
    private int[] boardArray = null;

    public ReversiBoard(){
        boardArray = new int[64];
        this.setupBoard();
        this.setInitialPieces();
    }

    private void setupBoard(){
        for(int i = 0; i < boardArray.length; i++){
            boardArray[i] = 0;
        }
    }

    private void setInitialPieces(){
          boardArray[27] = 1;
          boardArray[28] = 2;
          boardArray[35] = 2;
          boardArray[36] = 1;
    }

    public boolean makeMove(int index, int player){
        if(index < 0 || index >= boardArray.length){return false;}

        if(boardArray[index] != 0){
            boardArray[index] = player;
            return true;
        }
        else return false;
    }

    public int[] getBoard(){
        return boardArray;
    }

    private int[] getValidMoves(int player){
        int[] playerPieces = findPlayersPieces(player);
        for(int i = 0; i < playerPieces.length; i++){
            findAvailableMoves(playerPieces[i], player);
        }
    }

    private int[] findAvailableMoves(int index, int player){
        int[] offset = {1,-1,-8,8,-9,-7,7,9};
        int[] results = new int[8];
        int k = 0;
        int result;
        for(int i = 0; i < offset.length; i++){
           if((result = findMove(index, offset[i], player)) != 0){
               results[k] = result;
               k++;
            }
        }
        return results;
    }

    private int findMove(int index, int offset, int player){
        int testIndex = index+offset;
        if(boardArray[testIndex] < 0 || boardArray[testIndex] >= boardArray.length){
            return 0;
        }
        if(boardArray[testIndex] != 0 && boardArray[testIndex] != player){
            return findMove(testIndex, offset, player);
        }
        else if(boardArray[testIndex] == player){
            return 0;
        }
        else if (boardArray[testIndex] == 0){
            if(testIndex == index+offset){
                return 0;
            }
            else return testIndex;
        }
         return 0;
    }

    private int[] findPlayersPieces(int player){
        int k = 0;
        int[] temp = new int[64];
        for(int i = 0; i < boardArray.length; i++){
            if(boardArray[i] == player){
                temp[k] = i;
                k++;
            }
        }

        int[] playerPieces = new int[k-1];
        for (int i = 0; i < k; i++){
             playerPieces[i] = temp[i];
        }
        return playerPieces;
    }

    public int checkGameState(){
        //TODO: check if there is a winner, tie, or continue_game
        return 0;
    }
}
