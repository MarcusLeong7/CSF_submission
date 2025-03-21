import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {CartStore} from '../store/cart.store';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {RestaurantService} from '../services/restaurant.service';
import {Menu, Order, OrderItem} from '../models/models';
import {Subscription} from 'rxjs';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit, OnDestroy {

  // TODO: Task 3

  private router = inject(Router);
  private fb = inject(FormBuilder);
  private cartStore = inject(CartStore);
  private restaurantSvc = inject(RestaurantService);

  protected form !: FormGroup;

  protected menuItems: Menu[] = [];
  protected cartTotal: number = 0;
  // For error handling
  orderError: string | null = null;
  isSubmitting = false;

  private cartSubscription!: Subscription;
  private totalSubscription!: Subscription;

  ngOnInit() {
    this.form = this.createForm()
    // Subscribe to cart items
    this.cartSubscription = this.cartStore.menuItems$.subscribe(items => {
      this.menuItems = items;
    });
    // Subscribe to cart total
    this.totalSubscription = this.cartStore.cartTotal$.subscribe(total => {
      this.cartTotal = total;
    });
  }

  private createForm() {
    return this.fb.group({
      username: this.fb.control<string>("", [Validators.required]),
      password: this.fb.control<string>("", [Validators.required]),
    })
  }

  processForm(): void {
    if (this.form.valid) {
      // Convert Menu items to OrderItem format to send to backend
      const orderItems: OrderItem[] = this.menuItems.map(item => ({
        id: item.id,
        price: item.price,
        quantity: item.quantity || 0
      }));
      const order: Order = {
        username: this.form.value.username,
        password: this.form.value.password,
        items: orderItems
      };
      this.restaurantSvc.placeOrder(order).subscribe({
        next: (response: any) => {
          console.log('Order successful:', response);

          // Store the order details in sessionStorage to access in confirmation component
          sessionStorage.setItem('orderDetails', JSON.stringify(response));

          // Clear the cart after successful order
          this.cartStore.clearCart();

          // Navigate to order confirmation page
          this.router.navigate(['/order']);
        },
        error: (err: HttpErrorResponse) => {
          console.error('Error placing order:', err);
          this.isSubmitting = false;

          if (err.status === 401) {
            // Handle authentication error
            this.orderError = 'Invalid username or password. Please try again.';
          } else if (err.error && err.error.message) {
            this.orderError = err.error.message;
          }
        }
      });
    }
  }

  navigateToMenu():void{
    this.cartStore.clearCart;
    this.router.navigate(['/']);
  }

  ngOnDestroy() {
      this.cartSubscription.unsubscribe();
      this.totalSubscription.unsubscribe();
  }


}
