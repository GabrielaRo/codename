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


    List<User> search(Double lon, Double lat, Double offsetRange, Double limitRange, 
            List<String> interests, List<String> lookingFors, List<String> iams, 
            Integer offset, Integer limit, List<String> exludes) throws ServiceException;
    
    List<Object> searchWithScore(Double lon, Double lat, Double offsetRange, Double limitRange, List<String> interests,
            List<String> lookingFors, List<String> iAms, Integer offset, Integer limit, List<String> excludes) throws ServiceException; 

}
