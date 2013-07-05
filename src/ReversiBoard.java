import java.util.ArrayList;

public class ReversiBoard implements BoardInterface {
    private static int OPEN = 0;
    private static int MININDEX = 0;
    private static int PLAYER1 = 1;
    private static int PLAYER2 = 2;

    private int[] boardArray = null;

    public ReversiBoard(){
        boardArray = new int[64];
        this.initializeBoard();
        this.setInitialPieces();
    }

    private void initializeBoard(){
        for(int i = 0; i < boardArray.length; i++){
            boardArray[i] = OPEN;
        }
    }

    private void setInitialPieces(){
      boardArray[27] = PLAYER1;
      boardArray[36] = PLAYER1;
      boardArray[28] = PLAYER2;
      boardArray[35] = PLAYER2;

    }

    public boolean makeMove(int index, int player){
        if(validMove(index, player)){
            boardArray[index] = player;
            updateBoard(player);
            return true;
        } else return false;
    }

    private boolean validMove(int index, int player){
        if(indexIsOutOfBounds(index)){return false;}

        ArrayList<Integer> validMoves = this.getValidMoves(player);
        return validMoves.contains(index);
    }

    public boolean indexIsOutOfBounds(int index){
        return index < MININDEX || index >= boardArray.length;
    }

    public int[] getBoard(){
        return boardArray;
    }

    public ArrayList<Integer> getValidMoves(int player){
        ArrayList<Integer> playerPieces = findPlayerPieces(player);
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        for (Integer playerPiece : playerPieces) {
            legalMoves = findLegalMoves(playerPiece, player, legalMoves);
        }
        return legalMoves;
    }

    public ArrayList<Integer> findLegalMoves(int index, int player, ArrayList<Integer> moves){
        int[] offset = getOffsets();

        for (int anOffset : offset) {
            int result = findMove(index, anOffset, player);
            if (result != -1 && result != index + anOffset && !moves.contains(result)) {
                moves.add(result);
            }
        }
        return moves;
    }

    public int findMove(int index, int offset, int player){
        int testIndex = index+offset;
        if(indexIsOutOfBounds(index)){
            return -1;
        }
        else if(boardArray[testIndex] == opponent(player)){
            return findMove(testIndex, offset, player);   //recursive call on findMove
        }
        else if (boardArray[testIndex] == OPEN){
            return testIndex;
        }
        else return -1;
    }

    public void updateBoard(int player){
        ArrayList<Integer> playerPieces = findPlayerPieces(player);
        int[] offset = getOffsets();
        for (Integer playerPiece : playerPieces) {
            for (int anOffset : offset) searchForUpdate(playerPiece, playerPiece, anOffset, player);
        }
    }

    public int[] getOffsets(){
        return new int[]{1, -1, -8, 8, 9, -9, 7, -7};
    }

    public boolean searchForUpdate(int origIndex, int currIndex, int offset, int player){
       int testIndex = currIndex + offset;
        if(indexIsOutOfBounds(testIndex) || boardArray[testIndex] == OPEN){return false;}

        if(boardArray[testIndex] == opponent(player)){
            boolean update = searchForUpdate(origIndex, testIndex, offset, player);
            if(update){
                boardArray[testIndex] = player;
                return true;
            }
        }
        else if(boardArray[testIndex] == player && boardArray[testIndex] != origIndex+offset){
             return true;
        }
        return false;
    }

    public ArrayList<Integer> findPlayerPieces(int player){
        ArrayList<Integer> playerPieces = new ArrayList<Integer>();
        for(int i = 0; i < boardArray.length; i++){
            if(boardArray[i] == player){
                 playerPieces.add(i);
            }
        }
        return playerPieces;
    }

    public boolean checkGameState(){
        //TODO: check if there is a winner, tie, or continue_game
        ArrayList<Integer> player1Moves = getValidMoves(PLAYER1);
        ArrayList<Integer> player2Moves = getValidMoves(PLAYER2);
        return player1Moves.isEmpty() && player2Moves.isEmpty();
    }

    public int opponent(int player){
        if(player == PLAYER1){return PLAYER2;}
        else return PLAYER1;
    }

    public void printBoard(){
        int[] boardArray = this.getBoard();
        for(int i = 0; i < 64; i++){
            if(String.valueOf(boardArray[i]).length() == 1){
                System.out.print(boardArray[i]+"  ");
            }
            else if(String.valueOf(boardArray[i]).length() > 1){
                System.out.print(boardArray[i]+" ");
            }
            if((i+1)%8 == 0){
                System.out.println("");
            }
        }
    }
}
