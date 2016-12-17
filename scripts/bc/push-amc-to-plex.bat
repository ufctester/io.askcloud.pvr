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

SET BC_BACKUP_SCRIPT=C:\gitbash\opt\beyondcompare\scripts\backup.bc
SET BC_MIRROR_SCRIPT=C:\gitbash\opt\beyondcompare\scripts\mirror.bc
SET BC_REPORT_SCRIPT=C:\gitbash\opt\beyondcompare\scripts\report.bc

:: ===========================================================
:: Push Movies to Plex
:: ===========================================================
call :ECHO_FILE "Pushing Movies from AMC to Plex"
call :ECHO_FILE "Pushing changes from C:\gitbash\opt\kodi\amccompleted\Movies to here C:\Entertainment\Movies"
call %BC% @"%BC_BACKUP_SCRIPT%" "C:\gitbash\opt\kodi\amccompleted\Movies" "C:\Entertainment\Movies" %BC_SCRIPT_LOG%


:: ===========================================================
:: Push TVShows to Plex
:: ===========================================================
call :ECHO_FILE "Pushing TVShows from AMC to Plex"
call :ECHO_FILE "Pushing changes from C:\gitbash\opt\kodi\amccompleted\TVShows to here C:\Entertainment\TVShows"
call %BC% @"%BC_BACKUP_SCRIPT%" "C:\gitbash\opt\kodi\amccompleted\TVShows" "C:\Entertainment\TVShows" %BC_SCRIPT_LOG%

goto EOF


:: a function to write to a log file and write to stdout
:ECHO_FILE
ECHO %* >> "%BC_SCRIPT_LOG%"
ECHO %BC_SCRIPT_LOG% %*
EXIT /B 0

:EOF
