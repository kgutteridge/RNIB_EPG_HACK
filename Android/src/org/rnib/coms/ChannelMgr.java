package org.rnib.coms;

import java.util.ArrayList;

import org.rnib.coms.ChannelRetriever.ChannelsRetrievedCallback;
import org.rnib.coms.ProgrammeRetriever.ProgramesRetrievedCallback;
import org.rnib.model.channels.Channel;
import org.rnib.model.progs.Channels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ChannelMgr {

	private final ChannelRetriever channelretriver;
	private ProgrammeRetriever programmeRetriver;
	public ArrayList<org.rnib.model.progs.Channels> channels = new ArrayList<org.rnib.model.progs.Channels>();
	
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

	public void updateShownChannels(ArrayList<org.rnib.model.channels.Channel> result) {
		channels.clear();
		for(int i=0;i<result.size();i++){
			Channels guideItem = new Channels();
			guideItem.channelID=result.get(i).channelID;
			guideItem.title=result.get(i).title;
			guideItem.channelType=result.get(i).channelType;
			channels.add(guideItem);
		}
	}

	public void refreshShownPrograms() {
		programmeRetriver.retrieveProgs();
	}

	public void updateUIWithProgrammes(ArrayList<org.rnib.model.progs.Channels> result) {
		channels = result;
	}
}
