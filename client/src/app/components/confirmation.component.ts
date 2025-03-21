import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit {

  // TODO: Task 5
  private router = inject(Router)

  // Order details properties
  orderId: string = '';
  paymentId: string = '';
  orderDate: string = '';
  total: number = 0;

  ngOnInit() {
    // Get order details from sessionStorage
    const orderDetailsString = sessionStorage.getItem('orderDetails');

    if (orderDetailsString) {
      try {
        const orderDetails = JSON.parse(orderDetailsString);

        this.orderId = orderDetails.orderId;
        this.paymentId = orderDetails.paymentId;
        this.orderDate = orderDetails.orderDate;
        this.total = orderDetails.total;

        sessionStorage.removeItem('orderDetails');
      } catch (error) {
        console.error('Error parsing order details:', error);
        this.navigateToMenu();
      }
    } else {
      console.warn('No order details found');
      this.navigateToMenu();
    }
  }

  navigateToMenu() {
    this.router.navigate(['/']);
  }

}
