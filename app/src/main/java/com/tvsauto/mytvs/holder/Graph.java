package com.tvsauto.mytvs.holder;

import java.io.Serializable;

/**
 * Created by Zayid on 3/9/2019.
 */

public class Graph implements Serializable{

    private String name, salary;

    public Graph(String name, String salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
