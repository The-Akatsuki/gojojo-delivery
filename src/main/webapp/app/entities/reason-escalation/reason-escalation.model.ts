import { IEscalation } from 'app/entities/escalation/escalation.model';

export interface IReasonEscalation {
  id?: number;
  title?: string | null;
  escalations?: IEscalation[] | null;
}

export class ReasonEscalation implements IReasonEscalation {
  constructor(public id?: number, public title?: string | null, public escalations?: IEscalation[] | null) {}
}

export function getReasonEscalationIdentifier(reasonEscalation: IReasonEscalation): number | undefined {
  return reasonEscalation.id;
}
