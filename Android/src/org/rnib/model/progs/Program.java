package org.rnib.model.progs;

import com.google.gson.annotations.SerializedName;

public class Program {
	
	@SerializedName("eventid")	
	public String eventid;
	
	@SerializedName("channelid")
	public String channelid;
	
	@SerializedName("date")
	public String date;
	
	@SerializedName("start")
	public String start;
	
	@SerializedName("dur")
	public String dur;
	
	@SerializedName("genreid")
	public String genreid;
	
	@SerializedName("shortDesc")
	public String shortDesc;
	
	@SerializedName("genre")
	public String genre;
	
	@SerializedName("subgenre")
	public String subgenre;
	
	@SerializedName("edschoice")
	public String edschoice;
	
	@SerializedName("remoteRecordable")
	public String remoteRecordable;
	
	@SerializedName("record")
	public String record;
	
	@SerializedName("scheduleStatus")
	public String scheduleStatus;
	
	@SerializedName("blackout")
	public String blackout;
	
	@SerializedName("movielocator")
	public String movielocator;
}
