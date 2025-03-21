package vttp.batch5.csf.assessment.server.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.stereotype.Repository;
import vttp.batch5.csf.assessment.server.models.Items;
import vttp.batch5.csf.assessment.server.models.Menu;
import vttp.batch5.csf.assessment.server.models.Order;
import vttp.batch5.csf.assessment.server.models.OrderSummary;

import java.util.ArrayList;
import java.util.List;


@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate template;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  /*
    db.menus.aggregate([
    {$project:{_id:1,name:1,description:1,price:1}},
    {$sort:{name:1}}
])*/
  public List<Menu> getMenu() {

    ProjectionOperation projectMenu = Aggregation.project("_id")
            .and("name").as("name")
            .and("description").as("description")
            .and("price").as("price");

    SortOperation sortMenu = Aggregation.sort((Sort.by(Sort.Direction.ASC, "name")));

    Aggregation pipeline  = Aggregation.newAggregation(projectMenu, sortMenu);

    return template.aggregate(pipeline,"menus", Menu.class).getMappedResults();
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
     /*
       db.restaurant.insert({
        _id: <string>,
        order_id: <string>,
        username: <string>,
        total: <float>,
        timestamp: <long>,
        items: items[]
    })
    */
  public String insertOrder(OrderSummary summary, Order order) {
    try {
      // Create document for MongoDB
      Document orderDoc = new Document();
      orderDoc.append("_id", summary.getOrderId());
      orderDoc.append("order_id", summary.getOrderId());
      orderDoc.append("payment_id", summary.getPaymentId());
      orderDoc.append("username", summary.getUsername());
      orderDoc.append("total", summary.getTotal());
      orderDoc.append("timestamp", summary.getOrderDate());

      // Convert order items to documents
      List<Document> itemDocs = new ArrayList<>();
      for (Items item : order.getItems()) {
        Document itemDoc = new Document();
        itemDoc.append("id", item.getId());
        itemDoc.append("price", item.getPrice());
        itemDoc.append("quantity", item.getQuantity());
        itemDocs.add(itemDoc);
      }

      orderDoc.append("items", itemDocs);

      // Insert document into MongoDB
      Document inserted = template.insert(orderDoc, "orders");
      String id = inserted.getString("_id");

      return id;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
}
