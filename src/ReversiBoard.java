import java.util.ArrayList;
import java.util.Collections;

public class ReversiBoard implements BoardInterface {
    private final int OPEN = 0;
    private final int MININDEX = 0;
    private final int PLAYER1 = 1;
    private final int PLAYER2 = 2;
    private final int INVALIDMOVE = -1;

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

    public int[] getBoard(){ return boardArray; }

    public boolean makeMove(int move, int player){
        if(validateChosenMove(move, player)){
            boardArray[move] = player;
            updateBoard(player, move);
            return true;
        } else return false;
    }

    private boolean validateChosenMove(int index, int player){
        if(indexIsOutOfBounds(index)) return false;
        ArrayList<Integer> validMoves = this.getValidMoves(player);
        return validMoves.contains(index);
    }

    public boolean indexIsOutOfBounds(int index){
        return index < MININDEX || index >= boardArray.length;
    }

    public ArrayList<Integer> getValidMoves(int player){
        ArrayList<Integer> playerPieces = findPlayerPieces(player);
        ArrayList<Integer> legalMoves = new ArrayList<Integer>();
        for (Integer playerPiece : playerPieces)
            legalMoves = findLegalMoves(playerPiece, player, legalMoves);
        Collections.sort(legalMoves);
        return legalMoves;
    }

    public ArrayList<Integer> findLegalMoves(int index, int player, ArrayList<Integer> moves){
        for (int offset : getOffsets()) {
            int move = findMove(index, offset, player);
            if (move != INVALIDMOVE && move != index + offset && !moves.contains(move))
                moves.add(move);
        }
        return moves;
    }

    public int findMove(int index, int offset, int player){
        int nextIndex = index+offset;
        if(indexIsOutOfBounds(nextIndex)) return INVALIDMOVE;
        else if(boardArray[nextIndex] == opponent(player))
                return findMove(nextIndex, offset, player);   //recursive call on findMove
        else if (boardArray[nextIndex] == OPEN) return nextIndex;
        else return INVALIDMOVE;
    }

    public void updateBoard(int player, int move){
        for(int offset : getOffsets()) searchForUpdate(move, move, offset, player);
    }

    public int[] getOffsets(){
        return new int[]{1, -1, -8, 8, 9, -9, 7, -7};
    }

    public boolean searchForUpdate(int origIndex, int currIndex, int offset, int player){
       int nextIndex = currIndex + offset;
        if(indexIsOutOfBounds(nextIndex) || boardArray[nextIndex] == OPEN) return false;

        if(boardArray[nextIndex] == opponent(player)){
            boolean update = searchForUpdate(origIndex, nextIndex, offset, player);
            if(update){
                boardArray[nextIndex] = player;
                return true;
            }
        }
        else if(boardArray[nextIndex] == player && notNextToOriginalIndex(origIndex,offset,nextIndex))
             return true;
        return false;
    }

    public boolean notNextToOriginalIndex(int origIndex, int offset, int testIndex){
        return boardArray[testIndex] != origIndex+offset;
    }

    public ArrayList<Integer> findPlayerPieces(int player){
        ArrayList<Integer> playerPieces = new ArrayList<Integer>();
        for(int i = 0; i < boardArray.length; i++)
            if(boardArray[i] == player) playerPieces.add(i);
        return playerPieces;
    }

    public boolean checkGameState(){
        //TODO: check if there is a winner, tie, or continue_game
        ArrayList<Integer> player1Moves = getValidMoves(PLAYER1);
        ArrayList<Integer> player2Moves = getValidMoves(PLAYER2);
        return player1Moves.isEmpty() && player2Moves.isEmpty();
    }

    public int getScore(int player){
        int score = 0;
        for(int piece : boardArray) if(piece == player) score++;
        return score;
    }

    public int opponent(int player){
        return player == PLAYER1 ? PLAYER2 : PLAYER1;
    }

    public void printBoard(){
        int[] boardArray = this.getBoard();
        for(int i = 0; i < 64; i++){
            if(String.valueOf(boardArray[i]).length() == 1)
                System.out.print(boardArray[i]+"  ");
            else if(String.valueOf(boardArray[i]).length() > 1)
                System.out.print(boardArray[i]+" ");

            if((i+1)%8 == 0) System.out.println("");
        }
    }
}
