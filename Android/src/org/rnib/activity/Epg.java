package org.rnib.activity;

import java.text.Format;
import java.text.SimpleDateFormat;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class Epg extends Activity implements ChannelsRetrievedCallback, ProgramesRetrievedCallback {

    private Context mContext;
    
	private ChannelMgr channelServicesMgr = new ChannelMgr(this, this);

	public static final String BUNDLE_PROG_DESC = "description";
	public static final String BUNDLE_PROG_TITLE = "programmetitle";
	public static final String BUNDLE_PROG_TIME = "time";
	public static final String BUNDLE_PROG_CHANNELID = "channelid";
	public static final String BUNDLE_PROG_CHANNEL = "channel";
	
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

        public View getView(final int position, View convertView, ViewGroup parent) {
        	LinearLayout li_channel;
        	TextView title;
        	TextView channel;
            if (convertView == null) {
                li_channel = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.li_channel, parent, false);
            } else {
                li_channel = (LinearLayout) convertView;
            }
            
            title = (TextView) li_channel.findViewById(R.id.li_channel_channel_title);
            channel = (TextView) li_channel.findViewById(R.id.li_channel_programme_title);            

//            if(channelServicesMgr.channels.get(position).channelID !=null){
//            	if(channelsVar==null){
//            		channelsVar = channelServicesMgr.channels.get(position).channelID;
//            	}else{
//            		channelsVar = channelsVar + ", "+ channelServicesMgr.channels.get(position).channelID;
//            	}
//            }
            
            if(mgr.channels.get(position).programmes == null){
            	channel.setText(channelServicesMgr.channels.get(position).title);
            	channel.setContentDescription(channelServicesMgr.channels.get(position).title);
            }else{
            	title.setText(channelServicesMgr.channels.get(position).programmes.get(0).title);
            	title.setContentDescription(channelServicesMgr.channels.get(position).programmes.get(0).title);
            	channel.setText(channelServicesMgr.channels.get(position).title);
            	channel.setContentDescription(channelServicesMgr.channels.get(position).title);
            	li_channel.setOnClickListener(new OnClickListener() {
            		public void onClick(View v) {
            			Intent programmeDetails = new Intent(Epg.this, ProgDetails.class);
            			programmeDetails.putExtra(BUNDLE_PROG_CHANNEL, channelServicesMgr.channels.get(position).title);
            			programmeDetails.putExtra(BUNDLE_PROG_CHANNELID, channelServicesMgr.channels.get(position).channelID);
            			programmeDetails.putExtra(BUNDLE_PROG_TIME, channelServicesMgr.channels.get(position).programmes.get(0).start);
            			programmeDetails.putExtra(BUNDLE_PROG_TITLE, channelServicesMgr.channels.get(position).programmes.get(0).title);
            			programmeDetails.putExtra(BUNDLE_PROG_DESC, channelServicesMgr.channels.get(position).programmes.get(0).shortDesc);
						startActivity(programmeDetails);
            		}
            	});
            }
            
            
            
            return li_channel;
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