package uk.co.kgutteridge.rnibhack.EPGModel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ChannelResponse
{
	public List <Channels> channels;
	public List <Epggenre> epgGenreList;
	public List <Filters> filterList;
	public List <Genre>	genreList;
	public List <RatingsText> ratingsTextList;
	public List <SelectedRegion> ratingsList;
	public List <StaticContent>	staticContent;
}
