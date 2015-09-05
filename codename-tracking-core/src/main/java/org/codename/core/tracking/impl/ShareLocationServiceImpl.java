/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tracking.impl;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.codename.core.tracking.api.ShareLocationService;
import org.codename.core.util.PersistenceManager;
import org.codename.model.tracking.SharedLocation;
import org.hibernate.service.spi.ServiceException;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class ShareLocationServiceImpl implements ShareLocationService {

    @Inject
    private PersistenceManager pm;


    @Override
    public void shareLocation(Long userId, Double latitude, Double longitude, String description) throws ServiceException {
        pm.persist(new SharedLocation(userId, latitude, longitude, description));
    }

    @Override
    public List<SharedLocation> getSharedLocations(Long userId) throws ServiceException {
        return pm.createNamedQuery("SharedLocations.getByUserId", SharedLocation.class)
                .setParameter("userId", userId)
////                .setFirstResult(offset)
////                .setMaxResults(limit)
                .getResultList();
    }
    
    

    

}
