package org.rnib.model.progs;

import java.util.List;

import com.google.gson.annotations.SerializedName;


/*
 *  Channels retireved for the purpose of documenting Programmesw
 */
public class Channels {
	@SerializedName("title")
	public String title;
	
	@SerializedName("channeltype")
	public String channelType;
	
	@SerializedName("channelid")
	public String channelID;
	
	@SerializedName("genre")
	public String genre;
	
	@SerializedName("program")
	public List <Program> programmes;
}
