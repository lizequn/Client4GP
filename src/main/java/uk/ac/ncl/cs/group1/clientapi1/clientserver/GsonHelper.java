package uk.ac.ncl.cs.group1.clientapi1.clientserver;

import com.google.gson.*;
import uk.ac.ncl.cs.group1.clientapi1.util.Base64Coder;

import java.lang.reflect.Type;

/**
 * @author ZequnLi
 *         Date: 14-3-17
 */
public class GsonHelper {
    public static final Gson customGson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class,
            new ByteArrayToBase64TypeAdapter()).create();

    // Using Android's base64 libraries. This can be replaced with any base64 library.
    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64Coder.decode(json.getAsString());
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64Coder.encode(src));
        }
    }
}
