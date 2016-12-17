//
// Read files in disk and 
// search for missing episodes not yet aired
//
//Episodes have the following data
//  - absolute: 1
//  - airdate: 1997-03-10
//  - episode: 1
//  - id: 2
//  - season: 1
//  - seasoninfo: TheTVDBv2::70327
//  - seriesName: Buffy the Vampire Slayer
//  - special: null
//  - title: Welcome to the Hellmouth (1)
//

import net.filebot.format.*
import net.filebot.media.*

// Define first what is now.
def now = Calendar.instance
def simpleNow=new SimpleDate(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))

def episodes=[]    // This will keep the list of episodes we have
def episodeList=[] // This will keep the list of all episodes
def showInfo = [:] // This will keep map of show infomation indexed by the TVDB
def seen=[:]       // This will keep a list of the shows already queried.
print "Processing files..."

def missingEpisodes = new File("C:\\gitbash\\opt\\eclipse\\workspace\\io.askcloud.pvr\\missing-episodes.txt")

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

println "Missing episodes"
def format=new ExpressionFormat("{plex}")
episodeList.each{ e ->
    def info=showInfo.get(e[0])
    info.episodeList.each{  i->
        // Exclude special episodes and those not yet aired.
        if(e[1]==i.season && e[2]==i.episode && i.special==null && i.airdate!=null && i.airdate < simpleNow) {
            //println i.toString()+" ["+i.airdate.toString()+"]"
            println NamingStandard.Plex.getPath(i)
            missingEpisodes.append(NamingStandard.Plex.getPath(i) + "\n")
            //println format.format(i.toString());
        }
    }
}