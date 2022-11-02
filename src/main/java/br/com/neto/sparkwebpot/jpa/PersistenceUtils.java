package br.com.neto.sparkwebpot.jpa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.hibernate.internal.SessionImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class PersistenceUtils {
    private static final EntityManagerFactory entityManagerFactory;
    private static final Logger log = LogManager.getLogger();
    private static final AtomicInteger counter = new AtomicInteger(0);

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("AppUnit");
    }

    public static void main(String[] args) {
        //noinspection ConstantValue
        if (false) saveAndList();
        else multiThreadTest();
    }

    protected static void create(@NonNull EntityManager entityManager, Book book) {
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(book);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            log.error("Error persisting Book", e);
        }
    }

    @SuppressWarnings("rawtypes")
    protected static List findAll(@NonNull EntityManager entityManager) {
        return entityManager.createQuery("select b from Book b").getResultList();
    }

    private static void saveAndList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        final var b = new Book();
        b.setTitle("Bible");
        log.info("b = {}", b);

        create(entityManager, b);
        log.info("After persist: b={}", b);

        @SuppressWarnings("rawtypes") List resultList = findAll(entityManager);
        log.info("resultList = {}", resultList);
    }

    private static void save() {

        log.info("Saving: counter.incrementAndGet() = {}", counter.incrementAndGet());

        final var b = new Book();
        b.setTitle("Bible");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            create(entityManager, b);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            entityManager.close();
        }
    }

    private static void openTrx() {
        log.info("openTrx: counter.incrementAndGet() = {}", counter.incrementAndGet());
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Connection connection = ((SessionImpl) entityManager).connection();
        log.info("connection = {}", connection);
    }

    private static void multiThreadTest() {

        Runnable suplier = PersistenceUtils::openTrx;

        Executor executor = Executors.newFixedThreadPool(150);

        var initTime = System.nanoTime();
        var finalTime = initTime;
        var futures = IntStream.range(0, 10)
                .mapToObj(n -> CompletableFuture.runAsync(suplier, executor))
                .toList();

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
            finalTime = System.nanoTime();
        } catch (Exception e) {
            log.error("", e);
        }

        log.info("Encerrado: {} us", (finalTime - initTime) / 1000);
        System.exit(0);
    }

}