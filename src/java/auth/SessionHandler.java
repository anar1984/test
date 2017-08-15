/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.security.Key;
import module.cr.entity.EntityCrUser;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.cr.entity.EntityCrCompany;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import utility.Carrier;
import utility.SessionManager;
import utility.sqlgenerator.EntityManager;

public class SessionHandler {

    public static boolean checkSession(String cookie) {
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
    }

    public static EntityCrUser getTokenFromCookie(String tokenString) throws JoseException {
        EntityCrUser token = new EntityCrUser();
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setKey(SKey.getKey());
        jwe.setCompactSerialization(tokenString);
        token.fromString(jwe.getPayload());
        return token;
    }

    public static EntityCrUser checkLogin(String username, String password, String domain) throws Exception {
        if (username.trim().equals("") || password.trim().equals("")) {
            throw new Exception(".Username or password is incorrect!!!!!!!!!");
        }
        password = password.replaceAll("%", "");
        username = username.replaceAll("%", "");
        domain = domain.replaceAll("%", "");
        
        /*EntityCrCompany company = new EntityCrCompany();
        company.setDeepWhere(false);
        company.setCompanyDomain(domain.trim());
        EntityManager.select(company);
        
        if (company == null) {
            throw new Exception(".There is no domain such as!!!!!!!!!");
        }*/
        EntityCrUser user = new EntityCrUser();
        user.setDeepWhere(false);
        if (domain.equals("")) {//personal
            user.setUsername(username.trim());
            user.setDbname("apdvoice");
            EntityManager.select(user);

            if (user.getFkCompanyId().length() == 0) {
                throw new Exception(".Username or password is incorrect!!!!!!!!!");
            }

            EntityCrCompany company = new EntityCrCompany();
            company.setDeepWhere(false);
            company.setId(user.getFkCompanyId());
            company.setCompanyType(
                    EntityCrCompany.CompanyType.PERSONAL.toString());
            EntityManager.select(company);

            if (company.getCompanyName().length()== 0) {
                throw new Exception(".Username or password is incorrect!!!!!!!!!");
            }

            //user.setDbname(company.getCompanyDb());
            if (user.getPassword().trim().equals("")
                    || !user.getPassword().trim().equals(password.trim())) {
                System.out.println(".Username or password is incorrect!!!!!!!!!");
                throw new Exception(".Username or password is incorrect!!!!!!!!!");
            } else {
                user.setDbname(company.getCompanyDb());
                return user;
            }
        } else {//is company
            EntityCrCompany company = new EntityCrCompany();
            company.setDeepWhere(false);
            company.setCompanyDomain(domain.trim());
            Carrier cComp = EntityManager.select(company);
            if (cComp.getTableRowCount(company.toTableName()) == 0) {
                throw new Exception(".There is no domain such as!!!!!!!!!");
            }

            //is contact person
            user.setFkCompanyId(company.getId());
            user.setUsername(username.trim());
            user.setDbname("apdvoice");
            Carrier cUser = EntityManager.select(user);

            //if user contact person
            if (cUser.getTableRowCount(user.toTableName()) == 0) {
                user = new EntityCrUser();
                user.setDeepWhere(false);
                user.setUsername(username.trim());
                user.setDbname(company.getCompanyDb());
                EntityManager.select(user);
            }

            if (user.getPassword().trim().equals("")
                    || !user.getPassword().trim().equals(password.trim())) {
                System.out.println(".Username or password is incorrect!!!!!!!!!");
                throw new Exception(".Username or password is incorrect!!!!!!!!!");
            } else {
                //SessionManager.setDomain(Thread.currentThread().getId(), company.getCompanyDb());
                user.setDbname(company.getCompanyDb());
                return user;
            }

        }

//        System.out.println(user.getUsername() + " - " + password);
        //is compnay simple user     
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

            return token;
        } catch (Exception ex) {
            System.out.println("error");

            Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
}
