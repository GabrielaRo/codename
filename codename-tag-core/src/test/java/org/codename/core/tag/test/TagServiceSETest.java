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

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ServiceException {        
    }

    @After
    public void tearDown() throws ServiceException {
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
    	User testUser = new User("test@gmail.com", "fakepassword");
    	usersService.newUser(testUser);
    	Long tagId = tagService.createTag(testUser.getId(), "Friends", Color.BLUE, true);
        Assert.assertTrue(0 != tagId);
    }
    
    @Test
    public void getAllTagsCreatedByUserTest() throws ServiceException, Exception {
    	User testUser1 = new User("test.user1@gmail.com", "fakepassword");
    	usersService.newUser(testUser1);
    	tagService.createTag(testUser1.getId(), "Friends", Color.BLUE, true);
    	tagService.createTag(testUser1.getId(), "Family", Color.RED, true);
    	tagService.createTag(testUser1.getId(), "Work", Color.BLACK, false);
    	List<Tag> tagsForUser = tagService.getAllTagsCreatedByUser(testUser1.getId()); 
        Assert.assertEquals(3, tagsForUser.size());
    }
    
    @Test
    public void getAllTagsCreatedByManyUsersTest() throws ServiceException, Exception {
    	User testUser2 = new User("test.user2@gmail.com", "fakepassword");
    	usersService.newUser(testUser2);    	
    	tagService.createTag(testUser2.getId(), "Friends", Color.BLUE, true);
    	tagService.createTag(testUser2.getId(), "Friends", Color.RED, true);
    	tagService.createTag(testUser2.getId(), "Work", Color.BLACK, false);
    	List<Tag> tagsForUser2 = tagService.getAllTagsCreatedByUser(testUser2.getId());

    	User testUser3 = new User("test.user3@gmail.com", "fakepassword");
    	usersService.newUser(testUser3);
    	tagService.createTag(testUser3.getId(), "Friends", Color.BLUE, true);
    	tagService.createTag(testUser3.getId(), "Family", Color.RED, true);
    	tagService.createTag(testUser3.getId(), "Work", Color.BLACK, false);
    	tagService.createTag(testUser3.getId(), "School", Color.BLACK, false);
    	tagService.createTag(testUser3.getId(), "Office", Color.BLACK, false);
    	tagService.createTag(testUser3.getId(), "Bar", Color.BLACK, false);
    	tagService.createTag(testUser3.getId(), "Course", Color.BLACK, false);
    	List<Tag> tagsForUser3 = tagService.getAllTagsCreatedByUser(testUser3.getId());
        Assert.assertEquals(3, tagsForUser2.size());
        Assert.assertEquals(7, tagsForUser3.size());
    }
    
    @Test
    public void deleteTagServiceTest() throws ServiceException, Exception {
    	User testUser4 = new User("test4@gmail.com", "fakepassword");
    	usersService.newUser(testUser4);
    	Long tagId = tagService.createTag(testUser4.getId(), "Friends", Color.BLUE, true);
    	tagService.deleteTag(tagId);
    	List<Tag> tagsForUser4 = tagService.getAllTagsCreatedByUser(testUser4.getId());
        Assert.assertEquals(0, tagsForUser4.size());
    }
    
    @Test
    public void createDeleteReturnTagsByManyUsersTest() throws ServiceException, Exception {
    	User testUser5 = new User("test.user5@gmail.com", "fakepassword");
    	usersService.newUser(testUser5);    	
    	tagService.createTag(testUser5.getId(), "Friends", Color.BLUE, true);
    	tagService.createTag(testUser5.getId(), "Friends", Color.RED, true);
    	tagService.createTag(testUser5.getId(), "Work", Color.BLACK, false);

    	User testUser6 = new User("test.user6@gmail.com", "fakepassword");
    	usersService.newUser(testUser6);
    	tagService.createTag(testUser6.getId(), "Friends", Color.BLUE, true);
    	Long tagID1User6 = tagService.createTag(testUser6.getId(), "Family", Color.RED, true);
    	tagService.createTag(testUser6.getId(), "Work", Color.BLACK, false);
    	tagService.createTag(testUser6.getId(), "School", Color.BLACK, false);
    	tagService.createTag(testUser6.getId(), "Office", Color.BLACK, false);
    	tagService.createTag(testUser6.getId(), "Bar", Color.BLACK, false);
    	tagService.createTag(testUser6.getId(), "Course", Color.BLACK, false);
    	tagService.deleteTag(tagID1User6);
        
        User testUser7 = new User("test.user7@gmail.com", "fakepassword");
    	usersService.newUser(testUser7);
    	Long tagID1User7 = tagService.createTag(testUser7.getId(), "Friends", Color.BLUE, true);
    	Long tagID2User7 = tagService.createTag(testUser7.getId(), "Family", Color.RED, true);
    	Long tagID3User7 = tagService.createTag(testUser7.getId(), "Work", Color.BLACK, false);
    	tagService.deleteTag(tagID1User7);
    	tagService.deleteTag(tagID2User7);
    	tagService.deleteTag(tagID3User7);
        
        User testUser8 = new User("test.user8@gmail.com", "fakepassword");
    	usersService.newUser(testUser8);    	
    	Long tagID1User8 = tagService.createTag(testUser8.getId(), "Friends", Color.BLUE, true);
    	Long tagID2User8 = tagService.createTag(testUser8.getId(), "Friends", Color.RED, true);
    	tagService.createTag(testUser8.getId(), "Work", Color.BLACK, false);
    	tagService.deleteTag(tagID1User8);
    	tagService.deleteTag(tagID2User8);
    	
    	List<Tag> tagsForUser5 = tagService.getAllTagsCreatedByUser(testUser5.getId());
    	List<Tag> tagsForUser6 = tagService.getAllTagsCreatedByUser(testUser6.getId());
    	List<Tag> tagsForUser7 = tagService.getAllTagsCreatedByUser(testUser7.getId());
    	List<Tag> tagsForUser8 = tagService.getAllTagsCreatedByUser(testUser8.getId());
    	Assert.assertEquals(3, tagsForUser5.size());
        Assert.assertEquals(6, tagsForUser6.size());
        Assert.assertEquals(0, tagsForUser7.size());
    	Assert.assertEquals(1, tagsForUser8.size());
    }

}
