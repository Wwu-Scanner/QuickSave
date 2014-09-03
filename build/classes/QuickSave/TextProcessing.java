/*
A class that is used to fix errors in the OCR engine
*/

package QuickSave;

import static QuickSave.MainGui.instance;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import net.sourceforge.tess4j.TesseractException;

public class TextProcessing
{
    //Runs OCR on a specified image
    String OCR(BufferedImage image, String lang)
    {
        instance.setLanguage(lang);
        String result;
        try
        {
            result = instance.doOCR(image);
        }
        catch (TesseractException e)
        {
            result = e.getMessage();
        }
        return result;
    }

    //Fixes errors that are commonly made by the OCR engine for numbers
    String FixNumber(String input)
    {
        String output = input.replaceAll(" ", "");
        output = output.replaceAll("\n", "");
        return output;
    }

    //Fixes errors that are commonly made by the OCR engine for names
    String FixName(String input)
    {
        String output = input.replaceAll("\n", "");
        output = output.replaceAll("/", "-");
        output = output.replaceAll("'", "");
        output = output.replaceAll("i", ",");

        boolean done = false;
        int i = output.length() - 1;
        if (i > 0)
        {
            int endpoint = i;
            while (i > 0 && done == false)
            {
                String comp = output.substring(i, i + 1);
                if (comp.equals(" "))
                {
                    i--;
                }
                else
                {
                    endpoint = i + 1;
                    done = true;
                }
            }

            output = output.substring(0, endpoint);
        }
        return output;
    }

    //Fixes errors that are comonly made by the OCR engine for dates
    String FixDate(String input)
    {
        String month;
        String output = input.replaceAll("\n", "");
        output = output.replaceAll("\\.", "");
        output = output.replaceAll(",", "");
        output = output.replaceAll(" ", "");

        if (output.length() >= 7)
        {
            String day = output.substring(0, 2);
            String monthName = output.substring(2, 5);
            monthName = monthName.replaceAll("1", "l");
            monthName = monthName.replaceAll("0", "O");
            monthName = monthName.replaceAll("e", "c");
            monthName = monthName.replaceAll("5", "S");

            String first = monthName.substring(0, 1);
            String mid = monthName.substring(1, 2);
            String last = monthName.substring(2, 3);
            switch (first)
            {
                case "F":
                    month = "02";
                    break;
                case "S":
                    month = "09";
                    break;
                case "O":
                    month = "10";
                    break;
                case "N":
                    month = "11";
                    break;
                case "D":
                    month = "12";
                    break;
                case "A":
                    if (last.equals("g"))
                    {
                        month = "08";
                    }
                    else
                    {
                        month = "04";
                    }
                    break;
                case "M":
                    if (last.equals("y") || last.equals("g"))
                    {
                        month = "05";
                    }
                    else
                    {
                        month = "03";
                    }
                    break;
                default:
                    if (last.equals("l"))
                    {
                        month = "07";
                    }
                    else
                    {
                        if (mid.equals("a"))
                        {
                            month = "01";
                        }
                        else
                        {
                            month = "06";
                        }
                    }
                    break;
            }

            int len = output.length();
            String year = output.substring(len - 2, len);

            output = month + "/" + day + "/" + year;

            if (output.contains("l"))
            {
                output = output.replaceAll("l", "1");
            }
            if (output.contains("I"))
            {
                output = output.replaceAll("I", "1");
            }
            if (output.contains("|"))
            {
                output = output.replaceAll("|", "1");
            }
            if (output.contains("O"))
            {
                output = output.replaceAll("O", "0");
            }
            if (output.contains("o"))
            {
                output = output.replaceAll("o", "0");
            }
            if (output.contains("S"))
            {
                output = output.replaceAll("S", "5");
            }
            if (output.contains("n"))
            {
                output = output.replaceAll("n", "0");
            }
            if (output.contains("t"))
            {
                output = output.replaceAll("t", "1");
            }
            if (output.contains("a"))
            {
                output = output.replaceAll("a", "1");
            }
        }
        return output;
    }

