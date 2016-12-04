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

import re,json,urllib,urllib2,urlparse,xbmc,xbmcgui,xbmcplugin,xbmcvfs,os,inspect,shutil,csv,threading

def download(name, image, url, duration):
    xbmc.log('ENTRY -> plexpvrdownloader.py download()', xbmc.LOGNOTICE)
    xbmc.log('    name: %s' % (name), xbmc.LOGNOTICE)
    xbmc.log('    image: %s' % (image), xbmc.LOGNOTICE)
    xbmc.log('    url: %s' % (url), xbmc.LOGNOTICE)
    xbmc.log('    duration: %s' % (duration), xbmc.LOGNOTICE)

    if url == None: return

    from resources.lib.modules import control

    try: headers = dict(urlparse.parse_qsl(url.rsplit('|', 1)[1]))
    except: headers = dict('')

    url = url.split('|')[0]

    content = re.compile('(.+?)\sS(\d*)E\d*$').findall(name)
    transname = name.translate(None, '\/:*?"<>|').strip('.')
    levels =['../../../..', '../../..', '../..', '..']

    if len(content) == 0:
        dest = control.setting('movie.download.path')
        dest = control.transPath(dest)
        for level in levels:
            try: control.makeFile(os.path.abspath(os.path.join(dest, level)))
            except: pass
        control.makeFile(dest)
        dest = os.path.join(dest, transname)
        control.makeFile(dest)
    else:
        dest = control.setting('tv.download.path')
        dest = control.transPath(dest)
        for level in levels:
            try: control.makeFile(os.path.abspath(os.path.join(dest, level)))
            except: pass
        control.makeFile(dest)
        transtvshowtitle = content[0][0].translate(None, '\/:*?"<>|').strip('.')
        dest = os.path.join(dest, transtvshowtitle)
        control.makeFile(dest)
        dest = os.path.join(dest, 'Season %01d' % int(content[0][1]))
        control.makeFile(dest)

    ext = os.path.splitext(urlparse.urlparse(url).path)[1][1:]
    if not ext in ['mp4', 'mkv', 'flv', 'avi', 'mpg']: ext = 'mp4'
    dest = os.path.join(dest, transname + '.' + ext)
    filename = dest
    xbmc.log('**** DOWNLOAD DEST ******* using dest %s' % dest, xbmc.LOGDEBUG)

    if dest.endswith(".flv"):
        xbmc.log('   **** ERROR **** found flv file.  Ignoring flv files: %s' % dest, xbmc.LOGWARNING) 
        return  

    if os.path.exists(filename):
        #xbmc.log('############# download ############# using fileExists %s' % dest, xbmc.LOGWARNING)
        i = 0

        while os.path.exists(filename):
            i += 1
            filename=dest.replace('.%s' , ' (%s).%s') % (ext, str(i) ,ext)
            #xbmc.log('############# download ############# using dest %s' % dest, xbmc.LOGWARNING)
    
    dest=filename
    xbmc.log('############# FINAL DESTINATION ############# %s' % dest, xbmc.LOGDEBUG)

    sysheaders = urllib.quote_plus(json.dumps(headers))

    sysurl = urllib.quote_plus(url)

    systitle = urllib.quote_plus(name)

    sysimage = urllib.quote_plus(image)

    xbmc.log('**** FINAL DOWNLOAD NAME ******* Downloading FILE %s' % dest, xbmc.LOGDEBUG) 
    sysdest = urllib.quote_plus(dest)
    #xbmc.log('******** download*** using sysdest %s' % sysdest, xbmc.LOGWARNING)

    script = inspect.getfile(inspect.currentframe())
    cmd = 'RunScript(%s, %s, %s, %s, %s, %s, %s)' % (script, sysurl, sysdest, systitle, duration, sysimage, sysheaders)

    xbmc.log('################################################################', xbmc.LOGDEBUG)
    xbmc.log('######################## DOWNLOADING %s ########################' % dest, xbmc.LOGDEBUG)
    xbmc.log('######################## DOWNLOADING script %s ########################' % script, xbmc.LOGDEBUG)
    xbmc.log('######################## DOWNLOADING sysurl %s ########################' % sysurl, xbmc.LOGDEBUG)
    #xbmc.log('######################## DOWNLOADING sysdest %s ########################' % sysdest, xbmc.LOGWARNING)
    #xbmc.log('######################## DOWNLOADING systitle %s ########################' % systitle, xbmc.LOGWARNING)
    xbmc.log('################################################################' , xbmc.LOGDEBUG)    

    #xbmc.sleep(20000)
    xbmc.executebuiltin(cmd)
    xbmc.log('EXIT <- plexpvrdownloader.py download()', xbmc.LOGNOTICE)


