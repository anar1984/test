package utility.sqlgenerator;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import label.CoreLabel;
import resources.config.Config;
import utility.Carrier;
import utility.CoreEntity;
import utility.DBConfigurationProperties;
import utility.QException;
import utility.SessionManager;
import utility.WhereSingle;
import static utility.sqlgenerator.SQLGenerator.EQUAL;
import static utility.sqlgenerator.SQLGenerator.SPACE;
import static utility.sqlgenerator.SQLGenerator.seperateTableFieldNameWithUnderscore;
import static utility.sqlgenerator.SQLGenerator.seperateTableFieldNameWithUnderscore;

public class SQLGenerator {

    public static String ENTITY = "Entity";
    public static String INSERT_INTO = "INSERT INTO ";
    public static String SEPERATOR_COMMA = ",";
    public static String QUESTION_MARK = "?";
    public static String SEPERATOR_DOT = ".";
    public static String OPEN_BRACKET = "(";
    public static String CLOSE_BRACKET = ")";
    public static String GET = "get";
    public static String UNDERSCORE = "_";
    public static String VALUES = "VALUES";
    public static String QUOTE = "'";
    public static String SELECT = "SELECT";
    public static String FROM = "FROM";
    public static String STAR = "*";
    public static String SPACE = " ";
    public static String WHERE = "WHERE";
    public static String UPDATE = "UPDATE";
    public static String DELETE = "DELETE";
    public static String SET = "SET";
    public static String TWO_DOTS = ":";
    public static String AND = "AND";
    public static String OR = "OR";
    public static String EQUAL = "=";
    public static String GT = "GT%";
    public static String LT = "LT%";
    public static String GE = "GE%";
    public static String LE = "LE%";
    public static String NE = "NE%";
    public static String BN = "%BN%";
    public static String LK = "LK";
    public static String IN = "%IN%";
    public static String FAIZ = "%%";
    public static String COMMAND_GT = ">";
    public static String COMMAND_LT = "<";
    public static String COMMAND_GE = ">=";
    public static String COMMAND_LE = "<=";
    public static String COMMAND_NE = "<>";
    public static String COMMAND_BN = "BETWEEN";
    public static String COMMAND_LK = "LIKE";
    public static String COMMAND_IN = "IN";

    private static final String TABLE_SQL_SCRIPT = "SQL_SCRIPT";
    private static final String FIELD_ID = "ID";
    private static final String FIELD_PART = "PART";
    private static final String FIELD_SQL = "SQL";
    private static final String ORDER_BY = "ORDER BY";
    private static final String PATTERN_FOR_PARAMS = ":param";
    private static final String HAS_UNDERSCORE_IN_TABLE_NAME = "hasUnderscoreInTableName";
    private static final String HAS_UNDERSCORE_IN_FIELD_NAME = "db.has.underscore.field-name.primary";
    //private static String = ;

    public static void main(String arg[]) {

    }

    public static String insertGenerator(CoreEntity core) {
        /*fist element of ListOfGetMethods keep the number of GetMethod.Calling part of this method should start number in from 1*/
        return null; // insertGenerator(core, label.LabelDBConfig.DBTYPE_PRIMARY);
    }

    public static String insertGenerator(CoreEntity core, String databaseNumber, String[] methodNames) {

        String retQuery = INSERT_INTO + SPACE + getTableNameBasedOnEntity(core) + SPACE + OPEN_BRACKET
                + getTableFieldNameFromList(methodNames, databaseNumber) + CLOSE_BRACKET
                + VALUES + SPACE + OPEN_BRACKET
                + getParamQuestionPartOfInsert(methodNames) + CLOSE_BRACKET;

        return retQuery;
    }

