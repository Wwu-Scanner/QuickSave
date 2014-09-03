/*
A small class with a few methods used to pause the program
*/

package QuickSave;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TempFileHandler {
    //Renames temp file to Temp.pdf
    void TempNameFix (String Name, String Directory)
    {
        WaitFileExist(Directory + "\\" + Name + "*.pdf");
        File TempFile = new File(Directory + "\\" + Name + "*.pdf");
        TempFile.renameTo(new File(Name + ".pdf"));
    }
    
    //pauses the program untill a specified file exists
    void WaitFileExist(String Path)
    {
        File TempFile = new File(Path);
        while(!TempFile.exists())
        {
            Wait(1000);
        }
    }
    
    //pauses the program untill a specified file exists
    void WaitFileNotExist(String Path)
    {
        File TempFile = new File(Path);
        while(TempFile.exists())
        {
            Wait(1000);
        }
    }
    
    //pauses the program for a specified number of miliseconds
    void Wait(int miliseconds)
    {
        try 
        {
            Thread.sleep(miliseconds);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
