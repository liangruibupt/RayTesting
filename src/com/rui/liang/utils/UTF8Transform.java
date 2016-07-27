package com.rui.liang.utils;

public class UTF8Transform {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        UTF8Transform test1 = new UTF8Transform();
        String cn_str = "三极管01   电阻02  散热器01    MyTitle";
        String utf8_str = test1.toUTF(cn_str);
        utf8_str = "\u7b56\u7565 SSLTransport, HTTPTransport \r\n\u8fd9\u662f\u9ed8\u8ba4\u7b56\u7565\u8bbe\u7f6e\uff0c\u7528\u4e8e\u5b9e\u73b0\u5728 JAX-WS \u5bfc\u5165\u7ed1\u5b9a\u4e2d\u643a\u5e26\u7528\u6237\u540d\u7684\u4ee4\u724c\u57fa\u672c\u8eab\u4efd\u9a8c\u8bc1\u3002";
        test1.jUTF8toUTF8(utf8_str);

    }

    public void printBytes(byte[] array, String name) {
        String str1 = null;
        String str2 = null;
        for (int k = 0; k < array.length; k++) {
            if(k % 2 == 0){
                //str1 = str1 + "&#x" + UnicodeFormatter.byteToHex(array[k]) ;
                //str2 = str2 + "/u" + UnicodeFormatter.byteToHex(array[k]);
                str1 = str1 + "&#x" + array[k] ;
                str2 = str2 + "/u" + array[k];
            }else{
                //str1 = str1 + UnicodeFormatter.byteToHex(array[k])+ ";" ;
                //str2 = str2 + UnicodeFormatter.byteToHex(array[k]);
                str1 = str1 + array[k]+ ";" ;
                str2 = str2 + array[k];
            }
            
        }
        System.out.println(name + ":" + str1);
        System.out.println(name + ":" + str2);
    }
    
    public String toUTF(String   inPara){  
        char   temChr;  
        int   ascChr;  
        int   i;  
        String   rtStr=new   String(""); 
        String   javaStr=new   String(""); 
        if(inPara==null){  
            inPara="";  
        }  
        for(i=0;i<inPara.length();i++){  
            temChr=inPara.charAt(i);  
            ascChr=temChr+0;  
            rtStr=rtStr+"&#x"+Integer.toHexString(ascChr)+";"; 
            javaStr=javaStr+"\\u"+Integer.toHexString(ascChr); 
        }
        System.out.println("rtStr: "+rtStr);
        System.out.println("javaStr: "+javaStr); 
        return javaStr;
    }   
    
    public void jUTF8toUTF8(String inPara){
        char   temChr; 
        int   ascChr; 
        String   rtStr=new   String("");
        if(inPara==null){  
            inPara="";  
        }  
        for(int i=0;i<inPara.length();i++){  
            temChr=inPara.charAt(i);  
            if(temChr == '\\'){
                rtStr = rtStr + ";&#x";
                i++;
            }
            else{
                rtStr = rtStr + temChr;
            }
        }
        System.out.println("rtStr: "+rtStr);
    }
}
