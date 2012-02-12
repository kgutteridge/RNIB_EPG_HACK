package org.rnib.activity;

import java.util.ArrayList;

import org.rnib.R;
import org.rnib.coms.ChannelMgr;
import org.rnib.coms.ChannelRetriever.ChannelsRetrievedCallback;
import org.rnib.coms.ProgrammeRetriever.ProgramesRetrievedCallback;
import org.rnib.model.channels.Channel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class Epg extends Activity implements ChannelsRetrievedCallback, ProgramesRetrievedCallback {

    private Context mContext;
    
	private ChannelMgr channelServicesMgr = new ChannelMgr(this, this);

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
		channelAdapter = new ChannelAdapter(this, channelServicesMgr);
		lv.setAdapter(channelAdapter);
		
		
		//TODO: add a timestamp to refresh
		if(channelServicesMgr.channels.size() <= 0){
			channelServicesMgr.refreshShownChannels(); 
		}
    }
    
    private class ChannelAdapter extends BaseAdapter {
    	private ChannelMgr mgr;
    	private String channelsVar;
    	
        public ChannelAdapter(Context context, ChannelMgr mgr) {
        	this.mgr=mgr;
            mContext = context;
        }

        public int getCount() {
            return channelServicesMgr.channels.size();
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

//            if(channelServicesMgr.channels.get(position).channelID !=null){
//            	if(channelsVar==null){
//            		channelsVar = channelServicesMgr.channels.get(position).channelID;
//            	}else{
//            		channelsVar = channelsVar + ", "+ channelServicesMgr.channels.get(position).channelID;
//            	}
//            }
            
            if(mgr.channels.get(position).programmes == null){
            	Log.i("TAG", "Channels list [" + channelsVar + "]");
            	tv.setText(channelServicesMgr.channels.get(position).title);
            }else{
            	Log.i("TAG", "Channels list [" + channelsVar + "]");
            	tv.setText(channelServicesMgr.channels.get(position).title + ":" + channelServicesMgr.channels.get(position).programmes.get(0).title);
            	
            	tv.setOnClickListener(new OnClickListener() {
            		public void onClick(View v) {
            			startActivity(new Intent(Epg.this, ProgDetails.class));
            		}
            	});
            }
            
            
            
            return tv;
        }

    }

	public void onChannelsDownloadedSuccess(ArrayList<org.rnib.model.channels.Channel> result) {
		if(result.size() > 0){
			channelServicesMgr.updateShownChannels((ArrayList<org.rnib.model.channels.Channel>) result);
			channelAdapter.notifyDataSetChanged();
	        channelServicesMgr.refreshShownPrograms(); 
		} else{
//			hashText.setText(R.string.results_empty_title);
		}
	}

	public void onChannelsDownloadFailure(String message) {
	}

	public void onChannelConnectionTimeOut() {
	}

	public void onProgrammesDownloadedSuccess(ArrayList<org.rnib.model.progs.Channels> result) {
		Log.i("TAG", "Programmes are all here " + result.size());
		if(result.size() > 0){
			channelServicesMgr.updateUIWithProgrammes((ArrayList<org.rnib.model.progs.Channels>) result);
			channelAdapter.notifyDataSetChanged();
		}
	}

	public void onProgrammesDownloadFailure(String message) {
	}

	public void onProgrammesConnectionTimeOut() {
	}

}