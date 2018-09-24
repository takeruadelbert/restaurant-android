package com.example.stn_com_01.orderfoodapp.Helper;

/*
This Helper Class is genereally to provide any useful function which was used frequently.
 */

import android.os.Build;
import android.text.Html;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Helper {
    // Reference : https://stackoverflow.com/questions/6502759/how-to-strip-or-escape-html-tags-in-android
    public String strip_html_tag(String text) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(text).toString();
        }
    }

    public String number_separator(double nominal) {
        DecimalFormat df = new DecimalFormat("#,###");
        String result = df.format(nominal);
        return result;
    }

    public String IDR(double nominal) {
        return "Rp. " + number_separator(nominal);
    }
}
