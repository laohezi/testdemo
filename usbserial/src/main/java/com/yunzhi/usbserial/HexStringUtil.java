package com.yunzhi.usbserial;

public class HexStringUtil {

    /** 
     * @Title:bytes2HexString 
     * @Description:字节数组转16进制字符串 
     * @param b 
     *            字节数组 
     * @return 16进制字符串 
     * @throws 
     */  
    public static String bytes2HexString(byte[] b) {  
        StringBuffer result = new StringBuffer();  
        String hex;  
        for (int i = 0; i < b.length; i++) {  
            hex = Integer.toHexString(b[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            result.append(hex.toUpperCase());  
        }  
        return result.toString();  
    }  
    
    /** 
     * @Title:hexString2Bytes 
     * @Description:16进制字符串转字节数组 
     * @param src 
     *            16进制字符串 
     * @return 字节数组 
     * @throws 
     */  
    public static byte[] hexString2Bytes(String src) {  
        int l = src.length() / 2;  
        byte[] ret = new byte[l];  
        for (int i = 0; i < l; i++) {  
            ret[i] = (byte) Integer  
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();  
        }  
        return ret;  
    }  
    
    /** 
     * @Title:string2HexString 
     * @Description:字符串转16进制字符串 
     * @param strPart 
     *            字符串 
     * @return 16进制字符串 
     * @throws 
     */  
    public static String string2HexString(String strPart) {  
//        StringBuffer hexString = new StringBuffer();  
//        for (int i = 0; i < strPart.length(); i++) {  
//            int ch = (int) strPart.charAt(i);  
//            String strHex = Integer.toHexString(ch);  
//            hexString.append(strHex);  
//        }  
//        return hexString.toString();  
        
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = strPart.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }  
    
    /** 
     * @Title:hexString2String 
     * @Description:16进制字符串转字符串 
     * @param src 
     *            16进制字符串 
     * @return 字节数组 
     * @throws 
     */  
    public static String hexString2String(String src) {  
//        String temp = "";  
//        for (int i = 0; i < src.length() / 2; i++) {  
//            temp = temp  
//                    + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),  
//                            16).byteValue();  
//        }  
//        return temp;  
        
        String str = "0123456789ABCDEF";
        char[] hexs = src.toCharArray();
        byte[] bytes = new byte[src.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }  
    

    public static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
    /** 
     * Convert hex string to byte[] 
     * @param hexString the hex string 
     * @return byte[] 
     */  
    public static byte[] hexStringToBytes(String hexString) {  
        if (hexString == null || hexString.equals("")) {  
            return null;  
        }  
        hexString = hexString.toUpperCase();  
        int length = hexString.length() / 2;  
        char[] hexChars = hexString.toCharArray();  
        byte[] d = new byte[length];  
        for (int i = 0; i < length; i++) {  
            int pos = i * 2;  
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
        }  
        return d;  
    }  
    /** 
     * Convert char to byte 
     * @param c char 
     * @return byte 
     */  
     private static byte charToByte(char c) {  
        return (byte) "0123456789ABCDEF".indexOf(c);  
    }
    
}
