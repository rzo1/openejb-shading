package com.github.rzo1.domain;


import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "DemoObject")
public class DemoObject implements Serializable {

    private static final long INVALID_OBJECT_ID = -1;
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

