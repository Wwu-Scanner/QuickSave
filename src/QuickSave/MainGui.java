/*
This is the primary class for the gui of the program. It handles most of the functionality
of the program, along with the methods for saving documents.
*/

package QuickSave;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PageExtractor;

public final class MainGui extends javax.swing.JFrame {
    
    //Initialization of other classes so instances of them can be used
    static Main main = new Main();
    static OutlookEmailHandler email = new OutlookEmailHandler();
    static Tesseract instance = Tesseract.getInstance();
    static ImageProcessing img = new ImageProcessing();
    static TextProcessing txt = new TextProcessing();
    static DateProcessing date = new DateProcessing();
    static TempFileHandler tmp = new TempFileHandler();
    static Settings settings = new Settings();
    static DatabaseHandler db = new DatabaseHandler();
    ChangedirectoriesGui directories = new ChangedirectoriesGui();
    CardHoldersGui holderdb = new CardHoldersGui();
    CardApproversGui approverdb = new CardApproversGui();
    ApprovalDatabaseGui approvals = new ApprovalDatabaseGui();
    EditEmailContentGui emailGui = new EditEmailContentGui();
    EditEmailContentGui emailGui2 = new EditEmailContentGui();
    
    //General global variables
    public static DefaultListModel listModel = new DefaultListModel();
    public static int SelectedDocumentType = 0;
    static int CurrentRotation = 0;
    static int CurrentPage;
    static int PrevPage;
    static int LastPage;
    static int TotalPages;
    static int displayedTotal = 0;
    static int lastPageLoaded = 0;
    static int[] displayedNumber = new int[Main.maxPages];
    static int[] pageList = new int[Main.maxPages];
    static ArrayList FlaggedPages = new ArrayList();
    static ArrayList ExpenseNames = new ArrayList();
    static int FlaggedPageIndex = 0;
    public static int filmStripCoordsx = 0;
    public static int filmStripCoordsy = 0;
    public static ArrayList ExpenseCoordsY = new ArrayList();
    static int[] pageRotation = new int[Main.maxPages];
    static boolean[] pageDeleted = new boolean[Main.maxPages];
    static double CurrentScale = 0.35;
    public static String CSDirectory = Main.DocumentsDirectory;
    static String EditPath = Main.DocumentsDirectory;
    int refNumber = 0;
    public static final int BufferSize = 7;
    public static int halfBuffer = (int)(BufferSize/2);
    public static int[] ConflictSetting = {0, 0};
    public static List<PDPage> pages;
    public static String manual = "";
    public static int filmStripWidth;
    public static String lastSavedDocumentPath;
    public int lastCheckNumber;
    
    //These are used to determine the cursor's position within the document preview pane
    static double xCoord;
    static double yCoord;
    static double xOffset;
    static double yOffset;
    
    //Logs
    public static int changeLog[][] = new int[100][2];
    public static int changeLogPointer = 0;
    public static int redoLog[][] = new int[100][2];
    public static int redoLogPointer = 0;
    
    //Flags
    public static boolean fieldsValid = false;
    public static boolean documentExists = false;
    static boolean noDoc = true;
    static boolean firstRun = true;
    static boolean changesMade = false;
    static boolean imageDisplayed = true;
    static boolean pageOperationInProgress = false;
    static boolean cursormoving = false;
    static boolean cursordragging = false;
    static boolean draggingNumber = false;
    static boolean draggingDate = false;
    static boolean draggingName = false;
    static boolean draggingAmount = false;
    boolean deleteDone = true;
    public static boolean showFilmStrip = true;
    public static boolean filmStripLoaded = false;
    public static boolean filmStripUpdateInProgress = false;
    public static boolean restoringFromLog = false;
    public static boolean rotateInProgress = false;
    public static boolean autoHideToTray = false;
    public static boolean hideOnExit = false;
    public static boolean exiting = true;
    public static boolean editingDocument = false;
    public static boolean emailAfterSaving = false;
    public static boolean Approvalopen = false;
    public static boolean Approveropen = false;
    public static boolean Holderopen = false;
    public static boolean selecting = false;
    public static boolean deselecting = false;
    
    //PDF variables
    public static PDDocumentCatalog Catalog;
    public static PDDocument document;
    public static BufferedImage[] images = new BufferedImage[Main.maxPages];
    public static BufferedImage[] filmStrip = new BufferedImage[Main.maxPages];
    public static BufferedImage blank;
    
    //Check OCR Fields variables
    static BufferedImage imageOCR = null;
    static BufferedImage CheckImgName = null;
    static BufferedImage CheckImgDate = null;
    static BufferedImage CheckImgAmount = null;
    static BufferedImage CheckImgNumber = null;
    static Graphics2D movingImage;
    static int[] RefCoords = {0,0};
    public static double[] CheckCoordsNumber = {0.75,0.935,0.15,0.035};
    public static double[] CheckCoordsDate = {0.51,0.875,0.15,0.035};
    public static double[] CheckCoordsAmount = {0.775,0.88,0.12,0.04};
    public static double[] CheckCoordsName = {0.12,0.82,0.5,0.0225};
    public static double[] CheckCoordsZero = {0,0,0.025,0.025};
    public static int[] CheckColorNumber = {75,175,75,100};
    public static int[] CheckColorDate = {150,75,175,100};
    public static int[] CheckColorAmount = {75,75,175,100};
    public static int[] CheckColorName = {175,75,75,100};
    public static int[] CheckColorZero = {75,175,75,100};
    static String CheckName = null;
    static String CheckNumber = null;
    static String CheckDate = null;
    static String CheckAmount = null;
    static String prevDate = "";
    static int prevNumber = 0;
    static float tol = 0.3f;
    static int w_o = 0;
    static int h_o = 0;
    
    //Icon image declarataion
    ImageIcon icon = new ImageIcon(getClass().getResource("Resources/Save.png"));
    ImageIcon tray = new ImageIcon(getClass().getResource("Resources/TrayIcon.gif"));
    ImageIcon iconCCW = new ImageIcon(getClass().getResource("Resources/ccw.png"));
    ImageIcon iconCW = new ImageIcon(getClass().getResource("Resources/cw.png"));
    ImageIcon iconNext = new ImageIcon(getClass().getResource("Resources/next.png"));
    ImageIcon iconPrev = new ImageIcon(getClass().getResource("Resources/prev.png"));
    
    public MainGui()
    {
        initComponents();
    }
    
    //Initializes gui at program startup
    void Init()
    {
        jFilmStrip.addMouseMotionListener(new MouseMotionListener()
        {
            int index = 0;
            @Override
            public void mouseDragged(MouseEvent e)
            {
                
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
                filmStripCoordsy = e.getY();
                if (!ExpenseCoordsY.isEmpty())
                {                    
                    if (filmStripCoordsy < Integer.parseInt(ExpenseCoordsY.get(index).toString()))
                    {
                        jFilmStrip.setSelectedIndex(index);
                    }
                }
            }
        });
        
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(25);
        BindKeys();
        SetDocumentType(SelectedDocumentType);
        jScrollFilmStrip.setVisible(showFilmStrip);
        jLabelCS_Destination.setText(CSDirectory);
        jCheckBoxUseAutomation.setSelected(Main.UseScanSnapAutomation);
        jCheckBoxAutoHide.setSelected(autoHideToTray);
        jCheckBoxHideOnExit.setSelected(hideOnExit);
        createTrayMenu();
        if (documentExists == true || autoHideToTray == false)
        {
            setVisible(true);
        }
        else if (autoHideToTray == true)
        {
            setVisible(true);
            setVisible(false);
        }
        if (Main.UseScanSnapAutomation == true)
        {
            Main.startScanSnapAutomation();
        }
    }
    
    //Updates the proceed button color depending on whether or not a document is ready
    //or input fields are filled
    public static void updateProceedColor()
    {    
        if (documentExists == true)
        {
            if (fieldsValid == true)
            {
                jButtonProceed.setBackground(Color.GREEN);
            }
            else
            {
                jButtonProceed.setBackground(Color.CYAN);
            }
        }
        else
        {
            jButtonProceed.setBackground(Color.ORANGE);
        }     
    }
    
