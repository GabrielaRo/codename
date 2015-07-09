/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.impl;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.lucene.search.Query;
import org.codename.model.User;
import org.codename.core.api.UsersQueryService;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class UsersQueryServiceImpl implements UsersQueryService {

    @Inject
    private EntityManager em;

    private final static Logger log = Logger.getLogger(UsersQueryServiceImpl.class.getName());

    public UsersQueryServiceImpl() {
    }

   
    @Override
    public List<User> getAll() {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        Query query = qb.spatial().onDefaultCoordinates()
                .within(1, Unit.KM)
                .ofLatitude(0)
                .andLongitude(0)
                .createQuery();

        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);
        fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);
        
        return fullTextQuery.getResultList();
    }
    
    
    
    

}
