package com.africastalking.example;

import com.google.gson.Gson;
import com.africastalking.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    private static final int HTTP_PORT = 3000;
    private static final int RPC_PORT = 3001;
    private static final String USERNAME = "at2fa";
    private static final String API_KEY = "8c940cd77db666ca100e9dd0d784191ada2ee3eaa1d0a952170a68595313f4ab";

    private static Gson gson = new Gson();

    private static HandlebarsTemplateEngine hbs = new HandlebarsTemplateEngine("/views");
    private static ATServer server;
    private static SMSService sms;

    private static void setupAfricastalking() {
        // SDK Server
        server = new ATServer(RPC_PORT, USERNAME, API_KEY);
        sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
    }

    public static void main(String[] args) throws MalformedURLException {

        setupAfricastalking();

        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions

        staticFiles.location("/public");

        port(HTTP_PORT);

        get("/", (req, res) -> {
            Map<String, Object> data = new HashMap<>();
            data.put("req", req.pathInfo());
            return render("index", data);
        });

        post("/auth/login", (req, res) -> {
            // Check username and password....
            HashMap<String, Object> data = new HashMap<>();
            data.put("port", RPC_PORT);
            data.put("token", server.generateToken());
            return data;
        }, gson::toJson);

        // Send SMS on server, or let client send from their end
        post("/auth/register/:phone", (req, res) -> sms.send("Welcome to Awesome Company", new String[] {req.params("phone")}));
    }

    private static String render(String view, Map data) {
        return hbs.render(new ModelAndView(data, view + ".hbs"));
    }

}