    //Creates a menu in the taskbar
    void createTrayMenu()
    {
        //Check the SystemTray is supported
        if (!SystemTray.isSupported())
        {
            JOptionPane.showMessageDialog(null,"The systemTray is not supported on your computer","SystemTray is not supported",1);
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(tray.getImage());
        final SystemTray sysTray = SystemTray.getSystemTray();
        
        // Create a pop-up menu components
        final MenuItem openItem = new MenuItem("Open");
        final MenuItem hideItem = new MenuItem("Hide");
        final CheckboxMenuItem autoHideItem = new CheckboxMenuItem("Auto Hide to Tray");
        final CheckboxMenuItem exitHideItem = new CheckboxMenuItem("Hide on Exit");
        final MenuItem helpItem = new MenuItem("Help");
        final MenuItem exitItem = new MenuItem("Exit");
       
        //Add components to pop-up menu
        popup.add(openItem);
        popup.add(hideItem);
        popup.addSeparator();
        popup.add(autoHideItem);
        popup.add(exitHideItem);
        popup.addSeparator();
        popup.add(helpItem);
        popup.addSeparator();
        popup.add(exitItem);
       
        //Create the popup menu
        trayIcon.setPopupMenu(popup);
       
        autoHideItem.setState(autoHideToTray);
        exitHideItem.setState(hideOnExit);
        
        try
        {
            sysTray.add(trayIcon);
        }
        catch (AWTException e)
        {
            JOptionPane.showMessageDialog(null,"The tray icon could not be added","Icon error",1);
        }
        
        MouseListener ml;
        ml = new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {               
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                autoHideItem.setState(autoHideToTray);
                exitHideItem.setState(hideOnExit);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
            }
        };
        trayIcon.addMouseListener(ml);
        
        trayIcon.addActionListener(new ActionListener()
        {  
            @Override
            public void actionPerformed(ActionEvent e)
            {  
                setVisible(true);
            }  
        });
        
        openItem.addActionListener(new ActionListener()
        {  
            @Override
            public void actionPerformed(ActionEvent e)
            {  
                setVisible(true);
            }  
        });
        
        hideItem.addActionListener(new ActionListener()
        {  
            @Override
            public void actionPerformed(ActionEvent e)
            {  
                closeProceedure(false); 
            }  
        });
        
        autoHideItem.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent itemEvent)
            {                      
                autoHideToTray = !autoHideToTray;
                jCheckBoxAutoHide.setSelected(autoHideToTray);
                autoHideItem.setState(autoHideToTray);
            }
        });
        
        exitHideItem.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent itemEvent)
            {      
                hideOnExit = !hideOnExit;
                exitHideItem.setState(hideOnExit);
                jCheckBoxHideOnExit.setSelected(hideOnExit);
            }  
        });
        
        helpItem.addActionListener(new ActionListener()
        {  
            @Override
            public void actionPerformed(ActionEvent e)
            {  
                File help = new File("C:\\Program Files (x86)\\QuickSave\\Help.html");
                try
                {
                    Desktop.getDesktop().open(help);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        });
        
        exitItem.addActionListener(new ActionListener()
        {  
            @Override
            public void actionPerformed(ActionEvent e)
            {  
                System.exit(0);
            }  
        });
    }
    
    //Sets the options pane to whatever document type is selected
    static void SetDocumentType(int Type)
    {
        SelectedDocumentType = Type;
        CardLayout cl = (CardLayout)(jPanelFields.getLayout());
        switch(Type)
        {
            case 1:
                cl.show(jPanelFields, "Check");
                jRadioButtonMenuItem1.setSelected(true);
                break;
            case 2:
                cl.show(jPanelFields, "CreditCard");
                jRadioButtonMenuItem2.setSelected(true);
                break;
            case 3:
                cl.show(jPanelFields, "JournalEntry");
                jRadioButtonMenuItem3.setSelected(true);
                break;
            case 4:
                cl.show(jPanelFields, "CashReceipt");
                jRadioButtonMenuItem4.setSelected(true);
                break;
            case 5:
                cl.show(jPanelFields, "HandCheck");
                jRadioButtonMenuItem5.setSelected(true);
                break;
            case 7:
                cl.show(jPanelFields, "ACH");
                jRadioButtonMenuItem8.setSelected(true);
                break;
            case 9:
                cl.show(jPanelFields, "Edit");
                jRadioButtonMenuItem9.setSelected(true);
                break;
            default:
                cl.show(jPanelFields, "CustomSave");
                jRadioButtonMenuItem7.setSelected(true);
                break;
        }
        checkFieldsValidity();
    }
    
    //Initializes gui for a new document
    static void NewDoc()
    {
        jSliderZoom.setEnabled(true);
        jCheckBoxScale.setEnabled(true);
        String path = Main.TempDirectory + "\\" + Main.SourceName;
        Path filePath = Paths.get(path);
        firstRun = true;
        CurrentRotation = 0;
        Arrays.fill(pageRotation,0);
        Arrays.fill(pageDeleted,false);
        Arrays.fill(images,null);
        changesMade = false;
        tmp.WaitFileExist(path);
        if (Files.exists(filePath))
        {
            noDoc = false;
            CurrentPage = 0;
            LoadPages(path);
            DisplayImage();
        }
        checkFieldsValidity();
    }
    
    //Automatically Scales the currently displayed image
    static void scaleCurrentImage()
    {
        int h = jScrollPane1.getHeight()-30;
        int w = jScrollPane1.getWidth()-30;
        CurrentScale = img.GetScale(images[CurrentPage], w, h);
        jSliderZoom.setValue((int)(100*CurrentScale));
        jCheckBoxScale.setSelected(true);
    }
    
    //Retrieves the first page of a pdf document and starts the page buffer 
    //thread to load aditional pages
    static void LoadPages(String path)
    {
        try
        {
            Thread.sleep(500);
            File doc = new File(path);
            document = PDDocument.load(doc);
            Catalog = document.getDocumentCatalog();
            pages = document.getDocumentCatalog().getAllPages();
            TotalPages = document.getNumberOfPages();
            if (TotalPages > Main.maxPages)
            {TotalPages = Main.maxPages;}     
            displayedTotal = TotalPages;
            LastPage = TotalPages-1;
            images[0] = pages.get(0).convertToImage(BufferedImage.TYPE_INT_RGB,Main.dpi);
            displayedNumber[0] = 1;
        }
        catch (IOException | InterruptedException ex)
        {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //After the document has been loaded populate the filmstrip and start the page buffer
        new Thread(new populateFilmStrip()).start();
        new Thread(new PageBuffer()).start();
    }
    
    //Deletes the pdf document from the source path
    static void DeleteDoc()
    {
        Path Source = Paths.get(Main.TempDirectory + "\\" + Main.SourceName);
        ClearDisplay();
        try
        {
            Files.delete(Source);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Checks to see if any pages in the PDF document wer deleted or rotated
    //and commits the changes to the PDF file
    static void SaveChanges(String path)
    {
        int i = 0;
        while (i < Main.maxPages && changesMade == false)
        {
            if (pageRotation[i] != 0 || pageDeleted[i] == true)
            {
                changesMade = true;
            }
            i++;
        }
        if (changesMade == true)
        {
            try
            {
                File doc = new File(path);
                PDDocument Document = PDDocument.load(doc);
                List<PDPage> docPages = Document.getDocumentCatalog().getAllPages();

                i = 0;
                while (i < Main.maxPages)
                {    
                    if(pageRotation[i] != 0)
                    {
                        docPages.get(i).setRotation(pageRotation[i]);
                    }
                    i++;
                }
                i = TotalPages-1;
                while (i >= 0)
                {
                    if(pageDeleted[i] == true)
                    {
                        Document.removePage(i);
                    }
                    i--;
                }
                try
                {
                    Document.save(path);
                }
                catch (COSVisitorException ex)
                {
                    Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                Document.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Updates the filmstrip
    static void updateFilmstrip()
    {
        if (filmStripLoaded == true)
        {
            filmStripUpdateInProgress = true;
            if (selecting)
            {
                try
                {
                    int ref = pages.get(CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,50).getWidth();
                    int dpi = (int)(50*((double)filmStripWidth/(double)ref));
                    
                    BufferedImage base = pages.get(CurrentPage).convertToImage(BufferedImage.TRANSLUCENT,dpi);
                    int width = base.getWidth();
                    int height = base.getHeight();
                    BufferedImage overlay = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
                    BufferedImage combined = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
                    
                    Graphics g = overlay.createGraphics();
                    g.setColor(new Color(0,0,20,200));
                    if (!deselecting)
                    {
                        g.fillRect(width - 30, height-35, 30, 35);
                    }
                    if (CurrentPage + 1 <= 9)
                    {
                        g.fillRect(0, height-35, 25, 35);
                    }
                    else if (CurrentPage + 1 >= 10 && CurrentPage + 1 < 100)
                    {
                        g.fillRect(0, height-35, 40, 35);
                    }
                    g.setColor(Color.white);
                    g.setFont(new Font("TimeRoman", Font.PLAIN, 25));
                    g.drawString(CurrentPage + 1+"", 5, height - 10);
                    if (!deselecting)
                    {
                        g.drawString("E", width - 23, height - 10);
                    }
                    Graphics g2 = combined.createGraphics();
                    g2.drawImage(base, 0, 0, null);
                    g2.drawImage(overlay, 0, 0, null);
                    
                    listModel.set(CurrentPage,new ImageIcon(combined));
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selecting = false;
            deselecting = false;
            jFilmStrip.setSelectedIndex(displayedNumber[CurrentPage] - 1);
            jFilmStrip.ensureIndexIsVisible(jFilmStrip.getSelectedIndex());
            filmStripUpdateInProgress = false;
        }
    }
    
    //Displays a given buffered image
    static void DisplayImage()
    {            
        if (noDoc == false)
        {
            BufferedImage image2;
            BufferedImage image1;
            if(images[CurrentPage] != null)
            {
                imageDisplayed = true;
                if(jCheckBoxScale.isSelected())
                {
                    scaleCurrentImage();
                }
                image1 = images[CurrentPage];
                int new_width = (int)(image1.getWidth()*CurrentScale);
                int new_height = (int)(image1.getHeight()*CurrentScale);
                
                //If the check document type is selected and the cover page is loaded
                if (SelectedDocumentType == 1 && CurrentPage == 0)
                {
                    if (firstRun == true)
                    {
                        imageOCR = images[0];
                        RefCoords = img.FindRefCoords(imageOCR,0.2);
                        w_o = imageOCR.getWidth();
                        h_o = imageOCR.getHeight();
                        ReadCheck();
                        firstRun = false;
                    }

                    BufferedImage overlay = new BufferedImage(w_o,h_o,BufferedImage.TYPE_INT_ARGB);
                    int mainWidth = jLabelImage.getWidth();
                    int mainHeight = jLabelImage.getHeight();
                    int overlayWidth = overlay.getWidth();
                    int overlayHeight = overlay.getHeight();
                    
                    int widthDifference = 0;
                    int heightDifference = 0;
                    double scaledXCoord = 0;
                    double scaledYCoord = 0;
                    
                    movingImage = overlay.createGraphics();
                    
                    if (new_width < mainWidth)
                    {
                        widthDifference = (mainWidth - new_width) / 2;
                        if (xCoord > widthDifference)
                        {
                            scaledXCoord = Double.parseDouble(new DecimalFormat("#.###").format((xCoord - widthDifference) / CurrentScale));
                        }
                    }
                    else
                    {
                        scaledXCoord = Double.parseDouble(new DecimalFormat("#.###").format(xCoord / CurrentScale));
                    }
                    if (new_height < mainHeight)
                    {
                        heightDifference = (mainHeight - new_height) / 2;
                        if (yCoord > heightDifference)
                        {
                            scaledYCoord = Double.parseDouble(new DecimalFormat("#.###").format((yCoord - heightDifference) / CurrentScale));
                        }
                    }
                    else
                    {
                        scaledYCoord = Double.parseDouble(new DecimalFormat("#.###").format(yCoord / CurrentScale));
                    }
                    
                    //These get the calculated coordinates and dimensions of the rectangles
                    int[] numberArea = {RefCoords[0] + (int) (CheckCoordsNumber[0] * overlayWidth),
                                        RefCoords[1] - (int) (CheckCoordsNumber[1] * overlayHeight),
                                        ((int) (CheckCoordsNumber[2] * overlayWidth)),
                                        ((int) (CheckCoordsNumber[3] * overlayHeight))};
                    int[] dateArea = {RefCoords[0] + (int) (CheckCoordsDate[0] * overlayWidth),
                                        RefCoords[1] - (int) (CheckCoordsDate[1] * overlayHeight),
                                        ((int) (CheckCoordsDate[2] * overlayWidth)),
                                        ((int) (CheckCoordsDate[3] * overlayHeight))};
                    int[] amountArea = {RefCoords[0] + (int) (CheckCoordsAmount[0] * overlayWidth),
                                        RefCoords[1] - (int) (CheckCoordsAmount[1] * overlayHeight),
                                        ((int) (CheckCoordsAmount[2] * overlayWidth)),
                                        ((int) (CheckCoordsAmount[3] * overlayHeight))};
                    int[] nameArea = {RefCoords[0] + (int) (CheckCoordsName[0] * overlayWidth),
                                        RefCoords[1] - (int) (CheckCoordsName[1] * overlayHeight),
                                        ((int) (CheckCoordsName[2] * overlayWidth)),
                                        ((int) (CheckCoordsName[3] * overlayHeight))};
                    
                    //Detect if the mouse is moving and highlights the rectangles if the mouse is over them
                    if (!cursordragging)
                    {
                        movingImage.setColor(new Color(CheckColorNumber[0],CheckColorNumber[1],CheckColorNumber[2],CheckColorNumber[3]));
                        movingImage.fillRect(numberArea[0],numberArea[1],numberArea[2],numberArea[3]);
                        movingImage.setColor(new Color(CheckColorDate[0],CheckColorDate[1],CheckColorDate[2],CheckColorDate[3]));
                        movingImage.fillRect(dateArea[0],dateArea[1],dateArea[2],dateArea[3]);
                        movingImage.setColor(new Color(CheckColorAmount[0],CheckColorAmount[1],CheckColorAmount[2],CheckColorAmount[3]));
                        movingImage.fillRect(amountArea[0],amountArea[1],amountArea[2],amountArea[3]);
                        movingImage.setColor(new Color(CheckColorName[0],CheckColorName[1],CheckColorName[2],CheckColorName[3]));
                        movingImage.fillRect(nameArea[0],nameArea[1],nameArea[2],nameArea[3]);
                        overlay = img.DrawRectangle(overlay,CheckCoordsZero,CheckColorZero);
                        draggingAmount = false;
                        draggingNumber = false;
                        draggingDate = false;
                        draggingName = false;
                    }
                    if (cursormoving && !cursordragging)
                    {
                        if (xCoord > widthDifference && yCoord > heightDifference)
                        {
                            if (scaledXCoord > numberArea[0] && 
                                    scaledYCoord > numberArea[1] &&
                                    scaledXCoord < numberArea[2] + numberArea[0] &&
                                    scaledYCoord < numberArea[3] + numberArea[1])
                            {
                                xOffset = scaledXCoord - numberArea[0];
                                yOffset = scaledYCoord - numberArea[1];
                                draggingNumber = true;
                                draggingDate = false;
                                draggingAmount = false;
                                draggingName = false;
                                movingImage.setColor(new Color(100,100,100,100));
                                movingImage.fillRect(numberArea[0],numberArea[1],numberArea[2],numberArea[3]);
                            }
                            else if (scaledXCoord > dateArea[0] && 
                                    scaledYCoord > dateArea[1] &&
                                    scaledXCoord < dateArea[2] + dateArea[0] &&
                                    scaledYCoord < dateArea[3] + dateArea[1])
                            {
                                xOffset = scaledXCoord - dateArea[0];
                                yOffset = scaledYCoord - dateArea[1];
                                draggingDate = true;
                                draggingNumber = false;
                                draggingAmount = false;
                                draggingName = false;
                                movingImage.setColor(new Color(100,100,100,100));
                                movingImage.fillRect(dateArea[0],dateArea[1],dateArea[2],dateArea[3]);
                            }
                            else if (scaledXCoord > amountArea[0] && 
                                    scaledYCoord > amountArea[1] &&
                                    scaledXCoord < amountArea[2] + amountArea[0] &&
                                    scaledYCoord < amountArea[3] + amountArea[1])
                            {
                                xOffset = scaledXCoord - amountArea[0];
                                yOffset = scaledYCoord - amountArea[1];
                                draggingAmount = true;
                                draggingNumber = false;
                                draggingDate = false;
                                draggingName = false;
                                movingImage.setColor(new Color(100,100,100,100));
                                movingImage.fillRect(amountArea[0],amountArea[1],amountArea[2],amountArea[3]);
                            }
                            else if (scaledXCoord > nameArea[0] && 
                                    scaledYCoord > nameArea[1] &&
                                    scaledXCoord < nameArea[2] + nameArea[0] &&
                                    scaledYCoord < nameArea[3] + nameArea[1])
                            {
                                xOffset = scaledXCoord - nameArea[0];
                                yOffset = scaledYCoord - nameArea[1];
                                draggingName = true;
                                draggingNumber = false;
                                draggingDate = false;
                                draggingAmount = false;
                                movingImage.setColor(new Color(100,100,100,100));
                                movingImage.fillRect(nameArea[0],nameArea[1],nameArea[2],nameArea[3]);
                            }
                        }
                        cursormoving = false;
                    }
                    else if (cursordragging)
                    {
                        if (xCoord > widthDifference && yCoord > heightDifference && scaledXCoord < overlayWidth && scaledYCoord < overlayHeight)
                        {
                            if (draggingNumber)
                            {
                                movingImage.setColor(new Color(CheckColorNumber[0],CheckColorNumber[1],CheckColorNumber[2],CheckColorNumber[3]));
                                movingImage.fillRect((int)scaledXCoord - (int)xOffset,(int)scaledYCoord - (int)yOffset,numberArea[2],numberArea[3]);
                                movingImage.setColor(new Color(CheckColorDate[0],CheckColorDate[1],CheckColorDate[2],CheckColorDate[3]));
                                movingImage.fillRect(dateArea[0],dateArea[1],dateArea[2],dateArea[3]);
                                movingImage.setColor(new Color(CheckColorAmount[0],CheckColorAmount[1],CheckColorAmount[2],CheckColorAmount[3]));
                                movingImage.fillRect(amountArea[0],amountArea[1],amountArea[2],amountArea[3]);
                                movingImage.setColor(new Color(CheckColorName[0],CheckColorName[1],CheckColorName[2],CheckColorName[3]));
                                movingImage.fillRect(nameArea[0],nameArea[1],nameArea[2],nameArea[3]);
                                
                                double formatX = Double.parseDouble(new DecimalFormat("#.###").format(((scaledXCoord - xOffset) - RefCoords[0]) / overlayWidth));
                                double formatY = Double.parseDouble(new DecimalFormat("#.###").format((RefCoords[1] - (scaledYCoord - yOffset)) / overlayHeight));
                                
                                CheckCoordsNumber[0] = formatX;
                                CheckCoordsNumber[1] = formatY;
                            }
                            else if (draggingDate)
                            {
                                movingImage.setColor(new Color(CheckColorNumber[0],CheckColorNumber[1],CheckColorNumber[2],CheckColorNumber[3]));
                                movingImage.fillRect(numberArea[0],numberArea[1],numberArea[2],numberArea[3]);
                                movingImage.setColor(new Color(CheckColorDate[0],CheckColorDate[1],CheckColorDate[2],CheckColorDate[3]));
                                movingImage.fillRect((int)scaledXCoord - (int)xOffset,(int)scaledYCoord - (int)yOffset,dateArea[2],dateArea[3]);
                                movingImage.setColor(new Color(CheckColorAmount[0],CheckColorAmount[1],CheckColorAmount[2],CheckColorAmount[3]));
                                movingImage.fillRect(amountArea[0],amountArea[1],amountArea[2],amountArea[3]);
                                movingImage.setColor(new Color(CheckColorName[0],CheckColorName[1],CheckColorName[2],CheckColorName[3]));
                                movingImage.fillRect(nameArea[0],nameArea[1],nameArea[2],nameArea[3]);
                                
                                double formatX = Double.parseDouble(new DecimalFormat("#.###").format(((scaledXCoord - xOffset) - RefCoords[0]) / overlayWidth));
                                double formatY = Double.parseDouble(new DecimalFormat("#.###").format((RefCoords[1] - (scaledYCoord - yOffset)) / overlayHeight));
                                
                                CheckCoordsDate[0] = formatX;
                                CheckCoordsDate[1] = formatY;
                            }
                            else if (draggingAmount)
                            {
                                movingImage.setColor(new Color(CheckColorNumber[0],CheckColorNumber[1],CheckColorNumber[2],CheckColorNumber[3]));
                                movingImage.fillRect(numberArea[0],numberArea[1],numberArea[2],numberArea[3]);
                                movingImage.setColor(new Color(CheckColorDate[0],CheckColorDate[1],CheckColorDate[2],CheckColorDate[3]));
                                movingImage.fillRect(dateArea[0],dateArea[1],dateArea[2],dateArea[3]);
                                movingImage.setColor(new Color(CheckColorAmount[0],CheckColorAmount[1],CheckColorAmount[2],CheckColorAmount[3]));
                                movingImage.fillRect((int)scaledXCoord - (int)xOffset,(int)scaledYCoord - (int)yOffset,amountArea[2],amountArea[3]);
                                movingImage.setColor(new Color(CheckColorName[0],CheckColorName[1],CheckColorName[2],CheckColorName[3]));
                                movingImage.fillRect(nameArea[0],nameArea[1],nameArea[2],nameArea[3]);
                                
                                double formatX = Double.parseDouble(new DecimalFormat("#.###").format(((scaledXCoord - xOffset) - RefCoords[0]) / overlayWidth));
                                double formatY = Double.parseDouble(new DecimalFormat("#.###").format((RefCoords[1] - (scaledYCoord - yOffset)) / overlayHeight));
                                
                                CheckCoordsAmount[0] = formatX;
                                CheckCoordsAmount[1] = formatY;
                            }
                            else if (draggingName)
                            {
                                movingImage.setColor(new Color(CheckColorNumber[0],CheckColorNumber[1],CheckColorNumber[2],CheckColorNumber[3]));
                                movingImage.fillRect(numberArea[0],numberArea[1],numberArea[2],numberArea[3]);
                                movingImage.setColor(new Color(CheckColorDate[0],CheckColorDate[1],CheckColorDate[2],CheckColorDate[3]));
                                movingImage.fillRect(dateArea[0],dateArea[1],dateArea[2],dateArea[3]);
                                movingImage.setColor(new Color(CheckColorAmount[0],CheckColorAmount[1],CheckColorAmount[2],CheckColorAmount[3]));
                                movingImage.fillRect(amountArea[0],amountArea[1],amountArea[2],amountArea[3]);
                                movingImage.setColor(new Color(CheckColorName[0],CheckColorName[1],CheckColorName[2],CheckColorName[3]));
                                movingImage.fillRect((int)scaledXCoord - (int)xOffset,(int)scaledYCoord - (int)yOffset,nameArea[2],nameArea[3]);
                                
                                double formatX = Double.parseDouble(new DecimalFormat("#.###").format(((scaledXCoord - xOffset) - RefCoords[0]) / overlayWidth));
                                double formatY = Double.parseDouble(new DecimalFormat("#.###").format((RefCoords[1] - (scaledYCoord - yOffset)) / overlayHeight));
                                
                                CheckCoordsName[0] = formatX;
                                CheckCoordsName[1] = formatY;
                            }
                            else
                            {
                                movingImage.setColor(new Color(CheckColorNumber[0],CheckColorNumber[1],CheckColorNumber[2],CheckColorNumber[3]));
                                movingImage.fillRect(numberArea[0],numberArea[1],numberArea[2],numberArea[3]);
                                movingImage.setColor(new Color(CheckColorDate[0],CheckColorDate[1],CheckColorDate[2],CheckColorDate[3]));
                                movingImage.fillRect(dateArea[0],dateArea[1],dateArea[2],dateArea[3]);
                                movingImage.setColor(new Color(CheckColorAmount[0],CheckColorAmount[1],CheckColorAmount[2],CheckColorAmount[3]));
                                movingImage.fillRect(amountArea[0],amountArea[1],amountArea[2],amountArea[3]);
                                movingImage.setColor(new Color(CheckColorName[0],CheckColorName[1],CheckColorName[2],CheckColorName[3]));
                                movingImage.fillRect(nameArea[0],nameArea[1],nameArea[2],nameArea[3]);
                                overlay = img.DrawRectangle(overlay,CheckCoordsZero,CheckColorZero);
                            }
                        }
                        cursordragging = false;
                    }
                    BufferedImage combined = new BufferedImage(w_o,h_o,BufferedImage.TYPE_INT_ARGB);
                    Graphics g = combined.getGraphics();
                    g.drawImage(image1, 0, 0, null);
                    g.drawImage(overlay, 0, 0, null);
                    image1 = combined;
                }
                BufferedImage resizedImage = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_ARGB); 
                Graphics2D g = resizedImage.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(image1, 0, 0, new_width, new_height, null);
                g.dispose();
                image2 = resizedImage;
                ImageIcon imageIcon = new ImageIcon(image2);
                jLabelImage.setIcon(imageIcon);
            }
            else
            {
                imageDisplayed = false;
            }
            jLabelPage.setText("Page " + Integer.toString(displayedNumber[CurrentPage]) + " of " + Integer.toString(displayedTotal));
        }
    }
    
    //Clears the displayed image and sets the noDoc variable to false indicating
    //that there is no document opened
    static void ClearDisplay()
    {
        jLabelImage.setIcon(null);
        jLabelPage.setText("");
        noDoc = true;
        try
        {
            filmStripLoaded = false;
            DefaultListModel model = new DefaultListModel();
            model.addElement("");
            jFilmStrip.clearSelection();
            jFilmStrip.setModel(model);
            document.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        jSliderZoom.setEnabled(false);
        jCheckBoxScale.setEnabled(false);
    }
    
    //Uses OCR to retrieve the text from specified sections of an image
    static void ReadCheck()
    {
        float scale = 1.2f;
        CheckImgNumber = img.RemoveBackground(img.CropImage(imageOCR, CheckCoordsNumber,scale),tol);
        if (CheckImgNumber != null)
        {
            CheckImgDate = img.RemoveBackground(img.CropImage(imageOCR, CheckCoordsDate,scale),tol);
            CheckImgAmount = img.RemoveBackground(img.CropImage(imageOCR, CheckCoordsAmount,scale),tol);
            CheckImgName = img.RemoveBackground(img.CropImage(imageOCR, CheckCoordsName,scale),tol);
        }
        CheckNumber = txt.OCR(CheckImgNumber,"num");
        jTextFieldC_Number.setText(txt.FixNumber(CheckNumber));
        
        CheckName = txt.OCR(CheckImgName,"nam");
        jTextFieldC_Name.setText(txt.FixName(CheckName));
        
        CheckDate = txt.OCR(CheckImgDate,"dat");
        jTextFieldC_Date.setText(txt.FixDate(CheckDate));
        
        CheckAmount = txt.OCR(CheckImgAmount,"amt");
        jTextFieldC_Amount.setText(txt.FixAmount(CheckAmount));
    }
    
    //Displays the processed images and the retrieved text in a new window
    //so the user can verify proper operation or see needed adjustments
    static void ViewFields()
    {
        JFrame frame = new JFrame();
        
        frame.setType(java.awt.Window.Type.UTILITY);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Raw Results");
        frame.setResizable(false);
        
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        frame.getContentPane().add(new JLabel(new ImageIcon(CheckImgNumber)));
        frame.getContentPane().add(new JLabel(CheckNumber));
        frame.getContentPane().add(new JLabel(new ImageIcon(CheckImgDate)));
        frame.getContentPane().add(new JLabel(CheckDate));
        frame.getContentPane().add(new JLabel(new ImageIcon(CheckImgAmount)));
        frame.getContentPane().add(new JLabel(CheckAmount));
        frame.getContentPane().add(new JLabel(new ImageIcon(CheckImgName)));
        frame.getContentPane().add(new JLabel(CheckName));
        frame.pack();
        frame.setVisible(true);
    }
    
    //Opens an email in outlook with the saved document attached so you can email it
    void EmailDocument()
    {
        if (emailAfterSaving == true && SelectedDocumentType == 0)
        {
            OutlookEmailHandler.sendDocumentEmail(lastSavedDocumentPath);
        }
    }
    
    // Saves the loaded document with the provided file names and paths
    void SaveDocument(String path)
    {
        Path Source = Paths.get(Main.TempDirectory + "\\" + Main.SourceName);
        try
        {
            Files.copy(Source, Paths.get(path),REPLACE_EXISTING);
            Files.delete(Source);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        lastSavedDocumentPath = path;
        EmailDocument();
    }
    
    void SaveDocument(String path, String fileName)
    {
        String[] paths = {path};
        String[] fileNames = {fileName};
        SaveDocument(paths, fileNames);
    }
    
    void SaveDocument(String[] paths, String[] fileNames)
    {
        int length = paths.length;
        if (length > fileNames.length)
        {
            length = fileNames.length;
        }
        if (length > 0)
        {
            Path Source = Paths.get(Main.TempDirectory + "\\" + Main.SourceName);
            int i = 0;
            while (i < length)
            {
                if (paths[i] != null && fileNames[i] != null)
                {
                    Path dest = Paths.get(paths[i] + "\\" + fileNames[i]);
                    File Path = new File(paths[i]);
                    File Dest = new File(paths[i] + "\\" + fileNames[i]);
                    String ConflictName = fileNames[i].substring(0, fileNames[i].length()-4);
                    String ConflictPathString = paths[i];
                    ConflictHandler con = new ConflictHandler();

                    if (!Path.exists())
                    {
                        Path.mkdirs();
                    }
                    if (Dest.exists())
                    {
                        con.ConflictHandler(ConflictName, ConflictPathString);
                    }
                    else 
                    {
                        try
                        {
                            Files.copy(Source, dest);
                        }
                        catch (IOException ex)
                        {
                            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (i == length-1)
                    {
                        try
                        {
                            Files.delete(Source);
                        }
                        catch (IOException ex)
                        {
                            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                i++;
            }
            lastSavedDocumentPath = paths[i-1] + "\\" + fileNames[i-1];
        }
        EmailDocument();
    }
    
    //Binds certain keys on the keyboard to interact with the program
    void BindKeys()
    {
        KeyStroke CONTROL_Y_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK);
        KeyStroke CONTROL_Z_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK);
        KeyStroke CONTROL_P_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK);
        KeyStroke CONTROL_W_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK);
        KeyStroke ENTER_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        KeyStroke LEFT_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        KeyStroke RIGHT_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        KeyStroke UP_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        KeyStroke DOWN_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        KeyStroke DELETE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        
        ActionMap Amap = jButtonProceed.getActionMap();
        
        Amap.put("redo_button", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                restoreFromRedoLog();
            }
         });
        Amap.put("undo_button", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                restoreFromChangeLog();
            }
         });
        Amap.put("print_button", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                print();
            }
         });
        Amap.put("close_button", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                documentStillOpenWarning();
            }
         });
        Amap.put("proceed_button", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                jButtonProceedActionPerformed(e);
            }
         });
        Amap.put("left_button", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                jButtonPrevActionPerformed(e);
            }
         });
        Amap.put("right_button", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                jButtonNextActionPerformed(e);
            }
         });
       Amap.put("up_button", new AbstractAction()
       {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (pageOperationInProgress == false)
                {
                    pageOperationInProgress = true;
                    jButtonRotateCWActionPerformed(e);
                    new Thread(new ButtonDelay()).start();
                }
            }
         });
        Amap.put("down_button", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (pageOperationInProgress == false)
                {
                    pageOperationInProgress = true;
                    jButtonRotateCCWActionPerformed(e);
                    new Thread(new ButtonDelay()).start();
                }
            }
         });
        Amap.put("delete_button", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deletePage();
            }
         });
        
        InputMap ProceedImap = jButtonProceed.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        ProceedImap.put(CONTROL_Y_STROKE, "redo_button");
        ProceedImap.put(CONTROL_Z_STROKE, "undo_button");
        ProceedImap.put(CONTROL_P_STROKE, "print_button");
        ProceedImap.put(CONTROL_W_STROKE, "close_button");
        ProceedImap.put(ENTER_KEY_STROKE, "proceed_button");
        ProceedImap.put(LEFT_KEY_STROKE, "left_button");
        ProceedImap.put(RIGHT_KEY_STROKE, "right_button");
        ProceedImap.put(UP_KEY_STROKE, "up_button");
        ProceedImap.put(DOWN_KEY_STROKE, "down_button");
        ProceedImap.put(DELETE_KEY_STROKE, "delete_button");
    }
    
    //Gets data from the GUI needed to do a custom save
    void SaveCustom()
    {
        String Name = jTextFieldCS_Name.getText();
        
            boolean valid = txt.CheckNameField(Name, true);
            if (valid == true)
            {
                SaveDocument(CSDirectory, Name + ".pdf");
                jTextFieldCS_Name.setText("");
            }
    }
    
    //Gets data from the GUI needed to save a check scan
    void SaveCheck()
    {
        String[] paths = {null, null};
        String[] names = {null, null};
        String Number = jTextFieldC_Number.getText();
        String Amount = jTextFieldC_Amount.getText();
        String Name = jTextFieldC_Name.getText();
        String Date = jTextFieldC_Date.getText();

        boolean valid = txt.CheckDateField(Date, true);
        if (valid == true)
        {
            valid = txt.CheckNumberField(Number, true);
        }
        if (valid == true)
        {
            valid = txt.CheckAmountField(Amount, true);
        }
        if (valid == true)
        {
            valid = txt.CheckNameField(Name, true);
        }
        if (valid == true)
        {
            Date = Date.replaceAll("/","-");
            if (!prevDate.equals("") && !prevDate.equals(Date))
            {
                int option =  JOptionPane.showConfirmDialog(rootPane, "The date has changed, do you want to use the new value?", "Date Changed", JOptionPane.YES_NO_OPTION);
                
                if (option == JOptionPane.NO_OPTION)
                {
                    Date = prevDate;
                }
                else
                {
                    prevDate = Date;
                }
            }
            String[] dateArray = Date.split("-");
            String month = dateArray[0];
            String day = dateArray[1];
            String year = dateArray[2];

            int FiscalYear = date.GetFiscalYear(Integer.parseInt(month), Integer.parseInt(year));

            if (jCheckBoxC_Void.isSelected())
            {
                names[0] = "(void) " + Name + " (" + Date + ") $" + Amount +".pdf";
                names[1] = Number + " (void).pdf";
            }
            else
            {
                names[0] = Name + " (" + Date + ") $" + Amount +".pdf";
                names[1] = Number + ".pdf";
            }
            paths[0] = Main.CheckDir + "\\20" + Integer.toString(FiscalYear - 1) + "-" + "20" + Integer.toString(FiscalYear) + " Vendor Name File";
            paths[1] = Main.CheckDir + "\\20" + Integer.toString(FiscalYear - 1) + "-" + "20" + Integer.toString(FiscalYear) + " Check Number File" + "\\" + Number.substring(0,3) + "---";
            
            int numberInt = Integer.parseInt(Number);
            String Number2;
            if (numberInt < refNumber)
            {
                refNumber = numberInt;
                numberInt--;
                Number2 = Integer.toString(numberInt);
            }
            else
            {
                refNumber = numberInt;
                numberInt++;
                Number2 = Integer.toString(numberInt);
            }
            jTextFieldC_Number.setText("");
            jTextFieldC_Amount.setText("");
            jTextFieldC_Name.setText("");
            jTextFieldC_Date.setText(Date);
            jCheckBoxC_Void.setSelected(false);
            int numberException = 0;
            if ((lastCheckNumber+1) == Integer.parseInt(Number) || lastCheckNumber == 0) {
                SaveDocument(paths, names);
            } else {
                numberException = JOptionPane.showConfirmDialog(null,"The number for this check is not sequential to the previous one.\n"
                        + "                                          The number you provided is " + Integer.parseInt(Number) + ", The last check number was " + lastCheckNumber + ".\n Do you want to proceed?", "Number Problem",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                switch(numberException) {
                    case JOptionPane.YES_OPTION:
                        SaveDocument(paths, names);
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                }
            }
            lastCheckNumber = Integer.parseInt(Number);
        }
    }
    
    //Gets data from the GUI needed to save a credit card scan
    void SaveCreditCard()
    {
        String[] paths = {null, null};
        String[] names = {null, null};
        String path;
        String name;
        int Index = jComboCC_Names.getSelectedIndex();
        String NameString = jComboCC_Names.getSelectedItem().toString();
        int ID = Main.CreditCardHolderIds[Index];
        int AppId = Main.CreditCardApproverIds[Index];
        String Date = jTextFieldCC_Date.getText();
        String business = "";
        boolean Approved = jCheckBoxCC_Approved.isSelected();   

        while(NameString.substring(NameString.length()-1,NameString.length()).equals(" "))
        {
            NameString = NameString.substring(0,NameString.length()-1);
        }
        
        boolean valid = txt.CheckDateField(Date, true);
        if (valid == true)
        {
            Date = Date.replaceAll("/","-");
            String[] dateArray = Date.split("-");
            String month = dateArray[0];
            String day = dateArray[1];
            String year = dateArray[2];
            String app = "0";
            boolean duplicate = false;
            int y = Integer.parseInt(year);
            int m = Integer.parseInt(month);
            if(Approved == true)
            {
                app = "1";
            }
            String AppName = "";
            if (!jCheckBoxNewMonth.isSelected())
            {
                db.openDatabase();
                if(db.checkATableExists(year, month, day) == false)
                {
                    db.createApprovalsTable(year, month, day);
                    if(db.checkListTableExists() == false)
                    {
                        db.createListTable();
                    }
                    db.openListTable();
                    int[] array = DatabaseHandler.list.getIntColumn(0);
                    int nextID;
                    if(array[0] == 0)
                    {
                        nextID = 1;
                    }
                    else
                    {
                        int result = 1;
                        boolean done = false;
                        Arrays.sort(array);
                        int i = 1;
                        while (done == false)
                        {
                            if(Arrays.binarySearch(array, i) < 0)
                            {
                                result = i;
                                done = true;
                            }
                            else
                            {
                                i++;
                            }
                        }
                        nextID = result;
                    }
                    String[] d = {Integer.toString(nextID), year, month, day};
                    DatabaseHandler.list.addRow(d);
                }
                db.openApprovalTable(year, month, day);
                db.openCardHolderTable();
                db.openCardApproverTable();
                AppName = DatabaseHandler.ca.lookup(AppId, 2) + ", " + DatabaseHandler.ca.lookup(AppId, 3);
                String tmpString = DatabaseHandler.ca.lookup(AppId, 4);
                String signed = "Signed";
                if (!tmpString.equals(" ") && !tmpString.equals(""))
                {
                    AppName = AppName + " " + tmpString;
                }
                if (!jCheckBoxCC_Approved.isSelected())
                {
                    signed = "Pending";
                }
                if (jCheckBoxFiscalEnd.isSelected())
                {
                    business = jComboBoxBusiness.getSelectedItem().toString();
                }
                else
                {
                    business = "Normal Business";
                }
                String Vals[] = {Integer.toString(ID), Integer.toString(AppId), NameString, AppName, app, signed, "1", business};

                if (DatabaseHandler.a.checkStatement(NameString))
                {
                    int duplicateEntry = JOptionPane.showConfirmDialog(jMenuDocument, "There is already a statement entered for " + NameString + ".\n Is this a second statement?", "Duplicate Entry",JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

                    if (duplicateEntry == JOptionPane.YES_OPTION)
                    {
                        Vals[6] = "2";
                        duplicate = true;
                    }
                }
                DatabaseHandler.a.addRow(Vals);
                db.closeDatabase();
            }
            if (jCheckBoxNewMonth.isSelected())
            {
                path = Main.CreditCardDir + "\\CC STATEMENT 20" + year;
                name = "WWU Statement " + Date + ".pdf";
                SaveDocument(path, name);
            }
            else
            {
                if (Approved == false)
                {
                    if (duplicate)
                    {
                        names[0] = Date + " (2).pdf";
                    }
                    else
                    {
                        names[0] = Date + ".pdf";
                    }
                    paths[0] = Main.CreditCardDir + "\\CC STATEMENT 20" + year + "\\" + NameString;
                    names[1] = NameString + ".pdf";
                    paths[1] = Main.CreditCardDir + "\\CC STATEMENTS in need of approval\\" + Date + "\\" + AppName;
                    SaveDocument(paths, names);
                }
                else
                {
                    if (duplicate)
                    {
                        name = Date + " (2).pdf";
                    }
                    else
                    {
                        name = Date + ".pdf";
                    }
                    path = Main.CreditCardDir + "\\CC STATEMENT 20" + year + "\\" + NameString;
                    SaveDocument(path, name);
                }
            }
            duplicate = false;
            jCheckBoxCC_Approved.setSelected(false);
            jCheckBoxNewMonth.setSelected(false);
            jCheckBoxCC_Approved.setEnabled(true);
            jComboCC_Names.setEnabled(true);
            jCheckBoxFiscalEnd.setSelected(true);
            jComboBoxBusiness.setEnabled(false);
        }
    }
    
    //Gets data from the GUI needed to save a cash receipt scan
    void SaveCashReceipt()
    {
        String[] paths = {null, null};
        String[] names = {null, null};
        
        String Date = jTextFieldCR_Date.getText();
        String Number = jTextFieldCR_Number.getText();

        boolean valid = txt.CheckDateField(Date, true);
        if (valid == true)
        {valid = txt.CheckNumberField(Number, true);}
        if (valid == true)
        {
            Date = Date.replaceAll("/","-");
            String[] dateArray = Date.split("-");
            String month = dateArray[0];
            String day = dateArray[1];
            String year = dateArray[2];

            int FiscalYear = date.GetFiscalYear(Integer.parseInt(month), Integer.parseInt(year));
            String MonthName = date.GetMonthName(Integer.parseInt(month));
            String statement = "";
            if (jRadioCR_ARStatement.isSelected())
            {statement = " AR";}
            else if (jRadioCR_GLStatement.isSelected())
            {statement = " GL";}

            paths[0] = Main.CashReceiptDir + "\\20" + Integer.toString(FiscalYear - 1) + "-" + "20" + Integer.toString(FiscalYear) + "\\DS " + year + " " + month + " " + MonthName + "\\";
            paths[1] = Main.JournalEntriesDir + "\\" + Number.substring(0,2) + "---\\";
            names[0] = "DS" + Date + statement + ".pdf";
            names[1] = Number + statement + ".pdf";

            jTextFieldCR_Number.setText("");
            
            SaveDocument(paths, names);
        }
    }
    
    //Gets data from the GUI needed to save a journal entry scan
    void SaveJournal()
    {
        String path;
        String name;
        String Number = jTextFieldJE_Number.getText();         
        boolean valid = txt.CheckNumberField(Number, true);
        String Number2;
        if (valid == true)
        {
            int numberInt = Integer.parseInt(Number);
            if (numberInt < refNumber)
            {
                refNumber = numberInt;
                numberInt--;
                Number2 = Integer.toString(numberInt);
            }
            else
            {
                refNumber = numberInt;
                numberInt++;
                Number2 = Integer.toString(numberInt);
            }
            name = Number + ".pdf";
            path = Main.JournalEntriesDir + "\\" + Number.substring(0,2) + "---";
            jTextFieldJE_Number.setText(Number2);
            SaveDocument(path, name);
        }
    }
    
    //Gets data from the GUI needed to save a handwritten check scan
    void SaveHandCheck()
    {
        String[] paths = {null, null};
        String[] names = {null, null};
        String Number = jTextFieldHC_Number.getText();
        String Amount = jTextFieldHC_Amount.getText();
        String Name = jTextFieldHC_Name.getText();
        String Date = jTextFieldHC_Date.getText();
        boolean valid = txt.CheckDateField(Date, true);
        if (valid == true)
        {valid = txt.CheckAmountField(Amount, true);}
        if (valid == true)
        {valid = txt.CheckNameField(Name, true);}
        if (valid == true)
        {valid = txt.CheckNumberField(Number, true);}
        if (valid == true)
        {   
            Date = Date.replaceAll("/","-");
            String[] dateArray = Date.split("-");
            String month = dateArray[0];
            String day = dateArray[1];
            String year = dateArray[2];

            int FiscalYear = date.GetFiscalYear(Integer.parseInt(month), Integer.parseInt(year));

            String Campus = "";
            if(jRadioHC_CollegePlace.isSelected())
            {
                Campus = "College Place";
            }
            else if(jRadioHC_Billings.isSelected())
            {
                Campus = "Billings";
            }
            else if(jRadioHC_Missoula.isSelected())
            {
                Campus = "Missoula";
            }
            else if(jRadioHC_Portland.isSelected())
            {
                Campus = "Portland";
            }
            else if(jRadioHC_Rosario.isSelected())
            {
                Campus = "Rosario";
            }
            else
            {
                valid = false;
                JOptionPane.showMessageDialog(null, "No campus has been selected.\n" + "Please select the campus that the check corresponds to before proceeding", "No Campus Selected", JOptionPane.ERROR_MESSAGE);
            }
            if (valid == true)
            {
                if (jCheckBoxHC_Void.isSelected())
                {        
                    names[0] = "(void) " + Name + " (" + Date + ") $" + Amount +".pdf";
                    names[1] = Number + " (void).pdf";
                }
                else
                {
                    names[0] = Name + " (" + Date + ") $" + Amount +".pdf";
                    names[1] = Number + ".pdf";
                }   
                paths[0] = Main.HandwrittenChecksDir + "\\Handwritten Checks 20" + Integer.toString(FiscalYear - 1) + "-20" + Integer.toString(FiscalYear) + "\\" + Campus + " - Vendor Name File";
                paths[1] = Main.HandwrittenChecksDir + "\\Handwritten Checks 20" + Integer.toString(FiscalYear - 1) + "-20" + Integer.toString(FiscalYear) + "\\" + Campus + " - Check Number File";

                int numberInt = Integer.parseInt(Number);
                String Number2;
                if (numberInt < refNumber)
                {
                    refNumber = numberInt;
                    numberInt--;
                    Number2 = Integer.toString(numberInt);
                }
                else
                {
                    refNumber = numberInt;
                    numberInt++;
                    Number2 = Integer.toString(numberInt);
                }

                jTextFieldHC_Number.setText(Number2);
                jTextFieldHC_Amount.setText("");
                jTextFieldHC_Name.setText("");
                jTextFieldHC_Date.setText("");
                jCheckBoxHC_Void.setSelected(false);
            }
            SaveDocument(paths, names);
        }
    }
    
    //Gets data from the GUI needed to save an ACH scan
    void SaveACH()
    {
        if (!ExpenseNames.isEmpty() && ExpenseNames.contains("Add Name"))
        {
            JOptionPane.showMessageDialog(jMenuDocument, "You have not provided all the names needed\nfor the Expense Reports!\n\nAdd them to continue.","Missing Names",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            String[] paths = {null, null};
            String[] names = {null, null};

            String Date = jTextFieldACH_Date.getText();
            String Number = jTextFieldACH_Number.getText();

            boolean valid = txt.CheckDateField(Date, true);
            if (valid == true)
            {valid = txt.CheckNumberField(Number, true);}
            if (valid == true)
            {
                Date = Date.replaceAll("/","-");
                names[0] = "REIMB" + " " + Date + ".pdf";
                paths[0] = Main.ACHTransactionsDir + "\\";
                names[1] = Number + ".pdf";
                paths[1] = Main.JournalEntriesDir + "\\" + Number.substring(0,2) + "---\\";

                for (int i = 0; i < FlaggedPages.size(); i++)
                {
                    try
                    {
                        int end = 0;
                        int size = FlaggedPages.size() - 1;
                        PDDocument extDoc = new PDDocument();
                        if (i < size)
                        {
                            end = Integer.parseInt(FlaggedPages.get(i + 1).toString());
                            System.out.println(end - 1);
                        }
                        else
                        {
                            end = displayedTotal;
                        }
                        PageExtractor pageExt = new PageExtractor(document, Integer.parseInt(FlaggedPages.get(i).toString()) + 1,end);
                        extDoc = pageExt.extract();
                        File expenseDir = new File(Main.ExpenseReportsDir + "//" + ExpenseNames.get(i));
                        File expenseFile = new File(Main.ExpenseReportsDir + "//" + ExpenseNames.get(i) + "//" + Date + ".pdf");
                        if (!expenseDir.exists())
                        {
                            expenseDir.mkdirs();
                        }
                        if (expenseFile.exists())
                        {
                            File expenseFile2 = new File(Main.ExpenseReportsDir + "//" + ExpenseNames.get(i) + "//" + Date + "(2).pdf");
                            if (expenseFile2.exists())
                            {
                                File expenseFile3 = new File(Main.ExpenseReportsDir + "//" + ExpenseNames.get(i) + "//" + Date + "(3).pdf");
                                extDoc.save(expenseFile3);
                            }
                            else
                            {
                                extDoc.save(expenseFile2);
                            }
                        }
                        else
                        {
                            extDoc.save(expenseFile);
                        }
                        extDoc.close();
                    }
                    catch (IOException | COSVisitorException ex)
                    {
                        Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                jTextFieldACH_Number.setText("");
                SaveDocument(paths, names);
            }
        }
    }
    
    void SaveEdit()
    {
        SaveDocument(EditPath);
    }
    
    //Removes images from the buffer that are outside of the buffer window
    static void cleanBuffer()
    {
        int ref1 = CurrentPage-halfBuffer-1;
        int ref2 = CurrentPage+halfBuffer+1;
        if(ref1 >= 0)
        {
            Arrays.fill(images,0,ref1,null);
        }
        if(ref2 < Main.maxPages)
        {
            Arrays.fill(images,ref2,Main.maxPages,null);
        }
    }
    
    void clearMemory()
    {
        Arrays.fill(images, null);
        System.gc();
    }
    
    //Checks the validity of GUI fields and issues error messages in the case
    //of an invalid field
    public static void checkFieldsValidity()
    {
        boolean valid = true;
        
        switch (SelectedDocumentType)
        {   
            //Custom Save
            case 0: 
                valid = txt.CheckNameField(jTextFieldCS_Name.getText(), false);
                break;
            //Check
            case 1:
                valid = txt.CheckNumberField(jTextFieldC_Number.getText(), false);
                if (valid == true)
                {
                    valid = txt.CheckDateField(jTextFieldC_Date.getText(), false);
                    if (valid == true)
                    {
                        valid = txt.CheckAmountField(jTextFieldC_Amount.getText(), false);
                        if (valid == true)
                        {
                            valid = txt.CheckNameField(jTextFieldC_Name.getText(), false);
                        }
                    }
                }
                break;
            //Credit Card
            case 2:
                valid = txt.CheckDateField(jTextFieldCC_Date.getText(), false);
                break;
            //Journal Entry
            case 3:
                valid = txt.CheckNumberField(jTextFieldJE_Number.getText(), false);
                break;
            //Cash Receipt
            case 4:
                valid = txt.CheckDateField(jTextFieldCR_Date.getText(), false);
                if (valid == true)
                {
                    valid = txt.CheckNumberField(jTextFieldCR_Number.getText(), false);
                }
                break;
            //Handwriten Check
            case 5:
                valid = txt.CheckNumberField(jTextFieldHC_Number.getText(), false);
                if (valid == true)
                {
                    valid = txt.CheckDateField(jTextFieldHC_Date.getText(), false);
                    if (valid == true)
                    {
                        valid = txt.CheckAmountField(jTextFieldHC_Amount.getText(), false);
                        if (valid == true)
                        {
                            valid = txt.CheckNameField(jTextFieldHC_Name.getText(), false);
                            if (valid == true)
                            {
                                valid = !jRadioButtonDefault.isSelected();
                            }
                        }
                    }
                }
                break;
            //Group Post
            case 6:
                break;
            //ACH
            case 7:
                valid = txt.CheckDateField(jTextFieldACH_Date.getText(), false);
                if (valid == true)
                {
                    valid = txt.CheckNumberField(jTextFieldACH_Number.getText(), false);
                }
                break;               
        }
        fieldsValid = valid;
        updateProceedColor();
    }
    
    static int getPageListIndex(int find)
    {
        int i = 0;
        int index = 0;
        boolean done = false;
        while (i < pageList.length && done == false)
        {
            if(pageList[i] == find)
            {
                index = i;
                done = true;
            }
            i++;
        }
        return index;
    }
    
    //Removes the selected page from the list but does not delete from the pfd
    //until the document is saved
    void deletePage()
    {
        if (noDoc == false && deleteDone == true)
        {
            deleteDone = false; 
            changesMade = true;
            pageDeleted[CurrentPage] = true;
            addChangeLogEntry(CurrentPage, 3);
            boolean LastPageDeleted;
            int pagesRemaining = getPageListIndex(LastPage);
            LastPageDeleted = CurrentPage == LastPage;
            
            int index = getPageListIndex(CurrentPage);
            DefaultListModel model = (DefaultListModel) jFilmStrip.getModel();
            model.remove(index);
            
            int i;
            for (i = index; i < getPageListIndex(LastPage); i++)
            {
                pageList[i] = pageList[i+1];
            }
            pageList[i] = 0;
            for (i = CurrentPage; i <= LastPage; i++)
            {
                displayedNumber[i]--;
                if (pageDeleted[i] == true)
                {
                    displayedNumber[i] = 0;
                }
            }   
            displayedTotal--;
            
            if (pagesRemaining == 0)
            {
                DeleteDoc();
            }
            else
            {
                pageRotation[CurrentPage] = CurrentRotation;
                if (CurrentPage < LastPage)
                {
                    CurrentPage = pageList[index];
                }
                else
                {
                    CurrentPage = pageList[index-1];
                }
            }
            DisplayImage();
            if (LastPageDeleted == true)
            {
                LastPage = CurrentPage;
            }
            deleteDone = true;
        }
    }
    
    //Used to rotate the current previewed page clock wise
    void rotateCW()
    {
        if (noDoc == false && rotateInProgress == false)
        {
            rotateInProgress = true;
            images[CurrentPage] = img.rotateCW(images[CurrentPage]);
            DisplayImage();
            if (CurrentRotation == 360)
            {
                CurrentRotation = 90;
            }
            else if (CurrentRotation == 270)
            {
                CurrentRotation = 0;
            }
            else
            {
                CurrentRotation = CurrentRotation + 90;
            }
            addChangeLogEntry(CurrentPage, 1);
            new Thread(new updateFilmStripImageRotation()).start();
            rotateInProgress = false;
            try {
                Thread.sleep(300);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Used to rotate the current previewed page counter clock wise
    void rotateCCW()
    {
        if (noDoc == false && rotateInProgress == false)
        {
            rotateInProgress = true;
            images[CurrentPage] = img.rotateCCW(images[CurrentPage]);
            DisplayImage();
            if (CurrentRotation >= 90)
            {
                CurrentRotation = CurrentRotation - 90;
            }
            else
            {
                CurrentRotation = 270;
            }
            addChangeLogEntry(CurrentPage, 2);
            new Thread(new updateFilmStripImageRotation()).start();
            rotateInProgress = false;
            try {
                Thread.sleep(300);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Used to update the filmstrip icons when a page is rotated
    void updateFilmStripImageRotation()
    {
        try
        {
            DefaultListModel model = (DefaultListModel) jFilmStrip.getModel();
            int index = getPageListIndex(CurrentPage);
            int ref;
            if (CurrentRotation == 90 || CurrentRotation == 270)
            {
                ref = MainGui.pages.get(CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,50).getHeight();
            }
            else
            {
                ref = MainGui.pages.get(CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,50).getWidth();
            }
            int dpi = (int)(50*((double)filmStripWidth/(double)ref));
            BufferedImage m = pages.get(CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,dpi);
            if (CurrentRotation == 90)
            {
                m = img.rotateCW(m);
            }
            else if (CurrentRotation == 270)
            {
                m = img.rotateCCW(m);
            }
            else if (CurrentRotation == 180)
            {
                m = img.rotateCW(img.rotateCW(m));
            }
            model.add(index, new ImageIcon(m));
            model.remove(index+1);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void addChangeLogEntry(int page, int value)
    {
        if (restoringFromLog == false)
        {
            redoLogPointer = 0;
            if (changeLogPointer >= 99)
            {
                changeLogPointer = 99;
                for (int i = 0; i < 99; i++)
                {
                    changeLog[i][0] = changeLog[1+i][0];
                    changeLog[i][1] = changeLog[1+i][1];
                }
                changeLog[99][0] = page;
                changeLog[99][1] = value;
            }
            else
            {
                changeLogPointer++;
                changeLog[changeLogPointer][0] = page;
                changeLog[changeLogPointer][1] = value;     
            }
        }
    }
    
    void addRedoLogEntry(int page, int value)
    {
        if (redoLogPointer >= 99)
        {
            redoLogPointer = 99;
            for (int i = 0; i < 99; i++)
            {
                redoLog[i][0] = redoLog[1+i][0];
                redoLog[i][1] = redoLog[1+i][1];
            }
            redoLog[99][0] = page;
            redoLog[99][1] = value;
        }
        else
        {
            redoLogPointer++;
            redoLog[redoLogPointer][0] = page;
            redoLog[redoLogPointer][1] = value;   
        }
    }
    
    //Undoes the last change made to the document
    void restoreFromChangeLog()
    {
        if (changeLogPointer > 0)
        {
            restoringFromLog = true;
            CurrentPage = changeLog[changeLogPointer][0];
            addRedoLogEntry(CurrentPage, changeLog[changeLogPointer][1]);
            if (changeLog[changeLogPointer][1] == 1)
            {
                rotateCCW();
            }
            else if (changeLog[changeLogPointer][1] == 2)
            {
                rotateCW();
            }
            else if (changeLog[changeLogPointer][1] == 3)
            {
                pageDeleted[CurrentPage] = false;
                displayedTotal = getPageListIndex(LastPage)+2;
                DefaultListModel model = (DefaultListModel) jFilmStrip.getModel();
                int ref = 0;
                try
                {
                    ref = pages.get(CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,50).getWidth();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                int dpi = (int)(50*((double)MainGui.filmStripWidth/(double)ref));
                if (CurrentPage > LastPage)
                {
                        LastPage = CurrentPage;
                        displayedNumber[CurrentPage] = displayedTotal;
                        pageList[displayedTotal-1] = CurrentPage;
                    try
                    {
                        model.addElement(new ImageIcon(MainGui.pages.get(CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,dpi)));
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    boolean foundIndex = false;
                    int index = 0;
                    for (int i = 0; foundIndex == false; i++)
                    {
                        if (pageList[i] > CurrentPage)
                        {
                            foundIndex = true;
                            index = i;
                        }
                    }
                    displayedNumber[CurrentPage] = index+1;
                    for (int i = CurrentPage+1; i <= TotalPages; i++)
                    {
                        if (pageDeleted[i] == false)
                        {
                            displayedNumber[i]++;
                        }
                    }
                    for (int i = displayedTotal-1; i > index; i--)
                    {
                        pageList[i] = pageList[i-1];
                    }
                    pageList[index] = CurrentPage;
                    try
                    {
                        model.add(index, new ImageIcon(MainGui.pages.get(CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,dpi)));
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                DisplayImage();
            }
            if (changeLogPointer > 0)
            {
                changeLogPointer--;
            }
            else
            {
                changeLogPointer = 0;
            }
            restoringFromLog = false;
        }
    }
    
    void restoreFromRedoLog()
    {
        if (redoLogPointer > 0)
        {
            restoringFromLog = true;
            if (redoLog[changeLogPointer][1] == 1)
            {
                rotateCW();
            }
            else if (redoLog[changeLogPointer][1] == 2)
            {
                rotateCCW();
            }
            else if (redoLog[changeLogPointer][1] == 3)
            {
                deletePage();
            }
            if (redoLogPointer > 0)
            {
                redoLogPointer--;
            }
            else
            {
                redoLogPointer = 0;
            }
            restoringFromLog = false;
        }
    }
    
    boolean documentStillOpenWarning()
    {
        boolean documentStillOpen = documentExists;
        if (documentExists)
        {
            int n = JOptionPane.showConfirmDialog(
            null,
            "Would you like to close the document and loose any unsaved data?\n",
            "Document Still Open",
            JOptionPane.YES_NO_OPTION);
            if (n == 0)
            {
                DeleteDoc();
                jFilmStrip.removeAll();
                documentStillOpen = false;
            }
        }
        return documentStillOpen;
    }
    
    void closeProceedure(boolean exit)
    {
        if (documentStillOpenWarning())
        {
            setVisible(true);
        }
        else if (exit == true)
        {
            if (exiting == true)
            {
                System.exit(0);
            }
            else
            {
                exiting = true;
            }
        }
        else
        {
            setVisible(false);
            exiting = false;
        }
    }
    
    void print()
    {
        if (documentExists)
        {
            File doc = new File(Main.TempDirectory + "\\" + Main.SourceName);
            PDDocument printDoc;
            try
            {
                printDoc = PDDocument.load(doc);
                printDoc.print();
            }
            catch (    IOException | PrinterException ex)
            {
                Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ButtonGroupDocumentType = new javax.swing.ButtonGroup();
        ButtonGroupCashReceipt = new javax.swing.ButtonGroup();
        buttonGroupHandCheck = new javax.swing.ButtonGroup();
        buttonGroupGroupPost = new javax.swing.ButtonGroup();
        buttonGroupPrint = new javax.swing.ButtonGroup();
        buttonGroupInsert = new javax.swing.ButtonGroup();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jSliderZoom = new javax.swing.JSlider();
        jButtonProceed = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jButtonPrev = new javax.swing.JButton();
        jLabelPage = new javax.swing.JLabel();
        jButtonRotateCCW = new javax.swing.JButton();
        jButtonRotateCW = new javax.swing.JButton();
        jFormattedTextFieldNumber = new javax.swing.JFormattedTextField();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabelImage = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanelFields = new javax.swing.JPanel();
        jPanelACH = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldACH_Date = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldACH_Number = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButtonSelect = new javax.swing.JButton();
        jLabelFlagged = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jTextFieldExpenseName = new javax.swing.JTextField();
        jButtonExpensePrev = new javax.swing.JButton();
        jButtonExpenseNext = new javax.swing.JButton();
        jLabelExpenseNumber = new javax.swing.JLabel();
        jButtonDeselect = new javax.swing.JButton();
        jPanelCashReceipt = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jRadioCR_ARStatement = new javax.swing.JRadioButton();
        jRadioCR_GLStatement = new javax.swing.JRadioButton();
        jTextFieldCR_Date = new javax.swing.JFormattedTextField();
        jTextFieldCR_Number = new javax.swing.JFormattedTextField();
        jLabel27 = new javax.swing.JLabel();
        jPanelCreditCard = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboCC_Names = new javax.swing.JComboBox();
        jCheckBoxCC_Approved = new javax.swing.JCheckBox();
        jTextFieldCC_Date = new javax.swing.JFormattedTextField();
        jLabel26 = new javax.swing.JLabel();
        jCheckBoxNewMonth = new javax.swing.JCheckBox();
        jCheckBoxFiscalEnd = new javax.swing.JCheckBox();
        jComboBoxBusiness = new javax.swing.JComboBox();
        jSeparator3 = new javax.swing.JSeparator();
        jPanelJournalEntry = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldJE_Number = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanelHandCheck = new javax.swing.JPanel();
        jPanelHC_Campus = new javax.swing.JPanel();
        jRadioHC_CollegePlace = new javax.swing.JRadioButton();
        jRadioHC_Billings = new javax.swing.JRadioButton();
        jRadioHC_Missoula = new javax.swing.JRadioButton();
        jRadioHC_Portland = new javax.swing.JRadioButton();
        jRadioHC_Rosario = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldHC_Name = new javax.swing.JTextField();
        jCheckBoxHC_Void = new javax.swing.JCheckBox();
        jTextFieldHC_Date = new javax.swing.JFormattedTextField();
        jTextFieldHC_Amount = new javax.swing.JFormattedTextField();
        jTextFieldHC_Number = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jRadioButtonDefault = new javax.swing.JRadioButton();
        jLabel23 = new javax.swing.JLabel();
        jPanelCustomSave = new javax.swing.JPanel();
        jButtonCS_Browse = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldCS_Name = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabelCS_Destination = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jCheckBoxEmail = new javax.swing.JCheckBox();
        jPanelCheck = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldC_Number = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldC_Date = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldC_Amount = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldC_Name = new javax.swing.JTextField();
        jCheckBoxC_Void = new javax.swing.JCheckBox();
        jButtonAdjust = new javax.swing.JButton();
        jButtonReprocess = new javax.swing.JButton();
        jButtonResults = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jPanelEdit = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jLabelEditName = new javax.swing.JLabel();
        jButtonEditBrowse = new javax.swing.JButton();
        jCheckBoxScale = new javax.swing.JCheckBox();
        jScrollFilmStrip = new javax.swing.JScrollPane();
        jFilmStrip = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuDocType = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem3 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem4 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem5 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem8 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem7 = new javax.swing.JRadioButtonMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jRadioButtonMenuItem9 = new javax.swing.JRadioButtonMenuItem();
        jMenuSettings = new javax.swing.JMenu();
        jCheckBoxMenuCheckupdate = new javax.swing.JCheckBoxMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItemEmailTemplate = new javax.swing.JMenuItem();
        jMenuItemAttachmentSize = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxFilmStrip = new javax.swing.JCheckBoxMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxUseAutomation = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemAppend = new javax.swing.JCheckBoxMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxAutoHide = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxHideOnExit = new javax.swing.JCheckBoxMenuItem();
        jMenuTools = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuDocument = new javax.swing.JMenu();
        jMenuItemUndo = new javax.swing.JMenuItem();
        jMenuItemRedo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuDeletePage = new javax.swing.JMenuItem();
        jMenuDeleteDoc = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItemPrint = new javax.swing.JMenuItem();
        jMenuApprovals = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItemAbout = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        jMenuItem10.setText("jMenuItem10");

        setTitle("Quick Save");
        setIconImage(icon.getImage());
        setMinimumSize(new java.awt.Dimension(1000, 700));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jSliderZoom.setMinimum(10);
        jSliderZoom.setMinorTickSpacing(5);
        jSliderZoom.setEnabled(false);
        jSliderZoom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderZoomStateChanged(evt);
            }
        });

        jButtonProceed.setText("Proceed");
        jButtonProceed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProceedActionPerformed(evt);
            }
        });
        jButtonProceed.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jButtonProceedPropertyChange(evt);
            }
        });

        jButtonNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QuickSave/Resources/next.png"))); // NOI18N
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jButtonPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QuickSave/Resources/prev.png"))); // NOI18N
        jButtonPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrevActionPerformed(evt);
            }
        });

        jLabelPage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPage.setText("Waiting for Document");

        jButtonRotateCCW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QuickSave/Resources/ccw.png"))); // NOI18N
        jButtonRotateCCW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotateCCWActionPerformed(evt);
            }
        });

        jButtonRotateCW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QuickSave/Resources/cw.png"))); // NOI18N
        jButtonRotateCW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotateCWActionPerformed(evt);
            }
        });

        jFormattedTextFieldNumber.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###"))));
        jFormattedTextFieldNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldNumberKeyTyped(evt);
            }
        });

        jLabel31.setText("Page Number:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jSliderZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRotateCCW, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelPage, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addGap(16, 16, 16)
                .addComponent(jFormattedTextFieldNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRotateCW, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNext, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonProceed, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFormattedTextFieldNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonProceed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSliderZoom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonRotateCCW, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonPrev, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonRotateCW, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonNext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabelPage, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jScrollPane1ComponentResized(evt);
            }
        });

        jLabelImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelImage.setDoubleBuffered(true);
        jLabelImage.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabelImageMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabelImageMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jLabelImage);

        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanelFields.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanelFields.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelFields.setName("Feilds"); // NOI18N
        jPanelFields.setLayout(new java.awt.CardLayout());

        jLabel14.setText("Date:");

        try {
            jTextFieldACH_Date.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jTextFieldACH_Date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel15.setText("Journal Number");

        jTextFieldACH_Number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("ACH");

        jButtonSelect.setText("Select Current Page");
        jButtonSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectActionPerformed(evt);
            }
        });

        jLabelFlagged.setText("0 expense reports selected");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Expense Reports"));

        jLabel24.setText("Name:");

        jTextFieldExpenseName.setEnabled(false);
        jTextFieldExpenseName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldExpenseNameKeyTyped(evt);
            }
        });

        jButtonExpensePrev.setText("Prev");
        jButtonExpensePrev.setEnabled(false);
        jButtonExpensePrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExpensePrevActionPerformed(evt);
            }
        });

        jButtonExpenseNext.setText("Next");
        jButtonExpenseNext.setEnabled(false);
        jButtonExpenseNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExpenseNextActionPerformed(evt);
            }
        });

        jLabelExpenseNumber.setText("No expense reports selected");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelExpenseNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButtonExpensePrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addComponent(jButtonExpenseNext))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldExpenseName)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelExpenseNumber)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextFieldExpenseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonExpensePrev)
                    .addComponent(jButtonExpenseNext))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        jButtonDeselect.setText("Deselect");
        jButtonDeselect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeselectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelACHLayout = new javax.swing.GroupLayout(jPanelACH);
        jPanelACH.setLayout(jPanelACHLayout);
        jPanelACHLayout.setHorizontalGroup(
            jPanelACHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelACHLayout.createSequentialGroup()
                .addGroup(jPanelACHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelACHLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelACHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelACHLayout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 14, Short.MAX_VALUE))))
                    .addGroup(jPanelACHLayout.createSequentialGroup()
                        .addGroup(jPanelACHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelACHLayout.createSequentialGroup()
                                .addComponent(jButtonSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonDeselect))
                            .addGroup(jPanelACHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldACH_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldACH_Number, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelFlagged, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelACHLayout.setVerticalGroup(
            jPanelACHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelACHLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldACH_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldACH_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelACHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSelect)
                    .addComponent(jButtonDeselect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelFlagged)
                .addGap(34, 34, 34)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jPanelFields.add(jPanelACH, "ACH");

        jPanelCashReceipt.setName("CashReceipt"); // NOI18N
        jPanelCashReceipt.setPreferredSize(new java.awt.Dimension(251, 200));

        jLabel1.setText("Date:");

        jLabel2.setText("Journal number:");

        ButtonGroupCashReceipt.add(jRadioCR_ARStatement);
        jRadioCR_ARStatement.setSelected(true);
        jRadioCR_ARStatement.setText("AR Statement");

        ButtonGroupCashReceipt.add(jRadioCR_GLStatement);
        jRadioCR_GLStatement.setText("GL Statement");

        try {
            jTextFieldCR_Date.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jTextFieldCR_Date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jTextFieldCR_Number.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jTextFieldCR_Number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Cash Receipt");

        javax.swing.GroupLayout jPanelCashReceiptLayout = new javax.swing.GroupLayout(jPanelCashReceipt);
        jPanelCashReceipt.setLayout(jPanelCashReceiptLayout);
        jPanelCashReceiptLayout.setHorizontalGroup(
            jPanelCashReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCashReceiptLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCashReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addGroup(jPanelCashReceiptLayout.createSequentialGroup()
                        .addGroup(jPanelCashReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jRadioCR_GLStatement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioCR_ARStatement, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldCR_Number, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCR_Date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCashReceiptLayout.setVerticalGroup(
            jPanelCashReceiptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCashReceiptLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCR_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCR_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jRadioCR_ARStatement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioCR_GLStatement)
                .addContainerGap(372, Short.MAX_VALUE))
        );

        jPanelFields.add(jPanelCashReceipt, "CashReceipt");

        jPanelCreditCard.setName("CreditCard"); // NOI18N
        jPanelCreditCard.setPreferredSize(new java.awt.Dimension(251, 180));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Date:");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Card Holder:");

        jCheckBoxCC_Approved.setText("Approved");

        try {
            jTextFieldCC_Date.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jTextFieldCC_Date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Credit Card Statement");

        jCheckBoxNewMonth.setText("New Month");
        jCheckBoxNewMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxNewMonthActionPerformed(evt);
            }
        });

        jCheckBoxFiscalEnd.setText("Fiscal Year End");
        jCheckBoxFiscalEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFiscalEndActionPerformed(evt);
            }
        });

        jComboBoxBusiness.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Old Business", "New Business" }));
        jComboBoxBusiness.setEnabled(false);

        javax.swing.GroupLayout jPanelCreditCardLayout = new javax.swing.GroupLayout(jPanelCreditCard);
        jPanelCreditCard.setLayout(jPanelCreditCardLayout);
        jPanelCreditCardLayout.setHorizontalGroup(
            jPanelCreditCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCreditCardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCreditCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addGroup(jPanelCreditCardLayout.createSequentialGroup()
                        .addGroup(jPanelCreditCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelCreditCardLayout.createSequentialGroup()
                                .addComponent(jCheckBoxFiscalEnd)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxBusiness, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jComboCC_Names, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCC_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBoxCC_Approved, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBoxNewMonth))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator3))
                .addContainerGap())
        );
        jPanelCreditCardLayout.setVerticalGroup(
            jPanelCreditCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCreditCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCC_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboCC_Names, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxCC_Approved)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxNewMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanelCreditCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxFiscalEnd)
                    .addComponent(jComboBoxBusiness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(318, Short.MAX_VALUE))
        );

        jPanelFields.add(jPanelCreditCard, "CreditCard");

        jPanelJournalEntry.setName("JournalEntry"); // NOI18N
        jPanelJournalEntry.setPreferredSize(new java.awt.Dimension(251, 100));

        jLabel5.setText("Journal Number:");

        jTextFieldJE_Number.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jTextFieldJE_Number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Journal Entry");

        javax.swing.GroupLayout jPanelJournalEntryLayout = new javax.swing.GroupLayout(jPanelJournalEntry);
        jPanelJournalEntry.setLayout(jPanelJournalEntryLayout);
        jPanelJournalEntryLayout.setHorizontalGroup(
            jPanelJournalEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelJournalEntryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelJournalEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addGroup(jPanelJournalEntryLayout.createSequentialGroup()
                        .addGroup(jPanelJournalEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldJE_Number, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelJournalEntryLayout.setVerticalGroup(
            jPanelJournalEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelJournalEntryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldJE_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(477, Short.MAX_VALUE))
        );

        jPanelFields.add(jPanelJournalEntry, "JournalEntry");

        jPanelHandCheck.setName("HandCheck"); // NOI18N
        jPanelHandCheck.setOpaque(false);
        jPanelHandCheck.setPreferredSize(new java.awt.Dimension(251, 450));

        jPanelHC_Campus.setBorder(javax.swing.BorderFactory.createTitledBorder("Campus"));

        buttonGroupHandCheck.add(jRadioHC_CollegePlace);
        jRadioHC_CollegePlace.setSelected(true);
        jRadioHC_CollegePlace.setText("College Place");

        buttonGroupHandCheck.add(jRadioHC_Billings);
        jRadioHC_Billings.setText("Billings");

        buttonGroupHandCheck.add(jRadioHC_Missoula);
        jRadioHC_Missoula.setText("Missoula");

        buttonGroupHandCheck.add(jRadioHC_Portland);
        jRadioHC_Portland.setText("Portland");

        buttonGroupHandCheck.add(jRadioHC_Rosario);
        jRadioHC_Rosario.setText("Rosario");

        javax.swing.GroupLayout jPanelHC_CampusLayout = new javax.swing.GroupLayout(jPanelHC_Campus);
        jPanelHC_Campus.setLayout(jPanelHC_CampusLayout);
        jPanelHC_CampusLayout.setHorizontalGroup(
            jPanelHC_CampusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHC_CampusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelHC_CampusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioHC_CollegePlace)
                    .addComponent(jRadioHC_Billings)
                    .addComponent(jRadioHC_Missoula)
                    .addComponent(jRadioHC_Portland)
                    .addComponent(jRadioHC_Rosario)))
        );
        jPanelHC_CampusLayout.setVerticalGroup(
            jPanelHC_CampusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHC_CampusLayout.createSequentialGroup()
                .addComponent(jRadioHC_CollegePlace)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioHC_Billings)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioHC_Missoula)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioHC_Portland)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioHC_Rosario))
        );

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Check Number:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Date:");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Amount:");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("VendorName:");

        jTextFieldHC_Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jCheckBoxHC_Void.setText("Void");

        try {
            jTextFieldHC_Date.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jTextFieldHC_Date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jTextFieldHC_Amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jTextFieldHC_Amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jTextFieldHC_Number.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jTextFieldHC_Number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel9.setText("$");

        buttonGroupHandCheck.add(jRadioButtonDefault);
        jRadioButtonDefault.setText("none");
        jRadioButtonDefault.setContentAreaFilled(false);
        jRadioButtonDefault.setFocusPainted(false);
        jRadioButtonDefault.setFocusable(false);
        jRadioButtonDefault.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonDefault.setMaximumSize(new java.awt.Dimension(0, 0));
        jRadioButtonDefault.setMinimumSize(new java.awt.Dimension(0, 0));
        jRadioButtonDefault.setRequestFocusEnabled(false);
        jRadioButtonDefault.setRolloverEnabled(false);
        jRadioButtonDefault.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButtonDefaultStateChanged(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Handwritten Check");

        javax.swing.GroupLayout jPanelHandCheckLayout = new javax.swing.GroupLayout(jPanelHandCheck);
        jPanelHandCheck.setLayout(jPanelHandCheckLayout);
        jPanelHandCheckLayout.setHorizontalGroup(
            jPanelHandCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHandCheckLayout.createSequentialGroup()
                .addGroup(jPanelHandCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHandCheckLayout.createSequentialGroup()
                        .addGroup(jPanelHandCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelHandCheckLayout.createSequentialGroup()
                                .addGap(121, 121, 121)
                                .addComponent(jRadioButtonDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelHandCheckLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanelHandCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldHC_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanelHandCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jCheckBoxHC_Void, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldHC_Date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextFieldHC_Number, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelHandCheckLayout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextFieldHC_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanelHC_Campus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 53, Short.MAX_VALUE))
                    .addGroup(jPanelHandCheckLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelHandCheckLayout.setVerticalGroup(
            jPanelHandCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHandCheckLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButtonDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelHC_Campus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldHC_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldHC_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHandCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldHC_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldHC_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxHC_Void)
                .addGap(99, 99, 99))
        );

        jPanelFields.add(jPanelHandCheck, "HandCheck");

        jPanelCustomSave.setName("CustomSave"); // NOI18N
        jPanelCustomSave.setPreferredSize(new java.awt.Dimension(251, 200));

        jButtonCS_Browse.setText("Browse");
        jButtonCS_Browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCS_BrowseActionPerformed(evt);
            }
        });

        jLabel11.setText("File Name:");

        jTextFieldCS_Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel12.setText("Destination:");

        jScrollPane2.setBorder(null);

        jLabelCS_Destination.setText("-");
        jLabelCS_Destination.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jScrollPane2.setViewportView(jLabelCS_Destination);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Custom Save");

        jCheckBoxEmail.setText("Email after saving");
        jCheckBoxEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxEmailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCustomSaveLayout = new javax.swing.GroupLayout(jPanelCustomSave);
        jPanelCustomSave.setLayout(jPanelCustomSaveLayout);
        jPanelCustomSaveLayout.setHorizontalGroup(
            jPanelCustomSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCustomSaveLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCustomSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelCustomSaveLayout.createSequentialGroup()
                        .addGroup(jPanelCustomSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxEmail)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonCS_Browse)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCS_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 24, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCustomSaveLayout.setVerticalGroup(
            jPanelCustomSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCustomSaveLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonCS_Browse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCS_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxEmail)
                .addContainerGap(340, Short.MAX_VALUE))
        );

        jPanelFields.add(jPanelCustomSave, "CustomSave");

        jPanelCheck.setName("Check"); // NOI18N
        jPanelCheck.setPreferredSize(new java.awt.Dimension(251, 230));

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel18.setText("Check Number:");

        jTextFieldC_Number.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jTextFieldC_Number.setFont(new java.awt.Font("Serif", 0, 11)); // NOI18N
        jTextFieldC_Number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("Date:");

        try {
            jTextFieldC_Date.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jTextFieldC_Date.setFont(new java.awt.Font("Serif", 0, 11)); // NOI18N
        jTextFieldC_Date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("Amount:");

        jLabel21.setText("$");

        jTextFieldC_Amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jTextFieldC_Amount.setFont(new java.awt.Font("Serif", 0, 11)); // NOI18N
        jTextFieldC_Amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setText("Vendor Name:");

        jTextFieldC_Name.setFont(new java.awt.Font("Serif", 0, 11)); // NOI18N
        jTextFieldC_Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jCheckBoxC_Void.setText("Void");

        jButtonAdjust.setText("Adjust Fields");
        jButtonAdjust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdjustActionPerformed(evt);
            }
        });

        jButtonReprocess.setText("Reprocess");
        jButtonReprocess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReprocessActionPerformed(evt);
            }
        });

        jButtonResults.setText("View Results");
        jButtonResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResultsActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Check");

        javax.swing.GroupLayout jPanelCheckLayout = new javax.swing.GroupLayout(jPanelCheck);
        jPanelCheck.setLayout(jPanelCheckLayout);
        jPanelCheckLayout.setHorizontalGroup(
            jPanelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCheckLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldC_Name)
                    .addGroup(jPanelCheckLayout.createSequentialGroup()
                        .addGroup(jPanelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldC_Number, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldC_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelCheckLayout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldC_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBoxC_Void)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelCheckLayout.createSequentialGroup()
                                .addGroup(jPanelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jButtonResults, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonAdjust, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonReprocess)))
                        .addGap(0, 67, Short.MAX_VALUE))
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelCheckLayout.setVerticalGroup(
            jPanelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCheckLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldC_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldC_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldC_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldC_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxC_Void)
                .addGap(18, 18, 18)
                .addGroup(jPanelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdjust)
                    .addComponent(jButtonReprocess))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonResults)
                .addGap(182, 182, 182))
        );

        jPanelFields.add(jPanelCheck, "Check");

        jPanelEdit.setName("Edit"); // NOI18N

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Edit");

        jLabel29.setText("Document Name:");

        jScrollPane4.setBorder(null);

        jLabelEditName.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jScrollPane4.setViewportView(jLabelEditName);

        jButtonEditBrowse.setText("Browse");
        jButtonEditBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditBrowseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEditLayout = new javax.swing.GroupLayout(jPanelEdit);
        jPanelEdit.setLayout(jPanelEditLayout);
        jPanelEditLayout.setHorizontalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addGroup(jPanelEditLayout.createSequentialGroup()
                        .addGroup(jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jButtonEditBrowse))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelEditLayout.setVerticalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonEditBrowse)
                .addContainerGap(415, Short.MAX_VALUE))
        );

        jPanelFields.add(jPanelEdit, "Edit");

        jScrollPane3.setViewportView(jPanelFields);

        jCheckBoxScale.setSelected(true);
        jCheckBoxScale.setText("Auto Scale");
        jCheckBoxScale.setEnabled(false);
        jCheckBoxScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxScaleActionPerformed(evt);
            }
        });

        jScrollFilmStrip.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollFilmStrip.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jScrollFilmStripComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollFilmStripComponentShown(evt);
            }
        });

        jFilmStrip.setBackground(new java.awt.Color(214, 217, 223));
        jFilmStrip.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jFilmStripValueChanged(evt);
            }
        });
        jScrollFilmStrip.setViewportView(jFilmStrip);

        jMenuDocType.setText("Document Type  ");
        ButtonGroupDocumentType.add(jMenuDocType);

        ButtonGroupDocumentType.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem1.setText("Check");
        jRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        jMenuDocType.add(jRadioButtonMenuItem1);

        ButtonGroupDocumentType.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setText("Credit Card Statement");
        jRadioButtonMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem2ActionPerformed(evt);
            }
        });
        jMenuDocType.add(jRadioButtonMenuItem2);

        ButtonGroupDocumentType.add(jRadioButtonMenuItem3);
        jRadioButtonMenuItem3.setText("Journal Entry");
        jRadioButtonMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem3ActionPerformed(evt);
            }
        });
        jMenuDocType.add(jRadioButtonMenuItem3);

        ButtonGroupDocumentType.add(jRadioButtonMenuItem4);
        jRadioButtonMenuItem4.setText("Cash Receipt");
        jRadioButtonMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem4ActionPerformed(evt);
            }
        });
        jMenuDocType.add(jRadioButtonMenuItem4);

        ButtonGroupDocumentType.add(jRadioButtonMenuItem5);
        jRadioButtonMenuItem5.setText("Handwritten Check");
        jRadioButtonMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem5ActionPerformed(evt);
            }
        });
        jMenuDocType.add(jRadioButtonMenuItem5);

        ButtonGroupDocumentType.add(jRadioButtonMenuItem8);
        jRadioButtonMenuItem8.setText("ACH");
        jRadioButtonMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem8ActionPerformed(evt);
            }
        });
        jMenuDocType.add(jRadioButtonMenuItem8);

        ButtonGroupDocumentType.add(jRadioButtonMenuItem7);
        jRadioButtonMenuItem7.setSelected(true);
        jRadioButtonMenuItem7.setText("Custom Save");
        jRadioButtonMenuItem7.setToolTipText("Used to save generic documents");
        jRadioButtonMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem7ActionPerformed(evt);
            }
        });
        jMenuDocType.add(jRadioButtonMenuItem7);
        jMenuDocType.add(jSeparator7);

        ButtonGroupDocumentType.add(jRadioButtonMenuItem9);
        jRadioButtonMenuItem9.setText("Edit");
        jRadioButtonMenuItem9.setToolTipText("Use to open an existing file");
        jRadioButtonMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem9ActionPerformed(evt);
            }
        });
        jMenuDocType.add(jRadioButtonMenuItem9);

        jMenuBar1.add(jMenuDocType);

        jMenuSettings.setText("Settings  ");

        jCheckBoxMenuCheckupdate.setSelected(true);
        jCheckBoxMenuCheckupdate.setText("Check for updates");
        jMenuSettings.add(jCheckBoxMenuCheckupdate);

        jMenuItem1.setText("Change directories");
        jMenuItem1.setToolTipText("Change the directories where documents are saved to");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuSettings.add(jMenuItem1);
        jMenuSettings.add(jSeparator9);

        jMenuItemEmailTemplate.setText("Email Template");
        jMenuItemEmailTemplate.setToolTipText("Edit the template email that is used for emailing documents");
        jMenuItemEmailTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEmailTemplateActionPerformed(evt);
            }
        });
        jMenuSettings.add(jMenuItemEmailTemplate);

        jMenuItemAttachmentSize.setText("Maximum Attachment Size");
        jMenuItemAttachmentSize.setToolTipText("Change the maximum allowable attachment size");
        jMenuItemAttachmentSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAttachmentSizeActionPerformed(evt);
            }
        });
        jMenuSettings.add(jMenuItemAttachmentSize);
        jMenuSettings.add(jSeparator8);

        jCheckBoxFilmStrip.setSelected(true);
        jCheckBoxFilmStrip.setText("Show Film-Strip");
        jCheckBoxFilmStrip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFilmStripActionPerformed(evt);
            }
        });
        jMenuSettings.add(jCheckBoxFilmStrip);
        jMenuSettings.add(jSeparator4);

        jCheckBoxUseAutomation.setSelected(true);
        jCheckBoxUseAutomation.setText("Use ScanSnap Automation");
        jCheckBoxUseAutomation.setToolTipText("Allows ScanSnap scanners to be used with the program");
        jCheckBoxUseAutomation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseAutomationActionPerformed(evt);
            }
        });
        jMenuSettings.add(jCheckBoxUseAutomation);

        jCheckBoxMenuItemAppend.setText("Append Scanned Documents");
        jCheckBoxMenuItemAppend.setToolTipText("Merges new scans to the end of the current document");
        jMenuSettings.add(jCheckBoxMenuItemAppend);
        jMenuSettings.add(jSeparator6);

        jCheckBoxAutoHide.setSelected(true);
        jCheckBoxAutoHide.setText("Auto Hide to Tray");
        jCheckBoxAutoHide.setToolTipText("Hides the program when no document is open");
        jCheckBoxAutoHide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAutoHideActionPerformed(evt);
            }
        });
        jMenuSettings.add(jCheckBoxAutoHide);

        jCheckBoxHideOnExit.setSelected(true);
        jCheckBoxHideOnExit.setText("Hide on Exit");
        jCheckBoxHideOnExit.setToolTipText("Hides the program when the exit button is pressed instead of exiting");
        jCheckBoxHideOnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxHideOnExitActionPerformed(evt);
            }
        });
        jMenuSettings.add(jCheckBoxHideOnExit);

        jMenuBar1.add(jMenuSettings);

        jMenuTools.setText("Tools  ");

        jMenuItem6.setText("PDF Merge Utility");
        jMenuItem6.setToolTipText("Used to merge multiple PDF files in a given directory");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItem6);

        jMenuBar1.add(jMenuTools);

        jMenuDocument.setText("Document ");

        jMenuItemUndo.setText("Undo (Ctrl+Z)");
        jMenuItemUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUndoActionPerformed(evt);
            }
        });
        jMenuDocument.add(jMenuItemUndo);

        jMenuItemRedo.setText("Redo (Ctrl+Y)");
        jMenuItemRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRedoActionPerformed(evt);
            }
        });
        jMenuDocument.add(jMenuItemRedo);
        jMenuDocument.add(jSeparator1);

        jMenuDeletePage.setText("Delete Page (Del)");
        jMenuDeletePage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDeletePageActionPerformed(evt);
            }
        });
        jMenuDocument.add(jMenuDeletePage);

        jMenuDeleteDoc.setText("Close Document (Ctrl+W)");
        jMenuDeleteDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDeleteDocActionPerformed(evt);
            }
        });
        jMenuDocument.add(jMenuDeleteDoc);
        jMenuDocument.add(jSeparator5);

        jMenuItem9.setText("Insert Pages");
        jMenuItem9.setToolTipText("Used to insert pages from a PDF file into the opened file");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenuDocument.add(jMenuItem9);

        jMenuItemPrint.setText("Print (Ctrl+P)");
        jMenuItemPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPrintActionPerformed(evt);
            }
        });
        jMenuDocument.add(jMenuItemPrint);

        jMenuBar1.add(jMenuDocument);

        jMenuApprovals.setText("Credit Card Approval  ");

        jMenuItem7.setText("Approval Database");
        jMenuItem7.setToolTipText("Stores information for approval status of credit card statements");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenuApprovals.add(jMenuItem7);

        jMenuItem4.setText("Cardholders Database");
        jMenuItem4.setToolTipText("Stores information for cardholders");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenuApprovals.add(jMenuItem4);

        jMenuItem5.setText("Approvers Database");
        jMenuItem5.setToolTipText("Stores information for credit card approvers");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenuApprovals.add(jMenuItem5);
        jMenuApprovals.add(jSeparator2);

        jMenuItem3.setText("Approval Email Template");
        jMenuItem3.setToolTipText("Make changes to the credit card approval email template");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenuApprovals.add(jMenuItem3);

        jMenuBar1.add(jMenuApprovals);

        jMenu1.setText("Help ");
        jMenu1.setToolTipText("Useful information about this program");

        jMenuItem8.setText("Help");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuItemAbout.setText("About");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemAbout);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxScale))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollFilmStrip, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(23, 23, 23))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollFilmStrip))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jCheckBoxScale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem2ActionPerformed
        SetDocumentType(2);
        DisplayImage();
    }//GEN-LAST:event_jRadioButtonMenuItem2ActionPerformed

    private void jRadioButtonMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem3ActionPerformed
        SetDocumentType(3);
        DisplayImage();
    }//GEN-LAST:event_jRadioButtonMenuItem3ActionPerformed

    private void jRadioButtonMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem4ActionPerformed
        SetDocumentType(4);
        DisplayImage();
    }//GEN-LAST:event_jRadioButtonMenuItem4ActionPerformed

    private void jRadioButtonMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem5ActionPerformed
        SetDocumentType(5);
        DisplayImage();
    }//GEN-LAST:event_jRadioButtonMenuItem5ActionPerformed

    private void jRadioButtonMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem7ActionPerformed
        SetDocumentType(0);
        DisplayImage();
    }//GEN-LAST:event_jRadioButtonMenuItem7ActionPerformed

    private void jButtonProceedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProceedActionPerformed
        if (noDoc == false)
        {
            if (new File(Main.TempDirectory + "\\" + Main.SourceName).exists())
            {
                pageRotation[CurrentPage] = CurrentRotation;
                SaveChanges(Main.TempDirectory + "\\" + Main.SourceName);
                   jLabelEditName.setText("");
                   editingDocument = false;
                  switch (SelectedDocumentType)
                  {
                    case 0:
                        SaveCustom();
                        break;      

                    case 1:
                        SaveCheck();
                        break;

                    case 2:
                        SaveCreditCard();
                        break;

                    case 3:
                        SaveJournal();
                        break;

                    case 4:
                        SaveCashReceipt();
                        break;

                    case 5:
                        SaveHandCheck();
                        break;

                    case 7:
                        SaveACH();
                        break;
                        
                    case 9:
                        SaveEdit();
                        break;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Temp file does not exist\n" + "Please scan a document before proceeding", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonProceedActionPerformed

    private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrevActionPerformed
        if (CurrentPage > 0 && noDoc == false)
        {
            pageRotation[CurrentPage] = CurrentRotation;
            PrevPage = CurrentPage;
            CurrentPage = pageList[getPageListIndex(CurrentPage)-1];
            CurrentRotation = pageRotation[CurrentPage];
            jFilmStrip.setSelectedIndex(CurrentPage);
            DisplayImage();
            cleanBuffer();
            updateFilmstrip();
        }
    }//GEN-LAST:event_jButtonPrevActionPerformed

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed
        if (CurrentPage < LastPage && noDoc == false)
        {
            pageRotation[CurrentPage] = CurrentRotation;
            PrevPage = CurrentPage;
            CurrentPage = pageList[getPageListIndex(CurrentPage)+1];
            CurrentRotation = pageRotation[CurrentPage];
            jFilmStrip.setSelectedIndex(CurrentPage);
            DisplayImage();
            cleanBuffer();
            updateFilmstrip();
        }
    }//GEN-LAST:event_jButtonNextActionPerformed

    private void jSliderZoomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderZoomStateChanged
        int SliderValue = jSliderZoom.getValue();
        CurrentScale = SliderValue*.01;
        if (noDoc == false)
        {
            DisplayImage();
            jCheckBoxScale.setSelected(false);
        }
    }//GEN-LAST:event_jSliderZoomStateChanged

    private void jButtonRotateCWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotateCWActionPerformed
        rotateCW();
    }//GEN-LAST:event_jButtonRotateCWActionPerformed

    private void jButtonRotateCCWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotateCCWActionPerformed
        rotateCCW();
    }//GEN-LAST:event_jButtonRotateCCWActionPerformed

    private void jButtonCS_BrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCS_BrowseActionPerformed
        JFileChooser chooser = new JFileChooser(CSDirectory);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            CSDirectory = chooser.getSelectedFile().getPath();
            jLabelCS_Destination.setText(CSDirectory);
        }
    }//GEN-LAST:event_jButtonCS_BrowseActionPerformed

    private void jRadioButtonMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem8ActionPerformed
        SetDocumentType(7);
        DisplayImage();
    }//GEN-LAST:event_jRadioButtonMenuItem8ActionPerformed

    private void jButtonReprocessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReprocessActionPerformed
        ReadCheck();
    }//GEN-LAST:event_jButtonReprocessActionPerformed

    private void jButtonAdjustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdjustActionPerformed
        FieldsGui edit = new FieldsGui();
        edit.FieldsGui();
    }//GEN-LAST:event_jButtonAdjustActionPerformed

    private void jButtonResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResultsActionPerformed
        ViewFields();
    }//GEN-LAST:event_jButtonResultsActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        directories.ChangedirectoriesGui();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
        SetDocumentType(1);
        DisplayImage();
    }//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

    private void jCheckBoxScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxScaleActionPerformed
        if(jCheckBoxScale.isSelected())
        {
            scaleCurrentImage();
        }
    }//GEN-LAST:event_jCheckBoxScaleActionPerformed

    private void jScrollPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane1ComponentResized
        DisplayImage();
    }//GEN-LAST:event_jScrollPane1ComponentResized

    private void KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyTyped
        new Thread(new checkFieldsValidity()).start();
    }//GEN-LAST:event_KeyTyped

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        emailGui2.EditEmailContentGui(Main.ApprovalEmailTemplateSubject, Main.ApprovalEmailTemplateBody, "Approval Email Template");
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        holderdb.CardHoldersGui();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        approverdb.CardApproversGui();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        approvals.ApprovalDatabaseGui();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        PDFMerge pdfmerge = new PDFMerge();
        pdfmerge.PDFMerge();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jCheckBoxUseAutomationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUseAutomationActionPerformed
        Main.UseScanSnapAutomation = jCheckBoxUseAutomation.isSelected();
        if (Main.UseScanSnapAutomation == true)
        {
            Main.startScanSnapAutomation();
        }
        else
        {
            Main.stopScanSnapAutomation();
        }
    }//GEN-LAST:event_jCheckBoxUseAutomationActionPerformed

    private void jFilmStripValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jFilmStripValueChanged
        if(filmStripUpdateInProgress == false)
        {
            if(deleteDone == true && filmStripLoaded == true)
            {
                PrevPage = CurrentPage;
                CurrentPage = pageList[jFilmStrip.getSelectedIndex()];
                DisplayImage();
                cleanBuffer();
            }
            else
            {
                jFilmStrip.clearSelection();
            }
        }
    }//GEN-LAST:event_jFilmStripValueChanged

    private void jMenuDeletePageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuDeletePageActionPerformed
        deletePage();
    }//GEN-LAST:event_jMenuDeletePageActionPerformed

    private void jMenuDeleteDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuDeleteDocActionPerformed
        if (noDoc == false)
        {
            documentStillOpenWarning();
        }
    }//GEN-LAST:event_jMenuDeleteDocActionPerformed

    private void jCheckBoxFilmStripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFilmStripActionPerformed
        if(jCheckBoxFilmStrip.isSelected() == true)
        {
            showFilmStrip = true;
            jScrollFilmStrip.setVisible(showFilmStrip);
        }
        else
        {
            showFilmStrip = false;
            jScrollFilmStrip.setVisible(showFilmStrip);
        }
    }//GEN-LAST:event_jCheckBoxFilmStripActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        //Opens the help html file
        File help = new File("C:\\Program Files (x86)\\QuickSave\\Help.html");
        try
        {
            Desktop.getDesktop().open(help);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItemUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUndoActionPerformed
        restoreFromChangeLog();
    }//GEN-LAST:event_jMenuItemUndoActionPerformed

    private void jMenuItemRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRedoActionPerformed
        restoreFromRedoLog();
    }//GEN-LAST:event_jMenuItemRedoActionPerformed

    private void jCheckBoxAutoHideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAutoHideActionPerformed
        autoHideToTray = jCheckBoxAutoHide.isSelected();
    }//GEN-LAST:event_jCheckBoxAutoHideActionPerformed

    private void jCheckBoxHideOnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxHideOnExitActionPerformed
        hideOnExit = jCheckBoxHideOnExit.isSelected();
    }//GEN-LAST:event_jCheckBoxHideOnExitActionPerformed

    private void jButtonProceedPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jButtonProceedPropertyChange
        if (documentExists == true)
        {
            setVisible(true);
        }
        else if (autoHideToTray == true)
        {
            setVisible(false);
            exiting = false;
        }
    }//GEN-LAST:event_jButtonProceedPropertyChange

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
        closeProceedure(!hideOnExit);
    }//GEN-LAST:event_formComponentHidden

    private void jScrollFilmStripComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollFilmStripComponentHidden
        revalidate();
    }//GEN-LAST:event_jScrollFilmStripComponentHidden

    private void jScrollFilmStripComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollFilmStripComponentShown
        revalidate();
    }//GEN-LAST:event_jScrollFilmStripComponentShown

    private void jRadioButtonMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem9ActionPerformed
        if (editingDocument == true)
        {
            SetDocumentType(9);
            DisplayImage();
        }
        else if (documentStillOpenWarning())
        {
            SetDocumentType(SelectedDocumentType);
        }
        else
        { 
            SetDocumentType(9);
            DisplayImage();
        }
    }//GEN-LAST:event_jRadioButtonMenuItem9ActionPerformed

    private void jButtonEditBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditBrowseActionPerformed
        if (!documentStillOpenWarning())
        {
            JFileChooser chooser = new JFileChooser(EditPath);
            FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter(
                    "pdf files (*.pdf)", "pdf");
            chooser.setFileFilter(pdfFilter);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setDialogTitle("Select file to open");
            chooser.setApproveButtonText("Open");
            int returnVal = chooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                editingDocument = true;
                EditPath = chooser.getSelectedFile().getPath();
                jLabelEditName.setText(chooser.getSelectedFile().getName());
                try
                {
                    Files.copy(Paths.get(chooser.getSelectedFile().getPath()), Paths.get(Main.TempDirectory + "\\" + Main.SourceName), REPLACE_EXISTING);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jButtonEditBrowseActionPerformed

    private void jMenuItemPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPrintActionPerformed
        print();
    }//GEN-LAST:event_jMenuItemPrintActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        Insert insert = new Insert();
        insert.Insert();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItemEmailTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEmailTemplateActionPerformed
        emailGui.EditEmailContentGui(Main.EmailTemplateSubject, Main.EmailTemplateBody, "Email Template");
    }//GEN-LAST:event_jMenuItemEmailTemplateActionPerformed

    private void jCheckBoxEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxEmailActionPerformed
        emailAfterSaving = jCheckBoxEmail.isSelected();
    }//GEN-LAST:event_jCheckBoxEmailActionPerformed

    private void jMenuItemAttachmentSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAttachmentSizeActionPerformed
        DecimalFormat numberFormat = new DecimalFormat("#.000");
        String s = (String)JOptionPane.showInputDialog(
                    null,
                    "Maximum attachment size (MB)",
                    "Maximum attachment size",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    numberFormat.format((float)Main.maxAttachmentSize/(float)1048576));
        if ((s != null) && (s.length() > 0))
        {
            Main.maxAttachmentSize = (long)((float)Integer.parseInt(s)*(float)1048576);
        }
    }//GEN-LAST:event_jMenuItemAttachmentSizeActionPerformed

    private void jRadioButtonDefaultStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButtonDefaultStateChanged
        checkFieldsValidity();
    }//GEN-LAST:event_jRadioButtonDefaultStateChanged

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        //This method creates a popup frame that displays the splash screen of the program
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        int imgWidth = 0;
        int imgHeight = 0;
        
        try
        {
            Image splash = ImageIO.read(new File("C:\\Program Files (x86)\\QuickSave\\Splash Screen 3.png"));
            imgWidth = splash.getWidth(null);
            imgHeight = splash.getHeight(null);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final JWindow window = new JWindow();
        window.setBackground(new Color(0, 0, 0, 0));
        window.getContentPane().add(new JLabel(new ImageIcon("C:\\Program Files (x86)\\QuickSave\\Splash Screen 3.png")));
        window.setBounds((width / 2) - (imgWidth / 2), (height / 2) - (imgHeight / 2), 500, 300);
        window.setVisible(true);
        window.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                window.setVisible(false);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                
            }
        });
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void jLabelImageMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelImageMouseMoved
        //Gets the mouse coordinates
        xCoord = evt.getX();
        yCoord = evt.getY();
        
        cursormoving = true;
        DisplayImage();
    }//GEN-LAST:event_jLabelImageMouseMoved

    private void jLabelImageMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelImageMouseDragged
        //Gets the mouse coordinates
        xCoord = evt.getX();
        yCoord = evt.getY();
        
        cursordragging = true;
        DisplayImage();
    }//GEN-LAST:event_jLabelImageMouseDragged

    private void jCheckBoxNewMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxNewMonthActionPerformed
        if (jCheckBoxNewMonth.isSelected())
        {
            jCheckBoxCC_Approved.setEnabled(false);
            jComboCC_Names.setEnabled(false);
            jCheckBoxFiscalEnd.setEnabled(false);
            jComboBoxBusiness.setEnabled(false);
        }
        else
        {
            jCheckBoxCC_Approved.setEnabled(true);
            jComboCC_Names.setEnabled(true);
            jCheckBoxFiscalEnd.setEnabled(true);
        }
    }//GEN-LAST:event_jCheckBoxNewMonthActionPerformed

    private void jButtonSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectActionPerformed
        if (FlaggedPages.contains(CurrentPage))
        {
            JOptionPane.showMessageDialog(jMenuDocument, "You have already flagged this page!", "Page Already Flagged", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            int index = FlaggedPages.size() - 1;
            int i = 0;
            boolean check = true;
            try
            {
                if (CurrentPage < Integer.parseInt(FlaggedPages.get(index).toString()))
                {
                    while (check)
                    {
                        if (CurrentPage > Integer.parseInt(FlaggedPages.get(i).toString()) && CurrentPage < Integer.parseInt(FlaggedPages.get(i+1).toString()))
                        {
                            FlaggedPages.add(i+1, CurrentPage);
                            ExpenseNames.add(i+1, "Add Name");
                            FlaggedPageIndex = i+1;
                            jLabelFlagged.setText(FlaggedPages.size() +  " expense reports selected");
                            int end = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex + 1).toString());
                            int reports = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString()) + 1;
                            jLabelExpenseNumber.setText("Expense Report " + (FlaggedPageIndex + 1) + ": pages " + (reports) + " to " + end);
                            selecting = true;
                            jButtonExpenseNext.setEnabled(true);
                            jButtonExpensePrev.setEnabled(true);
                            check = false;
                        }
                        i++;
                    }
                }
                else
                {
                    FlaggedPages.add(CurrentPage);
                    ExpenseNames.add("Add Name");
                    FlaggedPageIndex = FlaggedPages.size() - 1;
                    jLabelFlagged.setText(FlaggedPages.size() +  " expense reports selected");
                    jTextFieldExpenseName.setEnabled(true);
                    jTextFieldExpenseName.setText("");
                    int reports = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString()) + 1;
                    jLabelExpenseNumber.setText("Expense Report " + FlaggedPages.size() + ": pages " + reports + " to " + displayedTotal);
                    if (FlaggedPages.size() == 2)
                    {
                        jButtonExpensePrev.setEnabled(true);
                    }
                    selecting = true;
                }
            }
            catch (ArrayIndexOutOfBoundsException evnt)
            {
                FlaggedPages.add(CurrentPage);
                ExpenseNames.add("Add Name");
                FlaggedPageIndex = FlaggedPages.size() - 1;
                jLabelFlagged.setText(FlaggedPages.size() +  " expense reports selected");
                jTextFieldExpenseName.setEnabled(true);
                jTextFieldExpenseName.setText("");
                int reports = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString()) + 1;
                jLabelExpenseNumber.setText("Expense Report " + FlaggedPages.size() + ": pages " + reports + " to " + displayedTotal);
                if (FlaggedPages.size() == 2)
                { 
                    jButtonExpensePrev.setEnabled(true);
                }
                selecting = true;
            }
            updateFilmstrip();
        }
    }//GEN-LAST:event_jButtonSelectActionPerformed

    private void jButtonExpensePrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExpensePrevActionPerformed
        if (FlaggedPageIndex > 0)
        {
            FlaggedPageIndex--;
            jButtonExpenseNext.setEnabled(true);
            int end = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex + 1).toString());
            int report = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString());
            jLabelExpenseNumber.setText("Expense Report " + (FlaggedPageIndex + 1) + ": pages " + (report + 1) + " to " + end);
            jFilmStrip.setSelectedIndex(Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString()));
            jTextFieldExpenseName.setText(ExpenseNames.get(FlaggedPageIndex).toString());
            if (FlaggedPageIndex == 0)
            {
                jButtonExpensePrev.setEnabled(false);
            }
            updateFilmstrip();
        }
    }//GEN-LAST:event_jButtonExpensePrevActionPerformed

    private void jButtonExpenseNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExpenseNextActionPerformed
        if (FlaggedPageIndex < FlaggedPages.size() - 1)
        {
            FlaggedPageIndex++;
            int end = 0;
            if (FlaggedPageIndex == FlaggedPages.size() - 1)
            {
                end = displayedTotal;
            }
            else
            {
                end = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex + 1).toString());
            }
            jButtonExpensePrev.setEnabled(true);
            int report = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString());
            jLabelExpenseNumber.setText("Expense Report " + (FlaggedPageIndex + 1) + ": pages " + (report + 1) + " to " + end);
            jFilmStrip.setSelectedIndex(Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString()));
            jTextFieldExpenseName.setText(ExpenseNames.get(FlaggedPageIndex).toString());
            if (FlaggedPageIndex == FlaggedPages.size() - 1)
            {
                jButtonExpenseNext.setEnabled(false);
            }
            updateFilmstrip();
        }
    }//GEN-LAST:event_jButtonExpenseNextActionPerformed

    private void jTextFieldExpenseNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldExpenseNameKeyTyped
        String keyEntered = "" + evt.getKeyChar();
        if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
        {
            keyEntered = "";
        }
        ExpenseNames.set(FlaggedPageIndex, jTextFieldExpenseName.getText() + keyEntered);
    }//GEN-LAST:event_jTextFieldExpenseNameKeyTyped

    private void jButtonDeselectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeselectActionPerformed
        if (FlaggedPages.contains(CurrentPage))
        {
            int index = FlaggedPages.indexOf(CurrentPage);
            if (!FlaggedPages.isEmpty())
            {
                FlaggedPages.remove(index);
                ExpenseNames.remove(index);
            }
            if (FlaggedPageIndex == 0)
            {
                jButtonExpensePrev.setEnabled(false);
            }
            if (index == 0)
            {
                int report = 0;
                if (FlaggedPageIndex != 0)
                {
                    FlaggedPageIndex--;
                }
                if (!FlaggedPages.isEmpty())
                {
                    report = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString());
                    jTextFieldExpenseName.setText(ExpenseNames.get(FlaggedPageIndex).toString());
                    jLabelExpenseNumber.setText("Expense Report " + (FlaggedPageIndex + 1) + ": pages " + (report + 1) + " to " + displayedTotal);
                    jLabelFlagged.setText(FlaggedPages.size() +  " expense reports selected");
                }
                else
                {
                    jLabelExpenseNumber.setText("No expense reports selected.");
                    jLabelFlagged.setText(FlaggedPages.size() +  " expense reports selected");
                    jTextFieldExpenseName.setText("");
                }
            }
            else if (index == FlaggedPages.size() && FlaggedPages.size() > 1)
            {
                FlaggedPageIndex--;
                int report = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString());
                jLabelExpenseNumber.setText("Expense Report " + (FlaggedPageIndex + 1) + ": pages " + (report + 1) + " to " + displayedTotal);
                jTextFieldExpenseName.setText(ExpenseNames.get(FlaggedPageIndex).toString());
                jLabelFlagged.setText(FlaggedPages.size() +  " expense reports selected");
            }
            else if (FlaggedPageIndex < index)
            {
                FlaggedPageIndex--;
                int report = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString());
                jLabelExpenseNumber.setText("Expense Report " + (FlaggedPageIndex + 1) + ": pages " + (report + 1) + " to " + displayedTotal);
                jTextFieldExpenseName.setText(ExpenseNames.get(FlaggedPageIndex).toString());
                jLabelFlagged.setText(FlaggedPages.size() +  " expense reports selected");
            }
            else
            {
                FlaggedPageIndex--;
                int report = Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString());
                jLabelExpenseNumber.setText("Expense Report " + (FlaggedPageIndex + 1) + ": pages " + (report + 1) + " to " + displayedTotal);
                jTextFieldExpenseName.setText(ExpenseNames.get(FlaggedPageIndex).toString());
                jLabelFlagged.setText(FlaggedPages.size() +  " expense reports selected");
            }
            if (!FlaggedPages.isEmpty())
            {
                jFilmStrip.setSelectedIndex(Integer.parseInt(FlaggedPages.get(FlaggedPageIndex).toString()));
            }
            if (FlaggedPageIndex > 0 && FlaggedPages.size() > 2 && FlaggedPageIndex < FlaggedPages.size() - 2)
            {
                jButtonExpensePrev.setEnabled(true);
                jButtonExpenseNext.setEnabled(true);
            }
            else if (FlaggedPageIndex == 0 && FlaggedPages.size() > 1)
            {
                jButtonExpensePrev.setEnabled(false);
                jButtonExpenseNext.setEnabled(true);
            }
            else if (FlaggedPageIndex == FlaggedPages.size() - 1 && FlaggedPages.size() > 1)
            {
                jButtonExpensePrev.setEnabled(true);
                jButtonExpenseNext.setEnabled(false);
            }
            else
            {
                jButtonExpensePrev.setEnabled(false);
                jButtonExpenseNext.setEnabled(false);
                jTextFieldExpenseName.setEnabled(false);
            }
            deselecting = true;
            selecting = true;
            updateFilmstrip();
        }
    }//GEN-LAST:event_jButtonDeselectActionPerformed

    private void jFormattedTextFieldNumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldNumberKeyTyped
        try
        {
            int pageNumber = 0;
            if (evt.getKeyChar() == 8)
            {
                pageNumber = Integer.parseInt(jFormattedTextFieldNumber.getText())-1;
                jFilmStrip.setSelectedIndex(pageNumber);
            }
            else
            {
                pageNumber = Integer.parseInt(jFormattedTextFieldNumber.getText() + "" + evt.getKeyChar()) - 1;
                if (pageNumber > displayedTotal)
                {
                    jFormattedTextFieldNumber.remove(evt.getKeyChar());
                }
                else if (pageNumber < displayedTotal)
                {
                    jFilmStrip.setSelectedIndex(pageNumber);
                }
            }
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException evnt)
        {
            if (evt.getKeyChar() == 8)
            {
                jFormattedTextFieldNumber.remove(evt.getKeyChar());
                jFilmStrip.setSelectedIndex(Integer.parseInt(jFormattedTextFieldNumber.getText())+1);
            }
            else
            {
                jFormattedTextFieldNumber.removeAll();
                jFormattedTextFieldNumber.setText(displayedTotal + "");
                jFilmStrip.setSelectedIndex(displayedTotal-1);
                jFormattedTextFieldNumber.remove(evt.getKeyChar());
            }
        }
        updateFilmstrip();
    }//GEN-LAST:event_jFormattedTextFieldNumberKeyTyped

    private void jCheckBoxFiscalEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFiscalEndActionPerformed
        if (jCheckBoxFiscalEnd.isSelected())
        {
            jComboBoxBusiness.setEnabled(true);
        }
        else
        {
            jComboBoxBusiness.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBoxFiscalEndActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup ButtonGroupCashReceipt;
    private javax.swing.ButtonGroup ButtonGroupDocumentType;
    private javax.swing.ButtonGroup buttonGroupGroupPost;
    private javax.swing.ButtonGroup buttonGroupHandCheck;
    private javax.swing.ButtonGroup buttonGroupInsert;
    private javax.swing.ButtonGroup buttonGroupPrint;
    private javax.swing.JButton jButtonAdjust;
    private javax.swing.JButton jButtonCS_Browse;
    private javax.swing.JButton jButtonDeselect;
    private javax.swing.JButton jButtonEditBrowse;
    private javax.swing.JButton jButtonExpenseNext;
    private javax.swing.JButton jButtonExpensePrev;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JButton jButtonPrev;
    public static javax.swing.JButton jButtonProceed;
    private javax.swing.JButton jButtonReprocess;
    private javax.swing.JButton jButtonResults;
    private javax.swing.JButton jButtonRotateCCW;
    private javax.swing.JButton jButtonRotateCW;
    private javax.swing.JButton jButtonSelect;
    private javax.swing.JCheckBoxMenuItem jCheckBoxAutoHide;
    private javax.swing.JCheckBox jCheckBoxCC_Approved;
    private javax.swing.JCheckBox jCheckBoxC_Void;
    public static javax.swing.JCheckBox jCheckBoxEmail;
    private javax.swing.JCheckBoxMenuItem jCheckBoxFilmStrip;
    public static javax.swing.JCheckBox jCheckBoxFiscalEnd;
    private javax.swing.JCheckBox jCheckBoxHC_Void;
    private javax.swing.JCheckBoxMenuItem jCheckBoxHideOnExit;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuCheckupdate;
    public static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemAppend;
    private javax.swing.JCheckBox jCheckBoxNewMonth;
    public static javax.swing.JCheckBox jCheckBoxScale;
    private javax.swing.JCheckBoxMenuItem jCheckBoxUseAutomation;
    public static javax.swing.JComboBox jComboBoxBusiness;
    public static javax.swing.JComboBox jComboCC_Names;
    public static javax.swing.JList jFilmStrip;
    private javax.swing.JFormattedTextField jFormattedTextFieldNumber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCS_Destination;
    public static javax.swing.JLabel jLabelEditName;
    private javax.swing.JLabel jLabelExpenseNumber;
    private javax.swing.JLabel jLabelFlagged;
    public static javax.swing.JLabel jLabelImage;
    private static javax.swing.JLabel jLabelPage;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenuApprovals;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuDeleteDoc;
    private javax.swing.JMenuItem jMenuDeletePage;
    private javax.swing.JMenu jMenuDocType;
    private javax.swing.JMenu jMenuDocument;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemAttachmentSize;
    private javax.swing.JMenuItem jMenuItemEmailTemplate;
    private javax.swing.JMenuItem jMenuItemPrint;
    private javax.swing.JMenuItem jMenuItemRedo;
    private javax.swing.JMenuItem jMenuItemUndo;
    private javax.swing.JMenu jMenuSettings;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelACH;
    private javax.swing.JPanel jPanelCashReceipt;
    private javax.swing.JPanel jPanelCheck;
    private javax.swing.JPanel jPanelCreditCard;
    private javax.swing.JPanel jPanelCustomSave;
    private javax.swing.JPanel jPanelEdit;
    public static javax.swing.JPanel jPanelFields;
    private javax.swing.JPanel jPanelHC_Campus;
    private javax.swing.JPanel jPanelHandCheck;
    private javax.swing.JPanel jPanelJournalEntry;
    private static javax.swing.JRadioButton jRadioButtonDefault;
    public static javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    public static javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    public static javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem3;
    public static javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem4;
    public static javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem5;
    public static javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem7;
    public static javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem8;
    public static javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem9;
    private javax.swing.JRadioButton jRadioCR_ARStatement;
    private javax.swing.JRadioButton jRadioCR_GLStatement;
    private javax.swing.JRadioButton jRadioHC_Billings;
    private javax.swing.JRadioButton jRadioHC_CollegePlace;
    private javax.swing.JRadioButton jRadioHC_Missoula;
    private javax.swing.JRadioButton jRadioHC_Portland;
    private javax.swing.JRadioButton jRadioHC_Rosario;
    public static javax.swing.JScrollPane jScrollFilmStrip;
    public static javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private static javax.swing.JSlider jSliderZoom;
    private static javax.swing.JFormattedTextField jTextFieldACH_Date;
    private static javax.swing.JTextField jTextFieldACH_Number;
    private static javax.swing.JFormattedTextField jTextFieldCC_Date;
    private static javax.swing.JFormattedTextField jTextFieldCR_Date;
    private static javax.swing.JFormattedTextField jTextFieldCR_Number;
    private static javax.swing.JTextField jTextFieldCS_Name;
    private static javax.swing.JFormattedTextField jTextFieldC_Amount;
    private static javax.swing.JFormattedTextField jTextFieldC_Date;
    private static javax.swing.JTextField jTextFieldC_Name;
    private static javax.swing.JFormattedTextField jTextFieldC_Number;
    private javax.swing.JTextField jTextFieldExpenseName;
    private static javax.swing.JFormattedTextField jTextFieldHC_Amount;
    private static javax.swing.JFormattedTextField jTextFieldHC_Date;
    private static javax.swing.JTextField jTextFieldHC_Name;
    private static javax.swing.JFormattedTextField jTextFieldHC_Number;
    private static javax.swing.JFormattedTextField jTextFieldJE_Number;
    // End of variables declaration//GEN-END:variables
}

