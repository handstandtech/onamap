package net.onamap.server.task;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.maps.onamapmodels.AddressComponentType;

public class UppercaseEnumAdapter implements JsonDeserializer<AddressComponentType> {
    @Override
    public AddressComponentType deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            return AddressComponentType.valueOf(json.getAsString().toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}