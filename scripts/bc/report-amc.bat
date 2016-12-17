@echo off


:: ===========================================================
:: Create DataTime stamp for log file
:: ===========================================================
SET DATESTAMP=%DATE:~10,4%%DATE:~4,2%%DATE:~7,2%
SET HOUR=%time:~0,2%
if "%HOUR:~0,1%" == " " set HOUR=0%HOUR:~1,1%
SET MIN=%time:~3,2%
if "%MIN:~0,1%" == " " set MIN=0%MIN:~1,1%
rem set TIMESTAMP=%TIME:~0,2%%TIME:~3,2%
SET TIMESTAMP=%TIME:~0,2%%TIME:~3,2%
SET DATEANDTIME=%DATESTAMP%_%HOUR%%MIN%

:: ===========================================================
:: Environment Variables
:: ===========================================================
SET BC=C:\gitbash\opt\beyondcompare\BCompare.exe
SET ZIP=C:\gitbash\opt\7zip\7z.exe
SET BC_SCRIPT_LOG=C:\gitbash\opt\beyondcompare\scripts\logs\amc-%DATEANDTIME%.log

SET BC_MOVIE_REPORT=C:\gitbash\opt\eclipse\workspace\io.askcloud.pvr\logs\report-amc-movie.xml
SET BC_TVSHOWS_REPORT=C:\gitbash\opt\eclipse\workspace\io.askcloud.pvr\logs\report-amc-tv.xml

SET BC_BACKUP_SCRIPT=C:\gitbash\opt\beyondcompare\scripts\backup.bc
SET BC_MIRROR_SCRIPT=C:\gitbash\opt\beyondcompare\scripts\mirror.bc
SET BC_REPORT_SCRIPT=C:\gitbash\opt\beyondcompare\scripts\report.bc

:: ===========================================================
:: Generate a Movie Approval Report to push to plex
:: ===========================================================
call :ECHO_FILE "Generating Report for Approval to push to Plex"
call %BC% @"%BC_REPORT_SCRIPT%" "C:\gitbash\opt\kodi\amccompleted\Movies" "C:\Entertainment\Movies" %BC_SCRIPT_LOG% %BC_MOVIE_REPORT%

:: ===========================================================
:: Generate a TVShows Approval Report to push to plex
:: ===========================================================
call :ECHO_FILE "Generating Report for Approval to push to Plex"
call %BC% @"%BC_REPORT_SCRIPT%" "C:\gitbash\opt\kodi\amccompleted\TVShows" "C:\Entertainment\TVShows" %BC_SCRIPT_LOG% %BC_TVSHOWS_REPORT%


goto EOF


:: a function to write to a log file and write to stdout
:ECHO_FILE
ECHO %* >> "%BC_SCRIPT_LOG%"
ECHO %BC_SCRIPT_LOG% %*
EXIT /B 0

:EOF
