package com.icerrate.popularmovies.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ivan Cerrate.
 */

public class FormatUtils {

    public static final String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";

    public static final String FORMAT_MMM_dd_yyyy = "MMM', 'dd' 'yyyy";

    public static String formatDate(String date, String inputFormat, String outputFormat){
        if (date != null && !date.isEmpty()) {
            try {
                SimpleDateFormat inFormat = new SimpleDateFormat(inputFormat);
                Date newDate = inFormat.parse(date);

                SimpleDateFormat outformat = new SimpleDateFormat(outputFormat);
                return outformat.format(newDate);
            } catch (ParseException e) {
                return null;
            }
        } else {
          return null;
        }
    }
}
