# Note: Before running the install script
#
# 1) make sure that an actual JRE is located in "C:\Program Files (x86)\Java\jdk1.7.0_45\jre*"
#
# 2) the Phon distribution must be unziped in ..\NbPhon\dist\nbphon\nbphon\*
#
# 3) the derby files must be in "C:\Program Files (x86)\Java\jdk1.7.0_45\db"
#
# 4) the example database must be in "..\phonDb\dataExample"
#
# 5) the empty database must be in "..\phonDb\dataEmpty"
#
#
# Included files
!include Sections.nsh
!include logiclib.nsh
!include "MUI.nsh"
!include "StringReplace.nsh"

Name "Phon"


# General Symbol Definitions
!define REGKEY "SOFTWARE\$(^Name)"
!define VERSION 0.0.1
!define COMPANY "free-creations"
!define URL www.free-creations.de



RequestExecutionLevel user

# Reserved Files
ReserveFile "${NSISDIR}\Plugins\x86-unicode\StartMenu.dll"

# Variables
Var StartMenuGroup

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_INSTFILES

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES


# Installer languages
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "German"
!insertmacro MUI_LANGUAGE "French"

#LoadLanguageFile "${NSISDIR}\Contrib\Language files\English.nlf"
#LoadLanguageFile "${NSISDIR}\Contrib\Language files\German.nlf"
#LoadLanguageFile "${NSISDIR}\Contrib\Language files\French.nlf"

# Installer attributes
OutFile dist\install-Phon.exe
InstallDir "$LOCALAPPDATA\Phon-${VERSION}"
CRCCheck on
XPStyle on
Icon "${NSISDIR}\Contrib\Graphics\Icons\orange-install-nsis.ico"
ShowInstDetails show
AutoCloseWindow false
LicenseData ..\LICENSE-2.0.txt
VIProductVersion 0.0.0.0
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductName Phon
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyName "${COMPANY}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyWebsite "${URL}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileDescription ""
VIAddVersionKey /LANG=${LANG_ENGLISH} LegalCopyright ""
InstallDirRegKey HKCU "${REGKEY}" Path
UninstallIcon "${NSISDIR}\Contrib\Graphics\Icons\orange-uninstall-nsis.ico"
ShowUninstDetails show

# ------------------------------------------------------------------------
# Installer sections
# ------------------------------------------------------------------------

Section -Main SEC0000
    SetShellVarContext current
    
    # The example Media
    
    # The Java Runtime
    SetOverwrite on
    SetOutPath $INSTDIR\jre7
    File /r "C:\Program Files (x86)\Java\jdk1.7.0_45\jre\*"


    # The Phon application (unzip before running this)
    SetOutPath $INSTDIR
    File /r ..\NbPhon\dist\nbphon\nbphon\*
    
    SetOutPath $INSTDIR\bin
    File ..\Install\specialFiles\nbphon.exe
    WriteRegStr HKCU "${REGKEY}\Components" Main 1
    SetOutPath $INSTDIR\etc
    File ..\Install\specialFiles\nbphon.conf    
      
SectionEnd

# ------------------------------------------------------------------------
# The Database Server section installs the Derby Database libraries
# and the binary database files.

SectionGroup /e "Database Server" GRP_SERVER
Section /o "Empty Database" SEC_ServerEmpty

    # The Derby libraries 
    SetOverwrite on
    SetOutPath $INSTDIR\derby
    File /r "C:\Program Files (x86)\Java\jdk1.7.0_45\db"

    # The Database Data-files
    SetOverwrite on
    SetOutPath $INSTDIR\phonDb
    File /r "..\phonDb\dataEmpty"

    # configure the bat file that starts the database
    SetOutPath $INSTDIR\phonDb\bin
    FileOpen $0 "startServer.bat" w
    ;-----------------------------------------------------------
    fileWrite $0 'echo off $\r$\n'
    fileWrite $0 'prompt $$G $\r$\n'
    fileWrite $0 'echo *************************************************$\r$\n'
    fileWrite $0 'echo **** Produktions Datenbank                   ****$\r$\n'
    fileWrite $0 'echo *************************************************$\r$\n'
    fileWrite $0 'set DATA_LOC="$INSTDIR\phonDb\dataEmpty\" $\r$\n'
    fileWrite $0 'set _JAVACMD="$INSTDIR\jre7\bin\java.exe" $\r$\n'
    fileWrite $0 'set _JAR="$INSTDIR\derby\db\lib\derbyrun.jar" $\r$\n'
    fileWrite $0 'cd %DATA_LOC% $\r$\n'
    fileWrite $0 'echo *************************************************$\r$\n'
    fileWrite $0 'echo **** Schliessen dieses Fensters beendet alle ****$\r$\n'
    fileWrite $0 'echo **** Datenbankverbindungen                   ****$\r$\n'
    fileWrite $0 'echo *************************************************$\r$\n'
    fileWrite $0 '%_JAVACMD% -jar %_JAR% server start$\r$\n'
    ;-----------------------------------------------------------
    # close the file
    fileClose $0

    CreateShortcut "$SMPROGRAMS\Phon\PhonServer.lnk" $INSTDIR\phonDb\bin\startServer.bat
    CreateShortcut "$DESKTOP\PhonServer.lnk"  $INSTDIR\phonDb\bin\startServer.bat
