package app.index;


import com.github.kevinsawicki.http.HttpRequest;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EuroRate {
    private String[] currencies;

    public EuroRate() {
        File config = new File("src/main/resources/configRelativeCurrencies.txt");
        ArrayList currenciesArray = new ArrayList();

        try {
            BufferedReader configReader = new BufferedReader(new FileReader(config));
            String tempString = null;

            while ((tempString = configReader.readLine()) != null) {
                currenciesArray.add(tempString);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        currencies = new String[currenciesArray.size()];
        for (int i = 0; i < currenciesArray.size(); i++) {
            currencies[i] = currenciesArray.get(i).toString();
            System.out.println(currencies[i]);
        }
    }

    private Object getFromCP() {
        String reqURL = "http://apilayer.net/api/live" + "?access_key=c252f5f007e91fd07637e86c7e4cf324" + "&currencies=EUR,";
        for (int i = 0; i < currencies.length; i++) {
            reqURL += currencies[i];
            reqURL += ",";
        }
        reqURL = reqURL.substring(0, reqURL.length() - 1);
        System.out.println(reqURL);

        String response = HttpRequest.get(reqURL).body();

        JSONObject responseObj = (JSONObject) JSONValue.parse(response);
        responseObj = (JSONObject) responseObj.get("quotes");
        JSONObject cutPreficsObj = new JSONObject();
        Float EURCourse = 1 / Float.parseFloat(responseObj.get("USDEUR").toString());
        Float currency;
        for (Object key : responseObj.keySet()) {
            if (!key.toString().substring(3, 6).equals("EUR")) {
                currency = Float.parseFloat(responseObj.get(key).toString()) / EURCourse;
                cutPreficsObj.put(key.toString().substring(3, 6), currency.toString());
            }
        }
        System.out.println(cutPreficsObj);
        return cutPreficsObj;
    }

    private Object getFromOxr() {
        String reqURL = "https://openexchangerates.org/api/latest.json" + "?app_id=e0562836e26b45d0bc184ccaa779cf5c" + "&symbols=EUR,";

        for (int i = 0; i < currencies.length; i++) {
            reqURL += currencies[i];
            reqURL += ",";
        }
        reqURL = reqURL.substring(0, reqURL.length() - 1);
        System.out.println(reqURL);

        String response = HttpRequest.get(reqURL).body();
        JSONObject responseObj = (JSONObject) JSONValue.parse(response);
        responseObj = (JSONObject) responseObj.get("rates");
        JSONObject cutPreficsObj = new JSONObject();

        Float EURCourse = 1 / Float.parseFloat(responseObj.get("EUR").toString());
        Float currency;
        for (Object key : responseObj.keySet()) {
            if (!key.toString().equals("EUR")) {
                currency = Float.parseFloat(responseObj.get(key).toString()) / EURCourse;
                cutPreficsObj.put(key.toString(), currency.toString());
            }
        }
        System.out.println(cutPreficsObj);
        return cutPreficsObj;
    }

    private Object getFromFr() {
        String reqURL = "http://www.floatrates.com/daily/usd.xml";
        System.out.println(reqURL);
        String response = HttpRequest.get(reqURL).body();
        InputStream responceStream = new ByteArrayInputStream(response.getBytes());
        SAXBuilder builder = new SAXBuilder();
        Element root = null;
        String EURstring;
        String CurrencyString;
        Float EURCourse = new Float(0);
        JSONObject resultObj = new JSONObject();
        try {
            Document doc = builder.build(responceStream);
            root = doc.getRootElement();
            List<Element> list = root.getChildren();
            Element node;
            for (int j = 0; j < currencies.length; j++) {
                if (root.getChild("item").getChild("baseCurrency").getText().equals(currencies[j])) {
                    resultObj.put(currencies[j], 1);
                }
            }
            for (int i = 0; i < list.size(); i++) {
                node = list.get(i);
                if (node.getName() == "item") {
                    if (node.getChild("targetCurrency").getText().equals("EUR")) {
                        EURstring = node.getChild("exchangeRate").getText();
                        EURCourse = 1 / Float.parseFloat(EURstring);
                    }
                    for (int j = 0; j < currencies.length; j++) {
                        if (node.getChild("targetCurrency").getText().equals(currencies[j])) {
                            resultObj.put(currencies[j], node.getChild("exchangeRate").getText());
                        }
                    }
                }
            }
            float newValue;
            for (Object key : resultObj.keySet()) {
                newValue = Float.parseFloat(resultObj.get(key).toString()) / EURCourse;
                resultObj.replace(key, newValue);
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(resultObj);
        return resultObj;
    }

    public String prepareOutputJson() {
        JSONObject outputObj = new JSONObject();

        outputObj.put("CP", getFromCP());
        outputObj.put("Oxr", getFromOxr());
        outputObj.put("Fr", getFromFr());
        String output = outputObj.toJSONString();

        System.out.println("Output JSON: " + output);
        return output;
    }

}
