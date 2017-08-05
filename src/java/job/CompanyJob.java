/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.cr.entity.EntityCrCompany;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import utility.MailSender;
import utility.QException;
import utility.SessionManager;
import utility.sqlgenerator.DBConnection;
import utility.sqlgenerator.QLogger;

/**
 *
 * @author nikli
 */
public class CompanyJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        /*try {
        MailSender.send("ilkin@esrefli.com", "Company registered", "Your company "+"<test>" + " registered successfully.");
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("send mail error: "+e.getMessage());
        }*/
        Connection conn = null;
        try {
            conn = new DBConnection().getConnection();
            conn.setAutoCommit(false);
            //SessionManager.setConnection(Thread.currentThread().getId(), conn);

            PreparedStatement pst = conn.prepareStatement("select lower(table_name) table_name from cr_user_tables where type='t'");
            ResultSet rst = pst.executeQuery();
            List<String> tableList = new ArrayList<>();
            while (rst.next()) {
                tableList.add(rst.getString(1));
            }
            
            pst = conn.prepareStatement("SELECT ut.table_script FROM cr_user_tables ut WHERE ut.type='v'");
            rst = pst.executeQuery();
            List<String> viewList = new ArrayList<>();
            while (rst.next()) {
                viewList.add(rst.getString(1));
            }

            PreparedStatement ps = conn.prepareStatement("select c.id, lower(c.company_domain) company_domain, u.email_1, company_db, company_type, u.id user_id "
                    + "from cr_company c, cr_user u "
                    + "where c.status = ?  and c.id=u.fk_company_id order by c.insert_date");
            ps.setString(1, EntityCrCompany.CompanyStatus.PENDING.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String companyDb = rs.getString("company_db");
                String id = rs.getString("id");
                String email = rs.getString("email_1");
                String userId = rs.getString("user_id");
                String companyType = rs.getString("company_type");

                PreparedStatement psu = conn.prepareStatement("UPDATE cr_company SET status=? WHERE id=? AND status='p'");
                psu.setString(1, EntityCrCompany.CompanyStatus.CREATE.toString());
                psu.setString(2, id);
                psu.executeUpdate();
                conn.commit();

                Statement stmt = conn.createStatement();

                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + companyDb + ";");

                //conn.setCatalog("apd_"+companyDomain);
                for (String tableName : tableList) {
                    stmt.executeUpdate("CREATE TABLE " + companyDb + "." + tableName + " LIKE apdvoice." + tableName);
                }
                
                
                for (String viewScript : viewList) {
                    stmt.executeUpdate(viewScript);
                }

                if (companyType.equals(EntityCrCompany.CompanyType.COMPANY.toString())) {
                    psu = conn.prepareStatement("INSERT INTO " + companyDb + ".cr_user SELECT * from apdvoice.cr_user WHERE id=?");
                    psu.setString(1, userId);
                    psu.executeUpdate();

                    psu = conn.prepareStatement("DELETE FROM apdvoice.cr_user WHERE id=?");
                    psu.setString(1, userId);
                    psu.executeUpdate();
                }

                psu = conn.prepareStatement("UPDATE cr_company SET status=? WHERE id=? AND status=?");
                psu.setString(1, EntityCrCompany.CompanyStatus.ACTIVE.toString());
                psu.setString(2, id);
                psu.setString(3, EntityCrCompany.CompanyStatus.CREATE.toString());
                psu.executeUpdate();
                conn.commit();

                MailSender.send(email, "Company registered", "Your company " + companyDb + " registered successfully.");

                System.out.println(companyDb + " company created");

            }

            System.out.println("Java web application + Quartz 2.2.1");
        } catch (QException ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions("CompanyJob", "createConnection", ex.getMessage());
        } catch (SQLException ex) {
            DBConnection.rollbackConnection(conn);
            QLogger.saveExceptions("CompanyJob", "createCompany", ex.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }

    }

}
