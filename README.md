A little break down example for a [Stackoverflow question](http://stackoverflow.com/questions/42293912/building-a-standalone-openejb-jar-file) related to `maven-shade-plugin` and `openEJB`

Changes on this branch according to [Stackoverflow User P. Merkle](http://stackoverflow.com/a/42378393/4506678).

##Base Setting:

- `demo-services`: Hibernate Service and one Entity, EJB Module
- `demo-main`: Module for Shading containing the `DemoMain` class to start the `EJBContainer`. Shading happens here.

##Problem:

- Starting `DemoMain` via IDE (IntelliJ 2016.2.5) does work.

- Starting the shaded jar via `java -jar demo-shade-1.0-SNAPSHOT.jar` and the `conf/openejb.xml` in the same directory, it leads to:
 
 ```
INFORMATION - OpenWebBeans Container is starting...
INFORMATION - Adding OpenWebBeansPlugin : [CdiPlugin]
SCHWERWIEGEND - CDI Beans module deployment failed
java.lang.NullPointerException
        at org.apache.openejb.cdi.CdiScanner.handleBda(CdiScanner.java:271)
        at org.apache.openejb.cdi.CdiScanner.init(CdiScanner.java:148)
        at org.apache.openejb.cdi.OpenEJBLifecycle.startApplication(OpenEJBLifecycle.java:179)
        at org.apache.openejb.cdi.ThreadSingletonServiceImpl.initialize(ThreadSingletonServiceImpl.java:189)
        at org.apache.openejb.cdi.CdiBuilder.build(CdiBuilder.java:41)
        at org.apache.openejb.assembler.classic.Assembler.createApplication(Assembler.java:913)
        at org.apache.openejb.assembler.classic.Assembler.createApplication(Assembler.java:717)
        at org.apache.openejb.OpenEjbContainer$Provider.createEJBContainer(OpenEjbContainer.java:342)
        at javax.ejb.embeddable.EJBContainer.createEJBContainer(EJBContainer.java:56)
        at com.github.rzo1.DemoMain.run(DemoMain.java:90)
        at java.lang.Thread.run(Unknown Source)
INFORMATION - Closing DataSource: demoDS
INFORMATION - Closing DataSource: demoDSNonJTA
Exception in thread "Thread-0" java.lang.RuntimeException: org.apache.openejb.OpenEjbContainer$AssembleApplicationException: javax.enterprise.inject.spi.DeploymentException: couldn't start owb context
        at com.github.rzo1.DemoMain.run(DemoMain.java:116)
        at java.lang.Thread.run(Unknown Source)
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
Caused by: org.apache.openejb.OpenEJBRuntimeException: java.lang.NullPointerException
        at org.apache.openejb.cdi.OpenEJBLifecycle.startApplication(OpenEJBLifecycle.java:200)
        at org.apache.openejb.cdi.ThreadSingletonServiceImpl.initialize(ThreadSingletonServiceImpl.java:189)
        ... 7 more
Caused by: java.lang.NullPointerException
        at org.apache.openejb.cdi.CdiScanner.handleBda(CdiScanner.java:271)
        at org.apache.openejb.cdi.CdiScanner.init(CdiScanner.java:148)
        at org.apache.openejb.cdi.OpenEJBLifecycle.startApplication(OpenEJBLifecycle.java:179)
        ... 8 more

```
