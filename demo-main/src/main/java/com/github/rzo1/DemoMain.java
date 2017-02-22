package com.github.rzo1;

import com.github.rzo1.domain.DemoObject;
import com.github.rzo1.service.DemoService;
import org.apache.openejb.OpenEjbContainer;

import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class DemoMain implements Runnable {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DemoMain.class);

    private static final String applicationName = "DemoMain";

    private java.util.concurrent.CountDownLatch started = new CountDownLatch(1);

    @Inject
    private DemoService demoService;

    /**
     * @param args containing  the project oid and the paths to the csv files to parse. First argument specifies the
     *             eventCSV file, second
     *             argument the speakerCSV file. Both must be pre-processed according to RZs instructions.
     * @throws Exception    thrown if something went wrong.
     */
    public static void main(String[] args) throws Exception {

        logger.info("Application container start...");
        DemoMain importer;
        try {
            importer = new DemoMain();
            final Thread thread = new Thread(importer);
            thread.start();

            // looking of openejb container to come up
            importer.started.await();
            logger.info("Application container start... [OK]");

            DemoObject demoObject = new DemoObject();

            long oid = importer.demoService.store(demoObject);

            demoObject = importer.demoService.get(oid);

            logger.info("Object: {}", demoObject.toString());

            System.exit(0);


        } catch (final Exception e) {
            logger.error("Application terminating with exception: ", e);
            throw e;
        }
    }

    @Override
    public void run() {
        EJBContainer ejbContainer = null;
        try {
            final Properties properties = new Properties();
            properties.setProperty(EJBContainer.APP_NAME, applicationName);
            properties.setProperty(EJBContainer.PROVIDER, OpenEjbContainer.class.getName());
            properties.setProperty(OpenEjbContainer.OPENEJB_EMBEDDED_REMOTABLE, "false");
            properties.setProperty("ejbd.disabled", "true");
            properties.setProperty("ejbds.disabled", "true");
            properties.setProperty("admin.disabled", "true");
            properties.setProperty("openejb.jaxrs.application", "false");

            properties.setProperty("log4j.rootLogger", "warn,C");
            properties.setProperty("log4j.appender.C", "org.apache.log4j.ConsoleAppender");
            properties.setProperty("log4j.appender.C.layout", "org.apache.log4j.PatternLayout");
            properties.setProperty("log4j.appender.C.layout.ConversionPattern", "%d{ISO8601} %-5p[%t] %c(%L):\\n%m%n");
            properties.setProperty("log4j.category.OpenEJB", "debug");
            properties.setProperty("log4j.category.OpenEJB.cdi", "debug");
            properties.setProperty("log4j.category.OpenEJB.options", "debug");
            properties.setProperty("log4j.category.OpenEJB.server", "info");
            properties.setProperty("log4j.category.OpenEJB.startup", "info");
            properties.setProperty("log4j.category.OpenEJB.startup.service", "info");
            properties.setProperty("log4j.category.OpenEJB.startup.config", "info");
            properties.setProperty("log4j.category.org.apache.webbeans", "debug");

            Path launchPath = Paths.get(DemoMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            properties.setProperty("openejb.configuration", launchPath.toAbsolutePath() + "/conf/openejb.xml");

            //http://tomee-openejb.979440.n4.nabble.com/Embedded-Tests-not-found-in-classpath-td979899.html
            properties.setProperty("openejb.deployments.classpath.include", ".*demo.*");


            properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
            properties.put("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver"); // shouldnt be used if usin a managed datasource

            // This is the line starting the EJB container
            ejbContainer = EJBContainer.createEJBContainer(properties);
            ejbContainer.getContext().bind("inject", this);

            started.countDown();

            final CountDownLatch latch = new CountDownLatch(1);
            // Graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        logger.info("Shutting down..");
                        latch.countDown();
                        logger.info("Shutdown completed successfully.");
                    } catch (final Exception e) {
                        logger.error("Graceful shutdown went wrong. SIGKILL (kill -9) if you want.", e);
                    }
                }
            });
            try {
                latch.await();
            } catch (final InterruptedException e) {
                // ignored
            }
        } catch (final Exception e) {
            started.countDown();
            throw new RuntimeException(e);
        } finally {
            if (ejbContainer != null) {
                ejbContainer.close();
            }
        }

    }

}
