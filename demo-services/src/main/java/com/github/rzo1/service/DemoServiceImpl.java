package com.github.rzo1.service;

import com.github.rzo1.domain.DemoObject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateless
public class DemoServiceImpl implements DemoService{

    @PersistenceContext(unitName = "demo", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public long store(DemoObject persistentObject) {
        persistentObject = entityManager.merge(persistentObject);
        entityManager.persist(persistentObject);
        entityManager.flush();
        return persistentObject.getObjectID();
    }

    @Override
    public DemoObject get(long oid) {
        return entityManager.find(DemoObject.class, oid);
    }
}
