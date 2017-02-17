package com.github.rzo1.service;

import com.github.rzo1.domain.DemoObject;

/**
 * Created by rz on 17.02.2017.
 */
public interface DemoService {

    long store(DemoObject persistentObject);

    DemoObject get(long oid);
}
