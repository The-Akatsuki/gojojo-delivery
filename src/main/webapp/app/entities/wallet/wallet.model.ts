import { ITransaction } from 'app/entities/transaction/transaction.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IWallet {
  id?: number;
  usableBalance?: number | null;
  totalBalance?: number | null;
  balanceOnHold?: number | null;
  transactions?: ITransaction[] | null;
  order?: IOrder | null;
}

export class Wallet implements IWallet {
  constructor(
    public id?: number,
    public usableBalance?: number | null,
    public totalBalance?: number | null,
    public balanceOnHold?: number | null,
    public transactions?: ITransaction[] | null,
    public order?: IOrder | null
  ) {}
}

export function getWalletIdentifier(wallet: IWallet): number | undefined {
  return wallet.id;
}
