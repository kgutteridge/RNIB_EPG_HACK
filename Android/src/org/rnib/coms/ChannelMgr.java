package org.rnib.coms;

import java.util.ArrayList;

import org.rnib.coms.ChannelRetriever.ChannelsRetrievedCallback;
import org.rnib.model.channels.Channels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ChannelMgr {

	private final ChannelRetriever channelretriver;
	public ArrayList<Channels> channels = new ArrayList<Channels>();
	
	private BroadcastReceiver channelReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			refreshShownChannels();
		}
	};
	
	public ChannelMgr(ChannelsRetrievedCallback letMeKnow) {
		channelretriver = new ChannelRetriever(letMeKnow);
	}
	
	public void refreshShownChannels() {
		channelretriver.retrieveChannels();
	}
	
	public BroadcastReceiver getChannelShownReciever() {
		return channelReceiver;
	}

	public void updateShownChannels(ArrayList<Channels> result) {
		channels = result;
	}
}
