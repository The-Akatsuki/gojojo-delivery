import * as dayjs from 'dayjs';
import { ICourierCompany } from 'app/entities/courier-company/courier-company.model';
import { IEscalation } from 'app/entities/escalation/escalation.model';

export interface IManifest {
  id?: number;
  manifestId?: string | null;
  awb?: string | null;
  awbAssignedDate?: dayjs.Dayjs | null;
  pickupException?: string | null;
  remarks?: string | null;
  pickupReferenceNumber?: string | null;
  status?: string | null;
  courier?: ICourierCompany | null;
  escalation?: IEscalation | null;
}

export class Manifest implements IManifest {
  constructor(
    public id?: number,
    public manifestId?: string | null,
    public awb?: string | null,
    public awbAssignedDate?: dayjs.Dayjs | null,
    public pickupException?: string | null,
    public remarks?: string | null,
    public pickupReferenceNumber?: string | null,
    public status?: string | null,
    public courier?: ICourierCompany | null,
    public escalation?: IEscalation | null
  ) {}
}

export function getManifestIdentifier(manifest: IManifest): number | undefined {
  return manifest.id;
}
