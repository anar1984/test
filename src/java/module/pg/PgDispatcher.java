/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.pg;

import module.cr.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.*;

/**
 *
 * @author user
 */
public class PgDispatcher {

//    public static void main(String[] arg) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
//       String rs="";
//       Carrier carrier = new Carrier();
//       carrier.setValue("username1", "Orxan");
//       carrier.setValue("password", "Hasanov");
//    }
    public static Carrier callService(Carrier carrier) throws Exception {
        try {
//Veb servis standardı “Service” + MODULE(2 characters) + METHOD_NAME
//“ServiceHrInsertNewEmployee”
            String serviceName = carrier.getServiceName();

            if (serviceName.trim().equals("")) {
                return null;
            }

            String methodName = QUtility.getMethodNameFromWSTitle(serviceName);

            carrier = executeControllMethods(methodName, carrier);
            if (!carrier.hasError()) {
                carrier = executeModelMethods(methodName, carrier);
            }

            return carrier;
        } catch (Exception ex) {
           new QException(new Object(){}.getClass().getEnclosingClass().getName(),
                  new Object(){}.getClass().getEnclosingMethod().getName(),ex);
           throw new Exception();
        } 
    }

    ///////////
    private static Carrier executeControllMethods(String methodName, Carrier carrier) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PgController hrc = new PgController();
        Method method = hrc.getClass().getMethod(methodName, Carrier.class);
        Carrier carrier1 = (Carrier) method.invoke(hrc, carrier);
        return carrier1;
    }

    private static Carrier executeModelMethods(String methodName, Carrier carrier) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PgModel model = new PgModel();
        Method method = model.getClass().getMethod(methodName, Carrier.class);
        Carrier carrier1 = (Carrier) method.invoke(model, carrier);
        return carrier1;
    }
}
