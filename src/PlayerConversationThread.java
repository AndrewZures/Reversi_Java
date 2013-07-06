import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class PlayerConversationThread extends Thread {
    Socket socket;
    ReversiBoard board;
    int idNumber;

    public PlayerConversationThread(Socket socket, ReversiBoard board, int idNumber){
        this.socket = socket;
        this.board = board;
        this.idNumber =idNumber;
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
            // e.printStackTrace();
        }

        try{
            while((input = in.readLine()) != null){
                if(input.equals("exit")) break;
                String reply = handle(input);
                out.println(reply);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String handle(String input){
        String reply = "";
        StringTokenizer tokenizer = new StringTokenizer(input);
        String action = tokenizer.nextToken();

        if(action.equalsIgnoreCase("makemove")){
            int move = Integer.valueOf(tokenizer.nextToken());
            boolean moveAccepted = board.makeMove(move, idNumber);
            if(moveAccepted) reply = "move accepted";
            else reply = "invalid move, try again";
        }


        if(action.equalsIgnoreCase("printboard")){
           reply = board.getBoardString();
        }

        if(action.equalsIgnoreCase("getvalidmoves")){
            reply = "validmoves, "+board.getValidMoves(idNumber);
        }

        return reply;
    }
}
