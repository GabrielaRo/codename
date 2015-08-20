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
import org.hibernate.search.query.dsl.BooleanJunction;
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
    public List<User> search(Double lon, Double lat, Double offsetRange, Double limitRange, List<String> interests,
            List<String> lookingFors, List<String> categories, Integer offset, Integer limit) throws ServiceException {
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(pm.getEm());
        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        BooleanJunction<BooleanJunction> bool = qb.bool();
        bool.must(qb.keyword().onField("live").matching("true").createQuery());

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
        }

        Query query = bool.createQuery();

        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);
        if (offset != null && limit != null) {
            fullTextQuery.setFirstResult(offset);
            fullTextQuery.setMaxResults(limit);
        } else {
            fullTextQuery.setFirstResult(0);
            fullTextQuery.setMaxResults(20);
        }

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

        List resultList = null;

        List<Facet> interestsSelectedFacets = new ArrayList<Facet>();
        if (interests != null && !interests.isEmpty()) {
            facetManager.enableFaceting(interestsFacetingRequest);
            List<Facet> interestsFacets = facetManager.getFacets("interestsFacetingRequest");

            for (Facet f : interestsFacets) {

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

                if (categories.contains(f.getValue())) {
                    iAmsSelectedFacets.add(f);

                }
            }
        }

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
        System.out.println("Results size for query between page (" + fullTextQuery.getFirstResult() + "-to -" + fullTextQuery.getMaxResults() + ")range(" + offsetRange + " - " + limitRange + ") = " + resultList.size());
        return resultList;
    }

}
