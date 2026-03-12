package com.Ash_Conversion.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            logger.info("Démarrage de l'initialisation JPA (Ash_ConversionPU)...");

            emf = Persistence.createEntityManagerFactory("Ash_ConversionPU");

            logger.info("✅ EntityManagerFactory initialisé avec succès !");
        } catch (Exception e) {
            logger.error("❌ ÉCHEC CRITIQUE : Impossible d'initialiser l'EntityManagerFactory", e);
            
            throw new RuntimeException("Erreur lors de l'initialisation de la base de données", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) {
            emf.close();
            logger.info("EntityManagerFactory fermé proprement.");
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            logger.error("Tentative d'accès à l'EntityManagerFactory alors qu'il est NULL");
            throw new IllegalStateException("EntityManagerFactory non initialisé. Vérifiez les logs de démarrage.");
        }
        return emf;
    }
}
