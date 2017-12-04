package com.africastalking.example;

import com.africastalking.AfricasTalking;
import com.africastalking.AirtimeService;
import com.africastalking.Logger;
import com.africastalking.PaymentService;
import com.africastalking.Server;
import com.africastalking.SmsService;
import com.africastalking.payment.recipient.Consumer;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class App {

    private static final int HTTP_PORT = 8080;
    private static final int RPC_PORT = 35897;
    private static final String USERNAME = BuildConfig.USERNAME;
    private static final String API_KEY = BuildConfig.API_KEY;

    private static Gson gson = new Gson();

    private static HandlebarsTemplateEngine hbs = new HandlebarsTemplateEngine("/views");
    private static Server server;
    private static SmsService sms;
    private static AirtimeService airtime;
    private static PaymentService payment;

    private static void log(String message) {
        System.out.println(message);
    }

    private static void setupAfricastalking() throws IOException {
        // SDK Server
        AfricasTalking.initialize(USERNAME, API_KEY);
        AfricasTalking.setLogger(new Logger(){
            @Override
            public void log(String message, Object... args) {
                System.out.println(message);
            }
        });
        sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        airtime = AfricasTalking.getService(AirtimeService.class);
        payment = AfricasTalking.getService(AfricasTalking.SERVICE_PAYMENT);
        server = new Server();
        server.startInsecure();
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

        // Send SMS
        post("/auth/register/:phone", (req, res) -> sms.send("Welcome to Awesome Company", "AT2FA", new String[] {req.params("phone")}), gson::toJson);

        // Send Airtime
        post("/airtime/:phone", (req, res) -> airtime.send(req.params("phone"), req.queryParams("amount")), gson::toJson);

        // Mobile Checkout
        post("/mobile/checkout/:phone", (req, res) -> payment.mobileCheckout("TestProduct", req.params("phone"), req.queryParams("amount"), null), gson::toJson);

        // Mobile B2C
        post("/mobile/b2c/:phone", (req, res) -> payment.mobileB2C("TestProduct", Arrays.asList(new Consumer("Boby", req.params("phone"), req.queryParams("amount"), Consumer.REASON_SALARY))), gson::toJson);

    }

    private static String render(String view, Map data) {
        return hbs.render(new ModelAndView(data, view + ".hbs"));
    }

}
