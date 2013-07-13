import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ReversiServer {
    private static final int PORT = 8189;
    private ServerSocket serverSocket;
    private ReversiBoard board;
    ArrayList<Thread> threadArray = new ArrayList<Thread>();
    ReversiServer(ReversiBoard board){
        this.board = board;
    }

    public void go(){
        Socket s;
        System.out.println("Server listening on port " + PORT);
        try{
            serverSocket = new ServerSocket(PORT);
        }
        catch(IOException ioe){
            System.err.println("Error starting server on port " + PORT);
            System.out.println(ioe);
        }

        while (true){
            try{
                s = serverSocket.accept();
            }
            catch(IOException ioe){
                System.out.println("Error getting connection from client");
                System.out.println(ioe);
                continue;
            }
            Thread t = new PlayerConversationThread(s, this, board);
            threadArray.add(t);
            t.start();
        }
    }

    public static void main(String[] args){
        System.out.println("starting Reversi Game Server ....");
        new ReversiServer(new ReversiBoard()).go();
        System.out.println("Server Shutting Down");
    }



}
