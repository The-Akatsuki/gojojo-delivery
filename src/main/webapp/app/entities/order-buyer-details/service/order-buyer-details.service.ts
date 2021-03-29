import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderBuyerDetails, getOrderBuyerDetailsIdentifier } from '../order-buyer-details.model';

export type EntityResponseType = HttpResponse<IOrderBuyerDetails>;
export type EntityArrayResponseType = HttpResponse<IOrderBuyerDetails[]>;

@Injectable({ providedIn: 'root' })
export class OrderBuyerDetailsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/order-buyer-details');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(orderBuyerDetails: IOrderBuyerDetails): Observable<EntityResponseType> {
    return this.http.post<IOrderBuyerDetails>(this.resourceUrl, orderBuyerDetails, { observe: 'response' });
  }

  update(orderBuyerDetails: IOrderBuyerDetails): Observable<EntityResponseType> {
    return this.http.put<IOrderBuyerDetails>(
      `${this.resourceUrl}/${getOrderBuyerDetailsIdentifier(orderBuyerDetails) as number}`,
      orderBuyerDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(orderBuyerDetails: IOrderBuyerDetails): Observable<EntityResponseType> {
    return this.http.patch<IOrderBuyerDetails>(
      `${this.resourceUrl}/${getOrderBuyerDetailsIdentifier(orderBuyerDetails) as number}`,
      orderBuyerDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrderBuyerDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrderBuyerDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrderBuyerDetailsToCollectionIfMissing(
    orderBuyerDetailsCollection: IOrderBuyerDetails[],
    ...orderBuyerDetailsToCheck: (IOrderBuyerDetails | null | undefined)[]
  ): IOrderBuyerDetails[] {
    const orderBuyerDetails: IOrderBuyerDetails[] = orderBuyerDetailsToCheck.filter(isPresent);
    if (orderBuyerDetails.length > 0) {
      const orderBuyerDetailsCollectionIdentifiers = orderBuyerDetailsCollection.map(
        orderBuyerDetailsItem => getOrderBuyerDetailsIdentifier(orderBuyerDetailsItem)!
      );
      const orderBuyerDetailsToAdd = orderBuyerDetails.filter(orderBuyerDetailsItem => {
        const orderBuyerDetailsIdentifier = getOrderBuyerDetailsIdentifier(orderBuyerDetailsItem);
        if (orderBuyerDetailsIdentifier == null || orderBuyerDetailsCollectionIdentifiers.includes(orderBuyerDetailsIdentifier)) {
          return false;
        }
        orderBuyerDetailsCollectionIdentifiers.push(orderBuyerDetailsIdentifier);
        return true;
      });
      return [...orderBuyerDetailsToAdd, ...orderBuyerDetailsCollection];
    }
    return orderBuyerDetailsCollection;
  }
}
