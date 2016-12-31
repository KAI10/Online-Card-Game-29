/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author KAI10
 */
public class ClientThread implements Runnable{

    private final ObjectOutputStream out;
    private ObjectInputStream in;
    private final Socket connection;
    public int serial;
    
    public String hand[];
    
    private final Thread t;
    private boolean suspendFlag;
    
    public ClientThread(Socket client, int count) throws IOException{
        
        connection = client;
        out = new ObjectOutputStream(connection.getOutputStream());
        out.flush();
        System.out.println("OUT DONE");
        in = new ObjectInputStream(connection.getInputStream());
        System.out.println("IN DONE");
        
        serial = count%4;
        
        hand = new String[8];
        
        t = new Thread(this, "client " + serial);
        System.out.println("Thread " + t);
        
        suspendFlag = true;
        t.start();
    }
    
    @Override
    public void run(){
        try {
            while(true){
                synchronized(this) {
                    while(suspendFlag) wait();
                }           
            }
            } catch (InterruptedException e) {
                System.out.println("interrupted.");
            }
    }
    
    public Object read() throws IOException, ClassNotFoundException {
        
        return in.readObject();
        
    }
    
    public void write(Object send) throws IOException{
        
        out.writeObject(send);
        out.flush();
        
    }
    
    public void mySuspend(){
        suspendFlag = true;
    }
    
    synchronized public void resume(){
        suspendFlag = false;
        notify();
    }

}
