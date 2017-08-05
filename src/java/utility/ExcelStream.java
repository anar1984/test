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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author candide
 */
public class ExcelStream {

    public static String PATH = "C:\\Users\\candide\\Desktop\\FAB\\excel\\";
    public static final String TEMPLATE1 = "FAB RECEIPTS TEMPLATE";
    public static final String TEMPLATE2 = "FAB RECEIPTS TEMPLATE-1";
    public static final String EXCEL_EXTENSION = ".xls";

    static DecimalFormat df = new DecimalFormat("#.####");

    public static String convertJsonToExcel(String json) throws IOException {
        File excelFile = new File("C:\\Users\\candide\\Desktop\\Cədvəl.xlsx");

        FileInputStream file = null;
        try {
            file = new FileInputStream(excelFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelStream.class.getName()).log(Level.SEVERE, null, ex);
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet;
        if (excelFile.exists() && file != null) {
            workbook = new XSSFWorkbook(file);
        }

        sheet = workbook.getSheetAt(0);
        CellStyle tableCellStyle = workbook.createCellStyle();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCellStyle = workbook.createCellStyle();

        headerCellStyle.setFillForegroundColor(getColorCode(210, 210, 210));
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
            Cell cell = row.createCell(colIdx);
            columnIds.put(columnId, colIdx);
            cell.setCellValue(columnName);
            cell.setCellStyle(headerCellStyle);
            colIdx++;
        }
        colCount = colIdx;

        JsonArray contentArray = jsonObject.getAsJsonArray("r");
        for (JsonElement jsonElement : contentArray) {
            JsonObject rowObject = (JsonObject) jsonElement;
            Set<Map.Entry<String, JsonElement>> rowEntries = rowObject.entrySet();
//            colIdx = 0;
            row = sheet.createRow(rowIdx++);
            for (Map.Entry<String, JsonElement> rowEntry : rowEntries) {
                String key = rowEntry.getKey();
                colIdx = columnIds.get(key);
                Cell cell = row.createCell(colIdx);
                cell.setCellValue(rowObject.get(key).getAsString());
                cell.setCellStyle(tableCellStyle);
//                String key = rowEntry.getKey();
//                System.out.println(key);
//                Cell cell = row.createCell(colIdx++);
//                JsonElement celElm = ((JsonObject) rowObject.get(key)).get("value");
//                if (celElm != null) {
//                    String s = celElm.getAsString();
//                    cell.setCellValue(s);
//                }
//                cell.setCellStyle(tableCellStyle);
            }
        }
        for (int i = 0; i < colCount; i++) {
            sheet.autoSizeColumn(i, true);
        }

        String fileName = "C:\\Users\\candide\\Desktop\\Cədvəl.xlsx";
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            return fileName;
        } catch (IOException ex) {
            Logger.getLogger(ExcelStream.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static short getColorCode(int r, int g, int b) {
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFPalette palette = hwb.getCustomPalette();
        // get the color which most closely matches the color you want to use
        HSSFColor myColor = palette.findSimilarColor(r, g, b);
        // get the palette index of that color 
        short palIndex = myColor.getIndex();
        // code to get the style for the cell goes here
        return palIndex;
    }

    public static String getProductionReport(
            String templateName,
            String ingridientsJsonString,
            String ambalajJsonString,
            String qualityJsonString)
            throws Exception {

        int ingridientsRowStartIdx = 6;
        int ambalajRowStartIdx = 11;
        int qualityRowStartIdx = 17;

        FileUpload fu = new FileUpload();
        PATH = fu.getUploadPath();

        File templateFile = getTemplateFile(templateName);
        if (templateFile == null) {
            throw new Exception("There is no such a template: " + templateName);
        }

        HashMap<String, HashMap<String, String>> qualityTable = new HashMap<>();
        qualityTable.put("TOLER.", new HashMap<>());
        qualityTable.put("STAND.", new HashMap<>());
        qualityTable.put("MEAN.", new HashMap<>());
        qualityTable.put("MIN.", new HashMap<>());
        qualityTable.put("MAX.", new HashMap<>());

        Workbook workbook = WorkbookFactory.create(templateFile);
        Sheet sheet = workbook.getSheetAt(0);

        CellStyle productNameCellStyle = workbook.createCellStyle();
        Font productNameFont = workbook.createFont();
        productNameFont.setFontName("Arial Black");
        productNameFont.setFontHeight((short) (20 * 20));
        productNameCellStyle.setFont(productNameFont);
        productNameCellStyle.setAlignment(HorizontalAlignment.LEFT);

        CellStyle ordinaryCellStyle = workbook.createCellStyle();
        Font ordinaryFont = workbook.createFont();
        ordinaryFont.setFontName("Arial");
        ordinaryFont.setFontHeight((short) (14 * 20));
        ordinaryCellStyle.setFont(ordinaryFont);

        Cell productNameCell = sheet.getRow(2).getCell(0);
        productNameCell.setCellValue("Mehsul adi bura gedecek");

        Cell productColorCell = sheet.getRow(2).getCell(3);
        productColorCell.setCellValue("Reng");

        Cell productNameCodeCell = sheet.getRow(3).getCell(0);
//        productNameCodeCell.setCellValue("PARKE MOBİLYA TUTKALİ");
        productNameCodeCell.setCellValue("");

        Cell productCodeCell = sheet.getRow(3).getCell(3);
        productCodeCell.setCellValue("741-1000");

        Cell productNumberCell = sheet.getRow(4).getCell(1);
        productNumberCell.setCellValue("product number");

        Cell productDNumberCell = sheet.getRow(4).getCell(4);
        productDNumberCell.setCellValue("F.D.N. number");

        JsonParser parser = new JsonParser();
        JsonObject ingridientsObject = parser.parse(ingridientsJsonString).getAsJsonObject();

        if (ingridientsObject.entrySet().isEmpty()) {
            return null;
        }

        int ingridientsRowCount = ingridientsObject.getAsJsonObject("b").get("rowCount").getAsInt();
        if (ingridientsRowCount == 0) {

        }

        JsonArray ingridientsArray = ingridientsObject.get("res").getAsJsonObject().get("r").getAsJsonArray();

        if (ingridientsArray.size() > 0) {
            String mainProductName = ingridientsArray.get(0).getAsJsonObject().get("mainProductName").getAsString();
            String mainProductCode = ingridientsArray.get(0).getAsJsonObject().get("mainProductCode").getAsString();
            productNameCell.setCellValue(mainProductName);
            productCodeCell.setCellValue(mainProductCode);
        }

        Cell ingCodeCell = sheet.getRow(ingridientsRowStartIdx).getCell(0);
        CellStyle ingCodeCellStyle = ingCodeCell.getCellStyle();

        Cell ingProductNameCell = sheet.getRow(ingridientsRowStartIdx).getCell(1);
        CellStyle ingProductNameCellStyle = ingProductNameCell.getCellStyle();

        Cell ingProductAmountCell = sheet.getRow(ingridientsRowStartIdx).getCell(3);
        CellStyle ingProductAmountCellStyle = ingProductAmountCell.getCellStyle();

        Cell ingDescriptionCell = sheet.getRow(ingridientsRowStartIdx).getCell(8);
        CellStyle ingDescriptionCellStyle = ingDescriptionCell.getCellStyle();
        ingDescriptionCellStyle.setAlignment(HorizontalAlignment.LEFT);

        Cell ingProductPersentageCell = sheet.getRow(ingridientsRowStartIdx).getCell(7);
        CellStyle ingProductPersentageCellStyle = ingProductPersentageCell.getCellStyle();
        ingProductPersentageCellStyle.setAlignment(HorizontalAlignment.RIGHT);

        String ingCode;
        String ingDescription;
        String ingProductName;
        String ingProductAmount;
        String ingProductPersentage;

        double totalAmount = 0;
        double totalPercentage = 0;

        for (int i = 0; i < ingridientsRowCount; i++) {
            JsonObject ingridientRowObject = ingridientsArray.get(i).getAsJsonObject();

            ingCode = trimLeadingAndEndingDoubleQuotes(ingridientRowObject.get("childProductCode").toString());
            ingDescription = trimLeadingAndEndingDoubleQuotes(ingridientRowObject.get("childDescription").toString());
            ingProductName = trimLeadingAndEndingDoubleQuotes(ingridientRowObject.get("childProductName").toString());
            ingProductAmount = trimLeadingAndEndingDoubleQuotes(ingridientRowObject.get("childProductAmount").toString());
            ingProductPersentage = trimLeadingAndEndingDoubleQuotes(ingridientRowObject.get("childProductPercentage").toString());

            if (i > 0) {
                insertRow(sheet, ingridientsRowStartIdx + i, 1);
            }

            ingCodeCell = sheet.getRow(ingridientsRowStartIdx + i).createCell(0);
            ingCodeCell.setCellType(CellType.STRING);
            ingCodeCell.setCellValue(ingCode);
            ingCodeCell.setCellStyle(ingCodeCellStyle);

            ingProductNameCell = sheet.getRow(ingridientsRowStartIdx + i).createCell(1);
            ingProductNameCell.setCellValue(ingProductName);
            ingProductNameCell.setCellStyle(ingProductNameCellStyle);

            try {
                totalAmount += Double.valueOf(ingProductAmount);
            } catch (Exception e) {
            }

            ingProductAmountCell = sheet.getRow(ingridientsRowStartIdx + i).createCell(3);
            ingProductAmountCell.setCellValue(ingProductAmount);
            ingProductAmountCell.setCellStyle(ingProductAmountCellStyle);

            ingDescriptionCell = sheet.getRow(ingridientsRowStartIdx + i).createCell(6);
            ingDescriptionCell.setCellValue(ingDescription);
            ingDescriptionCell.setCellStyle(ingDescriptionCellStyle);
            try {
                totalPercentage += Double.valueOf(ingProductPersentage);
            } catch (Exception e) {
            }
            ingProductPersentageCell = sheet.getRow(ingridientsRowStartIdx + i).createCell(8);
            ingProductPersentageCell.setCellValue(ingProductPersentage);
            ingProductPersentageCell.setCellStyle(ingProductPersentageCellStyle);

            if (i > 0) {
                sheet.addMergedRegion(new CellRangeAddress(ingridientsRowStartIdx + i, ingridientsRowStartIdx + i, 1, 2));
                sheet.addMergedRegion(new CellRangeAddress(ingridientsRowStartIdx + i, ingridientsRowStartIdx + i, 3, 4));
                sheet.addMergedRegion(new CellRangeAddress(ingridientsRowStartIdx + i, ingridientsRowStartIdx + i, 6, 7));

                ambalajRowStartIdx++;
                qualityRowStartIdx++;
            }
        }
        sheet.getRow(ingridientsRowStartIdx + ingridientsRowCount).getCell(3).setCellValue(totalAmount);
        sheet.getRow(ingridientsRowStartIdx + ingridientsRowCount).getCell(8).setCellValue(totalPercentage);

        JsonObject ambalajObject = parser.parse(ambalajJsonString).getAsJsonObject();
        if (ambalajObject.entrySet().isEmpty()) {
            return null;
        }

        int ambalajRowCount = ambalajObject.getAsJsonObject("b").get("rowCount").getAsInt();
        if (ambalajRowCount == 0) {

        }

        JsonArray ambalajArray = ambalajObject.get("res").getAsJsonObject().get("r").getAsJsonArray();

        Cell ambalajProductNameCell = sheet.getRow(ambalajRowStartIdx).getCell(0);
        CellStyle ambalajProductNameCellStyle = ambalajProductNameCell.getCellStyle();

        Cell ambalajBarcodeCell = sheet.getRow(ambalajRowStartIdx).getCell(1);
        CellStyle ambalajBarcodeCellStyle = ambalajBarcodeCell.getCellStyle();

        Cell ambalajWeightCell = sheet.getRow(ambalajRowStartIdx).getCell(3);
        CellStyle ambalajWeightCellStyle = ambalajWeightCell.getCellStyle();

        Cell ambalajAmountCell = sheet.getRow(ambalajRowStartIdx).getCell(4);
        CellStyle ambalajAmountCellStyle = ambalajAmountCell.getCellStyle();

        Cell lineNetCell = sheet.getRow(ambalajRowStartIdx).getCell(5);
        CellStyle lineNetCellStyle = lineNetCell.getCellStyle();

        String ambalajAmount;
        String ambalajBarcode;
        String ambalajWeight;
        String productName;
        String lineNet;
        double totalAmbalajAmount = 0;
        try {
            sheet.addMergedRegion(new CellRangeAddress(ambalajRowStartIdx + -1, ambalajRowStartIdx - 1, 6, 7));
        } catch (Exception e) {
        }

        for (int i = 0; i < ambalajRowCount; i++) {
            JsonObject ambalajRowObject = ambalajArray.get(i).getAsJsonObject();

            ambalajAmount = trimLeadingAndEndingDoubleQuotes(ambalajRowObject.get("ambalajAmount").getAsString());
            ambalajBarcode = trimLeadingAndEndingDoubleQuotes(ambalajRowObject.get("barkod").getAsString());
            ambalajWeight = trimLeadingAndEndingDoubleQuotes(ambalajRowObject.get("weight").getAsString());
            productName = trimLeadingAndEndingDoubleQuotes(ambalajRowObject.get("afterProductName").getAsString());

            double tmp = 0;
            try {
                tmp = Double.parseDouble(ambalajWeight) * Double.parseDouble(ambalajAmount);
                tmp = Double.valueOf(df.format(tmp));
            } catch (Exception e) {
            }
            totalAmbalajAmount += tmp;

            lineNet = String.valueOf(tmp);

            if (i > 1) {
                insertRow(sheet, ambalajRowStartIdx + i, 1);
            }

            ambalajProductNameCell = sheet.getRow(ambalajRowStartIdx + i).getCell(0);
            ambalajProductNameCell.setCellStyle(ambalajProductNameCellStyle);
            ambalajProductNameCell.setCellValue(productName);

            ambalajBarcodeCell = sheet.getRow(ambalajRowStartIdx + i).getCell(1);
            ambalajBarcodeCell.setCellStyle(ambalajBarcodeCellStyle);
            ambalajBarcodeCell.setCellValue(ambalajBarcode);

            ambalajWeightCell = sheet.getRow(ambalajRowStartIdx + i).getCell(3);
            ambalajWeightCell.setCellStyle(ambalajWeightCellStyle);
            ambalajWeightCell.setCellValue(ambalajWeight);

            ambalajAmountCell = sheet.getRow(ambalajRowStartIdx + i).getCell(4);
            ambalajAmountCell.setCellStyle(ambalajAmountCellStyle);
            ambalajAmountCell.setCellValue(ambalajAmount);

            lineNetCell = sheet.getRow(ambalajRowStartIdx + i).getCell(5);
            lineNetCell.setCellStyle(lineNetCellStyle);
            lineNetCell.setCellValue(lineNet);

            if (i > 1) {
                qualityRowStartIdx++;
            }
            try {
                sheet.addMergedRegion(new CellRangeAddress(ambalajRowStartIdx + i, ambalajRowStartIdx + i, 6, 7));
            } catch (Exception e) {
            }
        }

        lineNetCell = sheet.getRow(ambalajRowStartIdx-1).getCell(8);
        lineNetCell.setCellStyle(lineNetCellStyle);
        lineNetCell.setCellValue(totalAmount);
        
        lineNetCell = sheet.getRow(ambalajRowStartIdx).getCell(8);
        lineNetCell.setCellStyle(lineNetCellStyle);
        lineNetCell.setCellValue(totalAmbalajAmount);


        try {
            sheet.addMergedRegion(new CellRangeAddress(ambalajRowStartIdx + ambalajRowCount, ambalajRowStartIdx + ambalajRowCount, 6, 7));
        } catch (Exception e) {
        }

        JsonObject qualityObject = parser.parse(qualityJsonString).getAsJsonObject();
        if (qualityObject.entrySet().isEmpty()) {
            return null;
        }

        int qualityRowCount = qualityObject.getAsJsonObject("b").get("rowCount").getAsInt();
        if (qualityRowCount == 0) {

        }

        JsonArray qualityArray = qualityObject.get("res").getAsJsonObject().get("r").getAsJsonArray();
        Cell qualityMinCell = sheet.getRow(qualityRowStartIdx).getCell(3);

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();

        String qualityMinValue;
        String qualityMeanValue;
        String qualityInsertDate = dateFormat.format(date);
        String qualityStandardNameValue;
        String qualityStandardValueValue;
        String qualityTolerantValueValue;
        String qualityMaxValue;
        String qualityProductCodeValue;
        String qualityDescriptionCodeValue;

        int standardNameIdx = 0;
        int standIdx = 1;
        int tolerIdx = 2;
        int minIdx = 3;
        int meanIdx = 4;
        int maxIdx = 5;

        for (int i = 0; i < qualityRowCount; i++) {
            JsonObject qualityRowObject = qualityArray.get(i).getAsJsonObject();

            qualityMinValue = trimLeadingAndEndingDoubleQuotes(qualityRowObject.get("minValue").getAsString());
            qualityMeanValue = trimLeadingAndEndingDoubleQuotes(qualityRowObject.get("meanValue").getAsString());
            qualityStandardNameValue = trimLeadingAndEndingDoubleQuotes(qualityRowObject.get("standardName").getAsString());
            qualityStandardValueValue = trimLeadingAndEndingDoubleQuotes(qualityRowObject.get("standardValue").getAsString());
            qualityTolerantValueValue = trimLeadingAndEndingDoubleQuotes(qualityRowObject.get("tolerantValue").getAsString());
            qualityMaxValue = trimLeadingAndEndingDoubleQuotes(qualityRowObject.get("maxValue").getAsString());
            qualityProductCodeValue = trimLeadingAndEndingDoubleQuotes(qualityRowObject.get("productCode").getAsString());
            qualityDescriptionCodeValue = trimLeadingAndEndingDoubleQuotes(qualityRowObject.get("description").getAsString());

            String standColumnName = qualityStandardNameValue.toUpperCase();
            qualityTable.get("TOLER.").put(standColumnName, qualityTolerantValueValue);
            qualityTable.get("STAND.").put(standColumnName, qualityStandardValueValue);
            qualityTable.get("MIN.").put(standColumnName, qualityMinValue);
            qualityTable.get("MEAN.").put(standColumnName, qualityMeanValue);
            qualityTable.get("MAX.").put(standColumnName, qualityMaxValue);

        }

        int rowCount = getNeededRowCountOfQualityTable(qualityTable);
        if (rowCount > 1) {
            for (int i = 1; i < rowCount; i++) {
                insertRow(sheet, qualityRowStartIdx, 1);
            }
        }

        HashMap<String, Integer> map = new HashMap<>();
        int i = 0;
        for (String key : qualityTable.get("STAND.").keySet()) {
            sheet.getRow(qualityRowStartIdx + i).getCell(standardNameIdx).setCellValue(key);
            map.put(key, i++);
        }
        int qualityCount = qualityRowStartIdx + i;
        sheet.autoSizeColumn(0);

        //populatiing stand
        for (String key : qualityTable.get("STAND.").keySet()) {
            sheet.getRow(qualityRowStartIdx + map.get(key)).getCell(standIdx).setCellValue(qualityTable.get("STAND.").get(key));
        }
        //populatiing toler
        for (String key : qualityTable.get("TOLER.").keySet()) {
            sheet.getRow(qualityRowStartIdx + map.get(key)).getCell(tolerIdx).setCellValue(qualityTable.get("TOLER.").get(key));
        }
        //populatiing min
        for (String key : qualityTable.get("MIN.").keySet()) {
            sheet.getRow(qualityRowStartIdx + map.get(key)).getCell(minIdx).setCellValue(qualityTable.get("MIN.").get(key));
        }
        //populatiing mean
        for (String key : qualityTable.get("MEAN.").keySet()) {
            sheet.getRow(qualityRowStartIdx + map.get(key)).getCell(meanIdx).setCellValue(qualityTable.get("MEAN.").get(key));
        }
        //populatiing max
        for (String key : qualityTable.get("MAX.").keySet()) {
            sheet.getRow(qualityRowStartIdx + map.get(key)).getCell(maxIdx).setCellValue(qualityTable.get("MAX.").get(key));
        }

        for (int u = 1; u <= 5; u++) {
            sheet.getRow(qualityCount).getCell(u).setCellValue(qualityInsertDate);
        }

        sheet.setDefaultRowHeight((short) (52 * 20));

        String fileName = "report" + QDate.getCurrentDate() + QDate.getCurrentTime() + EXCEL_EXTENSION;
        String fullFilename = PATH + fileName;
        try (FileOutputStream outputStream = new FileOutputStream(fullFilename)) {
            workbook.write(outputStream);
            return fileName;
        } catch (IOException ex) {
            Logger.getLogger(ExcelStream.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static File getTemplateFile(String templateName) {

        String fileName = PATH + templateName + EXCEL_EXTENSION;
        File templateFile = new File(fileName);

        if (!templateFile.exists()) {
            return null;
        }

        return templateFile;
    }

    private static void insertRow(Sheet sh, int rowIndex, int step) {
        sh.shiftRows(rowIndex, sh.getLastRowNum(), step);
        Row prevRow = sh.getRow(rowIndex - 1);
        Row currRow = sh.getRow(rowIndex);
        for (int i = 0; i < sh.getRow(rowIndex - 1).getLastCellNum(); i++) {
            Cell oldCell = prevRow.getCell(i);
            Cell newCell = currRow.createCell(i);
            if (oldCell != null) {
                newCell.setCellStyle(prevRow.getCell(i).getCellStyle());
            }
        }
    }

    private static String trimLeadingAndEndingDoubleQuotes(String str) {
        str = str.replaceAll("^\"+", "");
        str = str.replaceAll("\"+$", "");
        return str;
    }

    private static int getNeededRowCountOfQualityTable(HashMap<String, HashMap<String, String>> qualityTable) {
        Integer rowCount = getMaxOf(
                qualityTable.get("TOLER.").size(),
                qualityTable.get("STAND.").size(),
                qualityTable.get("MIN.").size(),
                qualityTable.get("MEAN.").size(),
                qualityTable.get("MAX.").size()
        );

        if (rowCount == null) {
            return 0;
        }
        return rowCount;
    }

    private static Integer getMaxOf(Integer... numbers) {
        if (numbers == null || numbers.length == 0) {
            return null;
        }
        Integer max = numbers[0];

        for (Integer number : numbers) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

}
