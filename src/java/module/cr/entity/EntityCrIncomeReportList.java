package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrIncomeReportList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String ACTION_DATE = "actionDate";
    public static String TOTAL_INCOME = "totalIncome";
    public static String TOTAL_OUTCOME = "totalOutcome";
    public static String CURRENCY = "currency";

    private String actionDate = "";
    private String totalIncome = "";
    private String totalOutcome = "";
    private String currency = "";

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getTotalOutcome() {
        return totalOutcome;
    }

    public void setTotalOutcome(String totalOutcome) {
        this.totalOutcome = totalOutcome;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";//temp
    }

}
