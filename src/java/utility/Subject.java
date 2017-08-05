/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author nikli
 */
public class Subject {
    
    private Set<String> permissionList = new HashSet<>();
    private Set<String> ruleList = new HashSet<>();
    private Set<String> roleList = new HashSet<>();
    
    public void addRole(String roleName) {
        roleList.add(roleName);
    }
    
    public void addRule(String roleName) {
        ruleList.add(roleName);
    }
    
    public void addPermission(String permissionString) {
        permissionList.add(permissionString);
    }
    
    public boolean isPermitted(String permission) {
        return permissionList.contains("*:*")||permissionList.contains(permission)||permissionList.contains(permission+":*");
    }
    
    public boolean hasRole(String role) {
        return roleList.contains(role);
    }
    
    public boolean hasRule(String rule) {
        return ruleList.contains(rule);
    }
   
    
}
