package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrPriceListList extends CoreEntity {

    public static String ID = "id";
    public static String STATUS = "status";
    public static String INSERT_DATE = "insertDate";
    public static String MODIFICATION_DATE = "modificationDate";
    public static String PAYMENT_NAME = "paymentName";
    public static String PRICE = "price";
    public static String LIST_STATUS = "listStatus";
    public static String DESCRIPTION = "description";
    public static String LIST_STATUS_NAME = "listStatusName";
    public static String CURRENCY = "currency";
    public static String CURRENCY_NAME = "currencyName";
    private String currencyName = "";
    private String currency  = "";

    private String listStatusName = "";
    private String paymentName = "";
    private String price = "";
    private String listStatus = "";
    private String description = "";

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getListStatusName() {
        return listStatusName;
    }

    public void setListStatusName(String listStatusName) {
        this.listStatusName = listStatusName;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getListStatus() {
        return listStatus;
    }

    public void setListStatus(String listStatus) {
        this.listStatus = listStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String selectDbname() {
        return "apdvoice";//temp
    }

}
