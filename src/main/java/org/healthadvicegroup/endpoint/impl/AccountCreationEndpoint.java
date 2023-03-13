package org.healthadvicegroup.endpoint.impl;

import com.google.gson.JsonObject;
import org.healthadvicegroup.Main;
import org.healthadvicegroup.account.UserAccount;
import org.healthadvicegroup.endpoint.Endpoint;
import spark.Request;
import spark.Response;

public class AccountCreationEndpoint extends Endpoint {
    @Override
    public Response handle(Request request, Response response) {
        // Deserialize request body
        JsonObject json = super.deserializeBody(request.body());

        // Create user account
        new UserAccount(
                json.get("username").getAsString(),
                json.get("email").getAsString(),
                json.get("password").getAsString());

        return response;
    }
}
