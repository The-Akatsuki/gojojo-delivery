import { IReturnLabel } from 'app/entities/return-label/return-label.model';

export interface IReturnReason {
  id?: number;
  comment?: string | null;
  image?: string | null;
  label?: IReturnLabel | null;
}

export class ReturnReason implements IReturnReason {
  constructor(public id?: number, public comment?: string | null, public image?: string | null, public label?: IReturnLabel | null) {}
}

export function getReturnReasonIdentifier(returnReason: IReturnReason): number | undefined {
  return returnReason.id;
}
