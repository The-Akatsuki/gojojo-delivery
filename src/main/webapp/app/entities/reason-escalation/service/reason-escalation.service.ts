import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReasonEscalation, getReasonEscalationIdentifier } from '../reason-escalation.model';

export type EntityResponseType = HttpResponse<IReasonEscalation>;
export type EntityArrayResponseType = HttpResponse<IReasonEscalation[]>;

@Injectable({ providedIn: 'root' })
export class ReasonEscalationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/reason-escalations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(reasonEscalation: IReasonEscalation): Observable<EntityResponseType> {
    return this.http.post<IReasonEscalation>(this.resourceUrl, reasonEscalation, { observe: 'response' });
  }

  update(reasonEscalation: IReasonEscalation): Observable<EntityResponseType> {
    return this.http.put<IReasonEscalation>(
      `${this.resourceUrl}/${getReasonEscalationIdentifier(reasonEscalation) as number}`,
      reasonEscalation,
      { observe: 'response' }
    );
  }

  partialUpdate(reasonEscalation: IReasonEscalation): Observable<EntityResponseType> {
    return this.http.patch<IReasonEscalation>(
      `${this.resourceUrl}/${getReasonEscalationIdentifier(reasonEscalation) as number}`,
      reasonEscalation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReasonEscalation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReasonEscalation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReasonEscalationToCollectionIfMissing(
    reasonEscalationCollection: IReasonEscalation[],
    ...reasonEscalationsToCheck: (IReasonEscalation | null | undefined)[]
  ): IReasonEscalation[] {
    const reasonEscalations: IReasonEscalation[] = reasonEscalationsToCheck.filter(isPresent);
    if (reasonEscalations.length > 0) {
      const reasonEscalationCollectionIdentifiers = reasonEscalationCollection.map(
        reasonEscalationItem => getReasonEscalationIdentifier(reasonEscalationItem)!
      );
      const reasonEscalationsToAdd = reasonEscalations.filter(reasonEscalationItem => {
        const reasonEscalationIdentifier = getReasonEscalationIdentifier(reasonEscalationItem);
        if (reasonEscalationIdentifier == null || reasonEscalationCollectionIdentifiers.includes(reasonEscalationIdentifier)) {
          return false;
        }
        reasonEscalationCollectionIdentifiers.push(reasonEscalationIdentifier);
        return true;
      });
      return [...reasonEscalationsToAdd, ...reasonEscalationCollection];
    }
    return reasonEscalationCollection;
  }
}
