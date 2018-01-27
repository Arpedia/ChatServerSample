/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ex4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author plane
 */
public class ServerThread extends Thread {
    private final Socket socket;
    private String Name;
    private final int clientID;
    private ChatServer server;
    private BufferedReader reader;
    private PrintWriter writer;
    
    
    public ServerThread(Socket socket, int id, ChatServer server){
        this.socket = socket;
        this.clientID = id;
        this.server = server;
        try{
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch(IOException ex){
            System.out.println("クライアント"+ this.clientID + "が切断されました。");
        }
    }
    
    @Override
    public void run(){
        String line;
        System.out.println(this.socket);
        try {
            this.Name = this.reader.readLine();
            this.write(this.Name + "さん　ようこそ");
            this.server.chatSend(this.Name + "が参加しました。", this);
        
            while((line = this.reader.readLine()) != null){
                line = "[" + this.Name + "]" + line;
                System.out.println("[Reserve]" + this.Name + ":" + line);
                this.server.chatSend(line, this);
            }
        } catch (IOException ex) {
            System.out.println("クライアント" + this.clientID + "が切断されました。");
            server.chatSend(this.Name + "さんが退出しました。", this);
        }
    }
    
    public void write(String line){
        this.writer.println(line);
        this.writer.flush();
    }
    
}
