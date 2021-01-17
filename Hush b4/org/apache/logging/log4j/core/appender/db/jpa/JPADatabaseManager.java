// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jpa;

import org.apache.logging.log4j.core.appender.ManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManager;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.LogEvent;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Constructor;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public final class JPADatabaseManager extends AbstractDatabaseManager
{
    private static final JPADatabaseManagerFactory FACTORY;
    private final String entityClassName;
    private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
    private final String persistenceUnitName;
    private EntityManagerFactory entityManagerFactory;
    
    private JPADatabaseManager(final String name, final int bufferSize, final Class<? extends AbstractLogEventWrapperEntity> entityClass, final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, final String persistenceUnitName) {
        super(name, bufferSize);
        this.entityClassName = entityClass.getName();
        this.entityConstructor = entityConstructor;
        this.persistenceUnitName = persistenceUnitName;
    }
    
    @Override
    protected void connectInternal() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(this.persistenceUnitName);
    }
    
    @Override
    protected void disconnectInternal() {
        if (this.entityManagerFactory != null && this.entityManagerFactory.isOpen()) {
            this.entityManagerFactory.close();
        }
    }
    
    @Override
    protected void writeInternal(final LogEvent event) {
        if (!this.isConnected() || this.entityManagerFactory == null) {
            throw new AppenderLoggingException("Cannot write logging event; JPA manager not connected to the database.");
        }
        AbstractLogEventWrapperEntity entity;
        try {
            entity = (AbstractLogEventWrapperEntity)this.entityConstructor.newInstance(event);
        }
        catch (Exception e) {
            throw new AppenderLoggingException("Failed to instantiate entity class [" + this.entityClassName + "].", e);
        }
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = this.entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist((Object)entity);
            transaction.commit();
        }
        catch (Exception e2) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + e2.getMessage(), e2);
        }
        finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    
    public static JPADatabaseManager getJPADatabaseManager(final String name, final int bufferSize, final Class<? extends AbstractLogEventWrapperEntity> entityClass, final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, final String persistenceUnitName) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, entityClass, entityConstructor, persistenceUnitName), (ManagerFactory<JPADatabaseManager, FactoryData>)JPADatabaseManager.FACTORY);
    }
    
    static {
        FACTORY = new JPADatabaseManagerFactory();
    }
    
    private static final class FactoryData extends AbstractFactoryData
    {
        private final Class<? extends AbstractLogEventWrapperEntity> entityClass;
        private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
        private final String persistenceUnitName;
        
        protected FactoryData(final int bufferSize, final Class<? extends AbstractLogEventWrapperEntity> entityClass, final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, final String persistenceUnitName) {
            super(bufferSize);
            this.entityClass = entityClass;
            this.entityConstructor = entityConstructor;
            this.persistenceUnitName = persistenceUnitName;
        }
    }
    
    private static final class JPADatabaseManagerFactory implements ManagerFactory<JPADatabaseManager, FactoryData>
    {
        @Override
        public JPADatabaseManager createManager(final String name, final FactoryData data) {
            return new JPADatabaseManager(name, data.getBufferSize(), data.entityClass, data.entityConstructor, data.persistenceUnitName, null);
        }
    }
}
