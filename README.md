A little break down example for a [Stackoverflow question](http://stackoverflow.com/questions/42293912/building-a-standalone-openejb-jar-file) related to `maven-shade-plugin` and `openEJB`

Changes on this branch according to [Stackoverflow User P. Merkle](http://stackoverflow.com/a/42378393/4506678).

##Base Setting:

- `demo-services`: Hibernate Service and one Entity, EJB Module
- `demo-main`: Module for Shading containing the `DemoMain` class to start the `EJBContainer`. Shading happens here.

##Problem:

- Starting `DemoMain` via IDE (IntelliJ 2016.2.5) does work.

- Starting the shaded jar via `java -jar demo-shade-1.0-SNAPSHOT.jar` and the `conf/openejb.xml` in the same directory, work too.
 