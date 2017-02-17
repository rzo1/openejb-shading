/**
 *
 *  Copyright (c) 2014-2017 Martin Wiesner, Richard Zowalla, Daniel Zsebedits
 *  						     ALL RIGHTS RESERVED
 *
 * 						Urheber i.S.d. §7 UrhG sind
 *               Martin Wiesner, Richard Zowalla, Daniel Zsebedits
 *
 */
package com.github.rzo1.domain;


import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "DemoObject")
public class DemoObject implements Serializable {

    private static final Long INVALID_OBJECT_ID = -1L;
    private static final long serialVersionUID = 4886218638126313028L;

    @SequenceGenerator(name = "sequence-object", sequenceName = "ID_MASTER_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-object")
    @Column(name = "id")
    protected Long objectID = INVALID_OBJECT_ID;

    @Version
    private int version;

    public DemoObject() {
    }

    public final int getVersion() {
        return version;
    }

    public long getObjectID() {
        return objectID;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getObjectID());
    }

}

