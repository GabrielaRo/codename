/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.api;

import java.util.List;
import org.codename.model.User;
import org.codename.services.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
public interface UsersQueryService {

    

    List<User> getAll() throws ServiceException;
    
   

   

}
