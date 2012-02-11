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

import uk.co.kgutteridge.rnibhack.EPGModel.ChannelResponse;
import uk.co.kgutteridge.rnibhack.EPGModel.ChannelRetriever.RetrieverCallback;
import uk.co.kgutteridge.rnibhack.EPGModel.Channels;
import uk.co.kgutteridge.rnibhack.EPGModel.ServicesMgr;
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


public class Epg extends Activity implements RetrieverCallback {

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
	
	private ServicesMgr servicesMgr = new ServicesMgr(this);

	private ListView lv;

	private ArrayList<Channels> channelsRetrieved;

	private ChannelAdapter channelAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_epg);
        lv = (ListView)findViewById(R.id.list);
        servicesMgr.update();        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
		channelAdapter = new ChannelAdapter(this, servicesMgr);
//        Log.i("TAG", "channels " + response.channels.size());
		
//        lv.setAdapter(channelAdapter);
    }
    
    private class ChannelAdapter extends BaseAdapter {
    	
    	ServicesMgr servicesMgr;
    	
        public ChannelAdapter(Context context, ServicesMgr mgr) {
        	this.servicesMgr = mgr;
            mContext = context;
        }

        public int getCount() {
        	Log.i("TAG", "response " +  response);
//        	Log.i("TAG", "response.channelsList " + response.channelsList);
//        	Log.i("TAG", "response.channelsList.size" + response.channelsList.size());
        	
            return servicesMgr.channelsRetrieved.size() - 1;
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
            tv.setText(servicesMgr.channelsRetrieved.get(position).title);
            return tv;
        }
    }

	public void onDownloadSuccess(ArrayList<Channels> result) {
		
		Log.i("TAG", "The result is not empty " + result.size());
		
		if(result.size() > 0){
			servicesMgr.updateResults((ArrayList<Channels>) result);
			Log.i("TAG", "The mgr results are not empty " + servicesMgr.channelsRetrieved.size());
			if (lv.getAdapter()==null){
				lv.setAdapter(channelAdapter);
			}else{
				channelAdapter.notifyDataSetChanged();
			}
		} else{
//			hashText.setText(R.string.results_empty_title);
		}
		
	}

	public void onDownloadFailure(String message) {
	}

	public void onConnectionTimeOut() {
	}
}