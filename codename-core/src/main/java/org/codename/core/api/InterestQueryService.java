/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.api;

import java.util.List;
import org.codename.core.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
public interface InterestQueryService {
    List<String> getAll() throws ServiceException;
}
