//
// Read files in disk and find missing episodes and then put the missing 
// episodes into the following format and into a csv file 
// "TVDB_ID,NAME,SEASON,EPISODE"
//
//

import net.filebot.format.*
import net.filebot.media.*

// Define first what is now.
log.info("Run script [$_args.script] at [$now]")

args.withIndex().each{ f, i -> if (f.exists()) { log.finest "Argument[$i]: $f" } else { log.warning "Argument[$i]: File does not exist: $f" } }

def now = Calendar.instance
def simpleNow=new SimpleDate(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))

episodesList = tryLogCatch{ any{ _args.output }{ '.' }.toFile().getCanonicalFile() }
log.info "Finding Episodes that are in the directory: " + episodesList.toString() 

//Create the header in the csv
//tvdbid,seriesName,season,episode
println "tvdbid,seriesName,season,episode" 
episodesList.append("TVDB_ID,IMDB_ID,NAME,SEASON,EPISODE,ENDED")

args.getFiles().each{ f ->
    // start looping through the video files.   
	if (f.isVideo()) {
	
    	log.info "Accept video file: " + f + " Searchig for video metadata";
    	
    	episodesList.append("\n" + f.toString().toLowerCase())	
	}
    else {
        log.info "ERROR: Could not parse " + f.toString()
    }
}