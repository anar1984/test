/*

 * To change this template, choose Tools | Templates

 * and open the template in the editor.

 */
package utility;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import module.cr.entity.EntityCrUser;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import label.CoreLabel;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utility.sqlgenerator.EntityManager;

public class Carrier {

    private final String THERE_IS_NOT_ANY_KEY_CALLED = "QCarry: There isn't any key called ";
    private final String ROW_AND_COLUMN_INDEX_MUST_BE_POSITIV_NUMBER = "QCarry: Row and column index must be positiv";
    private final String TABLE_KEY_SEPERATOR = ":";
    JsonObject jsonRootObject;
    private final Map<String, Object> params;
    private final Map<String, String> slimits;
    private final Map<String, String> elimits;
    private final Map<String, String> tableRowCount;
    private final Map<String, String> tableSequence;
    private Connection conn;

    private EntityCrUser session;
    private String serviceName = "";
    private String includedFields = "";
    private String excludedFields = "";
    private String username = "";
    private String matrixId = "";

    private final String SEQUENCE = "sequence";
    private final String USER_ERROR_TABLE = "USER_ERROR_TABLE";
    private final String USER_ERROR_NAME = "USER_ERROR_NAME";
    private final String USER_ERROR_VALUE = "val";
    private final String USER_ERROR_CODE = "code";
    private final String USER_ERROR_KEY = "code";
    private final String USER_ERROR_MESSAGE = "message";
    private final String INCLUDED_FIELDS = "includedFields";
    private final String EXCLUDED_FIELDS = "excludedFields";
    private final String SEPERATOR_FIELDS = ",";

    private int tableCount = 0;
    private final String keyNames[] = new String[1000];
    private int keyCount = 0;
    private final String[] tableNamesArray = new String[1000];

    public String[] getKeys() {
        String[] res = new String[keyCount];
        System.arraycopy(keyNames, 0, res, 0, keyCount);
        return res;
    }

    public void copyTo(Carrier carrier) throws QException {
        for (int i = 0; i < keyCount; i++) {
            carrier.setValue(keyNames[i], this.getValue(keyNames[i]));
        }

        for (int i = 0; i < tableCount; i++) {
            String tableName = tableNamesArray[i];
            String[] columns = this.getTableColumnNames(tableName);
            int rowc = this.getTableRowCount(tableName);
            for (int row = 0; row < rowc; row++) {
                for (String col : columns) {
                    carrier.setValue(tableName, row, col,
                            this.getValue(tableName, row, col));
                }
            }
        }

        carrier.addIncludedFields(includedFields.split(SEPERATOR_FIELDS));
        carrier.addExcludedFields(excludedFields.split(SEPERATOR_FIELDS));
        carrier.setServiceName(this.getServiceName());

    }

    public void addIncludedFields(String field) {
        if (!field.trim().equals("")) {
            this.includedFields = this.includedFields + field + SEPERATOR_FIELDS;
            if (!this.includedFields.trim().equals("")) {
                setValue(INCLUDED_FIELDS, this.includedFields);
            }
        }
    }

    public void addIncludedFields(String[] field) {
        for (String fd : field) {
            addIncludedFields(fd);
        }
    }

    public void addExcludedFields(String field) {
        if (!field.trim().equals("")) {
            this.excludedFields = this.excludedFields + field + SEPERATOR_FIELDS;
            if (!this.excludedFields.trim().equals("")) {
                setValue(EXCLUDED_FIELDS, this.excludedFields);
            }
        }
    }

    public void addExcludedFields(String[] field) {
        for (String fd : field) {
            addExcludedFields(fd);
        }
    }

    public Carrier() {
        params = new HashMap<>();
        slimits = new HashMap<>();
        elimits = new HashMap<>();
        tableRowCount = new HashMap<>();
        tableSequence = new HashMap<>();
        serviceName = "";
        isBusy = false;
    }

    public String getMatrixId() {
        return this.matrixId;
    }

    public void setMatrixId(String id) {
        this.matrixId = id;
    }

    public void addTableStartLimit(String tablename, String startLimit) {
        this.slimits.put(tablename, startLimit);
    }

    public void addTableEndLimit(String tablename, String endLimit) {
        this.elimits.put(tablename, endLimit);
    }

    public void addTableRowCount(String tablename, String rowCount) {
        this.tableRowCount.put(tablename, rowCount);
    }

    public void addTableSequence(String tablename, String sequence) {
        this.tableSequence.put(tablename, sequence);
    }

    public void addTableRowCount(String tablename, int rowCount) {
        addTableRowCount(tablename, String.valueOf(rowCount));
    }

    public int getTableRowCount4Json(String tablename) {
        return Integer.parseInt(this.tableRowCount.getOrDefault(tablename, "1"));
    }

    public String getTableStartLimit(String tablename) {
        return slimits.getOrDefault(tablename, "0");
    }

    public String getTableEndLimit(String tablename) {
        return elimits.getOrDefault(tablename, "0");
    }

    public String getTableSequence(String tablename) {
        return tableSequence.getOrDefault(tablename, "");
    }

