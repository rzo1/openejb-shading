package com.github.rzo1;

import org.apache.openejb.OpenEjbContainer;
import org.apache.openejb.junit.jee.EJBContainerRule;
import org.apache.openejb.junit.jee.InjectRule;
import org.apache.openejb.junit.jee.config.Properties;
import org.apache.openejb.junit.jee.config.Property;
import org.apache.openejb.junit.jee.resources.TestResource;
import org.junit.ClassRule;
import org.junit.Rule;

import javax.naming.Context;

@Properties(
        @Property(
                key = OpenEjbContainer.OPENEJB_EJBCONTAINER_CLOSE,
                value = OpenEjbContainer.OPENEJB_EJBCONTAINER_CLOSE_SINGLE))
public abstract class AbstractBaseTest {

    static {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
    }

    @ClassRule
    public static final EJBContainerRule CONTAINER_RULE = new EJBContainerRule();

    @Rule
    public final InjectRule injectRule = new InjectRule(this, CONTAINER_RULE);

    @TestResource
    private Context ctx;


}
