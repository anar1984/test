package controllerpool;

import java.util.Objects;
import utility.Carrier;
import utility.CoreEntity;
import utility.QException;
import utility.sqlgenerator.EntityManager;

public class ControllerPool {

    final String OK = "200";
    final int codeDogru = 200;
    final String SPACE = " ";

    public String isInteger(String value) throws QException {
        if (value.trim().equals("")) {
            return OK;
        }
        try {
            int i = Integer.valueOf(value);
            return OK;
        } catch (Exception e) {
            String s = EntityManager.getMessageText("isNotInteger");
            return s;
        }
    }

    public String isIntegerEqual(Integer value, Integer eq) throws QException {
        try {
            if (Objects.equals(value, eq)) {
                return OK;
            } else {
                String s = EntityManager.getMessageText("valueIsNotCorrect");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public String isDoubleEqual(double value, double eq) throws QException {
        try {
            if (Objects.equals(value, eq)) {
                return OK;
            } else {
                String s = EntityManager.getMessageText("valueIsNotCorrect");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String isFloat(String value) throws QException {
        if (value.trim().equals("")) {
            return OK;
        }
        try {
            double i = Double.valueOf(value);
            return OK;
        } catch (Exception e) {
            String s = EntityManager.getMessageText("isNotReal");
            return s;
        }
    }

    public String isPercent(String value) throws QException {
        if (value.trim().equals("")) {
            return OK;
        }
        try {
            double i = Double.valueOf(value);
            if ((i < 0) || (i > 100)) {
                String s = EntityManager.getMessageText("isNotPercent");
                return s;
            }
            return OK;
        } catch (Exception e) {
            String s = EntityManager.getMessageText("isNotPercent");
            return s;
        }
    }
    
    public String isPercent(Carrier carrier, String key) throws QException {
         return isPercent(carrier.getValue(key).toString());
    }

    public String isInteger(Carrier carrier, String key) throws QException {
        try {
            if (carrier.isKeyExist(key)) {
                return isInteger(carrier.getValue(key).toString());
            } else {
                String s = EntityManager.getMessageText("keyNotEntered");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String hasSpace(Carrier carrier, String key) throws QException {
        try {
            if (carrier.isKeyExist(key)) {
                return hasSpace(carrier.getValue(key).toString());
            } else {
                String s = EntityManager.getMessageText("keyNotEntered");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String isFloat(Carrier carrier, String key) throws QException {
        try {
            if (carrier.isKeyExist(key)) {
                return isFloat(carrier.getValue(key).toString());
            } else {
                String s = EntityManager.getMessageText("keyNotEntered");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String hasValue(String value) throws QException {

        try {
            if (!value.trim().equals("")) {
                return OK;
            } else {
                String s = EntityManager.getMessageText("valueNotEntered");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String hasSpace(String value) throws QException {

        try {
            if (value.trim().contains(SPACE)) {
                return OK;
            } else {
                String s = EntityManager.getMessageText("valueDoesntContainSpace");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String hasValue(Carrier carrier, String value) throws QException {
        return hasCarrierValue(carrier, value);
    }

    public String hasValueIfKeyExist(Carrier carrier, String value) throws QException {
        return carrier.isKeyExist(value) ? hasCarrierValue(carrier, value) : OK;
    }

    private String hasCarrierValue(Carrier carrier, String key) throws QException {
        try {
            if (carrier.isKeyExist(key)) {
                if (!carrier.getValue(key).toString().trim().equals("")) {
                    return OK;
                } else {
                    String s = EntityManager.getMessageText("valueNotEntered");
                    return s;
                }
            } else {
                String s = EntityManager.getMessageText("keyNotEntered");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }
    
    public String isValueNotNull(Carrier carrier, String key) throws QException {
        try {
            if (carrier.isKeyExist(key)) {
                if (carrier.getValue(key)==null || carrier.getValue(key).equals("null")) {
                    String s = EntityManager.getMessageText("valueIsNull");
                    return s;
                } else {
                    return OK;
                }
            } else {
                String s = EntityManager.getMessageText("keyNotEntered");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String hasCarrierTableValue(Carrier carrier, String tablename, int row, String col) throws QException {
        try {
            if (carrier.isKeyExist(tablename, row, col)) {
                if (!carrier.getValue(tablename, row, col).toString().trim().equals("")) {
                    return OK;
                } else {
                    String s = EntityManager.getMessageText("valueNotEntered");
                    return s;
                }
            } else {
                String s = EntityManager.getMessageText("keyNotEntered");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String isKeyExist(Carrier carrier, String key) throws QException {
        try {
            if (carrier.isKeyExist(key)) {
                return OK;
            } else {
                String s = EntityManager.getMessageText("keyNotEntered");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String isExistInEntity(CoreEntity entity, String key, String value) throws QException {
        Carrier carrier = new Carrier();
        carrier.setValue(key, value);
        return isExistInEntity(entity, carrier);
    }

    public String isExistInEntityExcept(CoreEntity entity, String key, String value, String exceptId) throws QException {
        Carrier carrier = new Carrier();
        carrier.setValue(key, value);
        return isExistInEntityExcept(entity, carrier, exceptId);
    }

    public String isNotExistInEntity(CoreEntity entity, String key, String value) throws QException {
        Carrier carrier = new Carrier();
        carrier.setValue(key, value);
        return isNotExistInEntity(entity, carrier);
    }

    public String isNotExistInEntityExcept(CoreEntity entity, String key, String value, String exceptId) throws QException {
        Carrier carrier = new Carrier();
        carrier.setValue(key, value);
        return isNotExistInEntityExcept(entity, carrier, exceptId);
    }

    public String isExistInEntity(CoreEntity entity, Carrier carrier) throws QException {
        try {
            EntityManager.mapCarrierToEntity(carrier, entity);
            Carrier temp = EntityManager.select(entity);

            if (temp.getTableRowCount(entity.toTableName()) == 0) {
                String s = EntityManager.getMessageText("valueIsNotExist");
                return s;
            } else {
                return OK;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String isExistInEntityExcept(CoreEntity entity, Carrier carrier, String exceptId) throws QException {
        try {
            EntityManager.mapCarrierToEntity(carrier, entity);
            entity.setId("NE%" + exceptId.trim());
            Carrier temp = EntityManager.select(entity);

            if (temp.getTableRowCount(entity.toTableName()) == 0) {
                String s = EntityManager.getMessageText("valueIsNotExist");
                return s;
            } else {
                return OK;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String isNotExistInEntity(CoreEntity entity, Carrier carrier) throws QException {
        try {
            EntityManager.mapCarrierToEntity(carrier, entity);
            Carrier temp = EntityManager.select(entity);

            if (temp.getTableRowCount(entity.toTableName()) == 0) {
                return OK;

            } else {
                String s = EntityManager.getMessageText("valueIsExist");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String isNotExistInEntityExcept(CoreEntity entity, Carrier carrier, String exceptId) throws QException {
        try {
            EntityManager.mapCarrierToEntity(carrier, entity);
            entity.setId("NE%" + exceptId.trim());
            Carrier temp = EntityManager.select(entity);

            if (temp.getTableRowCount(entity.toTableName()) == 0) {
                return OK;

            } else {
                String s = EntityManager.getMessageText("valueIsExist");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

    public String isValueSame(CoreEntity entity, String fieldname, String value, String id) throws QException {
        try {
            EntityManager.setEntityValue(entity, fieldname, value);
            entity.setId(id.trim());
            Carrier temp = EntityManager.select(entity);

            if (temp.getTableRowCount(entity.toTableName()) == 1) {
                return OK;
            } else {
                String s = EntityManager.getMessageText("valueIsNotTheSame");
                return s;
            }
        } catch (Exception ex) {
            throw new QException(new Object() {
            }.getClass().getEnclosingClass().getName(),
                    new Object() {
            }.getClass().getEnclosingMethod().getName(), ex);
        }
    }

}
