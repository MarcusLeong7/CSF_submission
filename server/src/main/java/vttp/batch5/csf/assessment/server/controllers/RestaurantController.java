package vttp.batch5.csf.assessment.server.controllers;

import jakarta.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vttp.batch5.csf.assessment.server.models.Items;
import vttp.batch5.csf.assessment.server.models.Menu;
import vttp.batch5.csf.assessment.server.models.Order;
import vttp.batch5.csf.assessment.server.models.OrderSummary;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(path="/api")
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantSvc;

  @Autowired
  private RestaurantRepository restaurantRepo;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path="/menu",produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getMenus(){

    try{
      // Get Menu Items
      List<Menu> menuList = restaurantSvc.getMenu();
      System.out.println("Menu List:"+ menuList);

      //Create Json Array Builder
      JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
      for (Menu item : menuList) {
        arrBuilder.add(
                Json.createObjectBuilder()
                        .add("id", item.get_id())
                        .add("name", item.getName())
                        .add("description", item.getDescription())
                        .add("price", item.getPrice())
                        .build()
        );
      }
        // Build the final JSON array
        JsonArray resp = arrBuilder.build();
        System.out.println(resp.toString());
        return ResponseEntity.ok(resp.toString());

    } catch (Exception e) {
      JsonObject errResp = Json.createObjectBuilder()
              .add("error", e.getMessage())
              .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(errResp.toString());
    }

  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path="/food_order", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    try {
      // Extract payload as Json
      JsonReader reader = Json.createReader(new StringReader(payload));
      JsonObject jsonObject = reader.readObject();

      // Extract order details
      String username = jsonObject.getString("username");
      System.out.println("getting username:" + username);
      String password = jsonObject.getString("password");
      System.out.println("getting password:" + password);

      // Verify user credentials in the controller
      try {
        boolean isValid = restaurantRepo.verifyUser(username,password);
        if (!isValid) {
          JsonObject errResp = Json.createObjectBuilder()
                  .add("message", "Invalid username or password")
                  .build();

          // Return 401 Unauthorized status for invalid credentials
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                  .body(errResp.toString());
        }
      } catch (Exception e) {
        JsonObject errResp = Json.createObjectBuilder()
                .add("message", "Authentication failed: " + e.getMessage())
                .build();

        // Return 401 Unauthorized status for authentication errors
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errResp.toString());
      }

      // Parse items
      List<Items> items = new LinkedList<>();
      JsonArray itemsArray = jsonObject.getJsonArray("items");

      for (int i = 0; i < itemsArray.size(); i++) {
        JsonObject itemObj = itemsArray.getJsonObject(i);
        Items item = new Items();
        item.setId(itemObj.getString("id"));
        item.setPrice(itemObj.getJsonNumber("price").bigDecimalValue().floatValue());
        item.setQuantity(itemObj.getInt("quantity"));
        items.add(item);
      }
      // Create order object
      Order order = new Order();
      order.setUsername(username);
      order.setPassword(password);
      order.setItems(items);

      // Process the order
      OrderSummary summary = restaurantSvc.processOrder(order);

      // Format date for response
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String formattedDate = dateFormat.format(summary.getOrderDate());

      JsonObject resp = Json.createObjectBuilder()
              .add("orderId", summary.getOrderId())
              .add("paymentId", summary.getPaymentId())
              .add("total", summary.getTotal())
              .add("timestamp",formattedDate)
              .build();

      // Return 200 status with the order details
      return ResponseEntity.ok(resp.toString());

    } catch (Exception ex) {
      ex.printStackTrace();
      JsonObject errResp = Json.createObjectBuilder()
              .add("message", ex.getMessage())
              .build();

      // Return 400 status error for other errors
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(errResp.toString());
    }
  }
}


