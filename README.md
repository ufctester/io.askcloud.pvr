#io.askcloud.pvr
http://www.opensubtitles.org/addons/show_my_ip.php

TODO

Finish handling Movies

Consolidate python script even more if possible

Each Exodus Downloader should be a thread and there needs to be a thread manager to manage the pool doing downloads.

Each downloader is responsible for determining when it is complete and can go back into the available queue

The Thread manager pools (sleep) the available thread pool and starts the next request (which is stored in a file).

Error Conditions
Threads need to have a reasonable timeout when it will assume it fails (and need to determine why)?

Nightly Filebot
 - Needs to stop putting requests in the queue and wait until all threads are availablle and then process the files.  
 - Needs to be scheduled and also smart to do this on the fly when it can

#============================================================

  - UI

  - Request (Movies,TVShows)

  - Personal Request Status

  - PVR Queue

  - PVR Whats coming up


