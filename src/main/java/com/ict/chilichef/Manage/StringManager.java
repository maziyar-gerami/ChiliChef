package com.ict.chilichef.Manage;

import android.support.annotation.NonNull;

public class StringManager {


// ____________________________________________________________________

    public static String stripPersianStringForSearch(String text, boolean removeSpaces, boolean keepCommas) {

        // Punctutions
        text = text.replaceAll("[‘÷×؛،’؟|~!@#$%&*()_`\\-={}\\[\\]:\";'<>?"+ (keepCommas ? "" : ",") +"./«»]", "");
        // Diacritics
        text = text.replaceAll("[ًٌٍَُِْ]", "");
        text = text.replace("للّه", "لله");
        // Quranic Diacritics
        text = text.replaceAll("[ٰۖۗۚۘۙۜ]", "");

        text = text.replace("ّ", "");
        text = text.replaceAll("[إأآ]", "ا");
        text = text.replaceAll("[يىئی]", "ی");
        text = text.replaceAll("[ؤ]", "و");
        text = text.replaceAll("[ة]", "ه");
        text = text.replace("[کككڬﮎﻙﻚ]", "ک");

        // Persian Numbers
        text = text.replace("۰", "0");
        text = text.replace("۱", "1");
        text = text.replace("۲", "2");
        text = text.replace("۳", "3");
        text = text.replace("۴", "4");
        text = text.replace("۵", "5");
        text = text.replace("۶", "6");
        text = text.replace("۷", "7");
        text = text.replace("۸", "8");
        text = text.replace("۹", "9");

        // Arabic Numbers
        text = text.replace("٠", "0");
        text = text.replace("١", "1");
        text = text.replace("٢", "2");
        text = text.replace("٣", "3");
        text = text.replace("٤", "4");
        text = text.replace("٥", "5");
        text = text.replace("٦", "6");
        text = text.replace("٧", "7");
        text = text.replace("٨", "8");
        text = text.replace("٩", "9");

        text = text.replaceAll("\\s+", removeSpaces ? "" : " ").trim();


        return text;
    }

// ____________________________________________________________________

    public static String addPathComponent(String path, @NonNull String component) {

        if(!path.endsWith("/") && !component.startsWith("/"))
            path += "/";
        return path + component;
    }

// ____________________________________________________________________

    public static String addPathComponents(String path, @NonNull String... components) {

        for(String component : components) {

            path = addPathComponent(path, component);
        }

        return path;
    }

//_________________________________________________________________________

    //TODO must works according to current locale
    public static String convertEnglishNumbersToPersian(String text) {

        if(text == null)
            return null;

        text = text.replaceAll("0", "\u06F0");
        text = text.replaceAll("1", "\u06F1");
        text = text.replaceAll("2", "\u06F2");
        text = text.replaceAll("3", "\u06F3");
        text = text.replaceAll("4", "\u06F4");
        text = text.replaceAll("5", "\u06F5");
        text = text.replaceAll("6", "\u06F6");
        text = text.replaceAll("7", "\u06F7");
        text = text.replaceAll("8", "\u06F8");
        text = text.replaceAll("9", "\u06F9");

        return text;
    }

//_________________________________________________________________________

    //TODO must works according to current locale
    public static String convertPersianNumbersTOEnglishNumbers(String text) {

        if(text == null)
            return null;

        text = text.replace("۰", "0");
        text = text.replace("۱", "1");
        text = text.replace("۲", "2");
        text = text.replace("۳", "3");
        text = text.replace("۴", "4");
        text = text.replace("۵", "5");
        text = text.replace("۶", "6");
        text = text.replace("۷", "7");
        text = text.replace("۸", "8");
        text = text.replace("۹", "9");

        // Arabic Numbers
        text = text.replace("٠", "0");
        text = text.replace("١", "1");
        text = text.replace("٢", "2");
        text = text.replace("٣", "3");
        text = text.replace("٤", "4");
        text = text.replace("٥", "5");
        text = text.replace("٦", "6");
        text = text.replace("٧", "7");
        text = text.replace("٨", "8");
        text = text.replace("٩", "9");

        return text;
    }

// ____________________________________________________________________

}

