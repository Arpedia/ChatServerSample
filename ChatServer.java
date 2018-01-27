/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ex4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author student
 */
public class ChatServer {
    /**
     * @param args the command line arguments
     */
    
    private ServerSocket server;
    ArrayList<ServerThread> threads = new ArrayList<ServerThread>();
    
    public static void main(String[] args) {
        // TODO code application logic here
        ChatServer server = new ChatServer();
        server.runServer();
    }
    
    public void runServer(){
        try{
            this.server = new ServerSocket(8000);
            System.out.println("サーバが起動しました。");
        }catch(IOException e){
            System.out.println("このポートは既に使用されています。");
        }
        
        int i=0;
        while(i < 32){
            i+=1;
            Socket socket;
            try {
                socket = server.accept();
                System.out.println("クライアント"+ i +"と接続されました。");
                ServerThread client  = new ServerThread(socket, i, this);
                threads.add(client);
                client.start();
            } catch (IOException ex) {
                System.out.println("クライアントとの接続に問題があります。");
            }
        }
    }
    
    public void chatSend(String line, ServerThread clientTh){
        for(ServerThread thread : this.threads ){
            if(clientTh.equals(thread)){
                continue;
            }
            thread.write(line);
        }
    }
}
