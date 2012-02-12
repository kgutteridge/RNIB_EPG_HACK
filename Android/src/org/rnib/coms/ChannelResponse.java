package org.rnib.coms;

import java.util.List;

import org.rnib.model.channels.Channel;
import org.rnib.model.channels.Epggenre;
import org.rnib.model.channels.Filters;
import org.rnib.model.channels.Genre;
import org.rnib.model.channels.RatingsText;
import org.rnib.model.channels.SelectedRegion;
import org.rnib.model.channels.StaticContent;

import com.google.gson.annotations.SerializedName;

public class ChannelResponse
{
	public List <Channel> channels;
	public List <Epggenre> epgGenreList;
	public List <Filters> filterList;
	public List <Genre>	genreList;
	public List <RatingsText> ratingsTextList;
	public List <SelectedRegion> ratingsList;
	public List <StaticContent>	staticContent;
}
