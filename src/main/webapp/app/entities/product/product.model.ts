import { ICategory } from 'app/entities/category/category.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IProduct {
  id?: number;
  name?: string | null;
  sku?: string | null;
  quantity?: number | null;
  unitPrice?: number | null;
  taxRate?: number | null;
  hsn?: string | null;
  discount?: number | null;
  categories?: ICategory[] | null;
  orders?: IOrder[] | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public sku?: string | null,
    public quantity?: number | null,
    public unitPrice?: number | null,
    public taxRate?: number | null,
    public hsn?: string | null,
    public discount?: number | null,
    public categories?: ICategory[] | null,
    public orders?: IOrder[] | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
