package com.sunil.utilis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // ✅ Generates getters, setters, toString, equals, and hashCode
@AllArgsConstructor // ✅ Constructor with all fields
@NoArgsConstructor  // ✅ Default constructor (important for deserialization)
public class ApiResponse {
    private boolean success;
    private String message;
    private String token;
}

