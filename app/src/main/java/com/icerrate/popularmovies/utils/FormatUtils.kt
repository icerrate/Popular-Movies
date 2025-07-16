package com.icerrate.popularmovies.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Ivan Cerrate.
 */
object FormatUtils {

    const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val FORMAT_MMM_DD_YYYY = "MMM', 'dd' 'yyyy"

    fun formatDate(date: String?, inputFormat: String, outputFormat: String): String {
        return if (!date.isNullOrEmpty()) {
            try {
                val inFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
                val newDate = inFormat.parse(date)
                val outFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
                outFormat.format(newDate!!)
            } catch (e: ParseException) {
                "-"
            }
        } else {
            "-"
        }
    }
}