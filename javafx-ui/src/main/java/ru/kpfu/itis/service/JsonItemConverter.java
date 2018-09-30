package ru.kpfu.itis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ru.kpfu.itis.exception.JsonParseException;
import ru.kpfu.itis.model.Item;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonItemConverter {

    private ObjectMapper mapper;

    public JsonItemConverter() {
        mapper = new ObjectMapper();
    }


    public Item convert(InputStream inputStream) throws JsonParseException {
        try {
            return mapper.readValue(inputStream, Item.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    public String convert(Item item) throws JsonParseException {
        try {
            return mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    public List<Item> convertToList(InputStream inputStream) throws JsonParseException {

        try {


            JsonNode tree = mapper.readTree(inputStream);

            ArrayNode itemsNode = (ArrayNode) tree.get("_embedded").get("item");

            return mapper.readValue(itemsNode.toString(),
                    mapper.getTypeFactory().constructCollectionType(List.class, Item.class));

        } catch (IOException e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }
}
