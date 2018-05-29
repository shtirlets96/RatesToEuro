package app;

import app.index.APIs.CPAPI;
import app.index.APIs.FrXML;
import app.index.APIs.OxrAPI;
import app.index.EuroRate;
import app.index.IndexController;


import java.io.IOException;
import java.util.Properties;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;


public class Applicaion {
    public static String requestResult;

    public static void main(String[] args) throws IOException {
        ProcessBuilder pb = null;
        Properties prop = System.getProperties();
        if (prop.getProperty("os.name").contains("Windows")) {
            pb = new ProcessBuilder("cmd");
        } else {
            pb = new ProcessBuilder("xterm");
        }
        pb.start();
        EuroRate ER = new EuroRate();
        requestResult = ER.rate;
        port(9015);
        staticFileLocation("/public");
        System.out.println("server is started at port 9015");
        IndexController.setControl();
    }
}