    //Fixes errors that are comonly made by the OCR engine for dollar amounts
    String FixAmount(String input)
    {
        String output = input.replaceAll(" ", "");
        output = output.replaceAll("\n", "");
        output = output.replaceAll("\\.", ",");
        output = output.replaceAll("\\*", ",");

        boolean done = false;
        int i = output.length() - 1;

        if (i >= 6)
        {
            int endpoint = i;
            String a;
            String b;
            String c;
            String d;
            while (i >= 4 && done == false) {
                d = output.substring(i - 1, i);
                c = output.substring(i - 2, i - 1);
                b = output.substring(i - 3, i - 2);
                a = output.substring(i - 4, i - 3);

                if (!",".equals(d) && !",".equals(c) && ",".equals(b) && !",".equals(a))
                {
                    endpoint = i;
                    done = true;
                }
                else
                {
                    i--;
                }
            }

            output = output.substring(1, endpoint);
            output = new StringBuffer(output).reverse().toString();
            output = output.replaceFirst(",", "\\.");
            output = new StringBuffer(output).reverse().toString();
        }
        return output;
    }

    //Checks the validity of a name String and displays an optional error if invalid
    boolean CheckNameField(String Name, boolean popup)
    {
        if (Name.length() < 1)
        {
            if (popup == true)
            {
                JOptionPane.showMessageDialog(null, "The 'Name' Field is empty\n" + "This field must be filled in to continue", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        if (Name.matches("^[a-zA-Z0-9!@#$%^&'(){}`~+=_,. -;]+$") == true)
        {
            return true;
        }
        else
        {
            if (popup == true)
            {
                JOptionPane.showMessageDialog(null, "The 'Name' Field contains an illegal character\n" + "This field only accepts letters, numbers, and the following characters\n" + "!, @, #, $, %, ^, &, ', (, ), {, }, -, +, =, _, ., ;, `, ~", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    //Checks the validity of a number String and displays an optional error if invalid
    boolean CheckNumberField(String Number, boolean popup)
    {
        if (Number.length() < 1)
        {
            if (popup == true)
            {
                JOptionPane.showMessageDialog(null, "The 'Number' Field is empty\n" + "This field must be filled in to continue", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        if (Number.matches("[0-9]+") == true)
        {
            return true;
        }
        else
        {
            if (popup == true)
            {
                JOptionPane.showMessageDialog(null, "The 'Number' Field contains an illegal character\n" + "This field only accepts numeric digits\n", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    //Checks the validity of an amount String and displays an optional error if invalid
    boolean CheckAmountField(String Amount, boolean popup)
    {
        if (Amount.length() < 1)
        {
            if (popup == true)
            {
                JOptionPane.showMessageDialog(null, "The 'Amount' Field is empty\n" + "This field must be filled in to continue", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        if (Amount.matches("^[0-9.,]+$") == true)
        {
            return true;
        }
        else
        {
            if (popup == true)
            {
                JOptionPane.showMessageDialog(null, "The 'Amount' Field contains an illegal character\n" + "This field only accepts numeric digits and decimals\n", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    //Checks the validity of a date String and displays an optional error if invalid
    boolean CheckDateField(String Date, boolean popup)
    {
        if (!Date.substring(7).equals(" "))
        {
            String DateArray[] = Date.split("/");
            int Month = 0;
            int Day = 0;
            if (!"  ".equals(DateArray[0]))
            {
                Month = Integer.parseInt(DateArray[0].replaceAll("\\s+",""));
                Day = Integer.parseInt(DateArray[1].replaceAll("\\s+",""));
            }
            if (DateArray[0].length() == 2 && DateArray[1].length() == 2 && DateArray[2].length() == 2 && Month >= 1 && Month <= 12 && Day >= 1 && Day <= 31)
            {
                return true;
            }
            else
            {
                if (popup == true)
                {
                    JOptionPane.showMessageDialog(null, "The 'Date' Field is not filled out properly\n" + "This field must contain a date in the folowoing format\n" + "MM/dd/yy  i.e. 01/01/13", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return false;
            }
        }
        else
        {
            if (popup == true)
            {
                JOptionPane.showMessageDialog(null, "The 'Date' Field is not filled out properly\n" + "This field must contain a date in the folowoing format\n" + "MM/dd/yy  i.e. 01/01/13", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }
    
    //Returns an array that consists of the combined values of two given arrays
    //with an optional seperator string
    String[] arrayConcatenate(String[] first, String[] second, String separator)
    {
        int len = first.length;
        String[] result = new String[len];
        if (len == second.length)
        {
            int i = 0;
            while (i < len)
            {
                result[i] = first[i] + separator + second[i];
                i++;
            }
            return result;
        }
        else
        {
            return result;
        }
    }
    String[] arrayConcatenate(String[] first, String[] second)
    {
        return arrayConcatenate(first, second, "");
    }
}