package org.rnib.coms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.rnib.R;
import org.rnib.app.SkyEPG;
import org.rnib.model.channels.Channels;

import android.util.Log;

import com.akshay.http.service.ResultHandler;
import com.akshay.http.service.constants.HttpStatusCodes;
import com.google.gson.Gson;

public class ChannelRetriever {

	private static final String UTF_8 = "UTF-8";
	private static String SEARCH = "googletv";
	
	String DEFAULT_URL = "http://epgservices.sky.com/tvlistings-proxy/TVListingsProxy/init.json";
	String BASE_URL = "http://epgservices.sky.com/tvlistings-proxy/TVListingsProxy/";
	String CHANNELS_API = "init.json";
	
	interface Params {
		final String QUERY = "";
	}

	private ChannelsRetrievedCallback callback;

	public ChannelRetriever(ChannelsRetrievedCallback callback) {
		this.callback = callback;
	}

	private final ResultHandler channelResponseHandler = new ResultHandler() {
		@Override
		public void onSuccess(int resultCode, byte[] array) throws IOException {
			
			ChannelResponse response = new Gson().fromJson(new String(array), ChannelResponse.class);  
			ArrayList<Channels> channelsRetrieved = new ArrayList<Channels>();
			Iterator<Channels> i = response.channels.iterator();
			while (i.hasNext()) {
				Channels res = (Channels) i.next();
				Log.i("TAG", "channel " + res.title);
				channelsRetrieved.add(res);
			}
		       
			callback.onChannelsDownloadedSuccess(channelsRetrieved);
		}

		@Override
		public void onError(int resultCode, byte[] result) {
			Log.i("TAG", "result came back erroring");
			switch (resultCode) {
			case HttpStatusCodes.GATEWAY_TIMEOUT:
				callback.onChannelConnectionTimeOut();
				Log.i("TAG", "timeout");
				break;
			case HttpStatusCodes.FORBIDDEN:
				Log.i("TAG", "forbidden");
				callback.onChannelsDownloadFailure("Cannot find any matches!");
				break;
			default:
				Log.i("TAG", "download failure");
				callback.onChannelsDownloadFailure(new String(result));
				break;
			}
		}

		@Override
		public void onFailure(int resultCode, Exception e) {
			e.printStackTrace();
			callback.onChannelsDownloadFailure("Failed to retrieve");
		}

	};

	public void retrieveChannels() {
		retrieveChannelStream();
	}

	private String channelsVar;
	
	private void retrieveChannelStream() {
		Log.i("TAG", "retrieving channels from retriever");
//		Intent intent = new ServiceIntentBuilder(
//				(Application) EPGApp.getContext())
//				.setHttpType(HttpIntentService.SERVICE_TYPE_GET)
//				.setData(Uri.withAppendedPath(Uri.parse(BASE_URL), CHANNELS_API))
//				.withParam(Params.QUERY, sanitizeString(""))
//				.setResultReceiver(channelResponseHandler).build();
//		EPGApp.getContext().startService(intent);
		
		//Canned response.
		InputStream is = SkyEPG.getContext().getResources().openRawResource(R.raw.init);
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		try {
		    Reader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		    int n;
		    try {
				while ((n = reader.read(buffer)) != -1) {
				    writer.write(buffer, 0, n);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
		    try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String jsonString = writer.toString();
		
		ChannelResponse response = new Gson().fromJson(jsonString, ChannelResponse.class);  
		ArrayList<Channels> channelsRetrieved = new ArrayList<Channels>();
			Iterator<Channels> i = response.channels.iterator();
			while (i.hasNext()) {
				Channels channel = (Channels) i.next();
	            if(channelsVar==null){
	            		channelsVar = channel.channelID;
            	}else{
            		channelsVar = channel.channelID + ", "+ channel.channelID;
            	}
				channelsRetrieved.add(channel);
			}
	        
		callback.onChannelsDownloadedSuccess(channelsRetrieved);
	}
	
	private String sanitizeString(String s) {
		try {
			return URLEncoder.encode(s, UTF_8);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}
	
	public interface ChannelsRetrievedCallback {
		public void onChannelsDownloadedSuccess(ArrayList<Channels> result);
		public void onChannelsDownloadFailure(String message);
		public void onChannelConnectionTimeOut();
	}
}
