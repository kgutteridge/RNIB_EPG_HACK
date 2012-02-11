package uk.co.kgutteridge.rnibhack.EPGModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import uk.co.kgutteridge.app.EPGApp;
import uk.co.kgutteridge.rnibhack.R;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
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

	private final ResultHandler textHandler = new ResultHandler() {
		@Override
		public void onSuccess(int resultCode, byte[] array) throws IOException {
			
			ChannelResponse response = new Gson().fromJson(new String(array), ChannelResponse.class);  
//		        Log.i("TAG", "channels " + response.channels.size());

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
			switch (resultCode) {
			case HttpStatusCodes.GATEWAY_TIMEOUT:
				callback.onConnectionTimeOut();
				break;
			case HttpStatusCodes.FORBIDDEN:
				callback.onDownloadFailure("Cannot find any matches!");
				break;
			default:
				callback.onDownloadFailure(new String(result));
				break;
			}
		}

		@Override
		public void onFailure(int resultCode, Exception e) {
			e.printStackTrace();
			callback.onDownloadFailure("Failed to retrieve");
		}

	};

	public void retrieveChannels() {
		retrieveChannelStream();
	}

	private void retrieveChannelStream() {
		Intent intent = new ServiceIntentBuilder(
				(Application) EPGApp.getContext())
				.setHttpType(HttpIntentService.SERVICE_TYPE_GET)
				.setData(Uri.withAppendedPath(Uri.parse(BASE_URL), CHANNELS_API))
				.setResultReceiver(textHandler).build();
		EPGApp.getContext().startService(intent);
	}
	
	public interface RetrieverCallback {
		public void onDownloadSuccess(ArrayList<Channels> result);
		public void onDownloadFailure(String message);
		public void onConnectionTimeOut();
	}
}
