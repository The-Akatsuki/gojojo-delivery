import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReturnLabel, getReturnLabelIdentifier } from '../return-label.model';

export type EntityResponseType = HttpResponse<IReturnLabel>;
export type EntityArrayResponseType = HttpResponse<IReturnLabel[]>;

@Injectable({ providedIn: 'root' })
export class ReturnLabelService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/return-labels');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(returnLabel: IReturnLabel): Observable<EntityResponseType> {
    return this.http.post<IReturnLabel>(this.resourceUrl, returnLabel, { observe: 'response' });
  }

  update(returnLabel: IReturnLabel): Observable<EntityResponseType> {
    return this.http.put<IReturnLabel>(`${this.resourceUrl}/${getReturnLabelIdentifier(returnLabel) as number}`, returnLabel, {
      observe: 'response',
    });
  }

  partialUpdate(returnLabel: IReturnLabel): Observable<EntityResponseType> {
    return this.http.patch<IReturnLabel>(`${this.resourceUrl}/${getReturnLabelIdentifier(returnLabel) as number}`, returnLabel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReturnLabel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReturnLabel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReturnLabelToCollectionIfMissing(
    returnLabelCollection: IReturnLabel[],
    ...returnLabelsToCheck: (IReturnLabel | null | undefined)[]
  ): IReturnLabel[] {
    const returnLabels: IReturnLabel[] = returnLabelsToCheck.filter(isPresent);
    if (returnLabels.length > 0) {
      const returnLabelCollectionIdentifiers = returnLabelCollection.map(returnLabelItem => getReturnLabelIdentifier(returnLabelItem)!);
      const returnLabelsToAdd = returnLabels.filter(returnLabelItem => {
        const returnLabelIdentifier = getReturnLabelIdentifier(returnLabelItem);
        if (returnLabelIdentifier == null || returnLabelCollectionIdentifiers.includes(returnLabelIdentifier)) {
          return false;
        }
        returnLabelCollectionIdentifiers.push(returnLabelIdentifier);
        return true;
      });
      return [...returnLabelsToAdd, ...returnLabelCollection];
    }
    return returnLabelCollection;
  }
}
