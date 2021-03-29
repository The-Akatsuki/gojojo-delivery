import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEscalation, getEscalationIdentifier } from '../escalation.model';

export type EntityResponseType = HttpResponse<IEscalation>;
export type EntityArrayResponseType = HttpResponse<IEscalation[]>;

@Injectable({ providedIn: 'root' })
export class EscalationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/escalations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(escalation: IEscalation): Observable<EntityResponseType> {
    return this.http.post<IEscalation>(this.resourceUrl, escalation, { observe: 'response' });
  }

  update(escalation: IEscalation): Observable<EntityResponseType> {
    return this.http.put<IEscalation>(`${this.resourceUrl}/${getEscalationIdentifier(escalation) as number}`, escalation, {
      observe: 'response',
    });
  }

  partialUpdate(escalation: IEscalation): Observable<EntityResponseType> {
    return this.http.patch<IEscalation>(`${this.resourceUrl}/${getEscalationIdentifier(escalation) as number}`, escalation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEscalation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEscalation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEscalationToCollectionIfMissing(
    escalationCollection: IEscalation[],
    ...escalationsToCheck: (IEscalation | null | undefined)[]
  ): IEscalation[] {
    const escalations: IEscalation[] = escalationsToCheck.filter(isPresent);
    if (escalations.length > 0) {
      const escalationCollectionIdentifiers = escalationCollection.map(escalationItem => getEscalationIdentifier(escalationItem)!);
      const escalationsToAdd = escalations.filter(escalationItem => {
        const escalationIdentifier = getEscalationIdentifier(escalationItem);
        if (escalationIdentifier == null || escalationCollectionIdentifiers.includes(escalationIdentifier)) {
          return false;
        }
        escalationCollectionIdentifiers.push(escalationIdentifier);
        return true;
      });
      return [...escalationsToAdd, ...escalationCollection];
    }
    return escalationCollection;
  }
}
