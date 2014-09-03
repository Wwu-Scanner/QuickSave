/*
This class is used to get the names of all the files in a specified folder.
It also determines the size of the files.
*/

package QuickSave;

import java.io.File;
import java.util.Arrays;

public class FileHandler {
    
    //Returns an array containing the names of all of the files in a given
    //folder path
    String[] listFiles(String path, String ext)
    {
        String file;
        File folder = new File(path);
        File[] files = folder.listFiles();
        String[] temp = new String[files.length];
        
        int i = 0;
        int j = 0;
        while (i < files.length)
        {
            if (files[i].isFile())
            {
                file = files[i].getName();
                if (ext == null)
                {
                    temp[j] = file;
                    j++;
                }
                else
                {
                    if (file.endsWith("." + ext.toLowerCase()) || file.endsWith("." + ext.toUpperCase()))
                    {
                        temp[j] = file;
                        j++;
                    }
                }
            }
            i++;
        }
        String[] results = Arrays.copyOfRange(temp, 0, j);
        return results;
    }
    String[] listFiles(String path)
    {
        return listFiles(path, null);
    }
    
    long getFileSize(File file)
    {
        long length = 0;
        if(file.exists())
        {
            length = file.length()/1048576;
        }
        return length;
    }
    
    long getFileSize(File[] files)
    {
        long length = 0;
        for (File file : files) {
            length = length + getFileSize(file);
        }
        return length;
    }
    
    long getFileSize(String path)
    {
        long length = 0;
        File file = new File(path);
        if(file.exists())
        {
            length = file.length()/1048576;
        }
        return length;
    }
    
    long getFileSize(String[] paths)
    {
        long length = 0;
        for (String path : paths) {
            length = length + getFileSize(path);
        }
        return length;
    }
    
    boolean fileExists(String path)
    {
        File file = new File(path);
        return file.exists();
    }
}