SectionEnd

Section /o "Example Database" SEC_ServerExample

    # The Derby libraries 
    SetOverwrite on
    SetOutPath $INSTDIR\derby
    File /r "C:\Program Files (x86)\Java\jdk1.7.0_45\db"

    # The Database Data-files
    SetOverwrite on
    SetOutPath $INSTDIR\phonDb
    File /r "..\phonDb\dataExample"

    # configure the bat file that starts the database
    SetOutPath $INSTDIR\phonDb\bin
    FileOpen $0 "startServer.bat" w
    ;-----------------------------------------------------------
    fileWrite $0 'echo off $\r$\n'
    fileWrite $0 'prompt $$G $\r$\n'
    fileWrite $0 'echo *************************************************$\r$\n'
    fileWrite $0 'echo **** Beispiel Datenbank                      ****$\r$\n'
    fileWrite $0 'echo *************************************************$\r$\n'
    fileWrite $0 'set DATA_LOC="$INSTDIR\phonDb\dataExample\" $\r$\n'
    fileWrite $0 'set _JAVACMD="$INSTDIR\jre7\bin\java.exe" $\r$\n'
    fileWrite $0 'set _JAR="$INSTDIR\derby\db\lib\derbyrun.jar" $\r$\n'
    fileWrite $0 'cd %DATA_LOC% $\r$\n'
    fileWrite $0 'echo *************************************************$\r$\n'
    fileWrite $0 'echo **** Schliessen dieses Fensters beendet alle ****$\r$\n'
    fileWrite $0 'echo **** Datenbankverbindungen                   ****$\r$\n'
    fileWrite $0 'echo *************************************************$\r$\n'
    fileWrite $0 '%_JAVACMD% -jar %_JAR% server start$\r$\n'
    ;-----------------------------------------------------------
    # close the file
    fileClose $0

    CreateShortcut "$SMPROGRAMS\Phon\PhonServer.lnk" $INSTDIR\phonDb\bin\startServer.bat
    CreateShortcut "$DESKTOP\PhonServer.lnk"  $INSTDIR\phonDb\bin\startServer.bat
SectionEnd
SectionGroupEnd

Section -post SEC0001
    SetShellVarContext current
    WriteRegStr HKCU "${REGKEY}" Path $INSTDIR
    WriteRegStr HKCU "${REGKEY}" StartMenuGroup $StartMenuGroup
    SetOutPath $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe
    SetOutPath $SMPROGRAMS\$StartMenuGroup
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\Phon.lnk" $INSTDIR\bin\nbphon.exe
    CreateShortcut "$DESKTOP\Phon.lnk" $INSTDIR\bin\nbphon.exe
    
    WriteRegStr HKCU "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKCU "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKCU "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKCU "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${URL}"
    WriteRegStr HKCU "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKCU "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    WriteRegDWORD HKCU "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKCU "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKCU "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section  -un.Main UNSEC0000
    Delete /REBOOTOK $INSTDIR\bin\nbphon.exe
    RmDir /r /REBOOTOK $INSTDIR
    DeleteRegValue HKCU "${REGKEY}\Components" Main
SectionEnd

Section /o "-un.Database Server 1" UNSEC_ServerEmpty
    RmDir /r /REBOOTOK $INSTDIR/phonDb
    RmDir /r /REBOOTOK $INSTDIR
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\PhonServer.lnk" 
    Delete /REBOOTOK "$DESKTOP\PhonServer.lnk"  

SectionEnd

Section /o "-un.Database Server 1" UNSEC_ServerExample
    RmDir /r /REBOOTOK $INSTDIR/phonDb
    RmDir /r /REBOOTOK $INSTDIR
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\PhonServer.lnk" 
    Delete /REBOOTOK "$DESKTOP\PhonServer.lnk"  

SectionEnd

Section -un.post UNSEC0001
    DeleteRegKey HKCU "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\Phon.lnk"
    Delete /REBOOTOK "$DESKTOP\Phon.lnk" 
    Delete /REBOOTOK $INSTDIR\uninstall.exe
    DeleteRegValue HKCU "${REGKEY}" StartMenuGroup
    DeleteRegValue HKCU "${REGKEY}" Path
    DeleteRegKey /IfEmpty HKCU "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKCU "${REGKEY}"
    RmDir /REBOOTOK $SMPROGRAMS\$StartMenuGroup
    RmDir /REBOOTOK $INSTDIR
SectionEnd

# ------------------------------------------------------------------------
# Installer functions
# ------------------------------------------------------------------------

Function StartMenuGroupSelect
    Push $R1
    StartMenu::Select /autoadd /text "$(StartMenuPageText)" /lastused $StartMenuGroup Phon
    Pop $R1
    StrCmp $R1 success success
    StrCmp $R1 cancel done
    MessageBox MB_OK $R1
    Goto done
