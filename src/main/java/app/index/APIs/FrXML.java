package app.index.APIs;

import com.github.kevinsawicki.http.HttpRequest;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static app.index.EuroRate.currencies;

public class FrXML implements CurrenciesService{
    private String reqURL = "http://www.floatrates.com/daily/usd.xml";
    public JSONObject getFormatedResponse(){
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
}
