package uk.co.kgutteridge.rnibhack.EPGModel;

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

import uk.co.kgutteridge.app.EPGApp;
import uk.co.kgutteridge.rnibhack.R;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.akshay.http.service.HttpIntentService;
import com.akshay.http.service.ResultHandler;
import com.akshay.http.service.builders.ServiceIntentBuilder;
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

	private RetrieverCallback callback;

	public ChannelRetriever(RetrieverCallback callback) {
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
		        
			
			callback.onDownloadSuccess(channelsRetrieved);
		}

		@Override
		public void onError(int resultCode, byte[] result) {
			Log.i("TAG", "result came back erroring");
			switch (resultCode) {
			case HttpStatusCodes.GATEWAY_TIMEOUT:
				callback.onConnectionTimeOut();
				Log.i("TAG", "timeout");
				break;
			case HttpStatusCodes.FORBIDDEN:
				Log.i("TAG", "forbidden");
				callback.onDownloadFailure("Cannot find any matches!");
				break;
			default:
				Log.i("TAG", "download failure");
				callback.onDownloadFailure(new String(result));
				break;
			}
		}

		@Override
		public void onFailure(int resultCode, Exception e) {
			Log.i("TAG", "result came failing");
			e.printStackTrace();
			callback.onDownloadFailure("Failed to retrieve");
		}

	};

	public void retrieveChannels() {
		retrieveChannelStream();
	}

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
		InputStream is = EPGApp.getContext().getResources().openRawResource(R.raw.init);
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
				Channels res = (Channels) i.next();
				channelsRetrieved.add(res);
			}
	        
		callback.onDownloadSuccess(channelsRetrieved);
		
	}
	
	private String sanitizeString(String s) {
		try {
			return URLEncoder.encode(s, UTF_8);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}
	
	public interface RetrieverCallback {
		public void onDownloadSuccess(ArrayList<Channels> result);
		public void onDownloadFailure(String message);
		public void onConnectionTimeOut();
	}
}
