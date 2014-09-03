;#NoTrayIcon
#SingleInstance force
TempPath = %A_MyDocuments%\QuickSave\%A_ComputerName%
TempName := "Temp"
FileCreateDir, %TempPath%

while true
{
	WinWaitActive,Scan to Folder,,2
	if (ErrorLevel = 1)
	{
		Process, Exist, javaw.exe
		Running = %ErrorLevel%
		if (Running = 0)
		{
			ExitApp
		}
	}
	else
	{
		BlockInput, On ;Blocks user input untill turned off
		FileDelete, %TempPath%\sntemp.pdf
		sleep, 500
		WinWaitActive,Scan to Folder
		ControlFocus,Edit2,Scan to Folder
		Sleep 100
		Send ^a
		Sleep 50
		Send sntemp
		ControlFocus,Edit3,Scan to Folder
		Sleep 100
		Send ^a
		Sleep 50
		Send %TempPath%
		Sleep 100
		ControlSend,Button10,{Enter},Scan to Folder
		Sleep 2300
		
		IfWinExist,Copying 1 item
		{
			WinWaitNotActive,Copying 1 item
			{
			}
		}
		Sleep 70
		Send {Enter}
		Sleep 500
		BlockInput, Off ;Unblocks user input
		FileMove, %TempPath%\sntemp.pdf, %TempPath%\%TempName%.pdf
	}
}