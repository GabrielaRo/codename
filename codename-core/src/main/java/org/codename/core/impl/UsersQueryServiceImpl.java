/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.impl;

import java.util.ArrayList;
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
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSelection;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
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
    public List<User> getAll(List<String> interests, List<String> lookingFors, List<String> categories) throws ServiceException {

        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();

        FacetingRequest interestsFacetingRequest = qb.facet()
                .name("interestsFacetingRequest")
                .onField("interests")
                .discrete()
                .orderedBy(FacetSortOrder.COUNT_DESC)
                .includeZeroCounts(true)
                .maxFacetCount(3)
                .createFacetingRequest();
        
        FacetingRequest lookingForsFacetingRequest = qb.facet()
                .name("lookingForsFacetRequest")
                .onField("lookingFor")
                .discrete()
                .orderedBy(FacetSortOrder.COUNT_DESC)
                .includeZeroCounts(true)
                .maxFacetCount(3)
                .createFacetingRequest();

        FacetingRequest iAmsFacetingRequest = qb.facet()
                .name("iAmsFacetingRequest")
                .onField("iAms")
                .discrete()
                .orderedBy(FacetSortOrder.COUNT_DESC)
                .includeZeroCounts(true)
                .maxFacetCount(3)
                .createFacetingRequest();

        Query query = qb.bool().must(qb.keyword().onField("live").matching("true").createQuery()).createQuery();
        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);

        fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);

        // retrieve facet manager and apply faceting request
        FacetManager facetManager = fullTextQuery.getFacetManager();
        List<Facet> interestsSelectedFacets = new ArrayList<Facet>();
        if (interests != null && !interests.isEmpty()) {
            facetManager.enableFaceting(interestsFacetingRequest);
            List<Facet> interestsFacets = facetManager.getFacets("interestsFacetingRequest");
            System.out.println("Interests Facets!!! size = " + interestsFacets.size());

            for (Facet f : interestsFacets) {
                System.out.println("Interests ("+interests+" - contains?  "+ f.getValue() + "- "+interests.contains(f.getValue()));
                System.out.println("Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (interests.contains(f.getValue())) {
                    
                    interestsSelectedFacets.add(f);
                }
                
            }
        }
        List<Facet> lookingForSelectedFacets = new ArrayList<Facet>();
        if (lookingFors != null && !lookingFors.isEmpty()) {
            facetManager.enableFaceting(lookingForsFacetingRequest);
            List<Facet> lookingForFacets = facetManager.getFacets("lookingForsFacetRequest");
            System.out.println("Looking For Facets!!! size = " + lookingForFacets.size());

            for (Facet f : lookingForFacets) {
                System.out.println("Looking fors ("+lookingFors+" - contains?  "+ f.getValue() + "- "+lookingFors.contains(f.getValue()));
                System.out.println("Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (lookingFors.contains(f.getValue())) {
                    
                    lookingForSelectedFacets.add(f);
                }
                
            }
        }
        List<Facet> iAmsSelectedFacets = new ArrayList<Facet>();
        if (categories != null && !categories.isEmpty()) {
            facetManager.enableFaceting(iAmsFacetingRequest);
            List<Facet> iAmsForFacets = facetManager.getFacets("iAmsFacetingRequest");

            System.out.println("Iam s Facets!!! size = " + iAmsForFacets.size());
            for (Facet f : iAmsForFacets) {
                System.out.println("Categories ("+categories+" - contains?  "+ f.getValue() + "- "+categories.contains(f.getValue()));
                System.out.println("Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (categories.contains(f.getValue())) {
                    iAmsSelectedFacets.add(f);
                    
                }
            }
        }

        //System.out.println("ALL Results Size = " + fullTextQuery.getResultSize());
        List resultList = null;// fullTextQuery.getResultList();
        System.out.println("Result List before faceting: " + resultList);

        // apply first facet as additional search criteria
        if (!interestsSelectedFacets.isEmpty()) {
            FacetSelection interestsFacetSelection = facetManager.getFacetGroup("interestsFacetingRequest");
            interestsFacetSelection.selectFacets(interestsSelectedFacets.toArray(new Facet[interestsSelectedFacets.size()]));
        }
        if (!lookingForSelectedFacets.isEmpty()) {
            FacetSelection lookingForFacetSelection = facetManager.getFacetGroup("lookingForsFacetRequest");
            lookingForFacetSelection.selectFacets(lookingForSelectedFacets.toArray(new Facet[lookingForSelectedFacets.size()]));
        }
        if (!iAmsSelectedFacets.isEmpty()) {
            FacetSelection iAmsFacetSelection = facetManager.getFacetGroup("iAmsFacetingRequest");
            iAmsFacetSelection.selectFacets(iAmsSelectedFacets.toArray(new Facet[iAmsSelectedFacets.size()]));
        }
        resultList = fullTextQuery.getResultList();
        System.out.println("Result List after faceting: " + resultList);
        return resultList;
    }

    @Override
    public List<User> getUserByRange(Double lon, Double lat, Double range, List<String> interests, List<String> lookingFors, List<String> categories) throws ServiceException {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        Query query = qb.bool().must(qb.keyword().onField("live").matching("true").createQuery())
                .must(qb.spatial().onDefaultCoordinates()
                        .within(range, Unit.KM)
                        .ofLatitude(lat)
                        .andLongitude(lon)
                        .createQuery())
                .createQuery();

        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);
        //fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);
        Sort distanceSort = new Sort(
                new DistanceSortField(lat, lon, "default"));
        fullTextQuery.setSort(distanceSort);
        
        FacetingRequest interestsFacetingRequest = qb.facet()
                .name("interestsFacetingRequest")
                .onField("interests")
                .discrete()
                .orderedBy(FacetSortOrder.COUNT_DESC)
                .includeZeroCounts(true)
                .maxFacetCount(3)
                .createFacetingRequest();
        
        FacetingRequest lookingForsFacetingRequest = qb.facet()
                .name("lookingForsFacetRequest")
                .onField("lookingFor")
                .discrete()
                .orderedBy(FacetSortOrder.COUNT_DESC)
                .includeZeroCounts(true)
                .maxFacetCount(3)
                .createFacetingRequest();

        FacetingRequest iAmsFacetingRequest = qb.facet()
                .name("iAmsFacetingRequest")
                .onField("iAms")
                .discrete()
                .orderedBy(FacetSortOrder.COUNT_DESC)
                .includeZeroCounts(true)
                .maxFacetCount(3)
                .createFacetingRequest();

        fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);

        // retrieve facet manager and apply faceting request
        FacetManager facetManager = fullTextQuery.getFacetManager();
        

        System.out.println("ALL Results Size = " + fullTextQuery.getResultSize());
        List resultList = null;//fullTextQuery.getResultList();
        System.out.println("Result List before faceting: " + resultList);
        
        List<Facet> interestsSelectedFacets = new ArrayList<Facet>();
        if (interests != null && !interests.isEmpty()) {
            facetManager.enableFaceting(interestsFacetingRequest);
            List<Facet> interestsFacets = facetManager.getFacets("interestsFacetingRequest");
            System.out.println("Interests Facets!!! size = " + interestsFacets.size());

            for (Facet f : interestsFacets) {
                System.out.println("Interests ("+interests+" - contains?  "+ f.getValue() + "- "+interests.contains(f.getValue()));
                System.out.println("Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (interests.contains(f.getValue())) {
                    
                    interestsSelectedFacets.add(f);
                }
                
            }
        }
        List<Facet> lookingForSelectedFacets = new ArrayList<Facet>();
        if (lookingFors != null && !lookingFors.isEmpty()) {
            facetManager.enableFaceting(lookingForsFacetingRequest);
            List<Facet> lookingForFacets = facetManager.getFacets("lookingForsFacetRequest");
            System.out.println("Looking For Facets!!! size = " + lookingForFacets.size());

            for (Facet f : lookingForFacets) {
                System.out.println("Looking fors ("+lookingFors+" - contains?  "+ f.getValue() + "- "+lookingFors.contains(f.getValue()));
                System.out.println("Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (lookingFors.contains(f.getValue())) {
                    
                    lookingForSelectedFacets.add(f);
                }
                
            }
        }
        List<Facet> iAmsSelectedFacets = new ArrayList<Facet>();
        if (categories != null && !categories.isEmpty()) {
            facetManager.enableFaceting(iAmsFacetingRequest);
            List<Facet> iAmsForFacets = facetManager.getFacets("iAmsFacetingRequest");

            System.out.println("Iam s Facets!!! size = " + iAmsForFacets.size());
            for (Facet f : iAmsForFacets) {
                System.out.println("Categories ("+categories+" - contains?  "+ f.getValue() + "- "+categories.contains(f.getValue()));
                System.out.println("Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (categories.contains(f.getValue())) {
                    iAmsSelectedFacets.add(f);
                    
                }
            }
        }

        // apply first facet as additional search criteria
        // apply first facet as additional search criteria
         if (!interestsSelectedFacets.isEmpty()) {
            FacetSelection interestsFacetSelection = facetManager.getFacetGroup("interestsFacetingRequest");
            interestsFacetSelection.selectFacets(interestsSelectedFacets.toArray(new Facet[interestsSelectedFacets.size()]));
        }
        if (!lookingForSelectedFacets.isEmpty()) {
            FacetSelection lookingForFacetSelection = facetManager.getFacetGroup("lookingForsFacetRequest");
            lookingForFacetSelection.selectFacets(lookingForSelectedFacets.toArray(new Facet[lookingForSelectedFacets.size()]));
        }
        if (!iAmsSelectedFacets.isEmpty()) {
            FacetSelection iAmsFacetSelection = facetManager.getFacetGroup("iAmsFacetingRequest");
            iAmsFacetSelection.selectFacets(iAmsSelectedFacets.toArray(new Facet[iAmsSelectedFacets.size()]));
        }
        resultList = fullTextQuery.getResultList();
        System.out.println("Result List after faceting: " + resultList);
        return resultList;
    }

}
