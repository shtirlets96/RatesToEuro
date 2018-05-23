package app.index;

import app.Applicaion;

import static spark.Spark.get;
import static spark.Spark.post;

public class IndexController {
    public static void setControl(){

        get("/*", (request, response) -> {
            response.redirect("/index.html");
            return null;
        });

        post("/index/EuroRate", (request, response) -> {
            response.type("application/json");
            response.header("Content-Encoding", "UTF-8");
            response.header("Access-Control-Allow-Origin", "*");
            response.body(Applicaion.requestResult);
            return Applicaion.requestResult;
        });
    }



}
