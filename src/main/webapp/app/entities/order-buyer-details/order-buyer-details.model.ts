import { IOrder } from 'app/entities/order/order.model';

export interface IOrderBuyerDetails {
  id?: number;
  name?: string | null;
  mobile?: string | null;
  alternateMobile?: string | null;
  email?: string | null;
  addressline1?: string | null;
  addressline2?: string | null;
  pincode?: string | null;
  city?: string | null;
  state?: string | null;
  country?: string | null;
  companyName?: string | null;
  isBillingSame?: boolean | null;
  billAddressline1?: string | null;
  billAddressline2?: string | null;
  billPincode?: string | null;
  billCity?: string | null;
  billState?: string | null;
  billCountry?: string | null;
  order?: IOrder | null;
}

export class OrderBuyerDetails implements IOrderBuyerDetails {
  constructor(
    public id?: number,
    public name?: string | null,
    public mobile?: string | null,
    public alternateMobile?: string | null,
    public email?: string | null,
    public addressline1?: string | null,
    public addressline2?: string | null,
    public pincode?: string | null,
    public city?: string | null,
    public state?: string | null,
    public country?: string | null,
    public companyName?: string | null,
    public isBillingSame?: boolean | null,
    public billAddressline1?: string | null,
    public billAddressline2?: string | null,
    public billPincode?: string | null,
    public billCity?: string | null,
    public billState?: string | null,
    public billCountry?: string | null,
    public order?: IOrder | null
  ) {
    this.isBillingSame = this.isBillingSame ?? false;
  }
}

export function getOrderBuyerDetailsIdentifier(orderBuyerDetails: IOrderBuyerDetails): number | undefined {
  return orderBuyerDetails.id;
}
