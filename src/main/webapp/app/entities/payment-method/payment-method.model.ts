import { IOrder } from 'app/entities/order/order.model';

export interface IPaymentMethod {
  id?: number;
  name?: string | null;
  isActive?: boolean | null;
  orders?: IOrder[] | null;
}

export class PaymentMethod implements IPaymentMethod {
  constructor(public id?: number, public name?: string | null, public isActive?: boolean | null, public orders?: IOrder[] | null) {
    this.isActive = this.isActive ?? false;
  }
}

export function getPaymentMethodIdentifier(paymentMethod: IPaymentMethod): number | undefined {
  return paymentMethod.id;
}
