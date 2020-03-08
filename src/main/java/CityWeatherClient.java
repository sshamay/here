import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;


class CityWeatherClient {

    private static String url = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";


    public static CityWeatherDetails getCityWeather(String city,String appkey) {

        String urlFormatted = String.format(url, city, appkey);
        JSONObject myRes = getCityWeather(urlFormatted);

        try {
            JSONObject sys = (JSONObject) myRes.get("sys");
            Long sunrise = (Long) sys.get("sunrise");
            Long sunset = (Long) sys.get("sunset");
            JSONObject main = (JSONObject) myRes.get("main");
            Double temperature = (Double) main.get("temp");
            System.out.println("City: " + city + " temperature : " + temperature + " Daylight Duration in seconds : " + (sunset - sunrise));

            return new CityWeatherDetails(city, sunset, sunrise, temperature);

        }catch(ClassCastException e){
            throw new RuntimeException("There was a problem with one of the City Weather details ",e) ;
        }
    }

    private static JSONObject getCityWeather(String url) {

        HttpResponse response = null;
        final int numOfRetries = 5;
        String entityText;

        CloseableHttpClient client = HttpClientBuilder.create().build();


        HttpGet get = new HttpGet(url);

        int attempts = 0;
        try {
            while (response == null && attempts++ < numOfRetries) {
                try {

                    response = client.execute(get);
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode >= 400) {
                        throw new RuntimeException("Request: " + url + ", GET status code " + statusCode);

                    }
                    entityText = EntityUtils.toString(response.getEntity());
                    EntityUtils.consume(response.getEntity());

                    return (JSONObject) new JSONParser().parse(entityText);

                } catch (IOException e) {
                    System.out.println("Failed getting weather sample,  attempt " + attempts);
                    e.printStackTrace();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        System.out.println("Sleep interrupted error");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
