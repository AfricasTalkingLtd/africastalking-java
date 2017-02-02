package com.africastalking;

/**********************************************************************************************************************
 * # COPYRIGHT (C) 2014 AFRICASTALKING LTD <www.africastalking.com>                                                   *
 **********************************************************************************************************************
 *AFRICAStALKING SMS GATEWAY CLASS IS A FREE SOFTWARE IE. CAN BE MODIFIED AND/OR REDISTRIBUTED                        *
 *UNDER THER TERMS OF GNU GENERAL PUBLIC LICENCES AS PUBLISHED BY THE                                                 *
 *FREE SOFTWARE FOUNDATION VERSION 3 OR ANY LATER VERSION                                                             *
 **********************************************************************************************************************
 *THE CLASS IS DISTRIBUTED ON 'AS IS' BASIS WITHOUT ANY WARRANTY, INCLUDING BUT NOT LIMITED TO                        *
 *THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.                      *
 *IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,             *
 *WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE        *
 *OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                                                                       *
 **********************************************************************************************************************/

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

import org.json.*;

@Deprecated
public class AfricasTalkingGateway
{
    private String _username;
    private String _apiKey;
    private String _environment;
    private int responseCode;

    private static final int HTTP_CODE_OK      = 200;
    private static final int HTTP_CODE_CREATED = 201;

    //Change debug flag to true to view raw server response
    private static final boolean DEBUG = true;

    public AfricasTalkingGateway(String username_, String apiKey_)
    {
        _username    = username_;
        _apiKey      = apiKey_;
        _environment = "production";
    }

    public AfricasTalkingGateway(String username_, String apiKey_, String environment_)
    {
        _username    = username_;
        _apiKey      = apiKey_;
        _environment = environment_;
    }


    //Bulk messages methods
    public JSONArray sendMessage(String to_, String message_) throws Exception
    {

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("to", to_);
        data.put("message", message_);

        return sendMessageImpl(to_, message_, data);
    }


    public JSONArray sendMessage(String to_, String message_, String from_, int bulkSMSMode_) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("to", to_);
        data.put("message", message_);

        if ( from_.length() > 0 ) data.put("from", from_);

        data.put("bulkSMSMode", Integer.toString(bulkSMSMode_));

