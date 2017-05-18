package com.hainantaxi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by develop on 16/8/1.
 */
public class RegexPwd {

    public static boolean checkPassword(String password){
        if(password.matches("\\w+")){
            Pattern p= Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,12}$");
            Matcher m=p.matcher(password);
            return m.find();
        }else{
            return false;
        }

    }
}
