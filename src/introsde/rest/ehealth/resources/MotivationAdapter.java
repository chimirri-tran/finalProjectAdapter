package introsde.rest.ehealth.resources;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
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
@Path("/motivationAdapter")
public class MotivationAdapter {
	
	static String serverUri = "http://api.forismatic.com/api/1.0/jsonp";
	
	public static Response makeRequest(String path, String mediaType, String method, String input){

		URI server = UriBuilder.fromUri(serverUri).build();
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(server);
		Response response = null;
		if(method=="get")
		response = service.path(path)
				.queryParam("method", "getQuote")
				.queryParam("key", "457653")
				.queryParam("format", "json")
				.queryParam("lang", "en")
				.queryParam("Accept", "json")
				.request(mediaType).accept(mediaType)
				.get(Response.class);

		return response;

	}
	
	public JSONObject responseToJson(Response response){
		
		String content = response.readEntity(String.class);
		JSONObject obj = new JSONObject(content);
		String quoteText = obj.get("quoteText").toString();

		JSONObject quoteJson = new JSONObject();
		quoteJson.append("motivationalPhrase",quoteText);
		return quoteJson;
	}
	
	public String jsontoString(JSONObject motivation){

        String motivationS = motivation.toString();
        return motivationS;
	}
	
    @GET
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public String getMotivation() throws ParserConfigurationException, SAXException, IOException {
	
		String result = "ERROR";		
		String path = "";
        System.out.println("Getting motivation");		
        Response response = makeRequest(path, MediaType.APPLICATION_JSON, "get", result);
        JSONObject motivation = responseToJson(response);
        String motivationString = jsontoString(motivation);
        System.out.println(this.getClass()+" post");
        
        return motivationString;

    }
}