/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mstchat;

import com.google.gson.Gson;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import static mstchat.MSTChat.MEETID;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class CreateMeeting {

    public static String GetMe(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(AppConfig.BaseURL)
                .method("GET", null).addHeader(AppConfig.AuthType, accessToken).build();

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

    public static String GenerateMeeting(String accessToken, String emailLst) throws IOException {
        String attendees = "";
        String email[] = emailLst.split("#");
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime meetingStart = currentTime.plusMinutes(30);

        LocalDateTime meetingEnd = currentTime.plusHours(1);
        for (String string : email) {
            String value = "{\n"
                    + "        \"upn\": \"" + string + "\"\n"
                    + "        }\n";
            attendees = (attendees == "") ? value : attendees + "," + value;
        }
        String bodyStr = "{\"startDateTime\":\"" + meetingStart + "-07:00\""
                + ",\"endDateTime\":\"" + meetingEnd + "-07:00\","
                + "\"subject\":\"User Token Meeting11\","
                + "\"joinMeetingIdSettings\": {\n"
                + "\"isPasscodeRequired\": true\n"
                + "},"
                + "\"participants\": {\n"
                + "        \"attendees\": [" + attendees + "]\n"
                + "    }"
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

        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
//        if (response.isSuccessful()) {
        String responseBodyString = response.body().string();

        System.out.println("Meeting Creation Response :: " + responseBodyString);
        if (response.isSuccessful()) {
            JSONObject jsonObject1 = new JSONObject(responseBodyString);

            SendInviteMail(accessToken, jsonObject1.getJSONObject("participants").getJSONArray("attendees"), jsonObject1.getString("meetingCode"), jsonObject1.getString("subject"), jsonObject1.getString("joinWebUrl"));
            System.out.println("Meeting Code:" + jsonObject1.getString("meetingCode") + "Subject :" + jsonObject1.getString("subject"));
            return "Meeting Code:" + jsonObject1.getString("meetingCode") + "Subject :" + jsonObject1.getString("subject");
        }
//        }
        return null;
    }

    public static void SendInviteMail(String accessToken, JSONArray attendeesList, String meetCode, String subject, String webUrl) throws IOException {

        
        String recipients = "";
        for (Object o : attendeesList) {
            if (o instanceof JSONObject) {
                String value = " {\n"
                        + "        \"emailAddress\": {\n"
                        + "          \"address\": \"" + ((JSONObject) o).getString("upn") + "\"\n"
                        + "        }}\n";
                recipients = (recipients == "") ? value : recipients + "," + value;
            }
        }

        String bodyStr = "{\"message\": {\n"
                + "    \"subject\": \""+subject+"\",\n"
                + "    \"body\": {\n"
                + "      \"contentType\": \"HTML\",\n"
                + "      \"content\": \"" + subject + "<br><a href=\'" + webUrl + "\'>Click To start Meeting</a><br><b>Meeting Code : " + meetCode + "</b>\"\n"
                + "    },\n"
                + "    \"toRecipients\": [" + recipients + "]\n"                
                + "  }\n"
                + "}";

        System.out.println("" + bodyStr);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, bodyStr);
        Request request = new Request.Builder()
                .url(AppConfig.BaseURL + "/sendMail")
                .addHeader("Content-Type", "application/json")
                .method("POST", body).addHeader(AppConfig.AuthType, accessToken).build();

        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
//        if (response.isSuccessful()) {
        String responseBodyString = response.body().string();

        System.out.println("Meeting Creation Response :: " + responseBodyString);
//        if (response.isSuccessful()) {
//            JSONObject jsonObject1 = new JSONObject(responseBodyString);
//            System.out.println("Meeting Code:" + jsonObject1.getString("meetingCode") + "Subject :" + jsonObject1.getString("subject"));
//            return "Meeting Code:" + jsonObject1.getString("meetingCode") + "Subject :" + jsonObject1.getString("subject");
//        }
    }
}
