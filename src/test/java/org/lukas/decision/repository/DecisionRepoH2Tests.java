package org.lukas.decision.repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lukas.decision.Decision;
import org.lukas.decision.Importance;
import org.lukas.decision.repository.impl.DecisionRepoH2;
import org.lukas.db.HibernateUtil;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecisionRepoH2Tests {
    private DecisionRepoH2 decisionRepo;

    @BeforeEach
    public void beforeEach() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        decisionRepo = new DecisionRepoH2(session);
    }

    @AfterEach
    public void afterEach() {
//        HibernateUtil.shutdown();
    }

    @Test
    public void testAdding() {
        Decision decision = new Decision(new Date(), "Grzyby", "Grzyb", Importance.MINOR, "dupa");
        decisionRepo.add(decision);
        assertTrue(decisionRepo.getById(decision.getId()).isPresent());
    }

    @Test
    public void testSearching() {
        String compName = "Grzyby";

        Decision decision = new Decision(new Date(), compName, "Grzyb", Importance.MINOR, "dupa");
        decisionRepo.add(decision);
        assertTrue(decisionRepo.find(compName).contains(decision));
    }
}
