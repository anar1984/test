/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility.sqlgenerator;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.Carrier;
import utility.CoreEntity;
import utility.CoreSystem;
import utility.DBConfigurationProperties;
import utility.QDate;
import module.cr.entity.EntityCrEntityLabel;
import module.cr.entity.EntityCrListItem;
import module.cr.entity.EntityCrSmsMessageText;
import module.cr.entity.EntityCrUserControllerList;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import label.CoreLabel;
import rp.module.entity.EntityCrSqlPool;
import utility.ListSequenceConfigurationProperties;
import utility.QException;
import utility.SessionManager;

/**
 *
 * @author 02483577
 */
public class EntityManager {

    public static String ENTITY = "ENTITY";
    public static String ENTITY_PREFIX = "entity";
    public static String SQL_QUERY = "sqlQuery";
    public static String SQL_POOL_TABLE = "sqlPool";
    private static final String PARAM_KEY = ":param";
    private static final String REFERENCE_KEY_NAME = "referenceKeyName";
    private static final String HAS_INSERT_DATE = "hasInsertDate";
    private static final String HAS_MODIFICATION_DATE = "hasModificationDate";
    private static final String DELETE_STATUS = "deleteStatus";
    public static String GET = "get";
    public static String SEPERATOR_DOT = ".";
    public static String SEPERATOR_COMMA = ",";
    public static String UNDERSCORE = "_";
    public static String UNION = "UNION";
    public static String SPACE = " ";
    public static String SUM_BY = "sumBy";
    public static String GROUP_BY = "groupBy";
    public static String INTERVAL_START_DATE = "intervalStartDate";
    public static String INTERVAL_END_DATE = "intervalEndDate";
    public static String INTERVAL_FIELD = "intervalField";
    public static String INTERVAL_SEPERATOR = "intervalSeperator";
    public static String EMPTY_VALUE = "A%%##%%##A";
    public static String ENTITY_LABEL_TYPE = "INTEGER";

    public static String convertEntityNameToDBTableName(CoreEntity core) {
        return SQLGenerator.getTableNameBasedOnEntity(core);
    }

    public static void insert(CoreEntity entity) throws QException {
        insert(entity, label.CoreLabel.DB_PRIMARY);
    }

    public static void insert(CoreEntity entity, String databaseNumber) throws QException {
        //entity.setId(IdGenerator.getId());
        setReferenceValue(entity, databaseNumber);
        addDateToInsert(entity, databaseNumber);
        String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
        String query = SQLGenerator.insertGenerator(entity, databaseNumber, methodNames);
        String[] values = SQLGenerator.getValuesOfAllGetMethodsOfEntity(entity, methodNames);
        if (entity.hasConnection()) {
            SQLConnection.execInsertSql(query, databaseNumber, values, entity.selectConnection());
        } else {
            SQLConnection.execInsertSql(query, databaseNumber, values);
        }
    }

