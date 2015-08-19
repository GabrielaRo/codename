/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tests;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import org.codename.core.api.UsersService;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author gabi
 */
@RunWith(Arquillian.class)
public class UsersServiceSETest {

    @Inject
    private UsersService usersService;

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

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.codename")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/users.xml", "users.xml")
                .addAsManifestResource("META-INF/servicekey.xml", "servicekey.xml")
                .addAsManifestResource("META-INF/notifications.xml", "notifications.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void removeUserTest() throws ServiceException, Exception {

        User u = new User("gabriela@gmail.com", "123456");

        Long newUserId = usersService.newUser(u);

        Assert.assertNotNull(newUserId);

        usersService.updateLive(newUserId, "true");

        usersService.removeUser(newUserId);

        User byId = usersService.getById(newUserId);
        Assert.assertTrue(byId == null);
    }

    @Test
    public void calculateUserProfilePercentageTest() throws ServiceException {

    	//User 1 -> 26%, filled just few of the fields
        User u1 = new User("gabriela.rogelova@gmail.com", "123456");
        u1.setFirstname("Gabriela");
        u1.setLastname("Rogelova");
        u1.setLongBio("I am a digital nomad.");
        u1.setTwitter("www.twitter.com/gabriela");

        Assert.assertEquals(30, usersService.calculateUserProfilePercentage(u1));

        //User 2 -> 43% filled the half of the fields
        User u2 = new User("gabriela.rogelova@gmail.com", "123456");
        u2.setFirstname("Gabriela");
        u2.setLastname("Rogelova");
        u2.setOriginallyFrom("Pernik");
        u2.setLinkedin("www.linkedin.com/gabriela");
        u2.setWebsite("www.gabriela.com");
        u2.setAvatarFileName("Picture");

        Assert.assertEquals(45, usersService.calculateUserProfilePercentage(u2));

      //User 3 -> 60%, filled more than half of the fields
        User u3 = new User("gabriela.rogelova@gmail.com", "123456");
        u3.setFirstname("Gabriela");
        u3.setLastname("Rogelova");
        u3.setOriginallyFrom("Pernik");
        u3.setLinkedin("www.linkedin.com/gabriela");
        u3.setWebsite("www.gabriela.com");
        u3.setAvatarFileName("Picture");
        u3.setCoverFileName("Cover Photo");
        u3.setMessageMeMessage("Message me");
        u3.setJobTitle("Job");
        u3.setBio("My Bio");

        Assert.assertEquals(60, usersService.calculateUserProfilePercentage(u3));

//      User 4 -> 100%, filled all the fields
        User u4 = new User("gabriela.rogelova@gmail.com", "123456");
        List<String> test = new ArrayList<String>();
        test.add("1 thing");
        test.add("2 things");
        test.add("3 things");
        u4.setAvatarFileName("Photo");
        u4.setFirstname("Gabriela");
        u4.setLastname("Rogelova");
        u4.setInterests(test);
        u4.setLookingFor(test);
        u4.setCoverFileName("Cover Photo");
        u4.setLocation("London");
        u4.setBio("My Bio");
        u4.setJobTitle("Job");
        u4.setShareMessage("Share");
        u4.setMessageMeMessage("Message me");
        u4.setOriginallyFrom("Pernik");
        u4.setInterests(test);
        u4.setWebsite("www.gabriela.com");
        u4.setTwitter("www.twitter.com/gabriela");
        u4.setLinkedin("www.linkedin.com/gabriela");

        Assert.assertEquals(100, usersService.calculateUserProfilePercentage(u4));

 //     User 5 -> 0%, no fields are filled
        User u5 = new User("gabriela.rogelova@gmail.com", "123456");

        Assert.assertEquals(0, usersService.calculateUserProfilePercentage(u5));
    }
    
    @Test
    public void updateLiveTest() throws ServiceException {
//      u1 profile -> 60% completed, it should return live == true
    	User u1 = new User("rogelova@gmail.com", "123456");
        Long newUserId1 = usersService.newUser(u1);
        Assert.assertNotNull(newUserId1);
        u1.setFirstname("Gabriela");
        u1.setLastname("Rogelova");
        u1.setOriginallyFrom("Pernik");
        u1.setLinkedin("www.linkedin.com/gabriela");
        u1.setWebsite("www.gabriela.com");
        u1.setAvatarFileName("Picture");
        u1.setCoverFileName("Cover Photo");
        
        u1.setMessageMeMessage("Message me");
        u1.setJobTitle("Job");
        u1.setBio("My Bio");
        usersService.updateLive(newUserId1, "true");
        Assert.assertTrue(u1.isLive());
    	
//      u2 profile -> 43% completed, it should return live == false  
        User u2 = new User("rog@gmail.com", "123456");
        Long newUserId2 = usersService.newUser(u2);
        Assert.assertNotNull(newUserId2);
        u2.setFirstname("Gabriela");
        u2.setLastname("Rogelova");
        u2.setOriginallyFrom("Pernik");
        u2.setLinkedin("www.linkedin.com/gabriela");
        u2.setWebsite("www.gabriela.com");
        u2.setAvatarFileName("Picture");
        usersService.updateLive(newUserId2, "true");
        Assert.assertTrue(!(u2.isLive()));
    	
    }

}
