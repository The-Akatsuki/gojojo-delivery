import * as dayjs from 'dayjs';
import { IShipmentActivity } from 'app/entities/shipment-activity/shipment-activity.model';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { IOrderBuyerDetails } from 'app/entities/order-buyer-details/order-buyer-details.model';
import { IProduct } from 'app/entities/product/product.model';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { IPickupAddress } from 'app/entities/pickup-address/pickup-address.model';
import { OrderType } from 'app/entities/enumerations/order-type.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface IOrder {
  id?: number;
  orderId?: string | null;
  referenceId?: string | null;
  orderType?: OrderType | null;
  orderStatus?: OrderStatus | null;
  orderDate?: dayjs.Dayjs | null;
  channel?: string | null;
  subtotal?: number | null;
  hasShippingCharges?: boolean | null;
  shipping?: number | null;
  hasGiftwrapCharges?: boolean | null;
  giftwrap?: number | null;
  hasTransactionCharges?: boolean | null;
  transaction?: number | null;
  hasDiscount?: boolean | null;
  discount?: number | null;
  weight?: number | null;
  weightCharges?: number | null;
  excessWeightCharges?: number | null;
  totalFreightCharges?: number | null;
  length?: number | null;
  width?: number | null;
  height?: number | null;
  resellerName?: string | null;
  gstin?: string | null;
  courier?: string | null;
  awb?: string | null;
  manifestId?: string | null;
  shipmentActivities?: IShipmentActivity[] | null;
  wallets?: IWallet[] | null;
  buyerDetails?: IOrderBuyerDetails | null;
  product?: IProduct | null;
  payment?: IPaymentMethod | null;
  pickupaddress?: IPickupAddress | null;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public orderId?: string | null,
    public referenceId?: string | null,
    public orderType?: OrderType | null,
    public orderStatus?: OrderStatus | null,
    public orderDate?: dayjs.Dayjs | null,
    public channel?: string | null,
    public subtotal?: number | null,
    public hasShippingCharges?: boolean | null,
    public shipping?: number | null,
    public hasGiftwrapCharges?: boolean | null,
    public giftwrap?: number | null,
    public hasTransactionCharges?: boolean | null,
    public transaction?: number | null,
    public hasDiscount?: boolean | null,
    public discount?: number | null,
    public weight?: number | null,
    public weightCharges?: number | null,
    public excessWeightCharges?: number | null,
    public totalFreightCharges?: number | null,
    public length?: number | null,
    public width?: number | null,
    public height?: number | null,
    public resellerName?: string | null,
    public gstin?: string | null,
    public courier?: string | null,
    public awb?: string | null,
    public manifestId?: string | null,
    public shipmentActivities?: IShipmentActivity[] | null,
    public wallets?: IWallet[] | null,
    public buyerDetails?: IOrderBuyerDetails | null,
    public product?: IProduct | null,
    public payment?: IPaymentMethod | null,
    public pickupaddress?: IPickupAddress | null
  ) {
    this.hasShippingCharges = this.hasShippingCharges ?? false;
    this.hasGiftwrapCharges = this.hasGiftwrapCharges ?? false;
    this.hasTransactionCharges = this.hasTransactionCharges ?? false;
    this.hasDiscount = this.hasDiscount ?? false;
  }
}

export function getOrderIdentifier(order: IOrder): number | undefined {
  return order.id;
}
