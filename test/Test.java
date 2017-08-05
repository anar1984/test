

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.cr.entity.EntityCrMenu;
import utility.Carrier;
//import utility.EntityCreator;
//import utility.MenuCarrier;
//import utility.MenuProperties;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class Test {

    public static void main(String[] arg) {
//        EntityUcMenu ent = new EntityUcMenu();
//        ent.setParentId("bilmirem");
//        try {
//            ent.insert();
//             } catch (Exception ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
        try {
            Carrier c = new Carrier();
            c.setServiceName("ServiceUcSelectMenus");
//            System.out.println(c.getActiveJSON());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
       
    }

    public static void URLConnectionReader(String url) throws Exception {
        URL yahoo = new URL(url);
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }
}
