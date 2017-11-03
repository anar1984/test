/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.security.Key;
import module.cr.entity.EntityCrUser;
import module.cr.entity.EntityCrCompany;
import module.cr.entity.EntityCrListItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import resources.config.Config;
import utility.CacheUtil;
import utility.Carrier;
import utility.QException;
import utility.QUtility;
import utility.SessionManager;
import utility.sqlgenerator.EntityManager;

public class SessionHandler {

    private static Logger logger = LogManager.getLogger();
    private static String SYS_ADMIN_PREFIX = "admin__";

    /*public static boolean checkSession(String cookie) {
        if (cookie == null) {
            return false;
        } else {
            try {
                JsonWebEncryption jwe = new JsonWebEncryption();
                Key key = SKey.getKey();
                byte[] keyBytes = key.getEncoded();
                String ln = "";
                for (byte b : keyBytes) {
                    ln += b;
                }
//                System.out.println("key in filter->" + ln);
                jwe.setKey(key);
                cookie = cookie.split(";")[0];
                jwe.setCompactSerialization(cookie.split("=")[1]);
                int l = jwe.getPayload().length();
                return l > 1;
            } catch (Exception ex) {
                return false;
            }
        }
    }*/
    public static boolean checkSession(String token) {
        if (token == null || token.equals("")) {
            return false;
        } else {
            try {
                JsonWebEncryption jwe = new JsonWebEncryption();
                //Key key = SKey.getKey();
                Key key = CacheUtil.getKeyFromCache(token);
                /*byte[] keyBytes = key.getEncoded();
                String ln = "";
                for (byte b : keyBytes) {
                    ln += b;
                }*/
                jwe.setKey(key);
                jwe.setCompactSerialization(token);
                int l = jwe.getPayload().length();
                return l > 1;
            } catch (Exception ex) {
                logger.error("checkSession token=" + token, ex);
                return false;   
            }
        }
    }

    public static EntityCrUser getTokenFromCookie(String tokenString) throws JoseException {
        EntityCrUser token = new EntityCrUser();
        JsonWebEncryption jwe = new JsonWebEncryption();
        //jwe.setKey(SKey.getKey());
        jwe.setKey(CacheUtil.getKeyFromCache(tokenString));
        jwe.setCompactSerialization(tokenString);
        token.fromString(jwe.getPayload());
        return token;
    }

    public static EntityCrUser checkLogin(String username, String password, String domain) throws Exception {
        

        password = password.replaceAll("%", "").trim();
        username = username.replaceAll("%", "").trim();
        domain = domain.replaceAll("%", "").trim();

        if (username.trim().equals("") || password.trim().equals("")) {
            throw new Exception(".Username or password is incorrect!!!!!!!!!");
        }
        
        
        EntityCrUser user = new EntityCrUser(); 
        user.setDeepWhere(false);

        if (domain.trim().length() == 0) {//personal
            //is sys admin user
            if (username.startsWith(Config.getProperty(Config.SYSADMIN_PREFIX))) {
                return loginSysadmin(user, username, password);  
            } else {
                return loginCompanyUser(user, username, password);
            }

        } else {
            //is company
            return loginCompany(user, username, password, domain);
        }

//        System.out.println(user.getUsername() + " - " + password);
        //is compnay simple user     
    }

    private static EntityCrUser loginCompany(EntityCrUser user, String username,
            String password, String domain) throws QException {
        System.out.println("OK - 1");
        EntityCrCompany company = new EntityCrCompany();
        company.setDeepWhere(false);
        company.setCompanyDomain(domain.trim());
        Carrier cComp = EntityManager.select(company);
        System.out.println("OK - 2");
        if (cComp.getTableRowCount(company.toTableName()) == 0) {
            System.out.println(".There is no domain such as!!!!!!!!!");
            throw new QException(".There is no domain such as!!!!!!!!!");
        }
        System.out.println("OK - 3");
        if (company.getFkUserId().length() == 0) {
            System.out.println(".There is no domain such as!!!!!!!!!");
            throw new QException(".There is no domain such as!!!!!!!!!");
        }

        System.out.println("OK - 4");

        user = new EntityCrUser();
        user.setDeepWhere(false);
        user.setUsername(username.trim());
        user.setDbname(company.getCompanyDb());

        EntityManager.select(user);
        System.out.println("OK - 5");
        System.out.println("passwd db->>" + user.getPassword().trim());
        System.out.println("passwd gui->>" + password.trim());
        if (user.getPassword().trim().equals("")
                || !user.getPassword().trim().equals(password.trim())) {
            System.out.println(".Username or password is incorrect!!!!!!!!!");
            System.out.println("OK - 6");

            throw new QException(".Username or password is incorrect!!!!!!!!!");
        } else {
            System.out.println("OK - 7");

            SessionManager.setDomain(Thread.currentThread().getId(), company.getCompanyDb());
            user.setDbname(company.getCompanyDb());
            user.setCompanyId(company.getId());
            System.out.println("OK - 8");

            return user;
        }
    }

