package repository;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import model.StudentCustomer;
import model.VIPCustomer;

import java.lang.reflect.Type;

/**
 * Đọc/ghi đa hình cho Customer: dựa vào field "customerType" để chọn đúng
 * subclass (NormalCustomer/StudentCustomer/VIPCustomer) khi đọc, và dùng đúng
 * runtime class khi ghi để không mất các field riêng của từng loại khách.
 */
class CustomerTypeAdapter implements JsonSerializer<Customer>, JsonDeserializer<Customer> {

    @Override
    public Customer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        CustomerType type = CustomerType.NORMAL;
        if (obj.has("customerType") && !obj.get("customerType").isJsonNull()) {
            type = CustomerType.valueOf(obj.get("customerType").getAsString());
        }
        switch (type) {
            case STUDENT:
                return context.deserialize(obj, StudentCustomer.class);
            case VIP:
                return context.deserialize(obj, VIPCustomer.class);
            default:
                return context.deserialize(obj, NormalCustomer.class);
        }
    }

    @Override
    public JsonElement serialize(Customer src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
