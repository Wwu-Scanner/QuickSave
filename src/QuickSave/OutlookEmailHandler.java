/*
This class creates the outlook emails for credit card approvals that are sent
to the card holder supervisors
*/

package QuickSave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class OutlookEmailHandler
{

    static String SubjectTemplate;
    static String BodyTemplate;    
    
    //Uses the provided information to create the email and attach the appropriate pdfs
    public static void sendApprovalEmail(String[] To, String[] CC, String[] BCC, String[] Attachments, String[] Names, String SubjectDate, boolean AutoSend)
    {	
        StringBuilder contentBuilder = new StringBuilder();
        try
        {
            BufferedReader in;
            in = new BufferedReader(new FileReader(Main.ApprovalEmailTemplateBody));
            String str;
            while ((str = in.readLine()) != null)
            {
                contentBuilder.append(str);
            }
            in.close();
        }
        catch (IOException e)
        {
        }
        String Body = "";
        Body = Body + contentBuilder.toString();
        
        String Subject = "";
        BufferedReader br;
        try
        {
            br = new BufferedReader(new FileReader(Main.ApprovalEmailTemplateSubject));
            Subject = Subject + br.readLine();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
        }

        String CardHolders = "";
        for (String Name : Names)
        {
            CardHolders = CardHolders + "<li>" + Name + "<\\/li>";
        }
        CardHolders = "<ul>" + CardHolders + "<\\/ul>";
        
        Subject = Subject.replaceAll("\\{MonthName\\}", SubjectDate);
        Body = Body.replaceAll("\\{CardHolders\\}", CardHolders);
        
        sendEmail(To,CC,BCC,Attachments,Subject,Body,AutoSend);
    }
    
    //Generates an email with the provided pdf attached
    public static void sendDocumentEmail(String path)
    {	
        String[] array = path.split("\\\\");
        String name = array[array.length - 1];
        String[] array2 = name.split("\\.");
        int length = name.length() - 1;
        length = length - array2[array2.length-1].length();
        name = name.substring(0, length);
        StringBuilder contentBuilder = new StringBuilder();
        try
        {
            BufferedReader in;
            in = new BufferedReader(new FileReader(Main.EmailTemplateBody));
            String str;
            while ((str = in.readLine()) != null)
            {
                contentBuilder.append(str);
            }
            in.close();
        }
        catch (IOException e)
        {
        }
        String Body = contentBuilder.toString();
        
        
        String Subject = "";
        BufferedReader br;
        try
        {
            br = new BufferedReader(new FileReader(Main.EmailTemplateSubject));
            String s = br.readLine();
            if (s != null)
            {
                Subject = s;
                Subject = Subject.replace("{DocumentName}", name);
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(EditEmailContentGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        Body = Body.replaceAll("\\{DocumentName\\}", name);
        
        String[] To = {};
        String[] CC = {};
        String[] BCC = {};
        String[] Attachments = {path};
        File file = new File(path);
        FileHandler f = new FileHandler();
        if (f.getFileSize(file)*1048576 > Main.maxAttachmentSize)
        {
            JOptionPane.showMessageDialog(null, "The size of the file exceeded the maximum attachment size.", "Error",JOptionPane.ERROR_MESSAGE);
        }
        sendEmail(To,CC,BCC,Attachments,Subject,Body,false);
    }
    
    public static void sendEmail(String[] To, String[] CC, String[] BCC, String[] Attachments, String Subject, String Body, boolean AutoSend)
    {
        Display display = new Display();
        Shell shell = new Shell(display);
        OleFrame frame = new OleFrame(shell, SWT.NONE);

        // This should start outlook if it is not running yet
        OleClientSite site = new OleClientSite(frame, SWT.NONE, "OVCtl.OVCtl");
        site.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);

        // Now get the outlook application
        OleClientSite site2 = new OleClientSite(frame, SWT.NONE, "Outlook.Application");
        OleAutomation outlook = new OleAutomation(site2);

        OleAutomation mail = invoke(outlook, "CreateItem", 0 /* Mail item */).getAutomation();
        
        if (To.length > 0)
        {
            String to = To[0];
            for (int i = 1; i < To.length; i++)
            {
                to = to + "; " + To[i];
            }
            setProperty(mail, "To", to);
        }
        if (CC.length > 0)
        {
            String cc = CC[0];
            for (int i = 1; i < CC.length; i++)
            {
                cc = cc + "; " + CC[i];
            }
            setProperty(mail, "CC", cc);
        }
        if (BCC.length > 0)
        {
            String bcc = BCC[0];
            for (int i = 1; i < BCC.length; i++)
            {
                bcc = bcc + "; " + BCC[i];
            }
            setProperty(mail, "BCC", bcc);
        }
                
        setProperty(mail, "Subject", Subject);
        setProperty(mail, "BodyFormat", 2 /* HTML */);
        setProperty(mail, "HtmlBody", Body);      
        for (String Attachment : Attachments)
        {
            File file = new File(Attachment);
            if (file.exists())
            {
                OleAutomation attachments = getProperty(mail, "Attachments");
                invoke(attachments, "Add", Attachment);
            }     
        }
        
        if (AutoSend == true)
        {
            invoke(mail, "Send");
        }
        else
        {
            invoke(mail, "Display");
        }
        
        display.dispose();
        shell.dispose();
        frame.dispose();
        mail.dispose();
    }

    private static OleAutomation getProperty(OleAutomation auto, String name)
    {
        Variant varResult = auto.getProperty(property(auto, name));
        if (varResult != null && varResult.getType() != OLE.VT_EMPTY)
        {
            OleAutomation result = varResult.getAutomation();
            varResult.dispose();
            return result;
        }
        return null;
    }

    private static Variant invoke(OleAutomation auto, String command, String value)
    {
        return auto.invoke(property(auto, command), new Variant[] { new Variant(value) });
    }

    private static Variant invoke(OleAutomation auto, String command)
    {
        return auto.invoke(property(auto, command));
    }

    private static Variant invoke(OleAutomation auto, String command, int value)
    {
        return auto.invoke(property(auto, command), new Variant[] { new Variant(value) });
    }

    private static boolean setProperty(OleAutomation auto, String name, String value)
    {
        return auto.setProperty(property(auto, name), new Variant(value));
    }

    private static boolean setProperty(OleAutomation auto, String name, int value)
    {
        return auto.setProperty(property(auto, name), new Variant(value));
    }

    private static int property(OleAutomation auto, String name)
    {
        return auto.getIDsOfNames(new String[] { name })[0];
    }
}
