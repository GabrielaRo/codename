/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.api;

import java.util.List;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.User;

/**
 *
 * @author grogdj
 */
public interface UsersQueryService {

    List<User> getAll(List<String> lookingFors, List<String> categories) throws ServiceException;

    List<User> getUserByRange(Double lon, Double lat, Double range, List<String> lookingFors, List<String> categories) throws ServiceException;

}
