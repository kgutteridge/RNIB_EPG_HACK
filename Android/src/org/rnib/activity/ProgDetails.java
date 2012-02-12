package org.rnib.activity;

import java.util.ArrayList;

import org.rnib.R;
import org.rnib.coms.ChannelMgr;
import org.rnib.coms.ChannelRetriever.ChannelsRetrievedCallback;
import org.rnib.model.channels.Channels;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class ProgDetails extends Activity implements ChannelsRetrievedCallback {

    private Context mContext;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_programme_details);
	}

	public void onDownloadSuccess(ArrayList<Channels> result) {
	}

	public void onChannelsDownloadFailure(String message) {
	}

	public void onChannelConnectionTimeOut() {
	}

	public void onChannelsDownloadedSuccess(ArrayList<Channels> result) {
	}

}
