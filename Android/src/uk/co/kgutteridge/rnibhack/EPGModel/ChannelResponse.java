package uk.co.kgutteridge.rnibhack.EPGModel;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import  uk.co.kgutteridge.rnibhack.EPGModel.*;

public class ChannelResponse
{
	List <Channel> 			channelsList;
	List <Epggenre> 		epgGenreList;
	List <Filters> 			filterList;
	List <Genre>			genreList;
	List <RatingsText>		ratingsTextList;
	List <SelectedRegion>	ratingsList;
	List <StaticContent>	staticContent;
	
	
}
