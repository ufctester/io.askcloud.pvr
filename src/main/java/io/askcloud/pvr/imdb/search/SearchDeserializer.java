package io.askcloud.pvr.imdb.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.askcloud.pvr.api.pvr.PlexPVRManager;


public class SearchDeserializer extends StdDeserializer<SearchObject> {

	private static Logger log = PlexPVRManager.getInstance().getLogger();
    private final Map<String, Class<? extends SearchObject>> registry = new HashMap<>();

    public SearchDeserializer() {
        super(SearchObject.class);
    }

    public void registerSearchObject(String uniqueAttribute, Class<? extends SearchObject> searchClass) {
        registry.put(uniqueAttribute, searchClass);
    }

    @Override
    public SearchObject deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = (ObjectNode) mapper.readTree(jp);
        Class<? extends SearchObject> searchClass = null;
        Iterator<Map.Entry<String, JsonNode>> elementsIterator = root.fields();

        while (elementsIterator.hasNext()) {
            Map.Entry<String, JsonNode> element = elementsIterator.next();
            String name = element.getKey();
            log.info("Name: " + name + " value: " + element.getValue().asText()); 

            if (registry.containsKey(name)) {
                searchClass = registry.get(name);
                log.info("Using class: " + searchClass.getSimpleName());
                break;
            }
        }

        if (searchClass == null) {
            log.info("END: No search class!");
            return new SearchObject();
        }

        SearchObject so = mapper.readValue(jp, searchClass);
        log.info("SO: " + so.toString());
        log.info("END");
        return so;
    }
}
