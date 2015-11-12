package com.util;

/**
 * @author nidayu
 * @Description:
 * @date 2015/11/2
 */
public class StringUtil {

    /**
     * 字符串截取！
     *
     * @param stext 开始字符串
     * @param etext 结束字符串
     * @param text  文本内容
     * @return
     */
    public static String subStr(String stext, String etext, String text) {
        int sindex = text.indexOf(stext);
        if (sindex >= 0) {
            int eindex = text.indexOf(etext, sindex + stext.length());
            if (eindex >= 0) {
                String ctext = text.substring(sindex + stext.length(), eindex);
                return ctext;
            }
        }
        return "";
    }

    /**
     * 转换\\u6316\\u8d22类型的Unicode字符！
     *
     * @param theString
     * @return
     */
    public static String unicodeDecode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);

                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.print(replaceToChinese(1023));
    }

    /**
     * 数字转汉字
     * @param amountPart
     * @return
     */
    public static String replaceToChinese(int amountPart) {
        String[] str={"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        if(amountPart < 0 || amountPart > 10000) {
            throw new IllegalArgumentException("参数必须是大于等于 0，小于 10000 的整数！");
        }
        String[] units = new String[] {"", "十", "百", "千"};
        int temp = amountPart;
        String amountStr = new Integer(amountPart).toString();
        int amountStrLength = amountStr.length();
        boolean lastIsZero = true; //在从低位往高位循环时，记录上一位数字是不是 0
        String chineseStr = "";
        for(int i=0; i<amountStrLength; i++) {
            if(temp == 0)  // 高位已无数据
                break;
            int digit = temp % 10;
            if(digit == 0) { // 取到的数字为 0
                if(!lastIsZero)  //前一个数字不是 0，则在当前汉字串前加“零”字;
                    chineseStr = "零" + chineseStr;
                lastIsZero = true;
            } else { // 取到的数字不是 0
                chineseStr = str[digit] + units[i] + chineseStr;
                lastIsZero = false;
            }
            temp = temp / 10;
        }
        return chineseStr;
    }
}
