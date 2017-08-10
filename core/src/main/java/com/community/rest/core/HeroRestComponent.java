package com.community.rest.core;
 
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
     
import com.community.rest.core.HeroTextBean;
    
   
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
     
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
    
    
   
import javax.jcr.Node;
import javax.jcr.Session;
     
  
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
   
   
import org.apache.http.client.methods.HttpGet;  
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
  
import java.io.StringReader ;
import java.io.StringWriter;
  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
    
public class HeroRestComponent
extends WCMUsePojo
{
    
     /** The hero text bean that stores values returned by the RestFul Web Service. */
    private HeroTextBean heroTextBean = null;
         
    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
                     
      
    @Override
    public void activate() throws Exception {
             
             
             
        Node currentNode = getResource().adaptTo(Node.class);
            
        heroTextBean = new HeroTextBean();
           
           
        //Set variables - we need two cities to pass to the Restful web service
        String toCity = "Ottawa" ;
        String fromCity =  "Dallas" ;
  
           
              
        //Make a Restful Web Service Call 
        if(currentNode.hasProperty("jcr:tocity")){
            toCity = currentNode.getProperty("./jcr:tocity").getString();
        }
        if(currentNode.hasProperty("jcr:fromcity")){
            fromCity = currentNode.getProperty("./jcr:fromcity").getString();
        }
          
          
        log.info("**** TO CITY IS "+ toCity );
             
        //Get Restful Web Service Data
        StringReader ss = new StringReader(getJSON(toCity,fromCity))   ;
  
  
          
          
        //Read the response using GSON
        JsonReader reader = new JsonReader(new BufferedReader(ss));
        reader.setLenient(true);
  
        Response r = (new Gson().fromJson(reader, Response.class));
         
                       
        Distance dd =  r.getRows()[0].getElements()[0].getDistance();
        Duration du =  r.getRows()[0].getElements()[0].getDuration();
          
        //Set Bean to store the values
        heroTextBean.setToCity(toCity);
        heroTextBean.setFromCity(fromCity);
        heroTextBean.setDistance(dd.getText());
        heroTextBean.setDuratione(du.getText());
               
    }
         
         
         
    public HeroTextBean getHeroTextBean() {
        return this.heroTextBean;
    }
       
          
   //Invokes a third party Restful Web Service and returns the results in a JSON String
    private static String getJSON(String toCity, String fromCity)
    {
  
        try
        {
   
  
            DefaultHttpClient httpClient = new DefaultHttpClient();
  
            HttpGet getRequest = new HttpGet("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + toCity +"&destinations="+fromCity+"&sensor=false");
            getRequest.addHeader("accept", "application/json");
  
            HttpResponse response = httpClient.execute(getRequest);
  
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
  
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
  
            String output;
            String myJSON="" ;
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                myJSON = myJSON + output;
            }
  
  
            httpClient.getConnectionManager().shutdown();
            return myJSON ;
        }
  
        catch (Exception e)
        {
            e.printStackTrace() ;
        }
  
        return null;
    }
  
       
    class Element{
        Duration duration;
        Distance distance;
        String status;
  
  
        public void setDuration(Duration val)
        {
            duration = val ;
        }
  
  
        public Duration getDuration()
        {
            return duration ;
        }
  
        public void setDistance(Distance val)
        {
            distance = val ;
        }
  
  
        public Distance getDistance()
        {
            return distance ;
        }
  
        public void setStatus(String val)
        {
            status = val ;
        }
  
        public String  getStatus()
                    {
            return status ;
        }
  
    }
  
  
  
  
    class Item {
        private Element[] elements;
  
  
        public void setElements(Element[] elements1)
        {
            elements=elements1;
        }
  
        public Element[] getElements()
        {
           return elements;
        }
  
    }
  
  
    class Response {
     
        private String status;
        private String[] destination_addresses;
        private String[] origin_addresses;
        private Item[] rows;
  
        public String getStatus() {
            return status;
        }
  
        public void setStatus(String status) {
            this.status = status;
        }
  
        public String[] getDestination_addresses() {
            return destination_addresses;
        }
  
        public void setDestination_addresses(String[] destination_addresses) {
            this.destination_addresses = destination_addresses;
        }
  
        public String[] getOrigin_addresses() {
            return origin_addresses;
        }
  
        public void setOrigin_addresses(String[] origin_addresses) {
            this.origin_addresses = origin_addresses;
        }
  
        public Item[] getRows() {
            return rows;
        }
  
        public void setRows(Item[] rows) {
            this.rows = rows;
        }
    }
  
    class Distance {
        private String text;
        private String value;
  
        public String getText() {
            return text;
        }
  
        public void setText(String text) {
            this.text = text;
        }
  
        public String getValue() {
            return value;
        }
  
        public void setValue(String value) {
            this.value = value;
        }
    }
  
    class Duration {
        private String text;
        private String value;
  
        public String getText() {
            return text;
        }
  
        public void setText(String text) {
            this.text = text;
        }
  
        public String getValue() {
            return value;
        }
  
        public void setValue(String value) {
            this.value = value;
        }
    }
  
    class Elements {
        Duration duration[];
        Distance distance[];
        String status;
    }
       
      
}