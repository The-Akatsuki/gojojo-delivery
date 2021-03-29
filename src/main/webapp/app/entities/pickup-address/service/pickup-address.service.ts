import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPickupAddress, getPickupAddressIdentifier } from '../pickup-address.model';

export type EntityResponseType = HttpResponse<IPickupAddress>;
export type EntityArrayResponseType = HttpResponse<IPickupAddress[]>;

@Injectable({ providedIn: 'root' })
export class PickupAddressService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pickup-addresses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(pickupAddress: IPickupAddress): Observable<EntityResponseType> {
    return this.http.post<IPickupAddress>(this.resourceUrl, pickupAddress, { observe: 'response' });
  }

  update(pickupAddress: IPickupAddress): Observable<EntityResponseType> {
    return this.http.put<IPickupAddress>(`${this.resourceUrl}/${getPickupAddressIdentifier(pickupAddress) as number}`, pickupAddress, {
      observe: 'response',
    });
  }

  partialUpdate(pickupAddress: IPickupAddress): Observable<EntityResponseType> {
    return this.http.patch<IPickupAddress>(`${this.resourceUrl}/${getPickupAddressIdentifier(pickupAddress) as number}`, pickupAddress, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPickupAddress>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPickupAddress[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPickupAddressToCollectionIfMissing(
    pickupAddressCollection: IPickupAddress[],
    ...pickupAddressesToCheck: (IPickupAddress | null | undefined)[]
  ): IPickupAddress[] {
    const pickupAddresses: IPickupAddress[] = pickupAddressesToCheck.filter(isPresent);
    if (pickupAddresses.length > 0) {
      const pickupAddressCollectionIdentifiers = pickupAddressCollection.map(
        pickupAddressItem => getPickupAddressIdentifier(pickupAddressItem)!
      );
      const pickupAddressesToAdd = pickupAddresses.filter(pickupAddressItem => {
        const pickupAddressIdentifier = getPickupAddressIdentifier(pickupAddressItem);
        if (pickupAddressIdentifier == null || pickupAddressCollectionIdentifiers.includes(pickupAddressIdentifier)) {
          return false;
        }
        pickupAddressCollectionIdentifiers.push(pickupAddressIdentifier);
        return true;
      });
      return [...pickupAddressesToAdd, ...pickupAddressCollection];
    }
    return pickupAddressCollection;
  }
}
