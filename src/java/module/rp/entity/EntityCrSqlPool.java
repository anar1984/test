package module.rp.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrSqlPool extends CoreEntity {

    public static String SQL_ID = "sqlId";
    public static String SQL_QUERY = "sqlQuery";

    private String sqlId = "";
    private String sqlQuery = "";

    public EntityCrSqlPool() {
    }

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlID) {
        this.sqlId = sqlID;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

}
