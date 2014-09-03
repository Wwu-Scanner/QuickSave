/*
This class saves and applies the program settings from a txt file
*/

package QuickSave;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Settings
{
    void RestoreSettings()
    {
        try
        {
            //this reads the settings file if it exists
            File settingsFile = new File(Main.TempDirectory + "\\settings.txt");
            if (settingsFile.exists())
            {   
                //this section sets the settings to that of the previous run of the program
                String[] Settings = Main.LoadArray(Main.TempDirectory + "\\settings.txt");
                if(Settings.length >= 50)
                {
                    MainGui.SelectedDocumentType = Integer.parseInt(Settings[0]);
                    MainGui.EditPath = Settings[1];
                    MainGui.CSDirectory = Settings[2];
                    MainGui.CheckColorNumber[0] = Integer.parseInt(Settings[3]);
                    MainGui.CheckColorNumber[1] = Integer.parseInt(Settings[4]);
                    MainGui.CheckColorNumber[2] = Integer.parseInt(Settings[5]);
                    MainGui.CheckColorNumber[3] = Integer.parseInt(Settings[6]);
                    MainGui.CheckColorName[0] = Integer.parseInt(Settings[7]);
                    MainGui.CheckColorName[1] = Integer.parseInt(Settings[8]);
                    MainGui.CheckColorName[2] = Integer.parseInt(Settings[9]);
                    MainGui.CheckColorName[3] = Integer.parseInt(Settings[10]);
                    MainGui.CheckColorDate[0] = Integer.parseInt(Settings[11]);
                    MainGui.CheckColorDate[1] = Integer.parseInt(Settings[12]);
                    MainGui.CheckColorDate[2] = Integer.parseInt(Settings[13]);
                    MainGui.CheckColorDate[3] = Integer.parseInt(Settings[14]);
                    MainGui.CheckColorAmount[0] = Integer.parseInt(Settings[15]);
                    MainGui.CheckColorAmount[1] = Integer.parseInt(Settings[16]);
                    MainGui.CheckColorAmount[2] = Integer.parseInt(Settings[17]);
                    MainGui.CheckColorAmount[3] = Integer.parseInt(Settings[18]);
                    MainGui.CheckColorZero[0] = Integer.parseInt(Settings[19]);
                    MainGui.CheckColorZero[1] = Integer.parseInt(Settings[20]);
                    MainGui.CheckColorZero[2] = Integer.parseInt(Settings[21]);
                    MainGui.CheckColorZero[3] = Integer.parseInt(Settings[22]);
                    MainGui.CheckCoordsNumber[0] = Double.parseDouble(Settings[23]);
                    MainGui.CheckCoordsNumber[1] = Double.parseDouble(Settings[24]);
                    MainGui.CheckCoordsNumber[2] = Double.parseDouble(Settings[25]);
                    MainGui.CheckCoordsNumber[3] = Double.parseDouble(Settings[26]);
                    MainGui.CheckCoordsName[0] = Double.parseDouble(Settings[27]);
                    MainGui.CheckCoordsName[1] = Double.parseDouble(Settings[28]);
                    MainGui.CheckCoordsName[2] = Double.parseDouble(Settings[29]);
                    MainGui.CheckCoordsName[3] = Double.parseDouble(Settings[30]);
                    MainGui.CheckCoordsDate[0] = Double.parseDouble(Settings[31]);
                    MainGui.CheckCoordsDate[1] = Double.parseDouble(Settings[32]);
                    MainGui.CheckCoordsDate[2] = Double.parseDouble(Settings[33]);
                    MainGui.CheckCoordsDate[3] = Double.parseDouble(Settings[34]);
                    MainGui.CheckCoordsAmount[0] = Double.parseDouble(Settings[35]);
                    MainGui.CheckCoordsAmount[1] = Double.parseDouble(Settings[36]);
                    MainGui.CheckCoordsAmount[2] = Double.parseDouble(Settings[37]);
                    MainGui.CheckCoordsAmount[3] = Double.parseDouble(Settings[38]);
                    MainGui.CheckCoordsZero[0] = Double.parseDouble(Settings[39]);
                    MainGui.CheckCoordsZero[1] = Double.parseDouble(Settings[40]);
                    MainGui.CheckCoordsZero[2] = Double.parseDouble(Settings[41]);
                    MainGui.CheckCoordsZero[3] = Double.parseDouble(Settings[42]);
                    Main.UseScanSnapAutomation = Boolean.parseBoolean(Settings[43]);
                    MainGui.emailAfterSaving = Boolean.parseBoolean(Settings[44]);
                    Main.maxAttachmentSize = Long.parseLong(Settings[45]);
                    MainGui.jCheckBoxEmail.setSelected(MainGui.emailAfterSaving);
                    if ("true".equals(Settings[46]))
                    {
                        MainGui.jCheckBoxMenuItemAppend.setSelected(true);
                    }
                    MainGui.showFilmStrip = Boolean.parseBoolean(Settings[47]);
                    MainGui.autoHideToTray = Boolean.parseBoolean(Settings[48]);
                    MainGui.hideOnExit = Boolean.parseBoolean(Settings[49]);
                    MainGui.jCheckBoxMenuCheckupdate.setSelected(Boolean.parseBoolean(Settings[50]));
                }
            }
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException ex)
        {
            JOptionPane.showMessageDialog(null, "Some settings may not have been restored properly", "Error restoring settings", 1, null);
        }
    }
    
    void SaveSettings()
    {
        // write program settings to a text file
        try
        {
            File settingsFile = new File(Main.TempDirectory + "\\settings.txt");
            settingsFile.delete();
            try (BufferedWriter out = new BufferedWriter(new FileWriter(Main.TempDirectory + "\\settings.txt")))
            {
                out.write(MainGui.SelectedDocumentType + "\n");
                out.write(MainGui.EditPath.substring(0,MainGui.EditPath.length() - MainGui.jLabelEditName.getText().length()) + "\n");
                out.write(MainGui.CSDirectory + "\n");
                out.write(MainGui.CheckColorNumber[0] + "\n");
                out.write(MainGui.CheckColorNumber[1] + "\n");
                out.write(MainGui.CheckColorNumber[2] + "\n");
                out.write(MainGui.CheckColorNumber[3] + "\n");
                out.write(MainGui.CheckColorName[0] + "\n");
                out.write(MainGui.CheckColorName[1] + "\n");
                out.write(MainGui.CheckColorName[2] + "\n");
                out.write(MainGui.CheckColorName[3] + "\n");
                out.write(MainGui.CheckColorDate[0] + "\n");
                out.write(MainGui.CheckColorDate[1] + "\n");
                out.write(MainGui.CheckColorDate[2] + "\n");
                out.write(MainGui.CheckColorDate[3] + "\n");
                out.write(MainGui.CheckColorAmount[0] + "\n");
                out.write(MainGui.CheckColorAmount[1] + "\n");
                out.write(MainGui.CheckColorAmount[2] + "\n");
                out.write(MainGui.CheckColorAmount[3] + "\n");
                out.write(MainGui.CheckColorZero[0] + "\n");
                out.write(MainGui.CheckColorZero[1] + "\n");
                out.write(MainGui.CheckColorZero[2] + "\n");
                out.write(MainGui.CheckColorZero[3] + "\n");
                out.write(MainGui.CheckCoordsNumber[0] + "\n");
                out.write(MainGui.CheckCoordsNumber[1] + "\n");
                out.write(MainGui.CheckCoordsNumber[2] + "\n");
                out.write(MainGui.CheckCoordsNumber[3] + "\n");
                out.write(MainGui.CheckCoordsName[0] + "\n");
                out.write(MainGui.CheckCoordsName[1] + "\n");
                out.write(MainGui.CheckCoordsName[2] + "\n");
                out.write(MainGui.CheckCoordsName[3] + "\n");
                out.write(MainGui.CheckCoordsDate[0] + "\n");
                out.write(MainGui.CheckCoordsDate[1] + "\n");
                out.write(MainGui.CheckCoordsDate[2] + "\n");
                out.write(MainGui.CheckCoordsDate[3] + "\n");
                out.write(MainGui.CheckCoordsAmount[0] + "\n");
                out.write(MainGui.CheckCoordsAmount[1] + "\n");
                out.write(MainGui.CheckCoordsAmount[2] + "\n");
                out.write(MainGui.CheckCoordsAmount[3] + "\n");
                out.write(MainGui.CheckCoordsZero[0] + "\n");
                out.write(MainGui.CheckCoordsZero[1] + "\n");
                out.write(MainGui.CheckCoordsZero[2] + "\n");
                out.write(MainGui.CheckCoordsZero[3] + "\n");
                out.write(Boolean.toString(Main.UseScanSnapAutomation) + "\n");
                out.write(Boolean.toString(MainGui.emailAfterSaving) + "\n");
                out.write(Main.maxAttachmentSize + "\n");
                out.write(Boolean.toString(MainGui.jCheckBoxMenuItemAppend.getState()) + "\n");
                out.write(Boolean.toString(MainGui.showFilmStrip) + "\n");
                out.write(Boolean.toString(MainGui.autoHideToTray) + "\n");
                out.write(Boolean.toString(MainGui.hideOnExit) + "\n");
                out.write(Boolean.toString(MainGui.jCheckBoxMenuCheckupdate.getState()) + "\n");
            }
        }
        catch (IOException e)
        {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
