package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */


public class EntityCrCoreMessage extends CoreEntity{
    
   
    
    private String messageCode="";
    private String messageText="";
    private String messageLang="";

     

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageLang() {
        return messageLang;
    }

    public void setMessageLang(String messageLang) {
        this.messageLang = messageLang;
    }

    
}


