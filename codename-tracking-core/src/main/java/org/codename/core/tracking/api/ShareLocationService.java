/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tracking.api;

import java.util.List;
import org.codename.model.tracking.SharedLocation;
import org.hibernate.service.spi.ServiceException;

/**
 *
 * @author grogdj
 */
public interface ShareLocationService {

    public void shareLocation(Long userId, Double latitude, Double longitude, String description) throws ServiceException;

    public List<SharedLocation> getSharedLocations(Long userId) throws ServiceException;

    public void removeSharedLocation(Long locationId);

    public void clearAllSharedLocations(Long userId);

}
