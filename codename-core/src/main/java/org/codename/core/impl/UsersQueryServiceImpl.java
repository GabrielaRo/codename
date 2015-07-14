/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.impl;

import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.codename.model.User;
import org.codename.core.api.UsersQueryService;
import org.codename.core.exceptions.ServiceException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.hibernate.search.spatial.DistanceSortField;

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

    @PostConstruct
    public void init() throws InterruptedException {
        Search.getFullTextEntityManager(em).createIndexer().startAndWait();
    }

    @Override
    public List<User> getAll() throws ServiceException {

        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        Query query = qb.bool().must(qb.keyword().onField("live").matching("true").createQuery()).createQuery();
        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);
        fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);
        System.out.println("ALL Results Size = " + fullTextQuery.getResultSize());
        List resultList = fullTextQuery.getResultList();

        return resultList;
    }

    @Override
    public List<User> getUserByRange(Double lon, Double lat, Double range, List<String> lookingFors, List<String> categories) throws ServiceException {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        Query query = qb.bool().must(qb.keyword().onField("live").matching("true").createQuery())
                               .must(qb.spatial().onDefaultCoordinates()
                                        .within(range, Unit.KM)
                                        .ofLatitude(lat)
                                        .andLongitude(lon)
                                        .createQuery())
                               .should(qb.keyword().onField("lookingFor").matching(lookingFors).createQuery())
                               .should(qb.keyword().onField("categories").matching(categories).createQuery())
                                .createQuery();

        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);
        //fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);
        Sort distanceSort = new Sort(
                new DistanceSortField(lat, lon, "default"));
        fullTextQuery.setSort(distanceSort);
        System.out.println("Results Size = " + fullTextQuery.getResultSize());
        List resultList = fullTextQuery.getResultList();
        return resultList;
    }

}
