import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPinCodes, getPinCodesIdentifier } from '../pin-codes.model';

export type EntityResponseType = HttpResponse<IPinCodes>;
export type EntityArrayResponseType = HttpResponse<IPinCodes[]>;

@Injectable({ providedIn: 'root' })
export class PinCodesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pin-codes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(pinCodes: IPinCodes): Observable<EntityResponseType> {
    return this.http.post<IPinCodes>(this.resourceUrl, pinCodes, { observe: 'response' });
  }

  update(pinCodes: IPinCodes): Observable<EntityResponseType> {
    return this.http.put<IPinCodes>(`${this.resourceUrl}/${getPinCodesIdentifier(pinCodes) as number}`, pinCodes, { observe: 'response' });
  }

  partialUpdate(pinCodes: IPinCodes): Observable<EntityResponseType> {
    return this.http.patch<IPinCodes>(`${this.resourceUrl}/${getPinCodesIdentifier(pinCodes) as number}`, pinCodes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPinCodes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPinCodes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPinCodesToCollectionIfMissing(pinCodesCollection: IPinCodes[], ...pinCodesToCheck: (IPinCodes | null | undefined)[]): IPinCodes[] {
    const pinCodes: IPinCodes[] = pinCodesToCheck.filter(isPresent);
    if (pinCodes.length > 0) {
      const pinCodesCollectionIdentifiers = pinCodesCollection.map(pinCodesItem => getPinCodesIdentifier(pinCodesItem)!);
      const pinCodesToAdd = pinCodes.filter(pinCodesItem => {
        const pinCodesIdentifier = getPinCodesIdentifier(pinCodesItem);
        if (pinCodesIdentifier == null || pinCodesCollectionIdentifiers.includes(pinCodesIdentifier)) {
          return false;
        }
        pinCodesCollectionIdentifiers.push(pinCodesIdentifier);
        return true;
      });
      return [...pinCodesToAdd, ...pinCodesCollection];
    }
    return pinCodesCollection;
  }
}
