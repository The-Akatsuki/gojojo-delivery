export interface IPinCodes {
  id?: number;
  pin?: string | null;
  city?: string | null;
  state?: string | null;
  country?: string | null;
}

export class PinCodes implements IPinCodes {
  constructor(
    public id?: number,
    public pin?: string | null,
    public city?: string | null,
    public state?: string | null,
    public country?: string | null
  ) {}
}

export function getPinCodesIdentifier(pinCodes: IPinCodes): number | undefined {
  return pinCodes.id;
}
