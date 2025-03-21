import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Menu, Order} from '../models/models';



@Injectable({ providedIn: 'root'})
export class RestaurantService {

  private http = inject(HttpClient);

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems() {
    return this.http.get<Menu[]>('/api/menu')
  }

  // TODO: Task 3.2
  placeOrder(order: Order){
    // Setup headers for the HTTP request
    // Sending request to backend
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
    return this.http.post('/api/food_order', order, { headers });
  }
}
