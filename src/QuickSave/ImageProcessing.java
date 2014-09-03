/*
This class performs image processing on check cover pages to generate clean
samples that are used to perform OCR in order to get the check information.
*/

package QuickSave;

import static QuickSave.MainGui.RefCoords;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class ImageProcessing {
    
    //Finds the reference coordinates in an image by looking for a dark border
    int[] FindRefCoords(BufferedImage img, double tol)
    {
        int h = img.getHeight();
        int w = img.getWidth();
        
        //Find the first line from the bottom of the document
        int x = w/2;
        int y = h-15;
        int refVal = getColorDarkness(img.getRGB(x,y));
        
        int Val = refVal;
        int upperBound = (int)(Val*(1+tol));
        int lowerBound = (int)(Val*(1-tol));
        while(Val < upperBound && Val > lowerBound && y > h*.75)
        {
            y--;
            Val = getColorDarkness(img.getRGB(x,y));
        }
        int Y = y;
        
        //Find the first line from the left of the document
        x = 15;
        y = (int)(h*0.9);
        refVal = getColorDarkness(img.getRGB(x,y));
        Val = refVal;
        
        while(Val < upperBound && Val > lowerBound && x < w*.25)
        {
            x++;
            Val = getColorDarkness(img.getRGB(x,y));
        }
        int X = x;
        
        int coords[] = {X,Y};
        return coords;
    }
    
    //Gets the the darkness of a given color
    private int getColorDarkness(int Color)
    {
        String binary = Integer.toBinaryString(Color);
        if (binary.length() == 24)
        {
            int r = Integer.parseInt(binary.substring(0,7),2);
            int g = Integer.parseInt(binary.substring(8,15),2);
            int b = Integer.parseInt(binary.substring(16,23),2);
            return (r+g+b)/3;
        }
        else if (binary.length() == 32)
        {
            int r = Integer.parseInt(binary.substring(8,15),2);
            int g = Integer.parseInt(binary.substring(16,23),2);
            int b = Integer.parseInt(binary.substring(24,31),2);
            return (r+g+b)/3;
        }
        else
        {
            return 0;
        }
    }
    
    //Removes the background of an image based on the average darkness in each
    //corner of the image
    BufferedImage RemoveBackground(BufferedImage src, float tol)
    {
        try
        {
            BufferedImage dest = src;
            int h = dest.getHeight();
            int w = dest.getWidth();
            int refCol = (getColorDarkness(dest.getRGB(2,2))+getColorDarkness(dest.getRGB(w-2,2))+getColorDarkness(dest.getRGB(2,h-2))+getColorDarkness(dest.getRGB(w-2,h-2)))/4;
            int Col;
            int x = 1;
            int y;
            int upperBound = (int)(refCol*(1+tol));
            int lowerBound = (int)(refCol*(1-tol));
            int color = 16777215;
            while (x < w)
            {
                y = 1;
                while (y < h)
                {
                    Col = getColorDarkness(dest.getRGB(x,y));
                    if (Col > lowerBound && Col < upperBound)
                    {
                        dest.setRGB(x,y,color);
                    }
                   y++; 
                }
                x++;
            }
            return dest;
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }   
    
    //Crops an image to the given coordiantes and scales it to the given scale
    BufferedImage CropImage(BufferedImage Source, double[] Coords, float scale)
    {
        int Hsrc = Source.getHeight();
        int Wsrc = Source.getWidth();
        int x = RefCoords[0]+(int)(Coords[0]*Wsrc);
        int y = RefCoords[1]-(int)(Coords[1]*Hsrc);
        int w = (int)(Coords[2]*Wsrc);
        int h = (int)(Coords[3]*Hsrc);
        if (x >= Wsrc-1 || y >= Hsrc-1 || x < 0 || y < 0 || w <= 0 || h <=0)
        {
            String[] options = {"Credit Card","Journal Entry","Cash Receipt","ACH","Hand Written Check","Custom Save"};
            JComboBox jcb = new JComboBox(options);
            Object[] message = {"There was an error reading the check fields.\nEither rescan the document or select a\ndifferent document type", jcb};
            int answer1 = JOptionPane.showConfirmDialog(null, message, "Cannont Crop", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            String answer = jcb.getSelectedItem().toString();
            if (answer1 == JOptionPane.OK_OPTION)
            {
                switch(answer)
                {
                    case "Credit Card":
                        MainGui.SetDocumentType(2);
                        MainGui.DisplayImage();
                        break;
                    case "Journal Entry":
                        MainGui.SetDocumentType(3);
                        MainGui.DisplayImage();
                        break;
                    case "Cash Receipt":
                        MainGui.SetDocumentType(4);
                        MainGui.DisplayImage();
                        break;
                    case "ACH":
                        MainGui.SetDocumentType(7);
                        MainGui.DisplayImage();
                        break;
                    case "Hand Written Check":
                        MainGui.SetDocumentType(5);
                        MainGui.DisplayImage();
                        break;
                    case "Custom Save":
                        MainGui.SetDocumentType(8);
                        MainGui.DisplayImage();
                        break;
                }
            }
            else
            {
                MainGui.DeleteDoc();
            }
            return null;
        }
        else
        {
            if (x+w > Wsrc)
            {
                w = Wsrc-x;
            }
            if (y+h > Hsrc)
            {
                h = Hsrc-y;
            }
            BufferedImage Dest = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = (Graphics2D) Dest.getGraphics().create();
            g.drawImage(Source, 0, 0, w, h, x, y, x+w, y+h, null);
            g.dispose();
            
            int Wnew = (int)(w*scale);
            int Hnew = (int)(h*scale);
            
            BufferedImage resizedImage = new BufferedImage(Wnew, Hnew, BufferedImage.TYPE_INT_RGB); 
            Graphics2D g2 = resizedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(Dest, 0, 0, Wnew, Hnew, null);
            g2.dispose();
            
            return resizedImage;
        }
    }
   
    //Draws a rectangle on an image in the given coordinates with the given color
    BufferedImage DrawRectangle(BufferedImage SourceImg, double[] Coords, int[] color)
    {
        int h = SourceImg.getHeight();
        int w = SourceImg.getWidth();
        BufferedImage Result = SourceImg;
        Graphics2D gr = Result.createGraphics();
        gr.setColor(new Color(color[0],color[1],color[2],color[3]));
        gr.fillRect(RefCoords[0]+(int)(Coords[0]*w),RefCoords[1]-(int)(Coords[1]*h),(int)(Coords[2]*w),(int)(Coords[3]*h));
        //gr.draw(new Rectangle2D.Double(x, y, w, h));
        gr.dispose();
        return Result;
    }
    
    //Rotates an image by 90 degrees clockwise
    BufferedImage rotateCW(BufferedImage input)
    {
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage rotatedImage = new BufferedImage(height, width, input.getType());
        Graphics2D g2 = rotatedImage.createGraphics();
        g2.rotate(Math.toRadians(90), height/2, height/2);
        g2.drawImage(input, 0, 0, Color.WHITE, null);
        g2.dispose();
        return rotatedImage;
    }
    
    //Rotates an image by 90 degrees counter clockwise
    BufferedImage rotateCCW(BufferedImage input)
    {
        int width = input.getWidth();
        int height = input.getHeight();
        int diff = height-width;
        BufferedImage rotatedImage = new BufferedImage(height, width, input.getType());
        Graphics2D g2 = rotatedImage.createGraphics();
        g2.rotate(Math.toRadians(-90), height/2, height/2);
        g2.drawImage(input, diff+1, 0, Color.WHITE, null);
        g2.dispose();
        return rotatedImage;
    }
    
    //Finds the apropriate scale to fit an image inside the given dimmensions
    double GetScale(BufferedImage img, int maxWidth, int maxHeight)
    {
        double h = img.getHeight();
        double w = img.getWidth();
        double scale;
        double sw = maxWidth/w;
        double sh = maxHeight/h;
        if(sw > sh)
        {
            scale = sh; 
        }
        else
        {
            scale = sw;
        }
        if(scale > 1)
        {
            scale = 1;
        }
        if(scale < 0.01)
        {
            scale = 0.01;
        }
        return scale;
    }
}