/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.util;

import java.util.List;
import javax.inject.Inject;
import org.codename.core.api.UsersService;
import org.codename.model.User;

/**
 *
 * @author salaboy
 */
public class UsersExporterServiceImpl implements UsersExporterImporterService {
    @Inject
    private UsersService usersService;
    
    public String exportToJson() {
         List<User> all = usersService.getAll();
//        Json.
        return "";
    }

    public void importFromJson(String content ) {
        
       // usersService.newUser(user)
        
    }
    
}
