package introsde.rest.ehealth.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

@Stateless
@LocalBean
@Path("/exercizeAdapter")
public class ExercizeAdapter {
	
	static String serverUri = "http://wger.de/api/v2/exercise/";
	
	public static Response makeRequest(String path, String mediaType, String method, 
			String input) throws IOException{

		URI server = UriBuilder.fromUri(serverUri).build();
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(server);
		Response response = null;
		System.out.println("preget");
		
		response = service
		        .path(path)
				.request()
		        .header("Authorization", "Token 10b0e36e7ec7308275f75f1ac8385373198b55be")
		        .header("Content-Type", "application/json;charset=UTF-8")
				.get(Response.class);
		
		System.out.println("postget");
		return response;

	}
	
	public String responseToString(Response response){
		
		String content = response.readEntity(String.class);
		
		content = content.replace("&quot;", "\"");
		content = content.replace("&lt;", "<");
		content = content.replace("&gt;", ">");
		int index = 0;
		JSONObject allExercizesObject = new JSONObject();
		JSONArray allExercizesArray = new JSONArray();
		while (index != -1){
			index = content.indexOf("\"description\": \"");
			content = content.substring(index+16,content.length());
			String newDesc = content.substring(0,content.indexOf("\","));
			newDesc = newDesc.replaceAll("\n", "");
			if(newDesc.indexOf("<a href=")!=-1){
				newDesc = newDesc.substring(0,newDesc.indexOf("<a href="));
			}
			//newDesc.replaceAll("<[/?][A-Za-z+]>", "");
			newDesc = newDesc.replaceAll("<[^>]*>", "");
			System.out.println(newDesc);
			allExercizesArray.put(newDesc);
		}
		allExercizesObject.put("exercizes", allExercizesArray);
		
		return allExercizesObject.toString();
		//return content;
	}
	
    @GET
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public String getExercize() 
    		throws ParserConfigurationException, SAXException, IOException {
	
		String path = "";
        System.out.println(this.getClass()+"pre");	
        Response response = makeRequest(path, MediaType.APPLICATION_JSON, "get", "");
        String exercizeString = responseToString(response);
        System.out.println(this.getClass()+"pos");	
        
        return exercizeString;

    }

}
