package com.example.myapplication.broadcast;

public interface BroadcastManagerCallerInterface {

    void MessageReceivedThroughBroadcastManager(
            String channel, String type, String message);

    void ErrorAtBroadcastManager(Exception error);
}
