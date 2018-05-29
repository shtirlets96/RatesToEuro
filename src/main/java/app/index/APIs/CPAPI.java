package app.index.APIs;

import com.github.kevinsawicki.http.HttpRequest;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CPAPI extends JsonAPI {
    public CPAPI() {
        APISecretPath = "src/main/resources/keys/CPSecret.txt";
        APISecret = getAPISecret();
        reqURL =  "http://apilayer.net/api/live" + "?access_key=" + APISecret + "&currencies=EUR,";
        valuesLocation = "quotes";
    }
    void getResponse(){
        String response = HttpRequest.get(reqURL).body();
        responseObj = (JSONObject) JSONValue.parse(response);
        responseObj = (JSONObject) responseObj.get(valuesLocation);
        JSONObject cutPreficsObj = new JSONObject();
        for (Object key : responseObj.keySet()) {
                cutPreficsObj.put(key.toString().substring(3, 6), responseObj.get(key).toString());
        }
        responseObj = cutPreficsObj;
    }
}
