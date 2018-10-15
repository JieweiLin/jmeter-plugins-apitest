package com.ljw.utils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author 林杰炜 linjiewei
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @date 2018/10/5 8:28
 */
public class StringOperateUtil {

    private static final String PHONE_PREFIX = "";
    private static final String EMAIL_SUFFIX = "";
    private static final String FAMILY_NAME = "";
    private static final String NAME = "";
    private static final String SEX = "";
    private static String phone;

    public static String getLastValue(String text, String separator) {
        String[] texts = text.split(separator);
        return texts[(texts.length - 1)];
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static String getCurrentTime() {
        String format = "yyyy-MM-dd HH:mm:ss";
        return getCurrentTime(format);
    }

    public static String getJsonStr(String params) {
        String[] str = params.split("&");
        JSONObject json = new JSONObject();
        if (!params.isEmpty()) {
            for (int i = 0; i < str.length; i++) {
                String value = str[i];
                int index = value.indexOf("=");
                String key = value.substring(0, index);
                String val = value.substring(index + 1, value.length());
                json.put(key, val);
            }
        }
        return json.toString();
    }

    public static String getRandomStringV2(int length) {
        String base = "qwertyuiopasdfghjklzxcvbnm1234567890";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(base.length());
            sb.append(base.charAt(num));
        }
        return sb.toString();
    }

    public static JSONObject getChangeJsonFromUniqueParams2(String uniqueParams, JSONObject paramsJson) {
        String[] uniqueParamsList = uniqueParams.split("&");
        if (!"".equals(uniqueParams)) {
            for (int i = 0; i < uniqueParamsList.length; i++) {
                String key = uniqueParamsList[i];
                String initial = paramsJson.getString(key);
                String newStr = getRandomStringV2(initial.length());
                paramsJson.put(key, newStr);
            }
        }
        return paramsJson;
    }

    public static String getStrFromJson(String s) {
        JSONObject json = new JSONObject(s);
        String result = "";
        for (String key : json.keySet()) {
            String val = json.getString(key);
            result += key + "="+val + "&";
        }
        return result.substring(0, result.length()-1);
    }

    public static String getFilterStr(String specialStr, String specialList) {
        for (int i = 0; i < specialList.length(); i++) {
            char item = specialList.charAt(i);
            String temp = String.valueOf(item);
            specialStr = specialStr.replace(temp, "");
        }
        return specialStr;
    }

    public static JSONObject getChangeJsonFromUniqueParams(String uniqueParam, JSONObject paramsJson) {
        String[] uniqueParamsList = uniqueParam.split("&");
        if (!"".equals(uniqueParam)) {
            for (int i = 0; i < uniqueParamsList.length; i++) {
                String key = uniqueParamsList[i];
                String initial = paramsJson.getString(key);
                String newStr = initial + getCurrentTime("mmssSSS");
                paramsJson.put(key, newStr);
            }
        }
        return paramsJson;
    }

    public static String getPhone() {
        String phone1 = getRandowStr("130,131,132,133,134,135,136,137,138,158,186,188,189", ",");

        return phone1 + getRandow(10000000, 99999999);
    }

    private static int getRandow(int i, int i1) {
        Random random = new Random();
        if (i != 0) {
            int s = random.nextInt(i1) % (i1 - i + 1) + i;
            return s;
        }
        return random.nextInt(i1 + 1);
    }

    private static String getRandowStr(String s, String s1) {
        String[] originalStr2 = s.split(s1);
        int index = getRandow(0, originalStr2.length - 1);
        return originalStr2[index];
    }
}
