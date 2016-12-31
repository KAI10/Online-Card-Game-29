/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author KAI10
 */
public class Server {
    
    private ServerSocket server;
    private Socket client[];
    private int count;
    private int port;
    
    private ClientThread player[][];
    
    public Server() throws IOException, ClassNotFoundException, InterruptedException{
        
        port = 23456;
        count = 0;
        
        player = new ClientThread[400][4];
        client = new Socket[400];
        
        server = new ServerSocket(port, 100);
        
        for(int i=0; ; i++){
            
            establishConnection();
            if (i==3){
                i=-1;
                new Game (player[(count)/4]);  //creating game thread so that multiple games can be run concurrently
            }
            count++;
        }
        
    }
    
    private void establishConnection() throws IOException{
        
        System.out.println("Waiting for connection " + count + "...");
        client[count] = server.accept();
        System.out.println("Connection " + count + " received from " + client[count].getInetAddress());
        
        player[count/4][count%4] = new ClientThread(client[count], count);
        
        
        if (count==400) count = 0;
        
    }
    
}
