package com.ljw.utils;

import com.google.common.collect.Lists;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author 林杰炜 linjiewei
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @date 2018/10/5 20:44
 */
public class TestFrame {

    private static final String SPECIAL_STR = "!@#$%^&*()_+|-=\\\\{}:\\\"<>?[];',./";

    public static String testApiByForm(String url, String method, JSONObject headers, JSONObject params) throws IOException {
        if ("post".equals(method) || "POST".equals(method)) {
            HttpRequestUtil.PostBuilder post = HttpRequestUtil.withPostBuilder();
            post.withUrl(url);
            Iterator<String> it = headers.keys();
            while (it.hasNext()) {
                String key = it.next();
                String val = headers.getString(key);
                post.withHeader(key, val);
            }
            Iterator<String> it2 = params.keys();
            while (it2.hasNext()) {
                String key = it2.next();
                String val = params.getString(key);
                post.withParam(key, val);
            }
            return post.buildAndPrint().get("Response");
        }
        if ("get".equals(method) || "GET".equals(method)) {
            HttpRequestUtil.GetBuilder get = HttpRequestUtil.withGetBuilder();
            get.withUrl(url);
            Iterator<String> it = headers.keys();
            while (it.hasNext()) {
                String key = it.next();
                String val = headers.getString(key);
                get.withHeader(key, val);
            }
            Iterator<String> it2 = params.keys();
            while (it2.hasNext()) {
                String key = it2.next();
                String val = params.getString(key);
                get.withParam(key, val);
            }
            return get.buildAndPrint().get("Response");
        }
        return "";
    }

    public static String testApiByJson(String url, String method, JSONObject headers, JSONObject params) throws IOException {
        if ("post".equals(method) || "POST".equals(method)) {
            HttpRequestUtil.PostBuilder post = HttpRequestUtil.withPostBuilder();
            post.withUrl(url);
            Iterator<String> it = headers.keys();
            while (it.hasNext()) {
                String key = it.next();
                String val = headers.getString(key);
                post.withHeader(key, val);
            }
            post.withJsonStr(params.toString());
            return post.buildAndPrint().get("Response");
        }
        if ("get".equals(method) || "GET".equals(method)) {
            HttpRequestUtil.GetBuilder get = HttpRequestUtil.withGetBuilder();
            get.withUrl(url);
            Iterator<String> it = headers.keys();
            while (it.hasNext()) {
                String key = it.next();
                String val = headers.getString(key);
                get.withHeader(key, val);
            }
            Iterator<String> it2 = params.keys();
            while (it2.hasNext()) {
                String key = it2.next();
                String val = params.getString(key);
                get.withParam(key, val);
            }
            return get.buildAndPrint().get("Response");
        }
        return "";
    }

    public static void testBoundary(String path, String sheetName) throws IOException {
        List<HashMap<String, String>> originData = CommonMethod.readExcelSheet(path, sheetName);
        for (HashMap<String, String> data : originData) {
            String url = (String) data.get("url");
            String method = (String) data.get("method");
            String headers = (String) data.get("headers");
            String params = (String) data.get("params");
            String uniqueParams = (String) data.get("uniqueParams");
            String checkParam = (String) data.get("checkParam");
            String fieldType = (String) data.get("fieldType");
            String isNullable = (String) data.get("isNullable");
            String min = (String) data.get("min");
            String max = (String) data.get("max");
            String checkValue = (String) data.get("checkValue");
            String reportPath = (String) data.get("reportPath");
            String ifCheckBoundary = (String) data.get("ifCheckBoundary");

            testBoundary(url, method, headers, params, uniqueParams, checkParam, fieldType, isNullable, min, max, checkValue, reportPath, ifCheckBoundary);
        }
    }

    private static void testBoundary(String url, String method, String headers, String params, String uniqueParams, String checkParam, String fieldType, String isNullable, String min, String max, String checkValue, String reportPath, String ifCheckBoundary) {
        if ("YES".equals(ifCheckBoundary) || "yes".equals(ifCheckBoundary)) {
            testBoundary(url, method, headers, params, uniqueParams, checkParam, fieldType, isNullable, min, max, checkValue, reportPath);
        }
    }

    private static void testBoundary(String url, String method, String headers, String params, String uniqueParams, String checkParam, String fieldType, String isNullable, String mins, String maxs, String checkValue, String reportPath) {
        String minStr = "";
        String maxStr = "";
        String minStr1 = "";
        String maxStr1 = "";
        String mustStr = "";

        int min = 0;
        int max = 0;
        if (!mins.isEmpty()) {
            min = Integer.valueOf(mins).intValue();
        }
        if (!maxs.isEmpty()) {
            max = Integer.valueOf(maxs).intValue();
        }
        JSONObject paramsJson = null;
        if ("{".equals(params.substring(0, 1))) {
            paramsJson = new JSONObject(params);
        } else {
            paramsJson = new JSONObject(StringOperateUtil.getJsonStr(params));
        }

        String response = "";
        String result = "";
        String saveParamStr = "";

        JSONObject headersJson = new JSONObject(StringOperateUtil.getJsonStr(headers));

        List<List<String>> list = Lists.newArrayList();
        if (Constants.IS_NULLABLE_NO.equals(isNullable) || Constants.IS_NULLABLE_NO1.equals(isNullable)) {
            mustStr = "yes";
        }
        if (Constants.FIELD_TYPE_NUM.equals(fieldType)) {
            if (!mins.isEmpty()) {
                minStr = String.valueOf(min);
                minStr1 = String.valueOf(min - 1);
            }
            if (!maxs.isEmpty()) {
                maxStr = String.valueOf(max);
                maxStr1 = String.valueOf(max + 1);
            }
        } else if (Constants.FIELD_TYPE_STR.equals(fieldType)) {
            if (!mins.isEmpty()) {
                if (min > 1) {
                    minStr = StringOperateUtil.getRandomStringV2(min);
                    minStr1 = StringOperateUtil.getRandomStringV2(min - 1);
                    mustStr = "yes";
                }
                if (min == 1) {
                    minStr = StringOperateUtil.getRandomStringV2(min);
                    mustStr = "yes";
                }
            }
            if ((!maxs.isEmpty()) && (max > 1)) {
                maxStr = StringOperateUtil.getRandomStringV2(max);
                maxStr = StringOperateUtil.getRandomStringV2(max + 1);
            }
        }

    }
}
