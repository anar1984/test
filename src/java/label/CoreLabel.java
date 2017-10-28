package label;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 02483577
 */
public class CoreLabel {
//    LabelEngine LabelEngine = new LabelEngine();
//    LabelOperation LabelOperation = new LabelOperation();
//    LabelDBConfig LabelProperties = new LabelDBConfig();
//    LabelResourceConfig LabelResourceConfig = new LabelResourceConfig();
//    LabelResourceQuery LabelResourceQuery = new LabelResourceQuery();
//    LabelSqlQueryIds LabelSqlQueryId = new LabelSqlQueryIds();

    /////////////////////////////////DB Config
    //public static String SQL_QUERY_POOL_DB_NUMBER = "sqlQueryPoolDBNumber";
    public static String MESSAGE_POOL_DB_NUMBER = "messagePoolDBNumber";
    //public static String DEFAULT_SELECT_END_LIMIT = "defaultSelectEndLimit";
    //public static String DEFAULT_SELECT_START_LIMIT = "defaultSelectStartLimit";

    public static String DB_PRIMARY = "primary";
    public static String DB_SLAVE = "slave";
    public static String DB_THIRD = "third";
    public static String DB_FORTH = "forth";
    public static String DB_FIFTH = "fifth";

    public static String DBTYPE_PRIMARY = "dbTypePrimary";
    public static String USERNAME_PRIMARY = "userNamePrimary";
    public static String PASSWORD_PRIMARY = "passwordPrimary";
    public static String URL_PRIMARY = "urlPrimary";
    public static String DRIVER_PRIMARY = "driverPrimary";
    public static String HAS_UNDERSCORE_IN_TABLE_NAME_PRIMARY = "hasUnderscoreInTableNamePrimary";
    public static String HAS_UNDERSCORE_IN_FIELD_NAME_PRIMARY = "hasUnderscoreInFieldNamePrimary";
    public static String DELETE_STATUS_PRIMARY = "deleteStatusPrimary";

    public static String DBTYPE_SLAVE = "dbTypeSlave";
    public static String USERNAME_SLAVE = "userNameSlave";
    public static String PASSWORD_SLAVE = "passwordSlave";
    public static String URL_SLAVE = "urlSlave";
    public static String DRIVER_SLAVE = "driverSlave";
    public static String HAS_UNDERSCORE_IN_TABLE_NAME_SLAVE = "hasUnderscoreInTableNameSlave";
    public static String HAS_UNDERSCORE_IN_FIELD_NAME_SLAVE = "hasUnderscoreInFieldNameSlave";
    public static String DELETE_STATUS_SLAVE = "deleteStatusSlave";

    public static String DBTYPE_THIRD = "dbTypeThird";
    public static String USERNAME_THIRD = "userNameThird";
    public static String PASSWORD_THIRD = "passwordThird";
    public static String URL_THIRD = "urlThird";
    public static String DRIVER_THIRD = "driverThird";

    public static String DBTYPE_FORTH = "dbTypeForth";
    public static String USERNAME_FORTH = "userNameForth";
    public static String PASSWORD_FORTH = "passwordForth";
    public static String URL_FORTH = "urlForth";
    public static String DRIVER_FORTH = "driverForth";

    public static String DBTYPE_FIFTH = "dbTypeFifth";
    public static String USERNAME_FIFTH = "userNameFifth";
    public static String PASSWORD_FIFTH = "passwordFifth";
    public static String URL_FIFTH = "urlFifth";
    public static String DRIVER_FIFTH = "driverFifth";

    //////////end DB Config
    ////////////module
    public static String MODULE_SALE = "SL";

    ////////////end 
    public static String RESOURCE_PATH = "resourcePath";

