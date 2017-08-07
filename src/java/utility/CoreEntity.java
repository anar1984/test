package utility;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import label.CoreLabel;
import utility.sqlgenerator.EntityManager;
import utility.sqlgenerator.SQLGenerator;
import static utility.sqlgenerator.SQLGenerator.AND;
import static utility.sqlgenerator.SQLGenerator.SPACE;

public abstract class CoreEntity {

    public String ENTITY = "ENTITY";
    public static String START_LIMIT = "startLimit";
    public static String END_LIMIT = "endLimit";
    public static String SUM_BY = "sumBy";
    public static String GROUP_BY = "groupBy";
    public static String INTERVAL_START_DATE = "intervalStartDate";
    public static String INTERVAL_END_DATE = "intervalEndDate";
    public static String INTERVAL_FIELD = "intervalField";
    public static String INTERVAL_SEPERATOR = "intervalSeperator";
    

    private String id = "";
    private String status = "A";
    private String insertDate = "";
    private String modificationDate = "";
    private String startLimit = "0";
    private String endLimit = "10000";
    private String intervalField = "";
    private String intervalStartDate = "";
    private String intervalEndDate = "";
    private String intervalSeperator = "";
    private String sumBy = "";
    private final ArrayList sortBy;
    private final ArrayList excludedFileds;
    private final ArrayList inlcudedFileds;
    private final ArrayList distinctFileds;
    private final ArrayList groupBy;
    private final ArrayList orStatementKey;
    private final ArrayList orStatementValue;

    private final ArrayList deepWhereStatementKey;
    private final ArrayList deepWhereStatementValue;
    private final ArrayList andStatementKey;
    private final ArrayList andStatementValue;
    private final ArrayList andOrStatementKey;
    private final ArrayList andOrStatementValue;
    private final ArrayList functionParams;
    private final ArrayList<String> andStatementField;
    private final ArrayList<String> andOrStatementField;
    private final ArrayList<String> orStatementField;
    private final ArrayList<String> deepWhereStatementField;
    private boolean sortByAsc;
    private boolean setAndStatAll;
    private final ArrayList concatinateKey;
    private final ArrayList concatinateValue;
    private final String CONCATINATE_SPLITER = "::";
    private PreparedStatement statement;
    private Connection connection;
    private boolean deepWhere;
    private String dbname;

    public CoreEntity() {
        this.sortByAsc = false;
        sortBy = new ArrayList();
        excludedFileds = new ArrayList();
        inlcudedFileds = new ArrayList();
        distinctFileds = new ArrayList();
        concatinateKey = new ArrayList();
        concatinateValue = new ArrayList();
        groupBy = new ArrayList();
        orStatementKey = new ArrayList();
        orStatementValue = new ArrayList();

        deepWhereStatementKey = new ArrayList();
        deepWhereStatementValue = new ArrayList();
        andStatementKey = new ArrayList();
        andStatementValue = new ArrayList();
        andOrStatementKey = new ArrayList();
        andOrStatementValue = new ArrayList();
        functionParams = new ArrayList();
        andStatementField = new ArrayList<>();
        andOrStatementField = new ArrayList<>();
        orStatementField = new ArrayList<>();
        deepWhereStatementField = new ArrayList<>();
        this.connection = null;
        setAndStatAll = false;
        deepWhere = true;
        dbname="";
    }

    public String selectDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    
    
    public boolean isDeepWhere() {
        return deepWhere;
    }

    public void setDeepWhere(boolean deepWhere) {
        this.deepWhere = deepWhere;
    }

    public void setAndStatement4All(boolean arg) {
        this.setAndStatAll = arg;
    }

    public void addAndStatementField(String field) {
        andStatementField.add(field);
    }

    public void addOrStatementField(String field) {
        orStatementField.add(field);
    }

    public void addDeepWhereStatementField(String field) {
        deepWhereStatementField.add(field);
    }

    public void addAndOrStatementField(String field) {
        andOrStatementField.add(field);
    }

    public boolean hasAndStatementField() {
        if (this.setAndStatAll) {
            String ents[] = selectAllAttribute(this);
            for (String ent : ents) {
                addAndStatementField(ent);
            }
        }
        return andStatementField.size() > 0;
    }

    public boolean hasOrStatementField() {
        return orStatementField.size() > 0;
    }

    public boolean hasDeepWhereStatementField() {
        return deepWhereStatementField.size() > 0;
    }

    public int selectDeepWhereStatementFieldSize() {
        return deepWhereStatementField.size();
    }

