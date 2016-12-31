/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Saqib
 */
public final class Client {
    
    //public static Thread t;
    public Socket clientSocket;
    public static Player player;
    public static int id, firstCaller;     // both set by server, id once, firstCaller dynamically
    //private final ObjectInputStream  in;
    private final ObjectOutputStream out;
    public Client() throws IOException, InterruptedException, ClassNotFoundException {

        //t = Thread.currentThread();
        System.out.println("client");

        player = new Player();
        
        clientSocket = new Socket(InetAddress.getLocalHost(), 23456);   //hardcoded port number & server ip address
        System.out.println ("In new before");
        //in = new ObjectInputStream(clientSocket.getInputStream());
        
        /*player.hand = (String[]) read();
        id = (Integer) read();*/
        
        System.out.println ("In new done, before out");
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        System.out.println ("In new done");
        
        ReadThread readThread = new ReadThread(clientSocket.getInputStream());
        
        while (All.status == false){
            Thread.sleep(10);
        }
        All.status = false;
        System.out.println("After synchronised");
        
        //System.out.println("Connection available.");
    }
    
    /*public Object read() throws IOException, InterruptedException, ClassNotFoundException{
        
        Object ret;
        ret = in.readObject();
        return ret;

    }*/
    
    public void write(Object send) throws IOException{
   
        out.writeObject(send);
        out.flush();
    }
    
}
