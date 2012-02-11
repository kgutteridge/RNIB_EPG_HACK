package uk.co.kgutteridge.rnibhack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import uk.co.kgutteridge.rnibhack.EPGModel.*;

public class AEPGActivity extends Activity {
	
	
	 String url = "http://epgservices.sky.com/tvlistings-proxy/TVListingsProxy/init.json";
	  
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        InputStream source = retrieveStream(url);
        
        Gson gson = new Gson();
        
        Reader reader = new InputStreamReader(source);
        
        ChannelResponse response = gson.fromJson(reader, ChannelResponse.class);  
    }
    
    private InputStream retrieveStream(String url) {
    	
    	DefaultHttpClient client = new DefaultHttpClient(); 
        
        HttpGet getRequest = new HttpGet(url);
          
        try {
           
           HttpResponse getResponse = client.execute(getRequest);
           final int statusCode = getResponse.getStatusLine().getStatusCode();
           
           if (statusCode != HttpStatus.SC_OK) { 
              Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
              return null;
           }

           HttpEntity getResponseEntity = getResponse.getEntity();
           return getResponseEntity.getContent();
           
        } 
        catch (IOException e) {
           getRequest.abort();
           Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        
        return null;
        
     }
    
    
}