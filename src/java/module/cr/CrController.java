/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.cr;

import controllerpool.ControllerPool;
import static module.cr.CrModel.getSubmoduleAttributeList;
import module.cr.entity.*;
import utility.Carrier;
import utility.QException;
import utility.sqlgenerator.EntityManager;

/**
 *
 * @author user
 */
public class CrController {

    private final String SPACE = " ";
    private final String COMMA = ",";

    public Carrier insertNewListItem(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrListItem.ITEM_CODE,
                    cp.hasValue(carrier, EntityCrListItem.ITEM_CODE));
            carrier.addController(EntityCrListItem.ITEM_KEY,
                    cp.hasValue(carrier, EntityCrListItem.ITEM_KEY));
            carrier.addController(EntityCrListItem.ITEM_VALUE,
                    cp.hasValue(carrier, EntityCrListItem.ITEM_VALUE));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier updateListItem(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrListItem.ID, cp.hasValue(carrier, EntityCrListItem.ID));
            carrier.addController(EntityCrListItem.ITEM_CODE, cp.hasValue(carrier, EntityCrListItem.ITEM_CODE));
            carrier.addController(EntityCrListItem.ITEM_KEY, cp.hasValue(carrier, EntityCrListItem.ITEM_KEY));
            carrier.addController(EntityCrListItem.ITEM_VALUE, cp.hasValue(carrier, EntityCrListItem.ITEM_VALUE));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    public Carrier deleteListItem(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrListItem.ID, cp.hasValue(carrier, EntityCrListItem.ID));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    public Carrier getListItemList(Carrier carrier) throws QException {
        return carrier;
    }

    public Carrier getListItemMainList(Carrier carrier) throws QException {
        return carrier;
    }

    public Carrier insertNewUser(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrUser.PASSWORD, cp.hasValue(carrier, EntityCrUser.PASSWORD));
            carrier.addController(EntityCrUser.USERNAME, cp.hasValue(carrier, EntityCrUser.USERNAME));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier insertNewUserController(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
//            carrier.addController(EntityCrUserController.FK_USER_ID, cp.hasValueIfKeyExist(carrier, EntityCrUserController.FK_USER_ID));
            carrier.addController("liComponentCode", cp.hasValue(carrier, "liComponentCode"));
            carrier.addController(EntityCrUserController.FK_COMPONENT_ID, cp.hasValue(carrier, EntityCrUserController.FK_COMPONENT_ID));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier getUserControllerList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier updateUserController(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrUserController.ID, cp.hasValue(carrier, EntityCrUserController.ID));
//            carrier.addController(EntityCrUserController.FK_USER_ID, cp.hasValueIfKeyExist(carrier, EntityCrUserController.FK_USER_ID));
            carrier.addController(EntityCrUserController.FK_COMPONENT_ID, cp.hasValue(carrier, EntityCrUserController.FK_COMPONENT_ID));
            carrier.addController("liComponentCode", cp.hasValue(carrier, "liComponentCode"));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier deleteUserController(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrUserController.ID, cp.hasValue(carrier, EntityCrUserController.ID));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier updateUser(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrUser.ID, cp.hasValue(carrier, EntityCrUser.ID));
            carrier.addController(EntityCrUser.PASSWORD, cp.hasValue(carrier, EntityCrUser.PASSWORD));
            carrier.addController(EntityCrUser.USERNAME, cp.hasValue(carrier, EntityCrUser.USERNAME));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier deleteUser(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrUser.ID, cp.hasValue(carrier, EntityCrUser.ID));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier getUserList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier getCurrentDateAndTime(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public static Carrier getUserList4Combo(Carrier carrier) throws QException {
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }

    }

    public Carrier insertNewEntityLabel(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrEntityLabel.FIELD_NAME, cp.hasValue(carrier, EntityCrEntityLabel.FIELD_NAME));
            carrier.addController(EntityCrEntityLabel.DESCRIPTION, cp.hasValue(carrier, EntityCrEntityLabel.DESCRIPTION));
            carrier.addController(EntityCrEntityLabel.LABEL_TYPE, cp.hasValue(carrier, EntityCrEntityLabel.LABEL_TYPE));
//            carrier.addController(EntityCrEntityLabel.FIELD_NAME,
//                    cp.isNotExistInEntity(new EntityCrEntityLabel(), EntityCrEntityLabel.FIELD_NAME, carrier.getValue(EntityCrEntityLabel.FIELD_NAME).toString()));

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier getEntityLabelList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier updateEntityLabel(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrEntityLabel.ID, cp.hasValue(carrier, EntityCrEntityLabel.ID));
            carrier.addController(EntityCrEntityLabel.DESCRIPTION, cp.hasValue(carrier, EntityCrEntityLabel.DESCRIPTION));
            carrier.addController(EntityCrEntityLabel.LABEL_TYPE, cp.hasValue(carrier, EntityCrEntityLabel.LABEL_TYPE));
            if (carrier.isKeyExist(EntityCrEntityLabel.FIELD_NAME)) {
                carrier.addController(EntityCrEntityLabel.FIELD_NAME,
                        cp.isNotExistInEntityExcept(new EntityCrEntityLabel(), EntityCrEntityLabel.FIELD_NAME,
                                carrier.getValue(EntityCrEntityLabel.FIELD_NAME).toString(),
                                carrier.getValue(EntityCrEntityLabel.ID).toString()));
            }
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier deleteEntityLabel(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrEntityLabel.ID, cp.hasValue(carrier, EntityCrUserController.ID));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier getListItemByComponentType(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController("liComponentCode", cp.hasValue(carrier, "liComponentCode"));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier insertNewRelRuleAndComponent(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrRelRuleAndComponent.LI_RULE_KEY, cp.hasValue(carrier, EntityCrRelRuleAndComponent.LI_RULE_KEY));
            carrier.addController(EntityCrRelRuleAndComponent.LI_COMPONENT_CODE, cp.hasValue(carrier, EntityCrRelRuleAndComponent.LI_COMPONENT_CODE));
            carrier.addController(EntityCrRelRuleAndComponent.LI_COMPONENT_KEY, cp.hasValue(carrier, EntityCrRelRuleAndComponent.LI_COMPONENT_KEY));

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier getRelRuleAndComponentList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier updateRelRuleAndComponent(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrRelRuleAndComponent.ID, cp.hasValue(carrier, EntityCrRelRuleAndComponent.ID));
            carrier.addController(EntityCrRelRuleAndComponent.LI_RULE_KEY, cp.hasValue(carrier, EntityCrRelRuleAndComponent.LI_RULE_KEY));
            carrier.addController(EntityCrRelRuleAndComponent.LI_COMPONENT_CODE, cp.hasValue(carrier, EntityCrRelRuleAndComponent.LI_COMPONENT_CODE));
            carrier.addController(EntityCrRelRuleAndComponent.LI_COMPONENT_KEY, cp.hasValue(carrier, EntityCrRelRuleAndComponent.LI_COMPONENT_KEY));

            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier deleteRelRuleAndComponent(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrRelRuleAndComponent.ID, cp.hasValue(carrier, EntityCrRelRuleAndComponent.ID));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier insertNewUserControllerByRule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrRelRuleAndComponent.LI_RULE_KEY, cp.hasValue(carrier, EntityCrRelRuleAndComponent.LI_RULE_KEY));
            carrier.addController(EntityCrUserController.FK_USER_ID, cp.hasValue(carrier, EntityCrUserController.FK_USER_ID));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public Carrier insertNewUserControllerByEnum(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("liComponentCode", cp.hasValue(carrier, "liComponentCode"));
        carrier.addController(EntityCrUserController.FK_COMPONENT_ID,
                cp.hasValue(carrier, EntityCrUserController.FK_COMPONENT_ID));
        carrier.addController(EntityCrUserController.FK_USER_ID,
                cp.hasValue(carrier, EntityCrUserController.FK_USER_ID));
        return carrier;

    }

    public Carrier insertNewAttribute(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrAttribute.ATTRIBUTE_NAME,
                cp.hasValue(carrier, EntityCrAttribute.ATTRIBUTE_NAME));
        carrier.addController(EntityCrAttribute.ATTRIBUTE_CODE,
                cp.hasValue(carrier, EntityCrAttribute.ATTRIBUTE_CODE));
        carrier.addController(EntityCrAttribute.ATTRIBUTE_NAME,
                cp.hasValue(carrier, EntityCrAttribute.LANG));

        return carrier;
    }

