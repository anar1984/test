/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility.qreport;

import label.CoreLabel;
import module.cr.CrModel;
import module.cr.entity.EntityCrPatient;
import module.cr.entity.EntityCrPatientList;
import utility.Carrier;
import utility.QException;
import utility.sqlgenerator.EntityManager;

/**
 *
 * @author Lenovo
 */
public class QRPatient {

    private String name = "";
    private String surname = "";
    private String middleName = "";
    private String birthDate = "";
    private String birthPlace = "";
    private String sex = "";
    private String sexName = "";
    private String occupation = "";
    private String maritualStatus = "";
    private String education = "";
    private String bloodGroup = "";
    private String rhFactor = "";
    private String mobile1 = "";
    private String mobile2 = "";
    private String telephone1 = "";
    private String telephone2 = "";
    private String email1 = "";
    private String email2 = "";
    private String country = "";
    private String city = "";
    private String postIndex = "";
    private String description = "";

    public void setPatientInfoById(String patientId) throws QException {
        Carrier cr = new Carrier();
        EntityCrPatientList ent = new EntityCrPatientList();

        if (patientId.trim().length() > 0) {
            cr.setValue("id", patientId);
            cr = CrModel.getPatientList(cr);
            EntityManager.mapCarrierToEntity(cr, CoreLabel.RESULT_SET, 0, ent);
        }
        
        this.name = ent.getPatientName();
        this.surname = ent.getPatientSurname();
        this.middleName = ent.getPatientMiddleName();
        this.birthDate = ent.getPatientBirthDate();
        this.birthPlace = ent.getPatientBirthPlace();
        this.sex = ent.getSexName();
        this.occupation = ent.getOccupationName();
        this.maritualStatus = ent.getMaritualStatusName();
        this.education = ent.getEducationName();
        this.bloodGroup = ent.getBloodGroupName();
        this.rhFactor = ent.getRhFactorName();
        this.mobile1 = ent.getMobile1();
        this.mobile2 = ent.getMobile2();
        this.telephone1 = ent.getTelephone1();
        this.telephone2 = ent.getTelephone2();
        this.email1 = ent.getEmail1();
        this.email2 = ent.getEmail2();
        this.country = ent.getCountry();
        this.city = ent.getCity();
        this.postIndex = ent.getPostIndex();
        this.description = ent.getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMaritualStatus() {
        return maritualStatus;
    }

    public void setMaritualStatus(String maritualStatus) {
        this.maritualStatus = maritualStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getRhFactor() {
        return rhFactor;
    }

    public void setRhFactor(String rhFactor) {
        this.rhFactor = rhFactor;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(String postIndex) {
        this.postIndex = postIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
