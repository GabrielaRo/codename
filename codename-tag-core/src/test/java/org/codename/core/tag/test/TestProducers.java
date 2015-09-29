/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tag.test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author gabriela
 */
@ApplicationScoped
public class TestProducers {

    @Produces
    public EntityManager getEntityManager() {
        return Persistence.createEntityManagerFactory("primary").createEntityManager();
    }
}
