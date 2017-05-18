package com.hainantaxi.utils;

import android.text.TextUtils;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexUtil {


    /**
     */

    public static String getPicWidth(String url) {

        Pattern pt = Pattern.compile("_w_[0-9]*");
        Matcher m = pt.matcher(url);
        Pattern pt1 = Pattern.compile("[^0-9]");
        if (m.find()) {
            Matcher m1 = pt1.matcher(m.group());
            return m1.replaceAll("").trim();
        } else {
            return "-2";
        }
    }

    public static String getPicHeight(String url) {
        Pattern pt = Pattern.compile("_h_[0-9]*");
        Matcher m = pt.matcher(url);
        Pattern pt1 = Pattern.compile("[^0-9]");
        if (m.find()) {
            Matcher m1 = pt1.matcher(m.group());
            return m1.replaceAll("").trim();
        } else {
            return "-2";
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "^[1][3-9]+\\d{9}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex) || mobiles.startsWith("23333");
        }
    }

    /**
     * 过滤评论中的用户 @张三
     *
     * @param text
     * @return
     */
    public static ArrayList<String> getUser(String text) {

        String telRegex = "@[^ @]+";

        Pattern pat = Pattern.compile(telRegex);

        Matcher mat = pat.matcher(text);

        ArrayList<String> s = new ArrayList<>();

        while (mat.find()) {
            s.add(mat.group());
        }
        return s;
    }



    public static String gettbLink(String text) {
        String str = "";

        String regx = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

        Pattern pat = Pattern.compile(regx, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher mat = pat.matcher(text);

        if (mat.find()) {
            str = mat.group(0).toString();
        }


        return str;
    }

}