    private static String getParamQuestionPartOfInsert(String[] methodNames) {
        String s = "";
        for (String methodName : methodNames) {
            s = s + "?,";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

//    public static String updateGenerator(CoreEntity entity) throws
//            Exception, SecurityException, IllegalAccessException,
//            IllegalArgumentException, InvocationTargetException {
//
//        return updateGenerator(entity, LabelDBConfig.DB_PRIMARY);
//    }
    public static String updateGenerator(CoreEntity entity, String databaseNumber, String[] methodNames, ArrayList valueList) {

        String updateSql = UPDATE + SPACE + getTableNameBasedOnEntity(entity) + SPACE + SET + SPACE
                + generateSetPartOfUpdateSql(entity, databaseNumber, methodNames);
        String wPart = generateWherePartOfUpdate(entity, entity.getId().trim(), valueList);
        if (wPart.equals("")) {
            return "";
        }
        updateSql += wPart;
        return updateSql;
    }

    public static String deleteGenerator(CoreEntity coreEnt) throws QException {
        try {
            /*fist element of ListOfGetMethods keep the number of GetMethod.Calling part of this method should start number in from 1*/
 /*String methodNames[] =getAllGetMethodNames(core);
            
            String retQuery = INSERT_INTO + getTableNameBasedOnEntity(core) + OPEN_BRACKET + getTableFieldNameFromList(methodNames) +CLOSE_BRACKET+
            VALUES + OPEN_BRACKET +getValuesOfAllGetMethodsOfEntityInOneString(core,methodNames) +CLOSE_BRACKET ;
             */
            String deleteSql = DELETE + SPACE + getTableNameBasedOnEntity(coreEnt);
            String wPart = generateWherePartOfUpdateSql(coreEnt).trim();
            if (!wPart.equals("")) {
                deleteSql += SPACE + WHERE + SPACE + wPart;
            }
            return deleteSql;
        } catch (QException ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    ex);
        }
    }

    private static String generateSetPartOfUpdateSql(CoreEntity coreEnt, String[] methodNames) {
        return generateSetPartOfUpdateSql(coreEnt, CoreLabel.DB_PRIMARY, methodNames);
    }

    private static String generateSetPartOfUpdateSql(CoreEntity coreEnt, String databaseNumber, String[] methodNames) {
        String setPart = SPACE;
//        String[] methodNames = getAllGetMethodNames(coreEnt);
        for (String methodName : methodNames) {
//            String vl = executeMethod(coreEnt, methodNames[i]).toString();
//			if (!vl.trim().isEmpty()){
            setPart += seperateTableFieldNameWithUnderscore(methodName.substring("GET".length(), methodName.length()), databaseNumber).toUpperCase() + EQUAL + "?" + SPACE + SEPERATOR_COMMA + SPACE;
//			}
        }
        return setPart.substring(1, setPart.length() - SEPERATOR_COMMA.length() - 1);
    }

    public static String generateSelectFieldPartOfSelectSql(CoreEntity coreEnt, String databaseNumber, boolean addEntityNameToAS) {
        String st = "";
        if (coreEnt.hasDistinctFields()) {
            st = selectFieldNameGeneratorByDistinctFileds(coreEnt, addEntityNameToAS);
//        } else if (coreEnt.hasIncludedField()) {
//            st = selectFieldNameGeneratorByIncludedFileds(coreEnt, addEntityNameToAS);
        } else {
            st = selectFieldNameGeneratorByMethodnames(coreEnt, addEntityNameToAS);
        }
        return st;
    }

    public static String selectFieldNameGeneratorByDistinctFileds(CoreEntity coreEnt, boolean addEntityNameToAS) {
        String tablename = coreEnt.toDBTableName();
        String st = SPACE + " DISTINCT ";
        String[] arg = coreEnt.selectDistinctFields();
        for (String arg1 : arg) {
            String fname = seperateTableFieldNameWithUnderscore(arg1).toUpperCase();
            if (addEntityNameToAS) {
                st += tablename + "." + fname;
                st += " AS " + tablename.trim() + UNDERSCORE + fname.trim();
            } else {
                st += fname;
                // st += " AS " + tablename + UNDERSCORE + fname;
            }
            st += SPACE + SEPERATOR_COMMA + SPACE;
        }
        return st.substring(1, st.length() - SEPERATOR_COMMA.length() - 1);
    }

    public static String selectFieldNameGeneratorByIncludedFileds(CoreEntity coreEnt, boolean addEntityNameToAS) {
        String tablename = coreEnt.toDBTableName();
        String st = SPACE;
        String[] arg = coreEnt.selectIncludedFields();
        for (String arg1 : arg) {
            String fname = seperateTableFieldNameWithUnderscore(arg1).toUpperCase();
            if (addEntityNameToAS) {
                st += tablename + "." + fname;
                st += " AS " + tablename + UNDERSCORE + fname;
            } else {
                st += fname;
                // st += " AS " + tablename + UNDERSCORE + fname;
            }
            st += SPACE + SEPERATOR_COMMA + SPACE;
        }
        return st.substring(1, st.length() - SEPERATOR_COMMA.length() - 1);
    }

    private static String selectFieldNameGeneratorByMethodnames(CoreEntity coreEnt, boolean addEntityNameToAS) {
        String tablename = coreEnt.toDBTableName();
        String st = SPACE;
        String[] methodNames = getAllGetMethodNames(coreEnt);
        for (String methodName : methodNames) {
            String fname = methodName.substring("GET".length(), methodName.length());
            fname = lowerFirstLetter(fname).trim();
//            if (!coreEnt.hasExcludedFieldName(fname)) {
            String fname1 = seperateTableFieldNameWithUnderscore(fname).trim().toUpperCase();
            if (addEntityNameToAS) {
                st += tablename + "." + fname1;
                st += " AS " + tablename + UNDERSCORE + fname1;
            } else {
                st += fname1;
                // st += " AS " + tablename + UNDERSCORE + fname1;
            }
            st += SPACE + SEPERATOR_COMMA + SPACE;
//            }
//			}
        }

        return st.substring(1, st.length() - SEPERATOR_COMMA.length() - 1);
    }

    static String[] getValuesOfAllGetMethodsOfEntity(CoreEntity core, String methodNames[]) throws QException {
        try {
            String[] rs = new String[methodNames.length];

            /*first element of arg[] must contain a number of getMethods*/
            for (int i = 0; i < methodNames.length; i++) {
                Method method = core.getClass().getMethod(methodNames[i]);
                Object retObj = method.invoke(core);
                rs[i] = retObj.toString();
            }
            return rs;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    e);

        }
    }

    static String getValuesOfAllGetMethodsOfEntityInOneStringNEW(CoreEntity core, String arg[]) {

        String rs = "";

        /*first element of arg[] must contain a number of getMethods*/
        for (String arg1 : arg) {
            rs = rs + "?" + SEPERATOR_COMMA;
        }
        rs = rs.substring(0, rs.length() - 1);
        return rs;
    }

