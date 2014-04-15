package com.mobvcasting.sockettest;

import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			SocketIO socket = new SocketIO("http://54.221.90.69:8080");
			socket.connect(new IOCallback() {
			    @Override
			    public void on(String event, IOAcknowledge ack, Object... args) {
			        if (event.equals("otherevent")) {
			            Log.d("SocketIO", "otherevent " + args[0]);
			        }
			    }
	
			    @Override
			    public void onMessage(JSONObject json, IOAcknowledge ack) {
			    		Log.v("SocketIO", json.toString());
			    }
			    @Override
			    public void onMessage(String data, IOAcknowledge ack) {
			    		Log.v("SocketIO", data);
			    	}
			    @Override
			    public void onError(SocketIOException socketIOException) {
			    		socketIOException.printStackTrace();
			    }
			    @Override
			    public void onDisconnect() {
	    				Log.v("SocketIO", "Disconnected");    			
			    }
			    @Override
			    public void onConnect() {
		    			Log.v("SocketIO", "Connected");
			    }		
			});
			socket.emit("message", "android says hello");	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
