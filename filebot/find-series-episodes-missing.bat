
SET TV_SHOW_PATH="C:\Entertainment\TVShows"
SET FILE_BOT_EXE=C:\gitbash\opt\filebot\filebot.exe
SET GROOVY_SCRIPT=C:\gitbash\opt\eclipse\workspace\io.askcloud.pvr\filebot\find-series-episodes-missing.groovy
SET SERIES_MISSING_EPISODES_OUTPUT=C:\gitbash\opt\eclipse\workspace\io.askcloud.pvr\queue\series-episodes-missing.txt
SET SERIES_MISSING_EPISODES_OUTPUT_FILTER=C:\gitbash\opt\eclipse\workspace\io.askcloud.pvr\filebot\find-series-episodes-missing-excludes.txt


echo Calling %FILE_BOT_EXE%  -script %GROOVY_SCRIPT% %TV_SHOW_PATH% --output %SERIES_MISSING_EPISODES_OUTPUT% --def excludeList=%SERIES_MISSING_EPISODES_OUTPUT_FILTER% --log all
call %FILE_BOT_EXE%  -script %GROOVY_SCRIPT% %TV_SHOW_PATH% --output %SERIES_MISSING_EPISODES_OUTPUT% --def excludeList=%SERIES_MISSING_EPISODES_OUTPUT_FILTER% --log all