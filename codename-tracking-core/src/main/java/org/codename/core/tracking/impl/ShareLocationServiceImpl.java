/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tracking.impl;

import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.TemporalType;
import org.codename.core.tracking.api.ShareLocationService;
import org.codename.core.util.PersistenceManager;
import org.codename.model.tracking.SharedLocation;
import org.hibernate.service.spi.ServiceException;
import org.joda.time.DateTime;

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
        
        DateTime now = DateTime.now( );
        DateTime todayStart = now.withTimeAtStartOfDay();
        List<SharedLocation> sharedLocations = pm.createNamedQuery("SharedLocations.getByTime", SharedLocation.class)
                .setParameter("userId", userId)
                .setParameter("description", description)
                .setParameter("now", new Date())
                .setParameter("startOfToday",todayStart.toDate()).getResultList();
        if (sharedLocations == null || sharedLocations.isEmpty()) {
            pm.persist(new SharedLocation(userId, latitude, longitude, description));
        } else {
            SharedLocation lastLocationShared = sharedLocations.get(0);
            lastLocationShared.setTimestamp(new Date());
            pm.merge(lastLocationShared);
        }
    }

    @Override
    public List<SharedLocation> getSharedLocations(Long userId) throws ServiceException {
        return pm.createNamedQuery("SharedLocations.getByUserId", SharedLocation.class)
                .setParameter("userId", userId)
                ////                .setFirstResult(offset)
                ////                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public void removeSharedLocation(Long locationId) throws ServiceException {
        SharedLocation location = pm.find(SharedLocation.class, locationId);
        if (location == null) {
            throw new ServiceException("Location with id " + locationId + " not found!");
        }
        pm.remove(location);
    }

    @Override
    public void clearAllSharedLocations(Long userId) throws ServiceException {
        List<SharedLocation> sharedLocations = pm.createNamedQuery("SharedLocations.getByUserId", SharedLocation.class)
                .setParameter("userId", userId)
                .getResultList();
        for (SharedLocation l : sharedLocations) {
            pm.remove(l);
        }
    }

}
