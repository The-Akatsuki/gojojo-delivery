import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReturnReason, getReturnReasonIdentifier } from '../return-reason.model';

export type EntityResponseType = HttpResponse<IReturnReason>;
export type EntityArrayResponseType = HttpResponse<IReturnReason[]>;

@Injectable({ providedIn: 'root' })
export class ReturnReasonService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/return-reasons');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(returnReason: IReturnReason): Observable<EntityResponseType> {
    return this.http.post<IReturnReason>(this.resourceUrl, returnReason, { observe: 'response' });
  }

  update(returnReason: IReturnReason): Observable<EntityResponseType> {
    return this.http.put<IReturnReason>(`${this.resourceUrl}/${getReturnReasonIdentifier(returnReason) as number}`, returnReason, {
      observe: 'response',
    });
  }

  partialUpdate(returnReason: IReturnReason): Observable<EntityResponseType> {
    return this.http.patch<IReturnReason>(`${this.resourceUrl}/${getReturnReasonIdentifier(returnReason) as number}`, returnReason, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReturnReason>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReturnReason[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReturnReasonToCollectionIfMissing(
    returnReasonCollection: IReturnReason[],
    ...returnReasonsToCheck: (IReturnReason | null | undefined)[]
  ): IReturnReason[] {
    const returnReasons: IReturnReason[] = returnReasonsToCheck.filter(isPresent);
    if (returnReasons.length > 0) {
      const returnReasonCollectionIdentifiers = returnReasonCollection.map(
        returnReasonItem => getReturnReasonIdentifier(returnReasonItem)!
      );
      const returnReasonsToAdd = returnReasons.filter(returnReasonItem => {
        const returnReasonIdentifier = getReturnReasonIdentifier(returnReasonItem);
        if (returnReasonIdentifier == null || returnReasonCollectionIdentifiers.includes(returnReasonIdentifier)) {
          return false;
        }
        returnReasonCollectionIdentifiers.push(returnReasonIdentifier);
        return true;
      });
      return [...returnReasonsToAdd, ...returnReasonCollection];
    }
    return returnReasonCollection;
  }
}
