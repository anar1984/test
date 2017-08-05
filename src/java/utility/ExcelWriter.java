/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Lenovo
 */
public class ExcelWriter {

    FileUpload fu = new FileUpload();

    public String convertJsonToExcel(String json, String templateName) {

        File excelFile = new File(fu.getUploadPath() + templateName + ".xls");

        FileInputStream file = null;

        try {
            file = new FileInputStream(excelFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet;
        if (excelFile.exists() && file != null) {
            try {
                workbook = new XSSFWorkbook(file);
            } catch (IOException ex) {
                Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        sheet = workbook.getSheetAt(0);
        CellStyle tableCellStyle = workbook.createCellStyle();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCellStyle = workbook.createCellStyle();

        headerCellStyle.setFillForegroundColor(this.getColorCode(210, 210, 210));
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);

        tableCellStyle.setBorderTop(BorderStyle.THIN);
        tableCellStyle.setBorderBottom(BorderStyle.THIN);
        tableCellStyle.setBorderRight(BorderStyle.THIN);
        tableCellStyle.setBorderLeft(BorderStyle.THIN);

        int colCount;
        int rowIdx = 1;

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();

//        System.out.println("============parse eledi");
//        System.out.println(jsonObject.toString());

        if (jsonObject.entrySet().isEmpty()) {
            return null;
        }

        HashMap<String, Integer> columnIds = new HashMap<>();

        JsonArray headerArray = jsonObject.getAsJsonArray("c");
        Row row = sheet.createRow(rowIdx++);
        int colIdx = 0;
        for (JsonElement jsonElement : headerArray) {
            String columnName = ((JsonObject) jsonElement).get("n").getAsString();
            String columnId = ((JsonObject) jsonElement).get("i").getAsString();
//            System.out.println("colId => " + columnId);
            if (!columnId.trim().toLowerCase().equals("id")) {
                Cell cell = row.createCell(colIdx);
                columnIds.put(columnId, colIdx);
                cell.setCellValue(columnName);
                cell.setCellStyle(headerCellStyle);
                colIdx++;
            }

        }
        colCount = colIdx;

        JsonArray contentArray = jsonObject.getAsJsonArray("r");
        for (JsonElement jsonElement : contentArray) {
            JsonObject rowObject = (JsonObject) jsonElement;
            Set<Map.Entry<String, JsonElement>> rowEntries = rowObject.entrySet();
            colIdx = 0;
            row = sheet.createRow(rowIdx++);
            for (Map.Entry<String, JsonElement> rowEntry : rowEntries) {
                String key = rowEntry.getKey();
                if (columnIds.containsKey(key)) {
                    colIdx = columnIds.get(key);
                    Cell cell = row.createCell(colIdx);
                    cell.setCellValue(rowObject.get(key).getAsString());
                    cell.setCellStyle(tableCellStyle);

                }
            }
        }
        for (int i = 0; i < colCount; i++) {
            sheet.autoSizeColumn(i, true);
        }

        String name = "file" + fu.getCurrentDateTime()+ ".xls";
//        String name = "print" + ".xls";

        String fileName = fu.getUploadPath() + name;
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            return name;
        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
        }

        return "";
    }

    private short getColorCode(int r, int g, int b) {
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFPalette palette = hwb.getCustomPalette();
        // get the color which most closely matches the color you want to use
        HSSFColor myColor = palette.findSimilarColor(r, g, b);
        // get the palette index of that color 
        short palIndex = myColor.getIndex();
        // code to get the style for the cell goes here
        return palIndex;
    }

}
