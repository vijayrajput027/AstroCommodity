package com.mohanastrology.commodity.javafiles;

/**
 * Created by niraj on 11/27/2015.
 */
public class ProblemSolutionPojo {


    public String id;
    public String name;

    public ProblemSolutionPojo(String id,String name)
    {
        this.id=id;
        this.name=name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }



}
