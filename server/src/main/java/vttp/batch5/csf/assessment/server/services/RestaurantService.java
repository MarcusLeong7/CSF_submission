package vttp.batch5.csf.assessment.server.services;


import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.batch5.csf.assessment.server.models.*;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepo;

  @Autowired
  private RestaurantRepository restaurantRepo;

  @Autowired
  private PaymentService paymentSvc;


  // TODO: Task 2.2
  // You may change the method's signature
  public List<Menu> getMenu() {
    return ordersRepo.getMenu();
  }
  
  // TODO: Task 4
  public OrderSummary processOrder(Order order) throws Exception {

    // Calculate total price
    float total = 0;
    for (Items item : order.getItems()) {
      total += item.getPrice() * item.getQuantity();
    }
    // Generate random UUID for orderId
    String orderId = UUID.randomUUID().toString().substring(0, 8);

    /* Payee: Official Name as in NRIC */
    PaymentResponse resp = paymentSvc.makePayment(orderId, order.getUsername(), "Marcus", total);

    // Create order summary
    OrderSummary summary = new OrderSummary();
    summary.setOrderId(resp.getOrder_id());
    summary.setPaymentId(resp.getPayment_id());
    summary.setOrderDate(resp.getDate()); // Convert to Date Object
    summary.setTotal(total);
    summary.setUsername(order.getUsername());

    // Save order in MySQL database
    restaurantRepo.placeOrder(summary);
    // Also save the order in MongoDB
    ordersRepo.insertOrder(summary, order);


    // Return order summary
    return summary;
  }


}
