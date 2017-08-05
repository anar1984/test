/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.pg;

import controllerpool.ControllerPool;
import module.cr.entity.EntityCrAttribute;
import module.cr.entity.EntityCrModule;
import utility.Carrier;
import utility.QException;

/**
 *
 * @author user
 */
public class PgController {

    private final String SPACE = " ";
    private final String COMMA = ",";

    public Carrier genSubmodule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrModule.ID,
                cp.hasValue(carrier, EntityCrModule.ID));

        return carrier;
    }
}
