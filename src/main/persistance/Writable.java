package persistance;

import org.json.JSONObject;

public interface Writable {
    // EFFECTS: returns this as JSON object, borrowed from JsonSerializationDemo
    // Borrowed from JsonSerializationDemo
    JSONObject toJson();
}
