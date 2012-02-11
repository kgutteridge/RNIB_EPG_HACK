package uk.co.kgutteridge.rnibhack.EPGModel;

import com.google.gson.annotations.SerializedName;

public class Channel
{
	@SerializedName("channelno")
	public String channelNo;
	
	@SerializedName("epggenre")
	public String epggenre;
	
	@SerializedName("title")
	public String title;
	
	@SerializedName("channeltype")
	public String channelType;
	
	@SerializedName("channelid")
	public String channelID;
	
	@SerializedName("genre")
	public String genre;
}
