package org.example.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.entity.ExchangeRateApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainController extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();

        }
        Message message = update.getMessage();
        String text = message.getText();
        String firstName = message.getChat().getFirstName();
        Long id = message.getChat().getId();
        String userName = message.getChat().getUserName();

        System.out.println("Chatning idsi: "+id);
        System.out.println("Foydalanuvchining ismi: "+firstName);
        System.out.println("Foydalanuvchi username: "+userName);
        System.out.println("Yuborgan xabar matni: "+text);
        String urlstr = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/RUB/2019-01-01/";


        String url = "https://v6.exchangerate-api.com/v6/2c2241afb9926ee04ed839b4/latest/USD";
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlstr);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();

            String responseString = EntityUtils.toString(entity);
            String substring = responseString.substring(1, responseString.length() - 1);
            System.out.println(substring);

            Gson gson = new Gson();
            ExchangeRateApi rateApi = gson.fromJson(substring,ExchangeRateApi.class);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(id);
            String sendText = "----Bugungi valyuta kursi----\n"
                    +"Valyuta nomi: "+rateApi.getCcyNm_UZ()
                    +"\nValyuta qiymati: "+rateApi.getNominal()
                    +"\nValyuta sumdagi qiymati: "+rateApi.getRate();
            sendMessage.setText(sendText);
            execute(sendMessage);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getBotUsername() {
        return "@Ra_qam_bot";
    }

    @Override
    public String getBotToken() {
        return "1326999462:AAFQ6yU1gfLg6_FaQz4twztTqcaQU2HDiNU";
    }
}
