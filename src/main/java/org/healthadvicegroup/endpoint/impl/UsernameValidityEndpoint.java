package org.healthadvicegroup.endpoint.impl;

import org.healthadvicegroup.account.AccountManager;
import org.healthadvicegroup.endpoint.Endpoint;
import spark.Request;
import spark.Response;

public class UsernameValidityEndpoint extends Endpoint {

    @Override
    public Response handle(Request request, Response response) {
        String username = request.params(":username");
        response.header("sent-username", username);
        response.status(AccountManager.doesUsernameExist(username) ? 204 : 404);
        return response;
    }
}
