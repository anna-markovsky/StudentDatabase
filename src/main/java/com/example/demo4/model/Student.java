package com.example.demo4.model;

import jakarta.persistence.*;
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int weight;
    private int height;
    private String colour;
    private float gpa;

    // Constructors, getters, setters

    public Student() {
    }

    public Student(String name, int weight, int height, String colour, float gpa) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.colour = colour;
        this.gpa = gpa;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){

        this.name=name;
    }
    public int getWeight(){
        return weight;
    }
    public void setWeight(int weight){
        this.weight=weight;
    }
    public int getHeight(){
        return height;
    }
    public void setHeight(int height){
        this.height=height;
    }
    public String getColour(){return colour;}
    public void setColour(String colour) {
        this.colour = colour;
    }

    public float getGpa(){
        return gpa;
    }
    public void setGpa(float gpa){
        this.gpa=gpa;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


