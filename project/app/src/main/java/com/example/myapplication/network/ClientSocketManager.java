package com.example.myapplication.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketManager extends Thread{

    private String serverHost;
    private int port;
    private ClientSocketManagerCallerInterface caller;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientSocketManager(String serverHost, int port, ClientSocketManagerCallerInterface caller) {
        this.serverHost = serverHost;
        this.port = port;
        this.caller = caller;
        start();
    }

    public boolean initializeClientSocketManager(){
        try{
            socket=new Socket(serverHost,port);
            return true;

        }catch (Exception error){
            caller.ErrorFromSocketManager(error);
        }
        return false;
    }

    public boolean initializeStreams(){
        try{
            writer=new PrintWriter(socket.getOutputStream(),true);
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;

        }catch (Exception error){
            caller.ErrorFromSocketManager(error);
        }
        return false;
    }

    @Override
    public void run(){
        try{
            if(initializeClientSocketManager()){
                if(initializeStreams()){
                    String message="";
                    while((message=reader.readLine())!=null){
                        caller.MessageReceived(message);
                    }
                }
            }
        }catch (Exception error){
            caller.ErrorFromSocketManager(error);
        }
    }

    public void sendMessage(String message){
        try{
            writer.write(message+"\n");
        }catch (Exception error){
            caller.ErrorFromSocketManager(error);
        }
    }


}
