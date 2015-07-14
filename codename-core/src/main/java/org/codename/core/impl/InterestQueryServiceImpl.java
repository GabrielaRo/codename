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
import org.codename.core.api.InterestQueryService;
import org.codename.core.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class InterestQueryServiceImpl implements InterestQueryService {

    
    private static List<String> interests = new ArrayList<String>();
    private final static Logger log = Logger.getLogger(InterestQueryServiceImpl.class.getName());

    public InterestQueryServiceImpl() {
        
    }

    @PostConstruct
    public void init() throws InterruptedException {
        interests.add("Design");
        interests.add("Journalism");
        interests.add("Photography");
        interests.add("Fashion");
        interests.add("Gaming");
        interests.add("Virtual Reality");
        interests.add("Software");
        interests.add("Education");
        interests.add("Startups");
        interests.add("Business");
        interests.add("Blogging");
        interests.add("Music");
        interests.add("Sports");
    }

    @Override
    public List<String> getAll() throws ServiceException {
        return interests;
    }

    

}
