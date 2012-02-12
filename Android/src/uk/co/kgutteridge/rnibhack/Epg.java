package uk.co.kgutteridge.rnibhack;

import java.util.ArrayList;

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


public class Epg extends Activity implements RetrieverCallback {

    private Context mContext;
    
	String url = "http://epgservices.sky.com/tvlistings-proxy/TVListingsProxy/init.json";

	private ServicesMgr servicesMgr = new ServicesMgr(this);

	private ListView lv;

	private ChannelAdapter channelAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_epg);
        lv = (ListView)findViewById(R.id.list);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
		channelAdapter = new ChannelAdapter(this, servicesMgr);
		lv.setAdapter(channelAdapter);
        servicesMgr.update(); 
    }
    
    private class ChannelAdapter extends BaseAdapter {
    	private ServicesMgr mgr;
    	
        public ChannelAdapter(Context context, ServicesMgr mgr) {
        	this.mgr=mgr;
            mContext = context;
        }

        public int getCount() {
            return servicesMgr.channelsRetrieved.size();
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
		
		if(result.size() > 0){
			servicesMgr.updateResults((ArrayList<Channels>) result);
			channelAdapter.notifyDataSetChanged();
			
		} else{
//			hashText.setText(R.string.results_empty_title);
		}
		
	}

	public void onDownloadFailure(String message) {
	}

	public void onConnectionTimeOut() {
	}
}