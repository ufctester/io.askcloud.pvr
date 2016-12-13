'''
    Simple XBMC Download Script
    Copyright (C) 2013 Sean Poyser (seanpoyser@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
'''


import sys,pkgutil,re,json,urllib,urllib2,urlparse,xbmc,xbmcgui,xbmcplugin,xbmcvfs,os,inspect,csv,shutil,random,datetime,time,os

try: from sqlite3 import dbapi2 as database
except: from pysqlite2 import dbapi2 as database

from resources.lib.modules import control
from resources.lib.modules import cleantitle
from resources.lib.modules import client
from resources.lib.modules import debrid
from resources.lib.modules import workers
from resources.lib.sources import sources

try: from sqlite3 import dbapi2 as database
except: from pysqlite2 import dbapi2 as database

try: import urlresolver
except: pass


class plexpvr:
    def __init__(self):
        xbmc.log('ENTRY -> plexpvr.__init__ ', xbmc.LOGNOTICE)

    def tvDownloader(self, title, year, imdb, tvdb, season, episode, tvshowtitle, premiered, meta, select):
        xbmc.log('ENTRY -> plexpvr tvDownloader()', xbmc.LOGNOTICE)
        xbmc.log('    title: %s' % (title), xbmc.LOGNOTICE)
        xbmc.log('    year: %s' % (year), xbmc.LOGNOTICE)
        xbmc.log('    imdb: %s' % (imdb), xbmc.LOGNOTICE)
        xbmc.log('    tvdb: %s' % (tvdb), xbmc.LOGNOTICE)
        xbmc.log('    Season: %s' % (season), xbmc.LOGNOTICE)
        xbmc.log('    Episode: %s' % (episode), xbmc.LOGNOTICE)

        xbmc.log('*** tvDownloader *** year %s' % year, xbmc.LOGDEBUG)
        xbmc.log('*** tvDownloader *** imdb %s' % imdb, xbmc.LOGDEBUG)
        xbmc.log('*** tvDownloader *** tvdb %s' % tvdb, xbmc.LOGDEBUG)
        xbmc.log('*** tvDownloader *** season %s' % season, xbmc.LOGDEBUG)
        xbmc.log('*** tvDownloader *** episode %s' % episode, xbmc.LOGDEBUG)
        xbmc.log('*** tvDownloader *** tvshowtitle %s' % tvshowtitle, xbmc.LOGDEBUG)

        #xbmc.log('*** tvDownloader *** control %s' % control, xbmc.LOGWARNING)
  
        url = None

        control.moderator()

        xbmc.log('*** ALERT *** Getting Sources %s Season: %s Episode: %s' % (tvshowtitle,season,episode), xbmc.LOGDEBUG)

        #This forces the sources() is done on a background tread
        xbmc.log('Setting Exodus setting progress.dialog: 1', xbmc.LOGDEBUG)
        control.setSetting(id='progress.dialog', value='1')

        items = sources().getSources(title, year, imdb, tvdb, season, episode, tvshowtitle, premiered)
        #labels = [i['label'] for i in items]
        xbmc.log('*** tvDownloader *** tvshowtitle %s item size %s' % (tvshowtitle,len(items)), xbmc.LOGWARNING)       

        #downloadSource=None           
        #for i in range(len(items)):
        #    try:
        #        if i < 3:
        #            finalSource=items[i]
        #            if finalSource['url'].endswith(".mp4"):
        #                downloadSource=finalSource
        #                break
        #            xbmc.log('*** tvDownloader *** finalSource %s' % finalSource, xbmc.LOGWARNING)
        #            xbmc.log('*** tvDownloader *** finalSource[quality] %s' % finalSource['quality'], xbmc.LOGWARNING)                  
        #        else:
        #            finalSource=items[i]
        #            if finalSource['url'].endswith(".mp4"):
        #                downloadSource=finalSource
        #                break
        #            xbmc.log('*** tvDownloader *** finalSource %s' % finalSource, xbmc.LOGWARNING)
        #            xbmc.log('*** tvDownloader *** finalSource[quality] %s' % finalSource['quality'], xbmc.LOGWARNING)     
        #            
        #        if downloadSource == None:
        #            downloadSource = items[0] 
        #
        #        xbmc.log('*** tvDownloader *** downloadSource %s' % downloadSource, xbmc.LOGWARNING)    
        #    except:
        #       pass

        for i in range(len(items)):
            try:
                downloadSource=items[i]
                xbmc.log('*** tvDownloader *** downloadSource %s' % downloadSource, xbmc.LOGDEBUG) 
                downloadResults=self.tvDownloaderForSource(downloadSource,title, year, imdb, tvdb, season, episode, tvshowtitle, premiered, meta, select)
                if downloadResults == True:
                    break   
            except:
               pass

        xbmc.log('EXIT <- plexpvr.py tvDownloader()', xbmc.LOGNOTICE)            
        
            
    def tvDownloaderForSource(self,downloadSource,title, year, imdb, tvdb, season, episode, tvshowtitle, premiered, meta, select):
        xbmc.log('ENTRY -> plexpvr.py tvDownloaderForSource()', xbmc.LOGNOTICE)
        xbmc.log('    downloadSource: %s' % (downloadSource), xbmc.LOGNOTICE)
        xbmc.log('    title: %s' % (title), xbmc.LOGNOTICE)
        xbmc.log('    year: %s' % (year), xbmc.LOGNOTICE)
        xbmc.log('    imdb: %s' % (imdb), xbmc.LOGNOTICE)
        xbmc.log('    tvdb: %s' % (tvdb), xbmc.LOGNOTICE)
        xbmc.log('    Season: %s' % (season), xbmc.LOGNOTICE)
        xbmc.log('    Episode: %s' % (episode), xbmc.LOGNOTICE)

        control.playlist.clear()
            
        xbmc.log('*** downloadSource url %s' % downloadSource['url'], xbmc.LOGDEBUG)        
        xbmc.log('*** tvDownloaderForSource *** meta %s' % meta, xbmc.LOGDEBUG)               

        sysaddon = sys.argv[0]
        xbmc.log('*** tvDownloaderForSource *** sysaddon %s' % sysaddon, xbmc.LOGDEBUG)

        syshandle = int(sys.argv[1])
        xbmc.log('*** tvDownloaderForSource *** syshandle %s' % syshandle, xbmc.LOGDEBUG)

        #This is where movies don't work
        if season == None or season == '' or season == 'N/A':
            xbmc.log('*** tvDownloaderForSource ***   show is a Movie: %s' % (title), xbmc.LOGNOTICE)
            name = title
            xbmc.log('*** tvDownloaderForSource ***   name:%s' % (name), xbmc.LOGNOTICE)
            return self.movieDownloadForSource(name,meta,downloadSource,sysaddon)
        else:
            xbmc.log('*** tvDownloaderForSource ***   show is a TVShow: %s' % (title), xbmc.LOGNOTICE)
            name = '%s S%02dE%02d' % (tvshowtitle, int(season), int(episode))
            xbmc.log('*** tvDownloaderForSource ***   name:%s' % (name), xbmc.LOGNOTICE)
            return self.tvShowDownloadForSource(name,tvshowtitle,season,meta,downloadSource,sysaddon)

    def tvShowDownloadForSource(self,name,tvshowtitle, season, meta, downloadSource, sysaddon):
        xbmc.log('ENTRY -> plexpvr.py tvShowDownloadForSource()', xbmc.LOGNOTICE)
        xbmc.log('    name: %s' % (name), xbmc.LOGNOTICE)
        xbmc.log('    tvshowtitle: %s' % (tvshowtitle), xbmc.LOGNOTICE)
        xbmc.log('    season: %s' % (season), xbmc.LOGNOTICE)
        xbmc.log('    meta: %s' % (meta), xbmc.LOGNOTICE)
        xbmc.log('    downloadSource: %s' % (downloadSource), xbmc.LOGNOTICE)
        xbmc.log('    sysaddon: %s' % (sysaddon), xbmc.LOGNOTICE)
    
        #Check if we already have an mp4
        #Path to Donwload
        destinationDir = control.setting('tv.download.path')
        transPath = control.transPath(destinationDir)
        xbmc.log('*** Found Name Destination Dir ***   destinationDir:%s' % (destinationDir), xbmc.LOGNOTICE)
        xbmc.log('*** tvShowDownloadForSource ***   transPath:%s' % (transPath), xbmc.LOGDEBUG)

        #Path to Season
        transPathSeasons = control.transPath('%s/Season %s' % (tvshowtitle,int(season)))
        xbmc.log('*** tvShowDownloadForSource *** transPathSeasons:%s' % (transPathSeasons), xbmc.LOGDEBUG)
        pathToSeason = os.path.join(destinationDir,'%s'% (transPathSeasons))
        xbmc.log('*** tvShowDownloadForSource *** pathToSeason %s' % pathToSeason, xbmc.LOGDEBUG)

        pathToMP4 = os.path.join(pathToSeason,'%s.mp4'% (name))
        xbmc.log('*** tvShowDownloadForSource *** pathToMP4 %s' % pathToMP4, xbmc.LOGDEBUG)

        if os.path.exists(pathToMP4):
            xbmc.log('STOP DOWNLOAD :: MP4 already exists: %s' % pathToMP4, xbmc.LOGNOTICE)
            xbmc.log('EXIT <- plexpvr tvShowDownloadForSource()', xbmc.LOGNOTICE)
            return True
        else:
            xbmc.log('START DOWNLOAD :: MP4 does not exists: %s' % pathToMP4, xbmc.LOGNOTICE)

        xbmc.log('Going to Download TV Show Episode ... %s' % name, xbmc.LOGDEBUG)
        sysname = urllib.quote_plus(name)
        #xbmc.log('*** tvShowDownloadForSource *** sysname %s' % sysname, xbmc.LOGDEBUG)

        #DSP NOTE: The meta object has already been parsed
        meta = json.loads(meta)
        #xbmc.log('*** tvShowDownloadForSource *** meta %s' % meta, xbmc.LOGDEBUG)
        poster = meta['poster'] if 'poster' in meta else '0'
        banner = meta['banner'] if 'banner' in meta else '0'
        thumb = meta['thumb'] if 'thumb' in meta else poster
        fanart = meta['fanart'] if 'fanart' in meta else '0'
        duration = meta['duration'] if 'duration' in meta else '0'

        if poster == '0': poster = control.addonPoster()
        if banner == '0' and poster == '0': banner = control.addonBanner()
        elif banner == '0': banner = poster
        if thumb == '0' and fanart == '0': thumb = control.addonFanart()
        elif thumb == '0': thumb = fanart
        if control.setting('fanart') == 'true' and not fanart == '0': pass
        else: fanart = control.addonFanart()

        sysimage = urllib.quote_plus(poster.encode('utf-8'))
        sysduration = urllib.quote_plus(duration)
        xbmc.log('*** tvShowDownloadForSource *** sysduration %s' % sysduration, xbmc.LOGWARNING)
  
        downloadSourceURL = json.dumps([downloadSource])
        syssource = urllib.quote_plus(downloadSourceURL)
        #xbmc.log('*** tvShowDownloadForSource *** syssource %s' % syssource, xbmc.LOGWARNING)

        #resolveSource = self.sourcesResolve(json.loads(downloadSource)[0], True)
        #xbmc.log('*** tvShowDownloadForSource *** resolveSource %s' % resolveSource, xbmc.LOGWARNING)
        #jsonObj = json.loads(syssource)
        #xbmc.log('*** tvShowDownloadForSource *** jsonObj %s' % jsonObj, xbmc.LOGWARNING)

        #resolveSource = self.sourcesResolve(json.loads(syssource)[0]   
        #xbmc.log('*** tvShowDownloadForSource *** resolveSource %s' % resolveSource, xbmc.LOGWARNING)   
        try:
            xbmc.log('Calling RunPlugin(%s?action=plexdownload&name=%s&duration=%s&image=%s&source=%s)' % (sysaddon, sysname, sysduration, sysimage,  syssource), xbmc.LOGNOTICE)
            control.execute('RunPlugin(%s?action=plexdownload&name=%s&duration=%s&image=%s&source=%s)' % (sysaddon, sysname, sysduration, sysimage, syssource))
        except ResolverError:
            xbmc.log('   **** ERROR **** ResolverError:', xbmc.LOGNOTICE)

        xbmc.log('   **** SLEEPING **** sleeping 10 seconds before processing another source.', xbmc.LOGNOTICE)
        xbmc.sleep(10000)
        xbmc.log('EXIT <- plexpvr tvShowDownloadForSource()', xbmc.LOGNOTICE)
        return False

    #name, image, source is all we need
    def movieDownloadForSource(self,name, meta, downloadSource, sysaddon):
        xbmc.log('ENTRY -> plexpvr.py movieDownloadForSource()', xbmc.LOGNOTICE)
        xbmc.log('    name: %s' % (name), xbmc.LOGNOTICE)
        xbmc.log('    meta: %s' % (meta), xbmc.LOGNOTICE)
        xbmc.log('    downloadSource: %s' % (downloadSource), xbmc.LOGNOTICE)
        xbmc.log('    sysaddon: %s' % (sysaddon), xbmc.LOGNOTICE)
    
        #Check if we already have an mp4
        #Path to Donwload
        destinationDir = control.setting('movie.download.path')
        transPath = control.transPath(destinationDir)
        xbmc.log('*** Found Name Destination Dir *** destinationDir:%s' % (destinationDir), xbmc.LOGNOTICE)
        xbmc.log('*** movieDownloadForSource *** transPath:%s' % (transPath), xbmc.LOGDEBUG)

        #Path to Movie
        movieName = name.translate(None, '\/:*?"<>|').strip('.')
        xbmc.log('*** movieDownloadForSource *** movieName: %s' % (movieName), xbmc.LOGDEBUG)

        #DSP NOTE: The meta object has already been parsed
        meta = json.loads(meta)
        #xbmc.log('*** movieDownloadForSource *** meta %s' % meta, xbmc.LOGDEBUG)
        #Name Movies like <Movie Name> - <Year>
        if 'year' in meta:
            movieName = '%s - %s' % (movieName, meta['year'])

        xbmc.log('*** movieDownloadForSource *** movieName: %s' % (movieName), xbmc.LOGDEBUG)

        #transPathMovie = control.transPath('%s',movieName)
        #xbmc.log('*** movieDownloadForSource *** transPathMovie:%s' % (transPathMovie), xbmc.LOGDEBUG)
        #pathToMovie = os.path.join(destinationDir,'%s'% (transPathMovie))
        pathToMovie = os.path.join(destinationDir,'%s'% (movieName))
        xbmc.log('*** movieDownloadForSource *** pathToMovie %s' % pathToMovie, xbmc.LOGDEBUG)

        pathToMP4 = os.path.join(pathToMovie,'%s.mp4'% (movieName))
        xbmc.log('*** movieDownloadForSource *** pathToMP4 %s' % pathToMP4, xbmc.LOGDEBUG)

        if os.path.exists(pathToMP4):
            xbmc.log('STOP DOWNLOAD :: Movie MP4 already exists: %s' % pathToMP4, xbmc.LOGNOTICE)
            xbmc.log('EXIT <- plexpvr movieDownloadForSource()', xbmc.LOGNOTICE)
            return True
        else:
            xbmc.log('START DOWNLOAD :: Movie MP4 does not exists: %s' % pathToMP4, xbmc.LOGNOTICE)

        xbmc.log('Going to Download Movie ... %s' % name, xbmc.LOGDEBUG)
        sysname = urllib.quote_plus(movieName)
        #xbmc.log('*** movieDownloadForSource *** sysname %s' % sysname, xbmc.LOGDEBUG)

        poster = meta['poster'] if 'poster' in meta else '0'
        banner = meta['banner'] if 'banner' in meta else '0'
        thumb = meta['thumb'] if 'thumb' in meta else poster
        fanart = meta['fanart'] if 'fanart' in meta else '0'
        duration = meta['duration'] if 'duration' in meta else '0'

        if poster == '0': poster = control.addonPoster()
        if banner == '0' and poster == '0': banner = control.addonBanner()
        elif banner == '0': banner = poster
        if thumb == '0' and fanart == '0': thumb = control.addonFanart()
        elif thumb == '0': thumb = fanart
        if control.setting('fanart') == 'true' and not fanart == '0': pass
        else: fanart = control.addonFanart()

        sysimage = urllib.quote_plus(poster.encode('utf-8'))
        sysduration = urllib.quote_plus(duration)
        #xbmc.log('*** movieDownloadForSource *** sysimage %s' % sysimage, xbmc.LOGWARNING)
  
        downloadSourceURL = json.dumps([downloadSource])
        syssource = urllib.quote_plus(downloadSourceURL)
 
        try:
            xbmc.log('Calling RunPlugin(%s?action=plexdownload&name=%s&duration=%s&image=%s&source=%s)' % (sysaddon, sysname, sysduration, sysimage, syssource), xbmc.LOGNOTICE)
            control.execute('RunPlugin(%s?action=plexdownload&name=%s&duration=%s&image=%s&source=%s)' % (sysaddon, sysname, sysduration, sysimage, syssource))
        except ResolverError:
            xbmc.log('   **** ERROR **** ResolverError:', xbmc.LOGNOTICE)

        xbmc.log('   **** SLEEPING **** sleeping 10 seconds before processing another source.', xbmc.LOGNOTICE)
        xbmc.sleep(10000)
        xbmc.log('EXIT <- plexpvr movieDownloadForSource()', xbmc.LOGNOTICE)
        return False


    def clearCacheAndSources(self):
        self.clearCache()
        self.clearSources()

    def clearCache(self):
        xbmc.log('ENTRY -> plexpvr.py clearCache()', xbmc.LOGNOTICE)

        control.idle()
        #DSP uncomment the dialog
        #yes = control.yesnoDialog(control.lang(32056).encode('utf-8'), '', '')
        #if not yes: return
        from resources.lib.modules import cache
        cache.clear()
        control.infoDialog(control.lang(32057).encode('utf-8'), sound=True, icon='INFO')

    def clearSources(self):
        xbmc.log('ENTRY -> plexpvr.py clearSources()', xbmc.LOGNOTICE)
        try:
            control.idle()

            #yes = control.yesnoDialog(control.lang(32407).encode('utf-8'), '', '')
            #if not yes: return

            control.makeFile(control.dataPath)
            dbcon = database.connect(control.providercacheFile)
            dbcur = dbcon.cursor()
            dbcur.execute("DROP TABLE IF EXISTS rel_src")
            dbcur.execute("VACUUM")
            dbcon.commit()

            #control.infoDialog(control.lang(32408).encode('utf-8'), sound=True, icon='INFO')
        except:
            pass
                