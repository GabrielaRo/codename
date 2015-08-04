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
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.codename.model.User;
import org.codename.core.api.UsersQueryService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.PersistenceManager;
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
    private PersistenceManager pm;

    private final static Logger log = Logger.getLogger(UsersQueryServiceImpl.class.getName());

    public UsersQueryServiceImpl() {
    }

    @PostConstruct
    public void init() throws InterruptedException {
        Search.getFullTextEntityManager(pm.getEm()).createIndexer().startAndWait();
    }

    @Override
    public List<User> getAll(List<String> interests, List<String> lookingFors, List<String> categories) throws ServiceException {

        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(pm.getEm());
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
            

            for (Facet f : interestsFacets) {
            
                System.out.println("Interest Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (interests.contains(f.getValue())) {
                    
                    interestsSelectedFacets.add(f);
                }
                
            }
        }
        List<Facet> lookingForSelectedFacets = new ArrayList<Facet>();
        if (lookingFors != null && !lookingFors.isEmpty()) {
            facetManager.enableFaceting(lookingForsFacetingRequest);
            List<Facet> lookingForFacets = facetManager.getFacets("lookingForsFacetRequest");
            

            for (Facet f : lookingForFacets) {
            
                System.out.println("Looking For Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (lookingFors.contains(f.getValue())) {
                    
                    lookingForSelectedFacets.add(f);
                }
                
            }
        }
        List<Facet> iAmsSelectedFacets = new ArrayList<Facet>();
        if (categories != null && !categories.isEmpty()) {
            facetManager.enableFaceting(iAmsFacetingRequest);
            List<Facet> iAmsForFacets = facetManager.getFacets("iAmsFacetingRequest");

           
            for (Facet f : iAmsForFacets) {
           
                System.out.println("I Am Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (categories.contains(f.getValue())) {
                    iAmsSelectedFacets.add(f);
                    
                }
            }
        }

        System.out.println("> All Users = " + fullTextQuery.getResultSize());
        List resultList = null;
        

        // apply first facet as additional search criteria
        if (!interestsSelectedFacets.isEmpty()) {
            System.out.println("Applying Interst Facet");
            FacetSelection interestsFacetSelection = facetManager.getFacetGroup("interestsFacetingRequest");
            interestsFacetSelection.selectFacets(interestsSelectedFacets.toArray(new Facet[interestsSelectedFacets.size()]));
        }
        if (!lookingForSelectedFacets.isEmpty()) {
            System.out.println("Applying Looking For Facet");
            FacetSelection lookingForFacetSelection = facetManager.getFacetGroup("lookingForsFacetRequest");
            lookingForFacetSelection.selectFacets(lookingForSelectedFacets.toArray(new Facet[lookingForSelectedFacets.size()]));
        }
        if (!iAmsSelectedFacets.isEmpty()) {
            System.out.println("Applying I Am Facet");
            FacetSelection iAmsFacetSelection = facetManager.getFacetGroup("iAmsFacetingRequest");
            iAmsFacetSelection.selectFacets(iAmsSelectedFacets.toArray(new Facet[iAmsSelectedFacets.size()]));
        }
        resultList = fullTextQuery.getResultList();
        System.out.println("> All Users Result (Faceted)= " + fullTextQuery.getResultSize());
        return resultList;
    }

    @Override
    public List<User> getUserByRange(Double lon, Double lat, Double range, List<String> interests, List<String> lookingFors, List<String> categories) throws ServiceException {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(pm.getEm());
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
        

        System.out.println("> All Users By Range = " + fullTextQuery.getResultSize());
        List resultList = null;
        
        
        List<Facet> interestsSelectedFacets = new ArrayList<Facet>();
        if (interests != null && !interests.isEmpty()) {
            facetManager.enableFaceting(interestsFacetingRequest);
            List<Facet> interestsFacets = facetManager.getFacets("interestsFacetingRequest");
           

            for (Facet f : interestsFacets) {
                
                System.out.println("> Interest Facet : " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (interests.contains(f.getValue())) {
                    
                    interestsSelectedFacets.add(f);
                }
                
            }
        }
        List<Facet> lookingForSelectedFacets = new ArrayList<Facet>();
        if (lookingFors != null && !lookingFors.isEmpty()) {
            facetManager.enableFaceting(lookingForsFacetingRequest);
            List<Facet> lookingForFacets = facetManager.getFacets("lookingForsFacetRequest");
            

            for (Facet f : lookingForFacets) {
                
                System.out.println("> Looking For Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (lookingFors.contains(f.getValue())) {
                    
                    lookingForSelectedFacets.add(f);
                }
                
            }
        }
        List<Facet> iAmsSelectedFacets = new ArrayList<Facet>();
        if (categories != null && !categories.isEmpty()) {
            facetManager.enableFaceting(iAmsFacetingRequest);
            List<Facet> iAmsForFacets = facetManager.getFacets("iAmsFacetingRequest");

            
            for (Facet f : iAmsForFacets) {
                
                System.out.println("> I Am Facet: " + f.getFacetingName() + " - " + f.getFieldName() + " - " + f.getValue() + "- " + f.getCount());
                if (categories.contains(f.getValue())) {
                    iAmsSelectedFacets.add(f);
                    
                }
            }
        }

        // apply first facet as additional search criteria
        // apply first facet as additional search criteria
         if (!interestsSelectedFacets.isEmpty()) {
             System.out.println("Applying Interest For Facet");
            FacetSelection interestsFacetSelection = facetManager.getFacetGroup("interestsFacetingRequest");
            interestsFacetSelection.selectFacets(interestsSelectedFacets.toArray(new Facet[interestsSelectedFacets.size()]));
        }
        if (!lookingForSelectedFacets.isEmpty()) {
            System.out.println("Applying Looking For For Facet");
            FacetSelection lookingForFacetSelection = facetManager.getFacetGroup("lookingForsFacetRequest");
            lookingForFacetSelection.selectFacets(lookingForSelectedFacets.toArray(new Facet[lookingForSelectedFacets.size()]));
        }
        if (!iAmsSelectedFacets.isEmpty()) {
            System.out.println("Applying I am For Facet");
            FacetSelection iAmsFacetSelection = facetManager.getFacetGroup("iAmsFacetingRequest");
            iAmsFacetSelection.selectFacets(iAmsSelectedFacets.toArray(new Facet[iAmsSelectedFacets.size()]));
        }
        resultList = fullTextQuery.getResultList();
        System.out.println("> All Users Result (Faceted)= " + fullTextQuery.getResultSize());
        return resultList;
    }

}
