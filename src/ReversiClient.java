import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ReversiClient{
    int port;
    String host;
    String playerName;
    BufferedReader serverIn = null;
    PrintWriter serverOut = null;
    Socket s = null;
    java.util.Scanner scan = new java.util.Scanner(System.in);

    public static void main(String[] args) throws Exception{
        new ReversiClient("localhost", 8189).go();
    }

    ReversiClient(String host, int port) throws Exception{
        this.host = host;
        this.port = port;
        s = new Socket(this.host, this.port);
        serverIn = new BufferedReader
                (new InputStreamReader(s.getInputStream()));
        serverOut = new PrintWriter
                (s.getOutputStream(), true);
        setPlayerName();
    }

    public void go() throws Exception{
        if(playerName.equalsIgnoreCase("Observer"))
            this.observerGo();
        else this.playerGo();
    }

    public void observerGo() throws Exception {
        System.out.println("YOU ARE AN OBSERVER");
        boolean gameOn = true;
        this.printPlayerTurn();
        this.updateBoard();
        String currentPlayer = getCurrentPlayer();
        String player;
        while(gameOn){
             player = getCurrentPlayer();
            if(!player.equalsIgnoreCase(currentPlayer)){
                this.printPlayerTurn();
                printScore();
                this.updateBoard();
                currentPlayer = player;
            }

            if(this.gameOver()){
                printGameResult();
            }
            Thread.sleep(3000);
        }
    }

    public void playerGo() throws Exception{
        System.out.println("YOU ARE: "+playerName);
        boolean gameOn = true;
        while(gameOn){

            if(this.gameOver()){
                printGameResult();
                if(this.askForNewGame()){
                    this.resetBoard();
                    this.printGameInstructions();
                }
                else{break;}
            }

            if(!myturn()) {
                this.updateUser();
                this.printWaitMessage();
            }
            while(!myturn()) Thread.sleep(3000);

            if(myturn()){
                this.updateUser();
                this.printValidMoves();
                this.printMyTurnMessage();
            }

            while (myturn()){
                System.out.print(playerName+" >> ");
                String input = scan.nextLine();
                if (input.equals("exit")) break;
                executeInput(input);
                Thread.sleep(1000);
            }
        }
        serverOut.close();
        serverIn.close();
        s.close();
    }

    public void executeInput(String input) throws Exception {
        String reply = "";
        StringTokenizer tokenizer = new StringTokenizer(input);
        String action = tokenizer.nextToken();

        try{
            int move = Integer.valueOf(action);
            this.makeMove(move);
        }
        catch (NumberFormatException nfe){
            reply = "invalid move, try again";
        };


        if(input.equalsIgnoreCase("P")){
           updateBoard();
        }

        else {
            serverOut.println(input);
            reply = serverIn.readLine();
            System.out.println(reply);
        }
    }

    private ArrayList<Integer> getIntegerMovesList() throws Exception{
        String temp = this.getValidMoves();
        temp = temp.replaceAll("\\[", "").replaceAll("\\]","");
        String moves = temp.replaceAll(",", "");
        StringTokenizer moveTokenizer = new StringTokenizer(moves);
        ArrayList<Integer> moveList =new ArrayList<Integer>();
        while(moveTokenizer.hasMoreTokens()){
           String move = moveTokenizer.nextToken();
            int num = Integer.parseInt(move);
            moveList.add(num);
        }
        return moveList;
    }

    private void makeMove(int move) throws Exception{
        serverOut.println("makemove "+move);
        String reply = serverIn.readLine();
        System.out.println(reply);
    }

    private boolean myturn() throws Exception {
        serverOut.println("myturn?");
        String result = serverIn.readLine();
        if(result.equalsIgnoreCase("yes")) return true;
        else return false;
    }

    private void updateUser() throws Exception {
        this.updateBoard();
        this.printPlayerTurn();
        this.printScore();
    }

    private void updateBoard() throws Exception {
        serverOut.println("P");
        String updatedBoard = serverIn.readLine();
        this.printBoard(updatedBoard);
    }

    public void resetBoard() throws Exception{
        serverOut.println("rest");
        String reply = serverIn.readLine();
    }

    private boolean gameOver() throws Exception {
        serverOut.println("gamestate");
        String reply = serverIn.readLine();
        if(reply.equalsIgnoreCase("gameover")) return true;
        else return false;
    }

    private String getValidMoves() throws Exception{
        serverOut.println("v");
        return serverIn.readLine();
    }

    private String getCurrentPlayer() throws Exception{
        serverOut.println('L');
        return serverIn.readLine();
    }

    public void setPlayerName() throws Exception {
        serverOut.println("g");
        playerName = serverIn.readLine();
    }

    private void printWaitMessage(){
        System.out.println("Waiting for Other Player To Move...");
    }

    public boolean askForNewGame(){
        System.out.println("Would You Like to Play again?  Press 'Y' for yes, any other key to exit");
        String result = scan.nextLine();
        if(result.equalsIgnoreCase("y")) return true;
        else return false;
    }

    private void printValidMoves() throws Exception{
        serverOut.println("v");
        String reply = serverIn.readLine();
        System.out.println("Valid Moves: "+reply);
    }

    private void printGameInstructions() {
        System.out.println("Game Has Been Reset");
    }

    private void printPlayerTurn() throws Exception{
        System.out.println("Turn: "+getCurrentPlayer());
    }

    private void printGameResult() throws Exception {
        serverOut.println("w");
        String reply = serverIn.readLine();
        System.out.println("GAME OVER");
        System.out.println(reply);
    }

    private void printScore() throws Exception {
        serverOut.println("S");
        String score = serverIn.readLine();
        System.out.println(score);
    }
    
    private void printMyTurnMessage(){
        System.out.println("Your Turn!  Choose:");
        System.out.println("    'V': get valid moves");
        System.out.println("    'P': print board");
        System.out.println("    'S': get game score");
        System.out.println("    'M': show me");
        System.out.println("    'L': player with current turn");
        System.out.println("    'A': pass - no move for this turn");
    }

    public void printBoard(String board) throws Exception{
        board = board.replaceAll("\\[", "").replaceAll("\\]","");
        board = board.replaceAll(",", "");
        StringTokenizer tokenizer = new StringTokenizer(board);
        String element = "";

        ArrayList<Integer> moveList = new ArrayList<Integer>();
        if(myturn()){
            moveList = getIntegerMovesList();
        }

        int count = 0;
        System.out.println("-----------------------------------------------------------");
        while(tokenizer.hasMoreTokens()){
            element = tokenizer.nextToken();
            if(element.equalsIgnoreCase("1")){
                System.out.print("(@)\t");
            }
            else if(element.equalsIgnoreCase("2")){
                System.out.print("(_)\t");
            }
            else if(moveList.contains(count)){
                System.out.print(count+"*\t");
            }
            else System.out.print(count+"\t");

            if((count+1)%8 == 0){
                System.out.println("");
            }
            count++;
        }
        System.out.println("-----------------------------------------------------------");
    }


}
