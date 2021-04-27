import { IProduct } from 'app/entities/product/product.model';

export interface ICategory {
  id?: number;
  name?: string | null;
  isActive?: boolean | null;
  categories?: ICategory[] | null;
  parent?: ICategory | null;
  products?: IProduct[] | null;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string | null,
    public isActive?: boolean | null,
    public categories?: ICategory[] | null,
    public parent?: ICategory | null,
    public products?: IProduct[] | null
  ) {
    this.isActive = this.isActive ?? false;
  }
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
