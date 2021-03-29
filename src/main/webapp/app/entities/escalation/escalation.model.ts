import { IManifest } from 'app/entities/manifest/manifest.model';
import { IReasonEscalation } from 'app/entities/reason-escalation/reason-escalation.model';

export interface IEscalation {
  id?: number;
  remark?: string | null;
  manifests?: IManifest[] | null;
  reason?: IReasonEscalation | null;
}

export class Escalation implements IEscalation {
  constructor(
    public id?: number,
    public remark?: string | null,
    public manifests?: IManifest[] | null,
    public reason?: IReasonEscalation | null
  ) {}
}

export function getEscalationIdentifier(escalation: IEscalation): number | undefined {
  return escalation.id;
}