class updateFilmStripImageRotation extends Thread
{
    @Override
    public void run()
    {
        try
        {
            ImageProcessing img = new ImageProcessing();
            DefaultListModel model = (DefaultListModel) MainGui.jFilmStrip.getModel();
            int index = MainGui.getPageListIndex(MainGui.CurrentPage);
            int ref;
            if (MainGui.CurrentRotation == 90 || MainGui.CurrentRotation == 270)
            {
                ref = MainGui.pages.get(MainGui.CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,50).getHeight();
            }
            else
            {
                ref = MainGui.pages.get(MainGui.CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,50).getWidth();
            }
            int dpi = (int)(50*((double)MainGui.filmStripWidth/(double)ref));
            BufferedImage m = MainGui.pages.get(MainGui.CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,dpi);
            if (MainGui.CurrentRotation == 90)
            {
                m = img.rotateCW(m);
            }
            else if (MainGui.CurrentRotation == 270)
            {
                m = img.rotateCCW(m);
            }
            else if (MainGui.CurrentRotation == 180)
            {
                m = img.rotateCW(img.rotateCW(m));
            }
            int width = m.getWidth();
            int height = m.getHeight();
            BufferedImage overlay = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
            BufferedImage combined = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);

            Graphics g = overlay.createGraphics();
            g.setColor(new Color(0,0,20,200));
            if (MainGui.FlaggedPages.contains(MainGui.CurrentPage))
            {
                g.fillRect(width - 30, height-35, 30, 35);
            }
            if (MainGui.CurrentPage + 1 <= 9)
            {
                g.fillRect(0, height-35, 25, 35);
            }
            else if (MainGui.CurrentPage + 1 >= 10 && MainGui.CurrentPage + 1 < 100)
            {
                g.fillRect(0, height-35, 40, 35);
            }
            g.setColor(Color.white);
            g.setFont(new Font("TimeRoman", Font.PLAIN, 25));
            g.drawString(MainGui.CurrentPage + 1+"", 5, height - 10);
            if (MainGui.FlaggedPages.contains(MainGui.CurrentPage))
            {
                g.drawString("E", width - 23, height - 10);
            }
            Graphics g2 = combined.createGraphics();
            g2.drawImage(m, 0, 0, null);
            g2.drawImage(overlay, 0, 0, null);
            model.add(index, new ImageIcon(combined));
            model.remove(index+1);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class populateFilmStrip extends Thread
{
    @Override
    public void run()
    {
        MainGui.filmStripLoaded = false;
        MainGui.jScrollFilmStrip.getVerticalScrollBar().setUnitIncrement(32);
        try
        {
            MainGui.listModel = new DefaultListModel();

            MainGui.filmStripWidth = MainGui.jFilmStrip.getWidth()-25;
            int i = 0;

            while (i <= MainGui.LastPage)
            {
                MainGui.pageList[i] = i;
                int ref = MainGui.pages.get(i).convertToImage(BufferedImage.TYPE_INT_RGB,50).getWidth();                
                int dpi = (int)(50*((double)MainGui.filmStripWidth/(double)ref));
                
                BufferedImage base = MainGui.pages.get(i).convertToImage(BufferedImage.TRANSLUCENT,dpi);
                int width = base.getWidth();
                int height = base.getHeight();
                BufferedImage overlay = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
                BufferedImage combined = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
                Graphics g = overlay.createGraphics();
                g.setColor(new Color(0,0,20,200));
                if (i + 1 <= 9)
                {
                    g.fillRect(0, height-35, 25, 35);
                }
                else if (i + 1 >= 10 && i + 1 < 100)
                {
                    g.fillRect(0, height-35, 40, 35);
                }
                g.setColor(Color.white);
                g.setFont(new Font("TimeRoman", Font.PLAIN, 25));
                g.drawString(i + 1+"", 5, height - 10);
                Graphics g2 = combined.createGraphics();
                g2.drawImage(base, 0, 0, null);
                g2.drawImage(overlay, 0, 0, null);
                
                MainGui.listModel.addElement(new ImageIcon(combined));
                MainGui.jFilmStrip.setModel(MainGui.listModel);
                i++;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        MainGui.filmStripLoaded = true;
        MainGui.updateFilmstrip();
    }
}

class PageBuffer extends Thread
{
    @Override
    public void run()
    {
        try
        {
            ImageProcessing img = new ImageProcessing();
            File doc = new File(Main.TempDirectory + "\\" + Main.SourceName);
            int prevPage = 1;

            int n = 0;
            while(n < MainGui.TotalPages)
            {
                MainGui.displayedNumber[n] = n+1;
                n++;
            }         
            
            while(MainGui.noDoc == false)
            {
                if(MainGui.images[MainGui.CurrentPage] == null)
                {
                    MainGui.images[MainGui.CurrentPage] = MainGui.pages.get(MainGui.CurrentPage).convertToImage(BufferedImage.TYPE_INT_RGB,Main.dpi);
                    
                    MainGui.blank = MainGui.images[MainGui.CurrentPage];
                    MainGui.DisplayImage();
                }
                
                if (prevPage != MainGui.CurrentPage)
                {              
                    prevPage = MainGui.CurrentPage;
                    
                    int i = MainGui.CurrentPage;
                    int j = 0;
                    while(i < MainGui.TotalPages && j <= MainGui.halfBuffer)
                    {
                        while(MainGui.pageDeleted[i] == true)
                        {
                            MainGui.images[i] = null;
                            i++;
                        }
                        if(i < MainGui.TotalPages)
                        {
                            if(MainGui.images[i] == null)
                            {
                                MainGui.images[i] = MainGui.pages.get(i).convertToImage(BufferedImage.TYPE_INT_RGB,Main.dpi);
                                if (MainGui.pageRotation[i] != 0)
                                {
                                    if (MainGui.pageRotation[i] == 90)
                                    {
                                        MainGui.images[i] = img.rotateCW(MainGui.images[i]);
                                    }
                                    else if (MainGui.pageRotation[i] == 180)
                                    {
                                        MainGui.images[i] = img.rotateCW(MainGui.images[i]);
                                        MainGui.images[i] = img.rotateCW(MainGui.images[i]);
                                    }
                                    else if (MainGui.pageRotation[i] == 270)
                                    {
                                        MainGui.images[i] = img.rotateCCW(MainGui.images[i]);
                                    }
                                }
                            }
                        }
                        i++;
                        j++;
                    }
                    i = MainGui.CurrentPage;
                    j = 0;
                    while(i >= 0 && j <= MainGui.halfBuffer)
                    {
                        while(i >= 0 && MainGui.pageDeleted[i] == true)
                        {
                            MainGui.images[i] = null;
                            i--;
                        }
                        if(i >= 0)
                        {
                            if(MainGui.images[i] == null)
                            {
                                MainGui.images[i] = MainGui.pages.get(i).convertToImage(BufferedImage.TYPE_INT_RGB,Main.dpi);
                                if (MainGui.pageRotation[i] != 0)
                                {
                                    if (MainGui.pageRotation[i] == 90)
                                    {
                                        MainGui.images[i] = img.rotateCW(MainGui.images[i]);
                                    }
                                    else if (MainGui.pageRotation[i] == 180)
                                    {
                                        MainGui.images[i] = img.rotateCW(MainGui.images[i]);
                                        MainGui.images[i] = img.rotateCW(MainGui.images[i]);
                                    }
                                    else if (MainGui.pageRotation[i] == 270)
                                    {
                                        MainGui.images[i] = img.rotateCCW(MainGui.images[i]);
                                    }
                                }
                            }
                        }
                        i--;
                        j++;
                    }
                    if(MainGui.imageDisplayed == false)
                    {
                        MainGui.DisplayImage();
                    }
                }
                
                try 
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(tempChecker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(PageBuffer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

//runs the checkFieldsValidity method after a short ButtonDelay
class checkFieldsValidity extends Thread
{
    @Override
    public void run()
    {
        try 
        {
            Thread.sleep(100);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(tempChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        MainGui.checkFieldsValidity();
    }
}

//A thread used to avoid fast repetition of actions when a keyboard key is
//held down
class ButtonDelay extends Thread
{
    @Override
    public void run()
    {
        try 
        {
            Thread.sleep(50);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(tempChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        MainGui.pageOperationInProgress = false;
    }
}