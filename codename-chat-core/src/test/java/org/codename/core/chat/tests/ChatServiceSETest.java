/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.chat.tests;

import java.util.List;
import javax.inject.Inject;

import org.codename.core.chat.api.ChatService;
import org.codename.core.user.api.UsersService;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.chat.Conversation;
import org.codename.model.chat.Message;
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
 * @author gabi
 */
@RunWith(Arquillian.class)
public class ChatServiceSETest {

    @Inject
    private ChatService chatService;

    @Inject
    private UsersService usersService;
    
    private User user = new User("marylandSup@gmail.com", "fakepassword");
    private User user1 = new User("grogDj@gmail.com", "fakepassword");

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ServiceException {
        
        usersService.newUser(user);
        usersService.newUser(user1);
    }

    @After
    public void tearDown() throws ServiceException {
        usersService.removeUser(user.getId());
        usersService.removeUser(user1.getId());
    }

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.codename")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/users.xml", "users.xml")
                .addAsManifestResource("META-INF/messages.xml", "messages.xml")
                .addAsManifestResource("META-INF/conversations.xml", "conversations.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void chatServiceInitialTest() throws ServiceException, Exception {


        Long conversationId = chatService.createConversation("marylandSup", "grogDj");

        Long message1 = chatService.sendMessage(conversationId, "marylandSup", "hi this is my first message");

        Long message2 = chatService.sendMessage(conversationId, "grogDj", "grogDj here! ");

        Long message3 = chatService.sendMessage(conversationId, "grogDj", "bro! ");

        List<Message> messages = chatService.getMessages(conversationId);
        Assert.assertEquals(3, messages.size());

        List<Conversation> conversations = chatService.getConversations("marylandSup");
        Assert.assertEquals(1, conversations.size());

        boolean removeConversation = chatService.removeConversation("marylandSup", "grogDj");
        Assert.assertTrue(removeConversation);

    }

    @Test
    public void chatServiceDuplicatedConversationTest() throws ServiceException, Exception {

        Long firstConversation = chatService.createConversation("marylandSup", "grogDj");

        chatService.sendMessage(firstConversation, "marylandSup", "hi this is my first message");

        chatService.sendMessage(firstConversation, "grogDj", "grogDj here! ");

        chatService.sendMessage(firstConversation, "grogDj", "bro! ");

        List<Message> messages = chatService.getMessages(firstConversation);
        Assert.assertEquals(3, messages.size());

        List<Conversation> conversations = chatService.getConversations("marylandSup");
        Assert.assertEquals(1, conversations.size());

        Long secondConverstaion = chatService.createConversation("marylandSup", "grogDj");

        Assert.assertEquals(firstConversation, secondConverstaion);

        Long thirdConversation = chatService.createConversation("grogDj", "marylandSup");

        Assert.assertEquals(thirdConversation, secondConverstaion);

        messages = chatService.getMessages(thirdConversation);
        Assert.assertEquals(3, messages.size());

        messages = chatService.getMessages(secondConverstaion);
        Assert.assertEquals(3, messages.size());

        boolean removeConversation = chatService.removeConversation("marylandSup", "grogDj");
        Assert.assertTrue(removeConversation);

    }

    // write test for conversation blocked
}
