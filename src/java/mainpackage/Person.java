/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainpackage;

/**
 *
 * @author Lenovo
 */
public class Person {
    String name;
    String surname;
    String address;
    int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String generateCode(String name){
        String res = "20170201F1";
        return res;
    }
}
