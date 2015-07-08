/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.tests;

import java.io.File;
import java.util.List;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.apache.lucene.search.Query;
import org.codename.model.User;

import org.codename.services.api.UsersService;
import org.codename.services.exceptions.ServiceException;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author grogdj
 */
@RunWith(Arquillian.class)
public class SimpleLocalServiceTest {

    @Deployment
    public static WebArchive createDeployment() {

        File[] libs = Maven.configureResolver()
                // .withRemoteRepo("jboss-repo", "https://repository.jboss.org/nexus/content/groups/public/", "default")
                .workOffline()
                .withMavenCentralRepo(false)
                .withClassPathResolution(true)
                .loadPomFromFile("pom.xml").importRuntimeDependencies().resolve("org.drools:drools-compiler",
                        "com.google.protobuf:protobuf-java")
                .withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsLibraries(libs)
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public SimpleLocalServiceTest() {
    }

    @PersistenceContext(name = "primary")
    @Produces
    EntityManager em;

    @Inject
    private UsersService usersService;

    @Inject
    private UserTransaction ut;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void newUserProfileLocationSearchTest() throws ServiceException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        ut.begin();
        Long newUser = usersService.newUser(new User("grogdj@gmail.com", "asdasd"));

        ut.commit();

        ut.begin();
        usersService.updateLocation(newUser, 51.5004232, -0.2588513);
        ut.commit();

        Double lon = 51.4987702;
        Double lat = -0.2624294;

        User byId = usersService.getById(newUser);
        Assert.assertEquals(byId.getLongitude(), Double.valueOf(51.5004232));
        Assert.assertEquals(byId.getLatitude(), Double.valueOf(-0.2588513));

        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        Assert.assertNotNull(fullTextEm);

        QueryBuilder qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        Query query = qb.spatial().onDefaultCoordinates()
                .within(1, Unit.KM)
                .ofLatitude(lat)
                .andLongitude(lon)
                .createQuery();

        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);
        fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);
        List resultList = fullTextQuery.getResultList();

        Assert.assertNotEquals(0, resultList.size());

        lon = 52.4987702;
        lat = -0.2624294;

        qb = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        query = qb.spatial().onDefaultCoordinates()
                .within(1, Unit.KM)
                .ofLatitude(lat)
                .andLongitude(lon)
                .createQuery();

        fullTextQuery = fullTextEm.createFullTextQuery(query, User.class);
        fullTextQuery.setSort(org.apache.lucene.search.Sort.RELEVANCE);
        resultList = fullTextQuery.getResultList();

        Assert.assertEquals(0, resultList.size());
    }

}
