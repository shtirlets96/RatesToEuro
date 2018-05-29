package app.index.APIs;

import app.index.EuroRate;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.util.ArrayList;

class JsonAPI implements CurrenciesService{
    String reqURL;
    JSONObject responseObj;
    Float  EURRate;
    String valuesLocation;
    String APISecret;
    String APISecretPath;

     String getAPISecret(){
         File config = new File(APISecretPath);
         String tempString = null;

         try {
             BufferedReader configReader = new BufferedReader(new FileReader(config));
             tempString = configReader.readLine();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }

         return tempString;
     }

     void addCurrencies(){
        for (int i = 0; i < EuroRate.currencies.length; i++) {
            reqURL += EuroRate.currencies[i];
            reqURL += ",";
        }
        reqURL = reqURL.substring(0, reqURL.length() - 1);
        System.out.println(reqURL);
    }

     void getResponse(){
        String response = HttpRequest.get(reqURL).body();
        responseObj = (JSONObject) JSONValue.parse(response);
        responseObj = (JSONObject) responseObj.get(valuesLocation);
    }

    void getEURRate(){

         EURRate = 1 / Float.parseFloat(responseObj.get("EUR").toString());
    }

    public JSONObject getFormatedResponse(){
        addCurrencies();
        getResponse();
        getEURRate();

        Float currency;
        JSONObject formatedObj=new JSONObject();

        System.out.println(EURRate);

        for (Object key : responseObj.keySet()) {
            if (!key.toString().equals("EUR")) {
                currency = Float.parseFloat(responseObj.get(key).toString()) / EURRate;
                formatedObj.put(key.toString(), currency.toString());
            }
        }
        System.out.println(formatedObj);
        return formatedObj;
    }
}



