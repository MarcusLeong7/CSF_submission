package vttp.batch5.csf.assessment.server.models;

import java.util.LinkedList;
import java.util.List;

public class Order {

    private String username;
    private String password;;
    private List<Items> items = new LinkedList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
               "username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", items=" + items +
               '}';
    }
}
