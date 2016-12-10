//
// Read files in disk and find missing episodes and then put the missing 
// episodes into the following format and into a csv file 
// "TVDB_ID,SERIES_NAME,SEASON,EPISODE"
//
//

@Grab('org.apache.commons:commons-csv:1.4')
@Grab('commons-io:commons-io:2.4')
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.*
import org.apache.commons.io.*

import java.nio.file.Paths

import net.filebot.format.*
import net.filebot.media.*

// Define first what is now.
log.info("Run script [$_args.script] at [$now]")

args.withIndex().each{ f, i -> if (f.exists()) { log.finest "Argument[$i]: $f" } else { log.warning "Argument[$i]: File does not exist: $f" } }

def now = Calendar.instance
def simpleNow=new SimpleDate(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))

// extra options, myepisodes updates and email notifications
excludeList = tryQuietly{ def f = excludeList as File; f.isAbsolute() ? f : outputFolder.resolve(f.path) }

def episodes=[]    // This will keep the list of episodes we have
def episodeList=[] // This will keep the list of all episodes
def showInfo = [:] // This will keep map of show infomation indexed by the TVDB
def seen=[:]       // This will keep a list of the shows already queried.

missingEpisodes = tryLogCatch{ any{ _args.output }{ '.' }.toFile().getCanonicalFile() }
log.info "missingEpisodes: " + missingEpisodes.toString() 


class IgnoreShow {
    String tvdbid, seriesName, season, episode
}

ignoreShows = []

//read in the exclude seasons or episodes for filtering
CSVFormat format = CSVFormat.EXCEL.withHeader().withQuoteMode(QuoteMode.MINIMAL);
CSVParser csv = CSVParser.parse(excludeList,Charset.forName("UTF-8"),format)
csv.iterator().each { record ->
    ignoreShows << ([tvdbid: record.TVDB_ID,
         seriesName: record.SERIES_NAME,
         season: record.SEASON,
         episode: record.EPISODE] as IgnoreShow)
}
    
//for (ignore in ignoreShows) {
//    log.info "tvdbid: " + ignore.tvdbid + " seriesName: " + ignore.seriesName + " season: " + ignore.season + " episode: " + ignore.episode
//}   

def removePunctuation(text){
	if(text != null)
	{
		results = text.replaceAll("\\p{Punct}+", "")
		results = results.replaceAll("  ", " ")
		return results
	}
	else
	{
		return text
	}
} 
  
//NOTE: This will determine if an entire series should be skipped which will ensure no metadata is retreived
def acceptSeries(f,excludeSet) {
	log.fine "aceptFile() ... Determine if should filter Season from Episode: $f "
	filePathName=f.toString().toLowerCase()
	
	if (f.isHidden()) {
		log.fine "Ignore hidden: $f"
		return false
	}

	if (f.isDirectory() && f.name ==~ /[.@].+|bin|initrd|opt|sbin|var|dev|lib|proc|sys|var.defaults|etc|lost.found|root|tmp|etc.defaults|mnt|run|usr|System.Volume.Information/) {
		log.fine "Ignore system path: $f"
		return false
	}
		
	def sXe = parseEpisodeNumber(f)
	def series = detectSeriesName(f)
	
	//Ignore Specials
	log.fine "Checking if the file is a special: $f series: $series sXe: $sXe"
	if((series == null) || (sXe == null))
	{
		log.fine "Ignore Specials: $f"	
		return false
	}
	
	series=removePunctuation(series).toLowerCase()
	def season = String.valueOf(sXe.season).toLowerCase()
	def episode = String.valueOf(sXe.episode).toLowerCase()
	log.fine "seriesName: $series Season: $season Episode: $episode"
	
	log.fine "Exclude series set: " + excludeSet
	// ignore archives that are on the exclude path list

	for (i = 0; i < excludeSet.size(); i++) {		
		tvdbidExclude=excludeSet[i].tvdbid
		seriesExclude=excludeSet[i].seriesName.toLowerCase()
		seasonExclude=excludeSet[i].season
		episodeExclude=excludeSet[i].episode
		
	    log.fine "tvdbid: " + tvdbidExclude + " seriesName: " + seriesExclude + " season: " + seasonExclude + " episode: " + episodeExclude
	     
	    //Series Matches
	    log.fine "Checking if series: $series contains seriesExclude: $seriesExclude"
		if(series.contains(seriesExclude))
		{
			//Check to see if the entire show should be ignored
			if((seasonExclude.equals("ALL")) && (episodeExclude.equals("ALL")))
			{
				log.fine "Excluded Series: $series"
				return false				
			}
		}			    
	}  

	// process only media files (accept audio files only if music mode is enabled)
	return f.isDirectory() || f.isVideo() || f.isSubtitle() || (music && f.isAudio())
}

