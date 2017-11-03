package utility;

import java.sql.Connection;
import module.cr.entity.EntityCrPerson;
import module.cr.entity.EntityCrUser;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import label.CoreLabel;
import module.cr.entity.EntityCrPermission;
import module.cr.entity.EntityCrRelRoleRule;
import module.cr.entity.EntityCrRelRuleAndPermission;
import module.cr.entity.EntityCrRole;
import module.cr.entity.EntityCrRule;
import org.ehcache.CacheManager;
import utility.sqlgenerator.EntityManager;

/**
 *
 * @author candide
 */
public class SessionManager {

    private static final String SEPERATOR = "__";
    private static final String LI_USER_PERMISSION_TYPE_ADMIN = "admin";
    private static final String LI_USER_PERMISSION_TYPE_USER = "user";
    public static final String DEFAULT_LANG = "ENG";

    private static Map<Long, String> userMap = new HashMap<>();
    private static Map<Long, String> langMap = new HashMap<>();
    private static Map<Long, Connection> conn = new HashMap<>();
    private static Map<Long, String> domainMap = new HashMap<>();
    private static Map<Long, String> userIdMap = new HashMap<>();
    private static Map<Long, String> companyIdMap = new HashMap<>();
    
    private static Map<String, Subject> permissionMap = new HashMap<>();
    
    
    
    public static void cleanSessionThread() {
        userMap.remove(getCurrentThreadId());
        langMap.remove(getCurrentThreadId());
        conn.remove(getCurrentThreadId());
        domainMap.remove(getCurrentThreadId());
        userIdMap.remove(getCurrentThreadId());
        companyIdMap.remove(getCurrentThreadId());
        
    }
    
    public static void setCompanyId(Long threadId,String CompanyId){
        companyIdMap.put(threadId, CompanyId);
    }
    
    public static String getCompanyId(Long threadId){
        return companyIdMap.getOrDefault(threadId,"__2__");
    }
    
    public static void setDomain(Long threadId,String domain){
        domainMap.put(threadId, domain);
    }
    
    public static String getDomain(Long threadId){
        return domainMap.get(threadId);
    }
 
    public static String getCurrentDomain(){
        return domainMap.getOrDefault(getCurrentThreadId(),"apdvoice");
    }
 
 
    public static Connection getCurrentConnection() {
        return conn.get(getCurrentThreadId());
    }

    public static void setConnection(Long threadId, Connection connection) {
        conn.put(threadId, connection);
    }

    public static void setUserName(Long threadId, String userName) {
        userMap.put(threadId, userName);
    }
    public static void setUserId(Long threadId, String userId) {
        userIdMap.put(threadId, userId);
    }

    public static void setLang(Long threadId, String lang) {
        langMap.put(threadId, lang);
    }

    public static String getLang(Long threadId) {
        String lang = langMap.getOrDefault(threadId, DEFAULT_LANG);

        return lang;
    }

    public static String getCurrentLang() {
        return getLang(getCurrentThreadId());
    }

    public static String getCurrentCompanyId() throws QException {
        String id = getCompanyId(getCurrentThreadId());
        if (id.trim().length()==0){
            throw new QException("company_id is not available");
        }
        return id;
    }
    
    public static String getUserByThreadId(Long threadId) {
        return userMap.get(threadId);
    }
    
    public static String getUserIdByThreadId(Long threadId) {
        return userIdMap.get(threadId);
    }

