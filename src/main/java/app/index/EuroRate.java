package app.index;


import app.index.APIs.CPAPI;
import app.index.APIs.FrXML;
import app.index.APIs.OxrAPI;
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
    public static String[] currencies;
    public static String rate;

    public EuroRate() {
        currencies = getCurrencies();
        rate = getActualRate();
    }

    private String[] getCurrencies(){
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

        String[] actualCurrencies = new String[currenciesArray.size()];
        for (int i = 0; i < currenciesArray.size(); i++) {
            actualCurrencies[i] = currenciesArray.get(i).toString();
            System.out.println(actualCurrencies[i]);
        }
        return actualCurrencies;
    }

    private String getActualRate() {
        JSONObject outputObj = new JSONObject();
        OxrAPI Oxr = new OxrAPI();
        CPAPI CP = new CPAPI();
        FrXML Fr = new FrXML();
        outputObj.put("CP", CP.getFormatedResponse());
        outputObj.put("Oxr", Oxr.getFormatedResponse());
        outputObj.put("Fr", Fr.getFormatedResponse());
        String output = outputObj.toJSONString();

        System.out.println("Output JSON: " + output);
        return output;
    }

}
