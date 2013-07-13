import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class PlayerConversationThread extends Thread {
    Socket socket;
    ReversiServer server;
    ReversiBoard board;
    int playerNum;
    String player;

    public PlayerConversationThread(Socket socket, ReversiServer server, ReversiBoard board){
        this.socket = socket;
        this.board = board;
        this.playerNum = board.getNewPlayer();
        this.player = board.getPlayerString(playerNum);
        this.server = server;
    }

    public void run(){
        String input = "";
        BufferedReader in = null;
        PrintWriter out = null;

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            System.out.println("error occurred setting up i/o streams");
            System.out.println(e);
        }

        try{
            while((input = in.readLine()) != null){
                if(input.equals("exit")) break;
                String reply = handle(input);
                out.println(reply);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String handle(String input){
        String reply = "";
        StringTokenizer tokenizer = new StringTokenizer(input);
        String action = tokenizer.nextToken();

        if(action.equalsIgnoreCase("makemove")){
            try{
                int move = Integer.valueOf(tokenizer.nextToken());
                boolean moveAccepted = board.makeMove(move, playerNum);
                if(moveAccepted){
                reply = "move accepted";
                }
                else reply = "invalid move, try again";
            }
            catch (NumberFormatException nfe){
                reply = "invalid move, try again";
            }
        }

        if(action.equalsIgnoreCase("g"))
            reply = player;

        if(action.equalsIgnoreCase("p"))
           reply = board.getBoardString();

        if(action.equalsIgnoreCase("v"))
            reply = ""+board.getValidMoves(playerNum);

        if(action.equalsIgnoreCase("l"))
            reply = ""+board.getCurrentPlayer();

        if(action.equalsIgnoreCase("myturn?")){
            boolean result = board.myTurn(playerNum);
            if(result) reply = "yes";
            else reply = "no";
        }

        if(action.equalsIgnoreCase("s")){
            reply = board.getScoreString();
        }

        if(action.equalsIgnoreCase("w")){
            reply = board.getWinnerString();
        }

        if(action.equalsIgnoreCase("gamestate")){
            reply = board.getGameStateString();
        }

        if(action.equalsIgnoreCase("a")){
            board.passTurn();
            reply = "Turn Passed";
        }

        if(action.equalsIgnoreCase("rest")){
            this.board.resetGame();
        }

        return reply;
    }

}
