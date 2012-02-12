package org.rnib.activity;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.rnib.R;
import org.rnib.coms.ChannelMgr;
import org.rnib.coms.ChannelRetriever.ChannelsRetrievedCallback;
import org.rnib.model.channels.Channel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProgDetails extends Activity implements ChannelsRetrievedCallback {

    private Context mContext;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_programme_details);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		intent.getStringExtra(Epg.BUNDLE_PROG_CHANNEL);
		intent.getStringExtra(Epg.BUNDLE_PROG_CHANNELID);
		intent.getStringExtra(Epg.BUNDLE_PROG_DESC);
		intent.getStringExtra(Epg.BUNDLE_PROG_TIME);
		
		TextView txt = (TextView) findViewById(R.id.prog_details_name);
		txt.setText(intent.getStringExtra(Epg.BUNDLE_PROG_TITLE));
		txt.setContentDescription(intent.getStringExtra(Epg.BUNDLE_PROG_TITLE));
		
		txt = (TextView) findViewById(R.id.prog_details_desc);
		txt.setText(intent.getStringExtra(Epg.BUNDLE_PROG_DESC));
		txt.setContentDescription(intent.getStringExtra(Epg.BUNDLE_PROG_DESC));
		
		txt = (TextView) findViewById(R.id.prog_details_channel);
		txt.setText(intent.getStringExtra(Epg.BUNDLE_PROG_CHANNEL));
		txt.setContentDescription(intent.getStringExtra(Epg.BUNDLE_PROG_CHANNEL));
		
		txt = (TextView) findViewById(R.id.prog_details_time);
		String time = intent.getStringExtra(Epg.BUNDLE_PROG_TIME);
		txt.setContentDescription(Epg.BUNDLE_PROG_TIME);
		time = time.subSequence(0, time.length()-3).toString();
		
		Date expiry = new Date(Long.parseLong(time) * 1000);
		Format formatter = new SimpleDateFormat("E, dd MMM HH:mm ");
		
		txt.setText(formatter.format(expiry));		
		txt.setContentDescription(formatter.format(expiry));		
	}

	public void onDownloadSuccess(ArrayList<Channel> result) {
	}

	public void onChannelsDownloadFailure(String message) {
	}

	public void onChannelConnectionTimeOut() {
	}

	public void onChannelsDownloadedSuccess(ArrayList<Channel> result) {
	}

}
