package com.africastalking.example;

import com.google.gson.Gson;
import com.africastalking.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    private static final int HTTP_PORT = 3000;
    private static final int RPC_PORT = 3001;
    private static final String USERNAME = BuildConfig.USERNAME;
    private static final String API_KEY = BuildConfig.API_KEY;

    private static Gson gson = new Gson();

    private static HandlebarsTemplateEngine hbs = new HandlebarsTemplateEngine("/views");
    private static Server server;
    private static SmsService sms;

    private static void log(String message) {
        System.out.println(message);
    }

    private static void setupAfricastalking() throws IOException {
        // SDK Server
        AfricasTalking.initialize(USERNAME, API_KEY);
        sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        server = new Server();
        server.startInsecure(RPC_PORT);
    }

    public static void main(String[] args) throws IOException {

        InetAddress host = Inet4Address.getLocalHost();
        log("\n");
        log(String.format("SDK Server: %s:%d", host.getHostAddress(), RPC_PORT));
        log(String.format("HTTP Server: %s:%d", host.getHostAddress(), HTTP_PORT));
        log("\n");

        setupAfricastalking();

        port(HTTP_PORT);

        staticFiles.location("/public");
        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions

        get("/", (req, res) -> {
            Map<String, Object> data = new HashMap<>();
            data.put("req", req.pathInfo());
            return render("index", data);
        });

        // Send SMS on server, or let client send from their end
        post("/auth/register/:phone", (req, res) -> sms.send("Welcome to Awesome Company", "AT2FA", new String[] {req.params("phone")}));
    }

    private static String render(String view, Map data) {
        return hbs.render(new ModelAndView(data, view + ".hbs"));
    }

}