    private static void addDateToInsert(CoreEntity entity, String databaseNumber) throws QException {
        try {
            DBConfigurationProperties prop = new DBConfigurationProperties();
            String f = prop.getProperty(HAS_INSERT_DATE + capitalizeOnlyFirstLetter(databaseNumber));

            if (f.equals("true")) {
                entity.setInsertDate(QDate.getCurrentDate());
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static void addDateToUpdate(CoreEntity entity, String databaseNumber) throws QException {
        entity.setModificationDate(QDate.getCurrentDate());
    }

    private static void setReferenceValue(CoreEntity entity, String databaseNumber) throws QException {
        try {
//            String refName = getReferenceKeyName(databaseNumber);
            String refName = "id";
            String val = IdGenerator.getId(entity, databaseNumber);
            setReferenceKeyValue(entity, refName, val);
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier select(CoreEntity entity) throws QException {
        return select(entity, CoreLabel.DB_PRIMARY);
    }

    public static Carrier select(CoreEntity entity, Statement statement) throws QException {
        return select(entity, CoreLabel.DB_PRIMARY);
    }

    public static Carrier select(CoreEntity entity, String databaseNumber) throws QException {
        return select(entity, entity.toTableName(), databaseNumber);
    }

    public static Carrier select(CoreEntity entity, String tablename, String databaseNumber) throws QException {
        Carrier carrier = new Carrier();
        try {
            if (entity.hasIntervalField()) {
                carrier = groupByInterval(entity);
            } else if (entity.hasSumBy()) {
                String[] groupByFields = entity.selectGroupBy();
                carrier = groupBy(entity, groupByFields, entity.selectSumBy());
            } else {
                carrier = coreSelect(entity, tablename, databaseNumber);
            }
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static Carrier coreSelect(CoreEntity entity, String tablename, String databaseNumber) throws QException {
        //And Statement olan saheleri addAndStatement() funksiyasina elavet etmek
        entity.splitAndStatementField();
        entity.splitAndOrStatementField();
        entity.splitDeepWhereStatementField();
        String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
        String[] values = SQLGenerator.getValuesOfAllGetMethodsOfEntity(entity, methodNames);
        ArrayList valueArr = new ArrayList();
        String query = SQLGenerator.selectGenerator(entity, databaseNumber, methodNames, values, valueArr);
//        System.out.println("query select->"+query);

        Carrier carrier = new Carrier();
        if (entity.hasConnection()) {
            carrier = SQLConnection.execSelectSql(query, tablename, databaseNumber, valueArr, entity.selectConnection());
        } else {
            carrier = SQLConnection.execSelectSql(query, tablename, databaseNumber, valueArr);
        }
        if (carrier.getTableRowCount(tablename) == 1) {
            mapCarrierToEntity(carrier, tablename, 0, entity);
        }
        return carrier;
    }

    private static String getAttributeNameFromMethodName(String methodname) {
        String fname = methodname.substring("GET".length(), methodname.length());
        fname = lowerFirstLetter(fname);
        return fname;
    }

    public static Carrier getFieldTitlesByMethodnames(Carrier carrier, CoreEntity entity, String[] methodnames) {
        return carrier;
    }

    public static Carrier selectBySqlId(String sqlId, String[] params) throws QException {
        return selectBySqlId(sqlId, params, CoreLabel.DB_PRIMARY);
    }

    private static ArrayList converArrayToArrayList(String[] arg) {
        ArrayList arr = new ArrayList();
        for (int i = 0; i < arg.length; i++) {
            arr.add(arg[i]);
        }
        return arr;
    }

    public static Carrier selectBySqlId(String sqlId, String[] params, String destinationDatabaseNumber) throws QException {
        try {
            DBConfigurationProperties prop = new DBConfigurationProperties();
            String sourceDB = prop.getProperty(CoreLabel.SQL_QUERY_POOL_DB_NUMBER);

            EntityCrSqlPool entity = new EntityCrSqlPool();
            entity.setSqlId(sqlId);

            Carrier carrier = select(entity, sourceDB);
            String query = entity.getSqlQuery();
            Carrier c = null;
            if (!query.trim().equals("")) {
                c = SQLConnection.execSelectSql(query, entity.convertEntityNameToTableName(entity),
                        destinationDatabaseNumber, converArrayToArrayList(params));
            }
            c.renameTableName(entity.toTableName(), CoreLabel.RESULT_SET);
            return c;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier selectBySql(String sqlQuery) throws QException {
        return selectBySql(sqlQuery, new String[]{});
    }

    public static Carrier selectBySql(String sqlQuery, String[] params) throws QException {
        try {
            String query = sqlQuery;
            Carrier c = null;
            if (!query.trim().equals("")) {
                c = SQLConnection.execSelectSql(query, CoreLabel.RESULT_SET,
                        "1", converArrayToArrayList(params));
            }
            return c;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier selectBySqlId(String sqlId) throws QException {
        return selectBySqlId(sqlId, new String[]{});
    }

    public static String replaceParamsInSqlQuery(String sqlQuery, String[] params) {
        for (int i = 1; i <= params.length; i++) {
            sqlQuery = sqlQuery.replace(PARAM_KEY + i, params[i - 1]);
        }
        return sqlQuery;
    }

    public static void update(CoreEntity entity) throws QException {
        update(entity, label.CoreLabel.DB_PRIMARY);
    }

    public static void update(CoreEntity entity, String databaseNumber) throws QException {
        ArrayList valueList = new ArrayList();
        if (entity.getId().trim().equals("")) {
            throw new QException("id is empty");
        }
        addDateToUpdate(entity, databaseNumber);
        String m1[] = SQLGenerator.getAllGetMethodNames(entity);
        String[] methodNames = removeReferenceKeyFromMethodsList(m1, "id");
        String[] values = SQLGenerator.getValuesOfAllGetMethodsOfEntity(entity, methodNames);
        valueList.addAll(Arrays.asList(values));
        String query = SQLGenerator.updateGenerator(entity, databaseNumber, methodNames, valueList);
        if (!query.isEmpty()) {
            if (entity.hasConnection()) {
                SQLConnection.execUpdateSql(query, databaseNumber, valueList, entity.selectConnection());
            } else {
                SQLConnection.execUpdateSql(query, databaseNumber, valueList);
            }
        }

    }

    private static String[] removeReferenceKeyFromMethodsList(String[] methods, String rKey) {
        String[] t = new String[methods.length - 1];
        int idx = 0;
        for (String method : methods) {
            if (!method.equals(GET + capitalizeOnlyFirstLetter(rKey))) {
                t[idx] = method;
                idx++;
            }
        }
        return t;
    }

    public static String getReferenceKeyName(String databaseNumber) {
        DBConfigurationProperties prop = null;
        try {
            prop = new DBConfigurationProperties();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EntityManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String propKeyName = REFERENCE_KEY_NAME + capitalizeFirstLetter(databaseNumber);
        String keyName = prop.getProperty(propKeyName);
        return keyName;
    }

    public static String getReferenceKeyValue(CoreEntity entity, String keyName) throws QException {
        try {
            String methodName = "get" + capitalizeFirstLetter(keyName);

            Method method = entity.getClass().getMethod(methodName);
            Object retObj = method.invoke(entity);
            String rs = (String) retObj;

            return rs;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static String setReferenceKeyValue(CoreEntity entity, String keyName, String keyValue) throws QException {
        try {
            String methodName = "set" + capitalizeFirstLetter(keyName);

            Method method = entity.getClass().getMethod(methodName, String.class);
            Object retObj;
            retObj = method.invoke(entity, keyValue);
            String rs = (String) retObj;

            return rs;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    static String capitalizeFirstLetter(String arg) {
        arg = arg.substring(0, 1).toUpperCase() + arg.substring(1, arg.length()).toLowerCase();
        return arg;
    }

    public static void delete(CoreEntity entity) throws QException {

        delete(entity, CoreLabel.DB_PRIMARY);
    }

    public static void delete(CoreEntity entity, String databaseNumber) throws QException {
        ArrayList valueList = new ArrayList();
        if (entity.getId().trim().equals("")) {
            throw new QException("id is empty");
        }

        addDateToUpdate(entity, databaseNumber);
        String methodNames[] = new String[]{"getStatus", "getModificationDate"};
        valueList.add("D");
        valueList.add(QDate.getCurrentDate());

        String query = SQLGenerator.updateGenerator(entity, databaseNumber, methodNames, valueList);
        if (!query.isEmpty()) {
            if (entity.hasConnection()) {
                SQLConnection.execUpdateSql(query, databaseNumber, valueList, entity.selectConnection());
            } else {
                SQLConnection.execUpdateSql(query, databaseNumber, valueList);
            }
        }
    }

    public static String getStatusValueOfDeleteByDatabaseNumber(String databaseNumber) {
        DBConfigurationProperties prop = null;
        try {
            prop = new DBConfigurationProperties();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EntityManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String propKeyName = DELETE_STATUS + capitalizeFirstLetter(databaseNumber);
        String keyName = prop.getProperty(propKeyName);
        return keyName;
    }

    public static String toTableName(CoreEntity entity) {
        String s = entity.getClass().getSimpleName();
        s = s.substring(ENTITY.length(), s.length());
        s = s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
        return s;
    }

    public static String[] getAllAtributes(CoreEntity core) {
        int idx = 0;
        String[] ListOfGetMethods = new String[1001];
        Method m[] = core.getClass().getMethods();
        Method m1[] = CoreEntity.class.getMethods();

        for (Method m2 : m) {
            if (m2.toString().contains(core.getClass().getName() + SEPERATOR_DOT + GET)) {
                String[] S = m2.toString().split(" ");
                ListOfGetMethods[idx] = S[S.length - 1].substring(core.getClass().getName().length() + 1, S[S.length - 1].length() - 2);
                idx++;
            }
        }

        //select all GET methods from CoreEntity
        for (Method m11 : m1) {
            if (m11.toString().contains(CoreEntity.class.getName() + SEPERATOR_DOT + GET)) {
                String[] S = m11.toString().split(" ");
                ListOfGetMethods[idx] = S[S.length - 1].substring(CoreEntity.class.getName().length() + 1, S[S.length - 1].length() - 2);
                idx++;
            }
        }

        String[] ls = new String[idx];
        for (int i = 0; i < idx; i++) {
            String t = ListOfGetMethods[i];
            ls[i] = lowerFirstLetter(t.substring(GET.length(), t.length()));
        }
        //ListOfGetMethods[0]=Integer.toString(idx-1);//fist element of ListOfGetMethods 
        //keep the number of GetMethod.Calling part of this method should start numberin
        //from 1.
        return ls;
    }

    private static String lowerFirstLetter(String arg) {
        return arg = arg.substring(0, 1).toLowerCase() + arg.substring(1, arg.length());
    }

    public static void mapCarrierToEntity(Carrier carrier, CoreEntity entity, boolean setAll) throws QException {
        String[] attributes = getAllAtributes(entity);
        for (String attribute : attributes) {
            String val = carrier.isKeyExist(attribute) ? carrier.getValue(attribute).toString() : EMPTY_VALUE;
            if (setAll && (!val.equals(EMPTY_VALUE))) {
                setEntityValue(entity, attribute, val);
            } else if (!val.equals(EMPTY_VALUE)) {
                setEntityValue(entity, attribute, val);
            }
        }
        if (carrier.isKeyExist(CoreLabel.START_LIMIT)) {
            entity.setStartLimit(carrier.getValue(CoreLabel.START_LIMIT).toString());
        }
        if (carrier.isKeyExist(CoreLabel.END_LIMIT)) {
            entity.setEndLimit(carrier.getValue(CoreLabel.END_LIMIT).toString());
        }
//        if (carrier.isKeyExist(CoreLabel.EXCLUDED_FIELDS)) {
//            entity.addExcludedField(carrier.getValue(CoreLabel.EXCLUDED_FIELDS).toString().split(CoreLabel.WS_EXCLUDED_FIELDS_SEPERATOR));
//        }
//        if (carrier.isKeyExist(CoreLabel.INCLUDED_FIELDS)) {
//            entity.addIncludedField(carrier.getValue(CoreLabel.INCLUDED_FIELDS).toString().split(CoreLabel.WS_INCLUDED_FIELDS_SEPERATOR));
//        }

        if (carrier.isKeyExist(CoreLabel.SORT_TABLE)) {
            entity.addSortBy(carrier.getValue(CoreLabel.SORT_TABLE).toString().split(CoreLabel.WS_SORT_TABLE_SEPERATOR));
        }

        if (carrier.isKeyExist(INTERVAL_END_DATE)) {
            entity.setIntervalEndDate(carrier.getValue(INTERVAL_END_DATE).toString().trim());
        }

        if (carrier.isKeyExist(INTERVAL_FIELD)) {
            entity.setIntervalField(carrier.getValue(INTERVAL_FIELD).toString().trim());
        }

        if (carrier.isKeyExist(INTERVAL_SEPERATOR)) {
            entity.setIntervalSeperator(carrier.getValue(INTERVAL_SEPERATOR).toString().trim());
        }

        if (carrier.isKeyExist(INTERVAL_START_DATE)) {
            entity.setIntervalStartDate(carrier.getValue(INTERVAL_START_DATE).toString().trim());
        }

        if (carrier.isKeyExist(SUM_BY)) {
            entity.setSumBy(carrier.getValue(SUM_BY).toString().trim());
        }

        if (carrier.isKeyExist(GROUP_BY)) {
            String[] groupByFields = carrier.getValue("groupBy").toString().split(CoreLabel.IN);
            entity.addGroupBy(groupByFields);
        }

        if (carrier.isKeyExist(CoreLabel.WS_SORT_TABLE_TYPE_ASC)) {
            entity.addSortBy(carrier.getValue(CoreLabel.WS_SORT_TABLE_TYPE_ASC).toString().split(CoreLabel.WS_SORT_TABLE_SEPERATOR));
            entity.setSortByAsc(true);
        }

        if (carrier.isKeyExist(CoreLabel.WS_SORT_TABLE_TYPE_DESC)) {
            entity.addSortBy(carrier.getValue(CoreLabel.WS_SORT_TABLE_TYPE_DESC).toString().split(CoreLabel.WS_SORT_TABLE_SEPERATOR));
            entity.setSortByAsc(false);
        }

        if (carrier.isKeyExist(CoreLabel.DISTINCT_FIELDS)) {
            entity.addDistinctField(carrier.getValue(CoreLabel.DISTINCT_FIELDS).toString().split(CoreLabel.WS_SORT_TABLE_SEPERATOR));
        }

        if (carrier.isKeyExist(CoreLabel.DISTINCT_FIELDS)) {
            entity.addDistinctField(carrier.getValue(CoreLabel.DISTINCT_FIELDS).toString().split(CoreLabel.WS_SORT_TABLE_SEPERATOR));
        }

        if (carrier.isKeyExist(CoreLabel.SORT_TABLE_TYPE)) {
            boolean sortType = carrier.getValue(CoreLabel.SORT_TABLE_TYPE).toString().
                    trim().equals(CoreLabel.WS_SORT_TABLE_TYPE_ASC);
            entity.setSortByAsc(sortType);
        }
    }

    public static void mapCarrierToEntity(Carrier carrier, CoreEntity entity) throws QException {
        mapCarrierToEntity(carrier, entity, true);
    }

    public static void mapEntityToCarrier(CoreEntity entity, Carrier carrier, String tableName, boolean setNulls) throws QException {
        String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
        String[] values = SQLGenerator.getValuesOfAllGetMethodsOfEntity(entity, methodNames);

        for (int i = 0; i < methodNames.length; i++) {
            String val = values[i];
            if (setNulls) {
                int row = carrier.getTableRowCount(tableName);
                String col = lowerFirstLetter(methodNames[i].substring(GET.length(), methodNames[i].length()));
                carrier.setValue(tableName, row, col, values[i]);
            } else if (!val.equals("")) {
                int row = carrier.getTableRowCount(tableName);
                String col = lowerFirstLetter(methodNames[i].substring(GET.length(), methodNames[i].length()));
                carrier.setValue(tableName, row, col, values[i]);
            }

        }
    }

    public static void mapEntityToCarrier(CoreEntity entity, Carrier carrier, boolean setNulls) throws QException {
        String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
        String[] values = SQLGenerator.getValuesOfAllGetMethodsOfEntity(entity, methodNames);

        for (int i = 0; i < methodNames.length; i++) {
            String val = values[i];
            if (setNulls) {
                String col = lowerFirstLetter(methodNames[i].substring(GET.length(), methodNames[i].length()));
                carrier.setValue(col, values[i]);
            } else if (!val.equals("")) {
                String col = lowerFirstLetter(methodNames[i].substring(GET.length(), methodNames[i].length()));
                carrier.setValue(col, values[i]);
            }

        }
    }

    public static void mapCarrierToEntity(Carrier carrier, String tableName, int row, CoreEntity entity, boolean setAll) throws QException {
        String[] attributes = getAllAtributes(entity);
        for (String attribute : attributes) {
            String f1 = entity.selectLongEntityFieldName(attribute);
            String val = carrier.isKeyExist(tableName, row, f1)
                    ? carrier.getValue(tableName, row, f1).toString() : EMPTY_VALUE;
            if (setAll && (!val.equals(EMPTY_VALUE))) {
                setEntityValue(entity, attribute, val);//EGER KEY(COLUMNNAME) MOVCUD DEYILSE O ZAMAN HEMIN ADLI SET METODU MOVCUD OLMAYACAQ
            } else if (!val.equals(EMPTY_VALUE)) {
                setEntityValue(entity, attribute, val);
            }
        }
        String startDate = carrier.isKeyExist(tableName, row, CoreLabel.START_LIMIT)
                ? carrier.getValue(tableName, row, CoreLabel.START_LIMIT).toString() : "";
        String endDate = carrier.isKeyExist(tableName, row, CoreLabel.END_LIMIT)
                ? carrier.getValue(tableName, row, CoreLabel.END_LIMIT).toString() : "";
        entity.setStartLimit(startDate);
        entity.setEndLimit(endDate);

//        if (carrier.isKeyExist(tableName, row, CoreLabel.EXCLUDED_FIELDS)) {
//            entity.addExcludedField(carrier.getValue(tableName, row, CoreLabel.EXCLUDED_FIELDS).toString().split(CoreLabel.WS_EXCLUDED_FIELDS_SEPERATOR));
//        }
//        if (carrier.isKeyExist(tableName, row, CoreLabel.INCLUDED_FIELDS)) {
//            entity.addIncludedField(carrier.getValue(tableName, row, CoreLabel.INCLUDED_FIELDS).toString().split(CoreLabel.WS_INCLUDED_FIELDS_SEPERATOR));
//        }
        if (carrier.isKeyExist(tableName, row, CoreLabel.SORT_TABLE)) {
            entity.addSortBy(carrier.getValue(tableName, row, CoreLabel.SORT_TABLE).toString().split(CoreLabel.WS_SORT_TABLE_SEPERATOR));
        }

        if (carrier.isKeyExist(tableName, row, CoreLabel.WS_SORT_TABLE_TYPE_ASC)) {
            entity.addSortBy(carrier.getValue(tableName, row, CoreLabel.WS_SORT_TABLE_TYPE_ASC).toString().split(CoreLabel.WS_SORT_TABLE_SEPERATOR));
        }

        if (carrier.isKeyExist(tableName, row, CoreLabel.WS_SORT_TABLE_TYPE_DESC)) {
            entity.addSortBy(carrier.getValue(tableName, row, CoreLabel.WS_SORT_TABLE_TYPE_DESC).toString().split(CoreLabel.WS_SORT_TABLE_SEPERATOR));
        }

        if (carrier.isKeyExist(tableName, row, CoreLabel.DISTINCT_FIELDS)) {
            entity.addDistinctField(carrier.getValue(tableName, row, CoreLabel.DISTINCT_FIELDS).toString().split(CoreLabel.WS_INCLUDED_FIELDS_SEPERATOR));
        }

        if (carrier.isKeyExist(tableName, row, CoreLabel.SORT_TABLE_TYPE)) {
            boolean sortType = carrier.getValue(tableName, row, CoreLabel.SORT_TABLE_TYPE).toString().
                    trim().equals(CoreLabel.WS_SORT_TABLE_TYPE_ASC);
            entity.setSortByAsc(sortType);
        }
    }

    public static void mapCarrierToEntity(Carrier carrier, String tableName, CoreEntity entity, boolean setAll) throws QException {
        String[] attributes = getAllAtributes(entity);
        int rowc = carrier.getTableRowCount(tableName);
        for (int row = 0; row < rowc; row++) {
            mapCarrierToEntity(carrier, tableName, row, entity, setAll);
        }
    }

    public static void mapCarrierToEntity(Carrier carrier, String tableName, CoreEntity entity) throws QException {
        mapCarrierToEntity(carrier, tableName, entity, true);
    }

    public static void mapCarrierToEntity(Carrier carrier, String tableName, int row, CoreEntity entity) throws QException {
        mapCarrierToEntity(carrier, tableName, row, entity, true);
    }

    public static void setEntityValue(CoreEntity entity, String keyName, String keyValue) throws QException {
        try {
            String rs = "";
            String methodName = "set" + capitalizeOnlyFirstLetter(keyName);
            Method method = entity.getClass().getMethod(methodName, String.class);
            Object retObj = method.invoke(entity, keyValue);
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static String getEntityValue(CoreEntity entity, String keyName) throws QException {
        try {
            String rs = "";
            String methodName = "get" + capitalizeOnlyFirstLetter(keyName);
            Method method = entity.getClass().getMethod(methodName);
            Object retObj = method.invoke(entity);
            return (String) retObj;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    static String capitalizeOnlyFirstLetter(String arg) {
        arg = arg.substring(0, 1).toUpperCase() + arg.substring(1, arg.length());
        return arg;
    }

    public static String getMessageText(String messageCode) throws QException {
        if (messageCode.trim().equals("")) {
            return "Message code is empty";
        }

        EntityCrListItem ent = new EntityCrListItem();
        ent.setItemCode("errorMessage");
        ent.setItemKey(messageCode);
        ent.setLang(SessionManager.getCurrentLang());
        Carrier carrier = EntityManager.select(ent);

        if (ent.getItemValue().trim().equals("")) {
            return "{" + messageCode + "}";
        } else {
            return ent.getItemValue().trim();
        }
    }

    public static String getEntityFieldLabel1(String entityName, String fieldName) throws QException {
        try {
            String res = "";
            if (fieldName.trim().equals("")) {
                return "";
            }

            String line = "SELECT LABEL_TYPE,DESCRIPTION, FIELD_NAME FROM CR_ENTITY_LABEL WHERE";
            line += " STATUS='A' AND LANG='" + SessionManager.getCurrentLang() + "' AND FIELD_NAME='" + fieldName + "'";
            if (!entityName.trim().equals("")) {
                line += " AND ENTITY_NAME ='" + entityName + "'";
            }
            Carrier carrier = SQLConnection.execSelectSql(line,
                    new EntityCrEntityLabel().toTableName(), CoreLabel.DB_PRIMARY, new ArrayList());
            if (carrier.isKeyExist(new EntityCrEntityLabel().toTableName(), 0, EntityCrEntityLabel.DESCRIPTION)) {
                res = carrier.getValue(new EntityCrEntityLabel().toTableName(), 0, EntityCrEntityLabel.DESCRIPTION).toString();
            }

            return res;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getEntityFieldType(String[] fieldName) throws QException {
        return getEntityFieldType("", fieldName);
    }

    public static Carrier getEntityFieldType(String entityName, String[] fieldName) throws QException {
        try {
            String res = "";
            if (fieldName.length == 0) {
                return null;
            }

            String inLine = "";
            for (String fieldName1 : fieldName) {
                inLine += "'" + fieldName1 + "',";
            }
            inLine = inLine.substring(0, inLine.length() - 1);

            String line = "";
            line += " SELECT LABEL_TYPE, DESCRIPTION,FIELD_NAME ";
            line += " FROM CR_ENTITY_LABEL";
            line += " WHERE STATUS='A' ";
            line += " AND LANG='" + SessionManager.getCurrentLang() + "'";
            line += " AND FIELD_NAME IN (" + inLine + ")";
            line += entityName.trim().length() > 0 ? " AND ENTITY_NAME ='" + entityName + "'" : "";
            
            line += " UNION ";
            
            line += " SELECT  'STRING' AS LABEL_TYPE,ATTRIBUTE_NAME AS DESCRIPTION,CONCAT('sa',ID) AS FIELD_NAME ";
            line += " FROM CR_SUBMODULE_ATTRIBUTE_LIST ";
            line += " WHERE STATUS='A' ";
            line += " AND LANG='" + SessionManager.getCurrentLang() + "'";
            line += " AND CONCAT('sa',ID) IN (" + inLine + ")";

            Carrier carrier = SQLConnection.execSelectSql(line,
                    new EntityCrEntityLabel().toTableName(), CoreLabel.DB_PRIMARY, new ArrayList());

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

     public static Carrier getEntityFieldTypeByMatrixId(String matrixId,String[] fieldName) throws QException {
        try {
            String res = "";
            if (matrixId.length() == 0) {
                return null;
            }

            String inLine = "";
            for (String fieldName1 : fieldName) {
                inLine += "'" + fieldName1 + "',";
            }
            inLine = inLine.substring(0, inLine.length() - 1);

            String line = "";
            line += " SELECT short_name ,concat('sa',fk_submodule_attribute_id) COLUMN_NAME ";
            line += " FROM cr_inspection_matrix_list";
            line += " WHERE STATUS='A' ";
            line += " AND FK_USER_ID='" + SessionManager.getCurrentUserId() + "'";
            line += " AND FK_PARENT_ID='" + matrixId + "'";
            line += " AND concat('sa',fk_submodule_attribute_id) IN (" + inLine + ")";
            line += " AND SHORT_NAME != ''";
            
            Carrier carrier = SQLConnection.execSelectSql(line,
                    new EntityCrEntityLabel().toTableName(), CoreLabel.DB_PRIMARY, new ArrayList());

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

     public static void copyCarrier(Carrier sourceCarrier, String tablename, Carrier destinationCarrier) throws QException {
        try {
            int rowD = destinationCarrier.getTableRowCount(tablename);
            int rowS = sourceCarrier.getTableRowCount(tablename);
            String[] cols = sourceCarrier.getTableColumnNames(tablename);
            for (int i = rowD; i <= rowD + rowS; i++) {
                for (String col : cols) {
                    destinationCarrier.setValue(tablename, i, col, sourceCarrier.getValue(tablename, rowS + rowD - i - 1, col).toString());
                }
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier join(CoreEntity sourceEntity, String sourceJoinField, CoreEntity destinatinEntity, String destinationJoinField, String joinType, boolean withLimit) throws QException {
        String res = "";
        ArrayList valuesArr = new ArrayList();
        String srcMethodNames[] = SQLGenerator.getAllGetMethodNames(sourceEntity);
        String[] srcValues = SQLGenerator.getValuesOfAllGetMethodsOfEntity(sourceEntity, srcMethodNames);
//        String srcQuery = SQLGenerator.selectGenerator(sourceEntity, CoreLabel.DB_PRIMARY, srcMethodNames, srcValues, valuesArr, false, true);

        String dstMethodNames[] = SQLGenerator.getAllGetMethodNames(destinatinEntity);
        String[] dstValues = SQLGenerator.getValuesOfAllGetMethodsOfEntity(destinatinEntity, dstMethodNames);
        String dstQuery = SQLGenerator.selectGenerator(destinatinEntity, CoreLabel.DB_PRIMARY, dstMethodNames, dstValues, valuesArr, false, true);

        String stLine;
        if (withLimit) {
            stLine = "SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY " + SQLGenerator.selectSortPartOfSelect(sourceEntity) + ") AS 'ROW',";
        } else {
            stLine = "SELECT";
        }
        stLine += SQLGenerator.generateSelectFieldPartOfSelectSql(sourceEntity, CoreLabel.DB_PRIMARY, true) + "\n";
        stLine += " ,T2.*" + "\n";
        stLine += " FROM " + "\n";
        stLine += SQLGenerator.getTableNameBasedOnEntity(sourceEntity) + "\n";
        stLine += " " + "\n";
        stLine += joinType + "\n";
        stLine += " " + "\n";
        stLine += "(" + "\n";
        stLine += dstQuery + "\n";
        stLine += ")" + " T2 \n";
        stLine += " ON " + "\n";
        stLine += sourceEntity.toDBTableName() + "." + SQLGenerator.seperateTableFieldNameWithUnderscore(sourceJoinField).toUpperCase();
        stLine += "=";
        stLine += "T2." + destinatinEntity.toDBTableName() + UNDERSCORE + SQLGenerator.seperateTableFieldNameWithUnderscore(destinationJoinField).toUpperCase() + "\n";
        stLine += SQLGenerator.generateWherePartOfSelect(sourceEntity, CoreLabel.DB_PRIMARY, srcMethodNames, srcValues, valuesArr, true, true) + "  \n";
        if (withLimit) {
            stLine = stLine + ") A WHERE A.ROW>= " + sourceEntity.selectStartLimit() + "  AND A.ROW<=" + sourceEntity.selectEndLimit() + ";";
        }
        res = stLine;

        Carrier c = SQLConnection.execSelectSql(stLine, sourceEntity.toTableName(), CoreLabel.DB_PRIMARY, valuesArr);

        return c;
    }

    public static String getListItemValue(String code, String key) throws QException {
        return getListItem(code, key).getItemValue();
    }

    public static String getListItemParam1(String code, String key) throws QException {
        return getListItem(code, key).getParam1();
    }

    public static String getListItemParam2(String code, String key) throws QException {
        return getListItem(code, key).getParam2();
    }

    public static String getListItemParam3(String code, String key) throws QException {
        return getListItem(code, key).getParam3();
    }

    public static String getListItemParam4(String code, String key) throws QException {
        return getListItem(code, key).getParam4();
    }

    public static String getListItemParam5(String code, String key) throws QException {
        return getListItem(code, key).getParam5();
    }

    private static EntityCrListItem getListItem(String code, String key) throws QException {
        try {
            if (code.trim().isEmpty() || key.trim().isEmpty()) {
                return null;
            }
            EntityCrListItem ent = new EntityCrListItem();
            ent.setItemCode(code);
            ent.setItemKey(key);
            select(ent);
            return ent;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getListItemValues(String code) throws QException {
        try {
            if (code.trim().isEmpty()) {
                return new Carrier();
            }
            EntityCrListItem ent = new EntityCrListItem();
            ent.setItemCode(code);
            Carrier carrier = select(ent);
            carrier = carrier.getKeyValuesPairFromTable(ent.toTableName(), EntityCrListItem.ITEM_KEY, EntityCrListItem.ITEM_VALUE);
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getListItemValues(String code, String tablename) throws QException {
        try {
            if (code.trim().isEmpty()) {
                return new Carrier();
            }
            EntityCrListItem ent = new EntityCrListItem();
            ent.setItemCode(code);
            Carrier carrier = select(ent);
            Carrier newc = new Carrier();
            int cnt = carrier.getTableRowCount(ent.toTableName());
            for (int i = 0; i < cnt; i++) {
                newc.setValue(tablename, i, EntityCrListItem.ITEM_KEY,
                        carrier.getValue(ent.toTableName(), i, EntityCrListItem.ITEM_KEY).toString());
                newc.setValue(tablename, i, EntityCrListItem.ITEM_VALUE,
                        carrier.getValue(ent.toTableName(), i, EntityCrListItem.ITEM_VALUE).toString());
            }
//            carrier = carrier.getKeyValuesPairFromTable(ent.toTableName(), EntityCrListItem.ITEM_KEY, EntityCrListItem.ITEM_VALUE);
            return newc;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static int getRowCount(CoreEntity entity) throws QException {
        try {
            String res = "";
            entity.splitAndStatementField();
            ArrayList valuesArr = new ArrayList();
            String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
            String[] values = SQLGenerator.getValuesOfAllGetMethodsOfEntity(entity, methodNames);

            String stLine = "SELECT COUNT(ID) AS ROW_COUNT ";
            stLine += " FROM " + "\n";
            stLine += entity.toDBTableName() + "\n";
            stLine += SQLGenerator.generateWherePartOfSelect(entity, CoreLabel.DB_PRIMARY, methodNames, values, valuesArr, true, false) + "  \n";
            res = stLine;

            Carrier c = SQLConnection.execSelectSql(stLine, entity.toTableName(), CoreLabel.DB_PRIMARY, valuesArr);
            int rowCount = Integer.valueOf(c.getValue(entity.toTableName(), 0, "rowCount").toString());
            return rowCount;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static double getSumOfColumn(CoreEntity entity, String columnName) throws QException {
        try {
            String res = "";
            ArrayList valuesArr = new ArrayList();
            String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
            String[] values = SQLGenerator.getValuesOfAllGetMethodsOfEntity(entity, methodNames);

            String stLine = "SELECT ISNULL(LTRIM(Str(SUM(CONVERT(FLOAT," + SQLGenerator.seperateTableFieldNameWithUnderscore(columnName) + ")),25,4)),'0') AS FIELD_SUM ";
            stLine += " FROM " + "\n";
            stLine += entity.toDBTableName() + "\n";
            stLine += SQLGenerator.generateWherePartOfSelect(entity, CoreLabel.DB_PRIMARY, methodNames, values, valuesArr, true, false) + "  \n";
            res = stLine;

            Carrier c = SQLConnection.execSelectSql(stLine, entity.toTableName(), CoreLabel.DB_PRIMARY, valuesArr);
            double sum = Double.valueOf(c.getValue(entity.toTableName(), 0, "fieldSum").toString());
            return sum;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static String getSmsMessageText(String messageCode) throws QException {
        return getSmsMessageText(messageCode, new String[]{});
    }

    public static String getSmsMessageText(String messageCode, String[] params) throws QException {
        try {
            if (messageCode.trim().equals("")) {
                return "";
            }

            EntityCrListItem ent = new EntityCrListItem();
            ent.setItemCode("smsMessage");
            ent.setItemKey(messageCode);
            ent.setLang(CoreSystem.getCurrentLanguage());
            Carrier carrier = EntityManager.select(ent);

            String line = "";
            if (ent.getItemValue().trim().equals("")) {
                return "{" + messageCode + "}";
            } else {
                line = ent.getItemValue().trim();
            }

            for (int i = 0; i < params.length; i++) {
                line = line.replaceFirst("@param" + (i + 1), params[i].trim());
            }
            return line;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static String[] getIntegerFieldsFromEntityLabel(String[] fields) throws QException {
        String fieldsIn = "";
        for (String field : fields) {
            fieldsIn += field.substring(3, field.length()) + CoreLabel.IN;
        }
        EntityCrEntityLabel ent = new EntityCrEntityLabel();
        ent.setFieldName(fieldsIn);
        ent.setLabelType(ENTITY_LABEL_TYPE);
        String line = EntityManager.select(ent).getValueLine(
                ent.toTableName(), EntityCrEntityLabel.FIELD_NAME, CoreLabel.IN);
        return line.split(CoreLabel.IN);
    }

    public static Carrier groupBy(CoreEntity entity, String groupByFields[], String sumByField) throws QException {
        try {
            if (sumByField.trim().equals("")) {
                return new Carrier();
            }
            String res = "";
            ArrayList valuesArr = new ArrayList();
            String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
            String integerFields[] = getIntegerFieldsFromEntityLabel(methodNames);
            String[] values = SQLGenerator.getValuesOfAllGetMethodsOfEntity(entity, methodNames);
            String sumByFieldWithSeperator = SQLGenerator.seperateTableFieldNameWithUnderscore(sumByField).toUpperCase();

            String sumByFields = "";
            for (String field : integerFields) {
                String fn = SQLGenerator.seperateTableFieldNameWithUnderscore(field).toUpperCase();
                String r = "";
                sumByFields += "ISNULL(LTRIM(Str(SUM(CONVERT(FLOAT,";
                sumByFields += fn;
                sumByFields += ")),25,4)),'0') AS " + fn + SEPERATOR_COMMA;
            }
            sumByFields = sumByFields.substring(0, sumByFields.length() - SEPERATOR_COMMA.length());

            String selectFields = "";
            for (String fields : groupByFields) {
//                System.out.println("start fields->" + fields);
                if (!fields.trim().toLowerCase().equals("id")) {
//                    System.out.println("start fields->" + fields);
                    selectFields += SQLGenerator.seperateTableFieldNameWithUnderscore(fields).toUpperCase() + SEPERATOR_COMMA;
                }
            }

            selectFields = !selectFields.trim().equals("")
                    ? selectFields.substring(0, selectFields.length() - SEPERATOR_COMMA.length()) : "";

            String sortByField = entity.hasSortBy()
                    ? SQLGenerator.seperateTableFieldNameWithUnderscore(entity.selectSortString()).toUpperCase()
                    + " " + entity.selectSortByAscValue()
                    : selectFields;

            String stLine = "";
            stLine += !selectFields.trim().equals("")
                    ? "SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY " + sortByField + ") AS 'ROW',"
                    : "SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY ISNULL(ROUND(SUM(CONVERT(FLOAT," + sumByFieldWithSeperator + ")),2),'0')) AS 'ROW',";
            stLine += !selectFields.trim().equals("") ? " " + selectFields + "," : "";
//            stLine += "ISNULL(LTRIM(Str(SUM(CONVERT(FLOAT," + sumByFieldWithSeperator + ")),25,4)),'0') AS " + sumByFieldWithSeperator + " ";
            stLine += sumByFields;
            stLine += " FROM " + "\n";
            stLine += entity.toDBTableName() + "\n";
            stLine += SQLGenerator.generateWherePartOfSelect(entity, CoreLabel.DB_PRIMARY, methodNames, values, valuesArr, true, false) + "  \n";
            if (!selectFields.trim().equals("")) {
                stLine += " GROUP BY " + selectFields;
            }
            if (entity.hasSumBy()) {
                String key = "SUM(CONVERT(FLOAT," + SQLGenerator.seperateTableFieldNameWithUnderscore(entity.selectSumBy()).toUpperCase() + "))";
                String value = getEntityValue(entity, entity.selectSumBy());
                if (!value.trim().equals("")) {
                    String t = SQLGenerator.getSingleClausOfWherePartOfSelect(entity.toTableName(), key, value, valuesArr);
                    stLine += t.trim().length() > 0 ? " HAVING " + t : "";
                }
            }
            stLine += ") A WHERE A.ROW>= " + entity.selectStartLimit() + "  AND A.ROW<=" + entity.selectEndLimit() + ";";
            res = stLine;

            //with LIMIT
            Carrier c = SQLConnection.execSelectSql(stLine, entity.toTableName(), CoreLabel.DB_PRIMARY, valuesArr);
            return c;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier groupByInterval(CoreEntity entity) throws QException {
        try {
            HashMap<String, String> groupByKV = new HashMap<>();
            String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
            String integerFields[] = getIntegerFieldsFromEntityLabel(methodNames);

            //eger sumBy varsa o zaman hemin field-in deyerlerini goturmek lazimdir.
            // where hissesinde olmamalidir.
            // having  hissesinde olmalidir
            if (entity.hasSumBy()) {
                for (String field : integerFields) {
                    String v = getEntityValue(entity, field);
                    if (v.trim().length() > 0) {
                        groupByKV.put(field, v);
                        setEntityValue(entity, field, "");
                    }
                }
            }

            ArrayList valuesArr = new ArrayList();
            String stLine = getCoreSqlQueryForIntervalGroup(entity, valuesArr);
            stLine = addGroupByTpIntervalFilter(entity, stLine, valuesArr, groupByKV);
//            System.out.println("sql->" + stLine);
            Carrier c = new Carrier();
            c = SQLConnection.execSelectSql(stLine, entity.toTableName(), CoreLabel.DB_PRIMARY, valuesArr);
            return c;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static String getCoreSqlQueryForIntervalGroup(CoreEntity entity, ArrayList valuesArr) throws QException {
        try {
            String intervalField = entity.selectIntervalField();
            String intervalStartDate = entity.selectIntervalStartDate();
            String intervalEndDate = entity.selectIntervalEndDate();
            String intervalSeperator = entity.selectIntervalSeperator();

            if (!entity.hasIntervalField()) {
                return "";
            }

            Date startDate = new Date();
            Date endDate = QDate.convertStringToDate(intervalStartDate);
            String fieldName = SQLGenerator.addUnderscoreToFieldName(intervalField).trim().toUpperCase();
            String stLine = "";
            do {
//                startDate = QDate.addDay(endDate, 1);
                startDate = endDate;
                endDate = getNextIntervalDate(startDate, intervalSeperator, intervalEndDate);
                Date endDate1 = QDate.addDay(endDate, -1);
                String dateVal = QDate.convertDateToString(startDate) + CoreLabel.BN + QDate.convertDateToString(endDate1);
                setEntityValue(entity, intervalField, dateVal);

                String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
                String[] values = SQLGenerator.getValuesOfAllGetMethodsOfEntity(entity, methodNames);

                String replaceFieldLine = "'" + QDate.convertToDateString(QDate.convertDateToString(startDate)) + "-"
                        + QDate.convertToDateString(QDate.convertDateToString(endDate1)) + "' AS " + fieldName;

                String st = SQLGenerator.selectGenerator(entity, CoreLabel.DB_PRIMARY, methodNames, values, valuesArr, false) + SPACE + "\n";
                st = st.replaceFirst(fieldName, replaceFieldLine);

                stLine += st;
                stLine += SPACE + UNION + SPACE;

            } while (endDate.before(QDate.convertStringToDate(intervalEndDate)));
            stLine = stLine.substring(0, stLine.length() - UNION.length() - 1);
            return stLine;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static String addGroupByTpIntervalFilter(
            CoreEntity entity, String sqlQuery, ArrayList valuesArr, HashMap<String, String> groupByKV) throws QException {
        String stLine = "";
        try {
            String intervalField = entity.selectIntervalField();
            String sumByField = entity.selectSumBy();
            String[] groupByFields = entity.selectGroupBy();
//            + SPACE+getSortByClause(entity)

            //insertval secilib, lakin groupby duymesi basilmadigi halda
            if (sumByField.trim().equals("")) {
                stLine += "SELECT  *  FROM (SELECT ROW_NUMBER() OVER (ORDER BY " + SQLGenerator.selectSortPartOfSelect(entity) + ") AS 'ROW',D.*";
                stLine += " FROM (";
                stLine += sqlQuery;
                stLine += ") D ) A WHERE A.ROW>= " + entity.selectStartLimit() + "  AND A.ROW<=" + entity.selectEndLimit() + ";";

                //insertval secilib, ve groupby duymesi basildigi halda
            } else {
                String methodNames[] = SQLGenerator.getAllGetMethodNames(entity);
                String integerFields[] = getIntegerFieldsFromEntityLabel(methodNames);
                String sumByFields = "";

                for (String field : integerFields) {
                    String fn = SQLGenerator.seperateTableFieldNameWithUnderscore(field).toUpperCase();
                    String r = "";
                    sumByFields += "ISNULL(LTRIM(Str(SUM(CONVERT(FLOAT,";
                    sumByFields += fn;
                    sumByFields += ")),25,4)),'0') AS " + fn + SEPERATOR_COMMA;
                }
                sumByFields = sumByFields.substring(0, sumByFields.length() - SEPERATOR_COMMA.length());

                String sumByFieldWithSeperator = SQLGenerator.seperateTableFieldNameWithUnderscore(sumByField).toUpperCase();
                String tmp = SQLGenerator.seperateTableFieldNameWithUnderscore(intervalField).toUpperCase();
                String s = entity.hasIntervalField() ? "(SUBSTRING(" + tmp + ",7,4)+"
                        + "SUBSTRING(" + tmp + ",4,2)+SUBSTRING(" + tmp + ",1,2))" : "";

                String selectFields = "";
                for (String fields : groupByFields) {
                    if (!fields.trim().toLowerCase().equals("id") && !fields.trim().toLowerCase().equals(intervalField.toLowerCase())) {
                        selectFields += SQLGenerator.seperateTableFieldNameWithUnderscore(fields).toUpperCase() + SEPERATOR_COMMA;
                    }
                }
                selectFields = !selectFields.trim().equals("")
                        ? selectFields.substring(0, selectFields.length() - SEPERATOR_COMMA.length()) : "";

                stLine += "SELECT  *  FROM (SELECT ROW_NUMBER() OVER (ORDER BY ";
                stLine += entity.hasIntervalField() ? SPACE + s : SPACE + sumByFieldWithSeperator;
                stLine += SPACE + "ASC"; //getSortByClause(entity);
                stLine += SPACE + ") AS 'ROW',*";
                stLine += SPACE + "FROM (SELECT";
                stLine += !selectFields.trim().equals("") ? SPACE + tmp + "," + selectFields + "," : SPACE + tmp + ", ";
//                stLine += SPACE + "LTRIM(Str(ROUND(SUM(CONVERT(FLOAT," + sumByFieldWithSeperator + ")),2),25,4)) AS " + sumByFieldWithSeperator + "";
                stLine += SPACE + sumByFields;
                stLine += SPACE + "FROM";
                stLine += SPACE + "(";
                stLine += SPACE + sqlQuery;
                stLine += SPACE + ") T";
                stLine += SPACE + "GROUP BY " + SPACE + tmp;
                stLine += !selectFields.trim().equals("") ? "," + selectFields : "";
                if (entity.hasSumBy()) {
//                    String key = "SUM(CONVERT(FLOAT," + SQLGenerator.seperateTableFieldNameWithUnderscore(entity.selectSumBy()).toUpperCase() + "))";
//                    String value = getEntityValue(entity, entity.selectSumBy());
//                    if (!value.trim().equals("")) {
//                        stLine += " HAVING " + SQLGenerator.getSingleClausOfWherePartOfSelect(entity.toTableName(), key, value, valuesArr);
//                    }
                    String having = "";
                    for (String field : integerFields) {
                        String value = "";
                        try {
                            value = groupByKV.get(field);
                            value = value == null ? "" : value;
                        } catch (Exception e) {
                        }

                        if (!value.trim().equals("")) {
                            String key = "SUM(CONVERT(FLOAT," + SQLGenerator.seperateTableFieldNameWithUnderscore(field).toUpperCase() + "))";
                            having += SPACE + SQLGenerator.getSingleClausOfWherePartOfSelect(entity.toTableName(), key, value, valuesArr) + SPACE + " AND ";
                        }
                    }
                    having = having.trim().length() > 0 ? " HAVING " + having.substring(0, having.length() - 4) : "";
                    stLine += having;
                }

                stLine += ") D ) A WHERE A.ROW>= " + entity.selectStartLimit() + "  AND A.ROW<=" + entity.selectEndLimit() + ";";

            }
            return stLine;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    private static String getSortByClause(CoreEntity ent) {
        String s;
        if (ent.selectSortByAsc()) {
            s = " ASC ";
        } else {
            s = " DESC";
        }
        return s;
    }

    private static Date getNextIntervalDate(Date date, String intervalSeperator, String intervalEndDate) {
        Date newDate = new Date();
        switch (intervalSeperator) {
            case "1":
                newDate = QDate.addDay(date, 1);
                break;
            case "2":
                newDate = QDate.addDay(date, 7);
                break;
            case "3":
                newDate = QDate.addMonth(date, 1);
                break;
            case "4":
                newDate = QDate.addMonth(date, 3);
                break;
            case "5":
                newDate = QDate.addYear(date, 1);
                break;
            default:
                break;
        }
        if (newDate.after(QDate.convertStringToDate(intervalEndDate))) {
            newDate = QDate.convertStringToDate(intervalEndDate);
        }
        return newDate;
    }

    public static void addSequence(Carrier carrier, String key) {
        try {
            ListSequenceConfigurationProperties prop = new ListSequenceConfigurationProperties();
            String fields = prop.getProperty(key);

            String[] fieldsArr = fields.split(",");
            carrier.addSequence(fieldsArr);
        } catch (Exception ex) {
            new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static String getListSequenceByKey(String key) {
        ListSequenceConfigurationProperties prop = null;
        try {
            prop = new ListSequenceConfigurationProperties();
        } catch (Exception ex) {
            new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
        return prop.getProperty(key);
    }

    /*birinci xalis entity melumatları user_controller cədvəlindən çəkilir. Daha sonra baxılır ki, entity-nin dəyəri hansıdır. 
    Burada iki hal ola bilir. 1-ci entity-yə bir veb servis baglana bilər və ya (2-ci) bir başa entity-nin təyin edilmiş sütünuna (key) 
    dəyər mənimsədilə bilinər. Aşağıdakı metodda birinci veb servislər çağrılır və dəyərləri entity-yə mənimsədilir. 
    İkincisində iki entity-yə təyin edilmiş açar sözlər (sütünlar) dəyərlərinə görə mənimsədirlir*/
    public static void entitySelectController(CoreEntity entity) throws QException {
        try {
            String componentId = ENTITY_PREFIX + entity.toTableName();
            if (componentId.trim().equals("")) {
                return;
            }

            Carrier carrier = EntityManager.getUserControllerList(componentId, "");
            int count = carrier.getTableRowCount(CoreLabel.RESULT_SET);
            for (int i = 0; i < count; i++) {
                String inputKey = carrier.getValue(CoreLabel.RESULT_SET, i, EntityCrUserControllerList.INPUT_KEY).toString();
//                String inputValue = carrier.getValue(EntityCrUserControllerList.INPUT_VALUE).toString();
//                String permissionType = carrier.getValue(EntityCrUserControllerList.PERMISSION_TYPE).toString();
                //inputValue bos olarsa o zaman inputKey-in deyeri vebservisin adini bildirir

                Carrier extCarrier = new Carrier();
                extCarrier = extCarrier.callService(inputKey);
                EntityManager.mapCarrierToEntity(extCarrier, entity);
            }

//            if (inputValue.trim().equals("")) {
//                Carrier extCarrier = new Carrier();
//                extCarrier = extCarrier.callService(inputKey);
//                EntityManager.mapCarrierToEntity(extCarrier, entity);
//            } else {
//                setEntityValue(entity, inputKey, inputValue);
//            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getUserControllerList(String entityName, String fkUserId) throws QException {
        try {
            if (entityName.trim().equals("")) {
                return new Carrier();
            }
            String stLine = "";

            stLine += " SELECT * ";
            stLine += " FROM ";
            stLine += " CR_USER_CONTROLLER_LIST ";
            stLine += " WHERE ";
            stLine += " COMPONENT_TYPE = '" + CoreLabel.USER_CONTROLLER_COMPONENT_TYPE_ENTITY + "' ";
            stLine += fkUserId.trim().equals("") ? "" : " AND FK_USER_ID = " + fkUserId;
            stLine += " AND FK_COMPONENT_ID = '" + entityName + "' ";
            stLine += " AND STATUS = 'A' ";

            Carrier c = SQLConnection.execSelectSql(stLine, CoreLabel.RESULT_SET, CoreLabel.DB_PRIMARY, new ArrayList());
            return c;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static String getEnumList(String itemCode) throws QException {
        if (SessionManager.isCurrentEmployeeAdmin()) {
            return "";
        }

        String itemKey = EMPTY_VALUE + CoreLabel.IN;
        EntityCrUserControllerList ent = new EntityCrUserControllerList();
        ent.setFkUserId(SessionManager.getCurrentUserId());
        ent.setControllerType("2");
        ent.setComponentType(itemCode);
        itemKey += EntityManager.select(ent).
                getValueLine(ent.toTableName(),
                        EntityCrUserControllerList.FK_COMPONENT_ID, CoreLabel.IN);
        return itemKey;
    }
}
