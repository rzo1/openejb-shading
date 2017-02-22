package com.github.rzo1;

import com.github.rzo1.domain.DemoObject;
import com.github.rzo1.service.DemoService;
import com.github.rzo1.service.DemoServiceImpl;
import org.apache.openejb.config.EjbModule;
import org.apache.openejb.jee.EjbJar;
import org.apache.openejb.jee.StatelessBean;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.testing.ApplicationComposers;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.Module;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Stateless
public class DemoMain    {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DemoMain.class);

    @Inject
    private DemoService demoService;

    @Module
    @Classes({ DemoServiceImpl.class, DemoObject.class })
    public EjbModule application() {
        final EjbJar ejbJar = new EjbJar().enterpriseBean(new StatelessBean(DemoMain.class));
        ejbJar.addEnterpriseBean(new StatelessBean(DemoServiceImpl.class));

        final EjbModule ejbModule = new EjbModule(ejbJar);

        ejbModule.setModuleId("batchee-shared-components");
        return ejbModule;
    }

    @Module
    public PersistenceUnit persistence() {
        PersistenceUnit unit = new PersistenceUnit("demo");
        unit.setJtaDataSource("demo");
        unit.setNonJtaDataSource("demoJTA");
        unit.getClazz().add(DemoObject.class.getName());
        return unit;
    }

    @Configuration
    public Properties configuration() throws URISyntaxException {
        final Properties properties = new Properties();
        properties.setProperty("openejb.jaxrs.application", "false");

        Path launchPath = Paths.get(DemoMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        properties.setProperty("openejb.configuration", launchPath.toAbsolutePath() + "/conf/openejb.xml");

        properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.put("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");


        return properties;
    }
    /**
     * @param args containing  the project oid and the paths to the csv files to parse. First argument specifies the
     *             eventCSV file, second
     *             argument the speakerCSV file. Both must be pre-processed according to RZs instructions.
     * @throws Exception    thrown if something went wrong.
     */
    public static void main(String[] args) throws Exception {

        logger.info("Application container start...");
        try {

            final DemoMain app = new DemoMain();
            new ApplicationComposers(app).evaluate(app, () -> {
                DemoObject demoObject = new DemoObject();

                long oid = app.demoService.store(demoObject);

                demoObject = app.demoService.get(oid);

                logger.info("Object: {}", demoObject.toString());
            });


        } catch (final Exception e) {
            logger.error("Application terminating with exception: ", e);
            throw e;
        }
    }


}