//NOTE: This will determine if a specific season and episode should be ignored (this can happen if tvdb is wrong or we can not find a download for it
def acceptMissingEpisode(episodeDetails,excludeSet) {
	log.fine "acceptMissingEpisode() ... Determine if should filter the missing episode"
	
	//ignore specials
	if(episodeDetails.special!=null)
	{
		return false
	}
		
	seriesId=String.valueOf(episodeDetails.seriesInfo.id) 
	series=removePunctuation(episodeDetails.seriesName).toLowerCase()
	season=String.valueOf(episodeDetails.season)
	episode=String.valueOf(episodeDetails.episode)
	log.fine "acceptMissingEpisode() seriesId: $seriesId seriesName: $series Season: $season Episode: $episode"
	

	// ignore archives that are on the exclude path list
	for (i = 0; i < excludeSet.size(); i++) {		
		tvdbidExclude=excludeSet[i].tvdbid
		seriesExclude=excludeSet[i].seriesName.toLowerCase()
		seasonExclude=excludeSet[i].season
		episodeExclude=excludeSet[i].episode
		
	    log.fine "Exclude: tvdbid: " + tvdbidExclude + " seriesName: " + seriesExclude + " season: " + seasonExclude + " episode: " + episodeExclude
	     
	    //Check if the tvdb id matches
	    log.fine "Checking tvdbidExclude: $tvdbidExclude to seriesId: $seriesId"
	    if(tvdbidExclude.equals(seriesId))
	    {
	    	log.fine "seriesId: $seriesId Matched: $tvdbidExclude"
		    //Series Matches
			if(series.contains(seriesExclude))
			{
				log.fine "seriesName: $series Matched: $seriesExclude"
				//check if the entire season should be ignored
				if((seasonExclude.equals("ALL")) || (season.equals(seasonExclude)))
				{
					//check if the episode is set to exclude ALL or just a specific episode
					if((episodeExclude.equals("ALL")) || (episode.equals(episodeExclude)))
					{
						log.fine "Exclude Missing Episode: $series $season" + "x$episode"
						log.fine "Excluding seriesId: $seriesId seriesName: $series Season: $season Episode: $episode"
						return false			
					}					
				}
			}		    
	    }  
	}  

	return true;
}

args.getFiles().each{ f ->
    // start looping through the video files.   
    // start looping through the video files.   
    if(!acceptSeries(f,ignoreShows))
    {
    	log.fine "TV Show Series is in the IGNORE list: " + f;
    }
    else if (f.isVideo()) {
    	log.fine "TV Show Series is in the INCLUDE list: " + f;
    	log.fine "Accept video file: " + f + " Searching for video metadata";
        // Get info from the filename
        def episode = parseEpisodeNumber(f)
        def show = detectSeriesName(f)
    
        if (episode != null & show != null) {
            if (!seen.containsKey(show)) {   
                log.info "Reading info for " + show + " ... "
                def rs=TheTVDB.search(show,Locale.ENGLISH)
                if (rs !=null) {
                    def s=rs[0]
                    log.finest "found " +s
                    def i=TheTVDB.getSeriesData(s,SortOrder.Airdate,Locale.ENGLISH)
                    i.episodeList.each { j->
                        episodeList << [i.seriesInfo.id,j.season,j.episode]
                    }
                showInfo.put(i.seriesInfo.id,i)
                seen.put(show,i)
                }
            }
            def info=seen.get(show)
            episodes << [info.seriesInfo.id,episode.season,episode.episode]     
        }
        else {
            log.info "ERROR: Could not parse " + f.toString()
        }
    }
}

episodeList = episodeList as LinkedHashSet
episodeList.removeAll(episodes)

//Create the header in the csv
//tvdbid,seriesName,season,episode
missingEpisodes.append("TVDB_ID,IMDB_ID,SERIES_NAME,SEASON,EPISODE,ENDED,STATUS,DOWNLOAD_PERCENT")

episodeList.each{ e ->
    def info=showInfo.get(e[0])
    info.episodeList.each{  i->
        // Exclude special episodes and those not yet aired.
        if(e[1]==i.season && e[2]==i.episode && i.special==null && i.airdate!=null && i.airdate < simpleNow) {
        
		    if(!acceptMissingEpisode(i,ignoreShows))
		    {
		    	log.fine "TV Show Episode is missing but is in the IGNORE list: " + i.seriesName + " " + i.season + "E" + i.episode;
		    }                	
            else if(i.seriesInfo.status == 'Ended'){
            	missingEpisodes.append("\n" + i.seriesInfo.id + "," + "," + i.seriesName + "," + i.season + "," + i.episode + ",true" + ",QUEUED,-1")
            } 
            else
            {
            	missingEpisodes.append("\n" + i.seriesInfo.id + "," + "," + i.seriesName + "," + i.season + "," + i.episode + ",false" + ",QUEUED,-1")
            }
        }
    }
}