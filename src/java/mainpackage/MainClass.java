/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainpackage;

import java.util.ArrayList;

import utility.Carrier;
import utility.sqlgenerator.EntityManager;
import utility.CallDispatcher;
import utility.QException;
import utility.WhereSingle;

/**
 *
 * @author 02483577
 */
public class MainClass {

    public static void main(String[] arg) throws QException, ClassNotFoundException {
        String f = "name";
        String st = "\"li\"";
         String s = new WhereSingle(f,st,new ArrayList()).exec();
         System.out.println(s);
 
//        test8();
    }

    public static void test8() {

        try {

            String json = "{\"kv\":{\"matrixId\":\"201706011901580491\",\"startLimit\":0,\"endLimit\":\"25\"}}"
                    + "";
            String servicename = "serviceCrGetInspectionMatrixBodyList";
//

            Carrier c = new Carrier();
            c.setServiceName(servicename);
            c.fromJson(json);
            System.out.println(c.getJson());
//            System.out.println(c.getJson());
            CallDispatcher.callService(c);
        } catch (Exception ex) {
        }
    }

    public static void test4() throws Exception {
        Carrier carrier = new Carrier();
        carrier = EntityManager.selectBySqlId("1111", new String[]{"100"});
//        System.out.println(carrier.toXML());
    }

    public static void test3() throws Exception {
        Carrier carrier = new Carrier();
//        carrier = EntityManager.selectBySqlId("1111");
//        System.out.println(carrier.toXML());
    }

}
