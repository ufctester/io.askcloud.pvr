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

def episodes=[]    // This will keep the list of episodes we have
def episodeList=[] // This will keep the list of all episodes
def showInfo = [:] // This will keep map of show infomation indexed by the TVDB
def seen=[:]       // This will keep a list of the shows already queried.
print "Processing files..."

missingEpisodes = tryLogCatch{ any{ _args.output }{ '.' }.toFile().getCanonicalFile() }
//def missingEpisodes = new File("C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\missing-episodes.txt")
println "missingEpisodes: " + missingEpisodes.toString() 

args.getFiles().each{ f ->
    // start looping through the video files.
    if (f.isVideo()) {
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
missingEpisodes.append("TVDB_ID,SERIES_NAME,SEASON,EPISODE")

def format=new ExpressionFormat("{n} {S00E00}")
episodeList.each{ e ->
    def info=showInfo.get(e[0])
    info.episodeList.each{  i->
        // Exclude special episodes and those not yet aired.
        if(e[1]==i.season && e[2]==i.episode && i.special==null && i.airdate!=null && i.airdate < simpleNow) {
            //println i.toString()+" ["+i.airdate.toString()+"]"
            //println i.seriesName + " S" + i.season + "E" + i.episode + " id: " + i.seriesInfo.id
            
            //tvdbid,seriesName,season,episode
            //println i.seriesInfo.id + "," + i.seriesName + "," + i.season + "," + i.episode
            
            //add the \n at the beginning so we don't end up with an empty new line
            missingEpisodes.append("\n" + i.seriesInfo.id + "," + i.seriesName + "," + i.season + "," + i.episode)
            //println format.format(i.toString());
        }
    }
}