    public Carrier updateAttribute(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrAttribute.ID,
                cp.hasValue(carrier, EntityCrAttribute.ID));
        carrier.addController(EntityCrAttribute.ATTRIBUTE_NAME,
                cp.hasValue(carrier, EntityCrAttribute.ATTRIBUTE_NAME));
        carrier.addController(EntityCrAttribute.ATTRIBUTE_CODE,
                cp.hasValue(carrier, EntityCrAttribute.ID));
        carrier.addController(EntityCrAttribute.ATTRIBUTE_NAME,
                cp.hasValue(carrier, EntityCrAttribute.LANG));

        return carrier;
    }

    public Carrier deleteAttribute(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrAttribute.ID,
                cp.hasValue(carrier, EntityCrAttribute.ID));

        return carrier;
    }

    public Carrier getAttributeList(Carrier carrier) {
        return carrier;
    }

    public Carrier getAttributeMainList(Carrier carrier) {
        return carrier;
    }

    public Carrier insertNewAppointment(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, EntityCrAppointment.FK_DOCTOR_USER_ID));
        carrier.addController("general",
                cp.hasValue(carrier, EntityCrAppointment.FK_PATIENT_ID));
        carrier.addController("general",
                cp.hasValue(carrier, "isNow"));
        if (carrier.isKeyExist("isNow") && carrier.getValue("isNow").toString().equals("false")) {
            carrier.addController("general",
                    cp.hasValue(carrier, EntityCrAppointment.APPOINTMENT_TIME_1));
            carrier.addController("general",
                    cp.hasValue(carrier, EntityCrAppointment.APPOINTMENT_TIME_2));
            carrier.addController("general",
                    cp.hasValue(carrier, EntityCrAppointment.APPOINTMENT_DATE));
            if (carrier.getValue(EntityCrAppointment.APPOINTMENT_TIME_1).toString().compareTo(
                    carrier.getValue(EntityCrAppointment.APPOINTMENT_TIME_2).toString()) > 0) {
                carrier.addController("general", EntityManager.getMessageText("time1ShoudbeGEtime2"));
            }
        }

        return carrier;
    }

    public Carrier updateAppointment(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrAppointment.ID,
                cp.hasValue(carrier, EntityCrAppointment.ID));
        carrier.addController(EntityCrAppointment.FK_DOCTOR_USER_ID,
                cp.hasValue(carrier, EntityCrAppointment.FK_DOCTOR_USER_ID));
        carrier.addController(EntityCrAppointment.FK_PATIENT_ID,
                cp.hasValue(carrier, EntityCrAppointment.FK_PATIENT_ID));
        carrier.addController(EntityCrAppointment.APPOINTMENT_TIME_1,
                cp.hasValue(carrier, EntityCrAppointment.APPOINTMENT_TIME_1));
        carrier.addController(EntityCrAppointment.APPOINTMENT_TIME_2,
                cp.hasValue(carrier, EntityCrAppointment.APPOINTMENT_TIME_2));
        carrier.addController(EntityCrAppointment.APPOINTMENT_DATE,
                cp.hasValue(carrier, EntityCrAppointment.APPOINTMENT_DATE));

        return carrier;
    }

    public Carrier deleteAppointment(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrAppointment.ID,
                cp.hasValue(carrier, EntityCrAppointment.ID));

        return carrier;
    }

    public Carrier getAppointmentList(Carrier carrier) {
        return carrier;
    }

    public Carrier insertNewModule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrModule.MODULE_NAME,
                cp.hasValue(carrier, EntityCrModule.MODULE_NAME));
        carrier.addController(EntityCrModule.LI_MODULE_STATUS,
                cp.hasValue(carrier, EntityCrModule.LI_MODULE_STATUS));
        carrier.addController(EntityCrModule.MODULE_NAME,
                cp.hasValue(carrier, EntityCrModule.LANG));

        return carrier;
    }

    public Carrier updateModule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, "moduleUniqueId"));
        carrier.addController(EntityCrModule.MODULE_NAME,
                cp.hasValue(carrier, EntityCrModule.MODULE_NAME));
        carrier.addController(EntityCrModule.LI_MODULE_STATUS,
                cp.hasValue(carrier, EntityCrModule.LI_MODULE_STATUS));
        carrier.addController(EntityCrModule.MODULE_NAME,
                cp.hasValue(carrier, EntityCrModule.LANG));

        return carrier;
    }

    public Carrier deleteModule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrModule.ID,
                cp.hasValue(carrier, EntityCrModule.ID));

        return carrier;
    }

    public Carrier getPage(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("page",
                cp.hasValue(carrier, "page"));

        return carrier;
    }

    public Carrier getModuleList(Carrier carrier) {
        return carrier;
    }

        public Carrier getModuleMainList(Carrier carrier) {
        return carrier;
    }
        
    public Carrier insertNewSubmodule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrSubmodule.SUBMODULE_NAME,
                cp.hasValue(carrier, EntityCrSubmodule.SUBMODULE_NAME));
        carrier.addController(EntityCrSubmodule.FK_MODULE_ID,
                cp.hasValue(carrier, EntityCrSubmodule.FK_MODULE_ID));

        return carrier;
    }

    public Carrier updateSubmodule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrSubmodule.ID,
                cp.hasValue(carrier, EntityCrSubmodule.ID));
        carrier.addController(EntityCrSubmodule.SUBMODULE_NAME,
                cp.hasValue(carrier, EntityCrSubmodule.SUBMODULE_NAME));
        carrier.addController(EntityCrSubmodule.FK_MODULE_ID,
                cp.hasValue(carrier, EntityCrSubmodule.FK_MODULE_ID));

        return carrier;
    }

    public Carrier deleteSubmodule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrSubmodule.ID,
                cp.hasValue(carrier, EntityCrSubmodule.ID));

        return carrier;
    }

    public Carrier getSubmoduleList(Carrier carrier) {
        return carrier;
    }

    public Carrier getSubmoduleMainList(Carrier carrier) {
        return carrier;
    }

    public Carrier insertNewOrganPoint(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrOrganPoint.ORGAN_POINT_NAME,
                cp.hasValue(carrier, EntityCrOrganPoint.ORGAN_POINT_NAME));

        return carrier;
    }

    public Carrier updateOrganPoint(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrOrganPoint.ID,
                cp.hasValue(carrier, EntityCrOrganPoint.ID));
        carrier.addController(EntityCrOrganPoint.ORGAN_POINT_NAME,
                cp.hasValue(carrier, EntityCrOrganPoint.ORGAN_POINT_NAME));
        return carrier;
    }

    public Carrier deleteOrganPoint(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrOrganPoint.ID,
                cp.hasValue(carrier, EntityCrOrganPoint.ID));

        return carrier;
    }

    public Carrier getOrganPointList(Carrier carrier) {
        return carrier;
    }

    public Carrier insertNewValueType(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrValueType.VALUE_TYPE_NAME,
                cp.hasValue(carrier, EntityCrValueType.VALUE_TYPE_NAME));

        return carrier;
    }

    public Carrier updateValueType(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrValueType.ID,
                cp.hasValue(carrier, EntityCrValueType.ID));
        carrier.addController(EntityCrValueType.VALUE_TYPE_NAME,
                cp.hasValue(carrier, EntityCrValueType.VALUE_TYPE_NAME));
        return carrier;
    }

    public Carrier deleteValueType(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrValueType.ID,
                cp.hasValue(carrier, EntityCrValueType.ID));
        return carrier;
    }

    public Carrier getValueTypeList(Carrier carrier) {
        return carrier;
    }

    public Carrier insertNewSubmoduleAttribute(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("fkModuleId",
                cp.hasValue(carrier, "fkModuleId"));
        carrier.addController(EntityCrSubmoduleAttribute.SUBMODULE_VALUE,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.SUBMODULE_VALUE));
        carrier.addController(EntityCrSubmoduleAttribute.FK_ATTRIBUTE_ID,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.FK_ATTRIBUTE_ID));
        carrier.addController(EntityCrSubmoduleAttribute.FK_ORGAN_POINT_ID,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.FK_ORGAN_POINT_ID));
        carrier.addController(EntityCrSubmoduleAttribute.FK_SUBMODULE_ID,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.FK_SUBMODULE_ID));
        carrier.addController(EntityCrSubmoduleAttribute.FK_VALUE_TYPE_ID,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.FK_VALUE_TYPE_ID));

        return carrier;
    }

    public Carrier updateSubmoduleAttribute(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrSubmoduleAttribute.SUBMODULE_VALUE,
                cp.hasValue(carrier, "id"));
        carrier.addController(EntityCrSubmoduleAttribute.SUBMODULE_VALUE,
                cp.hasValue(carrier, "fkModuleId"));
        carrier.addController(EntityCrSubmoduleAttribute.SUBMODULE_VALUE,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.SUBMODULE_VALUE));
        carrier.addController(EntityCrSubmoduleAttribute.FK_ATTRIBUTE_ID,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.FK_ATTRIBUTE_ID));
        carrier.addController(EntityCrSubmoduleAttribute.FK_ORGAN_POINT_ID,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.FK_ORGAN_POINT_ID));
        carrier.addController(EntityCrSubmoduleAttribute.FK_SUBMODULE_ID,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.FK_SUBMODULE_ID));
        carrier.addController(EntityCrSubmoduleAttribute.FK_VALUE_TYPE_ID,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.FK_VALUE_TYPE_ID));
        return carrier;
    }

    public Carrier deleteSubmoduleAttribute(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrSubmoduleAttribute.ID,
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.ID));
        return carrier;
    }

    public Carrier getSubmoduleAttributeList(Carrier carrier) {
        return carrier;
    }

    public Carrier getSubmoduleFormBody(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, EntityCrSubmoduleAttribute.FK_SUBMODULE_ID));
        carrier.addController("general",
                cp.hasValue(carrier, "fkSessionId"));
        return carrier;
    }

    public Carrier insertNewPatient(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPatient.PATIENT_NAME,
                cp.hasValue(carrier, EntityCrPatient.PATIENT_NAME));
        carrier.addController(EntityCrPatient.PATIENT_SURNAME,
                cp.hasValue(carrier, EntityCrPatient.PATIENT_SURNAME));
        carrier.addController(EntityCrPatient.PATIENT_BIRTH_DATE,
                cp.hasValue(carrier, EntityCrPatient.PATIENT_BIRTH_DATE));
        carrier.addController(EntityCrPatient.PATIENT_BIRTH_DATE,
                cp.isValueNotNull(carrier, EntityCrPatient.PATIENT_BIRTH_DATE));
        carrier.addController(EntityCrPatient.SEX,
                cp.hasValue(carrier, EntityCrPatient.SEX));
        return carrier;
    }

    public Carrier updatePatient(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPatient.ID,
                cp.hasValue(carrier, EntityCrPatient.ID));
        carrier.addController(EntityCrPatient.PATIENT_NAME,
                cp.hasValue(carrier, EntityCrPatient.PATIENT_NAME));
        carrier.addController(EntityCrPatient.PATIENT_SURNAME,
                cp.hasValue(carrier, EntityCrPatient.PATIENT_SURNAME));
        carrier.addController(EntityCrPatient.PATIENT_BIRTH_DATE,
                cp.hasValue(carrier, EntityCrPatient.PATIENT_BIRTH_DATE));
        carrier.addController(EntityCrPatient.PATIENT_BIRTH_DATE,
                cp.isValueNotNull(carrier, EntityCrPatient.PATIENT_BIRTH_DATE));
        carrier.addController(EntityCrPatient.SEX,
                cp.hasValue(carrier, EntityCrPatient.SEX));
        return carrier;
    }

    public Carrier deletePatient(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPatient.ID,
                cp.hasValue(carrier, EntityCrPatient.ID));
        return carrier;
    }

    public Carrier getPatientList(Carrier carrier) {
        return carrier;
    }

    public Carrier insertNewInspection(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, EntityCrInspection.FK_PATIENT_ID));
        carrier.addController("general",
                cp.hasValue(carrier, "fkSessionId"));
        return carrier;
    }

    public Carrier updateInspection(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrInspection.FK_PATIENT_ID,
                cp.hasValue(carrier, EntityCrInspection.FK_PATIENT_ID));
        carrier.addController(EntityCrInspection.FK_PATIENT_ID,
                cp.hasValue(carrier, EntityCrInspection.INSPECTION_CODE));
        carrier.addController(EntityCrInspection.FK_PATIENT_ID,
                cp.isExistInEntity(new EntityCrInspection(), "inspectionCode",
                        carrier.getValue("inspectionCode").toString()));
        return carrier;
    }

    public Carrier deleteInspection(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrInspection.ID,
                cp.hasValue(carrier, EntityCrInspection.ID));
        return carrier;
    }

    public Carrier getInspectionList(Carrier carrier) {
        return carrier;
    }

    public Carrier getInspectionListBySession(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, "fkSessionId"));
        carrier.addController("general",
                cp.hasValue(carrier, "fkSubmoduleId"));
        carrier.addController("general",
                cp.isExistInEntity(new EntityCrAppointment(),
                        "id", carrier.getValue("fkSessionId").toString()));
        return carrier;
    }

    public static Carrier getSubmoduleAttributeList4Matrix(Carrier carrier)
            throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, "fkMainModuleId"));
        return carrier;
    }

    public Carrier getInspectionCodeList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrInspection.FK_PATIENT_ID,
                cp.hasValue(carrier, EntityCrInspection.FK_PATIENT_ID));
        carrier.addController(EntityCrInspectionList.FK_MODULE_ID,
                cp.hasValue(carrier, EntityCrInspectionList.FK_MODULE_ID));
        return carrier;
    }

    public static Carrier genSubmoduleButtonList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, EntityCrInspectionList.FK_MODULE_ID));
        carrier.addController("general",
                cp.isExistInEntity(new EntityCrModule(), "id", carrier.getValue("fkModuleId").toString()));

        return carrier;
    }

    public static Carrier getNewSInspectionCode(Carrier carrier) {
        return carrier;
    }

    public Carrier insertNewReportLine(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrReportLine.FK_MODULE_ID,
                cp.hasValue(carrier, EntityCrReportLine.FK_MODULE_ID));
        carrier.addController(EntityCrReportLine.REPORT_NAME,
                cp.hasValue(carrier, EntityCrReportLine.REPORT_NAME));
        carrier.addController(EntityCrReportLine.REPORT_HTML,
                cp.hasValue(carrier, EntityCrReportLine.REPORT_HTML));
        return carrier;
    }

    public Carrier updateReportLine(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrReportLine.ID,
                cp.hasValue(carrier, EntityCrReportLine.ID));
        carrier.addController(EntityCrReportLine.FK_MODULE_ID,
                cp.hasValue(carrier, EntityCrReportLine.FK_MODULE_ID));
        carrier.addController(EntityCrReportLine.REPORT_NAME,
                cp.hasValue(carrier, EntityCrReportLine.REPORT_NAME));
        carrier.addController(EntityCrReportLine.REPORT_HTML,
                cp.hasValue(carrier, EntityCrReportLine.REPORT_HTML));
        return carrier;
    }

    public Carrier deleteReportLine(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrReportLine.ID,
                cp.hasValue(carrier, EntityCrReportLine.ID));
        return carrier;
    }

    public Carrier getReportLineList(Carrier carrier) {
        return carrier;
    }

    public Carrier getReportLineList4Print(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general", cp.hasValue(carrier, "id"));
        carrier.addController("general", cp.hasValue(carrier, "fkModuleId"));
        carrier.addController("general", cp.hasValue(carrier, "fkSessionId"));
//        carrier.addController("general",
//                cp.isExistInEntity(new EntityCrInspection(),
//                        "inspectionCode", carrier.getValue("inspectionCode").toString()));
        carrier.addController("general",
                cp.isExistInEntity(new EntityCrModule(),
                        "fkModuleId", carrier.getValue("fkModuleId").toString()));
        return carrier;
    }

    public Carrier getAgendaOfDoctor(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general", cp.hasValue(carrier, "id"));
        return carrier;
    }

    public Carrier finishSession(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general", cp.hasValue(carrier, "id"));
//        carrier.addController("general",
//                cp.isExistInEntity(new EntityCrInspection(),
//                        "inspectionCode", carrier.getValue("inspectionCode").toString()));
        carrier.addController("general",
                cp.isExistInEntity(new EntityCrAppointment(),
                        "id", carrier.getValue("id").toString()));
        return carrier;
    }

    public Carrier insertNewInspectionMatrix(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("matrixName",
                cp.hasValue(carrier, "matrixName"));
        return carrier;
    }

    public Carrier deleteInspectionMatrix(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrInspectionMatrix.ID,
                cp.hasValue(carrier, EntityCrInspectionMatrix.ID));
        return carrier;
    }

    public Carrier getInspectionMatrixList(Carrier carrier) {
        return carrier;
    }

    public Carrier getInspectionMatrixMainList(Carrier carrier) {
        return carrier;
    }

    public Carrier getInspectionMatrixBodyList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();

        return carrier;
    }

    public static Carrier getMessageText(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, "messageCode"));
        return carrier;
    }

    public Carrier insertNewPriceList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPriceList.PAYMENT_NAME,
                cp.hasValue(carrier, EntityCrPriceList.PAYMENT_NAME));
        carrier.addController(EntityCrPriceList.PRICE,
                cp.hasValue(carrier, EntityCrPriceList.PRICE));
        carrier.addController(EntityCrPriceList.LIST_STATUS,
                cp.hasValue(carrier, EntityCrPriceList.LIST_STATUS));
        carrier.addController(EntityCrPriceList.PRICE,
                cp.isFloat(carrier, EntityCrPriceList.PRICE));

        return carrier;
    }

    public Carrier updatePriceList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPriceList.ID,
                cp.hasValue(carrier, EntityCrPriceList.ID));
        carrier.addController(EntityCrPriceList.PAYMENT_NAME,
                cp.hasValue(carrier, EntityCrPriceList.PAYMENT_NAME));
        carrier.addController(EntityCrPriceList.PRICE,
                cp.hasValue(carrier, EntityCrPriceList.PRICE));
        carrier.addController(EntityCrPriceList.LIST_STATUS,
                cp.hasValue(carrier, EntityCrPriceList.LIST_STATUS));
        carrier.addController(EntityCrPriceList.PRICE,
                cp.isFloat(carrier, EntityCrPriceList.PRICE));

        return carrier;
    }

    public Carrier deletePriceList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPriceList.ID,
                cp.hasValue(carrier, EntityCrPriceList.ID));

        return carrier;
    }

    public Carrier getPriceListList(Carrier carrier) {
        return carrier;
    }

    public Carrier getCurrencyOfCompany(Carrier carrier) {
        return carrier;
    }

    public Carrier getDoctorList(Carrier carrier) {
        return carrier;
    }

    public Carrier insertNewPayment(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPayment.FK_PATIENT_ID,
                cp.hasValue(carrier, EntityCrPayment.FK_PATIENT_ID));
        carrier.addController(EntityCrPayment.FK_PRICE_LIST_ID,
                cp.hasValue(carrier, EntityCrPayment.FK_PRICE_LIST_ID));
        carrier.addController(EntityCrPayment.FK_DOCTOR_USER_ID,
                cp.hasValue(carrier, EntityCrPayment.FK_DOCTOR_USER_ID));
        carrier.addController(EntityCrPayment.PAYMENT_AMOUNT,
                cp.hasValue(carrier, EntityCrPayment.PAYMENT_AMOUNT));
        carrier.addController(EntityCrPayment.PAYMENT_STATUS,
                cp.hasValue(carrier, EntityCrPayment.PAYMENT_STATUS));
        carrier.addController(EntityCrPayment.PAYMENT_DISCOUNT,
                cp.hasValue(carrier, EntityCrPayment.PAYMENT_DISCOUNT));
        carrier.addController(EntityCrPayment.PAYMENT_DISCOUNT,
                cp.isPercent(carrier, EntityCrPayment.PAYMENT_DISCOUNT));

        return carrier;
    }

    public Carrier updatePayment(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPayment.ID,
                cp.hasValue(carrier, EntityCrPayment.ID));
        carrier.addController(EntityCrPayment.FK_PATIENT_ID,
                cp.hasValue(carrier, EntityCrPayment.FK_PATIENT_ID));
        carrier.addController(EntityCrPayment.FK_PRICE_LIST_ID,
                cp.hasValue(carrier, EntityCrPayment.FK_PRICE_LIST_ID));
        carrier.addController(EntityCrPayment.FK_DOCTOR_USER_ID,
                cp.hasValue(carrier, EntityCrPayment.FK_DOCTOR_USER_ID));
        carrier.addController(EntityCrPayment.PAYMENT_AMOUNT,
                cp.hasValue(carrier, EntityCrPayment.PAYMENT_AMOUNT));
        carrier.addController(EntityCrPayment.PAYMENT_STATUS,
                cp.hasValue(carrier, EntityCrPayment.PAYMENT_STATUS));
        carrier.addController(EntityCrPayment.PAYMENT_DISCOUNT,
                cp.hasValue(carrier, EntityCrPayment.PAYMENT_DISCOUNT));
        carrier.addController(EntityCrPayment.PAYMENT_DISCOUNT,
                cp.isPercent(carrier, EntityCrPayment.PAYMENT_DISCOUNT));
        return carrier;
    }

    public Carrier deletePayment(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPayment.ID,
                cp.hasValue(carrier, EntityCrPayment.ID));

        return carrier;
    }

    public Carrier getPaymentList(Carrier carrier) {
        return carrier;
    }

    public Carrier insertNewExpense(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrExpense.EXPENSE_AMOUNT,
                cp.hasValue(carrier, EntityCrExpense.EXPENSE_AMOUNT));
        carrier.addController(EntityCrExpense.EXPENSE_AMOUNT,
                cp.isFloat(carrier, EntityCrExpense.EXPENSE_AMOUNT));
        carrier.addController(EntityCrExpense.EXPENSE_DATE,
                cp.hasValue(carrier, EntityCrExpense.EXPENSE_DATE));
        return carrier;
    }

    public Carrier updateExpense(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrExpense.ID,
                cp.hasValue(carrier, EntityCrExpense.ID));
        carrier.addController(EntityCrExpense.EXPENSE_AMOUNT,
                cp.hasValue(carrier, EntityCrExpense.EXPENSE_AMOUNT));
        carrier.addController(EntityCrExpense.EXPENSE_AMOUNT,
                cp.isFloat(carrier, EntityCrExpense.EXPENSE_AMOUNT));
        carrier.addController(EntityCrExpense.EXPENSE_DATE,
                cp.hasValue(carrier, EntityCrExpense.EXPENSE_DATE));

        return carrier;
    }

    public Carrier deleteExpense(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrExpense.ID,
                cp.hasValue(carrier, EntityCrExpense.ID));

        return carrier;
    }

	public Carrier getDiscountedPrice(Carrier carrier) throws QException {
        return carrier;
    }

    

    public Carrier getExpenseList(Carrier carrier) {
        return carrier;
    }

    public Carrier getIncomeReportList(Carrier carrier) {
        return carrier;
    }

    public Carrier getUserFullname(Carrier carrier) {
        return carrier;
    }

