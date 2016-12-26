package com.africastalking.example;


import com.africastalking.AfricasTalking;
import com.africastalking.Currency;
import com.africastalking.Format;
import com.africastalking.SMSService;
import com.africastalking.example.models.RequestSend;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.ResponseTransformer;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    private static final int PORT = 3000;
    private static HandlebarsTemplateEngine hbs = new HandlebarsTemplateEngine("/views");
    static SMSService sms;

    static {
        AfricasTalking.initialize(
                "at2fa",
                "8c940cd77db666ca100e9dd0d784191ada2ee3eaa1d0a952170a68595313f4ab",
                Format.JSON,
                Currency.KES,
                true
        );

        sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
    }

    public static void main(String[] args) {

        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions


        staticFiles.location("/public");

        port(PORT);

        get("/", (req, res) -> {
            Map<String, Object> data = new HashMap<>();
            data.put("req", req.pathInfo());
            return render("index", data);
        });

        post("/register/:phone", (req, res) -> {
            return sms.send("Welcome to Awesome Company", new String[] {req.params("phone")});
        });

    }

    private static String render(String view, Map data) {
        return hbs.render(new ModelAndView(data, view + ".hbs"));
    }

}
