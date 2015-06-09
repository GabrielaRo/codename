/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.impl;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.codename.services.api.NotificationsService;
import org.codename.services.api.RulesService;
import org.kie.api.KieBase;
import org.kie.api.cdi.KBase;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class RulesServiceImpl implements RulesService {

    @Inject
    private NotificationsService notificationsService;
    

    @Inject
    @KBase("matchingrules")
    KieBase kBase;

    private KieSession kieSession;

    public RulesServiceImpl() {
    }

    @PostConstruct
    public void init() {
        kieSession = kBase.newKieSession();
        kieSession.addEventListener(new RuleRuntimeEventListener() {

            public void objectInserted(ObjectInsertedEvent event) {

            }

            public void objectUpdated(ObjectUpdatedEvent event) {

            }

            public void objectDeleted(ObjectDeletedEvent event) {
                System.out.println("event.getOldObject() = " + event.getOldObject());
//                if (event.getOldObject() instanceof Matching) {
//                    notificationService.notifyUser(((Matching) event.getOldObject()).getMembership().getUserId() + ((Matching) event.getOldObject()).getListing().getUserId(),
//                            ((Matching) event.getOldObject()).getId().toString(), "removeMatching");
//                }
            }
        });
        kieSession.setGlobal("notificationsService", notificationsService);
       
        
    }

    @Override
    public int insert(Object o) {
        System.out.println(">> Rules Service inserted: " + o);
        kieSession.insert(o);
        return kieSession.fireAllRules();
    }

    @Override
    public void reset() {
        System.out.println(">> Rules Service init!");
        init();
    }

    @Override
    public void retract(Object o) {
        System.out.println(">> Rules Service retracted: " + o);
        FactHandle factHandle = kieSession.getFactHandle(o);
        kieSession.delete(factHandle);
        kieSession.fireAllRules();
    }

    @Override
    public int update(Object o) {
        System.out.println(">> Rules Service updated: " + o);
        FactHandle factHandle = kieSession.getFactHandle(o);
        kieSession.update(factHandle, o);
        return kieSession.fireAllRules();
    }

}
