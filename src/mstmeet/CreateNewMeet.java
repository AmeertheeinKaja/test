/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mstmeet;

import com.google.gson.Gson;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class CreateNewMeet {
    public String UPN;
    
       public static String GetMe(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(mstchat.AppConfig.BaseURL)
                .method("GET", null).addHeader(mstchat.AppConfig.AuthType, accessToken).build();

        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
//        System.out.println("In meetingID");
        if (response.isSuccessful()) {
            String responseBodyString = response.body().string();

            JSONObject jsonObject1 = new JSONObject(responseBodyString);
            
            System.out.println("" + jsonObject1.getString("displayName") + " :: " + jsonObject1.getString("userPrincipalName"));
            return "" + jsonObject1.getString("displayName") + " :: " + jsonObject1.getString("userPrincipalName");
        }
        return null;
    }
       
public static void getDetails(String accessToken,String subject, String Date,String start_time, String end_time, List<String> attendees){
    
    
    
        try {
            System.out.println("Subject"+subject);
            System.out.println("Date:"+Date);
            System.out.println("start_time:"+start_time);
            System.out.println("end_time"+end_time);
            System.out.println("participant"+attendees);
            
            
            
                  StringBuilder attendeesJSON = new StringBuilder();
        for (String attendee : attendees) {
            attendeesJSON.append("{\"upn\":\"").append(attendee).append("\"},");
        }
            
String bodyStr = "{"
        + "\"startDateTime\": \"" + convertToUTC(start_time) + "\","
        + "\"endDateTime\": \"" + convertToUTC(end_time) + "\","
        + "\"subject\": \"" + subject.replace("\\", "\\\\") + "\","
        + "\"joinMeetingIdSettings\": {"
        +     "\"isPasscodeRequired\": true"
        + "},"
        + "\"participants\": {"
        +     "\"attendees\": ["+ attendeesJSON.toString() +"]"
      + "}"
        + "}";

     
            
            
            System.out.println("" + bodyStr);
            
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, bodyStr);
            Request request = new Request.Builder()
                    .url(AppConfig.BaseURL + "/onlineMeetings")
                    .addHeader("Content-Type", "application/json")
                    .method("POST", body).addHeader(AppConfig.AuthType, accessToken).build();
            System.out.println("Request:"+request);
              System.out.println("Body:"+body);
            
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            
//        if (response.isSuccessful()) {
String responseBodyString = response.body().string();
        System.out.println("Meeting Creation Response :: " + responseBodyString);
        JSONObject jsonObject1 = new JSONObject(responseBodyString);
        System.out.println(responseBodyString);
        addToCalendar(accessToken,jsonObject1.getString("startDateTime"),
                jsonObject1.getString("endDateTime"),jsonObject1.getString("joinUrl"),
                jsonObject1.getString("subject"),
//                jsonObject1.getJSONObject("participants")
//                          .getJSONArray("attendees")
//                          .getJSONObject(i)
//                          .getString("upn")
        
        attendees
        );
        } catch (IOException ex) {
            Logger.getLogger(CreateNewMeet.class.getName()).log(Level.SEVERE, null, ex);
        }
}




public static String addToCalendar( String accessToken,String startDateTime,String endDateTime, String joinUrl,String subject,List<String> attendees ){

    
StringBuilder attendeesJSON = new StringBuilder();
for (int i = 0; i < attendees.size(); i++) {
    String email = attendees.get(i);
    attendeesJSON.append("{")
                 .append("\"emailAddress\":{\"address\":\"").append(email).append("\",\"name\":\"\"},")
                 .append("\"type\":\"required\"}");
    // Add a comma if this is not the last attendee
    if (i < attendees.size() - 1) {
        attendeesJSON.append(",");
    }
}
String bodyStr = "{\"subject\":\"" + subject + "\","
        + "\"start\":{\"dateTime\":\"" + removeZ(startDateTime) + "\","
        + "\"timeZone\":\"India Standard Time\"},\"end\":{\"dateTime\":\"" + removeZ(endDateTime) + "\","
        + "\"timeZone\":\"India Standard Time\"},"
        + "\"body\":{\"contentType\":\"HTML\",\"content\":\"Join the meeting using the following link: <a href=\'" + joinUrl + "\'>Join Meeting</a>\"},"
        + "\"location\":{\"displayName\":\"Virtual Meeting\"},"
        + "\"attendees\":["+ attendeesJSON.toString() +"]}"
          + "}";



    System.out.println("Body:"+bodyStr);
    
        try {
            System.out.println("" + bodyStr);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, bodyStr);
            Request request = new Request.Builder()
                    .url(AppConfig.BaseURL + "/events")
                    .addHeader("Content-Type", "application/json")
                    .method("POST", body).addHeader(AppConfig.AuthType, accessToken).build();
            
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
//        if (response.isSuccessful()) {
String responseBodyString = response.body().string();

System.out.println("Meeting calendar Response :: " + responseBodyString);

return "Meeting added to calendar";
        } catch (IOException ex) {
            Logger.getLogger(CreateNewMeet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
}

              
 public static String convertToUTC(String originalDateStr) {
        String utcDateString = null;
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        originalFormat.setTimeZone(TimeZone.getTimeZone("IST")); // Set input time zone

        try {
            // Parse the original date string
            Date originalDate = originalFormat.parse(originalDateStr);

            // Convert to UTC by setting UTC time zone
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set output time zone

            // Format the date in UTC
            utcDateString = utcFormat.format(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcDateString;
    }
 public static String removeZ(String dateTimeString) {
    if (dateTimeString != null && dateTimeString.endsWith("Z")) {
        return dateTimeString.substring(0, dateTimeString.length() - 1);
    }
    return dateTimeString;
}
}