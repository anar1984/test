package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrExpense extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String EXPENSE_DATE = "expenseDate";
    public static String EXPENSE_AMOUNT = "expenseAmount";
    public static String DESCRIPTION = "description";
    public static String EXPENSE_CURRENCY = "expenseCurrency";
    private String expenseCurrency = "";

    private String expenseDate = "";
    private String expenseAmount = "";
    private String description = "";

    public String getExpenseCurrency() {
        return expenseCurrency;
    }

    public void setExpenseCurrency(String expenseCurrency) {
        this.expenseCurrency = expenseCurrency;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
