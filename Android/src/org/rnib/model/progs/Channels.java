package org.rnib.model.progs;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Channels {
	@SerializedName("title")
	public String channelNo;
	
	@SerializedName("channeltype")
	public String channelType;
	
	@SerializedName("channelid")
	public String channelID;
	
	@SerializedName("genre")
	public String genre;
	
	public List <Program> programmes;
}
