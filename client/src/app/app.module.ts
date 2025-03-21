import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { provideHttpClient } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { MenuComponent } from './components/menu.component';
import { PlaceOrderComponent } from './components/place-order.component';

import { ConfirmationComponent } from './components/confirmation.component';
import {RouterModule, Routes} from '@angular/router';
import {RestaurantService} from './services/restaurant.service';
import {CartStore} from './store/cart.store';

const appRoutes: Routes = [
  // View 0: Home
  { path: '', component: MenuComponent },
  // View 1: Place Order
  { path: 'confirm', component: PlaceOrderComponent },
  { path: 'order', component: ConfirmationComponent },
  // wild card must be the last route
  { path: '**', redirectTo: '/', pathMatch: 'full' }
]

@NgModule({
  declarations: [
    AppComponent, MenuComponent, PlaceOrderComponent, ConfirmationComponent
  ],
  imports: [
    BrowserModule, ReactiveFormsModule,RouterModule.forRoot(appRoutes, { useHash: true })
  ],
  providers: [ provideHttpClient(),RestaurantService,CartStore],
  bootstrap: [AppComponent]
})
export class AppModule { }
