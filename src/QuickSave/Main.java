/*
This is the main class that is run when the program initially starts.
It handles many tasks in the background
*/

package QuickSave;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ColorUIResource;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

public class Main
{
    public static int maxPages = 10000;
    public static int dpi = 200;
    public static long maxAttachmentSize = 25*1048576;
    public static String SourceName = "Temp.pdf";//Name of the temp file

    //Directory where the program was launched from
    public static String WorkingDirectory = System.getProperty("user.dir");
    public static String Help;
    
    //Local User File Paths i.e. "My Documents Folder"
    public static String DocumentsDirectory;
    public static String UserDirectory;
    public static String TempDirectory;
    public static String EmailTemplateBody;
    public static String EmailTemplateSubject;
    public static String DatabaseFolder;
    public static String ApprovalEmailTemplateBody;
    public static String ApprovalEmailTemplateSubject;
    
    //Document Directories
    public static String CheckDir = "R:\\Check Scans";
    public static String CreditCardDir = "R:\\Credit Card Statement Scans";
    public static String JournalEntriesDir = "R:\\Journal Entries (J)";
    public static String CashReceiptDir = "R:\\Cash Receipts\\00 Day Sheets";
    public static String HandwrittenChecksDir = "R:\\Check Scans";
    public static String GroupPostDir = "R:\\Accounts Receivable\\PeopleSoft Group Post";
    public static String ACHTransactionsDir = "R:\\ACH Transactions (nonstudent)";
    public static String ExpenseReportsDir = "R:\\Expense Report Scans";
    
    public static String DatabaseDirectory = null;
    
    //Save file directories
    public static String DestPath1 = null;
    public static String DestPath2 = null;
    public static String FileName1 = null;
    public static String FileName2 = null;
    
    public static boolean UseScanSnapAutomation = true;
    
    public static String[] CreditCardHolders;
    public static int[] CreditCardHolderIds;
    public static int[] CreditCardApproverIds;
    public static String[] CreditCardApprovers;
    public static String[] Departments;
    ChangedirectoriesGui directories = new ChangedirectoriesGui();
    
    //These variables are used to run the ScanSnapAutomation application
    public static Process p = null;
    public static Runtime rt;
    
    //This is the first method that runs when the program starts
    public static void main(String[] args) throws IOException
    {
        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();
        DocumentsDirectory =  "" + fw.getDefaultDirectory();
        UserDirectory = DocumentsDirectory + "\\QuickSave";
        TempDirectory = UserDirectory + "\\" + InetAddress.getLocalHost().getHostName();
        DatabaseFolder = UserDirectory + "\\QuickSaveDatabse";
        File dir = new File(UserDirectory);
        if(!dir.exists())
        {
            dir.mkdir();
        }
        EmailTemplateBody = UserDirectory + "\\EmailTemplateBody.html";
        EmailTemplateSubject = UserDirectory + "\\EmailTemplateSubject.txt";
        ApprovalEmailTemplateBody = UserDirectory + "\\ApprovalEmailTemplateBody.html";
        ApprovalEmailTemplateSubject = UserDirectory + "\\ApprovalEmailTemplateSubject.txt";
        Help = WorkingDirectory + "\\Help.html";
        
        //Reads a file to get the current directories
        File DirectoriesFile = new File(UserDirectory+"\\Directories.txt");
        if (DirectoriesFile.exists())
        {
            BufferedReader ReadDirectories;
            ReadDirectories = new BufferedReader(new FileReader(UserDirectory+"\\Directories.txt"));
            String[] Directories = LoadArray(UserDirectory+"\\Directories.txt");
            CheckDir = Directories[0];
            CreditCardDir = Directories[1];
            JournalEntriesDir = Directories[2];
            HandwrittenChecksDir = Directories[3];
            CashReceiptDir = Directories[4];
            ACHTransactionsDir = Directories[5];
            GroupPostDir = Directories[6];
            ReadDirectories.close();
        }
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //Change the orange theme color to green for the progress bars
        ColorUIResource colorResource = new ColorUIResource(Color.green.darker());
        UIManager.put("nimbusOrange",colorResource);
        
        //Initialize MainGui and the DatabaseHandler
        MainGui Gui = new MainGui();
        DatabaseHandler db = new DatabaseHandler();
        
        //Restore settings from txt file
        Settings settings = new Settings();
        settings.RestoreSettings();
        
        ShutDownHook hook = new ShutDownHook();
	Runtime runtime = Runtime.getRuntime(); 
	runtime.addShutdownHook(hook);
        
        //Check that database is functional
        db.openDatabase();
        if (db.checkCHTableExists() == false)
        {
            db.createCardHolderTable();
        }
        if (db.checkCATableExists() == false)
        {
            db.createCardApproverTable();
            db.openCardApproverTable();
            String[] vals = {Integer.toString(0),"0","Unspecified","","none","","","","01/01/0001","01/01/0001"};
            DatabaseHandler.ca.addRow(vals);
        }
        if (db.checkListTableExists() == false)
        {
            db.createListTable();
        }
        db.closeDatabase();
         
        loadCardHolders();
        
        //Check to see is there is an update for the program
        if (MainGui.jCheckBoxMenuCheckupdate.getState() == true)
        {
            Path currentVersion = Paths.get("C:\\Program Files (x86)\\QuickSave\\QuickSave.exe");
            Path newVersion = Paths.get("I:\\Ted\\Programs\\QuickSave\\QuickSaveInstaller.exe");
            BasicFileAttributes currentTime = Files.readAttributes(currentVersion, BasicFileAttributes.class);
            BasicFileAttributes newTime = Files.readAttributes(newVersion, BasicFileAttributes.class);
            
            //These two variables were created for testing purposes of the check update functionality of the program
	    //DateFormat formatterCurrent = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            //DateFormat formatterNew = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
	    Calendar calendarCurrent = Calendar.getInstance();
            Calendar calendarNew = Calendar.getInstance();
	    calendarCurrent.setTimeInMillis(currentTime.lastAccessTime().toMillis());
            calendarNew.setTimeInMillis(newTime.lastModifiedTime().toMillis());
            Date dateCurrent = calendarCurrent.getTime();
            Date dateNew = calendarNew.getTime();
            
            if (dateCurrent.before(dateNew))
            {
                JOptionPane.showMessageDialog(null, "There is a new update available,\n\nIf you do not want to get this notification disable it under the Settings menu", "Update available", 1);
            }
        }
        
        //start the threads that monitor scanned documents
        new Thread(new tempChecker()).start();
        new Thread(new tempWatcher()).start();

        //initiallize the main gui
        Gui.Init();
    }
    
