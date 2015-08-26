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
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.codename.model.User;
import org.codename.core.api.UsersQueryService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.PersistenceManager;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
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
    private PersistenceManager pm;

    private final static Logger log = Logger.getLogger(UsersQueryServiceImpl.class.getName());

    public UsersQueryServiceImpl() {
    }

    @PostConstruct
    public void init() throws InterruptedException {
        Search.getFullTextEntityManager(pm.getEm()).createIndexer().startAndWait();
    }

    @Override
    public List<User> search(Double lon, Double lat, Double offsetRange, Double limitRange, List<String> interests,
            List<String> lookingFors, List<String> iAms, Integer offset, Integer limit, List<String> excludes) throws ServiceException {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(pm.getEm());
        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        String excludesString = "";
        for (String e : excludes) {
            excludesString += e + " ";
        }
        System.out.println(">>> Excluding nicknames : " + excludesString);
        BooleanJunction<BooleanJunction> bool = qb.bool();
        bool.must(qb.keyword().onField("live").matching("true").createQuery());
        bool.must(qb.keyword().onField("nickname").matching(excludesString).createQuery()).not();
        Sort distanceSort = null;
        if (limitRange > 0.0 && lat != 0.0 & lon != 0.0) {
            if (offsetRange > 0.0) {
                bool.must(qb.spatial()
                        .within(offsetRange, Unit.KM)
                        .ofLatitude(lat)
                        .andLongitude(lon)
                        .createQuery()).not();
            }

            bool.must(qb.spatial()
                    .within(limitRange, Unit.KM)
                    .ofLatitude(lat)
                    .andLongitude(lon)
                    .createQuery());
            distanceSort = new Sort(
                    new DistanceSortField(lat, lon, "default"));

        }

        
        
        if (!iAms.isEmpty()) {
            String iAmsString = "";
            for (String iam : iAms) {
                iAmsString += iam + " ";
            }
            bool.should(qb.keyword().onField("iAms").matching(iAmsString).createQuery());
        }
        
        if (!lookingFors.isEmpty()) {
            String lookingForsString = "";
            for (String lf : lookingFors) {
                lookingForsString += lf + " ";
            }
            bool.should(qb.keyword().onField("lookingFor").matching(lookingForsString).createQuery());
        }
        
        if (!interests.isEmpty()) {
            String interestsString = "";
            for (String i : interests) {
                interestsString += i + " ";
            }
            bool.should(qb.keyword().onField("interests").matching(interestsString).createQuery());
        }

        Query query = bool.createQuery();

        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);
        fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);

        if (distanceSort != null) {
            fullTextQuery.setSort(distanceSort);
            fullTextQuery.setSpatialParameters(lat, lon, "default");
        }
        if (offset != null && limit != null) {
            fullTextQuery.setFirstResult(offset);
            fullTextQuery.setMaxResults(limit);
        } else {
            fullTextQuery.setFirstResult(0);
            fullTextQuery.setMaxResults(20);
        }    

        List resultList = fullTextQuery.getResultList();

        System.out.println("Results size for query between page (" + fullTextQuery.getFirstResult() + "-to -" + fullTextQuery.getMaxResults() + ")range(" + offsetRange + " - " + limitRange + ") = " + resultList.size());
        return resultList;
    }

    @Override
    public List<Object> searchWithScore(Double lon, Double lat, Double offsetRange, Double limitRange, List<String> interests,
            List<String> lookingFors, List<String> iAms, Integer offset, Integer limit, List<String> excludes) throws ServiceException {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(pm.getEm());
        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        String excludesString = "";
        for (String e : excludes) {
            excludesString += e + " ";
        }
        System.out.println(">>> Excluding nicknames : " + excludesString);
        BooleanJunction<BooleanJunction> bool = qb.bool();
        bool.must(qb.keyword().onField("live").matching("true").createQuery());
        bool.must(qb.keyword().onField("nickname").matching(excludesString).createQuery()).not();
        Sort distanceSort = null;
        if (limitRange > 0.0 && lat != 0.0 & lon != 0.0) {
            if (offsetRange > 0.0) {
                bool.must(qb.spatial()
                        .within(offsetRange, Unit.KM)
                        .ofLatitude(lat)
                        .andLongitude(lon)
                        .createQuery()).not();
            }

            bool.must(qb.spatial()
                    .within(limitRange, Unit.KM)
                    .ofLatitude(lat)
                    .andLongitude(lon)
                    .createQuery());
            distanceSort = new Sort(
                    new DistanceSortField(lat, lon, "default"));

        }
        if (!iAms.isEmpty()) {
            String iAmsString = "";
            for (String iam : iAms) {
                iAmsString += iam + " ";
            }
            bool.should(qb.keyword().onField("iAms").matching(iAmsString).createQuery());
        }
        
        if (!lookingFors.isEmpty()) {
            String lookingForsString = "";
            for (String lf : lookingFors) {
                lookingForsString += lf + " ";
            }
            bool.should(qb.keyword().onField("lookingFor").matching(lookingForsString).createQuery());
        }
        
        if (!interests.isEmpty()) {
            String interestsString = "";
            for (String i : interests) {
                interestsString += i + " ";
            }
            bool.should(qb.keyword().onField("interests").matching(interestsString).createQuery());
        }

        Query query = bool.createQuery();

        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);
        fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);

        String[] fields = {"nickname", ProjectionConstants.SCORE, ProjectionConstants.EXPLANATION, ProjectionConstants.SPATIAL_DISTANCE};
        fullTextQuery.setProjection(fields);
        if (distanceSort != null) {
            fullTextQuery.setSort(distanceSort);
            fullTextQuery.setSpatialParameters(lat, lon, "default");
        }
        if (offset != null && limit != null) {
            fullTextQuery.setFirstResult(offset);
            fullTextQuery.setMaxResults(limit);
        } else {
            fullTextQuery.setFirstResult(0);
            fullTextQuery.setMaxResults(20);
        }
        List resultList = null;


        resultList = fullTextQuery.getResultList();
        for (Object o : resultList) {
            Object[] oArray = (Object[]) o;
            System.out.println("Nickname: " + oArray[0]);
            System.out.println("Score: " + oArray[1]);
            System.out.println("Explanation: " + oArray[2]);
            System.out.println("Spatial Distance: " + oArray[3]);
        }
        System.out.println("Results size for query between page (" + fullTextQuery.getFirstResult() + "-to -" + fullTextQuery.getMaxResults() + ")range(" + offsetRange + " - " + limitRange + ") = " + resultList.size());
        return resultList;
    }

}
