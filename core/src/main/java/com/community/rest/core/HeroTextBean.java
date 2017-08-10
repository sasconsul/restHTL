package com.community.rest.core;
 
 
/* Stores data returned from the Restful web service */
public class HeroTextBean {
     
     
    private String fromcity ; 
    private String tocity ; 
    private String distance; 
    private String duration ; 
     
     
     
    public void setDuratione(String duration)
    {
        this.duration = duration ; 
    }
     
    public String getDuration()
    {
        return this.duration ; 
    }
     
     
     
     
    public void setDistance(String distance)
    {
        this.distance = distance ; 
    }
     
    public String getDistance()
    {
        return this.distance ; 
    }
     
     
     
    public void setToCity(String city)
    {
        this.tocity = city ; 
    }
     
    public String getToCity()
    {
        return this.tocity ; 
    }
     
     
     
    public void setFromCity(String city)
    {
        this.fromcity = city ; 
    }
     
    public String getFromCity()
    {
        return this.fromcity ; 
    }
     
 
}