package org.healthadvicegroup.endpoint.impl;

import com.google.gson.JsonObject;
import org.healthadvicegroup.account.UserAccount;
import org.healthadvicegroup.endpoint.Endpoint;
import spark.Request;
import spark.Response;

public class AccountCreationEndpoint extends Endpoint {
    @Override
    public Response handle(Request request, Response response) {
        // Deserialize request body
        JsonObject json = super.deserializeBody(request.body());

        // Ensure all parameters are present
        if (!json.has("username") || !json.has("email") || !json.has("password")) {
            response.body("Missing username, email or password field");
            response.status(400);
            return response;
        }
        String username = json.get("username").getAsString();
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();

        // Validate input lengths individually to provide specific errors
        if (username.length() > 16) {
            response.body("Username too long, max length is 16 characters");
            response.status(400);
            return response;
        }
        if (email.length() > 64) {
            response.body("Email too long, max length is 64 characters");
            response.status(400);
            response.body();
        }
        if (password.length() > 64) {
            response.body("Password is too long, max length is 64 characters");
            response.status(400);
            return response;
        }
        // Create user account
        new UserAccount(
                username,
                email,
                password);

        return response;
    }
}
