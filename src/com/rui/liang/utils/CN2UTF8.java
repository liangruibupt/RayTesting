package com.rui.liang.utils;

import java.io.IOException;

/**
 * @author lr
 * 
 */
public class CN2UTF8 {

    /**
     * @param args
     * @throws DSConnectorException
     * @throws IOException
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CN2UTF8 test1 = new CN2UTF8();
        String cn_str = "\u00A9\u00A7\u00A3\u00A2\u00A5\u00AE\u00C8\u00C6\u00E5\u00B6";
        String utf8_str = test1.toUTF(cn_str);
        test1.jUTF8toUTF8(utf8_str);
    }

    public String toUTF(String inPara) {
        char temChr;
        int ascChr;
        int i;
        String rtStr = new String("");
        String javaStr = new String("");
        if (inPara == null) {
            inPara = "";
        }
        for (i = 0; i < inPara.length(); i++) {
            temChr = inPara.charAt(i);
            ascChr = temChr + 0;
            rtStr = rtStr + "&#x" + Integer.toHexString(ascChr) + ";";
            javaStr = javaStr + "\\u" + Integer.toHexString(ascChr);
        }
        System.out.println("rtStr: " + rtStr);
        System.out.println("javaStr: " + javaStr);
        return javaStr;
    }

    public void jUTF8toUTF8(String inPara) {
        char temChr;
        int ascChr;
        String rtStr = new String("");
        if (inPara == null) {
            inPara = "";
        }
        for (int i = 0; i < inPara.length(); i++) {
            temChr = inPara.charAt(i);
            if (temChr == '\\') {
                rtStr = rtStr + ";&#x";
                i++;
            } else {
                rtStr = rtStr + temChr;
            }
        }
        System.out.println("rtStr: " + rtStr);
    }
}