public Carrier getLabel(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, "code"));
        return carrier;
    }
    
    public static Carrier getVoiceAnalyse(Carrier carrier)  throws QException{
        ControllerPool cp = new ControllerPool();
        carrier.addController("general",
                cp.hasValue(carrier, "filename"));
        return carrier;
    }
    
     public static Carrier getBasicStatistics(Carrier carrier)  throws QException{
        ControllerPool cp = new ControllerPool();
//        carrier.addController("general",
//                cp.hasValue(carrier, "fkMatrixId"));
//        carrier.addController("general",
//                cp.hasValue(carrier, "fkSubmoduleAttributeId"));
        return carrier;
    }
    
    public Carrier signupCompany(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrCompany.COMPANY_NAME, cp.hasValue(carrier, EntityCrCompany.COMPANY_NAME));
            carrier.addController(EntityCrCompany.COMPANY_DOMAIN, cp.hasValue(carrier, EntityCrCompany.COMPANY_DOMAIN));
            carrier.addController(EntityCrCompany.COMPANY_COUNTRY, cp.hasValue(carrier, EntityCrCompany.COMPANY_COUNTRY));
            carrier.addController(EntityCrCompany.COMPANY_ADDRESS, cp.hasValue(carrier, EntityCrCompany.COMPANY_ADDRESS));
            
            carrier.addController(EntityCrCompany.COMPANY_DOMAIN,
                cp.isNotExistInEntity(new EntityCrCompany(), EntityCrCompany.COMPANY_DOMAIN,
                        carrier.getValue(EntityCrCompany.COMPANY_DOMAIN).toString()));
            
            carrier.addController(EntityCrCompany.COMPANY_NAME,
                cp.isNotExistInEntity(new EntityCrCompany(), EntityCrCompany.COMPANY_NAME,
                        carrier.getValue(EntityCrCompany.COMPANY_NAME).toString()));
            
            carrier.addController(EntityCrUser.USERNAME, cp.hasValue(carrier, EntityCrUser.USERNAME));
            carrier.addController(EntityCrUser.EMAIL_1, cp.hasValue(carrier, EntityCrUser.EMAIL_1));
            carrier.addController(EntityCrUser.PASSWORD, cp.hasValue(carrier, EntityCrUser.PASSWORD));
            carrier.addController(EntityCrUser.USER_PERSON_NAME, cp.hasValue(carrier, EntityCrUser.USER_PERSON_NAME));
            carrier.addController(EntityCrUser.USER_PERSON_SURNAME, cp.hasValue(carrier, EntityCrUser.USER_PERSON_SURNAME));
            
            carrier.addController(EntityCrUser.SEX, cp.hasValue(carrier, EntityCrUser.SEX));
            carrier.addController(EntityCrUser.MOBILE_1, cp.hasValue(carrier, EntityCrUser.MOBILE_1));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public Carrier signupPersonal(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            carrier.addController(EntityCrUser.USERNAME, cp.hasValue(carrier, EntityCrUser.USERNAME));
            carrier.addController(EntityCrUser.USERNAME,
                cp.isNotExistInEntity(new EntityCrUser(), EntityCrUser.USERNAME,
                        carrier.getValue(EntityCrUser.USERNAME).toString()));
            
            carrier.addController(EntityCrUser.EMAIL_1, cp.hasValue(carrier, EntityCrUser.EMAIL_1));
            carrier.addController(EntityCrUser.PASSWORD, cp.hasValue(carrier, EntityCrUser.PASSWORD));
            carrier.addController(EntityCrUser.USER_PERSON_NAME, cp.hasValue(carrier, EntityCrUser.USER_PERSON_NAME));
            carrier.addController(EntityCrUser.USER_PERSON_SURNAME, cp.hasValue(carrier, EntityCrUser.USER_PERSON_SURNAME));
            carrier.addController(EntityCrCompany.COMPANY_COUNTRY, cp.hasValue(carrier, EntityCrCompany.COMPANY_COUNTRY));
            carrier.addController(EntityCrUser.SEX, cp.hasValue(carrier, EntityCrUser.SEX));
            carrier.addController(EntityCrUser.MOBILE_1, cp.hasValue(carrier, EntityCrUser.MOBILE_1));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public Carrier activateCompany(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            //carrier.addController(EntityCrUser.PASSWORD, cp.hasValue(carrier, EntityCrUser.PASSWORD));
            carrier.addController(EntityCrCompany.ACTIVATION_ID, cp.hasValue(carrier, EntityCrCompany.ACTIVATION_ID));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public Carrier resendEmail(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            //carrier.addController(EntityCrUser.PASSWORD, cp.hasValue(carrier, EntityCrUser.PASSWORD));
            //carrier.addController(EntityCrUser.USERNAME, cp.hasValue(carrier, EntityCrUser.USERNAME));
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public Carrier getPermissionList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public Carrier insertNewPermission(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPermission.PERMISSION_STRING,
                cp.hasValue(carrier, EntityCrPermission.PERMISSION_STRING));

        return carrier;
    }

    public Carrier updatePermission(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPermission.ID,
                cp.hasValue(carrier, EntityCrPermission.ID));
        carrier.addController(EntityCrPermission.PERMISSION_STRING,
                cp.hasValue(carrier, EntityCrPermission.PERMISSION_STRING));
        return carrier;
    }

    public Carrier deletePermission(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrPermission.ID,
                cp.hasValue(carrier, EntityCrPermission.ID));

        return carrier;
    }
    
    public Carrier getRuleList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public Carrier insertNewRule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRule.RULE_NAME,
                cp.hasValue(carrier, EntityCrRule.RULE_NAME));

        return carrier;
    }

    public Carrier updateRule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRule.ID,
                cp.hasValue(carrier, EntityCrRule.ID));
        carrier.addController(EntityCrRule.RULE_NAME,
                cp.hasValue(carrier, EntityCrRule.RULE_NAME));
        return carrier;
    }

    public Carrier deleteRule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRule.ID,
                cp.hasValue(carrier, EntityCrRule.ID));

        return carrier;
    }
    
    public Carrier getRulePermissionList(Carrier carrier) throws QException {
        //ControllerPool cp = new ControllerPool();
        //carrier.addController(EntityCrRelRulePermission.FK_RULE_ID,
        //        cp.hasValue(carrier, EntityCrRelRulePermission.FK_RULE_ID));
        //carrier.addController(EntityCrRelRulePermission.FK_PERMISSION_ID,
        //        cp.hasValue(carrier, EntityCrRelRulePermission.FK_PERMISSION_ID));
        return carrier;
    }
    
    public Carrier assignPermissionRule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRelRulePermission.FK_RULE_ID,
                cp.hasValue(carrier, EntityCrRelRulePermission.FK_RULE_ID));
        carrier.addController(EntityCrRelRulePermission.FK_PERMISSION_ID,
                cp.hasValue(carrier, EntityCrRelRulePermission.FK_PERMISSION_ID));
        return carrier;
    }
    
    public Carrier deleteRulePermission(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRelRulePermission.ID,
                cp.hasValue(carrier, EntityCrRelRulePermission.ID));
        
        return carrier;
    }
    
    
    
    
    
    public Carrier getRoleList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public Carrier insertNewRole(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRole.ROLE_NAME,
                cp.hasValue(carrier, EntityCrRole.ROLE_NAME));

        return carrier;
    }

    public Carrier updateRole(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRole.ID,
                cp.hasValue(carrier, EntityCrRole.ID));
        carrier.addController(EntityCrRole.ROLE_NAME,
                cp.hasValue(carrier, EntityCrRole.ROLE_NAME));
        return carrier;
    }

    public Carrier deleteRole(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRole.ID,
                cp.hasValue(carrier, EntityCrRole.ID));

        return carrier;
    }
    
    public Carrier getRoleRuleList(Carrier carrier) throws QException {
        //ControllerPool cp = new ControllerPool();
        //carrier.addController(EntityCrRelRulePermission.FK_RULE_ID,
        //        cp.hasValue(carrier, EntityCrRelRulePermission.FK_RULE_ID));
        //carrier.addController(EntityCrRelRulePermission.FK_PERMISSION_ID,
        //        cp.hasValue(carrier, EntityCrRelRulePermission.FK_PERMISSION_ID));
        return carrier;
    }
    
    public Carrier assignRuleRole(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRelRoleRule.FK_ROLE_ID,
                cp.hasValue(carrier, EntityCrRelRoleRule.FK_ROLE_ID));
        carrier.addController(EntityCrRelRoleRule.FK_RULE_ID,
                cp.hasValue(carrier, EntityCrRelRoleRule.FK_RULE_ID));
        return carrier;
    }
    
    public Carrier deleteRoleRule(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        carrier.addController(EntityCrRelRoleRule.ID,
                cp.hasValue(carrier, EntityCrRelRoleRule.ID));
        
        return carrier;
    }
    
    public Carrier getUserRuleList(Carrier carrier) throws QException {
        ControllerPool cp = new ControllerPool();
        try {
            return carrier;
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

}