    public static Long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }

    public static String getCurrentUsername() {
        String username = "";
        username = SessionManager.getUserByThreadId(Thread.currentThread().getId());
        //if (username == null) {
          //  username = "admin1";
        //}
        return username;
    }

    public static String getCurrentUserId() throws QException {
        return SessionManager.getUserIdByThreadId(Thread.currentThread().getId());
        /*try {
            String usename = SessionManager.getCurrentUsername();

            String id = "";
            if (!usename.trim().equals("")) {
                EntityCrUser ent = new EntityCrUser();
                ent.setUsername(usename);
                EntityManager.select(ent);
                id = ent.getId();
            }
            return id;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }*/
    }

    public static String getUserIdByEmployeeId(String employeeId) throws QException {
        try {
            String id = "";
            if (!employeeId.trim().equals("")) {
                EntityCrUser ent = new EntityCrUser();
                ent.setFkEmployeeId(employeeId);
                EntityManager.select(ent);
                id = ent.getId();
            }
            return id;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static String getCurrentEmployeeId() throws QException {
        try {
            String usename = SessionManager.getCurrentUsername();

            String id = "";
            if (!usename.trim().equals("")) {
                EntityCrUser ent = new EntityCrUser();
                ent.setUsername(usename);
                EntityManager.select(ent);
                id = ent.getFkEmployeeId();

            }
            return id;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static boolean isCurrentEmployeeAdmin() throws QException {
        try {
            String usename = SessionManager.getCurrentUsername();
            boolean f = false;
            String type = LI_USER_PERMISSION_TYPE_USER;
            if (!usename.trim().equals("")) {
                EntityCrUser ent = new EntityCrUser();
                ent.setUsername(usename);
                EntityManager.select(ent);
                type = ent.getLiUserPermissionCode();
            }

            if (type.equals(LI_USER_PERMISSION_TYPE_ADMIN)) {
                f = true;
            }
            return f;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static String getCurrentTgUserId() throws QException {
        try {
            String usename = SessionManager.getCurrentUsername();

            String id = "";
            if (!usename.trim().equals("")) {
                EntityCrUser ent = new EntityCrUser();
                ent.setUsername(usename);
                EntityManager.select(ent);
                id = ent.getTgUserId();
            }
            return id;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static String getFullnameOfCurrentUser() throws QException {
        try {
            String usename = SessionManager.getUserByThreadId(Thread.currentThread().getId());
            String fullname = "Anar Rustamov";
//            if (!usename.trim().equals("")) {
//                EntityCrUser ent = new EntityCrUser();
//                ent.setUsername(usename);
//                EntityManager.select(ent);
//                if (!ent.getFkEmployeeId().equals("")) {
//                    EntityHrEmployee entHr = new EntityHrEmployee();
//                    entHr.setId(ent.getFkEmployeeId());
//                    EntityManager.select(entHr);
//                    if (!entHr.getFkPersonId().equals("")) {
//                        EntityCrPerson entPerson = new EntityCrPerson();
//                        entPerson.setId(entHr.getFkPersonId());
//                        EntityManager.select(entPerson);
//                        fullname = entPerson.getName() + " " + entPerson.getSurname() + " " + entPerson.getMiddlename();
//                    }
//                }
//            }
            return fullname;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    /*public static boolean isInWebServiceInterval(String servicename) throws QException {
        try {
            boolean f;

            //default web service call interval per user
            CommonConfigurationProperties prop = new CommonConfigurationProperties();
            String timeInterval = prop.getProperty(WEB_SERVICE_INTERVAL);
            int timeInt = Integer.valueOf(timeInterval);

            //key=value standard of webservicecallconfiguration file
            //key = USERNAME + __ + SERVICENAME
            //value = DATE + __ + HOUR
            String username = SessionManager.getCurrentUsername();
            String key = username.trim().toUpperCase() + SEPERATOR + servicename.trim().toUpperCase();
            String value = QDate.getCurrentDate() + SEPERATOR + QDate.getCurrentTime();

            String time = "";
            WebServiceCallConfigurationProperties propWeb = new WebServiceCallConfigurationProperties();
            try {
                String v = propWeb.getProperty(key);
                String date = v.split(SEPERATOR)[0];
                if (date.trim().equals(QDate.getCurrentDate())) {
                    time = v.split(SEPERATOR)[1];
                }
            } catch (Exception e) {
            }

            if (time.length() == 0) {
                Date d = new Date();
                d = QDate.addSecond(d, (-1) * (timeInt + 10));
                time = QDate.convertTimeToString(d);
            }

            String currentTime = QDate.getCurrentTime();
            long timeDifference = QDate.getDifferenceInSeconds(QDate.convertStringToTime(time), QDate.convertStringToTime(currentTime));

            f = timeDifference > timeInt;

            if (f) {
                //yeni deyeri elave etmek lazimdir.
                propWeb.setProperty(key, value);
            }

            return f;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }*/
    
    public static boolean hasAccessToService(String serviceName) throws QException {
        Subject subject = permissionMap.get(getCurrentUsername());
        if (subject == null) {
            loadUserPermissions();
            subject = permissionMap.get(getCurrentUsername());
        }
        return subject.isPermitted("post:srv:"+serviceName);
    }
    
    public static boolean isSysAdmin() throws QException {
        return hasRole("SYSADMIN");
    }
    
    public static boolean hasRole(String roleName) throws QException {
        Subject subject = permissionMap.get(getCurrentUsername());
        if (subject == null) {
            loadUserPermissions();
            subject = permissionMap.get(getCurrentUsername());
        }
        return subject.hasRole(roleName);
    }
    
    public static boolean hasRule(String ruleName) throws QException {
        Subject subject = permissionMap.get(getCurrentUsername());
        if (subject == null) {
            loadUserPermissions();
            subject = permissionMap.get(getCurrentUsername());
        }
        return subject.hasRule(ruleName);
    }
    
    public static boolean isPermitted(String permission) throws QException {
        Subject subject = permissionMap.get(getCurrentUsername());
        if (subject == null) {
            loadUserPermissions();
            subject = permissionMap.get(getCurrentUsername());
        }
        return subject.isPermitted(permission);
    }
    
    public static boolean isCurrentUserCompanyAdmin() throws QException{
        String userId = getCurrentUserId();
        String permissionCode = "A"+CoreLabel.IN+"AD";
        EntityCrUser entUsr = new EntityCrUser();
        entUsr.setDeepWhere(false);
        entUsr.setDbname(getCurrentDomain());
        entUsr.setId(userId);
        entUsr.setLiUserPermissionCode(permissionCode);
        Carrier cr = EntityManager.select(entUsr);
        return cr.getTableRowCount(entUsr.toTableName())>0;
    }
    
    private static void loadUserPermissions() throws QException {
        String userId = SessionManager.getCurrentUserId();
        Subject subject = new Subject();
        
//        entityCrRelUserRole.setFkUserId(userId);
//        Carrier crRelUserRole = EntityManager.select(entityCrRelUserRole);
//        
//        for(String fkRoleId: crRelUserRole.getValue("EntityCrRelUserRole", EntityCrRelUserRole.FK_ROLE_ID)) {
//            EntityCrRole entityCrRole = new EntityCrRole();
//            entityCrRole.setId(fkRoleId);
//            EntityManager.select(entityCrRole);
//            subject.addRole(entityCrRole.getRoleName());
//            
//            EntityCrRelRoleRule entityCrRelRoleRule = new EntityCrRelRoleRule();
//            entityCrRelRoleRule.setFkRoleId(fkRoleId);
//            Carrier crRelRoleRule = EntityManager.select(entityCrRelRoleRule);
//            
//            for(String fkRuleId: crRelRoleRule.getValue("EntityCrRelRoleRule", EntityCrRelRoleRule.FK_RULE_ID)) {
//                EntityCrRule entityCrRule = new EntityCrRule();
//                entityCrRule.setId(fkRuleId);
//                EntityManager.select(entityCrRule);
//                subject.addRule(entityCrRule.getRuleName());
//                
//                EntityCrRelRuleAndPermission entityCrRelRulePermission = new EntityCrRelRuleAndPermission();
//                entityCrRelRulePermission.setFkRuleId(fkRuleId);
//                Carrier crRelRulePermission = EntityManager.select(entityCrRelRulePermission);
//                
//                for(String fkPermissionId: crRelRulePermission.getValue("EntityCrRelRulePermission", EntityCrRelRuleAndPermission.FK_PERMISSION_ID)) {
//                    EntityCrPermission entityCrPermission = new EntityCrPermission();
//                    entityCrPermission.setId(fkPermissionId);
//                    EntityManager.select(entityCrPermission);
//                    subject.addPermission(entityCrPermission.getPermissionString());
//                }
//            }
//        }
//        
//        EntityCrRelUserRule entityCrRelUserRule = new EntityCrRelUserRule();
//        entityCrRelUserRule.setFkUserId(userId);
//        Carrier crRelUserRule = EntityManager.select(entityCrRelUserRule);
//        
//        for(String fkRuleId: crRelUserRule.getValue("EntityCrRelUserRule", EntityCrRelUserRule.FK_RULE_ID)) {
//            EntityCrRule entityCrRule = new EntityCrRule();
//            entityCrRule.setId(fkRuleId);
//            EntityManager.select(entityCrRule);
//            subject.addRule(entityCrRule.getRuleName());
//
//            EntityCrRelRuleAndPermission entityCrRelRulePermission = new EntityCrRelRuleAndPermission();
//            entityCrRelRulePermission.setFkRuleId(fkRuleId);
//            Carrier crRelRulePermission = EntityManager.select(entityCrRelRulePermission);
//
//            for(String fkPermissionId: crRelRulePermission.getValue("EntityCrRelRulePermission", EntityCrRelRuleAndPermission.FK_PERMISSION_ID)) {
//                EntityCrPermission entityCrPermission = new EntityCrPermission();
//                entityCrPermission.setId(fkPermissionId);
//                EntityManager.select(entityCrPermission);
//                subject.addPermission(entityCrPermission.getPermissionString());
//            }
//            
//        }
//        
//        permissionMap.put(SessionManager.getCurrentUsername(), subject);
        
    }

}
