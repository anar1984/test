package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrUserTable extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String TABLE_NAME = "tableName";
    public static String TYPE = "type";
    public static String TABLE_SCRIPT = "tableScript";
    public static String SEQNUM = "seqnum";

    private String tableName = "";
    private String type = "";
    private String tableScript = "";
    private String seqnum = "";

    public enum Type {
        VIEW("V"),
        TABLE("T"),
        SCRIPT("S"),      
        BUGSCRIPT("BS");       
        private final String status;

        Type(String status) {
            this.status = status;
        }

        public String toString() {
            return this.status;
        }
    }

    

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTableScript() {
        return tableScript;
    }

    public void setTableScript(String tableScript) {
        this.tableScript = tableScript;
    }

    public String getSeqnum() {
        return seqnum;
    }

    public void setSeqnum(String seqnum) {
        this.seqnum = seqnum;
    }

    

    @Override
    public String selectDbname() {
        return "apdvoice";
    }

}