    public Connection getConnection() {
        return conn;
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    public boolean hasConnection() {
        return conn != null;
    }

    public void addError(String errCode, String errMsg) throws QException {
        int row = this.getTableRowCount(this.USER_ERROR_TABLE);
        this.setValue(this.USER_ERROR_TABLE, row, this.USER_ERROR_KEY, errCode);
        this.setValue(this.USER_ERROR_TABLE, row, this.USER_ERROR_MESSAGE, errMsg);
    }

    public void addSequence(String fieldName) {
        try {
            if (this.isKeyExist(SEQUENCE)) {
                String value = this.getValue(SEQUENCE).toString();
                value += "," + fieldName;
                this.setValue(SEQUENCE, value);
            } else {
                this.setValue(SEQUENCE, fieldName);
            }
        } catch (Exception ex) {
            Logger.getLogger(Carrier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSequence(String[] fieldName) {
        for (String field : fieldName) {
            addSequence(field.trim());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean hasUsername() {
        return !this.username.equals("");
    }

    public String getErrorJson() throws QException {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        int rowCount = this.getTableRowCount(this.USER_ERROR_TABLE);

        try {
            for (int i = 0; i < rowCount; i++) {
                JSONObject jsonRow = new JSONObject();
                jsonRow.put("0", this.getValue(this.USER_ERROR_TABLE, i, this.USER_ERROR_CODE));
                jsonRow.put("1", this.getValue(this.USER_ERROR_TABLE, i, this.USER_ERROR_NAME));
                array.put(jsonRow);
            }
            object.put(this.USER_ERROR_TABLE, array);
        } catch (QException | JSONException ex) {
            System.err.println(ex.getMessage());
        }
        return object.toString();
    }

    public String getXMLString() {
        return XMLString;
    }

    public void removeKey(String key) {
        this.params.remove(key);
    }

    public void setXMLString(String XMLString) {
        this.XMLString = XMLString;
    }
    private String XMLString;

    private String xmlFile;
    private final boolean isBusy;

    //****************************************//
    /////
    public void print() {
        Set keySet = this.params.keySet();
        int size = keySet.size();
        String[] keys = new String[size]; //all the key names
        keySet.toArray(keys);
        Map<String, String> tableNames = new HashMap<>();

//        System.out.println("\n================================");
        for (int i = 0; i < size; i++) {
            String tname = keys[i].split("\\:")[0];
            if (tname.equals(keys[i])) { // keys
//                try {
//                    System.out.println("Keys:");
//                    System.out.println(tname + " " + this.getValue(tname).toString());
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
            } else //tables
             if (!tableNames.containsKey(tname)) {
                    tableNames.put(tname, "");
                    int rowCount = 0, colCount = 0;
                    String[] colNames = null;
                    try {
                        rowCount = this.getTableRowCount(tname);
                        colCount = this.getTableColumnCount(tname);
                        colNames = this.getTableColumnNames(tname);
                    } catch (Exception ex) {
                    }

//                    System.out.println("Group: " + tname);
                    for (int j = 0; j < rowCount; j++) {
                        for (int u = 0; u < colCount; u++) {
                            int idx = j;
                            String colName = colNames[u];
                            try {
//                                System.out.print(tname + ":" + j + ":" + colName);
                                if (this.getValue(tname, j, colName) != null) {
//                                    System.out.println(" -> " + this.getValue(tname, j, colName).toString());
                                } else {
//                                    System.out.println(" -> " + null);
                                }

                            } catch (Exception ex) {
                            }
                        }
                    }
                }
        }
//        System.out.println("================================\n");
    }

    public Map<String, String> getTableNames() {
        Map<String, String> tableNames = new HashMap<>();

        Set keySet = this.params.keySet();
        int size = keySet.size();
        String[] keys = new String[size]; //all the key names
        keySet.toArray(keys);

        for (int i = 0; i < size; i++) {
            String tname = keys[i].split("\\:")[0];
            if (!tname.equals(keys[i])) { //tables
                String[] tableFullName = tname.split("_");
                String name = tableFullName[0];
                String id = tableFullName[1];
                tableNames.put(id, name);
            }
        }
        return tableNames;
    }

    public String[] getTableNamesArray() {
        String[] table = new String[this.tableCount];
        System.arraycopy(this.tableNamesArray, 0, table, 0, table.length);
        return table;
    }

    public void renameTableName(String oldName, String newName) {

        try {
            int rowCount = this.getTableRowCount(oldName);
            String[] cols = this.getTableColumnNames(oldName);

            for (int i = 0; i < rowCount; i++) {
                for (String col : cols) {
                    Object value = this.getValue(oldName, i, col);
                    this.setValue(newName, i, col, value);
                }
            }
            this.deleteTable(oldName);
        } catch (Exception ex) {
        }

    }

    public void renameTableColumn(String tablename, String oldName, String newName) {

        try {
            int rowCount = this.getTableRowCount(tablename);
            String[] cols = this.getTableColumnNames(tablename);

            for (int i = 0; i < rowCount; i++) {
                for (String col : cols) {
                    if (oldName.trim().equals(col)) {
                        String val = this.getValue(tablename, i, col).toString();
                        this.setValue(tablename, i, newName, val);
                        this.params.remove(tablename + ":" + i + ":" + col);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    public void copyTableColumn(String tablename, String colName, String newColName) {

        try {
            int rowCount = this.getTableRowCount(tablename);
            String[] cols = this.getTableColumnNames(tablename);

            for (int i = 0; i < rowCount; i++) {
                for (String col : cols) {
                    if (colName.trim().equals(col)) {
                        String val = this.getValue(tablename, i, col).toString();
                        this.setValue(tablename, i, newColName, val);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }
    
    public void renameKey(String oldName, String newName) {

        try {
            if (this.isKeyExist(oldName)) {
                String value = this.getValue(oldName).toString();
                this.setValue(newName, value);

                this.params.remove(oldName);
                deleteKeyFromList(oldName);
            }
        } catch (Exception ex) {
        }
    }

    private void deleteTableFromList(String tablename) {
        int delInd = -1;
        for (int i = 0; i < tableCount; i++) {
            if (tableNamesArray[i].equals(tablename)) {
                delInd = i;
                break;
            }
        }

        if (delInd != -1) {
            for (int i = delInd; i < tableCount; i++) {
                tableNamesArray[i] = tableNamesArray[i + 1];
            }
            tableCount--;
        }

    }

    private void deleteKeyFromList(String key) {
        int delInd = -1;
        for (int i = 0; i < keyCount; i++) {
            if (keyNames[i].equals(key)) {
                delInd = i;
                break;
            }
        }

        if (delInd != -1) {
            for (int i = delInd; i < keyCount; i++) {
                keyNames[i] = keyNames[i + 1];
            }
            keyCount--;
        }
    }

    public void truncateTable(String tableName) {
        boolean isTableNameExists = false;
        Set keySet = this.params.keySet();
        int size = keySet.size();
        String[] keys = new String[size]; //all the key names
        keySet.toArray(keys);

        try {
            for (int i = 0; i < size; i++) {
                if (keys[i].split("\\:")[0].equals(tableName)) {

                    int rowCount = this.getTableRowCount(tableName);
                    int colCount = this.getTableColumnCount(tableName);
                    for (int j = 0; j < rowCount; j++) {
                        for (int u = 0; u < colCount; u++) {
                            this.setValue(tableName, j, u, null);
                        }
                    }

                    isTableNameExists = true;
                    break;
                }
            }
            if (!isTableNameExists) {
                //System.out.println("Sorry, there is no table named :tableName");
            }
        } catch (Exception ex) {
        }
    }

    public void removeColoumn(String tableName, String column) throws QException {
        int c = this.getTableRowCount(tableName);
        for (int i = 0; i < c; i++) {
            String tname = tableName + ":" + i + ":" + column;
            this.params.remove(tname);
        }
    }

    public void removeRow(String tableName, String column, Object value) throws QException {
        Map<String, Object> temp = new HashMap<>();
        int idx = 0;
        int c = this.getTableRowCount(tableName);
        for (int i = 0; i < c; i++) {
            if (this.getValue(tableName, i, column).toString().equals(value)) {
                continue;
            }
            String[] cols = this.getTableColumnNames(tableName);
            for (String col : cols) {
                String tname = tableName + ":" + idx + ":" + col;
                temp.put(tname, this.getValue(tableName, i, col).toString());
            }
            idx++;
        }

        //replace new values of the table
        this.deleteTable(tableName);

        temp.keySet().stream().forEach((key) -> {
            this.params.put(key, temp.get(key));
        });
    }

    public void removeRow(String tableName, int column, Object value) {
        boolean isTableNameExists = false;
        Set keySet = this.params.keySet();
        int size = keySet.size();
        String[] keys = new String[size]; //all the key names
        keySet.toArray(keys);
        Map<String, Object> temp = new HashMap<>();

        try {
            for (int i = 0; i < size; i++) {
                if (keys[i].split("\\:")[0].equals(tableName)) {

                    int rowCount = this.getTableRowCount(tableName);
                    int colCount = this.getTableColumnCount(tableName);

                    for (int j = 0; j < rowCount; j++) {
                        if (!this.getValue(tableName, j, column).toString().
                                equals(value.toString())) {
                            for (int u = 0; u < colCount; u++) {
                                String key = tableName + ":" + j + ":" + u;
                                String key1 = tableName + ":" + j + ":" + column;
                                String kVal = this.params.get(key1).toString();
                                temp.put(key, kVal);
                                temp.put(key1, kVal);
                            }
                        }
                    }

                    this.deleteTable(tableName);

                    Set tempKeySet = temp.keySet();
                    int tempSize = tempKeySet.size();
                    String[] tempKeys = new String[tempSize]; //all the key names
                    tempKeySet.toArray(tempKeys);

                    for (int u = 0; u < tempSize; u++) {
                        this.params.put(tempKeys[u], temp.get(tempKeys[u]));
                    }
                    isTableNameExists = true;
                    break;
                }
            }
            if (!isTableNameExists) {
                //System.out.println("Sorry, there is no table named :tableName");
            }
        } catch (Exception ex) {
        }
    }

    public void removeRow(String tableName, int rowNumber) throws QException {

        Set keySet = this.params.keySet();
        int size = keySet.size();
        String[] keys = new String[size]; //all the key names
        keySet.toArray(keys);
        Map<String, Object> temp = new HashMap<>();

        int rowCount = this.getTableRowCount(tableName);
        String cols[] = this.getTableColumnNames(tableName);

        for (int j = rowNumber; j < rowCount; j++) {
            for (String col : cols) {
                if (j + 1 < rowCount) {
                    String val = this.getValue(tableName, j + 1, col).toString();
                    this.setValue(tableName, j, col, val);
                } else if (j + 1 == rowCount) {
                    this.params.remove(tableName + ":" + j + ":" + col);
                }
            }

        }
    }

    public void toXML(String projectName, String file) {
        String fullpath = "C:\\Users\\Nuraddin\\Documents\\TaSBPM\\" + projectName + "\\" + file;

        Set keySet = this.params.keySet();
        int size = keySet.size();
        String[] keys = new String[size]; //all the key names
        keySet.toArray(keys);
        Map<String, String> tableNames = new HashMap<>();

        try {
            Element root = new Element("Carrier");
            Document doc = new Document(root);

            Element carrierName = new Element("name").setText("bCarrier");
            doc.getRootElement().addContent(carrierName);

            Element mapKey = new Element("mapKey");
            Element mapTable = new Element("mapTable");

            Element keyElemets = null;
            Element tableElemets = null;

            for (int i = 0; i < size; i++) {
                String tname = keys[i].split("\\:")[0];
                if (tname.equals(keys[i])) { // keys
                    keyElemets = new Element("key");
                    keyElemets.setAttribute("name", tname);
                    keyElemets.setText(this.getValue(tname).toString());
                    mapKey.addContent(keyElemets);
                } else //tables
                {
                    if (!tableNames.containsKey(tname)) {
                        tableNames.put(tname, "");

                        tableElemets = new Element("table");
                        tableElemets.setAttribute("name", tname);

                        int rowCount = this.getTableRowCount(tname);
                        int colCount = this.getTableColumnCount(tname);
                        String[] colNames = this.getTableColumnNames(tname);
                        Element rowElements = null;

                        for (int j = 0; j < rowCount; j++) {
                            for (int u = 0; u < colCount; u++) {
                                String value = null;
                                rowElements = new Element("row");
                                rowElements.setAttribute("idx", String.valueOf(j));
                                rowElements.setAttribute("colName", colNames[u]);
                                try {
                                    value = this.getValue(tname, j, colNames[u]).toString();
                                } catch (Exception ex) {
                                }
                                rowElements.setText(value);
                                tableElemets.addContent(rowElements);
                            }
                        }
                        mapTable.addContent(tableElemets);
                    }
                }
            }
            doc.getRootElement().addContent(mapKey);
            doc.getRootElement().addContent(mapTable);

            // new XMLOutputter().output(doc, System.out);
            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(fullpath));
        } catch (QException | IOException e) {
        }
    }

    public String toXML() {
        Set keySet = this.params.keySet();
        int size = keySet.size();
        String[] keys = new String[size]; //all the key names
        keySet.toArray(keys);
        Map<String, String> tableNames = new HashMap<>();

        String res = "";

        try {
            Element root = new Element("Carrier");
            Document doc = new Document(root);

            Element carrierName = new Element("name").setText("bCarrier");
            doc.getRootElement().addContent(carrierName);

            Element mapKey = new Element("mapKey");
            Element mapTable = new Element("mapTable");

            Element keyElemets = null;
            Element tableElemets = null;

            for (int i = 0; i < size; i++) {
                String tname = keys[i].split("\\:")[0];
                if (tname.equals(keys[i])) { // keys
                    keyElemets = new Element("key");
                    keyElemets.setAttribute("name", tname);
                    keyElemets.setText(this.getValue(tname).toString());
                    mapKey.addContent(keyElemets);
                } else //tables
                {
                    if (!tableNames.containsKey(tname)) {
                        tableNames.put(tname, "");

                        tableElemets = new Element("table");
                        tableElemets.setAttribute("name", tname);

                        int rowCount = this.getTableRowCount(tname);
                        int colCount = this.getTableColumnCount(tname);
                        String[] colNames = this.getTableColumnNames(tname);
                        Element rowElements = null;

                        for (int j = 0; j < rowCount; j++) {
                            for (int u = 0; u < colCount; u++) {
                                String value = null;
                                rowElements = new Element("row");
                                rowElements.setAttribute("idx", String.valueOf(j));
                                rowElements.setAttribute("colName", colNames[u]);
                                try {
                                    value = this.getValue(tname, j, colNames[u]).toString();
                                } catch (Exception ex) {
                                }
                                rowElements.setText(value);
                                tableElemets.addContent(rowElements);
                            }
                        }
                        mapTable.addContent(tableElemets);
                    }
                }
            }
            doc.getRootElement().addContent(mapKey);
            doc.getRootElement().addContent(mapTable);

            //new XMLOutputter().output(doc, System.out);
            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            res = xmlOutput.outputString(doc);
//            res = doc.getRootElement().getChildText("Carrier");
        } catch (Exception e) {
        }

        return res;
    }

    public void fromXML(String projectName, String file) {
        String fullpath = "C:\\Users\\Nuraddin\\Documents\\TaSBPM\\" + projectName + "\\" + file;

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(fullpath);
        if (!xmlFile.exists()) {
            return;
        }

        try {
            Document doc = builder.build(xmlFile);
            Element root = doc.getRootElement();

            String carrierName = root.getChildText("name");
            //////////////////////////////////////////////
            Element mapKey = root.getChild("mapKey");
            List keyElemets = mapKey.getChildren();
            int keyCount1 = keyElemets.size();

            for (int i = 0; i < keyCount1; i++) {
                Element key = (Element) keyElemets.get(i);
                this.setValue(key.getAttributeValue("name"), key.getText());
            }
            //////////////////////////////////////////////
            Element mapTable = root.getChild("mapTable");
            List tableElements = mapTable.getChildren();
            int tableCount1 = tableElements.size();

            for (int i = 0; i < tableCount1; i++) {
                Element table = (Element) tableElements.get(i);
                String tableName = table.getAttributeValue("name");
                List rows = table.getChildren();
                int rowsCount = rows.size();

                for (int j = 0; j < rowsCount; j++) {
                    Element row = (Element) rows.get(j);
                    String idx = row.getAttributeValue("idx");
                    String colName = row.getAttributeValue("colName");
                    String value = row.getText();

                    this.setValue(tableName, Integer.parseInt(idx), colName, value);
                }
            }

        } catch (Exception e) {
        }
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    private void rangeCheck(int row) throws QException {
        this.rangeCheck(row, 0);
    }

    private void rangeCheck(int row, int column) throws QException {
        if (row < 0 || column < 0) {
            throw new QException(ROW_AND_COLUMN_INDEX_MUST_BE_POSITIV_NUMBER);
        }
    }

    private String mergeTableKey(String tableName, int row, String column) {

        String key = tableName + TABLE_KEY_SEPERATOR + row
                + TABLE_KEY_SEPERATOR + column;

        return key;

    }

    private String mergeTableKey(String tableName, int row, int column) {

        return mergeTableKey(tableName, row, Integer.toString(column));

    }

    private String returnColumnNameOfMergedTableKey(String tableName) {

        String st[] = tableName.split(TABLE_KEY_SEPERATOR);

        String columnName = st[st.length - 1];// Bizim standarda gore 3 cu
        // element columnName'leri
        // saxlayirdi

        return columnName;

    }

    private String returnRowNameOfMergedTableKey(String tableName) {
        String st[] = tableName.split(TABLE_KEY_SEPERATOR);
        String columnName = st[1];// Bizim standarda gore 2 cu element
        // columnName'leri saxlayirdi

        return columnName;

    }

    public void setValue(String key, Object value) {

        params.put(key, value);
        if (!key.contains(TABLE_KEY_SEPERATOR) && !hasKey(key)) {
            this.keyNames[this.keyCount] = key;
            this.keyCount++;
        }
    }

    public boolean hasKey(String key) {
        for (int i = 0; i < keyCount; i++) {
            if (keyNames[i].equals(key)) {
                return true;
            }
        }
        return false;
    }

    public void setValue(String tableName, int row, String column, Object value) throws QException {

        rangeCheck(row);

        if (!isTableNameExist(tableName)) {
            this.tableNamesArray[this.tableCount] = tableName;
            this.tableCount++;
        }
        this.setValue(mergeTableKey(tableName, row, column), value);

    }

    public void setValue(String tableName, int row, int column, Object value)
            throws QException {

        rangeCheck(row, column);

        if (!isTableNameExist(tableName)) {
            this.tableNamesArray[this.tableCount] = tableName;
            this.tableCount++;
        }
        this.setValue(tableName, row, Integer.toString(column), value);

    }

    public boolean isTableNameExist(String tableName) {
        for (int i = 0; i < this.tableCount; i++) {
            if (tableName.equals(this.tableNamesArray[i])) {
                return true;
            }
        }
        return false;
    }

    public Object getValue(String key) throws QException {

        if (!params.containsKey(key)) {

//            throw new QException(THERE_IS_NOT_ANY_KEY_CALLED + " \'" + key + "\'");
            return "";

        } else {

            return params.get(key);

        }

    }

    public Object getValue(String tableName, int row, String column)
            throws QException {
        Object res = "";
        rangeCheck(row);
        res = this.getValue(mergeTableKey(tableName, row, column));
        return res;

    }

    public Carrier getValue(String tableName, int row)
            throws QException {

        rangeCheck(row);
        Carrier outCarrier = new Carrier();

        String[] cols = this.getTableColumnNames(tableName);
        for (String col : cols) {
            outCarrier.setValue(col, this.getValue(tableName, row, col));
        }

        return outCarrier;

    }

    public Object getValue(String tableName, int row, int column)
            throws QException {

        rangeCheck(row, column);

        return this.getValue(tableName, row, Integer.toString(column));

    }

    public String[] getTableColumnNames(String tableName) throws QException {

        Set set1 = params.keySet();
        String[] nameArray = (String[]) set1.toArray(new String[0]);
        Map<String, Object> colNames = new HashMap<String, Object>();

        for (int i = 0; i < nameArray.length; i++) {
            String tn = nameArray[i].split(TABLE_KEY_SEPERATOR)[0];
            if (tn.equals(tableName)) {
                colNames.put(returnColumnNameOfMergedTableKey(nameArray[i]), "");
            }
        }

        String[] nameArray1 = (String[]) colNames.keySet().toArray(
                new String[0]);
        return nameArray1;

    }

    public String[] getTableColumnNamesByIntColumns(String tableName)
            throws QException {

        Set set1 = params.keySet();
        String[] nameArray = (String[]) set1.toArray(new String[0]);
        Map<String, Object> colNames = new HashMap<String, Object>();
        // = new String[nameArray.length];
        for (int i = 0; i < nameArray.length; i++) {
            String tn = nameArray[i].split(TABLE_KEY_SEPERATOR)[0];
            if (tn.equals(tableName)) {
                String cn = returnColumnNameOfMergedTableKey(nameArray[i]);
                if (isInteger(cn)) {
                    colNames.put(cn, "");
                }
            }
        }

        String[] nameArray1 = (String[]) colNames.keySet().toArray(
                new String[0]);
        return nameArray1;
    }

    public String[] getTableColumnNamesByStringColumns(String tableName)
            throws QException {
        Set set1 = params.keySet();
        String[] nameArray = (String[]) set1.toArray(new String[0]);
        Map<String, Object> colNames = new HashMap<String, Object>();
        for (int i = 0; i < nameArray.length; i++) {
            String tn = nameArray[i].split(TABLE_KEY_SEPERATOR)[0];
            if (tn.equals(tableName)) {
                String cn = returnColumnNameOfMergedTableKey(nameArray[i]);
                if (!isInteger(cn)) {
                    colNames.put(cn, "");
                }
            }
        }

        String[] nameArray1 = (String[]) colNames.keySet().toArray(
                new String[0]);
        return nameArray1;

    }

    public static boolean isInteger(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public int getTableRowCount(String tableName) throws QException {

        Set set1 = params.keySet();

        String[] nameArray = (String[]) set1.toArray(new String[0]);

        Map<String, Object> colNames = new HashMap<String, Object>();

        // = new String[nameArray.length];
        for (int i = 0; i < nameArray.length; i++) {
            String tn = nameArray[i].split(TABLE_KEY_SEPERATOR)[0];
            if (tn.equals(tableName)) {
                colNames.put(returnRowNameOfMergedTableKey(nameArray[i]), "");
            }

        }

        String[] nameArray1 = (String[]) colNames.keySet().toArray(
                new String[0]);

        return nameArray1.length;

    }

    public int getTableColumnCount(String tableName) throws QException {
        Set set1 = params.keySet();
        String[] nameArray = (String[]) set1.toArray(new String[0]);
        Map<String, Object> colNames = new HashMap<String, Object>();
        // = new String[nameArray.length];
        for (int i = 0; i < nameArray.length; i++) {
            String tn = nameArray[i].split(TABLE_KEY_SEPERATOR)[0];
            if (tn.equals(tableName)) {
                colNames.put(returnColumnNameOfMergedTableKey(nameArray[i]), "");
            }
        }
        String[] nameArray1 = (String[]) colNames.keySet().toArray(
                new String[0]);
        return nameArray1.length;

    }

    public boolean isKeyExist(String key) {
        if (params.containsKey(key.trim())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isKeyAndValueExist(String key, String value) {
        if (params.containsKey(key.trim()) && params.get(key).toString().trim().equals(value)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isKeyAndValueExist(String tableName, int row, String coloumn, String value) {
        String key = mergeTableKey(tableName, row, coloumn);
        if (params.containsKey(key.trim()) && params.get(key).toString().trim().equals(value)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isKeyAndValueExist(String tableName, String coloumn, String value) throws QException {
        int rowCount = getTableRowCount(tableName);
        boolean f = false;

        for (int i = 0; i < rowCount; i++) {
            String key = mergeTableKey(tableName, i, coloumn);
            if (params.containsKey(key.trim()) && params.get(key).toString().trim().equals(value)) {
                f = true;
            }
        }
        return f;
    }

    public boolean isKeyExist(String tableName, int row, String coloumn) {
        String key = mergeTableKey(tableName, row, coloumn);
        if (params.containsKey(key.trim())) {
            return true;
        } else {
            return false;
        }
    }

    public void fromJson(String jsonString) {
        if (jsonString.trim().length() == 0) {
            return;
        }
        JsonObject object;
        JsonParser parser = new JsonParser();
        object = parser.parse(jsonString).getAsJsonObject();

        if (object.has("kv")) {
            JsonObject kvObject = object.get("kv").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : kvObject.entrySet()) {
                String key = entry.getKey();
                JsonElement val = entry.getValue();
                String valObj = "";
                if (!val.isJsonNull()) {
                    valObj = val.getAsString();
                }

                this.setValue(key, valObj);
            }
        }

        if (object.has("err")) {
            JsonArray errObjectArray = object.get("err").getAsJsonArray();
            for (JsonElement columnElement : errObjectArray) {
                JsonObject columnObject = columnElement.getAsJsonObject();
                String code = columnObject.get("code").getAsString();
                String val = columnObject.get("val").getAsString();
                try {
                    this.addController(code, val);

                } catch (QException ex) {
                    Logger.getLogger(Carrier.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (object.has("tbl")) {
            JsonArray tblObjectArray = object.get("tbl").getAsJsonArray();

            for (JsonElement columnElement : tblObjectArray) {
                JsonObject columnObject = columnElement.getAsJsonObject();
                String table = columnObject.get("tn").getAsString();
                String startLimit = "";
                try {
                    startLimit = columnObject.get("startLimit").getAsString();
                } catch (Exception e) {
                }
                String endLimit = "";
                try {
                    endLimit = columnObject.get("endLimit").getAsString();
                } catch (Exception e) {
                }
                String rowCount = "";
                try {
                    rowCount = columnObject.get("rowCount").getAsString();
                } catch (Exception e) {
                }
                this.addTableStartLimit(table, startLimit);
                this.addTableEndLimit(table, endLimit);
                this.addTableRowCount(table, rowCount);

                JsonArray tbl_r_Array = columnObject.get("r").getAsJsonArray();
                int id = 0;
                for (JsonElement r_element : tbl_r_Array) {
                    JsonObject r_Object = r_element.getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : r_Object.entrySet()) {
                        String key = entry.getKey();
                        JsonElement val = entry.getValue();
                        String valObj = val.getAsString();
                        try {
                            this.setValue(table, id, key, valObj);

                        } catch (QException ex) {
                            Logger.getLogger(Carrier.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    id++;
                }
//            
            }
        }
    }

    public void fromJsonTable(String jsonString) throws QException {
        JsonParser parser = new JsonParser();
        JsonObject tableObject = parser.parse(jsonString).getAsJsonObject();
        String table = "";

        if (tableObject.entrySet().isEmpty()) {
            return;
        }

        table = tableObject.get("tn").getAsString();
        HashMap<String, String> columns = new HashMap<>();

        JsonArray tableHeaderJsonArray = tableObject.get("err").getAsJsonArray();
        for (JsonElement columnElement : tableHeaderJsonArray) {
            JsonObject columnObject = columnElement.getAsJsonObject();
            String code = columnObject.get("code").getAsString();
            String val = columnObject.get("val").getAsString();
            this.addController(code, val);
        }

//        JsonObject tableContentObject = tableObject.get("tbl").getAsJsonObject();
//        Set<Map.Entry<String, JsonElement>> rowEntries = tableContentObject.entrySet();
//        for (Map.Entry<String, JsonElement> rowEntry : rowEntries) {
//            String columnId = rowEntry.getKey();
//            String columnName = columns.get(columnId);
//            Object val = "";
//            JsonElement cellEl = rowEntry.getValue();
//            if (cellEl != null) {
//                val = cellEl.getAsString();
//                if (val == null) {
//                    val = "";
//                }
//            }
//            this.setValue(table, r, columnName, val);
//        }
//
//        int r = 0;
//        for (JsonElement rowElement : tableContentArray) {
//            JsonObject rowObject = rowElement.getAsJsonObject();
////            Set<Map.Entry<String, JsonElement>> rowEntries = rowObject.entrySet();
//            for (Map.Entry<String, JsonElement> rowEntry : rowEntries) {
//                String columnId = rowEntry.getKey();
//                String columnName = columns.get(columnId);
//                Object val = "";
//                JsonElement cellEl = rowEntry.getValue();
//                if (cellEl != null) {
//                    val = cellEl.getAsString();
//                    if (val == null) {
//                        val = "";
//                    }
//                }
//                this.setValue(table, r, columnName, val);
//            }
//            r++;
//        }
    }

    public JsonArray toJsonAllTables() throws QException {
        JsonArray rArray = new JsonArray();
        for (String tableName : this.getTableNamesArray()) {
//            System.out.println("\tgeldi");
//            System.out.println("\t" + tableName);
            JsonObject obj = this.toJsonTable(tableName);
            if (obj != null) {
                rArray.add(obj);
//                System.out.println("\tadded:: " + this.toJsonTable(tableName).toString());
            }

        }
        return rArray;
    }

    public JsonObject toJsonTable(String tableName) throws QException {
        if (!isTableNameExist(tableName)) {
            return null;
        }

        String[] columnNames = this.getTableColumnNames(tableName);
        int rowCount = this.getTableRowCount(tableName);

        Gson gson = new Gson();
        JsonObject tableObject = new JsonObject();
        tableObject.add("tn",
                new JsonParser().parse(gson.toJson(tableName)));

        JsonArray tableHeaderArray = new JsonArray();
        for (String colName : columnNames) {
            JsonObject columnObject = new JsonObject();
            columnObject.addProperty("n", colName);
            columnObject.addProperty("i", colName);
            tableHeaderArray.add(columnObject);
        }
        tableObject.add("c", tableHeaderArray);

        JsonArray tableContentArray = new JsonArray();
        for (int i = 0; i < rowCount; i++) {
            JsonObject rowObject = new JsonObject();
            for (String colName : columnNames) {
                Object val = this.getValue(tableName, i, colName);
                if (val == null) {
                    val = "";
                }
                rowObject.addProperty(colName, val.toString());
            }
            tableContentArray.add(rowObject);
        }
        tableObject.add("r", tableContentArray);

        return tableObject;
    }

    private String mergeArray(String[] arg, String seperator) {
        String res = "";
        for (String arg1 : arg) {
            res += arg1 + seperator;
        }
        return res;
    }

    private Carrier getColNamesDefination(String[] cols) throws QException {
        Carrier c = EntityManager.getEntityFieldType(cols);
        String carrTable = "crEntityLabel";

        if (this.getMatrixId().length() > 0) {

            Carrier tc = EntityManager.getEntityFieldTypeByMatrixId(
                    this.getMatrixId(), cols);
            String colName = tc.getValueLine(carrTable, "columnName", ",");
            tc = tc.getKeyValuesPairFromTable(carrTable, "columnName", "shortName");

            int rc = c.getTableRowCount(carrTable);
            for (int i = 0; i < rc; i++) {
                String fname = c.getValue(carrTable, i, "fieldName").toString();
                if (colName.contains(fname)) {
                    c.setValue(carrTable, i, "description", tc.getValue(fname));
                }
            }
        }
        return c;
    }

    public String toJson(String tablename) throws QException {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        int rowCount = this.getTableRowCount(tablename);
        String[] colNames = this.getTableColumnNames(tablename);
        Carrier carrier = getColNamesDefination(colNames);
        String carrTable = "crEntityLabel";
        try {

            //set column name
            JSONArray array1 = new JSONArray();
            ArrayList dateList = new ArrayList();
            ArrayList timeList = new ArrayList();
            for (int j = 0; j < colNames.length; j++) {
                JSONObject jsonRow1 = new JSONObject();

                String type = CoreLabel.ENTITY_LABEL_TYPE_STRING;
                String name = colNames[j];

                //is type and description exist;
                int idx = -1;
                int c = carrier.getTableRowCount(carrTable);
                for (int l = 0; l < c; l++) {
                    if (carrier.getValue(carrTable, l, CoreLabel.LABEL_FIELD_NAME).toString().trim().equals(name)) {
                        idx = l;
                        break;
                    }
                }
                if (idx != -1) {
                    type = carrier.getValue(carrTable, idx, CoreLabel.LABEL_TYPE).toString().trim().equals("")
                            ? CoreLabel.ENTITY_LABEL_TYPE_STRING : carrier.getValue(carrTable, idx, CoreLabel.LABEL_TYPE).toString();
                    name = carrier.getValue(carrTable, idx, CoreLabel.LABEL_DESCRIPTION).toString().trim().equals("")
                            ? colNames[j] : carrier.getValue(carrTable, idx, CoreLabel.LABEL_DESCRIPTION).toString();
                }

                jsonRow1.put(CoreLabel.JSON_TABLE_FIELD_ID, colNames[j]);
                jsonRow1.put(CoreLabel.JSON_TABLE_FIELD_NAME, name);
                jsonRow1.put(CoreLabel.JSON_TABLE_FIELD_TYPE, type);
                jsonRow1.put(colNames[j], name);

                if (type.equals(CoreLabel.ENTITY_LABEL_TYPE_DATE)) {
                    dateList.add(j);
                } else if (type.equals(CoreLabel.ENTITY_LABEL_TYPE_TIME)) {
                    timeList.add(j);
                }
                array1.put(jsonRow1);
            }

            object.put("c", array1);

            ////end set cols
            for (int i = 0; i < rowCount; i++) {
                JSONObject jsonRow = new JSONObject();
                String value;

                for (int j = 0; j < colNames.length; j++) {
                    String vt1 = this.getValue(tablename, i, colNames[j]).toString();
                    if (dateList.contains(j)) {
                        if (vt1.trim().length() == 8) {
                            value = QDate.convertToDateString(vt1);
                        } else {
                            value = vt1;
                        }
                    } else if (timeList.contains(j)) {
                        if (vt1.trim().length() == 6) {
                            value = QDate.convertToTimeString(vt1);
                        } else {
                            value = vt1;
                        }
                    } else {
                        value = this.getValue(tablename, i, colNames[j]).toString();
                    }
                    jsonRow.put(colNames[j], value);
                }
                array.put(jsonRow);
            }
            object.put("r", array);
            object.put("tn", tablename);
            object.put("startLimit", this.getTableStartLimit(tablename));
            object.put("endLimit", this.getTableEndLimit(tablename));
            object.put("rowCount", this.getTableRowCount4Json(tablename));
            object.put("seq", this.getTableSequence(tablename));

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return object.toString();
    }

    public String toErrorJson() throws QException {
        try {

            JSONArray array = new JSONArray();

            String tablename = USER_ERROR_TABLE;
            int rowCount = this.getTableRowCount(tablename);

            //end add table name
            for (int i = 0; i < rowCount; i++) {
                JSONObject jsonRow = new JSONObject();
                String code = this.getValue(tablename, i, USER_ERROR_KEY).toString();
                String val = this.getValue(tablename, i, USER_ERROR_MESSAGE).toString();
                jsonRow.put(USER_ERROR_CODE, code);
                jsonRow.put(USER_ERROR_VALUE, val);
                array.put(jsonRow);
            }
//            object.put(CoreLabel.JSON_ERROR_TABLE_RESULT_TABLE_NAME, array);
            return array.toString();
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String toJsonKeyValue() throws QException {
        JSONObject jsonRow = new JSONObject();
        try {

            String[] keyNames = this.keyNames;
            for (int j = 0; j < keyCount; j++) {
                jsonRow.put(keyNames[j], this.getValue(keyNames[j]).toString());
            }
//           
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return jsonRow.toString();

    }

    private void initializeJSONStructure() {
        JsonArray arr = new JsonArray();

        this.jsonRootObject.add("j", arr);

    }

    public void deleteTable(String tableName) {
        Object[] s = this.params.keySet().toArray();
        for (Object item : s) {
            String tn = item.toString().split(TABLE_KEY_SEPERATOR)[0];
            if (tn.equals(tableName)) {
                this.params.remove(item);
            }
        }
        deleteTableFromList(tableName);
    }

    public String getJson() throws QException {
        try {
            this.jsonRootObject = new JsonObject();

            JSONArray array = new JSONArray();

            JsonParser parser = new JsonParser();
//         
            JsonObject ojbKV = parser.parse(this.toJsonKeyValue()).getAsJsonObject();
            this.jsonRootObject.add(CoreLabel.KEY_VALUE_TABLE, ojbKV);

            JsonArray arrayTbl = new JsonArray();
            for (String tn : this.tableNamesArray) {
                if (tn != null && !tn.equals("USER_ERROR_TABLE")) {
                    JsonObject ojbres = parser.parse(this.toJson(tn)).getAsJsonObject();
                    arrayTbl.add(ojbres);
                }

            }
            this.jsonRootObject.add("tbl", arrayTbl);

            JsonArray ojbres1 = parser.parse(this.toErrorJson()).getAsJsonArray();
            this.jsonRootObject.add(CoreLabel.JSON_ERROR_TABLE_RESULT_TABLE_NAME, ojbres1);

            return this.jsonRootObject.toString();
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String getAllTableJsonString() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        String[] tablenames = this.getTableNamesArray();

        int rowCount = tablenames.length;

        try {
            for (int i = 0; i < rowCount; i++) {
                if (!tablenames[i].trim().equals(USER_ERROR_TABLE)) {
                    JSONObject objC = new JSONObject(this.toJson(tablenames[i]));
                    array.put(objC);
                }
            }
            object.put("f", array);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return object.toString();
    }

    public void addController(String paramName, String messageText) throws QException {
        if (!messageText.equals("200")) {
            addError(paramName, messageText);
        }
    }

    public String toJSONACNIS(String tablename) throws QException {
        String res = "";

        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        int rowCount = this.getTableRowCount(tablename);
        int colCount = this.getTableColumnCount(tablename);
        String[] colNames = this.getTableColumnNames(tablename);

        try {
            JSONObject jsonHeaderRow = new JSONObject();
            for (int i = 0; i < colCount; i++) {
                jsonHeaderRow.put(colNames[i], colNames[i]);
            }
            array.put(jsonHeaderRow);

            for (int i = 0; i < rowCount; i++) {
                JSONObject jsonRow = new JSONObject();
                for (int j = 0; j < colCount; j++) {
                    jsonRow.put(colNames[j], this.getValue(tablename, i, colNames[j]));
                }
                array.put(jsonRow);
            }
            object.put(tablename, array);

        } catch (Exception ex) {
            Logger.getLogger(Carrier.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return object.toString();

    }

    public boolean hasError() throws QException {
        boolean f = this.getTableRowCount(this.USER_ERROR_TABLE) != 0;
        return f;
    }

    public EntityCrUser getSession() {
        return session;
    }

    public void setSession(EntityCrUser session) {
        this.session = session;
    }

    public String[] getValue(String tableName, String columnName) throws QException {
        try {
            int row = this.getTableRowCount(tableName);
            String[] res = new String[row];
            for (int i = 0; i < row; i++) {
                res[i] = this.getValue(tableName, i, columnName).toString();
            }
            return res;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String getValueLine(String tableName) throws QException {
        return getValueLine(tableName, "id", CoreLabel.IN);
    }

    public String getValueLine(String tableName, String columnName) throws QException {
        return getValueLine(tableName, columnName, CoreLabel.IN);
    }

    public String getValueLine(String tableName, String columnName, String seperator) throws QException {
        try {
            int row = this.getTableRowCount(tableName);
            String res = "";
            for (int i = 0; i < row; i++) {
                try {
                    String val = this.getValue(tableName, i, columnName).toString();
                    if (!val.trim().isEmpty()) {
                        res = res + val + seperator;
                    }
                } catch (Exception e) {
                }
            }
            res = res.length() > 0 ? res.substring(0, res.length() - seperator.length()) : "";
            return res;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier callService(String servicename) throws QException {
        this.setServiceName(servicename);
        String moduleName = CallDispatcher.getModuleName(servicename);
        return CallDispatcher.executeDispatcher(moduleName, this);
    }

    public Carrier getKVFromTable(String tablename, String keyColumnName, String valueColumnName) throws QException {
        return getKeyValuesPairFromTable(tablename, keyColumnName, valueColumnName);
    }

    public Carrier getKeyValuesPairFromTable(String tablename, String keyColumnName, String valueColumnName) throws QException {
        return getKeyValuesPairFromTable(tablename, new String[]{keyColumnName}, "", new String[]{valueColumnName}, "");
    }

    public Carrier getKeyValuesPairFromTable(String tablename, String keyColumnName[], String valueColumnName) throws QException {
        return getKeyValuesPairFromTable(tablename, keyColumnName, "", new String[]{valueColumnName}, "");
    }

    public Carrier getKeyValuesPairFromTable(String tablename, String keyColumnName[],
            String valueColumnName[]) throws QException {
        return getKeyValuesPairFromTable(tablename, keyColumnName, "",
                valueColumnName, "");
    }

    public Carrier getKeyValuesPairFromTable(String tablename, String keyColumnName, String[] valueColumnName) throws QException {
        return getKeyValuesPairFromTable(tablename, new String[]{keyColumnName}, "", valueColumnName, " ");
    }

    public Carrier getKeyValuesPairFromTable(String tablename, String[] keyColumnName,
            String keySeperator, String[] valueColumnName, String valueSeperator)
            throws QException {
        try {
            Carrier c = new Carrier();
            int cnt = this.getTableRowCount(tablename);
            for (int i = 0; i < cnt; i++) {
                //get key field
                String key = "";
                for (String keyColumnName1 : keyColumnName) {
                    key += this.getValue(tablename, i, keyColumnName1).toString() + keySeperator;
                }
                key = key.substring(0, key.length() - keySeperator.length());

                String value = "";
                for (String valueColumnName1 : valueColumnName) {
                    value += this.getValue(tablename, i, valueColumnName1).toString() + valueSeperator;
                }
                value = value.substring(0, value.length() - valueSeperator.length());
                c.setValue(key, value);
            }
            return c;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public void mergeCarrier(String sourceTablename,
            String sourceRelatedColName, String newColumnName, Carrier carrier)
            throws QException {
        mergeCarrier(sourceTablename,
                new String[]{sourceRelatedColName}, "", newColumnName, carrier);
    }

    public void mergeCarrier(String sourceTablename,
            String[] sourceRelatedColName, String newColumnName, Carrier carrier)
            throws QException {
        mergeCarrier(sourceTablename, sourceRelatedColName, "", newColumnName, carrier);
    }

    public void mergeCarrier(String sourceTablename,
            String sourceRelatedColName[], String sourceSeperator,
            String newColumnName, Carrier carrier)
            throws QException {

         

        String newCol = newColumnName;
        Carrier tc = carrier;
        String coreTablename = sourceTablename;
        int rc = this.getTableRowCount(coreTablename);
        for (int i = 0; i < rc; i++) {
            String val ="";  
            for (int k = 0; k < sourceRelatedColName.length; k++) {
                val += this.getValue(coreTablename, i,
                        sourceRelatedColName[k]).toString();
                val += k + 1 < sourceRelatedColName.length ? sourceSeperator : "";
            }
            if (tc.isKeyExist(val)) {
                this.setValue(coreTablename, i, newCol, tc.getValue(val));
            } else {
                this.removeRow(coreTablename, i);
                i--;
                rc--;
            }
        }
    }

}
