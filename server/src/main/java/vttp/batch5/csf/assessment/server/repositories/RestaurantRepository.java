package vttp.batch5.csf.assessment.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vttp.batch5.csf.assessment.server.models.OrderSummary;
import static vttp.batch5.csf.assessment.server.repositories.Queries.*;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate template;

    public void placeOrder(OrderSummary summary) {
        // Insert into place_orders table
        template.update(
                SQL_ORDER_DETAILS,
                summary.getOrderId(),
                summary.getPaymentId(),
                summary.getOrderDate(),
                summary.getTotal(),
                summary.getUsername()
        );
    }

    // Verify user credentials
    public boolean verifyUser(String username, String password) {
        System.out.println("Verifying user: " + username);
        // Don't log the password in production code
        Integer count = template.queryForObject(SQL_USER_VERIFICATION,
                Integer.class, username, password);
        System.out.println("Found users: " + count);
        return count != null && count > 0;
    }

}
