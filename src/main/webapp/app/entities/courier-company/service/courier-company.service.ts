import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourierCompany, getCourierCompanyIdentifier } from '../courier-company.model';

export type EntityResponseType = HttpResponse<ICourierCompany>;
export type EntityArrayResponseType = HttpResponse<ICourierCompany[]>;

@Injectable({ providedIn: 'root' })
export class CourierCompanyService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/courier-companies');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courierCompany: ICourierCompany): Observable<EntityResponseType> {
    return this.http.post<ICourierCompany>(this.resourceUrl, courierCompany, { observe: 'response' });
  }

  update(courierCompany: ICourierCompany): Observable<EntityResponseType> {
    return this.http.put<ICourierCompany>(`${this.resourceUrl}/${getCourierCompanyIdentifier(courierCompany) as number}`, courierCompany, {
      observe: 'response',
    });
  }

  partialUpdate(courierCompany: ICourierCompany): Observable<EntityResponseType> {
    return this.http.patch<ICourierCompany>(
      `${this.resourceUrl}/${getCourierCompanyIdentifier(courierCompany) as number}`,
      courierCompany,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourierCompany>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourierCompany[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourierCompanyToCollectionIfMissing(
    courierCompanyCollection: ICourierCompany[],
    ...courierCompaniesToCheck: (ICourierCompany | null | undefined)[]
  ): ICourierCompany[] {
    const courierCompanies: ICourierCompany[] = courierCompaniesToCheck.filter(isPresent);
    if (courierCompanies.length > 0) {
      const courierCompanyCollectionIdentifiers = courierCompanyCollection.map(
        courierCompanyItem => getCourierCompanyIdentifier(courierCompanyItem)!
      );
      const courierCompaniesToAdd = courierCompanies.filter(courierCompanyItem => {
        const courierCompanyIdentifier = getCourierCompanyIdentifier(courierCompanyItem);
        if (courierCompanyIdentifier == null || courierCompanyCollectionIdentifiers.includes(courierCompanyIdentifier)) {
          return false;
        }
        courierCompanyCollectionIdentifiers.push(courierCompanyIdentifier);
        return true;
      });
      return [...courierCompaniesToAdd, ...courierCompanyCollection];
    }
    return courierCompanyCollection;
  }
}
