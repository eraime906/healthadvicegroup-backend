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
        JsonObject json = super.deserializeBody(request.body());
        if (!json.has("usernameOrEmail") || !json.has("password")) {
            response.body("Missing username/email or password field");
            response.status(400);
            return response;
        }
        String usernameOrEmail = json.get("usernameOrEmail").getAsString();
        String password = json.get("password").getAsString();
        UserAccount account = AccountManager.getByUsernameOrEmail(usernameOrEmail);
        if (account == null || !account.verifyPassword(password)) {
            // To uphold security we don't want to be specific with what was incorrect
            response.body("Invalid username, email or password");
            response.status(401);
            return response;
        }
        response.body("OK");
        return response;
    }
}