package vttp.batch5.csf.assessment.server.services;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vttp.batch5.csf.assessment.server.models.PaymentResponse;

import java.util.Collections;


@Service
public class PaymentService {

    private static final String PAYMENT_URL = "https://payment-service-production-a75a.up.railway.app/api/payment";

    public PaymentResponse makePayment(String orderId, String payer, String payee, float amount) {
        // Set up headers with correct content types
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-Authenticate", payer);

        // Create JSON request body
        JsonObject req = Json.createObjectBuilder()
                .add("order_id", orderId)
                .add("payer", payer)
                .add("payee", payee)
                .add("payment", amount)
                .build();

        System.out.println("Sending payment request to: " + PAYMENT_URL);
        System.out.println("Request body: " + req);

        // Create HTTP entity with headers and JSON object
        HttpEntity<JsonObject> requestEntity = new HttpEntity<>(req, headers);

        // Send the request
        RestTemplate template = new RestTemplate();
        ResponseEntity<PaymentResponse> resp = template.postForEntity(
                PAYMENT_URL,
                requestEntity,
                PaymentResponse.class
        );

        System.out.println("Payment response status: " + resp.getStatusCode());
        System.out.println("Payment response body: " + resp.getBody());

        return resp.getBody();

        /* Expected Response */
        /*
        * { "payment_id":"",
        * "order_id":"",
        * "timestamp:""
        * }
        * */


    }
}
