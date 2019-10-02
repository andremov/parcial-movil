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
public class SocketManagementService extends IntentService implements ClientSocketManagerCallerInterface{

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

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CONNECT.equals(action)) {
                initializeClientSocketManager();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clientSocketManager.sendMessage("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            }
        }
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
                clientSocketManager.run();
            }
        }catch (Exception error){

        }
    }

    @Override
    public void MessageReceived(String message) {
        /*
        try {
            if(broadcastManager!=null){
                broadcastManager.sendBroadcast( SERVER_TO_CLIENT_MESSAGE,message);
            }
        } catch (Exception error) { }
         */
        System.out.println(message);
        if(message.equals("ping")) {
            clientSocketManager.sendMessage("pong");
        }else if(message.equals("update@locations")){

        }else if(message.equals("update@messages")){

        }
    }

    @Override
    public void ErrorFromSocketManager(Exception error) { }

    @Override
    public void onDestroy() {

        clientSocketManager=null;
        super.onDestroy();
    }
}
