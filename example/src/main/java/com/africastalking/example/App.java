package com.africastalking.example;

import com.africastalking.*;
import com.africastalking.payment.recipient.Consumer;
import com.africastalking.voice.action.*;
import com.africastalking.voice.action.Record;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.*;
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
    private static final String USERNAME = "fake";
    private static final String API_KEY = "fake";

    private static Gson gson = new Gson();

    private static HandlebarsTemplateEngine hbs = new HandlebarsTemplateEngine("/views");
    private static SmsService sms;
    private static AirtimeService airtime;
    private static PaymentService payment;

    private static void log(String message) {
        System.out.println(message);
    }

    private static void setupAfricastalking() throws IOException {
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
    }

    public static void main(String[] args) throws IOException {

        InetAddress host = Inet4Address.getLocalHost();
        log("\n");
        log(String.format("HTTP Server: %s:%d", host.getHostAddress(), HTTP_PORT));
        log("\n");

        HashMap<String, String> states = new HashMap<>();
        String baseUrl = "http://fake.ngrok.io";
        String songUrl = "https://upload.wikimedia.org/wikipedia/commons/transcoded/4/49/National_Anthem_of_Kenya.ogg/National_Anthem_of_Kenya.ogg.mp3";

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
        post("/auth/register/:phone", (req, res) -> sms.send("Welcome to Awesome Company", "AT2FA", new String[] {req.params("phone")}, false), gson::toJson);

        // Send Airtime
        post("/airtime/:phone", (req, res) -> airtime.send(req.params("phone"), req.queryParams("currencyCode"), Float.parseFloat(req.queryParams("amount"))), gson::toJson);

        post("/voice", (req, res) -> {

            // Parse POST data
            String[] raw = URLDecoder.decode(req.body()).split("&");
            Map<String, String> data = new HashMap<>();
            for (String item: raw) {
                String [] kw = item.split("=");
                if (kw.length == 2) {
                    data.put(kw[0], kw[1]);
                }
            }

            // Prep state
            boolean isActive = data.get("isActive").contentEquals("1");
            String sessionId = data.get("sessionId");
            String callerNumber = data.get("callerNumber");
            String dtmf = data.get("dtmfDigits");
            String state = isActive ? states.getOrDefault(sessionId, "menu") : "";

            ActionBuilder response = new ActionBuilder();

            switch (state) {
                case "menu":
                    states.put(sessionId, "process");
                    response
                            .say(new Say("Hello bot " + data.getOrDefault("callerNumber", "There")))
                            .getDigits(new GetDigits(new Say("Press 1 to listen to some song. Press 2 to tell me your name. Press 3 to talk to a human. Press 4 or hang up to quit"), 1, "#", null));
                    break;
                case "process":
                    switch (dtmf) {
                        case "1":
                            states.put(sessionId, "menu");
                            response
                                    .play(new Play(new URL(songUrl)))
                                    .redirect(new Redirect(new URL(baseUrl + "/voice")));
                            break;
                        case "2":
                            states.put(sessionId, "name");
                            response.record(new Record(new Say("Please say your full name after the beep"), "#", 30, 0, true, true, null));
                            break;
                        case "3":
                            states.remove(sessionId);
                            response
                                    .say(new Say("We are getting our resident human on the line for you, please wait while enjoying this nice tune. You have 30 seconds to enjoy a nice conversation with them"))
                                    .dial(new Dial(Arrays.asList("+254718769882"), false, false, null, new URL(songUrl), 30));
                            break;
                        case "4":
                            states.remove(sessionId);
                            response.say(new Say("Bye Bye, Long Live Our Machine Overlords")).reject(new Reject());
                            break;
                        default:
                            states.put(sessionId, "menu");
                            response
                                    .say(new Say("Invalid choice, try again and you will be exterminated!"))
                                    .redirect(new Redirect(new URL(baseUrl + "/voice")));
                            break;

                    }
                    break;
                case "name":
                    states.put(sessionId, "menu");
                    response
                            .say(new Say("Your human name is"))
                            .play(new Play(new URL(data.get("recordingUrl"))))
                            .say(new Say("Now forget is, you new name is bot " + callerNumber))
                            .redirect(new Redirect(new URL(baseUrl + "/voice")));
                    break;
                default:
                    response.say(new Say("Well, this is unexpected! Bye Bye, Long Live Our Machine Overlords")).reject(new Reject());
                    break;
            }

            return response.build();
        });

    }

    private static String render(String view, Map data) {
        return hbs.render(new ModelAndView(data, view + ".hbs"));
    }

}
