package com.example.retrofitgridview;

import java.io.Serializable;

public class Author implements Serializable {

    private int birth_year;
    private int death_year;
    private String name;

    public int getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(int birth_year) {
        this.birth_year = birth_year;
    }

    public int getDeath_year() {
        return death_year;
    }

    public void setDeath_year(int death_year) {
        this.death_year = death_year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{" +
                "birth_year=" + birth_year +
                ", death_year=" + death_year +
                ", name='" + name + '\'' +
                '}';
    }
}
