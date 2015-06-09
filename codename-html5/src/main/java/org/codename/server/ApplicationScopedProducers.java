/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.grogshop.server;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class ApplicationScopedProducers {

    @PersistenceContext
    private EntityManager em;

    @Produces
    public EntityManager produceEntityManager() {
        return em;
    }
}
