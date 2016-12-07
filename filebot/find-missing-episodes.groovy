//
// Read files in disk and find missing episodes and then put the missing 
// episodes into the following format and into a csv file 
// "TVDB_ID,SERIES_NAME,SEASON,EPISODE"
//
//

import net.filebot.format.*
import net.filebot.media.*

// Define first what is now.
log.fine("Run script [$_args.script] at [$now]")

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
println "missingEpisodes: " + missingEpisodes.toString() 

// define and load exclude list (e.g. to make sure files are only processed once)
excludeSeriesSet = []

if (excludeList) {
	log.info "Exclude File Exists.  Iterate over exclude list: " + excludeList
	if (excludeList.exists()) {
		excludeSeriesSet=excludeList as String[]
		log.fine "Use excludes: $excludeList ${excludeSeriesSet} size: (${excludeSeriesSet.size()})"
	} else {
		log.fine "Use excludes: $excludeList"
		if ((!excludeList.parentFile.isDirectory() && !excludeList.parentFile.mkdirs()) || (!excludeList.isFile() && !excludeList.createNewFile())) {
			fail "Failed to create excludeList: $excludeList"
		}
	}
}


def acceptFile(f, excludeSet) {
	log.fine "aceptFile() ... Determine if should filter Season from Episode: $f " + " excludeSet: " + excludeSet

	log.fine "Looping over excludeSet: " + excludeSet
//	excludeSeriesSet.each {
//	    log.fine "excludeSeriesSet ${it}"
//	}
	
	if (f.isHidden()) {
		log.fine "Ignore hidden: $f"
		return false
	}

	if (f.isDirectory() && f.name ==~ /[.@].+|Cops|bin|initrd|opt|sbin|var|dev|lib|proc|sys|var.defaults|etc|lost.found|root|tmp|etc.defaults|mnt|run|usr|System.Volume.Information/) {
		log.info "Ignore system path: $f"
		return false
	}
	
	log.fine "Exclude series set: " + excludeSet
	// ignore archives that are on the exclude path list
	for (i = 0; i < excludeSet.length; i++) {
		log.fine "Exclude item " + excludeSet[i].toString().toLowerCase()
		log.fine "File " + f
		if(f.toString().toLowerCase().contains(excludeSet[i].toString().toLowerCase()))
		{
			log.fine "*** Excluded Series: $f"
			return false;
		}	   
	}	

	// process only media files (accept audio files only if music mode is enabled)
	return f.isDirectory() || f.isVideo() || f.isSubtitle() || (music && f.isAudio())
}

args.getFiles().each{ f ->
    // start looping through the video files.   
    if(!acceptFile(f,excludeSeriesSet))
    {
    	log.fine("TV Show is in exclude list: " + f);
    }
    else if (f.isVideo()) {
    	log.fine("Accept video file: " + f);
    	log.fine("Searching for video metadata: " + f);
        // Get info from the filename
        def episode = parseEpisodeNumber(f)
        def show = detectSeriesName(f)
    
        if (episode != null & show != null) {
            if (!seen.containsKey(show)) {   
                log.finest "Reading info for "+show+" ... "
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
            log.info "** Could not parse "+f.toString()
        }
    }
}

episodeList = episodeList as LinkedHashSet
episodeList.removeAll(episodes)

//Create the header in the csv
//tvdbid,seriesName,season,episode
println "tvdbid,seriesName,season,episode" 
missingEpisodes.append("TVDB_ID,IMDB_ID,SERIES_NAME,SEASON,EPISODE,ENDED")

def format=new ExpressionFormat("{n} {S00E00}")
episodeList.each{ e ->
    def info=showInfo.get(e[0])
    info.episodeList.each{  i->
        // Exclude special episodes and those not yet aired.
        if(e[1]==i.season && e[2]==i.episode && i.special==null && i.airdate!=null && i.airdate < simpleNow) {
        
            if(i.seriesInfo.status == 'Ended'){
            	missingEpisodes.append("\n" + i.seriesInfo.id + "," + "," + i.seriesName + "," + i.season + "," + i.episode + ",true")
            } 
            else
            {
            	missingEpisodes.append("\n" + i.seriesInfo.id + "," + "," + i.seriesName + "," + i.season + "," + i.episode + ",false")
            }
        }
    }
}