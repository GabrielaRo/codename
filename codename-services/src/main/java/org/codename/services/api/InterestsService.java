/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.api;

import org.codename.model.Interest;
import java.util.List;
import org.codename.services.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
public interface InterestsService {
    
    List<Interest> getAllInterests() throws ServiceException;
    
    Interest get(String interest) throws ServiceException;
    
    Interest newInterest(String interest) throws ServiceException;
    
    Interest newInterest(String interest, String imagePath) throws ServiceException;
    
}