    public static void startScanSnapAutomation()
    {
        try
        {
            rt = Runtime.getRuntime();
            p = rt.exec(WorkingDirectory + "\\ScanSnapAutomation.exe");
        }
        catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void stopScanSnapAutomation()
    {
        if (p != null)
        {
            p.destroy();
        }
    }
    
    //Gets a list of card holders from the database and populates the drop-down
    //list for credit card scans
    public static void loadCardHolders()
    {
        DatabaseHandler db = new DatabaseHandler();
        TextProcessing txt = new TextProcessing();
        db.openDatabase();
        db.openCardHolderTable();
        int[] cols = {1,2,3};
        DatabaseHandler.ch.sortTable(cols);
        CreditCardHolders = DatabaseHandler.ch.getStringColumn(1);
        CreditCardHolders = txt.arrayConcatenate(CreditCardHolders, DatabaseHandler.ch.getStringColumn(2), ", ");
        CreditCardHolders = txt.arrayConcatenate(CreditCardHolders, DatabaseHandler.ch.getStringColumn(3), " ");
        CreditCardHolderIds = DatabaseHandler.ch.getIntColumn(0);
        CreditCardApproverIds = DatabaseHandler.ch.getIntColumn(4);
        
        db.openCardApproverTable();
        int[] cols2 = {0,1,2};
        DatabaseHandler.ca.sortTable(cols2);
        CreditCardApprovers = DatabaseHandler.ca.getStringColumn(0);
        CreditCardApprovers = txt.arrayConcatenate(CreditCardApprovers, DatabaseHandler.ca.getStringColumn(1), " ");
        CreditCardApprovers = txt.arrayConcatenate(CreditCardApprovers, DatabaseHandler.ca.getStringColumn(2), ", ");
        
        db.openDepTable();
        Departments = DatabaseHandler.dep.getStringColumn(0);
        
        db.closeDatabase();
        
        MainGui.jComboCC_Names.setModel(new DefaultComboBoxModel(CreditCardHolders));
    }
    
    // Used to populate an array from the given file path
    static String[] LoadArray(String path)
    {
        BufferedReader br;
        String str = "";
        try 
        {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null)
            {
               sb.append(line);
               sb.append("\n");
               line = br.readLine();
            }
        
            str = sb.toString();
            br.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str.split("\n");
    }
}

// Constantly checks to see if a document exists in the temp folder, and opens it if one does exist
class tempChecker extends Thread
{
    @Override
    public void run()
    {
        File PreTempFile = new File(Main.TempDirectory + "\\" + "processing.pdf");
        File TempFile = new File(Main.TempDirectory + "\\" + Main.SourceName);
        Path PreTempPath = Paths.get(Main.TempDirectory + "\\" + "processing.pdf");
        Path TempPath = Paths.get(Main.TempDirectory + "\\" + Main.SourceName);
        boolean first = true;
        while (true)
        {
            if(PreTempFile.exists())
            {
                if (TempFile.exists())
                {
                    if (MainGui.jCheckBoxMenuItemAppend.isSelected())
                    {
                        if (MainGui.changesMade == true)
                        {
                            MainGui.SaveChanges(Main.TempDirectory + "\\" + Main.SourceName);
                        }
                        try
                        {
                            PDFMergerUtility mergePdf = new PDFMergerUtility();
                            mergePdf.addSource(TempFile);
                            mergePdf.addSource(PreTempFile);
                            mergePdf.setDestinationFileName(Main.TempDirectory + "\\" + Main.SourceName);
                            mergePdf.mergeDocuments();
                            Files.delete(PreTempPath);
                        }
                        catch (IOException | COSVisitorException ex)
                        {
                            Logger.getLogger(tempChecker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        first = true;
                    }
                    else
                    {
                        try
                        {
                            Files.delete(TempPath);
                            PreTempFile.renameTo(TempFile);
                            MainGui.documentExists = true;
                            MainGui.NewDoc();
                        }
                        catch (IOException ex)
                        {
                            
                        }
                    }
                }
                else
                {
                    PreTempFile.renameTo(TempFile);
                }
            }
            else if (TempFile.exists())
            {
                MainGui.documentExists = true;
                if(first == true)
                {
                    MainGui.NewDoc();
                }
                first = false;
            }
            else
            {
                MainGui.documentExists = false;
                if (MainGui.document != null)
                {
                    MainGui.ClearDisplay();
                }
                first = true;
            }
            MainGui.updateProceedColor();
            try 
            {
                Thread.sleep(500);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(tempChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
 //Used with the xerox scanner, this class performs the same function as the one above.
class tempWatcher extends Thread
{
    @Override
    public void run()
    {
        String path = Main.TempDirectory;
        String name = "qstemp";
        name = name.substring(0, name.length()-4);
        File folder = new File(path);
        int i = 0;
        File[] FileList = folder.listFiles();
        Path source;
        Path dest;
        
        while (i < FileList.length)
        {
            if (FileList[i].getName().contains(name) && (FileList[i].getName().endsWith(".PDF") || FileList[i].getName().endsWith(".pdf")) && FileList[i].getName().length() > 8)
            {
                source = Paths.get(path + "\\" + FileList[i].getName());
                try
                {
                    Files.delete(source);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(tempWatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            i++;
        }
        while (true)
        {
            FileList = folder.listFiles();
            int Index = 0;
            boolean found = false;
            i = 0;

            while (found == false && i < FileList.length)
            {
                if (FileList[i].getName().contains(name) && (FileList[i].getName().endsWith(".PDF") || FileList[i].getName().endsWith(".pdf")) && FileList[i].getName().length() > 8)
                {
                    Index = i;
                    found = true;
                }
                else
                {
                    found = false;
                }
                i++;
            }
            
            if (found == true)
            {
                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(tempWatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
                source = Paths.get(path + "\\" + FileList[Index].getName());
                dest = Paths.get(path + "\\processing.pdf");
                try
                {
                    Files.copy(source, dest, REPLACE_EXISTING);
                    Files.delete(source);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(tempWatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try 
            {
                Thread.sleep(500);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(tempChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

//Called when the program exits to save the settings
class ShutDownHook extends Thread
{
    @Override
    public void run()
    {
        Settings settings = new Settings();
        settings.SaveSettings();
        
        File ConflictSetting = new File(Main.TempDirectory + "//Conflict Variable.txt");
        ConflictSetting.delete();
    }
}

//Used if there a need to pause the program
class wait extends Thread
{
    public static boolean length(long len)
    {
        try
        {
            Thread.sleep(len);
        }
        catch (InterruptedException ex)
        {
            JOptionPane.showMessageDialog(null,"Thread " + Thread.currentThread().getName() + " failed to sleep","Thread not sleepint",1);
        }
        return true;
    }
}