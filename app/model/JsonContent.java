package model;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import play.mvc.Content;

public class JsonContent implements Content {

	private JsonNode json;
	private String contentType = "application/json";
	
	public JsonContent(String content) {
		super();
		ObjectMapper mapper = new ObjectMapper();
		json = mapper.createObjectNode();
		((ObjectNode)json).put("content", content);
	}

	@Override
	public String body() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String contentType() {
		return contentType;
	}

}
