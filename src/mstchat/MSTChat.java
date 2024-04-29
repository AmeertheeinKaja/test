/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mstchat;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;


/**
 *
 * @author admin
 */
public class MSTChat {
    public static String URL;
    public static String MEETID;
    public static String CHAT_THREAD_ID;
    public static String MEETING_SUBJECT;
    public static ArrayList<String> participants;
    public static ArrayList<String> ChatMessage;
    public static String SENDMESSAGE = "welcome to testing";
     public static      Multimap<String, String> ChatDetail = ArrayListMultimap.create();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
          getEvents();
    }
    
    public static void InitConfiguration() {
        AppConfig.BaseURL = "https://graph.microsoft.com/v1.0/me";
        AppConfig.AuthType = "Authorization";
        AppConfig.APIKey = "eyJ0eXAiOiJKV1QiLCJub25jZSI6Iko5S29mX1ZfZE5VVXBpbmRCdmlHNl9DbWxYck14bVBOakJCWlo3aUxxR3ciLCJhbGciOiJSUzI1NiIsIng1dCI6InEtMjNmYWxldlpoaEQzaG05Q1Fia1A1TVF5VSIsImtpZCI6InEtMjNmYWxldlpoaEQzaG05Q1Fia1A1TVF5VSJ9.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTAwMDAtYzAwMC0wMDAwMDAwMDAwMDAiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC82ZGU4MDVmOS1lNWVkLTQ2YWMtODBkMS1kMjIxNzI0NjQ0NTcvIiwiaWF0IjoxNzEzOTU2MDU3LCJuYmYiOjE3MTM5NTYwNTcsImV4cCI6MTcxNDA0Mjc1NywiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkFWUUFxLzhXQUFBQUEzcG1IZDVZU0tHenU4MnlFaERsM043QXNRWVBsYnh2NGdvaGUyVTVydWVWdlJvbkM0dEowMUlVN0Z4K2tzY20va0FDYmIrM3BwSUthMmlxbWpLTWJUTmxzQmFhV3IxRlY0UTY5aWQvdEtjPSIsImFtciI6WyJwd2QiLCJtZmEiXSwiYXBwX2Rpc3BsYXluYW1lIjoiR3JhcGggRXhwbG9yZXIiLCJhcHBpZCI6ImRlOGJjOGI1LWQ5ZjktNDhiMS1hOGFkLWI3NDhkYTcyNTA2NCIsImFwcGlkYWNyIjoiMCIsImZhbWlseV9uYW1lIjoiSyIsImdpdmVuX25hbWUiOiJBbWVlciIsImlkdHlwIjoidXNlciIsImlwYWRkciI6IjE0Ljk4LjIyNS4xMzgiLCJuYW1lIjoiQW1lZXIgSyIsIm9pZCI6ImUwNDEyZDg1LTJhZjktNGNkYy1iMGJlLThlNmI1YWUxOTUyZiIsInBsYXRmIjoiMyIsInB1aWQiOiIxMDAzMjAwMjg5QzU3QjI0IiwicmgiOiIwLkFYd0EtUVhvYmUzbHJFYUEwZEloY2taRVZ3TUFBQUFBQUFBQXdBQUFBQUFBQUFDN0FQay4iLCJzY3AiOiJBUElDb25uZWN0b3JzLlJlYWQuQWxsIEFQSUNvbm5lY3RvcnMuUmVhZFdyaXRlLkFsbCBDYWxlbmRhcnMuUmVhZCBDYWxlbmRhcnMuUmVhZC5TaGFyZWQgQ2FsZW5kYXJzLlJlYWRXcml0ZSBDYWxlbmRhcnMuUmVhZFdyaXRlLlNoYXJlZCBDaGFubmVsLlJlYWRCYXNpYy5BbGwgQ2hhbm5lbE1lbWJlci5SZWFkLkFsbCBDaGFubmVsTWVtYmVyLlJlYWRXcml0ZS5BbGwgQ2hhbm5lbE1lc3NhZ2UuUmVhZC5BbGwgQ2hhbm5lbFNldHRpbmdzLlJlYWQuQWxsIENoYW5uZWxTZXR0aW5ncy5SZWFkV3JpdGUuQWxsIENoYXQuQ3JlYXRlIENoYXQuUmVhZCBDaGF0LlJlYWRXcml0ZSBEZXZpY2VNYW5hZ2VtZW50QXBwcy5SZWFkLkFsbCBEZXZpY2VNYW5hZ2VtZW50QXBwcy5SZWFkV3JpdGUuQWxsIERldmljZU1hbmFnZW1lbnRDb25maWd1cmF0aW9uLlJlYWQuQWxsIERldmljZU1hbmFnZW1lbnRDb25maWd1cmF0aW9uLlJlYWRXcml0ZS5BbGwgRGV2aWNlTWFuYWdlbWVudE1hbmFnZWREZXZpY2VzLlByaXZpbGVnZWRPcGVyYXRpb25zLkFsbCBEZXZpY2VNYW5hZ2VtZW50TWFuYWdlZERldmljZXMuUmVhZC5BbGwgRGV2aWNlTWFuYWdlbWVudE1hbmFnZWREZXZpY2VzLlJlYWRXcml0ZS5BbGwgRGlyZWN0b3J5LlJlYWQuQWxsIERpcmVjdG9yeS5SZWFkV3JpdGUuQWxsIEdyb3VwLlJlYWQuQWxsIEdyb3VwLlJlYWRXcml0ZS5BbGwgT25saW5lTWVldGluZ0FydGlmYWN0LlJlYWQuQWxsIE9ubGluZU1lZXRpbmdzLlJlYWQgT25saW5lTWVldGluZ3MuUmVhZFdyaXRlIG9wZW5pZCBQZW9wbGUuUmVhZC5BbGwgcHJvZmlsZSBUZWFtLlJlYWRCYXNpYy5BbGwgVGVhbXNBY3Rpdml0eS5TZW5kIFRlYW1zQXBwSW5zdGFsbGF0aW9uLlJlYWRGb3JDaGF0IFRlYW1zQXBwSW5zdGFsbGF0aW9uLlJlYWRXcml0ZUZvckNoYXQgVGVhbXNBcHBJbnN0YWxsYXRpb24uUmVhZFdyaXRlU2VsZkZvckNoYXQgVGVhbVNldHRpbmdzLlJlYWQuQWxsIFRlYW1TZXR0aW5ncy5SZWFkV3JpdGUuQWxsIFVzZXIuUmVhZCBVc2VyLlJlYWQuQWxsIGVtYWlsIFRlYW0uQ3JlYXRlIiwic3ViIjoiN1ZpUVR1alUtMEtJMTlJR0FRemFWLTBQNlRGU2xCVXJYUEp1WnZrWjhkVSIsInRlbmFudF9yZWdpb25fc2NvcGUiOiJOQSIsInRpZCI6IjZkZTgwNWY5LWU1ZWQtNDZhYy04MGQxLWQyMjE3MjQ2NDQ1NyIsInVuaXF1ZV9uYW1lIjoiYW1lZXJAcGI2NC5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJhbWVlckBwYjY0Lm9ubWljcm9zb2Z0LmNvbSIsInV0aSI6IkpOa2JUSDJLdFUycjdOSmxhRWZIQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCIsImI3OWZiZjRkLTNlZjktNDY4OS04MTQzLTc2YjE5NGU4NTUwOSJdLCJ4bXNfY2MiOlsiQ1AxIl0sInhtc19zc20iOiIxIiwieG1zX3N0Ijp7InN1YiI6ImN5ZFpZN1RIdERKVzJ5NGNPNXFYZmt0WXVNN0tLYm9tcXNudGdNcnJoU2MifSwieG1zX3RjZHQiOjE2Nzk1MTkwOTl9.J-SiBd8dbzxQm1l0fiAa9jvZ3PdpAIQMPxI3oFn-LOzgAfxI2gsb2RyufD2IcuHVOfLeyCBJcbYR61DgIoDXk9_MilnmVGfwLkNyEOxc7p1JN1hdVTVs1ye8fsTXtIE8N4ARZqwB6QqQshQBS-Q3HRu2rBw7gSAL5aR2wu2VVeUDo273hwdFGNLeb8m2NolexZpSoXYIb8d9wjDe9mThy7czrASgGGB6AA0lX9qWE1GJdHr3zDse2ina-RN2W7vAX73hdmlX9EtJ22a1qjq3GiKlQpnnutNrbzzSdXRLwZFnfwfVUmqG5jko6HWvmjVvY8xVStAgu43P9TwRIDQPJA";
        
    }
    
        public static HashMap getOnlineMeeting() {
        HashMap hmEvent = new HashMap<>(); 
        ArrayList<String> part = new ArrayList<String>();
        if (MEETID == null) {
            getEvents();
        }
        try {
            if (AppConfig.APIKey == null) {
                InitConfiguration();
            }

//            System.out.println(meetId);
            //System.out.println(AppConfig.BaseURL + "/onlineMeetings/" + MEETID);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(AppConfig.BaseURL + "/onlineMeetings/" + MEETID)
                    .method("GET", null).addHeader(AppConfig.AuthType, AppConfig.APIKey).build();
            
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            System.out.println("In meetingID");
            if (response.isSuccessful()) {
                String responseBodyString = response.body().string();

//    JsonObject jsonObject = gson.fromJson(responseBodyString, JsonObject.class);
//    System.out.println(jsonObject.get("chatInfo"));
//    String chat=jsonObject.get("chatInfo");
////    JsonArray value=(JsonArray) jsonObject.get("id");
//    
//    JsonObject valueObj=value.get(0).getAsJsonObject();
//    
//    System.out.println(valueObj.get("id"));
//   // Process the JSON object here
                JSONObject jsonObject1 = new JSONObject(responseBodyString);
                CHAT_THREAD_ID = jsonObject1.getJSONObject("chatInfo").getString("threadId");
                String MEETING_SUBJECT = jsonObject1.getString("subject");
                System.out.println("Subject:" + MEETING_SUBJECT);
                jsonObject1.getJSONObject("participants").getJSONArray("attendees").getJSONObject(0).get("upn");
                jsonObject1.getJSONObject("participants").getJSONArray("attendees").getJSONObject(0).length();
                int sizeOfObj = jsonObject1.getJSONObject("participants").getJSONArray("attendees").length();
                
                for (int i = 1; i < sizeOfObj; i++) {
                    part.add(jsonObject1.getJSONObject("participants").getJSONArray("attendees").getJSONObject(i).get("upn").toString());
                    //  System.out.println( "particpants:"+jsonObject1.getJSONObject("participants").getJSONArray("attendees").getJSONObject(i).get("upn"));
                }
                
                hmEvent.put("MeetingTitle", MEETING_SUBJECT);
                hmEvent.put("Participants", part);
                System.out.println("ChatThread Id:" + CHAT_THREAD_ID);
                
//                getChatInfo();
                return hmEvent;
                
            } else {
                System.out.println("error");
            }
            
            response.close();
        } catch (IOException ex) {
            Logger.getLogger(MSTChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        
            public static String getEvents() {
        try {
            if (AppConfig.APIKey == null) {
                InitConfiguration();
            }
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(AppConfig.BaseURL + "/events")
                    .method("GET", null)
                    .addHeader(AppConfig.AuthType, AppConfig.APIKey)
                    .build();
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            
            if (response.isSuccessful()) {
                String responseBodyString = response.body().string();
                JsonObject jsonObject = gson.fromJson(responseBodyString, JsonObject.class);
                JsonArray value = (JsonArray) jsonObject.get("value");
                
                JsonObject valueObj = value.get(0).getAsJsonObject();
                JsonObject valueObj1 = valueObj.get("onlineMeeting").getAsJsonObject();
                URL = valueObj1.get("joinUrl").getAsString();
//                   System.out.println(valueObj1.get("joinUrl"));
                getMeetingInfoByUrl();

//                   String meetingID=valueObj.get("id").getAsString();
////                   getOnlineMeeting(meetingID);
//                   System.out.println("Meeting ID::"+valueObj.get("id"));
                // Process the JSON object here
            } else {
                // Handle error response
            }
            
            response.close();
//  System.out.println("" + response.body().string());
//  String responseBodyString = response.body().string();
//  response.close();
        } catch (IOException ex) {
            Logger.getLogger(MSTChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
               public static void getMeetingInfoByUrl() {
        try {
            if (AppConfig.APIKey == null) {
                InitConfiguration();
            }
//                System.out.println(AppConfig.BaseURL+"/onlineMeetings?$filter=JoinWebUrl eq"+" "+"'"+url+"'");
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(AppConfig.BaseURL + "/onlineMeetings?$filter=JoinWebUrl eq" + " " + "'" + URL + "'")
                    .method("GET", null)
                    .addHeader(AppConfig.AuthType, AppConfig.APIKey)
                    .build();
            Response response = client.newCall(request).execute();
            
            Gson gson = new Gson();
            
            if (response.isSuccessful()) {
                String responseBodyString = response.body().string();
                JsonObject jsonObject = gson.fromJson(responseBodyString, JsonObject.class);
                JsonArray value = (JsonArray) jsonObject.get("value");
                
                JsonObject valueObj = value.get(0).getAsJsonObject();
                MEETID = valueObj.get("id").getAsString();
                System.out.println(MEETID);
                
                getOnlineMeeting();

//                   String meetingID=valueObj.get("id").getAsString();
////                   getOnlineMeeting(meetingID);
//                   System.out.println("Meeting ID::"+valueObj.get("id"));
                // Process the JSON object here
            } else {
                System.out.println("error in joinurl block");// Handle error response
            }
            response.close();
            
        } catch (IOException ex) {
            Logger.getLogger(MSTChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
               
    public static String SendMessage(String SENDMESSAGE) {
        
        try {
            
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            
            String json = String.format("{ \"body\": { \"content\": \"%s\" } }", SENDMESSAGE);
            RequestBody body = RequestBody.create(mediaType, json);
//RequestBody body = RequestBody.create(mediaType, "{\r\n     \"body\": {\r\n        \"content\": \"Thanks for response\"\r\n    }\r\n}");
            Request request = new Request.Builder()
                    .url("https://graph.microsoft.com/v1.0/chats/" + CHAT_THREAD_ID + "/messages")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader(AppConfig.AuthType, AppConfig.APIKey)
                    .build();
            Response response = client.newCall(request).execute();            
        } catch (IOException ex) {
            Logger.getLogger(MSTChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
               
               public static String getChat(){
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://graph.microsoft.com/v1.0/chats/"+CHAT_THREAD_ID+"/messages?$bottom=20")
                    .method("GET", null)
                  .addHeader(AppConfig.AuthType, AppConfig.APIKey)
                    .build();
            Response response = client.newCall(request).execute();
            
            Gson gson = new  GsonBuilder().serializeNulls().create();
            
            
            if (response.isSuccessful()) {
                String responseBodyString = response.body().string();


JSONObject jsonObject1 = new JSONObject(responseBodyString);
JSONArray messagesArray =jsonObject1.getJSONArray("value");
System.out.println(messagesArray);
System.out.println("In get chat");


for (int i = 0; i < messagesArray.length(); i++) {
    
    JSONObject jsonObject=new JSONObject(responseBodyString);
            JSONObject messageObj = messagesArray.getJSONObject(i);
            JSONObject fromObj = messageObj.optJSONObject("from");
            messageObj.getJSONObject("body").get("content");
System.out.println("Counnnnt"+i);
            if (fromObj != null) {
                String displayName = fromObj.getJSONObject("user").get("displayName").toString();
//                String Content=messageObj.getJSONObject("body").getJSONObject("content").toString();
                
                if (displayName != null && !displayName.isEmpty()) {
                    ChatDetail.put(displayName, messageObj.getJSONObject("body").getString("content"));
                   
                } else {
                    // The "displayName" field is null or empty
                    System.out.println("Display Name is null or empty");
                }
            } else {
                // The "from" field is null
                System.out.println("From field is null");
            }
        }
for (Map.Entry<String, String> entry : ChatDetail.entries()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }








            } else {
                System.out.println("error");
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
return null;
    }
}
