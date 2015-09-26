/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.chat.tests;

import javax.inject.Inject;

import org.codename.core.chat.api.PresenceService;
import org.codename.core.user.api.UsersService;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.user.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author gabi
 */
@RunWith(Arquillian.class)
public class PresenceServiceSETest {

    @Inject
    private PresenceService presenceService;

    @Inject
    private UsersService usersService;

    private User marylandSupUser = new User("marylandSup@gmail.com", "fakepassword");
    private User grogDjUser = new User("grogdj@gmail.com", "fakepassword");
    private User ezeUser = new User("eze@gmail.com", "fakepassword");

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ServiceException {

        usersService.newUser(marylandSupUser);
        usersService.newUser(grogDjUser);
        usersService.newUser(ezeUser);
    }

    @After
    public void tearDown() throws ServiceException {
        usersService.removeUser(marylandSupUser.getId());
        usersService.removeUser(grogDjUser.getId());
        usersService.removeUser(ezeUser.getId());
    }

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.codename")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/users.xml", "users.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void chatServiceInitialTest() throws ServiceException, Exception {

       

    }
    
    

    // write test for conversation blocked
}
