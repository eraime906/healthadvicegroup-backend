package org.healthadvicegroup.endpoint;

import com.google.gson.Gson;
import org.healthadvicegroup.Main;
import spark.Response;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(Object o) {
        return new Gson().toJson(o);
    }
}