    static Object executeMethod(CoreEntity core, String methodName) throws QException {

        try {
            String rs = "";

            /*first element of arg[] must contain a number of getMethods*/
            Method method = core.getClass().getMethod(methodName);
            Object retObj = method.invoke(core);

            return retObj;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    ex);
        }
    }

    public static String getTableNameBasedOnEntity(CoreEntity core) {
        String mtSt[] = core.getClass().getName().split("\\.");
        String tableName = mtSt[mtSt.length - 1].substring(ENTITY.length());
        tableName = seperateTableFieldNameWithUnderscore(tableName).toUpperCase();
        tableName = !core.isFunction() ? tableName : convertTableNameToFunctionFormatWithParams(core, tableName);

        String dbname = core.selectDbname().trim().length() == 0
                ? SessionManager.getCurrentDomain() : core.selectDbname().trim();
        tableName = dbname + "." + tableName;
        return tableName;
    }

    private static String convertTableNameToFunctionFormatWithParams(CoreEntity core, String tableName) {
        String t = OPEN_BRACKET;
        int s = core.selectFunctionParamSize();
        for (int i = 0; i < s; i++) {
            t += "'" + core.selectFunctionParam(i) + "'";
            t += i == s - 1 ? "" : SEPERATOR_COMMA;
        }
        t += CLOSE_BRACKET;
        tableName += t;
        return tableName;
    }

    static String getTableNameBasedOnEntity(CoreEntity core, String databaseNumber) {
//        DBConfigurationProperties prop = new DBConfigurationProperties();
//        String hasSeperator = prop.getProperty(HAS_UNDERSCORE_IN_FIELD_NAME + 
//UNDERSCORE + capitalizeFirstLetter(databaseNumber));
        String mtSt[] = core.getClass().getName().split("\\.");
        String tableName = mtSt[mtSt.length - 1].substring(ENTITY.length());
        tableName = seperateTableFieldNameWithUnderscore(tableName).toUpperCase();

        String dbname = core.selectDbname().trim().length() == 0
                ? SessionManager.getCurrentDomain() : core.selectDbname().trim();
        tableName = dbname + "." + tableName;
        return tableName;
    }

    static String capitalizeFirstLetter(String arg) {
        arg = arg.substring(0, 1).toUpperCase() + arg.substring(1, arg.length()).toLowerCase();
        return arg;
    }

    static String lowerFirstLetter(String arg) {
        arg = arg.substring(0, 1).toLowerCase() + arg.substring(1, arg.length());
        return arg;
    }

    private static Method[] concat(Method[] a, Method[] b) {
        int aLen = a.length;
        int bLen = b.length;
        Method[] c = new Method[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static String[] getAllGetMethodNames(CoreEntity core) {
        int idx = 0;
        String[] ListOfGetMethods = new String[1001];
        Method m[] = core.getClass().getMethods();
        Method m1[] = CoreEntity.class
                .getMethods();

        for (Method m2 : m) {
            if (m2.toString().contains(core.getClass().getName() + SEPERATOR_DOT + GET)) {
                String[] S = m2.toString().split(" ");
                ListOfGetMethods[idx] = S[S.length - 1].substring(core.getClass().getName().length() + 1, S[S.length - 1].length() - 2);
                idx++;
            }
        }

        //select all GET methods from CoreEntity
        for (Method m11 : m1) {
            if (m11.toString().contains(CoreEntity.class
                    .getName() + SEPERATOR_DOT + GET)) {
                String[] S = m11.toString().split(" ");
                ListOfGetMethods[idx] = S[S.length - 1].substring(CoreEntity.class
                        .getName().length() + 1, S[S.length - 1].length() - 2);
                idx++;
            }
        }

        String[] ls = new String[idx];
        System.arraycopy(ListOfGetMethods, 0, ls, 0, idx);
        //ListOfGetMethods[0]=Integer.toString(idx-1);//fist element of ListOfGetMethods 
        //keep the number of GetMethod.Calling part of this method should start numberin
        //from 1.
        return ls;
    }

    public static String[] getAllAttributes(CoreEntity core) {
        int idx = 0;
        String[] ListOfGetMethods = new String[1001];
        Method m[] = core.getClass().getMethods();
        Method m1[] = CoreEntity.class
                .getMethods();

        for (Method m2 : m) {
            if (m2.toString().contains(core.getClass().getName() + SEPERATOR_DOT + GET)) {
                String[] S = m2.toString().split(" ");
                ListOfGetMethods[idx] = S[S.length - 1].substring(core.getClass().getName().length() + 1, S[S.length - 1].length() - 2);
                idx++;
            }
        }

        //select all GET methods from CoreEntity
        for (Method m11 : m1) {
            if (m11.toString().contains(CoreEntity.class
                    .getName() + SEPERATOR_DOT + GET)) {
                String[] S = m11.toString().split(" ");
                ListOfGetMethods[idx] = S[S.length - 1].substring(CoreEntity.class
                        .getName().length() + 1, S[S.length - 1].length() - 2);
                idx++;
            }
        }

        String[] ls = new String[idx];
        System.arraycopy(ListOfGetMethods, 0, ls, 0, idx);
        //ListOfGetMethods[0]=Integer.toString(idx-1);//fist element of ListOfGetMethods 
        //keep the number of GetMethod.Calling part of this method should start numberin
        //from 1.
        return ls;
    }

    public static String seperateTableFieldNameWithUnderscore(String arg) {
        String rs = arg.substring(0, 1);
        String lt = "";
        for (int i = 1; i < arg.length(); i++) {
            lt = arg.substring(i, i + 1);
            char lastCh = rs.charAt(rs.length() - 1);
            String lastChar = String.valueOf(lastCh);
            if (isNumeric(lt) && (!isNumeric(lastChar)) && (!lastChar.equals(UNDERSCORE))) {
                lt = UNDERSCORE + lt;
            } else if (isNumeric(lt) || (lt.equals(UNDERSCORE))) {
                lt = lt;
            } else if (lt.toUpperCase().equals(lt)) {
                lt = UNDERSCORE + lt;
            }

            rs = rs + lt;
        }
        return rs;
    }

    static String seperateTableFieldNameWithUnderscore(String arg, String databaseNumber) {
//        String propKeyName = HAS_UNDERSCORE_IN_FIELD_NAME + "." + databaseNumber.toLowerCase();
//        boolean hasSeperator = Config.getPropertyBool(propKeyName);
//        if (!hasSeperator) {
//            return arg;
//        } else {
        return addUnderscoreToFieldName(arg);
//        }
    }

    static String addUnderscoreToFieldName(String arg) {
        String rs = arg.substring(0, 1);
        String lt = "";
        for (int i = 1; i < arg.length(); i++) {
            lt = arg.substring(i, i + 1);
            char lastCh = rs.charAt(rs.length() - 1);
            String lastChar = String.valueOf(lastCh);
            if (isNumeric(lt) && (!isNumeric(lastChar)) && (!lastChar.equals(UNDERSCORE))) {
                lt = UNDERSCORE + lt;
            } else if (isNumeric(lt) || (lt.equals(UNDERSCORE))) {
                lt = lt;
            } else if (lt.toUpperCase().equals(lt)) {
                lt = UNDERSCORE + lt;
            }

            rs = rs + lt;
        }
        return rs;
    }

    static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    static String getTableFieldNameFromList(String arg[]) {
        String tbNames = "";
        /*first element of arg[] must contain a number of getMethods
         * */
        for (String arg1 : arg) {
            tbNames = tbNames + seperateTableFieldNameWithUnderscore(arg1.substring(GET.length())) + SEPERATOR_COMMA;
        }
        tbNames = tbNames.toUpperCase();
        tbNames = tbNames.substring(0, tbNames.length() - 1);

        return tbNames;
    }

    static String getTableFieldNameFromList(String arg[], String databaseNumber) {

        String tbNames = "";
        /*first element of arg[] must contain a number of getMethods
         * */
        for (String arg1 : arg) {
            tbNames = tbNames + seperateTableFieldNameWithUnderscore(arg1.substring(GET.length()), databaseNumber) + SEPERATOR_COMMA;
        }
        tbNames = tbNames.toUpperCase();
        tbNames = tbNames.substring(0, tbNames.length() - 1);

        return tbNames;
    }

    public static String selectGenerator(CoreEntity coreEnt, String databaseNumber, String[] methodNames, String[] values, ArrayList valueArr) throws QException {
        return selectGenerator(coreEnt, databaseNumber, methodNames, values, valueArr, true, false);
    }

    public static String selectGenerator(CoreEntity coreEnt, String databaseNumber, String[] methodNames,
            String[] values, ArrayList valueArr, boolean withLimit) throws QException {
        return selectGenerator(coreEnt, databaseNumber, methodNames, values, valueArr, withLimit, false);
    }

    public static String selectGenerator(CoreEntity coreEnt, String databaseNumber,
            String[] methodNames, String[] values, ArrayList valueArr,
            boolean withLimit, boolean addEntityName) throws QException {
        String selectSql = "";

        selectSql = "SELECT ";
        selectSql += coreEnt.hasDistinctFields() ? " DISTINCT " : "";

        selectSql += SPACE + generateSelectFieldPartOfSelectSql(coreEnt, databaseNumber, addEntityName);
        selectSql += SPACE + FROM;
        selectSql += SPACE + getTableNameBasedOnEntity(coreEnt);

        String wherePart = generateWherePartOfSelect(coreEnt, databaseNumber,
                methodNames, values, valueArr, withLimit, addEntityName);
        selectSql += wherePart;

        //add order by
        selectSql += coreEnt.hasSortBy() ? selectSortPartOfSelect(coreEnt) : " ORDER BY ID DESC ";

        //add limit 
        String slimit = coreEnt.selectStartLimit();
        String elimit = coreEnt.selectEndLimit();
        int idx = Integer.parseInt(elimit) - Integer.parseInt(slimit) + 1;
        selectSql += " LIMIT " + slimit + ", " + idx;
        return selectSql;
    }

    private static String get1stFieldOfEntityForDistinctSort(CoreEntity ent) {
        String res = "";
//        if (!ent.seIncludedField() && !ent.hasExcludedFields()){
//            res = "INSERT_DATE";
//        }
        return res;

    }

    private static String modifyKey(boolean addEntityNameToAS, String tablename, String key) {
        if (addEntityNameToAS) {
            return tablename + "." + key;
        } else {
            return key;
        }
    }

    public static String generateWherePartOfSelect(CoreEntity coreEnt, String databaseNumber, String[] methodNames, String[] values, ArrayList valueArr,
            boolean withLimit, boolean addEntityNameToAS) {
        String whereCondition = SPACE;
        String tablename = coreEnt.toDBTableName();
        String sumByField = coreEnt.selectSumBy().trim().equals("") ? "" : seperateTableFieldNameWithUnderscore(coreEnt.selectSumBy()).toUpperCase();;
        try {
            for (int i = 0; i < methodNames.length; i++) {
//                String vl = executeMethod(coreEnt, methodNames[i]).toString();
                if (!values[i].trim().isEmpty()) {
                    String val = values[i].trim();
                    String atrName = getAttributeNameFromMethodName(methodNames[i]);
                    String operation = EQUAL;
                    String key = seperateTableFieldNameWithUnderscore(atrName)
                            .toUpperCase();
                    String singleClause = "";
                    if (!key.trim().equals(sumByField)) {
                        singleClause = coreEnt.isDeepWhere()
                                && !val.contains(CoreLabel.IN)
                                ? new WhereSingle(key, val, valueArr).exec()
                                : getSingleClausOfWherePartOfSelect(tablename,
                                        key, val, valueArr, addEntityNameToAS);
                        singleClause = singleClause.trim().length() == 0
                                ? singleClause : singleClause + AND;
                        whereCondition = whereCondition + singleClause + SPACE;
                    }

                }
            }

            //add and statement
            if (coreEnt.hasAndStatement()) {
                String andStatement = getAndStatementOfWhereClause(coreEnt, valueArr);
                if (andStatement.length() > 0) {
                    whereCondition += andStatement;
                    whereCondition += SPACE + AND;
                }

            }

            //add andOr statement
            if (coreEnt.hasAndOrStatement()) {
                String andOrStatement = getAndOrStatementOfWhereClause(coreEnt, valueArr);
                if (andOrStatement.length() > 0) {
                    whereCondition += andOrStatement;
                    whereCondition += SPACE + AND;
                }

            }

            //add or statement
            if (coreEnt.hasOrStatement()) {
                String orStatement = getOrStatementOfWhereClause(coreEnt, valueArr);
                if (orStatement.length() > 0) {
                    whereCondition += orStatement;
                    whereCondition += SPACE + AND;
                }

            }

            //add deepWhere statement
            if (coreEnt.hasDeepWhereStatementField()) {
                int rc = coreEnt.selectDeepWhereStatementKeySize();
                for (int i = 0; i < rc; i++) {
                    String key = coreEnt.selectDeepWhereStatementKey(i);
                    String val = coreEnt.selectDeepWhereStatementValue(i);
                    key = addUnderscoreToFieldName(key);
                    String stmt = new WhereSingle(key, val, valueArr).exec();
                    if (stmt.length() > 0) {
                        whereCondition += stmt;
                        whereCondition += SPACE + AND;
                    }
                }
            }

            if (!whereCondition.trim().equals("")) {
                whereCondition = SPACE + WHERE + SPACE + whereCondition.substring(0, whereCondition.length() - AND.length() - 1);
            }
        } catch (Exception e) {
            new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    e);
        }
        return whereCondition;
    }

    public static String getAndOrStatementOfWhereClause(CoreEntity entity, ArrayList valueArr) {
        String res = "";
        entity.sortAndOrStatement();
        int rc = entity.selectAndOrstatementSize();
        boolean f = true;
        String oldKey = entity.selectAndOrStatementKey(0);
        if (rc > 0) {
            String ln = "";
            for (int i = 0; i < rc; i++) {
                String key = entity.selectAndOrStatementKey(i);
                key = seperateTableFieldNameWithUnderscore(key).toUpperCase();
                String val = entity.selectAndOrStatementValue(i);
                String tname = entity.toTableName();
                String singleClause = getSingleClausOfWherePartOfSelect(tname, key, val, valueArr);
                if (i + 1 == rc) {
                    String o = "";
                    if (!entity.selectAndOrStatementKey(i).equals(oldKey)) {
                        ln = ln.substring(0, ln.length() - AND.length()) + "  " + OR;
                    }
                    oldKey = "";
                    ln += SPACE + singleClause;
                    ln += rc > 2  ? AND: OR;

                }

                if (!entity.selectAndOrStatementKey(i).equals(oldKey)) {
                    res += "( " + ln.substring(0, ln.length() - AND.length()) + ") " + OR;
                    oldKey = entity.selectAndOrStatementKey(i);
                    f = false;
                    ln = "";
                }

                if (singleClause.trim().length() > 0) {
                    ln += SPACE + singleClause;
                    ln += rc > 2 ? AND : OR;
                }

            }

            if (f) {
                res = ln;
            }

            if (res.length() > 0) {
                res = "(" + SPACE + res;
                res = res.substring(0, res.length() - 3);
                res += ")" + SPACE;
            }

        }
        return res;
    }

    public static String getAndStatementOfWhereClause(CoreEntity entity, ArrayList valueArr) {
        String res = "";
        int rc = entity.selectAndStatementSize();
        if (rc > 0) {
            for (int i = 0; i < rc; i++) {
                String key = entity.selectAndStatementKey(i);
                key = seperateTableFieldNameWithUnderscore(key).toUpperCase();
                String val = entity.selectAndStatementValue(i);
                String tname = entity.toTableName();
                String singleClause = getSingleClausOfWherePartOfSelect(tname, key, val, valueArr);
                if (singleClause.trim().length() > 0) {
                    res += SPACE + singleClause + AND;
                }
            }

            if (res.length() > 0) {
                res = "(" + SPACE + res;
                res = res.substring(0, res.length() - AND.length());
                res += ")" + SPACE;
            }

        }
        return res;
    }

    public static String getOrStatementOfWhereClause(CoreEntity entity, ArrayList valueArr) {
        String res = "";
        int rc = entity.selectOrStatementSize();
        if (rc > 0) {
            for (int i = 0; i < rc; i++) {
                String key = entity.selectOrStatementKey(i);
                key = seperateTableFieldNameWithUnderscore(key).toUpperCase();
                String val = entity.selectOrStatementValue(i);
                String tname = entity.toTableName();
                String singleClause = getSingleClausOfWherePartOfSelect(tname, key, val, valueArr);
                if (singleClause.trim().length() > 0) {
                    res += SPACE + singleClause + OR;
                }
            }

            if (res.length() > 0) {
                res = "(" + SPACE + res;
                res = res.substring(0, res.length() - OR.length());
                res += ")" + SPACE;
            }

        }
        return res;
    }

    public static String getSingleClausOfWherePartOfSelect(String tablename, String key, String val, ArrayList valueArr) {
        return getSingleClausOfWherePartOfSelect(tablename, key, val, valueArr, false);
    }

    public static String getSingleClausOfWherePartOfSelect(String tablename, String key, String val, ArrayList valueArr, boolean addEntityNameToAS) {
        String res = "";
        String operVal = "?";
        String operation = "";
        if (val.startsWith(GT)) {
            val = val.replaceFirst(GT, "");
            if (val.trim().length() > 0) {
                operation = modifyKey(addEntityNameToAS, tablename, key) + SPACE + COMMAND_GT;
                valueArr.add(val);
            }
        } else if (val.startsWith(GE)) {
            val = val.replaceFirst(GE, "");
            if (val.trim().length() > 0) {
                operation = modifyKey(addEntityNameToAS, tablename, key) + SPACE + COMMAND_GE;
                valueArr.add(val);
            }
        } else if (val.startsWith(LE)) {
            val = val.replaceFirst(LE, "");
            if (val.trim().length() > 0) {
                operation = modifyKey(addEntityNameToAS, tablename, key) + SPACE + COMMAND_LE;
                valueArr.add(val);
            }
        } else if (val.startsWith(LT)) {
            val = val.replaceFirst(LT, "");
            if (val.trim().length() > 0) {
                operation = modifyKey(addEntityNameToAS, tablename, key) + SPACE + COMMAND_LT;
//            operation = modifyKey(addEntityNameToAS, tablename, key) + SPACE + COMMAND_LT;
                valueArr.add(val);
            }
        } else if (val.startsWith(NE)) {
            val = val.replaceFirst(NE, "");
            if (val.trim().length() > 0) {
                operation = modifyKey(addEntityNameToAS, tablename, key) + SPACE + COMMAND_NE;
                valueArr.add(val);
            }
        } else if (val.startsWith(FAIZ) || val.endsWith(FAIZ)) {
            operation = "LOWER(" + modifyKey(addEntityNameToAS, tablename, key.trim().toLowerCase()) + ")" + SPACE + COMMAND_LK;
            valueArr.add(val.replaceAll(FAIZ, "%"));
        } else if (val.contains(BN)) {
            String v[] = val.split(BN);
            try {
                if (v[0].trim().length() > 0 && v[1].trim().length() > 0) {
                    operation = modifyKey(addEntityNameToAS, tablename, key) + SPACE + COMMAND_BN;
                    operVal = QUESTION_MARK + SPACE + AND + SPACE + QUESTION_MARK;
                    valueArr.add(v[0].trim());
                    valueArr.add(v[1].trim());
                }
            } catch (Exception e1) {
                val = ""; //bu sorgu sifirlans;
            }

        } else if (val.contains(IN)) {
            try {
                operation = modifyKey(addEntityNameToAS, tablename, key) + SPACE + COMMAND_IN;
                operVal = OPEN_BRACKET;
                String v[] = val.split(IN);
                for (String v1 : v) {
                    if (v1.trim().length() > 0) {
                        operVal = operVal + QUESTION_MARK + SEPERATOR_COMMA;
                        valueArr.add(v1.trim());
                    }
                }
                if (operVal.trim().length() > 0) {
                    operVal = operVal.substring(0, operVal.length() - 1) + CLOSE_BRACKET;
                }
            } catch (Exception e1) {
                val = ""; //bu sorgu sifirlans;
            }
        }

        if (operation.trim().length() == 0) {
            if (val.trim().length() > 0) {
                operation = modifyKey(addEntityNameToAS, tablename, key) + SPACE + EQUAL;
                valueArr.add(val);
                res = operation + SPACE + operVal + SPACE;
            }
        } else if (val.trim().length() > 0) {
            res = operation + SPACE + operVal + SPACE;
        }

        return res;
    }

    public static String generateWherePartOfUpdate(CoreEntity coreEnt, String value, ArrayList valueArr) {
        String whereCondition = "";
        String tablename = coreEnt.toDBTableName();
        String operation = "";
        String key = "ID";
        String operVal;
        try {
            if (!value.trim().isEmpty()) {
                if (value.contains(IN)) {
                    operation = key + SPACE + COMMAND_IN;
                    operVal = OPEN_BRACKET;
                    String v[] = value.split(IN);
                    for (String v1 : v) {
                        operVal = operVal + QUESTION_MARK + SEPERATOR_COMMA;
                        valueArr.add(v1.trim());
                    }
                    operVal = operVal.substring(0, operVal.length() - 1) + CLOSE_BRACKET;
                    operation += operVal;
                } else {
                    operation = key + SPACE + EQUAL + QUESTION_MARK;
                    valueArr.add(value);
                }
            }
            if (!operation.trim().equals("")) {
                whereCondition = SPACE + WHERE + SPACE + operation;
            }
        } catch (Exception e) {
            new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    e);
        }
        return whereCondition;
    }

    private static String getAttributeNameFromMethodName(String methodname) {
        String fname = methodname.substring("GET".length(), methodname.length());
        fname = lowerFirstLetter(fname);
        return fname;
    }

    public static String selectSortPartOfSelect(CoreEntity ent) {
        String s = "";
        String[] st = ent.selectSort();
        String s1[] = ent.selectDistinctFields();

        for (String st1 : st) {
            if (st1.trim().length() > 0) {
                if ((s1.length == 0) || (s1.length > 0 && Arrays.asList(s1).contains(st1))) {
                    String sby = ent.selectSortByAsc() ? " ASC " : " DESC ";
                    String t = seperateTableFieldNameWithUnderscore(st1).toUpperCase() + sby;
                    s = s + t + SEPERATOR_COMMA;
                }
            }

        }
        s = s.substring(0, s.length() - 1);
        s = s.trim().length() == 0 ? " ORDER BY ID DESC" : "ORDER BY " + s;
        return s;
    }

    public static String selectByIdGenerator(String sqlId, String params[]) throws QException {
        try {
            String querySql = "";
            String tempTableName = "tempTableName";
            String tmpSqlQuery = generateSqlQueryById(sqlId);
            querySql = mergeSqlQueriesByRequiredSqlId(tempTableName, tmpSqlQuery, params);
            //Carrier qc = getSqlQeuryById(tempTableName,tmpSqlQuery);
            return querySql;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    ex);
        }
    }

    private static String mergeSqlQueriesByRequiredSqlId(String tableName, String coreSqlQuery, String params[])
            throws Exception {
//        Carrier qc = getSqlQeuryById(tableName, coreSqlQuery);
//        String st = generateMainSqlQueryBasedOnCarrier(tableName, qc);
//        String updateStr = replaceParamsInMainSql(st, params);
//        return updateStr;
        return null;
    }

    private static String generateMainSqlQueryBasedOnCarrier(String tableName, Carrier qc) throws Exception {
        String query = "";
        int cnt = qc.getTableRowCount(tableName);
        for (int i = 0; i < cnt; i++) {
            query += qc.getValue(tableName, i, FIELD_SQL.toLowerCase()).toString().trim();
        }
        return query;
    }

    private static String replaceParamsInMainSql(String mainSql, String params[]) throws Exception {
        String query = mainSql;
        for (int i = 0; i < params.length; i++) {
            query = query.replace(PATTERN_FOR_PARAMS + Integer.toString(i + 1), params[i]);
        }
        return query;
    }

    private static String generateSqlQueryById(String sqlId) throws QException {
        String query = SELECT + SPACE + STAR + SPACE + FROM + SPACE + TABLE_SQL_SCRIPT + SPACE + WHERE + SPACE + SPACE + FIELD_ID + EQUAL
                + QUOTE + sqlId.trim() + QUOTE + SPACE + ORDER_BY + SPACE + FIELD_PART;
        return query;
    }

    public static String selectByConditionGenerator(CoreEntity coreEnt) throws QException {
        String selectSql = SELECT + SPACE + STAR + SPACE + FROM + SPACE + getTableNameBasedOnEntity(coreEnt);
        try {
            String wCon = generateWherePartOfSelectMultipeSql(coreEnt);
            if (!wCon.trim().equals("")) {
                selectSql += SPACE + WHERE + SPACE + wCon;
            }
        } catch (Exception e) {
            throw new QException(e);
        }
        return selectSql;
    }

    private static String generateWherePartOfSelectMultipeSql(CoreEntity coreEnt) throws QException {
        String wCon = SPACE;
        String[] methodNames = getAllGetMethodNames(coreEnt);
        for (int i = 0; i < methodNames.length; i++) {
            try {
                String fieldName = seperateTableFieldNameWithUnderscore(methodNames[i].substring("GET".length(), methodNames[i].length())).toUpperCase();
                String value = executeMethod(coreEnt, methodNames[i]).toString().trim();
                String prefix = startPrefix(value);
                String pureValue = removePrefixFromTheValue(value);
                if (!pureValue.trim().isEmpty()) {
                    wCon = wCon + generateWhereCondByStartPrefix(fieldName, prefix, pureValue.trim()) + SPACE + AND + SPACE;
                }
            } catch (QException ex) {
                throw new QException(new Object() {
                }.getClass().getEnclosingClass().getName(),
                        new Object() {
                }.getClass().getEnclosingMethod().getName(),
                        ex);
            }
        }
        return wCon.substring(1, wCon.length() - AND.length() - 1);
    }

    private static String generateWherePartOfUpdateSql(CoreEntity coreEnt) throws QException {
        String wCon = SPACE;
        String[] methodNames = getAllGetMethodNames(coreEnt);
        for (String methodName : methodNames) {
            try {
                String fieldName = seperateTableFieldNameWithUnderscore(methodName.substring("GET".length(), methodName.length())).toUpperCase();
                String value = executeMethod(coreEnt, methodName).toString().trim();
                String prefix = startPrefix(value);
                String pureValue = removePrefixFromTheValue(value);
                if (!pureValue.trim().isEmpty()) {
                    wCon = wCon + generateWhereCondByStartPrefix(fieldName, prefix, pureValue.trim()) + SPACE + AND + SPACE;
                }
            } catch (QException ex) {
                throw new QException(new Object() {
                }.getClass().getEnclosingClass().getName(),
                        new Object() {
                }.getClass().getEnclosingMethod().getName(),
                        ex);
            }
        }
        return wCon.substring(1, wCon.length() - AND.length() - 1);
    }

    private static String removePrefixFromTheValue(String value) {
        String prefix = startPrefix(value);
        String pureValue = value;
        if (!prefix.trim().equals("")) {
            pureValue = value.substring(3, value.length());
        }//bizim sertimize goze valuenin ilk uc caracter'i prefix olacaq.pureValue 
        //hissesi ise 3-cu karakterden sonra olan hisse nezerde tutulacaq;
        return pureValue;
    }

    private static String splitValueBy2Dots(String value, String prefix) {
        String st = "";
        if (!prefix.equals("")) {
            st = value.trim().split(TWO_DOTS)[1];
        } else {
            st = value;
        }
        return st.trim();
    }

    private static String[] splitValueBy2DotsForBetweenCommand(String value) {
        String[] st = value.trim().split(TWO_DOTS);
        //lse{st = value;}
        return st;
    }

    public static String startPrefix(String arg) {
        String st = "";
        if (arg.startsWith(NE + TWO_DOTS)) {
            st = NE;
        } else if (arg.startsWith(LT + TWO_DOTS)) {
            st = LT;
        } else if (arg.startsWith(GT + TWO_DOTS)) {
            st = GT;
        } else if (arg.startsWith(GE + TWO_DOTS)) {
            st = GE;
        } else if (arg.startsWith(LE + TWO_DOTS)) {
            st = LE;
        } else if (arg.startsWith(BN + TWO_DOTS)) {
            st = BN;
        } else if (arg.startsWith(LK + TWO_DOTS)) {
            st = LK;
        } else if (arg.startsWith(IN + TWO_DOTS)) {
            st = IN;
        }

        return st;
    }

    public static String generateWhereCondByStartPrefix(String fieldName, String prefix, String value) {
        String rs = "";
        //String vl = splitValueBy2Dots(value.trim(),prefix).trim();
        String vl = value;
        if (prefix.equals(NE)) {
            rs = fieldName + " " + COMMAND_NE + " " + QUOTE + vl + QUOTE;
        } else if (prefix.equals(GT) && (!vl.equals(""))) {
            rs = fieldName + " " + COMMAND_GT + " " + QUOTE + vl + QUOTE;
        } else if (prefix.equals(GE) && (!vl.equals(""))) {
            rs = fieldName + " " + COMMAND_GE + " " + QUOTE + vl + QUOTE;
        } else if (prefix.equals(LT) && (!vl.equals(""))) {
            rs = fieldName + " " + COMMAND_LT + " " + QUOTE + vl + QUOTE;
        } else if (prefix.equals(LE) && (!vl.equals(""))) {
            rs = fieldName + " " + COMMAND_LE + " " + QUOTE + vl + QUOTE;
        } else if (prefix.equals(LK) && (!vl.equals(""))) {
            rs = fieldName + " " + COMMAND_LK + " " + QUOTE + vl + QUOTE;
        } else if (prefix.equals(BN)) {
            String st[] = splitValueBy2DotsForBetweenCommand(value);
            rs = fieldName + " " + COMMAND_BN + " " + concatStringArrayForBetweenStatement(st);
        } else if (prefix.equals(IN)) {
            String st[] = splitValueBy2DotsForBetweenCommand(value);
            if (st.length > 0) {
                rs = fieldName + " " + COMMAND_IN + " " + OPEN_BRACKET + concatStringArrayForInStatement(st) + CLOSE_BRACKET + SPACE;
            }
        } else {
            rs = fieldName + " " + EQUAL + " " + QUOTE + value.trim() + QUOTE;
        }

        return rs;
    }

    private static String concatStringArrayForInStatement(String arg[]) {
        String st = "";
        for (String arg1 : arg) {
            st = st + QUOTE + arg1 + QUOTE + ",";
        }
        return st.substring(0, st.length() - 1);//-1 : silinmesine goredir
    }

    private static String concatStringArrayForBetweenStatement(String arg[]) {
        String st = "";
        if (arg.length >= 2) {//for between statement we need 2 values;
            for (int i = 1; i < arg.length; i++) {
                st = st + QUOTE + arg[0] + QUOTE
                        + SPACE + AND + SPACE + QUOTE + arg[1] + QUOTE + " ";
            }
        } else if (arg.length == 1) {
            st = st + QUOTE + arg[0] + QUOTE
                    + SPACE + AND + SPACE + QUOTE + "" + QUOTE + " ";
        } else {
            st = st + QUOTE + "" + QUOTE
                    + SPACE + AND + SPACE + QUOTE + "" + QUOTE + " ";
        }

        return st.substring(0, st.length() - 1);//-1 : silinmesine goredir
    }

    private static boolean isStartsWithPrefix(String arg) {

        return true;
    }

}
