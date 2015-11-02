package com.test;

import com.util.StringUtil;

/**
 * @author nidayu
 * @Description:
 * @date 2015/11/2
 */
public class Test {

    public static void main(String[] args){
        String text = "nidsayu\"dsands\"das";
        System.out.println(StringUtil.subStr("u\"", "\"", text));
    }
}
