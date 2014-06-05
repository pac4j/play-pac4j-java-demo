package model;

import java.io.IOException;

import play.twirl.api.Content;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonContent implements Content {
    
    private final JsonNode json;
    private final String contentType = "application/json";
    
    public JsonContent(final String content) {
        super();
        final ObjectMapper mapper = new ObjectMapper();
        this.json = mapper.createObjectNode();
        ((ObjectNode) this.json).put("content", content);
    }
    
    @Override
    public String body() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.json);
        } catch (final JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    
    @Override
    public String contentType() {
        return this.contentType;
    }
    
}
