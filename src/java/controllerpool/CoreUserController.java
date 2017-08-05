/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllerpool;

import module.cr.entity.EntityCrUserControllerList;
import label.CoreLabel;
import utility.Carrier;
import utility.QException;

/**
 *
 * @author Lenovo
 */
public class CoreUserController {
     public static Carrier getEntityController(String entityName) throws QException {
        try {
            if (entityName.trim().equals("")) {
                return new Carrier();
            }

            Carrier carrier = new Carrier();
            carrier.setValue(EntityCrUserControllerList.FK_COMPONENT_ID, entityName);
            carrier.setValue(EntityCrUserControllerList.COMPONENT_TYPE, CoreLabel.USER_CONTROLLER_COMPONENT_TYPE_ENTITY);
            carrier = carrier.callService("serviceCrGetUserControllerList");
            return carrier;

        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public static Carrier getServiceController(String serviceName,String userId) throws QException {
        try {
           
            if (serviceName.trim().equals("") || userId.trim().equals("")) {
                return new Carrier();
            }

            Carrier carrier = new Carrier();
            carrier.setValue(EntityCrUserControllerList.FK_USER_ID,userId );
            carrier.setValue(EntityCrUserControllerList.FK_COMPONENT_ID, serviceName);
            carrier.setValue(EntityCrUserControllerList.COMPONENT_TYPE, CoreLabel.USER_CONTROLLER_COMPONENT_TYPE_SERVICE+"%IN%SRVFORMCOMPONENT");
            carrier = carrier.callService("serviceCrGetUserControllerList");
            return carrier;

        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
}
