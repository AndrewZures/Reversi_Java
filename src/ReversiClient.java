import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class ReversiClient{
    int port;
    String host;
    java.util.Scanner scan = new java.util.Scanner(System.in);

    public static void main(String[] args) throws Exception{
        new ReversiClient("localhost", 8189).go();
    }

    ReversiClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void go() throws Exception{
        String reply = "";
        //create a socket connection to server
        Socket s = new Socket(this.host, this.port);
        BufferedReader serverIn = new BufferedReader
                (new InputStreamReader(s.getInputStream()));
        PrintWriter serverOut = new PrintWriter
                (s.getOutputStream(), true);
        //loop to get user input
        while (true){
            System.out.print("STORE >> ");
            String input = scan.nextLine();
            if(input.equalsIgnoreCase("update")){
               serverOut.println("printboard");
               String updatedBoard = serverIn.readLine();
               this.printBoard(updatedBoard);
            }


            if (input.equals("exit")) break;
            //this is a "dumb client", it simply passes
            //passes user input to the server in raw form
            serverOut.println(input);
            //this is still a "dumb client" so it only prints
            //verbatim what the server sends it.
            reply = serverIn.readLine();
            System.out.println(reply);
        }
        serverOut.close();
        serverIn.close();
        s.close();
    }

    public void printBoard(String board){
        StringTokenizer tokenizer = new StringTokenizer(board);
        String element = "";
        int count = 0;
        while(tokenizer.hasMoreTokens()){
            element = tokenizer.nextToken();
            if(element.length() == 1) System.out.print(element+"  ");
            else if(element.length() > 1) System.out.print(element+" ");

            if((count+1)%8 == 0) System.out.println("");
            count++;
        }
    }

}
