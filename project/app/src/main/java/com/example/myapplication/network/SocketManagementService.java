package com.example.myapplication.network;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

//import com.example.myapplication.broadcast.BroadcastManager;
import com.example.myapplication.broadcast.BroadcastManagerCallerInterface;
import com.example.myapplication.utils.Settings;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SocketManagementService extends IntentService implements ClientSocketManagerCallerInterface, BroadcastManagerCallerInterface {

    ClientSocketManager clientSocketManager;
    //BroadcastManager broadcastManager;
    public static String SOCKET_SERVICE_CHANNEL="com.example.myfirstapplication.SOCKET_SERVICE_CHANNEL";
    public static String SERVER_TO_CLIENT_MESSAGE="SERVER_TO_CLIENT_MESSAGE";
    public static String CLIENT_TO_SERVER_MESSAGE="CLIENT_TO_SERVER_MESSAGE";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_CONNECT = "com.example.myfirstapplication.network.action.ACTION_CONNECT";

    public SocketManagementService() {
        super("SocketManagementService");

        Toast.makeText(this,"constructor",Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int a = 1;
        /*
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CONNECT.equals(action)) {
                */
                initializeClientSocketManager();
                //initializeBroadcastManager();
                Toast.makeText(this,"init",Toast.LENGTH_LONG).show();
                /*
            }
        }
                 */
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void initializeClientSocketManager(){
        try{
            if(clientSocketManager==null){
                clientSocketManager=new ClientSocketManager( Settings.getPushIP(), Settings.getPushPort(), this);
                Toast.makeText(this,"created client socket manager",Toast.LENGTH_LONG).show();
            }
        }catch (Exception error){
            Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
/*
    public void initializeBroadcastManager(){
        try{
            if(broadcastManager == null){
                broadcastManager=new BroadcastManager(getApplicationContext(), SOCKET_SERVICE_CHANNEL,this);
                Toast.makeText(this,"created broadcast manager",Toast.LENGTH_LONG).show();
            }
        }catch (Exception error){
            Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
*/

    @Override
    public void MessageReceived(String message) {
        /*
        try {
            if(broadcastManager!=null){
                broadcastManager.sendBroadcast( SERVER_TO_CLIENT_MESSAGE,message);
            }
        } catch (Exception error) { }
         */
        Toast.makeText(getApplicationContext(), "Received message from Socket!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void ErrorFromSocketManager(Exception error) {

    }

    @Override
    public void MessageReceivedThroughBroadcastManager(String channel, String type,String message) {
        /*
        try {
            if (clientSocketManager != null) {
                if (type.equals(CLIENT_TO_SERVER_MESSAGE)) {
                    clientSocketManager.sendMessage(message);
                }
            }
        }catch (Exception error){

        }
         */
        Toast.makeText(getApplicationContext(), "Received message from Socket! (BM)", Toast.LENGTH_LONG).show();
    }

    @Override
    public void ErrorAtBroadcastManager(Exception error) {

    }

    @Override
    public void onDestroy() {
        /*
        if(broadcastManager!=null){
            broadcastManager.unRegister();
        }
         */
        super.onDestroy();
    }
}
