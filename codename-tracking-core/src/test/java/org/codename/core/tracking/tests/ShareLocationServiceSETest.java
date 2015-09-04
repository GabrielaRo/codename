/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tracking.tests;

import javax.inject.Inject;

import org.codename.core.exceptions.ServiceException;
import org.codename.core.tracking.api.ShareLocationService;

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
public class ShareLocationServiceSETest {

    @Inject
    private ShareLocationService shareLocationService;

    

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
                .addAsManifestResource("META-INF/sharedlocations.xml", "sharedlocations.xml")
                
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void sharedLocationByUsersServiceInitialTest() throws ServiceException, Exception {

        

    }
    
    

    // write test for conversation blocked
}
