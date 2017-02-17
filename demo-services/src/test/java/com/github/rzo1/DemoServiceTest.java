package com.github.rzo1;

import com.github.rzo1.domain.DemoObject;
import com.github.rzo1.service.DemoService;
import org.apache.openejb.junit.jee.EJBContainerRunner;
import org.apache.openejb.junit.jee.config.PropertyFile;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

@PropertyFile("openejb-junit.properties")
@RunWith(EJBContainerRunner.class)
public class DemoServiceTest extends AbstractBaseTest {

    @Inject
    private DemoService demoService;

    @Test
    public void test() {
        DemoObject demoObject = new DemoObject();

        long oid = demoService.store(demoObject);

        demoObject = demoService.get(oid);

        assertNotNull(demoObject);


    }

}
