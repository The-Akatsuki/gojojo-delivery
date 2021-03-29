import { IWallet } from 'app/entities/wallet/wallet.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

export interface ITransaction {
  id?: number;
  category?: string | null;
  credit?: number | null;
  debit?: number | null;
  finalBalance?: number | null;
  description?: string | null;
  transactionType?: TransactionType | null;
  wallet?: IWallet | null;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public category?: string | null,
    public credit?: number | null,
    public debit?: number | null,
    public finalBalance?: number | null,
    public description?: string | null,
    public transactionType?: TransactionType | null,
    public wallet?: IWallet | null
  ) {}
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
