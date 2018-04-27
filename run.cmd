@REM ----------------------------------------------------------------------------
@REM LineServer Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JVM home dir
@REM ----------------------------------------------------------------------------

@echo off

@setlocal

set ERROR_CODE=0

if not "%JAVA_HOME%" == "" goto OkJHome

echo Error: JAVA_HOME not found in your environment. >&2
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto init

echo Error: JAVA_HOME is set to an invalid directory. >&2
goto error

:init

SET JAVA_EXE="%JAVA_HOME%\bin\java.exe"

%JAVA_EXE% -jar target\line-server-0.0.1-SNAPSHOT.jar %*
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

exit /B %ERROR_CODE%
