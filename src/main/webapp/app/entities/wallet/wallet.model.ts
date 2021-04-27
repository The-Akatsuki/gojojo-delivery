import { IOrder } from 'app/entities/order/order.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';

export interface IWallet {
  id?: number;
  usableBalance?: number | null;
  totalBalance?: number | null;
  balanceOnHold?: number | null;
  orders?: IOrder[] | null;
  transactions?: ITransaction[] | null;
}

export class Wallet implements IWallet {
  constructor(
    public id?: number,
    public usableBalance?: number | null,
    public totalBalance?: number | null,
    public balanceOnHold?: number | null,
    public orders?: IOrder[] | null,
    public transactions?: ITransaction[] | null
  ) {}
}

export function getWalletIdentifier(wallet: IWallet): number | undefined {
  return wallet.id;
}