    ////////////////////////////web services
    public static String RESULT_SET = "Response";
    public static String RESULT_SET_COLUMN = "c";
    public static String JSON_TABLE_FIELD_ID = "i";
    public static String JSON_TABLE_FIELD_NAME = "n";
    public static String JSON_TABLE_FIELD_TYPE = "t";
    public static String JSON_TABLE_FIELD_TABLE_NAME = "tn"; 
    public static String JSON_TABLE_RESULT_TABLE_NAME = "r";
    public static String JSON_ERROR_TABLE_RESULT_TABLE_NAME = "err";
    public static String JSON_TABLE_COMMON_RESULT_TABLE_NAME = "tbl";
    public static String RESULT_SET_R = "res";
    public static String INPUT_TABLE = "b";
    public static String KEY_VALUE_TABLE = "kv";
    public static String START_LIMIT = "startLimit";
    public static String END_LIMIT = "endLimit";
    public static String SORT_TABLE = "sortTable";
    public static String DISTINCT_FIELDS = "distinctFields";
    public static String SORT_TABLE_TYPE = "sortTableType";
    public static String INCLUDED_FIELDS = "includedFields";
    public static String EXCLUDED_FIELDS = "excludedFields";
    public static String WS_EXCLUDED_FIELDS_SEPERATOR = ",";
    public static String WS_INCLUDED_FIELDS_SEPERATOR = ",";
    public static String WS_SORT_TABLE_TYPE_ASC = "asc";
    public static String WS_SORT_TABLE_TYPE_DESC = "desc";  
    public static String WS_SORT_TABLE_SEPERATOR = ",";
    public static String TABLE_ROW_COUNT = "tableRowCount";
    public static String TABLE_FIELD_TITLE = "fieldTitleTable";
    public static String SEPERATOR_VERTICAL_LINE= "\\|";

    //////////////////////////end web service
    public static String YES = "YES";
    public static String NO = "NO";
    public static String STATUS = "STATUS";


    public static String REPOSITORY_FILE_INSERT_SQL = "insert.sql";
    public static String RESOURCE_FILE_INSERT_SQL = "insert.sql";
    public static String RESOURCE_QUERIES_PATH = "resources\\queries\\";

    ///////////////////////LOG LABEL
    //public static String LOG_PATH = "logPath";
    //public static String LOG_EXCEPTION_PATH = "logExceptionPath";
    //public static String LOG_SMS_PATH = "logSmsPath";
    //public static String UPLOAD_PATH = "uploadPath";
    ////////////////////// END LOG LABEL

    /////////////////////// STATUS TABLE
    public static String PAYMENT_STATUS_PASSIVE = "P";
    public static String PAYMENT_STATUS_ACTIVE = "A";
    public static String PAYMENT_STATUS_CONFIRMED = "C";

    /////////////////////// END STATUS TABLE
    //////////////////////SMS SENDER
    //public static String SMS_SENDER_URL = "smsSenderUrl";
    //public static String SMS_SENDER_USERNAME = "username";
    //public static String SMS_SENDER_PASSWORD = "password";
    //public static String SMS_SENDER_NAME_OMID = "senderNameOmid";
    //public static String SMS_SENDER_NAME_FAB = "senderNameFab";

    //////////////////////END SMS SENDER
    //////////////////////SQL QUERIES
    public static String SQL_QUERY_GET_LAST_ID = "getLastId";
    public static String SQL_QUERY_SELECT_SALE_TRACTION_BY_SALESMAN = "selectSaleTractionBySalesman";
    public static String SQL_QUERY_SELECT_SALE_TRACTION_BY_SALESMAN_ROW_COUNT = "selectSaleTractionBySalesmanRowCount";

    //////////////////////END SQL QUERIES
    //ENTITY LABEL
    public static String ENTITY_LABEL_TYPE_STRING = "STRING";
    public static String ENTITY_LABEL_TYPE_DATE = "DATE";
    public static String ENTITY_LABEL_TYPE_TIME = "TIME";
    public static String ENTITY_LABEL_TYPE_NUMBER = "NUMBER";
    public static String LABEL_TYPE = "labelType";
    public static String LABEL_DESCRIPTION = "description";
    public static String LABEL_FIELD_NAME = "fieldName";
    ///END ENTITY LABEL
    ////////////OPERATIONS
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
    public static String INNER_JOIN = "INNER JOIN";
    public static String LEFT_JOIN = "LEFT JOIN";
    public static String RIGHT_JOIN = "RIGHT JOIN";

    ////////////END ORPERATIONS
    
    
   ///////////////// TIGER LABEL
    public static String TIGER_LAST_DB = "db";
    public static String TIGER_LAST_PERIOD = "lastPeriod";
   public static String TIGER_LAST_COMPANY = "lastCompany";
   
   ///////////// END TIGER LABEL
   
   public static String NONE_DOT_LINES = "------";
   
   public static String USER_CONTROLLER_COMPONENT_TYPE_ENTITY = "ENT";
   public static String USER_CONTROLLER_COMPONENT_TYPE_SERVICE = "SRV";
   
   public static String RESULT_SET_ROW_COUNT = "rowCount";
   
   
   public static String LIST_ITEM_LISTENER_DAILY = "daily";
   public static String LIST_ITEM_LISTENER_WEEKLY = "weekly";
   
   
   
}
