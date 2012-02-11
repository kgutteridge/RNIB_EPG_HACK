package uk.co.kgutteridge.rnibhack.EPGModel;

import java.util.ArrayList;

import uk.co.kgutteridge.rnibhack.EPGModel.ChannelRetriever.RetrieverCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServicesMgr {

	private final ChannelRetriever channelretriver;
	public ArrayList<Channels> channelsRetrieved = new ArrayList<Channels>();
	private String latestChannelChoice = "bbc1";
	
	private BroadcastReceiver channelReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			update();
		}
	};
	
	public ServicesMgr(RetrieverCallback letMeKnow) {
		channelretriver = new ChannelRetriever(letMeKnow);
	}
	
	public void update() {
		channelretriver.retrieveChannels();
	}
	
	public BroadcastReceiver getReciever() {
		return channelReceiver;
	}

	public void updateResults(ArrayList<Channels> result) {
		channelsRetrieved = result;
	}
}
