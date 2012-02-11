package uk.co.kgutteridge.rnibhack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.kgutteridge.rnibhack.EPGModel.Channels;
import uk.co.kgutteridge.rnibhack.EPGModel.ChannelResponse;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;


public class Epg extends Activity {

    private Context mContext;
    
	String url = "http://epgservices.sky.com/tvlistings-proxy/TVListingsProxy/init.json";
	
    private String[] mStrings = {
            "Match of the Day : Sky sports",
            "News : BBC",
            "Emmerdale : ITV",
            "IT Crowd : CH4",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------",
            "----------"
    };

	private ChannelResponse response;

	private ListView lv;

	private ArrayList<Channels> channelsRetrieved;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_epg);
        lv = (ListView)findViewById(R.id.list);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
        
        InputStream source = retrieveStream(url);
        
        Reader reader = new InputStreamReader(source);
        
        response = (ChannelResponse)new Gson().fromJson(reader, ChannelResponse.class);  
        Log.i("TAG", "channels " + response.channels);

//        channelsRetrieved = new ArrayList<Channel>();
//		Iterator<Channel> i = response.channelsList.iterator();
//		while (i.hasNext()) {
//			Channel res = (Channel) i.next();
//			Log.i("TAG", "channel " + res.title);
//			channelsRetrieved.add(res);
//		}
        
        lv.setAdapter(new ChannelAdapter(this));
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
    
    private class ChannelAdapter extends BaseAdapter {
        public ChannelAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
        	Log.i("TAG", "response " +  response);
//        	Log.i("TAG", "response.channelsList " + response.channelsList);
//        	Log.i("TAG", "response.channelsList.size" + response.channelsList.size());
        	
            return channelsRetrieved.size() - 1;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                tv = (TextView) LayoutInflater.from(mContext).inflate(
                        android.R.layout.simple_list_item_1, parent, false);
            } else {
                tv = (TextView) convertView;
            }
            tv.setText(channelsRetrieved.get(position).title);
            return tv;
        }
    }
}