def getResponse(url, headers, size):
    try:
        if size > 0:
            size = int(size)
            headers['Range'] = 'bytes=%d-' % size

        req = urllib2.Request(url, headers=headers)

        resp = urllib2.urlopen(req, timeout=30)
        return resp
    except:
        return None


def done(title, dest, downloaded):
    xbmc.log('ENTRY -> plexpvrdownloader.py done()', xbmc.LOGNOTICE)
    xbmc.log('    title: %s' % (title), xbmc.LOGNOTICE)
    xbmc.log('    dest: %s' % (dest), xbmc.LOGNOTICE)
    xbmc.log('    downloaded: %s' % (downloaded), xbmc.LOGNOTICE)

    playing = xbmc.Player().isPlaying()

    text = xbmcgui.Window(10000).getProperty('GEN-DOWNLOADED')

    if len(text) > 0:
        text += '[CR]'

    if downloaded:
        text += '%s : %s' % (dest.rsplit(os.sep)[-1], '[COLOR forestgreen]Download succeeded[/COLOR]')
    else:
        text += '%s : %s' % (dest.rsplit(os.sep)[-1], '[COLOR red]Download failed[/COLOR]')

    xbmcgui.Window(10000).setProperty('GEN-DOWNLOADED', text)

    if (not downloaded) or (not playing): 
        #xbmcgui.Dialog().ok(title, text)
        xbmc.log('   **** DONE DOWNLOADING **** file: %s' % text, xbmc.LOGNOTICE) 
        xbmcgui.Window(10000).clearProperty('GEN-DOWNLOADED')


