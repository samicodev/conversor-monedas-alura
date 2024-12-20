package org.alura;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Gson gson = new Gson();
    static JsonObject responseJson;
    static Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) {
        boolean flag = true;
        String welcomeMessage = "" +
            "--------------------------------------------------------------" + "\n" +
            "| ****************** CONVERSOR DE MONEDAS ****************** |" + "\n" +
            "| ---------------------------------------------------------- |" + "\n" +
            "| 1.- Dólar => Peso argentino                                |" + "\n" +
            "| 2.- Peso argentino => Dólar                                |" + "\n" +
            "| 3.- Dólar => Real brasileño                                |" + "\n" +
            "| 4.- Real brasileño => Dólar                                |" + "\n" +
            "| 5.- Dólar => Peso colombiano                               |" + "\n" +
            "| 6.- Peso colombiano => Dólar                               |" + "\n" +
            "| 7.- Salir                                                  |" + "\n" +
            "--------------------------------------------------------------" + "\n\n" +
            "Elija una opcion válida: ";

        while (flag) {
            System.out.print(welcomeMessage);
            String option = scanner.next();

            Double prevValue;
            Double postValue;

            switch (option) {
                case "1":
                    prevValue = getPrevValue();
                    postValue = getPostValue("USD", "ARS", prevValue);

                    getResultMessage(prevValue, postValue, "USD", "ARS");
                    break;
                case "2":
                    prevValue = getPrevValue();
                    postValue = getPostValue("ARS", "USD", prevValue);

                    getResultMessage(prevValue, postValue, "ARS", "USD");
                    break;
                case "3":
                    prevValue = getPrevValue();
                    postValue = getPostValue("USD", "BRL", prevValue);

                    getResultMessage(prevValue, postValue, "USD", "BRL");
                    break;
                case "4":
                    prevValue = getPrevValue();
                    postValue = getPostValue("BRL", "USD", prevValue);

                    getResultMessage(prevValue, postValue, "BRL", "USD");
                    break;
                case "5":
                    prevValue = getPrevValue();
                    postValue = getPostValue("USD", "COP", prevValue);

                    getResultMessage(prevValue, postValue, "USD", "COP");
                    break;
                case "6":
                    prevValue = getPrevValue();
                    postValue = getPostValue("COP", "USD", prevValue);

                    getResultMessage(prevValue, postValue, "COP", "USD");
                    break;
                default:
                    System.out.println("¡Gracias por utilizar esta aplicación señor usuari@!");
                    flag = false;
                    break;
            }
        }
    }

    static Double getPrevValue() {
        String convertMessage = "Ingrese el valor que deseas convertir: ";
        System.out.print(convertMessage);

        Double prevValue = scanner.nextDouble();
        System.out.println();

        return prevValue;
    }

    static Double getPostValue(String baseCode, String targetCode, Double prevValue) {
        fetchAPI(baseCode, targetCode);

        Moneda valueTargetCode = gson.fromJson(responseJson, Moneda.class);
        return prevValue * valueTargetCode.getConversion_rate();
    }

    static void fetchAPI(String baseCode, String targetCode) {
        String API_KEY = dotenv.get("API_KEY");
        String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + baseCode + "/" + targetCode;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            responseJson = gson.fromJson(response.body(), JsonObject.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void getResultMessage(Double prevValue, Double postValue, String baseCode, String targetCode) {
        System.out.println("El valor " + prevValue + " [" + baseCode + "] " + "corresponde al valor final de => " + postValue + " [" + targetCode + "]");
        System.out.println();
    }
}