success:
    Pop $StartMenuGroup
done:
    Pop $R1
FunctionEnd

#--- Installer init functions
Function .onInit
    InitPluginsDir
    # Auto- uninstall old before installing new
    # ..search for registry key
    ReadRegStr $R0 HKCU \
                   "Software\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" \
                   "UninstallString"
    StrCmp $R0 "" done
    
    #.. ask user if OK to uninstall
   MessageBox MB_OKCANCEL|MB_ICONEXCLAMATION \
              $(allreadyInstalled) \
               IDOK uninst
               Abort

    ;Run the uninstaller
uninst:
    ClearErrors
    ExecWait '$R0 _?=$INSTDIR' ;Do not copy the uninstaller to a temp file
 
    IfErrors no_remove_uninstaller done
      ;You can either use Delete /REBOOTOK in the uninstaller or add some code
      ;here to remove the uninstaller. Use a registry key to check
      ;whether the user has chosen to uninstall. If you are using an uninstaller
      ;components page, make sure all sections are uninstalled.
   no_remove_uninstaller:
 
done:

FunctionEnd


# Uninstaller functions
Function un.onInit
    ReadRegStr $INSTDIR HKCU "${REGKEY}" Path
    ReadRegStr $StartMenuGroup HKCU "${REGKEY}" StartMenuGroup
    !insertmacro SELECT_UNSECTION Main ${UNSEC0000}
FunctionEnd
# ------------------------------------------------------------------------
# Installer Language Strings
# ------------------------------------------------------------------------
# TODO Update the Language Strings with the appropriate translations.

LangString allreadyInstalled ${LANG_ENGLISH} "$(^Name) is already installed. $\n$\nClick `OK` to remove the \
                                              previous version or `Cancel` to cancel this upgrade." 
LangString allreadyInstalled ${LANG_GERMAN} "$(^Name) ist schon installiert. $\n$\n `OK` entfernt \
                                              die alte Version und installert die neue Version; `Abbrechen` stoppt die Installation." 
LangString allreadyInstalled ${LANG_FRENCH} "$(^Name) est deja installé. $\n$\n `OK` désinstal \
                                              l'ancien version; `Cancel` arrete l'installation." 

LangString DESC_SectionServer ${LANG_ENGLISH} "The database server shall be installed only on one Compuer in the local network."
LangString DESC_SectionServer ${LANG_GERMAN} "Der Datenbank Server sollte nur auf einem Rechner im Netz installiert werden.."
LangString DESC_SectionServer ${LANG_FRENCH} "Le serveur de donnees doit etre installe sur un seul ordinateur dans le reseau."


!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${GRP_Server} $(DESC_SectionServer)
!insertmacro MUI_FUNCTION_DESCRIPTION_END



LangString StartMenuPageTitle ${LANG_ENGLISH} "Start Menu Folder"
LangString StartMenuPageTitle ${LANG_GERMAN} "Start Menu Folder"
LangString StartMenuPageTitle ${LANG_FRENCH} "Start Menu Folder"

LangString StartMenuPageText ${LANG_ENGLISH} "Select the Start Menu folder in which to create the program's shortcuts:"
LangString StartMenuPageText ${LANG_GERMAN} "Select the Start Menu folder in which to create the program's shortcuts:"
LangString StartMenuPageText ${LANG_FRENCH} "Select the Start Menu folder in which to create the program's shortcuts:"

LangString ^UninstallLink ${LANG_ENGLISH} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_GERMAN} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_FRENCH} "Uninstall $(^Name)"

;----------------------------------------------------------------------------
; Helper Functions to make the selection between empty database and example database mutually exclusive
; this code is copied from:
; http://stackoverflow.com/questions/20961153/disable-two-mutually-exclusive-sections-when-sectiongroup-unchecked

Function SaveState
!macro SaveSel id var
SectionGetFlags ${id} ${var}
IntOp ${var} ${var} & ${SF_SELECTED}
!macroend
SectionGetFlags ${GRP_Server} $R0
!insertmacro SaveSel ${SEC_ServerEmpty} $R1
!insertmacro SaveSel ${SEC_ServerExample} $R2

FunctionEnd

Function .onSelChange
!macro OneOfTwoItemsInAGroup gid gv i1 v1 i2 v2
#SectionGetFlags ${gid} $0
!insertmacro SaveSel ${i1} $1
!insertmacro SaveSel ${i2} $2
${If} $1 <> 0
${AndIf} $2 <> 0
    StrCpy $1 ${i1}
    ${IfThen} ${v1} = 0 ${|} StrCpy $1 ${i2} ${|}
    !insertmacro UnselectSection $1
    /*${If} ${gv} = $0 
        !insertmacro UnselectSection ${gid}
    ${EndIf}*/
${EndIf}
!macroend
!insertmacro OneOfTwoItemsInAGroup ${GRP_Server} $R0 ${SEC_ServerEmpty} $R1 ${SEC_ServerExample} $R2

Call SaveState
FunctionEnd
