package com.liferon.usermgt.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sequence_generator")
public class Sequencer implements Serializable {

    @Id
    private String name;
    private long count = 0;

    public Sequencer() {
    }

    public Sequencer(String name) {
        this.name = name;
    }
    
    public Sequencer(String name, long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long increment() {
        this.count++;

        return this.count;
    }
}
