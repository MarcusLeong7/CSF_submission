// You may use this file to create any models
export interface Menu {
  id: string;
  name: string;
  description: string;
  price:number;
  quantity?:number;
}

// For component store
// Structure of the state or slice
export interface Cart {
  lineItems: Menu[]
}

export interface OrderItem {
  id: string;
  price:number;
  quantity:number;
}

export interface Order {
  username: string,
  password:string,
  items: OrderItem[]
}

// Response after placing an order
export interface OrderResponse {
  orderId: string;
  paymentId: string;
  orderDate: string;
  total: number;
}
