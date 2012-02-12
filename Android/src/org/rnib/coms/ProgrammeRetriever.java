package org.rnib.coms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import org.rnib.R;
import org.rnib.app.SkyEPG;
import org.rnib.model.channels.Channel;

import android.util.Log;

import com.akshay.http.service.ResultHandler;
import com.akshay.http.service.constants.HttpStatusCodes;
import com.google.gson.Gson;

public class ProgrammeRetriever {

	String DEFAULT_URL = "http://epgservices.sky.com/tvlistings-proxy/TVListingsProxy/tvlistings.json";
	String BASE_URL = "http://epgservices.sky.com/tvlistings-proxy/TVListingsProxy/";
	String PROGS_API = "tvlistings.json";
	
	public interface ProgramesRetrievedCallback {
		public void onProgrammesDownloadedSuccess(ArrayList<org.rnib.model.progs.Channels> result);
		public void onProgrammesDownloadFailure(String message);
		public void onProgrammesConnectionTimeOut();
	}
	
	interface Params {
		final String QUERY = "detail=2&dur=360&time=201202120100&channels=2002,2006,6000,1621,1801,1402,2201,1412,2617,2304,2306,2510,1842,2505,2061,2018,6240,6260,6272,2205,2207,4330,1305,2202,2501,3802,1813,1833,2203,6155,2303,2703,5701,1670,1628,3300,3340,3310,4075,2076,3618,1865,3352,3602,4610,3617,3207,1360,1832";
	}
	
	private ProgramesRetrievedCallback callback;
	
	public ProgrammeRetriever(ProgramesRetrievedCallback letMeKnow) {
		this.callback = letMeKnow;
	}
	
	private final ResultHandler channelResponseHandler = new ResultHandler() {
		@Override
		public void onSuccess(int resultCode, byte[] array) throws IOException {
			Log.i("A successful call", "");
			callback.onProgrammesDownloadedSuccess(null);
		}

		@Override
		public void onError(int resultCode, byte[] result) {
			switch (resultCode) {
			case HttpStatusCodes.GATEWAY_TIMEOUT:
				callback.onProgrammesConnectionTimeOut();
				break;
			case HttpStatusCodes.FORBIDDEN:
				callback.onProgrammesDownloadFailure("Cannot find any matches!");
				break;
			default:
				callback.onProgrammesDownloadFailure(new String(result));
				break;
			}
		}

		@Override
		public void onFailure(int resultCode, Exception e) {
			e.printStackTrace();
			callback.onProgrammesDownloadFailure("Failed to retrieve");
		}

	};
	
	public void retrieveProgs() {
		retrieveProgramStream();
	}
	
	private String channelsVar;
	
	private void retrieveProgramStream() {
		Log.i("TAG", "retrieving channels from retriever");
//		Intent intent = new ServiceIntentBuilder(
//				(Application) EPGApp.getContext())
//				.setHttpType(HttpIntentService.SERVICE_TYPE_GET)
//				.setData(Uri.withAppendedPath(Uri.parse(BASE_URL), CHANNELS_API))
//				.withParam(Params.QUERY, sanitizeString(""))
//				.setResultReceiver(channelResponseHandler).build();
//		EPGApp.getContext().startService(intent);
		
		//Canned response.
		InputStream is = SkyEPG.getContext().getResources().openRawResource(R.raw.progs);
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
		
		ProgrammeResponse response = new Gson().fromJson(jsonString, ProgrammeResponse.class);  
		ArrayList<org.rnib.model.progs.Channels> lineupsRetrieved = new ArrayList<org.rnib.model.progs.Channels>();
		Iterator<org.rnib.model.progs.Channels> i = response.channels.iterator();
		while (i.hasNext()) {
			org.rnib.model.progs.Channels channel = (org.rnib.model.progs.Channels) i.next();
			lineupsRetrieved.add(channel);
		}
	        
		callback.onProgrammesDownloadedSuccess(lineupsRetrieved);
	}

}
