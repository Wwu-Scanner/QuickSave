/*
This class gets a cetain date based upon the input provided when called. It is
used to get the current fiscal year documents are saved by, along with the
given month.
*/

package QuickSave;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

public class DateProcessing {
    
    //Returns the year that the current fiscal year ends on
    int GetCurrentFiscalYearEnd()
    {
        int End, DateYear, DateMonth;
        java.util.Date DateNow = new java.util.Date();
        SimpleDateFormat MM = new SimpleDateFormat ("MM");
        SimpleDateFormat yyyy = new SimpleDateFormat ("yyyy");
        DateMonth = Integer.parseInt(MM.format(DateNow));
        DateYear = Integer.parseInt(yyyy.format(DateNow));
        if (DateMonth>6) {
            End = DateYear+1;
        }else{
            End = DateYear;
        }
        return End;
    }
    
    //Returns the name of the given month
    String GetMonthName(int m)
    {
        String month = "invalid";
        m = m-1;
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11 ) {
            month = months[m];
        }
        return month;
    }
    
    //Returns the year that the fiscal year of the given date ends on
    int GetFiscalYear(int month, int year)
    {
        int FiscalYear = year;
        if (month > 6)
        {
            FiscalYear = year + 1;
        }
        return FiscalYear;
    }
    
}
