package com.mobvcasting.sockettest;

import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends Activity {

	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView) this.findViewById(R.id.textView1);
		
		try {
			SocketIO socket = new SocketIO("http://54.221.90.69:8080");
			socket.connect(new IOCallback() {
			    @Override
			    public void on(String event, IOAcknowledge ack, Object... args) {
			        if (event.equals("otherevent")) {
			            Log.v("SocketIO", "otherevent " + args[0]);
			        } else {
			        		Log.v("SocketIO", event + " " + args[0]);
			        }
			    }
	
			    @Override
			    public void onMessage(JSONObject json, IOAcknowledge ack) {
			    		Log.v("SocketIO", json.toString());
			    		handleMessage(json.toString());	    		    		
			    }
			    
			    @Override
			    public void onMessage(String data, IOAcknowledge ack) {
			    		Log.v("SocketIO", data);
			    		handleMessage(data);
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
	
	// Handlers let us interact with threads on the UI thread
	// The handleMessage method receives messages from other threads and will act upon it on the UI thread
	Handler handler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
		    
		    // Pull out the data that was packed into the message with the key "message"
			String messageData = msg.getData().getString("message");
			
			// Send it over to the view
			tv.setText(messageData);
		  }
	};	

	public void handleMessage(String message) {
		// First we obtain a message object
		Message msg = handler.obtainMessage();
		
		// Create a bundle to hold data
		Bundle bundle = new Bundle();
		
		// Put our value with the key "message"
		bundle.putString("message", message);
		
		// Set the message data to our bundle
		msg.setData(bundle);
		
		// and finally send the message via the handler
		handler.sendMessage(msg);
	}
	
}
