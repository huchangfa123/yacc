@echo YACC Test
@echo Author: huchangfa


@echo off
set path=%path%:C:\Program Files\Java\jre1.8.0_60\bin
set test=%cd%\testcases
@echo Testing Start

for /f "delims=" %%a in ('"dir %test% /B"') do (
    echo ***** Testing %%~a
    java -jar F:\java-cx\Yacc\bin\Yacc.jar F:\java-cx\Yacc\testcases\%%~a
    echo ***** Testing End
)
pause