        return sendMessageImpl(to_, message_, data);
    }


    public JSONArray sendMessage(String to_, String message_, String from_, int bulkSMSMode_, HashMap<String, String> options_) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("to", to_);
        data.put("message", message_);

        if ( from_.length() > 0 ) data.put("from", from_);

        data.put("bulkSMSMode", Integer.toString(bulkSMSMode_));


        if (options_.containsKey("enqueue")) data.put("enqueue", options_.get("enqueue"));
        if (options_.containsKey("keyword")) data.put("keyword", options_.get("keyword"));
        if (options_.containsKey("linkId"))  data.put("linkId", options_.get("linkId"));
        if (options_.containsKey("retryDurationInHours"))  data.put("retryDurationInHours", options_.get("retryDurationInHours"));

        return sendMessageImpl(to_, message_, data);
    }


    public JSONArray fetchMessages(int lastReceivedId_) throws Exception
    {
        String requestUrl = getSmsUrl() + "?" +
                URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(_username, "UTF-8") +
                "&" + URLEncoder.encode("lastReceivedId", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(lastReceivedId_), "UTF-8");

        String response = sendGETRequest(requestUrl);
        if(responseCode == HTTP_CODE_OK) {
            JSONObject jsObject = new JSONObject(response);
            return jsObject.getJSONObject("SMSMessageData").getJSONArray("Messages");
        }

        throw new Exception(response.toString());
    }


    //Subcscription methods
    public JSONObject createSubscription(String phoneNumber_, String shortCode_, String keyword_) throws Exception
    {
        if(phoneNumber_.length() == 0 || shortCode_.length() == 0 || keyword_.length() == 0)
            throw new Exception("Please supply phoneNumber, shortCode and keyword");

        HashMap <String, String> data_ = new HashMap<String, String>();
        data_.put("username", _username);
        data_.put("phoneNumber", phoneNumber_);
        data_.put("shortCode", shortCode_);
        data_.put("keyword", keyword_);
        String requestUrl = getSubscriptionUrl() + "/create";

        String response = sendPOSTRequest(data_, requestUrl);

        if(responseCode != HTTP_CODE_CREATED)
            throw new Exception(response.toString());

        JSONObject jsObject = new JSONObject(response);
        return jsObject;
    }


    public JSONObject deleteSubscription(String phoneNumber_,String shortCode_, String keyword_) throws Exception
    {
        if(phoneNumber_.length() == 0 || shortCode_.length() == 0 || keyword_.length() == 0)
            throw new Exception("Please supply phone number, short code and keyword");

        HashMap <String, String> data_ = new HashMap<String, String>();
        data_.put("username", _username);
        data_.put("phoneNumber", phoneNumber_);
        data_.put("shortCode", shortCode_);
        data_.put("keyword", keyword_);
        String requestUrl = getSubscriptionUrl() + "/delete";

        String response = sendPOSTRequest(data_, requestUrl);

        if(responseCode != HTTP_CODE_CREATED)
            throw new Exception(response.toString());

        JSONObject jsObject = new JSONObject(response);
        return jsObject;
    }


    public JSONArray fetchPremiumSubscriptions (String shortCode_, String keyword_, int lastReceivedId_) throws Exception
    {
        if(shortCode_.length() == 0 || keyword_.length() == 0)
            throw new Exception("Please supply short code and keyword");

        lastReceivedId_ = lastReceivedId_ > 0? lastReceivedId_ : 0;
        String requestUrl = getSubscriptionUrl() + "?username="+_username+"&shortCode="+shortCode_+"&keyword="+keyword_+"&lastReceivedId="+lastReceivedId_;

        String response = sendGETRequest(requestUrl);
        if(responseCode == HTTP_CODE_OK) {
            JSONObject jsObject = new JSONObject(response);
            return jsObject.getJSONArray("responses");
        }

        throw new Exception(response.toString());
    }


    public JSONArray call(String from_, String to_) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("from", from_);
        data.put("to", to_);
        String urlString = getVoiceUrl() + "/call";
        String response  = sendPOSTRequest(data, urlString);

        JSONObject jsObject = new JSONObject(response);

        if(jsObject.getString("errorMessage").equals("None"))
            return jsObject.getJSONArray("entries");
        throw new Exception(jsObject.getString("errorMessage"));
    }

    //Call methods
    public JSONArray getNumQueuedCalls(String phoneNumber, String queueName) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("phoneNumber", phoneNumber);
        data.put("queueName", queueName);

        return queuedCalls(data);
    }


    public JSONArray getNumQueuedCalls(String phoneNumber) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("phoneNumbers", phoneNumber);

        return queuedCalls(data);
    }


    public void uploadMediaFile(String url_) throws Exception
    {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", _username);
        data.put("url", url_);
        String requestUrl = getVoiceUrl() + "/mediaUpload";

        String response = sendPOSTRequest(data, requestUrl);

        JSONObject jsObject = new JSONObject(response);

        if(!jsObject.getString("errorMessage").equals("None"))
            throw new Exception(jsObject.getString("errorMessage"));

    }


    //Airtime methods
    public JSONArray sendAirtime(String recipients_) throws Exception
    {
        HashMap<String, String> data_ = new HashMap<String, String>();
        data_.put("username", _username);
        data_.put("recipients", recipients_);
        String urlString = getAirtimeUrl() + "/send";

        String response = sendPOSTRequest(data_, urlString);

        if(responseCode == HTTP_CODE_CREATED) {
            JSONObject jsObject = new JSONObject(response);
            JSONArray results = jsObject.getJSONArray("responses");
            if(results.length() > 0)
                return results;
            throw new Exception(jsObject.getString("errorMessage"));
        }

        throw new Exception(response);
    }


    //User data method
    public JSONObject getUserData() throws Exception
    {
        String requestUrl = getUserDataUrl() + "?username="+_username;

        String response   = sendGETRequest(requestUrl);
        if(responseCode == HTTP_CODE_OK) {
            JSONObject jsObject = new JSONObject(response);
            return jsObject.getJSONObject("UserData");
        }

        throw new Exception(response);
    }

    public JSONObject initiateMobilePaymentCheckout(String productName_,
                                                    String phoneNumber_,
                                                    String currencyCode_,
                                                    Double amount_,
                                                    Map<String, String> metadata_) throws Exception
    {
        JSONObject requestBody = new JSONObject()
                .put("username", _username)
                .put("productName", productName_)
                .put("phoneNumber", phoneNumber_)
                .put("currencyCode", currencyCode_)
                .put("amount", amount_)
                .put("metadata", metadata_);
        String response = sendJsonPOSTRequest(requestBody.toString(), getMobilePaymentCheckoutUrl());
        return new JSONObject(response);
    }

    public JSONArray mobilePaymentB2CRequest(String productName_,
                                             List<MobilePaymentB2CRecipient> recipients_) throws Exception
    {
        List<JSONObject> jsonRecipients = new ArrayList<JSONObject>();
        for (MobilePaymentB2CRecipient recipient: recipients_) {
            jsonRecipients.add(recipient.toJSON());
        }
        JSONObject requestBody = new JSONObject()
                .put("username", _username)
                .put("productName", productName_)
                .put("recipients", jsonRecipients);
        System.out.println("SNG: Raw Request: " + requestBody.toString());
        String response = sendJsonPOSTRequest(requestBody.toString(), getMobilePaymentB2CUrl());
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray entries = jsonResponse.getJSONArray("entries");
        if ( entries.length() > 0 ) return entries;
        throw new Exception(jsonResponse.getString("errorMessage"));
    }

    private JSONArray sendMessageImpl(String to_, String message_, HashMap<String, String> data_) throws Exception{
        String response = sendPOSTRequest(data_, getSmsUrl());
        if (responseCode == HTTP_CODE_CREATED) {
            JSONObject jsObject   = new JSONObject(response);
            JSONArray  recipients = jsObject.getJSONObject("SMSMessageData").getJSONArray("Recipients");
            if(recipients.length() > 0) return recipients;

            throw new Exception(jsObject.getJSONObject("SMSMessageData").getString("Message"));
        }

        throw new Exception(response);
    }

    //Private accessor methods
    private JSONArray queuedCalls(HashMap<String, String> data_) throws Exception {
        String requestUrl = getVoiceUrl() + "/queueStatus";
        String response = sendPOSTRequest(data_, requestUrl);
        JSONObject jsObject = new JSONObject(response);
        if(jsObject.getString("errorMessage").equals("None"))
            return jsObject.getJSONArray("entries");
        throw new Exception(jsObject.getString("errorMessage"));
    }

    private String sendPOSTRequest(HashMap<String, String> dataMap_, String urlString_) throws Exception {
        String data = new String();
        Iterator<Entry<String, String>> it = dataMap_.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            data += URLEncoder.encode(pairs.getKey().toString(), "UTF-8");
            data += "=" + URLEncoder.encode(pairs.getValue().toString(), "UTF-8");
            if ( it.hasNext() ) data += "&";
        }
        URL url = new URL(urlString_);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("apikey", _apiKey);
        conn.setDoOutput(true);
        return sendPOSTRequestImpl(data, conn);
    }

    private String sendJsonPOSTRequest(String data_, String urlString_) throws Exception
    {
        URL url = new URL(urlString_);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("apikey", _apiKey);
        conn.setDoOutput(true);
        return sendPOSTRequestImpl(data_, conn);
    }

    private String sendPOSTRequestImpl(String data_, URLConnection conn_) throws Exception {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(conn_.getOutputStream());
            writer.write(data_);
            writer.flush();

            HttpURLConnection http_conn = (HttpURLConnection)conn_;
            responseCode = http_conn.getResponseCode();

            BufferedReader reader;
            boolean passed = true;

            if(responseCode == HTTP_CODE_OK || responseCode == HTTP_CODE_CREATED) {
                reader = new BufferedReader(new InputStreamReader(http_conn.getInputStream()));
            }
            else {
                reader = new BufferedReader(new InputStreamReader(http_conn.getErrorStream()));
                passed = false;
            }
            String response = readResponse(reader);

            if(DEBUG) System.out.println("ResponseCode: " + responseCode + " RAW Response: " + response);

            reader.close();

            if(passed) return response;

            throw new Exception(response);

        } catch (Exception e){
            throw e;
        }
    }

    private String readResponse(BufferedReader reader) throws Exception
    {
        StringBuilder response = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }

    private String sendGETRequest(String urlString) throws Exception
    {
        try {
            URL url= new URL(urlString);
            URLConnection connection = (URLConnection)url.openConnection();
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("apikey", _apiKey);

            HttpURLConnection http_conn = (HttpURLConnection)connection;
            responseCode = http_conn.getResponseCode();

            BufferedReader reader;
            boolean passed = true;
            if(responseCode == HTTP_CODE_OK || responseCode == HTTP_CODE_CREATED) {
                reader = new BufferedReader(new InputStreamReader(http_conn.getInputStream()));
            }
            else {
                reader = new BufferedReader(new InputStreamReader(http_conn.getErrorStream()));
                passed = false;
            }
            String response = reader.readLine();

            if(DEBUG) System.out.println(response);

            reader.close();

            if(passed) return response;

            throw new Exception(response);
        }
        catch (Exception e) {throw e;}
    }

    private String getApiHost() {
        return (_environment == "sandbox") ? "https://api.sandbox.africastalking.com" : "https://api.africastalking.com";
    }

    private String getVoiceHost() {
        return (_environment == "sandbox") ? "https://voice.sandbox.africastalking.com" : "https://voice.africastalking.com";
    }

    private String getSmsUrl() {
        return getApiHost() + "/version1/messaging";
    }

    private String getVoiceUrl() {
        return getVoiceHost();
    }

    private String getSubscriptionUrl() {
        return getApiHost() + "/version1/subscription";
    }

    private String getUserDataUrl() {
        return getApiHost() + "/version1/user";
    }

    private String getAirtimeUrl() {
        return getApiHost() + "/version1/airtime";
    }

    private String getMobilePaymentCheckoutUrl() {
        return getApiHost() + "/payment/mobile/checkout/request";
    }

    private String getMobilePaymentB2CUrl() {
        return getApiHost() + "/payment/mobile/b2c/request";
    }


    @Deprecated
    public static class MobilePaymentB2CRecipient {
        private String _phoneNumber;
        private String _currencyCode;
        private Double _amount;
        private HashMap<String, String> _metadata;

        public MobilePaymentB2CRecipient(String phoneNumber_,
                                         String currencyCode_,
                                         Double amount_) {
            _phoneNumber  = phoneNumber_;
            _currencyCode = currencyCode_;
            _amount       = amount_;
            _metadata     = new HashMap<String, String>();
        }

        public void addMetadata(String key_, String  value_) {
            _metadata.put(key_, value_);
        }

        public JSONObject toJSON() throws Exception {
            return new JSONObject()
                    .put("phoneNumber", _phoneNumber)
                    .put("currencyCode", _currencyCode)
                    .put("amount", _amount)
                    .put("metadata", _metadata);
        }
    }

}