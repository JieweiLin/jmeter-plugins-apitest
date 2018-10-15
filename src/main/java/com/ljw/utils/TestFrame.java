package com.ljw.utils;

import com.google.common.collect.Lists;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

    private static void testBoundary(String url, String method, String headers, String params, String uniqueParams, String checkParam, String fieldType, String isNullable, String min, String max, String checkValue, String reportPath, String ifCheckBoundary) throws IOException {
        if ("YES".equals(ifCheckBoundary) || "yes".equals(ifCheckBoundary)) {
            testBoundary(url, method, headers, params, uniqueParams, checkParam, fieldType, isNullable, min, max, checkValue, reportPath);
        }
    }

    private static void testBoundary(String url, String method, String headers, String params, String uniqueParams, String checkParam, String fieldType, String isNullable, String mins, String maxs, String checkValue, String reportPath) throws IOException {
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
        if (!"".equals(maxStr)) {
            paramsJson.put(checkParam, maxStr);
            if ("{".equals(params.substring(0, 1))) {
                response = testApiByJson(url, method, headersJson, paramsJson);
                saveParamStr = paramsJson.toString();
            } else {
                paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams2(uniqueParams, paramsJson);
                response = testApiByForm(url, method, headersJson, paramsJson);
                saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
            }
            if (response.contains(checkValue)) {
                result = "pass";
            } else {
                result = "failed";
            }
            List<String> rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response, result, checkParam + "边界校验，最大边界" + max);
            list.add(rowData);
        }
        if (!"".equals(minStr1)) {
            paramsJson.put(checkParam, minStr1);
            if ("{".equals(params.substring(0, 1))) {
                response = testApiByJson(url, method, headersJson, paramsJson);
                saveParamStr = paramsJson.toString();
            } else {
                paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams2(uniqueParams, paramsJson);
                response = testApiByForm(url, method, headersJson, paramsJson);
                saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
            }
            if (!response.contains(checkValue)) {
                result = "pass";
            } else {
                result = "failed";
            }
            List<String> rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response, result, checkParam + "边界校验，最小边界-1");
            list.add(rowData);
        }
        if (!"".equals(maxStr1)) {
            paramsJson.put(checkParam, maxStr1);
            if ("{".equals(params.substring(0, 1))) {
                response = testApiByJson(url, method, headersJson, paramsJson);
                saveParamStr = paramsJson.toString();
            } else {
                paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams2(uniqueParams, paramsJson);
                response = testApiByForm(url, method, headersJson, paramsJson);
                saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
            }
            if (!response.contains(checkValue)) {
                result = "pass";
            } else {
                result = "failed";
            }
            List<String> rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response, result, checkParam + "边界校验，最大边界+1");
            list.add(rowData);
        }
        if ("yes".equals(mustStr)) {
            paramsJson.put(checkParam, "");
            if ("{".equals(params.substring(0, 1))) {
                response = testApiByJson(url, method, headersJson, paramsJson);
                saveParamStr = paramsJson.toString();
            } else {
                paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams2(uniqueParams, paramsJson);
                response = testApiByForm(url, method, headersJson, paramsJson);
                saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
            }
            if (!response.contains(checkValue)) {
                result = "pass";
            } else {
                result = "failed";
            }
            List<String> rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response,
                    result, checkParam + "必填项校验");

            list.add(rowData);
        }
        if (!"".equals(minStr)) {
            paramsJson.put(checkParam, minStr);
            if ("{".equals(params.substring(0, 1))) {
                response = testApiByJson(url, method, headersJson, paramsJson);
                saveParamStr = paramsJson.toString();
            } else {
                paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams2(uniqueParams, paramsJson);
                response = testApiByForm(url, method, headersJson, paramsJson);
                saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
            }
            if (response.contains(checkValue)) {
                result = "pass";
            } else {
                result = "failed";
            }
            List<String> rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response,
                    result, checkParam + "边界校验，最小边界" + min);

            list.add(rowData);
        }
        String header = "url, method, headers, params, checkParam, checkValue, response, result, testName";
        CommonMethod.writeList(reportPath, "test-report", header, list);
    }

    private static List<String> getRowData(String url, String method, String headers, String saveParamStr, String checkParam, String checkValue, String response, String result, String s) {
        List<String> rowData = Lists.newArrayList();
        rowData.add(url);
        rowData.add(method);
        rowData.add(headers);
        rowData.add(saveParamStr);
        rowData.add(checkParam);
        rowData.add(checkValue);
        rowData.add(response);
        rowData.add(result);
        rowData.add(s);
        return rowData;
    }

    public static void testSpecialCharacters(String excelPath, String sheetName) throws IOException {
        List<HashMap<String, String>> originData = CommonMethod.readExcelSheet(excelPath, sheetName);
        for (HashMap<String, String> data : originData) {
            String url = data.get("url");
            String method = data.get("method");
            String headers = data.get("headers");
            String params = data.get("params");
            String uniqueParam = data.get("uniqueParams");
            String checkParam = data.get("checkParam");
            String fieldType = data.get("fieldType");
            String isNullable = data.get("isNullable");
            String specialList = data.get("specialList");
            String checkValue = data.get("checkValue");
            String reportPath = data.get("reportPath");
            String ifCheckSpecial = data.get("ifCheckSpecial");

            testSpecialCharacters(url, method, headers, params, uniqueParam, checkParam, fieldType, isNullable, checkValue, specialList, reportPath, ifCheckSpecial);
        }
    }

    private static void testSpecialCharacters(String url, String method, String headers, String params, String uniqueParam, String checkParam, String fieldType, String isNullable, String checkValue, String specialList, String reportPath, String ifCheckSpecial) throws IOException {
        if ((Objects.equals("YES", ifCheckSpecial) || Objects.equals("yes", ifCheckSpecial)) && Objects.equals("字符串", fieldType)) {
            testSpecialCharacters(url, method, headers, params, uniqueParam, checkParam, fieldType, isNullable, checkValue, specialList, reportPath);
        }
    }

    private static void testSpecialCharacters(String url, String method, String headers, String params, String uniqueParam, String checkParam, String fieldType, String isNullable, String checkValue, String specialList, String reportPath) throws IOException {
        String headersStr = StringOperateUtil.getJsonStr(headers);
        String paramsStr = "";
        if ("{".equals(params.substring(0, 1))) {
            paramsStr = params;
        } else {
            paramsStr = StringOperateUtil.getJsonStr(params);
        }
        JSONObject headersJson = new JSONObject(headersStr);
        String otherSpecialStr = StringOperateUtil.getFilterStr(SPECIAL_STR, specialList);
        String response = "";
        String result = "";
        String temp = "";
        String saveParamStr = "";
        List<List<String>> list = Lists.newArrayList();
        if (!"".equals(specialList)) {
            for (int i = 0; i < specialList.length(); i++) {
                String item = String.valueOf(specialList.charAt(i));
                JSONObject paramsJson = new JSONObject(paramsStr);
                paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams(uniqueParam, paramsJson);
                temp = paramsJson.getString(checkParam);

                paramsJson.put(checkParam, temp + item);
                if ("{".equals(params.substring(0, 1))) {
                    response = testApiByJson(url, method, headersJson, paramsJson);
                    saveParamStr = paramsJson.toString();
                } else {
                    response = testApiByForm(url, method, headersJson, paramsJson);
                    saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
                }
                if (response.contains(checkValue)) {
                    result = "pass";
                } else {
                    result = "failed";
                }
                List<String> rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response, result, checkParam + "特殊字符校验" + item);
                list.add(rowData);
            }
            for (int i = 0; i < otherSpecialStr.length(); i++) {
                String item = String.valueOf(otherSpecialStr.charAt(i));
                JSONObject paramsJson = new JSONObject(paramsStr);
                paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams(uniqueParam, paramsJson);
                temp = paramsJson.getString(checkParam);
                paramsJson.put(checkParam, temp + item);
                if ("{".equals(params.substring(0, 1))) {
                    response = testApiByJson(url, method, headersJson, paramsJson);
                    saveParamStr = paramsJson.toString();
                } else {
                    response = testApiByForm(url, method, headersJson, paramsJson);
                    saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
                }
                if (!response.contains(checkValue)) {
                    result = "pass";
                } else {
                    result = "failed";
                }
                List<String> rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response, result, checkParam + "特殊字符校验" + item);
                list.add(rowData);
            }
        } else {
            for (int i = 0; i < SPECIAL_STR.length(); i++) {
                String item = String.valueOf(SPECIAL_STR.charAt(i));
                JSONObject paramsJson = new JSONObject(paramsStr);
                paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams(uniqueParam, paramsJson);
                temp = paramsJson.getString(checkParam);
                paramsJson.put(checkParam, temp + item);
                if ("{".equals(params.substring(0, 1))) {
                    response = testApiByJson(url, method, headersJson, paramsJson);
                    saveParamStr = paramsJson.toString();
                } else {
                    response = testApiByForm(url, method, headersJson, paramsJson);
                    saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
                }
                if (response.contains(checkValue)) {
                    result = "pass";
                } else {
                    result = "failed";
                }
                List<String> rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response, result, checkParam + "特殊字符校验" + item);
                list.add(rowData);
            }
        }
        String header = "url, method, headers,params,checkParam,checkValue,response,result,testName";
        CommonMethod.writeList(reportPath, "test-report", header, list);
    }

    public static void testValidity(String excelPath, String sheetName) throws IOException {
        List<HashMap<String, String>> originData = CommonMethod.readExcelSheet(excelPath, sheetName);
        for (HashMap<String, String> data : originData) {
            String url = (String) data.get("url");
            String method = (String) data.get("method");
            String headers = (String) data.get("headers");
            String params = (String) data.get("params");
            String uniqueParams = (String) data.get("uniqueParams");
            String checkParam = (String) data.get("checkParam");
            String fieldType = (String) data.get("fieldType");

            String checkValue = (String) data.get("checkValue");
            String reportPath = (String) data.get("reportPath");
            String ifCheckValidity = (String) data.get("ifCheckValidity");
            testValidity(url, method, headers, params, uniqueParams, checkParam, fieldType, checkValue, reportPath,
                    ifCheckValidity);
        }
    }

    private static void testValidity(String url, String method, String headers, String params, String uniqueParams, String checkParam, String fieldType, String checkValue, String reportPath, String ifCheckValidity) throws IOException {
        if (ifCheckValidity.equals("YES")) {
            testValidity(url, method, headers, params, uniqueParams, checkParam, fieldType, checkValue, reportPath);
        }
    }

    private static void testValidity(String url, String method, String headers, String params, String uniqueParams, String checkParam, String fieldType, String checkValue, String reportPath) throws IOException {
        List<List<String>> list = Lists.newArrayList();
        if (fieldType.equals("手机号")) {
            list.add(validityMobilePhone(url, method, headers, params, uniqueParams, checkParam, checkValue, "正"));
            list.add(validityMobilePhone(url, method, headers, params, uniqueParams, checkParam, checkValue, "反"));
        }
        if (fieldType.equals("数字")) {
            list.add(validityNum(url, method, headers, params, uniqueParams, checkParam, checkValue));
        }
        String header = "url, method, headers,params,checkParam,checkValue,response,result,testName";
        CommonMethod.writeList(reportPath, "test-report", header, list);
    }

    private static List<String> validityNum(String url, String method, String headers, String params, String uniqueParams, String checkParam, String checkValue) throws IOException {
        String headersStr = StringOperateUtil.getJsonStr(headers);
        JSONObject headersJson = new JSONObject(headersStr);
        String paramsStr = "";
        if (params.substring(0, 1).equals("{")) {
            paramsStr = params;
        } else {
            paramsStr = StringOperateUtil.getJsonStr(params);
        }
        JSONObject paramsJson = new JSONObject(paramsStr);
        String response = "";
        String saveParamStr = "";
        String result = "";
        List<String> rowData = null;
        paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams(uniqueParams, paramsJson);

        paramsJson.put(checkParam, "test");
        if (params.substring(0, 1).equals("{")) {
            response = testApiByJson(url, method, headersJson, paramsJson);
            saveParamStr = paramsJson.toString();
        } else {
            response = testApiByForm(url, method, headersJson, paramsJson);
            saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
        }
        if (!response.contains(checkValue)) {
            result = "pass";
        } else {
            result = "failed";
        }
        rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response, result,
                checkParam + "数字有效性校验（反）");

        return rowData;
    }

    private static List<String> validityMobilePhone(String url, String method, String headers, String params, String uniqueParams, String checkParam, String checkValue, String s) throws IOException {
        String headersStr = StringOperateUtil.getJsonStr(headers);
        JSONObject headersJson = new JSONObject(headersStr);
        String paramsStr = "";
        if (params.substring(0, 1).equals("{")) {
            paramsStr = params;
        } else {
            paramsStr = StringOperateUtil.getJsonStr(params);
        }
        JSONObject paramsJson = new JSONObject(paramsStr);
        String response = "";
        String saveParamStr = "";
        String result = "";
        List<String> rowData = null;
        paramsJson = StringOperateUtil.getChangeJsonFromUniqueParams(uniqueParams, paramsJson);
        if (s.equals("正")) {
            paramsJson.put(checkParam, StringOperateUtil.getPhone());
            if (params.substring(0, 1).equals("{")) {
                response = testApiByJson(url, method, headersJson, paramsJson);
                saveParamStr = paramsJson.toString();
            } else {
                response = testApiByForm(url, method, headersJson, paramsJson);
                saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
            }
            if (response.contains(checkValue)) {
                result = "pass";
            } else {
                result = "failed";
            }
            rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response, result,
                    checkParam + "手机有效性校验（正）");
        } else {
            paramsJson.put(checkParam, "11111111111");
            if (params.substring(0, 1).equals("{")) {
                response = testApiByJson(url, method, headersJson, paramsJson);
                saveParamStr = paramsJson.toString();
            } else {
                response = testApiByForm(url, method, headersJson, paramsJson);
                saveParamStr = StringOperateUtil.getStrFromJson(paramsJson.toString());
            }
            if (!response.contains(checkValue)) {
                result = "pass";
            } else {
                result = "failed";
            }
            rowData = getRowData(url, method, headers, saveParamStr, checkParam, checkValue, response, result,
                    checkParam + "手机号有效性校验（反）");
        }
        return rowData;
    }
}
