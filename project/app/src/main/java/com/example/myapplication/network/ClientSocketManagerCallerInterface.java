package com.example.myapplication.network;

public interface ClientSocketManagerCallerInterface {

    void MessageReceived(String message);
    void ErrorFromSocketManager(Exception error);

}
