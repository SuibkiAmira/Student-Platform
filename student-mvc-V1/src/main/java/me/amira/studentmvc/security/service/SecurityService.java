package me.amira.studentmvc.security.service;


import me.amira.studentmvc.security.entities.AppRole;
import me.amira.studentmvc.security.entities.AppUser;

public interface SecurityService {
    AppUser saveNewUser(String username, String password, String rePwd);
    AppRole saveNewRole(String roleName, String description);
    void addRoleToUser(String username, String roleName);
    void removeRoleFromUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
}
