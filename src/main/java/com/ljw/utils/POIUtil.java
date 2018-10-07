package com.ljw.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * @author 林杰炜 linjiewei
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @date 2018/10/5 8:23
 */
public class POIUtil {

    private static void writeData(Row row, int col, String content) {
        Cell cell = row.createCell(col);
        cell.setCellValue(content);
    }

    public static void createExcel(String excelPath, String sheetName) throws IOException {
        if (!new File(excelPath).exists()) {
            Workbook workbook = getWorkBook2(excelPath);
            workbook.createSheet(sheetName);
            FileOutputStream os = new FileOutputStream(excelPath);
            workbook.write(os);
            os.close();
            workbook.close();
        }
    }

    public static void updateExcel(String excelPath, String sheetName, String header, List<List<String>> list) throws IOException {
        FileInputStream fis = new FileInputStream(excelPath);
        Workbook workbook = getWorkBook(excelPath);
        Sheet sheet = workbook.getSheet(sheetName);

        int k = 0;
        Row row = sheet.createRow(0);
        String[] arrayOfString;
        int j = (arrayOfString = header.split(",")).length;
        for (int i = 0; i < j; i++) {
            String entry = arrayOfString[i];
            Cell cell = row.createCell(k);
            cell.setCellValue(entry);
            k++;
        }
        int i = sheet.getPhysicalNumberOfRows();
        for (List<String> rowData : list) {
            int l = 0;
            row = sheet.createRow(i);
            for (String entry : rowData) {
                writeData(row, l, entry);
                l++;
            }
            i++;
        }
        FileOutputStream os = new FileOutputStream(excelPath);
        workbook.write(os);
        fis.close();
        os.close();
        workbook.close();
    }

    private static Workbook getWorkBook(String excelPath) throws IOException {
        String suffix = StringOperateUtil.getLastValue(excelPath, "\\.");
        File file = new File(excelPath);
        Workbook workbook = null;
        if (Constants.SUFFIX_XLSX.equals(suffix)) {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        }
        if (Constants.SUFFIX_XLS.equals(suffix)) {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        }
        return workbook;
    }

    private static Workbook getWorkBook2(String excelPath) {
        String suffix = StringOperateUtil.getLastValue(excelPath, "\\.");
        Workbook workbook = null;
        if (Constants.SUFFIX_XLSX.equals(suffix)) {
            workbook = new XSSFWorkbook();
        }
        if (Constants.SUFFIX_XLS.equals(suffix)) {
            workbook = new HSSFWorkbook();
        }
        return workbook;
    }

    public static List<String> getSheetName(String excelPath) throws IOException {
        Workbook workbook = getWorkBook(excelPath);

        int num = workbook.getNumberOfSheets();
        List<String> lists = Lists.newArrayList();
        for (int i = 0; i < num; i++) {
            lists.add(workbook.getSheetName(i));
        }
        workbook.close();
        return lists;
    }

    public static List<HashMap<String, String>> readExcel(String excelPath, String sheetName) throws IOException {
        Workbook workbook = getWorkBook(excelPath);

        List<HashMap<String, String>> data = Lists.newArrayList();
        List<String> header = Lists.newArrayList();
        Sheet sheet = workbook.getSheet(sheetName);
        String value = "";
        Row row = null;
        Cell cell = null;

        DecimalFormat nf = new DecimalFormat("0");
        int lastRows = sheet.getPhysicalNumberOfRows();

        Row firstRow = sheet.getRow(0);
        for (int k = 0; k < firstRow.getLastCellNum(); k++) {
            cell = firstRow.getCell(k);
            header.add(cell.toString());
        }

        for (int i = 1; i < lastRows; i++) {
            HashMap<String, String> rowData = Maps.newHashMap();
            row = sheet.getRow(i);
            if (row != null) {
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    if (cell == null) {
                        rowData.put(header.get(j), "");
                    } else {
                        switch (cell.getCellTypeEnum()) {
                            case STRING:
                                value = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                value = nf.format(cell.getNumericCellValue());
                                break;
                            default:
                                value = cell.getStringCellValue();
                                break;
                        }
                        rowData.put(header.get(j), value);
                    }
                }
                data.add(rowData);
            }
        }
        workbook.close();
        return data;
    }

    public static void main(String[] args) throws IOException {
        List<HashMap<String, String>> result = readExcel("H:\\JMeter\\那些年，追寻Jmeter的足迹\\jmeter插件\\jmeter接口字段自动化校验\\api-testcase-demo.xlsx", "local-json");
        System.out.println(result.toString());
    }
}
