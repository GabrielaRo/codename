/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tag.test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import org.codename.core.user.api.UsersService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.tag.api.TagService;
import org.codename.model.tag.Tag;
import org.codename.model.user.User;
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
 * @author gabriela
 */
@RunWith(Arquillian.class)
public class TagServiceSETest {

    @Inject
    private TagService tagService;

    @Inject
    private UsersService usersService;

    private User testUser = new User("test.user@gmail.com", "fakepassword");

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ServiceException {

        usersService.newUser(testUser);
    }

    @After
    public void tearDown() throws ServiceException {
//        tagService.deleteTag(testUser.getId());
    }

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.codename")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/users.xml", "users.xml")
                .addAsManifestResource("META-INF/tags.xml", "tags.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void createTagServiceTest() throws ServiceException, Exception {
    	Long tagId = tagService.createTag(testUser.getId(), "Friends", Color.BLUE, true);
//    	tagService.createTag(101056L, "Family", Color.RED, true);
//    	tagService.createTag(101056L, "Work", Color.BLACK, false);
//    	Tag tag = new Tag(testUser, "Friends", Color.GREEN, true);
//    	Long tagIdNumer = tagIdNumer = tagService.createTag(101056L, "Friends", Color.BLUE, true);
        Assert.assertTrue(0 != tagId);
    }

}
