package org.healthadvicegroup.endpoint.impl;

import com.google.gson.JsonObject;
import org.healthadvicegroup.account.AccountManager;
import org.healthadvicegroup.account.UserAccount;
import org.healthadvicegroup.endpoint.Endpoint;
import spark.Request;
import spark.Response;

public class CredentialsValidationEndpoint extends Endpoint {

    @Override
    public Response handle(Request request, Response response) {
        JsonObject json = super.bodyToJsonObject(request.body());
        if (!json.has("username") || !json.has("email") || !json.has("password")) {
            response.body("Missing username/email or password field");
            response.status(400);
            return response;
        }
        String username = json.get("username").getAsString();
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();
        UserAccount account = AccountManager.getByUsernameOrEmail(username);
        if (account == null // No account found
                || !account.getEmail().equalsIgnoreCase(email) // Mismatched email
                || !account.getUsername().equalsIgnoreCase(username)  // Mismatched username
                || !account.verifyPassword(password)) { // Invalid password
            // To uphold security we don't want to be specific with what was incorrect
            response.body("Invalid username, email or password");
            response.status(401);
            return response;
        }
        response.body("OK");
        response.status(204);
        return response;
    }
}
