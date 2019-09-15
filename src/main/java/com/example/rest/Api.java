package com.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api")
public class Api {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
   
    @POST()
    @Path("/adddata")
    @Consumes(MediaType.APPLICATION_JSON)// specifies the request body content
    @Produces(MediaType.TEXT_PLAIN)
    public Response addPerson(String jsondata) {
         String res="Error";
        try{ 
            ObjectMapper mapper = new ObjectMapper();

//JSON from String to Object
PersonData user = mapper.readValue(jsondata, PersonData.class);
        String sql="insert into test (RName) values(?)";
            
            Connection conn=  new  DbConnection().getConnection();
            
            PreparedStatement pst=conn.prepareStatement(sql);
                pst.setString(1,user.getName());
                
                pst.executeUpdate();
                pst.close();
                conn.close();
                res="Data saved Successfully";
         }catch(Exception e){
            //e.printStackTrace();
            res=e.getMessage();
        }
        
        Response response = Response.status(Response.Status.OK)
                          .entity(res)
                            .type(MediaType.APPLICATION_JSON).build();
        return response;
    }
    @GET()
    @Path("/getdata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response  getData()
    {
        try{
            ArrayList<PersonData> data = new ArrayList<>();
          Connection conn=  new  DbConnection().getConnection();
            
            String query = "SELECT * FROM test";

      // create the java statement
      Statement st = conn.createStatement();
      
      // execute the query, and get a java resultset
      ResultSet rs = st.executeQuery(query);
      
      // iterate through the java resultset
      while (rs.next())
      {
        int id = rs.getInt("RID");
        String firstName = rs.getString("RName");
        
        PersonData pd=new PersonData();
        pd.setId(id);
        pd.setName(firstName);
        // print the results
        data.add(pd);
      // return String.format("%s, %s\n", id, firstName);
       
        
      }
      st.close();
      conn.close();
      Response response = Response.status(Response.Status.OK)
                            .entity(data)
                            .type(MediaType.APPLICATION_JSON).build();
        return response;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
