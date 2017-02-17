A little break down example for a [Stackoverflow question](http://stackoverflow.com/questions/42293912/building-a-standalone-openejb-jar-file) related to `maven-shade-plugin` and `openEJB`

##Base Setting:

- `demo-services`: Hibernate Service and one Entity, EJB Module
- `demo-main`: Module for Shading containing the `DemoMain` class to start the `EJBContainer`. Shading happens here.

##Problem:

- Starting `DemoMain` via IDE (IntelliJ 2016.2.5) works. No issues occur.
- Starting the shaded jar via `java -jar demo-shade-1.0-SNAPSHOT.jar` and the `conf/openejb.xml` in the same directory, it leads to:
 
 ```
 INFORMATION - PersistenceUnit(name=demo, provider=org.hibernate.jpa.HibernatePersistenceProvider) - provider time 2706ms
 INFORMATION - Jndi(name="java:global/DemoMain/demo-shade-1.0-SNAPSHOT/MEJB!javax.management.j2ee.ManagementHome")
 INFORMATION - Jndi(name="java:global/DemoMain/demo-shade-1.0-SNAPSHOT/MEJB")
 INFORMATION - Jndi(name="java:global/DemoMain/demo-shade-1.0-SNAPSHOT/openejb/Deployer!org.apache.openejb.assembler.Deployer")
 INFORMATION - Jndi(name="java:global/DemoMain/demo-shade-1.0-SNAPSHOT/openejb/Deployer")
 INFORMATION - Jndi(name="java:global/DemoMain/demo-shade-1.0-SNAPSHOT/openejb/ConfigurationInfo!org.apache.openejb.assembler.classic.cmd.ConfigurationInfo")
 INFORMATION - Jndi(name="java:global/DemoMain/demo-shade-1.0-SNAPSHOT/openejb/ConfigurationInfo")
 INFORMATION - Jndi(name="java:global/DemoMain/demo-shade-1.0-SNAPSHOT/DemoServiceImpl!com.github.rzo1.service.DemoService")
 INFORMATION - Jndi(name="java:global/DemoMain/demo-shade-1.0-SNAPSHOT/DemoServiceImpl")
 INFORMATION - Existing thread singleton service in SystemInstance(): org.apache.openejb.cdi.ThreadSingletonServiceImpl@557c8e7e
 INFORMATION - Closing DataSource: demoDS
 INFORMATION - Closing DataSource: demoDSNonJTA
 Exception in thread "Thread-0" java.lang.RuntimeException: org.apache.openejb.OpenEjbContainer$AssembleApplicationException: javax.enterprise.inject.spi.DeploymentException: couldn't start owb context
         at com.github.rzo1.DemoMain.run(DemoMain.java:116)
         at java.lang.Thread.run(Thread.java:745)
 Caused by: org.apache.openejb.OpenEjbContainer$AssembleApplicationException: javax.enterprise.inject.spi.DeploymentException: couldn't start owb context
         at org.apache.openejb.OpenEjbContainer$Provider.createEJBContainer(OpenEjbContainer.java:346)
         at javax.ejb.embeddable.EJBContainer.createEJBContainer(EJBContainer.java:56)
         at com.github.rzo1.DemoMain.run(DemoMain.java:90)
         ... 1 more
 Caused by: javax.enterprise.inject.spi.DeploymentException: couldn't start owb context
         at org.apache.openejb.cdi.ThreadSingletonServiceImpl.initialize(ThreadSingletonServiceImpl.java:191)
         at org.apache.openejb.cdi.CdiBuilder.build(CdiBuilder.java:41)
         at org.apache.openejb.assembler.classic.Assembler.createApplication(Assembler.java:913)
         at org.apache.openejb.assembler.classic.Assembler.createApplication(Assembler.java:717)
         at org.apache.openejb.OpenEjbContainer$Provider.createEJBContainer(OpenEjbContainer.java:342)
         ... 3 more
 Caused by: org.apache.webbeans.exception.WebBeansException: Wrong startup object.
         at org.apache.webbeans.web.lifecycle.WebContainerLifecycle.getServletContext(WebContainerLifecycle.java:227)
         at org.apache.webbeans.web.lifecycle.WebContainerLifecycle.startApplication(WebContainerLifecycle.java:86)
         at org.apache.openejb.cdi.ThreadSingletonServiceImpl.initialize(ThreadSingletonServiceImpl.java:189)
         ... 7 more
```
