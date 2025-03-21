import {Component, inject, OnInit} from '@angular/core';
import {RestaurantService} from '../services/restaurant.service';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {Cart, Menu} from '../models/models';
import {CartStore} from '../store/cart.store';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {

  // TODO: Task 2
  private restaurantSvc = inject(RestaurantService);
  private activatedRoute = inject(ActivatedRoute)
  private cartStore = inject(CartStore);

  itemCount$ = this.cartStore.cartItemCount$;
  menuItems$!: Observable<Menu[]>
  cartTotal$ = this.cartStore.cartTotal$;

  ngOnInit() {
    console.log("menu items:" + this.restaurantSvc.getMenuItems());
    this.menuItems$ = this.restaurantSvc.getMenuItems()
  }


  addToCart(li: Menu) {
    // Create a copy of the menu item to add to cart
    const itemToAdd: Menu = {
        id: li.id,
        name: li.name,
        description: li.description,
        price: li.price,
        quantity: 1,
      }
    // Add to cart using your CartStore service
    this.cartStore.addToCart(itemToAdd);
  }


  deleteFromCart(li: Menu) {
    const itemToDelete: Menu = {
      id: li.id,
      name: li.name,
      description: li.description,
      price: li.price,
      quantity: -1,
    }
   // delete form cart
    this.cartStore.deleteFromCart(itemToDelete);
  }

  // Helper method to calculate total cost
  getItemQuantityInCart(itemId: string): number {
    let quantity = 0;
    this.cartStore.menuItems$.subscribe(items => {
      const foundItem = items.find(item => item.id === itemId);
      if (foundItem) {
        quantity = foundItem.quantity || 0;
      }
    });
    return quantity;
  }

}
