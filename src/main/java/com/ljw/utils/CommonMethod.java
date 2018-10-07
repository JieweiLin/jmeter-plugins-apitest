package com.ljw.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author 林杰炜 linjiewei
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @date 2018/10/5 20:34
 */
public class CommonMethod {

    public static void sleep(int time) {
        try {
            time *= 1000;
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

    public static List<String> getSheetNames(String excelPath) throws IOException {
        return POIUtil.getSheetName(excelPath);
    }

    public static List<HashMap<String, String>> readExcelSheet(String excelPath, String sheetName) throws IOException {
        return POIUtil.readExcel(excelPath, sheetName);
    }

    public static void writeList(String excelPath, String sheetName, String header, List<List<String>> list) throws IOException {
        POIUtil.createExcel(excelPath, sheetName);
        POIUtil.updateExcel(excelPath, sheetName, header, list);
    }

    public static boolean isContain(String[] list, String str) {
        for (int i = 0; i < list.length; i++) {
            String a = list[i];
            if (str.equals(a)) {
                return true;
            }
        }
        return false;
    }
}
