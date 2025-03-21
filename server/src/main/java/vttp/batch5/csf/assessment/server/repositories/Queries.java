package vttp.batch5.csf.assessment.server.repositories;

public class Queries {

    // Orders table queries
    public static String SQL_ORDER_DETAILS = "insert into place_orders (order_id,payment_id,order_date,total,username) values (?,?,?,?,?)";

    // Customers table queries
    public static String SQL_USER_VERIFICATION = "select count(*) from customers where username = ? and password = SHA2(?,224)";;
}

