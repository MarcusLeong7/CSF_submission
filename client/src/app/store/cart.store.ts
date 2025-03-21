import {ComponentStore} from '@ngrx/component-store';
import {Cart, Menu} from '../models/models';
import {Injectable} from '@angular/core';

// For initializing initial state
const INIT: Cart = {
  lineItems: []
}

/* Creates a specialised store for managing the TaskSlice state*/
@Injectable()
export class CartStore extends ComponentStore<Cart> {

  constructor() {
    // Initialize the store with default state
    super(INIT);
  }

  /* MUTATORS - UPDATE METHODS */
  // addItem (Menu)
  readonly addToCart = this.updater<Menu>(
    (state: Cart, newLineItem: Menu) => {
      // Check if the item already exists in the cart
      const existingItemIndex = state.lineItems.findIndex(item => item.id === newLineItem.id);

      if (existingItemIndex !== -1) {
        // Item exists, update its quantity
        const updatedItems = [...state.lineItems];
        const existingItem = updatedItems[existingItemIndex];

        updatedItems[existingItemIndex] = {
          ...existingItem,
          quantity: (existingItem.quantity || 0) + 1
        };

        return {
          ...state,
          lineItems: updatedItems
        } as Cart;
      } else {
        // Item doesn't exist, add it to the cart with quantity 1
        return {
          ...state,
          lineItems: [...state.lineItems, { ...newLineItem, quantity: 1 }]
        } as Cart;
      }
    }
  );

  // deleteItem
  readonly deleteFromCart = this.updater<Menu>(
    (state: Cart, itemToDelete: Menu) => {
      const existingItemIndex = state.lineItems.findIndex(item => item.id === itemToDelete.id);
      if (existingItemIndex !== -1) {
        const updatedItems = [...state.lineItems];
        const existingItem = updatedItems[existingItemIndex];
        // If item exists, delete by filtering
        if (existingItem.quantity === 1) {
          return {
            ...state,
            lineItems: state.lineItems.filter(item => item.id !== itemToDelete.id)
          };
        } else {
          // Otherwise, decrease quantity by 1
          updatedItems[existingItemIndex] = {
            ...existingItem,
            quantity: (existingItem.quantity || 0) - 1
          };

          return {
            ...state,
            lineItems: updatedItems
          } as Cart;
        }
      }

      return state;
    }
  );

  // Clear Entire Cart
  readonly clearCart = this.updater((state) => {
    return {
      ...state,
      lineItems: []
    };
  });

  /* SELECTORS - Query */
  // Get all line items
  readonly menuItems$ = this.select(state => state.lineItems);

  // Get line item count
  readonly cartItemCount$ = this.select(state =>
    state.lineItems.reduce((total, item) => total + (item.quantity || 0), 0)
  );

  // Calculate total price
  readonly cartTotal$ = this.select(state =>
    state.lineItems.reduce((total, item) => total + (item.price * (item.quantity || 0)), 0)
  );

  // Returns the entire cart state
  readonly cart$ = this.select(state => state);

}
