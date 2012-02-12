package uk.co.kgutteridge.app;

import java.util.ArrayList;

import uk.co.kgutteridge.rnibhack.R;
import uk.co.kgutteridge.rnibhack.EPGModel.ChannelRetriever.RetrieverCallback;
import uk.co.kgutteridge.rnibhack.EPGModel.Channels;
import android.app.Activity;
import android.os.Bundle;

public class ProgDetails extends Activity implements RetrieverCallback {

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
