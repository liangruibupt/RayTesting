/*
 * IBM Confidential
 * 
 * OCO Source Materials
 * 
 * 5725-G68
 * 
 * Copyright IBM Corporation 2015. All Rights Reserved.
 * 
 * The source code for this program is not published or otherwise divested of its trade secrets, irrespective of what
 * has been deposited with the U.S. Copyright Office.
 */
package com.rui.liang.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: Provide a class description.
 *
 * @author liangrui
 */
public class URLParserUtils {

    //private static final Pattern URL_REGEX = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
    //@see https://tools.ietf.org/html/rfc3986#appendix-B
    public static final String SECURE_URL_PREFIX = "/scr/download/";
    public static final String URL_PARAM = "secureDownloadToken";
    public static final String PUBLIC_URL_PREFIX = "/download/";
    public static final String RESTRICTED_DOWNLOAD_URL_PREFIX = "/restricteddownload/";
    private static final Pattern URL_REGEX = Pattern.compile("src=\"(([^:/?#]+):)?(//([^/?#]*))" + SECURE_URL_PREFIX + "(.*[?])(.*)");
    private static final Pattern RELATIVE_URL_REGEX = Pattern.compile("(src=\".." + PUBLIC_URL_PREFIX + "|" + 
                "src=\"" + SECURE_URL_PREFIX + ")" + "(.*[?])(.*)");
    public static String getAuthority(String uriString) {
        String result = uriString;
        Matcher urlMatcher = URL_REGEX.matcher(uriString);
        if (urlMatcher.find()) {
            result = urlMatcher.replaceFirst("src=\"" + webServerAddress + RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URL_PARAM + "=" + getTemporaryDownloadToken() + "&$6");
        }
        return result;
    }
    
    private static final String webServerAddress = "https://localhost:8443";
    
    public static String replaceImageTag(String urlString) {
        String result = urlString;
        Matcher urlMatcher = URL_REGEX.matcher(urlString);
        Matcher relativeMatcher = RELATIVE_URL_REGEX.matcher(urlString);
        // Rewrite the url to add in a temporary token the download servlet can use for downloading this file.
        // Also convert the path to use the public url so it doesn't require authentication
        if (urlMatcher.find()) {
            result = urlMatcher.replaceFirst("src=\"" + webServerAddress + RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URL_PARAM + "=" + getTemporaryDownloadToken() + "&$6");
        } else if (relativeMatcher.find()) {
            result = relativeMatcher.replaceFirst("src=\"" + webServerAddress + RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URL_PARAM + "=" + getTemporaryDownloadToken() + "&$3");
        }
        return result;
    }
    
    public static String getIdentityProviderServerAddress() {
        return "https://www.idp.qa.bpm.ibm.com";
    }
    
    public static String getTemporaryDownloadToken() {
        return "61928562281f4d555c98f2d15901bbccc3ef58bb6aee6585";
    }
    
    public static String getWebServerAddress() {
        return webServerAddress;
    }
}
