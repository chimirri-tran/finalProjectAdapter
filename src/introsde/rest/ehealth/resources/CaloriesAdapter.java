package introsde.rest.ehealth.resources;

import java.io.IOException;
import java.net.URI;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;
import org.xml.sax.SAXException;

@Stateless
@LocalBean
@Path("/caloriesAdapter")
public class CaloriesAdapter {
	
	static String serverUri = "https://api.edamam.com/api/nutrition-data";
	
	public static Response makeRequest(String path, String mediaType, String method, 
			String input){

		URI server = UriBuilder.fromUri(serverUri).build();
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(server);
		Response response = null;
		try {
			int a = Integer.parseInt(input.substring(0));
			if(!input.substring(1).equals(" ")){
				input = a + " " + input.substring(1,input.length());
			}
		} catch (Exception e) {
			input = "1 " + input;
		}
		if(method=="get")
		response = service.path(path)
				.queryParam("app_id", "6cd17711")
				.queryParam("app_key", "ace237ed2707a1812542f5816b11a581")
				.queryParam("ingr", input)
				.request(mediaType).accept(mediaType)
				.get(Response.class);

		return response;

	}
	
	public String responseToString(Response response){
		
		String content = response.readEntity(String.class);
		return content;
	}
	
    @GET
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public String getCalories(@DefaultValue("apple") @QueryParam("food") String food) 
    		throws ParserConfigurationException, SAXException, IOException {
	
		String path = "";
        System.out.println(this.getClass()+"pre");	
        Response response = makeRequest(path, MediaType.APPLICATION_JSON, "get", food);
	
        String caloriesString = responseToString(response);
        System.out.println(this.getClass()+"pos");
        
        return caloriesString;

    }

}
