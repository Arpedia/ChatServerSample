/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ex4;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student
 */
public class ChatClient {
    
    private Socket socket;
    private BufferedReader systemReader;
    private PrintWriter chatWriter;
    private BufferedReader chatReader;
    private Boolean connectedF;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ChatClient client = new ChatClient();
        client.runClient();
    }
    
    private void runClient(){
        try{
            this.socket = new Socket("localhost" , 8000);
            System.out.println("クライアントが起動しました。サーバとの接続完了");
            this.connectedF = true;

            this.systemReader = new BufferedReader(new InputStreamReader(System.in));
            this.chatWriter = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.chatReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            
            System.out.println("チャットで使うあなたの名前を入力してください。");
            
            String line;            
            line = systemReader.readLine();
            this.ChatWrite(line);
            
            
            Thread chatTh = new Thread(){
                    @Override
                    public void run(){
                        ChatClient.this.receiver();
                    }
                };
            chatTh.start();
                
            while(true){
                
                line = this.SystemRead();
                this.ChatWrite(line);

                if(line.equals("exit")){
                    socket.close();
                    this.connectedF = false;
                    break;
                }
            }
        }catch(IOException e){
            System.out.println("クライアントが起動しません");
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void receiver(){
        while(this.connectedF){
            this.SystemWrite(this.ChatRead());
        }
    }
    
    private void ChatWrite(String line){
        this.chatWriter.println(line);
        this.chatWriter.flush();        
    }
    
    private void SystemWrite(String line){
        System.out.println(line);
    }
    
    private String ChatRead(){
        try {
            String line = this.chatReader.readLine();
            return line;
        } catch (IOException ex) {
            this.SystemWrite("サーバから切断されました。");
            return "";
        }
    }
    
    private String SystemRead(){
        try {
            String line = this.systemReader.readLine();
            return line;
        } catch (IOException ex) {
            this.SystemWrite("入力に関する問題が発生しました。");
            return "Error";
        }
    }
}
