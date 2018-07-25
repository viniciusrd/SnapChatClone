package br.com.vinicius.android.snapchatclone.Util;

import java.net.MalformedURLException;

public class BuildUrl {

    public static java.net.URL stringToURL(String urlString){
        try{
            java.net.URL url = new java.net.URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }
}
