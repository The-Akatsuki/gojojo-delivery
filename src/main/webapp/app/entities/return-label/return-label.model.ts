import { IReturnReason } from 'app/entities/return-reason/return-reason.model';

export interface IReturnLabel {
  id?: number;
  title?: string | null;
  returnReasons?: IReturnReason[] | null;
}

export class ReturnLabel implements IReturnLabel {
  constructor(public id?: number, public title?: string | null, public returnReasons?: IReturnReason[] | null) {}
}

export function getReturnLabelIdentifier(returnLabel: IReturnLabel): number | undefined {
  return returnLabel.id;
}
