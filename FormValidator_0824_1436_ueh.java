// 代码生成时间: 2025-08-24 14:36:08
import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class FormValidator {

    // Define the required fields and their expected data types
    private static final Map<String, Class<?>> REQUIRED_FIELDS = new HashMap<>();
    static {
        REQUIRED_FIELDS.put("username", String.class);
        REQUIRED_FIELDS.put("email", String.class);
        REQUIRED_FIELDS.put("age", Integer.class);
    }

    /**
     * Validates the form data and returns an error message if validation fails.
     * 
     * @param formData The form data to be validated.
     * @return A map containing error messages for each field that failed validation.
     */
    public static Map<String, String> validateFormData(Map<String, Object> formData) {
        Map<String, String> errors = new HashMap<>();
        for (Map.Entry<String, Class<?>> entry : REQUIRED_FIELDS.entrySet()) {
            String fieldName = entry.getKey();
            Class<?> expectedType = entry.getValue();
            
            // Check if the field is present in the form data
            if (!formData.containsKey(fieldName)) {
                errors.put(fieldName, "Field is required."