    private static EntityCrUser loginSysadmin(EntityCrUser user, String username,
            String password) throws QException {

        user = new EntityCrUser();
        user.setDeepWhere(false);
        user.setUsername(username.trim());
        user.setDbname(Config.getProperty(Config.SYSADMIN_COMPANY_DB_NAME));
        user.setPassword(password);
        user.setStartLimit(0);
        user.setEndLimit(0);
        int rc  = EntityManager.select(user).getTableRowCount(user.toTableName());
        

        if (rc==0) {
            System.out.println(".Username or password is incorrect!!!!!!!!!");
            throw new QException(".Username or password is incorrect!!!!!!!!!");
        } else {
            SessionManager.setDomain(Thread.currentThread().getId(),Config.getProperty(Config.SYSADMIN_COMPANY_DB_NAME));
            user.setDbname(Config.getProperty(Config.SYSADMIN_COMPANY_DB_NAME));
            user.setCompanyId(Config.getProperty(Config.SYSADMIN_COMPANY_DB_ID));
            return user;
        }

    }

    private static EntityCrUser loginCompanyUser(EntityCrUser user, String username,
            String password) throws QException {
        EntityCrCompany company = new EntityCrCompany();
        company.setDeepWhere(false);
        company.setPersonUsername(username);
        company.setCompanyType(
                EntityCrCompany.CompanyType.PERSONAL.toString());
        company.setStartLimit(0);;
        company.setEndLimit(0);
        EntityManager.select(company);

        if (company.getCompanyName().length() == 0) {
            throw new QException(".Username or password is incorrect!!!!!!!!!");
        }

        user = new EntityCrUser();
        user.setDeepWhere(false);
        user.setUsername(username.trim());
        user.setDbname(company.getCompanyDb());
        user.setPassword(password);
        user.setStartLimit(0);
        user.setEndLimit(0);
       int rc  = EntityManager.select(user).getTableRowCount(user.toTableName());
        

        if (rc==0) {
            System.out.println(".Username or password is incorrect!!!!!!!!!");
            throw new QException(".Username or password is incorrect!!!!!!!!!");
        } else {
            SessionManager.setDomain(Thread.currentThread().getId(), company.getCompanyDb());
            user.setDbname(company.getCompanyDb());
            user.setCompanyId(company.getId());
            return user;
        }
    }

    public static String encryptUser(EntityCrUser user) {
        try {
            JsonWebEncryption jwe = new JsonWebEncryption();
            jwe.setPayload(user.toString());
            jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
            jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
            Key key = SKey.getKey();
            byte[] keyBytes = key.getEncoded();
            String ln = "";
            for (byte b : keyBytes) {
                ln += b;
            }
//            System.out.println("key->" + ln);

            jwe.setKey(key);
            String token = jwe.getCompactSerialization();
            System.out.println("ok");
            
            CacheUtil.putSessionCache(token, key);
            return token;
        } catch (Exception ex) {
            System.out.println("error");
            logger.error("encryptUser", ex);
            return "";
        }
    }

    public static boolean isLangAvailable(String lang) throws QException {
        boolean f = true;

        EntityCrListItem ent = new EntityCrListItem();
        ent.setDeepWhere(false);
        ent.setItemCode("language");
        ent.setItemKey(lang);
        ent.setLang("ENG");
        Carrier c = EntityManager.select(ent);
        return c.getTableRowCount(ent.toTableName()) > 0;

    }

}
