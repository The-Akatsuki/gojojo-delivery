import { IOrder } from 'app/entities/order/order.model';

export interface IShipmentActivity {
  id?: number;
  status?: string | null;
  pincode?: string | null;
  location?: string | null;
  order?: IOrder | null;
}

export class ShipmentActivity implements IShipmentActivity {
  constructor(
    public id?: number,
    public status?: string | null,
    public pincode?: string | null,
    public location?: string | null,
    public order?: IOrder | null
  ) {}
}

export function getShipmentActivityIdentifier(shipmentActivity: IShipmentActivity): number | undefined {
  return shipmentActivity.id;
}
