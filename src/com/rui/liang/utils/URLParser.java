package com.rui.liang.utils;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.lombardi.online.model.gwt.FileAttachmentCommonUtils;
import com.lombardi.online.security.TemporaryTokenManager;

import static org.junit.Assert.assertEquals;

/**
 * TODO: Provide a class description.
 *
 * @author liangrui
 */
public class URLParser {

    //private static final Pattern URL_REGEX = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
    //@see https://tools.ietf.org/html/rfc3986#appendix-B
    private static final Pattern URL_REGEX = Pattern.compile("src=\"(([^:/?#]+):)?(//([^/?#]*))" + FileAttachmentCommonUtils.SECURE_URL_PREFIX + "(.*[?])(.*)");
    private static final Pattern RELATIVE_URL_REGEX = Pattern.compile("(src=\".." + FileAttachmentCommonUtils.PUBLIC_URL_PREFIX + "|" + 
                "src=\"" + FileAttachmentCommonUtils.SECURE_URL_PREFIX + ")" + "(.*[?])(.*)");
    public String getAuthority(String uriString) {
        String result = uriString;
        Matcher urlMatcher = URL_REGEX.matcher(uriString);
        if (urlMatcher.find()) {
            result = urlMatcher.replaceFirst("src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&$6");
        }
        return result;
    }
    
    private static final String webServerAddress = "https://localhost:8443";
    
    public String replaceImageTag(String urlString) {
        String result = urlString;
        Matcher urlMatcher = URL_REGEX.matcher(urlString);
        Matcher relativeMatcher = RELATIVE_URL_REGEX.matcher(urlString);
        // Rewrite the url to add in a temporary token the download servlet can use for downloading this file.
        // Also convert the path to use the public url so it doesn't require authentication
        if (urlMatcher.find()) {
            result = urlMatcher.replaceFirst("src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&$6");
        } else if (relativeMatcher.find()) {
            result = relativeMatcher.replaceFirst("src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&$3");
        }
        return result;
    }
    
    public String getIdentityProviderServerAddress() {
        return "https://www.idp.qa.bpm.ibm.com";
    }
    
    public String getTemporaryDownloadToken() {
        return "61928562281f4d555c98f2d15901bbccc3ef58bb6aee6585";
    }
    
    @Test
    public void testReplaceImageTag() throws URISyntaxException {
        String[] testStrings = {
                "src=\"https://www.blueworkslive.com:443/scr/download/image.png?processId=27260008&fileItemId=272b112d",
                "src=\"https://www.blueworkslive.com:443/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d",
                "src=\"https://www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d",
                "src=\"/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d",
                "src=\"/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"../scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"./scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"https://www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com",
                "src=\"https://www.blueworkslive.com",
                "src=\"blueworkslive.com",
                "src=\"https://bwlqa-web-sp1.qa.bpm.ibm.com/scr/download/small_logo.jpg?processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"https://upload.wikimedia.org/wikipedia/commons/c/ce/Konqueror4_Logo.png",
                "src=\"https://www.idp.qa.bpm.ibm.com/scr/download/small_logo.jpg?processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"../download/small_logo.jpg?processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"https://localhost:8443/scr/download/wukong.jpg?processId=ab0010&fileItemId=ac0007",
        };

        String[] expectedArray = {
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d#param=123",
                "src=\"../scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"./scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com",
                "src=\"https://www.blueworkslive.com",
                "src=\"blueworkslive.com",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"https://upload.wikimedia.org/wikipedia/commons/c/ce/Konqueror4_Logo.png",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=ab0010&fileItemId=ac0007",
        };

        for (int index = 0; index < testStrings.length; index++) {
            String testString = testStrings[index];
            String uri = replaceImageTag(testString);
            assertEquals(expectedArray[index], uri.toString());
        }
    }
    
    @Test
    public void testGetAuthority() throws URISyntaxException {
        String[] testStrings = {
                "src=\"https://www.blueworkslive.com:443/scr/download/image.png?processId=27260008&fileItemId=272b112d",
                "src=\"https://www.blueworkslive.com:443/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d",
                "src=\"https://www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d",
                "src=\"/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d",
                "src=\"/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"../scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"./scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"https://www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com",
                "src=\"https://www.blueworkslive.com",
                "src=\"blueworkslive.com",
                "src=\"https://bwlqa-web-sp1.qa.bpm.ibm.com/scr/download/small_logo.jpg?processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"https://upload.wikimedia.org/wikipedia/commons/c/ce/Konqueror4_Logo.png",
                "src=\"https://www.idp.qa.bpm.ibm.com/scr/download/small_logo.jpg?processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"../download/small_logo.jpg?processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"https://localhost:8443/scr/download/wukong.jpg?processId=ab0010&fileItemId=ac0007",
        };

        String[] expectedArray = {
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d",
                "src=\"/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"../scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"./scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com",
                "src=\"https://www.blueworkslive.com",
                "src=\"blueworkslive.com",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"https://upload.wikimedia.org/wikipedia/commons/c/ce/Konqueror4_Logo.png",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"../download/small_logo.jpg?processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"" + webServerAddress + FileAttachmentCommonUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + TemporaryTokenManager.URL_PARAM + "=" + getTemporaryDownloadToken() + "&processId=ab0010&fileItemId=ac0007",
        };

        for (int index = 0; index < testStrings.length; index++) {
            String testString = testStrings[index];
            String uri = getAuthority(testString);
            assertEquals(expectedArray[index], uri.toString());
        }
    }

}
