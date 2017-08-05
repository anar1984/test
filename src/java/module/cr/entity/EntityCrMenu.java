/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package module.cr.entity;

import utility.CoreEntity;

/**
 *
 * @author user
 */
public class EntityCrMenu extends CoreEntity {

   

    //////////////////////=mandatory
    public static  String ID = "id";
    public static  String STATUS = "status";
    public static  String INSERT_DATE = "insertDate";
    public static  String MODIFICATION_DATE = "modificationDate";
    public static  String NAME = "name";
    public static  String PARENT_ID = "parentId";
    public static  String MENU_URL = "url";
    public static  String MENU_ORDER = "order";
    public static  String MODULE = "module";

    private String name = "";
//    private String classLevel=""; //menu 3 sinifden ibaretdir. En uste gelen 1.sinif, ortada gelen 2.sinif ve 
//                                // viewlara giris etmek ucun 3.sinif menular
//    private String menuLevel=""; // aid oldugu sinifde necenci seviyyeli altmenudur.
    private String parentId = "";
    private String menuUrl = "";
    private String menuOrder = ""; //yerlesdiyi levelde necenci sirada oldugnuu gosterir
    private String module = "";

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMenuOrder() {
        return menuOrder;
    }

    public void setOrder(String order) {
        this.menuOrder = order;
    }

    public String getUrl() {
        return menuUrl;
    }

    public void setUrl(String url) {
        this.menuUrl = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    
}
