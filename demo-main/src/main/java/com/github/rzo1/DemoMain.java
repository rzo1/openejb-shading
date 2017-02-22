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

    private boolean ejbContainerReady;

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
            while (!importer.ejbContainerReady) {
                Thread.sleep(200);
            }
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

            Path launchPath = Paths.get(DemoMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            properties.setProperty("openejb.configuration", launchPath.toAbsolutePath() + "/conf/openejb.xml");

            properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
            properties.put("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");

            // This is the line starting the EJB container
            ejbContainer = EJBContainer.createEJBContainer(properties);
            ejbContainer.getContext().bind("inject", this);

            ejbContainerReady = true;

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
            ejbContainerReady = false;
            throw new RuntimeException(e);
        } finally {
            if (ejbContainer != null) {
                ejbContainer.close();
            }
        }

    }

}
