package com.rui.liang.utils;

import java.net.URISyntaxException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TODO: Provide a class description.
 *
 * @author liangrui
 */
public class URLParserUtilsTest {

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
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d#param=123",
                "src=\"../scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"./scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com",
                "src=\"https://www.blueworkslive.com",
                "src=\"blueworkslive.com",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"https://upload.wikimedia.org/wikipedia/commons/c/ce/Konqueror4_Logo.png",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=ab0010&fileItemId=ac0007",
        };

        for (int index = 0; index < testStrings.length; index++) {
            String testString = testStrings[index];
            String uri = URLParserUtils.replaceImageTag(testString);
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
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d",
                "src=\"/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d",
                "src=\"/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"../scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"./scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/scr/download/_Atch-Space in ${Name}(\u7e41\u9ad4)test.png?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com/?processId=27260008&fileItemId=272b112d#param=123",
                "src=\"www.blueworkslive.com",
                "src=\"https://www.blueworkslive.com",
                "src=\"blueworkslive.com",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"https://upload.wikimedia.org/wikipedia/commons/c/ce/Konqueror4_Logo.png",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"../download/small_logo.jpg?processId=5f6002f4d3509&fileItemId=5f6002f4d352f",
                "src=\"" + URLParserUtils.getWebServerAddress() + URLParserUtils.RESTRICTED_DOWNLOAD_URL_PREFIX + "?" + URLParserUtils.URL_PARAM + "=" + URLParserUtils.getTemporaryDownloadToken() + "&processId=ab0010&fileItemId=ac0007",
        };

        for (int index = 0; index < testStrings.length; index++) {
            String testString = testStrings[index];
            String uri = URLParserUtils.getAuthority(testString);
            assertEquals(expectedArray[index], uri.toString());
        }
    }

}