def doDownload(url, dest, title, duration, image, headers):
    xbmc.log('ENTRY -> plexpvrdownloader.py doDownload()', xbmc.LOGNOTICE)
    xbmc.log('    url: %s' % (url), xbmc.LOGNOTICE)
    xbmc.log('    dest: %s' % (dest), xbmc.LOGNOTICE)
    xbmc.log('    title: %s' % (title), xbmc.LOGNOTICE)
    xbmc.log('    duration: %s' % (duration), xbmc.LOGNOTICE)
    xbmc.log('    image: %s' % (image), xbmc.LOGNOTICE)
    xbmc.log('    headers: %s' % (headers), xbmc.LOGNOTICE)

    headers = json.loads(urllib.unquote_plus(headers))

    url = urllib.unquote_plus(url)

    title = urllib.unquote_plus(title)

    duration = urllib.unquote_plus(duration)

    image = urllib.unquote_plus(image)

    dest = urllib.unquote_plus(dest)

    file = dest.rsplit(os.sep, 1)[-1]

    resp = getResponse(url, headers, 0)

    xbmc.log('***doDownload*** using url %s' % url, xbmc.LOGDEBUG)
    xbmc.log('***doDownload*** using dest %s' % dest, xbmc.LOGDEBUG)
    xbmc.log('***doDownload*** using title %s' % title, xbmc.LOGDEBUG)
    xbmc.log('***doDownload*** using image %s' % image, xbmc.LOGDEBUG)
    xbmc.log('***doDownload*** using duration %s' % duration, xbmc.LOGDEBUG)
    xbmc.log('***doDownload*** using headers %s' % headers, xbmc.LOGDEBUG)

    if not resp:
        xbmc.log('   **** ERROR **** Response from video source is null', xbmc.LOGERROR)
        return
        #xbmcgui.Dialog().ok(title, dest, 'Download failed', 'No response from server')

    try: content = int(resp.headers['Content-Length'])
    except: content = 0

    try:    resumable = 'bytes' in resp.headers['Accept-Ranges'].lower()
    except: resumable = False

    try:
        xbmc.log('Video Duration Before Changing to Integer: %s' % (duration), xbmc.LOGERROR)    
        totalseconds = int(duration)
        xbmc.log('Video Duration After Changing to Integer:: %s' % (totalseconds), xbmc.LOGERROR)
        totalminutes = totalseconds / 60
        xbmc.log('Video Duration After Changing to Integer:: %s' % (totalminutes), xbmc.LOGERROR)
        
    except: 
        totalminutes = 1

    xbmc.log('Video Duration in Minutes: %s' % (totalminutes), xbmc.LOGERROR)
    timethreashold = totalminutes * 1.5
    xbmc.log('Video Duration Threashold: %s' % (timethreashold), xbmc.LOGERROR)

    #print "Download Header"
    #print resp.headers
    if resumable:
        print "Download is resumable"

    if content < 1:
        return
        #xbmcgui.Dialog().ok(title, file, 'Unknown filesize', 'Unable to download')

    size = 1024 * 1024
    mb   = content / (1024 * 1024)

    if content < size:
        size = content

    total   = 0
    notify  = 0
    errors  = 0
    count   = 0
    resume  = 0
    sleep   = 0

    #Rules for file sizes to ensure we are not downloading trailors or odd shows which are not right
    if mb < timethreashold:
        xbmc.log('   **** FILE SIZE IS TO SMALL **** size: %s' %mb, xbmc.LOGERROR)
        return
        
    #DSP Only Change here
    #if mb < 30:
    #    if xbmcgui.Dialog().yesno(title + ' - Confirm Download', file, 'Complete file is %dMB' % mb, 'Continue with download?', 'Confirm',  'Cancel') == 1:
    #        return

    print 'Download File Size : %dMB %s ' % (mb, dest)

    #f = open(dest, mode='wb')
    f = xbmcvfs.File(dest, 'w')

    chunk  = None
    chunks = []

    while True:
        downloaded = total
        for c in chunks:
            downloaded += len(c)
        percent = min(100 * downloaded / content, 100)
        if percent >= notify:
            xbmc.executebuiltin( "XBMC.Notification(%s,%s,%i,%s)" % ( title + ' - Download Progress - ' + str(percent)+'%', dest, 10000, image))

            print 'Download percent : %s %s %dMB downloaded : %sMB File Size : %sMB' % (str(percent)+'%', dest, mb, downloaded / 1000000, content / 1000000)

            #DSP Add the download file into a csv file
            xbmc.log('Downloading ... %s' % (dest), xbmc.LOGNOTICE)
            xbmc.log('Download file size %smb' % (mb), xbmc.LOGNOTICE)
            xbmc.log('Content : %s Download : %s' % (content,downloaded), xbmc.LOGNOTICE)
            xbmc.log('Download percent : %s %s %dMB downloaded : %sMB File Size : %sMB' % (str(percent)+'%', dest, mb, downloaded / 1000000, content / 1000000), xbmc.LOGDEBUG)

            #Threadsafe guard
            lock = threading.Lock()
            lock.acquire()
            try:
                shutil.copy2('kodi-downloader.csv','kodi-downloader.csv.temp')
                pvrFileTemp = open('kodi-downloader.csv.temp','rb')
                pvrFile = open('kodi-downloader.csv','wb')
                try:
                    reader = csv.reader(pvrFileTemp)
                    writer = csv.writer(pvrFile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)

                    for row in reader:
                        xbmc.log('ROW: %s' % row,xbmc.LOGNOTICE)
                        xbmc.log('dest: %s' % dest,xbmc.LOGNOTICE)
                        xbmc.log('file: %s' % file,xbmc.LOGNOTICE)

                        if str(row).find(str(file)) == -1:
                            xbmc.log('Did not find desination in kodi-downloader.csv: %s' % row, xbmc.LOGDEBUG)
                            writer.writerow(row)
                        else:
                            xbmc.log('Found Destination File in kodi-downloader.csv: %s == %s' % (row, dest), xbmc.LOGDEBUG)

                        xbmc.log('Checking kodi-downloader.csv: %s' % row)
                    
                    #Write the status to the file
                    writer.writerow([str(percent)+'%',dest,downloaded / 1000000,content / 1000000])

                finally:
                    pvrFileTemp.close()
                    pvrFile.close()

                #Delete the temp file
                if os.path.isfile('kodi-downloader.csv.temp'):
                    os.remove('kodi-downloader.csv.temp')
            finally:
                # Always called, even if exception is raised in try block
                lock.release()

            notify += 5

        chunk = None
        error = False

        try:        
            chunk  = resp.read(size)
            if not chunk:
                if percent < 99:
                    error = True
                else:
                    while len(chunks) > 0:
                        c = chunks.pop(0)
                        f.write(c)
                        del c

                    f.close()
                    print '%s download complete' % (dest)
                    return done(title, dest, True)

        except Exception, e:
            print str(e)
            error = True
            sleep = 10
            errno = 0

            if hasattr(e, 'errno'):
                errno = e.errno

            if errno == 10035: # 'A non-blocking socket operation could not be completed immediately'
                pass

            if errno == 10054: #'An existing connection was forcibly closed by the remote host'
                errors = 10 #force resume
                sleep  = 30

            if errno == 11001: # 'getaddrinfo failed'
                errors = 10 #force resume
                sleep  = 30

        if chunk:
            errors = 0
            chunks.append(chunk)
            if len(chunks) > 5:
                c = chunks.pop(0)
                f.write(c)
                total += len(c)
                del c

        if error:
            errors += 1
            count  += 1
            print '%d Error(s) whilst downloading %s' % (count, dest)
            xbmc.sleep(sleep*1000)

        if (resumable and errors > 0) or errors >= 10:
            if (not resumable and resume >= 50) or resume >= 500:
                #Give up!
                print '%s download canceled - too many error whilst downloading' % (dest)
                xbmc.log('EXIT <- plexpvrdownloader.py download()', xbmc.LOGNOTICE)
                return done(title, dest, False)

            resume += 1
            errors  = 0
            if resumable:
                chunks  = []
                #create new response
                print 'Download resumed (%d) %s' % (resume, dest)
                resp = getResponse(url, headers, total)
            else:
                #use existing response
                pass


if __name__ == '__main__':
    xbmc.log('ENTRY -> plexpvrdownloader.py __main__', xbmc.LOGNOTICE)

    if 'plexpvrdownloader.py' in sys.argv[0]:
        xbmc.log('    url: %s' % (sys.argv[1]), xbmc.LOGNOTICE)
        xbmc.log('    dest: %s' % (sys.argv[2]), xbmc.LOGNOTICE)
        xbmc.log('    title: %s' % (sys.argv[3]), xbmc.LOGNOTICE)
        xbmc.log('    duration: %s' % (sys.argv[4]), xbmc.LOGNOTICE)
        xbmc.log('    image: %s' % (sys.argv[5]), xbmc.LOGNOTICE)
        xbmc.log('    headers: %s' % (sys.argv[6]), xbmc.LOGNOTICE)
        doDownload(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5], sys.argv[6])

