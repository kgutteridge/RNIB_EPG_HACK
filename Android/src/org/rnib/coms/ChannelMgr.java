package org.rnib.coms;

import java.util.ArrayList;

import org.rnib.coms.ChannelRetriever.ChannelsRetrievedCallback;
import org.rnib.coms.ProgrammeRetriever.ProgramesRetrievedCallback;
import org.rnib.model.channels.Channels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ChannelMgr {

	private final ChannelRetriever channelretriver;
	private ProgrammeRetriever programmeRetriver;
	public ArrayList<Channels> channels = new ArrayList<Channels>();
	
	private BroadcastReceiver channelRefreshReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			refreshShownChannels();
		}
	};
	
	public ChannelMgr(ChannelsRetrievedCallback letMeKnow, ProgramesRetrievedCallback meToo) {
		channelretriver = new ChannelRetriever(letMeKnow);
		programmeRetriver = new ProgrammeRetriever(meToo);
	}
	
	public void refreshShownChannels() {
		channelretriver.retrieveChannels();
	}
	
	public BroadcastReceiver getChannelShownReciever() {
		return channelRefreshReceiver;
	}

	public void updateShownChannels(ArrayList<Channels> result) {
		channels = result;
	}

	public void refreshShownPrograms() {
		programmeRetriver.retrieveProgs();
	}
}
