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

    def tvshowDownload(self, title, year, imdb, tvdb, season, episode, tvshowtitle, premiered, meta, select):
        xbmc.log('ENTRY -> plexpvr tvshowDownload()', xbmc.LOGNOTICE)
        xbmc.log('    title: %s' % (title), xbmc.LOGNOTICE)
        xbmc.log('    year: %s' % (year), xbmc.LOGNOTICE)
        xbmc.log('    imdb: %s' % (imdb), xbmc.LOGNOTICE)
        xbmc.log('    tvdb: %s' % (tvdb), xbmc.LOGNOTICE)
        xbmc.log('    Season: %s' % (season), xbmc.LOGNOTICE)
        xbmc.log('    Episode: %s' % (episode), xbmc.LOGNOTICE)

        xbmc.log('*** tvshowDownload *** year %s' % year, xbmc.LOGDEBUG)
        xbmc.log('*** tvshowDownload *** imdb %s' % imdb, xbmc.LOGDEBUG)
        xbmc.log('*** tvshowDownload *** tvdb %s' % tvdb, xbmc.LOGDEBUG)
        xbmc.log('*** tvshowDownload *** season %s' % season, xbmc.LOGDEBUG)
        xbmc.log('*** tvshowDownload *** episode %s' % episode, xbmc.LOGDEBUG)
        xbmc.log('*** tvshowDownload *** tvshowtitle %s' % tvshowtitle, xbmc.LOGDEBUG)

        #xbmc.log('*** tvshowDownload *** control %s' % control, xbmc.LOGWARNING)
  
        url = None

        control.moderator()
 
        xbmc.log('*** ALERT *** Getting Sources %s Season: %s Episode: %s' % (tvshowtitle,season,episode), xbmc.LOGDEBUG)
        items = sources().getSources(title, year, imdb, tvdb, season, episode, tvshowtitle, premiered)
        #labels = [i['label'] for i in items]
        xbmc.log('*** tvshowDownload *** tvshowtitle %s item size %s' % (tvshowtitle,len(items)), xbmc.LOGWARNING)       

        #downloadSource=None           
        #for i in range(len(items)):
        #    try:
        #        if i < 3:
        #            finalSource=items[i]
        #            if finalSource['url'].endswith(".mp4"):
        #                downloadSource=finalSource
        #                break
        #            xbmc.log('*** tvshowDownload *** finalSource %s' % finalSource, xbmc.LOGWARNING)
        #            xbmc.log('*** tvshowDownload *** finalSource[quality] %s' % finalSource['quality'], xbmc.LOGWARNING)                  
        #        else:
        #            finalSource=items[i]
        #            if finalSource['url'].endswith(".mp4"):
        #                downloadSource=finalSource
        #                break
        #            xbmc.log('*** tvshowDownload *** finalSource %s' % finalSource, xbmc.LOGWARNING)
        #            xbmc.log('*** tvshowDownload *** finalSource[quality] %s' % finalSource['quality'], xbmc.LOGWARNING)     
        #            
        #        if downloadSource == None:
        #            downloadSource = items[0] 
        #
        #        xbmc.log('*** tvshowDownload *** downloadSource %s' % downloadSource, xbmc.LOGWARNING)    
        #    except:
        #       pass

        for i in range(len(items)):
            try:
                downloadSource=items[i]
                xbmc.log('*** tvshowDownload *** downloadSource %s' % downloadSource, xbmc.LOGDEBUG) 
                downloadResults=self.tvshowDownloadForSource(downloadSource,title, year, imdb, tvdb, season, episode, tvshowtitle, premiered, meta, select)
                if downloadResults == True:
                    break   
            except:
               pass

        xbmc.log('EXIT <- plexpvr.py tvshowDownload()', xbmc.LOGNOTICE)            
        
            
    def tvshowDownloadForSource(self,downloadSource,title, year, imdb, tvdb, season, episode, tvshowtitle, premiered, meta, select):
        xbmc.log('ENTRY -> plexpvr.py tvshowDownloadForSource()', xbmc.LOGNOTICE)
        xbmc.log('    downloadSource: %s' % (downloadSource), xbmc.LOGNOTICE)
        xbmc.log('    title: %s' % (title), xbmc.LOGNOTICE)
        xbmc.log('    year: %s' % (year), xbmc.LOGNOTICE)
        xbmc.log('    imdb: %s' % (imdb), xbmc.LOGNOTICE)
        xbmc.log('    tvdb: %s' % (tvdb), xbmc.LOGNOTICE)
        xbmc.log('    Season: %s' % (season), xbmc.LOGNOTICE)
        xbmc.log('    Episode: %s' % (episode), xbmc.LOGNOTICE)

        control.playlist.clear()
            
        xbmc.log('*** downloadSource url %s' % downloadSource['url'], xbmc.LOGDEBUG)        
        xbmc.log('*** tvshowDownloadForSource *** meta %s' % meta, xbmc.LOGDEBUG)               

        sysaddon = sys.argv[0]
        xbmc.log('*** tvshowDownloadForSource *** sysaddon %s' % sysaddon, xbmc.LOGDEBUG)

        syshandle = int(sys.argv[1])
        xbmc.log('*** tvshowDownloadForSource *** syshandle %s' % syshandle, xbmc.LOGDEBUG)

        #This is where movies don't work
        if season == None or season == '' or season == 'N/A':
            xbmc.log('*** tvshowDownloadForSource ***   show is a Movie: %s' % (title), xbmc.LOGNOTICE)
            name = title
        else:
            xbmc.log('*** tvshowDownloadForSource ***   show is a TVShow: %s' % (title), xbmc.LOGNOTICE)
            name = '%s S%02dE%02d' % (tvshowtitle, int(season), int(episode))

        xbmc.log('*** tvshowDownloadForSource ***   name:%s' % (name), xbmc.LOGNOTICE)

        #DSP TODO HANDLE MOVIES
        #Check if we already have an mp4
        #Path to Donwload
        destinationDir = control.setting('tv.download.path')
        transPath = control.transPath(destinationDir)
        xbmc.log('*** tvshowDownloadForSource ***   transPath:%s' % (transPath), xbmc.LOGDEBUG)

        #Path to Season
        transPathSeasons = control.transPath('%s/Season %s' % (tvshowtitle,int(season)))
        xbmc.log('*** tvshowDownloadForSource *** transPathSeasons:%s' % (transPathSeasons), xbmc.LOGDEBUG)
        pathToSeason = os.path.join(destinationDir,'%s'% (transPathSeasons))
        xbmc.log('*** tvshowDownloadForSource *** pathToSeason %s' % pathToSeason, xbmc.LOGDEBUG)

        #Path to TVShow
        transPathToShow = control.transPath('%s' % tvshowtitle)
        xbmc.log('*** tvshowDownloadForSource *** transPathToShow:%s' % (transPathToShow), xbmc.LOGDEBUG)
        pathToTVShow = os.path.join(pathToSeason,'%s'% (transPathSeasons))
        xbmc.log('*** tvshowDownloadForSource *** pathToTVShow %s' % pathToTVShow, xbmc.LOGDEBUG)

        pathToMP4 = os.path.join(pathToSeason,'%s.mp4'% (name))
        xbmc.log('*** tvshowDownloadForSource *** pathToMP4 %s' % pathToMP4, xbmc.LOGDEBUG)

        if os.path.exists(pathToMP4):
            xbmc.log('STOP DOWNLOAD :: MP4 already exists: %s' % pathToMP4, xbmc.LOGNOTICE)
            xbmc.log('EXIT <- plexpvr tvshowDownloadForSource()', xbmc.LOGNOTICE)
            return True
        else:
            xbmc.log('START DOWNLOAD :: MP4 does not exists: %s' % pathToMP4, xbmc.LOGNOTICE)

        xbmc.log('Going to Download Name ... %s' % name, xbmc.LOGDEBUG)
        sysname = urllib.quote_plus(name)
        #xbmc.log('*** tvshowDownloadForSource *** sysname %s' % sysname, xbmc.LOGDEBUG)

        meta = json.loads(meta)
        #xbmc.log('*** tvshowDownloadForSource *** meta %s' % meta, xbmc.LOGDEBUG)
        poster = meta['poster'] if 'poster' in meta else '0'
        banner = meta['banner'] if 'banner' in meta else '0'
        thumb = meta['thumb'] if 'thumb' in meta else poster
        fanart = meta['fanart'] if 'fanart' in meta else '0'

        if poster == '0': poster = control.addonPoster()
        if banner == '0' and poster == '0': banner = control.addonBanner()
        elif banner == '0': banner = poster
        if thumb == '0' and fanart == '0': thumb = control.addonFanart()
        elif thumb == '0': thumb = fanart
        if control.setting('fanart') == 'true' and not fanart == '0': pass
        else: fanart = control.addonFanart()

        sysimage = urllib.quote_plus(poster.encode('utf-8'))
        #xbmc.log('*** tvshowDownloadForSource *** sysimage %s' % sysimage, xbmc.LOGWARNING)
  
        downloadSourceURL = json.dumps([downloadSource])
        syssource = urllib.quote_plus(downloadSourceURL)
        #xbmc.log('*** tvshowDownloadForSource *** syssource %s' % syssource, xbmc.LOGWARNING)

        #resolveSource = self.sourcesResolve(json.loads(downloadSource)[0], True)
        #xbmc.log('*** tvshowDownloadForSource *** resolveSource %s' % resolveSource, xbmc.LOGWARNING)
        #jsonObj = json.loads(syssource)
        #xbmc.log('*** tvshowDownloadForSource *** jsonObj %s' % jsonObj, xbmc.LOGWARNING)

        #resolveSource = self.sourcesResolve(json.loads(syssource)[0]   
        #xbmc.log('*** tvshowDownloadForSource *** resolveSource %s' % resolveSource, xbmc.LOGWARNING)   
        try:
            xbmc.log('Calling RunPlugin(%s?action=plexdownload&name=%s&image=%s&source=%s)' % (sysaddon, sysname, sysimage, syssource), xbmc.LOGNOTICE)
            control.execute('RunPlugin(%s?action=plexdownload&name=%s&image=%s&source=%s)' % (sysaddon, sysname, sysimage, syssource))
        except ResolverError:
            xbmc.log('   **** ERROR **** ResolverError:', xbmc.LOGNOTICE)

        xbmc.log('   **** SLEEPING **** sleeping 10 seconds before processing another source.', xbmc.LOGNOTICE)
        xbmc.sleep(10000)
        xbmc.log('EXIT <- plexpvr tvshowDownloadForSource()', xbmc.LOGNOTICE)
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
                