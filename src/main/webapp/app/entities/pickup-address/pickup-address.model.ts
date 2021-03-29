import { IOrder } from 'app/entities/order/order.model';

export interface IPickupAddress {
  id?: number;
  nickName?: string | null;
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
  isMobileVerified?: boolean | null;
  otp?: string | null;
  orders?: IOrder[] | null;
}

export class PickupAddress implements IPickupAddress {
  constructor(
    public id?: number,
    public nickName?: string | null,
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
    public isMobileVerified?: boolean | null,
    public otp?: string | null,
    public orders?: IOrder[] | null
  ) {
    this.isMobileVerified = this.isMobileVerified ?? false;
  }
}

export function getPickupAddressIdentifier(pickupAddress: IPickupAddress): number | undefined {
  return pickupAddress.id;
}
