package org.rnib.activity;

import java.util.ArrayList;

import org.rnib.R;
import org.rnib.coms.ChannelRetriever.ChannelsRetrievedCallback;
import org.rnib.model.channels.Channels;

import android.app.Activity;
import android.os.Bundle;

public class ProgDetails extends Activity implements ChannelsRetrievedCallback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_programme_details);
	}

	public void onDownloadSuccess(ArrayList<Channels> result) {
	}

	public void onDownloadFailure(String message) {
	}

	public void onConnectionTimeOut() {
	}
}