    public int selectDeepWhereStatementKeySize() {
        return deepWhereStatementKey.size();
    }


    public String selectDeepWhereStatementFieldName(int key) {
        return deepWhereStatementField.get(key);
    }

    public boolean hasAndOrStatementField() {
        return andOrStatementField.size() > 0;
    }

    public static String[] selectAllGetMethodNames(CoreEntity core) {
        int idx = 0;
        String[] ListOfGetMethods = new String[1001];
        Method m[] = core.getClass().getMethods();
        Method m1[] = CoreEntity.class
                .getMethods();

        for (Method m2 : m) {
            if (m2.toString().contains(core.getClass().getName() + "." + "get")) {
                String[] S = m2.toString().split(" ");
                ListOfGetMethods[idx] = S[S.length - 1].substring(core.getClass().getName().length() + 1, S[S.length - 1].length() - 2);
                idx++;
            }
        }

        //select all GET methods from CoreEntity
        for (Method m11 : m1) {
            if (m11.toString().contains(CoreEntity.class
                    .getName() + "." + "get")) {
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

    public static String[] selectAllAttribute(CoreEntity core) {
        int idx = 0;
        String[] ListOfGetMethods = new String[1001];
        Method m[] = core.getClass().getMethods();
        Method m1[] = CoreEntity.class
                .getMethods();

        for (Method m2 : m) {
            if (m2.toString().contains(core.getClass().getName() + "." + "get")) {
                String[] S = m2.toString().split(" ");
                ListOfGetMethods[idx] = S[S.length - 1].substring(core.getClass().getName().length() + 4, S[S.length - 1].length() - 2);
                idx++;
            }
        }

        //select all GET methods from CoreEntity
        for (Method m11 : m1) {
            if (m11.toString().contains(CoreEntity.class
                    .getName() + "." + "get")) {
                String[] S = m11.toString().split(" ");
                ListOfGetMethods[idx] = S[S.length - 1].substring(CoreEntity.class
                        .getName().length() + 4, S[S.length - 1].length() - 2);
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

    public void splitAndStatementField() throws QException {
        if (!hasAndStatementField()) {
            return;
        }

        for (String andStatementField1 : andStatementField) {
            convertFieldIntoAndStatement(andStatementField1);
        }
    }

    public void splitAndOrStatementField() throws QException {
        if (!hasAndOrStatementField()) {
            return;
        }

        for (String andOrStatementField1 : andOrStatementField) {
            convertFieldIntoAndOrStatement(andOrStatementField1);
        }
    }

    public void splitOrStatementField() throws QException {
        if (!hasOrStatementField()) {
            return;
        }

        for (String orStatementField1 : orStatementField) {
            convertFieldIntoOrStatement(orStatementField1);
        }
    }

    public void splitDeepWhereStatementField() throws QException {
        if (!hasDeepWhereStatementField()) {
            return;
        }

        int rc = this.selectDeepWhereStatementFieldSize();
        for (int i = 0; i < rc; i++) {
            String key = this.selectDeepWhereStatementFieldName(i);
            String val = this.selectEntityValue(key);
            if (val.contains(CoreLabel.IN)) {
                String[] v1 = val.split(CoreLabel.IN);
                for (String v : v1) {
                    this.deepWhereStatementKey.add(key);
                    this.deepWhereStatementValue.add(v);
                }
                this.setEntityValue(key, "");
            } else {
            this.deepWhereStatementKey.add(key);
            this.deepWhereStatementValue.add(val);
            this.setEntityValue(key, "");
        }
    }
    }

    public String selectDeepWhereStatementKey(int key) throws QException {
        return (String) this.deepWhereStatementKey.get(key);
    }

    public String selectDeepWhereStatementValue(int key) throws QException {
        return (String) this.deepWhereStatementValue.get(key);
    }

    public void sortAndOrStatement() {
        Collections.sort(andOrStatementKey);
    }

    public String selectEntityValue(CoreEntity entity, String keyName) throws QException {
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

    public String selectEntityValue(String keyName) throws QException {
        return selectEntityValue(this, keyName);
    }

    public void setEntityValue(CoreEntity entity, String keyName, String keyValue) throws QException {
        try {
            String rs = "";
            String methodName = "set" + capitalizeOnlyFirstLetter(keyName);
            Method method = entity.getClass().getMethod(methodName, String.class);
            Object retObj = method.invoke(entity, keyValue);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public void setEntityValue(String keyName, String keyValue) throws QException {
        setEntityValue(this, keyName, keyValue);
    }

    String capitalizeOnlyFirstLetter(String arg) {
        arg = arg.substring(0, 1).toUpperCase() + arg.substring(1, arg.length());
        return arg;
    }

    private void convertFieldIntoAndStatement(String fieldName) throws QException {
        String value = selectEntityValue(this, fieldName);

        if (value.trim().length() == 0) {
            return;
        }

        //eger %IN% deyeri olmamalidir
        if (value.trim().contains(CoreLabel.IN)) {
            return;
        }

        value = value.replace(CoreLabel.FAIZ, "");
        String[] subValue = value.trim().split(" ");

        for (String n : subValue) {
            if (n.trim().length() != 0) {
                String tvalue = CoreLabel.FAIZ + n + CoreLabel.FAIZ;
                this.addAndStatement(fieldName, tvalue);
            }
        }
        setEntityValue(this, fieldName, "");
    }

    private void convertFieldIntoAndOrStatement(String fieldName) throws QException {
        String value = selectEntityValue(this, fieldName);

        if (value.trim().length() == 0) {
            return;
        }

        //eger %IN% deyeri olmamalidir
        if (value.trim().contains(CoreLabel.IN)) {
            return;
        }

        value = value.replace(CoreLabel.FAIZ, "");
        String[] subValue = value.trim().split(" ");

        for (String n : subValue) {
            if (n.trim().length() != 0) {
                String tvalue = CoreLabel.FAIZ + n + CoreLabel.FAIZ;
                this.addAndOrStatement(fieldName, tvalue);
            }
        }
        setEntityValue(this, fieldName, "");
    }

    private void convertFieldIntoOrStatement(String fieldName) throws QException {
        String value = selectEntityValue(this, fieldName);

        if (value.trim().length() == 0) {
            return;
        }

        //eger %IN% deyeri olmamalidir
        if (value.trim().contains(CoreLabel.IN)) {
            return;
        }

        value = value.replace(CoreLabel.FAIZ, "");
        String[] subValue = value.trim().split(" ");

        for (String n : subValue) {
            if (n.trim().length() != 0) {
                String tvalue = CoreLabel.FAIZ + n + CoreLabel.FAIZ;
                this.addOrStatement(fieldName, tvalue);
            }
        }
        setEntityValue(this, fieldName, "");
    }

    public void addOrStatement(String key, String value) {
        orStatementKey.add(key);
        orStatementValue.add(value);
    }

    public void addAndStatement(String key, String value) {
        andStatementKey.add(key);
        andStatementValue.add(value);
    }

    public void addAndOrStatement(String key, String value) {
        andOrStatementKey.add(key);
        andOrStatementValue.add(value);
    }

    public int selectOrStatementSize() {
        return orStatementKey.size();
    }

    public int selectAndStatementSize() {
        return andStatementKey.size();
    }

    public int selectAndOrstatementSize() {
        return andOrStatementKey.size();
    }

    public void addFunctionParams(String param) {
        functionParams.add(param);
    }

    public int selectFunctionParamSize() {
        return functionParams.size();
    }

    public String selectFunctionParam(int index) {
        return functionParams.get(index).toString();
    }

    public void addFunctionParams(String param[]) {
        for (String p : param) {
            functionParams.add(param);
        }
    }

    public boolean isFunction() {
        return functionParams.size() > 0;
    }

    public String selectOrStatementKey(int index) {
        return orStatementKey.get(index).toString();
    }

    public String selectOrStatementValue(int index) {
        return orStatementValue.get(index).toString();
    }

    public boolean hasOrStatement() {
        return orStatementKey.size() > 0;
    }

    public String selectAndStatementKey(int index) {
        return andStatementKey.get(index).toString();
    }

    public String selectAndStatementValue(int index) {
        return andStatementValue.get(index).toString();
    }

    public boolean hasAndStatement() {
        return andStatementKey.size() > 0;
    }

    public String selectAndOrStatementKey(int index) {
        return andOrStatementKey.get(index).toString();
    }

    public String selectAndOrStatementValue(int index) {
        return andOrStatementValue.get(index).toString();
    }

    public boolean hasAndOrStatement() {
        return andOrStatementKey.size() > 0;
    }

    public Connection selectConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement selectStatement() {
        return statement;
    }

    public void setStatement(PreparedStatement statement) {
        this.statement = statement;
    }

    public boolean hasStatement() {
        boolean f = this.statement != null;
        return f;
    }

    public boolean hasConnection() {
        boolean f = this.connection != null;
        return f;
    }

    public void addConcatinateWhere(String[] key, String value) {
        String ln = "";
        for (String keys : key) {
            ln += keys.trim() + CONCATINATE_SPLITER;
        }
        ln = ln.substring(0, ln.length() - CONCATINATE_SPLITER.length());
        concatinateKey.add(ln);
        concatinateValue.add(value.trim());
    }

    public void setIntervalField(String intervalField) {
        this.intervalField = intervalField;
    }

    public void setIntervalStartDate(String intervalStartDate) {
        this.intervalStartDate = intervalStartDate;
    }

    public void setIntervalEndDate(String intervalEndDate) {
        this.intervalEndDate = intervalEndDate;
    }

    public void setIntervalSeperator(String intervalSeperator) {
        this.intervalSeperator = intervalSeperator;
    }

    public void setSumBy(String sumBy) {
        this.sumBy = sumBy;
    }

    public String selectSumBy() {
        return this.sumBy;
    }

    public String selectIntervalField() {
        return intervalField;
    }

    public String selectIntervalStartDate() {
        return intervalStartDate;
    }

    public String selectIntervalEndDate() {
        return intervalEndDate;
    }

    public String selectIntervalSeperator() {
        return intervalSeperator;
    }

    public boolean hasConcatinate() {
        return !concatinateKey.isEmpty();
    }

    public int selectConcatinateSize() {
        return concatinateKey.size();
    }

    public String[] selectConcatinateKeys(int index) {
        return concatinateKey.get(index).toString().split(CONCATINATE_SPLITER);
    }

    public String selectConcatinateValue(int index) {
        return concatinateKey.get(index).toString().trim();
    }

    public void setSortByAsc(boolean arg) {
        this.sortByAsc = arg;
    }

    public boolean selectSortByAsc() {
        return this.sortByAsc;
    }

    public String selectSortByAscValue() {
        if (this.sortByAsc) {
            return "asc";
        } else {
            return "desc";
        }
    }

    public void addSortBy(String sort) {
        if (!sort.trim().equals("") && !sortBy.contains(sort)) {
            sortBy.add(sort);
        }
    }

    public void addGroupBy(String groupByField) {
//        System.out.println("group by deyeri "+groupByField);
        if (!groupByField.trim().equals("") && !groupBy.contains(groupByField)) {

            groupBy.add(groupByField);
//            System.out.println("group by deyeri  elave edildi"+groupByField);

        }
    }

    public void addGroupBy(String[] groupByFields) {
        for (String groupByField : groupByFields) {
            addGroupBy(groupByField);
        }
    }

    public String selectGroupByString() {
        String s = "";
        String[] res = new String[groupBy.size()];
        for (int i = 0; i < res.length; i++) {
            s = s + groupBy.get(i).toString() + ", ";
        }
        return s;
    }

    public String[] selectGroupBy() {
        String[] res = new String[groupBy.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = groupBy.get(i).toString();
        }
        return res;
    }

    public boolean hasGroupBy() {
        return 0 < groupBy.size();
    }

    public boolean hasSumBy() {
        return !sumBy.trim().equals("");
    }

    public boolean hasSortBy() {
        return sortBy.size() > 0;
    }

    public boolean hasIntervalField() {
        boolean f = true;
        if (intervalEndDate.equals("") || intervalField.equals("")
                || intervalSeperator.equals("") || intervalStartDate.equals("")) {
            f = false;
        }
        return f;
    }

    public void addSortBy(String[] sorts) {
        for (String sort : sorts) {
            addSortBy(sort);
        }
    }

    public String[] selectSort() {
        String[] res = new String[sortBy.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = sortBy.get(i).toString();
        }
        return res;
    }

    public String selectSortString() {
        String s = "";
        String[] res = new String[sortBy.size()];
        for (int i = 0; i < res.length; i++) {
            s = s + sortBy.get(i).toString() + ", ";
        }
        s = s.substring(0, s.length() - 2);
        return s;
    }

    public void addIncludedField(String arg) {
        if (!arg.trim().equals("") && !this.inlcudedFileds.contains(arg)) {
            this.inlcudedFileds.add(arg);
        }
    }

    public void addIncludedField(String[] arg) {
        for (String arg1 : arg) {
            addIncludedField(arg1);
        }
    }

    public String[] selectIncludedFields() {
        String[] res = new String[inlcudedFileds.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = inlcudedFileds.get(i).toString();
        }
        return res;
    }

    public String selectInlcudedFiledsString() {
        String s = "";
        String[] res = new String[inlcudedFileds.size()];
        for (int i = 0; i < res.length; i++) {
            s = s + inlcudedFileds.get(i).toString() + ", ";
        }
        return s;
    }

    public boolean hasIncludedField() {
        return !this.inlcudedFileds.isEmpty();
    }

    public boolean hasExcludedFieldName(String fieldname) {
        return excludedFileds.contains(fieldname);
    }

    public void addDistinctField(String arg) {
        if (!arg.trim().equals("") && !distinctFileds.contains(arg)) {
            this.distinctFileds.add(arg);
        }
    }

    public void addDistinctField(String[] arg) {
        for (String arg1 : arg) {
            addDistinctField(arg1);
        }
    }

    public String[] selectDistinctFields() {
        String[] res = new String[distinctFileds.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = distinctFileds.get(i).toString();
        }
        return res;
    }

    public String selectDistinctFiledsString() {
        String s = "";
        String[] res = new String[distinctFileds.size()];
        for (int i = 0; i < res.length; i++) {
            s = s + distinctFileds.get(i).toString() + ", ";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

    public boolean hasDistinctFields() {
        return !this.distinctFileds.isEmpty();
    }

    public boolean hasDistictFieldName(String fieldname) {
        return distinctFileds.contains(fieldname);
    }

    public boolean hasExcludedFields() {
        return !this.excludedFileds.isEmpty();
    }

    public void addExcludedField(String arg) {
        if (!arg.trim().equals("") && !excludedFileds.contains(arg)) {
            this.excludedFileds.add(arg);
        }
    }

    public void addExcludedField(String[] arg) {
        for (String arg1 : arg) {
            addExcludedField(arg1);
        }
    }

    public String[] selectExcludedFields() {
        String[] res = new String[excludedFileds.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = excludedFileds.get(i).toString();
        }
        return res;
    }

    public String selectExlcudedFiledsString() {
        String s = "";
        String[] res = new String[excludedFileds.size()];
        for (int i = 0; i < res.length; i++) {
            s = s + excludedFileds.get(i).toString() + ", ";
        }
        return s;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String selectStartLimit() {
        if (this.startLimit.equals("")) {
            this.startLimit = "0";
        }
        return startLimit;
    }

    public void setStartLimit(String startLimit) {
        this.startLimit = startLimit;
    }

    public void setStartLimit(int startLimit) {
        this.setStartLimit(String.valueOf(startLimit));
    }

    public String selectEndLimit() throws QException {
        try {
            if (this.endLimit.equals("")) {
                DBConfigurationProperties prop = new DBConfigurationProperties();
                this.endLimit = prop.getProperty(CoreLabel.DEFAULT_SELECT_END_LIMIT);
            }
        } catch (UnsupportedEncodingException ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    ex);
        }
        return endLimit;
    }

    public void setEndLimit(String endLimit) {
        this.endLimit = endLimit;
    }

    public void setEndLimit(int endLimit) {
        this.setEndLimit(String.valueOf(endLimit));
    }

    public String convertEntityNameToTableName(CoreEntity core) {
        String mtSt[] = core.getClass().getName().split("\\.");
        String tableName = mtSt[mtSt.length - 1];
        tableName = tableName.substring(ENTITY.length(), ENTITY.length() + 1).toLowerCase() + tableName.substring(ENTITY.length() + 1);
        return tableName;
    }

    public String toTableName() {
        String s = this.getClass().getSimpleName();
        s = s.substring(ENTITY.length(), s.length());
        s = s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
        return s;
    }

    public String toDBTableName() {
        return SQLGenerator.getTableNameBasedOnEntity(this);
    }

    public void fromCarrier(Carrier carrier) throws QException {
        try {
            EntityManager.mapCarrierToEntity(carrier, this);
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    ex);
        }
    }

    public void fromCarrier(Carrier carrier, boolean setAll) throws QException {
        try {
            EntityManager.mapCarrierToEntity(carrier, this, setAll);
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    ex);
        }
    }

    public void fromCarrier(Carrier carrier, String tablename, boolean setAll) throws QException {
        try {
            EntityManager.mapCarrierToEntity(carrier, tablename, this, setAll);
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(),
                    ex);
        }
    }

    public String selectLongEntityFieldName(String shortEntityName) {
//        return this.toTableName() + shortEntityName.substring(0, 1).toUpperCase()
//                + shortEntityName.substring(1, shortEntityName.length());
        return shortEntityName;
    }

}
