package com.util;

/**
 * @author nidayu
 * @Description:
 * @date 2015/11/2
 */
public class StringUtil {

    /**
     * �ַ�����ȡ��
     * @param stext ��ʼ�ַ���
     * @param etext �����ַ���
     * @param text �ı�����
     * @return
     */
    public static String subStr(String stext, String etext, String text) {
        int sindex = text.indexOf(stext);
        if (sindex >= 0) {
            int eindex = text.indexOf(etext, sindex+stext.length());
            if (eindex >= 0) {
                String ctext = text.substring(sindex + stext.length(), eindex);
                return ctext;
            }
        }
        return "";
    }
}
