package app.index.APIs;

public class OxrAPI extends JsonAPI{
    public OxrAPI() {
        APISecretPath = "src/main/resources/keys/OxrSecret.txt";
        APISecret = getAPISecret();
        reqURL = "https://openexchangerates.org/api/latest.json" + "?app_id=" + APISecret+ "&symbols=EUR,";
        valuesLocation = "rates";

    }
}
