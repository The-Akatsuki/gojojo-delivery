import { IManifest } from 'app/entities/manifest/manifest.model';

export interface ICourierCompany {
  id?: number;
  name?: string | null;
  mobile?: string | null;
  alternateMobile?: string | null;
  email?: string | null;
  addressline1?: string | null;
  addressline2?: string | null;
  pincode?: string | null;
  city?: string | null;
  state?: string | null;
  country?: string | null;
  image?: string | null;
  manifests?: IManifest[] | null;
}

export class CourierCompany implements ICourierCompany {
  constructor(
    public id?: number,
    public name?: string | null,
    public mobile?: string | null,
    public alternateMobile?: string | null,
    public email?: string | null,
    public addressline1?: string | null,
    public addressline2?: string | null,
    public pincode?: string | null,
    public city?: string | null,
    public state?: string | null,
    public country?: string | null,
    public image?: string | null,
    public manifests?: IManifest[] | null
  ) {}
}

export function getCourierCompanyIdentifier(courierCompany: ICourierCompany): number | undefined {
  return courierCompany.id;
}
