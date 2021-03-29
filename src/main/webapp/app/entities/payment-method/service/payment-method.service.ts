import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaymentMethod, getPaymentMethodIdentifier } from '../payment-method.model';

export type EntityResponseType = HttpResponse<IPaymentMethod>;
export type EntityArrayResponseType = HttpResponse<IPaymentMethod[]>;

@Injectable({ providedIn: 'root' })
export class PaymentMethodService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-methods');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(paymentMethod: IPaymentMethod): Observable<EntityResponseType> {
    return this.http.post<IPaymentMethod>(this.resourceUrl, paymentMethod, { observe: 'response' });
  }

  update(paymentMethod: IPaymentMethod): Observable<EntityResponseType> {
    return this.http.put<IPaymentMethod>(`${this.resourceUrl}/${getPaymentMethodIdentifier(paymentMethod) as number}`, paymentMethod, {
      observe: 'response',
    });
  }

  partialUpdate(paymentMethod: IPaymentMethod): Observable<EntityResponseType> {
    return this.http.patch<IPaymentMethod>(`${this.resourceUrl}/${getPaymentMethodIdentifier(paymentMethod) as number}`, paymentMethod, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentMethod>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentMethod[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPaymentMethodToCollectionIfMissing(
    paymentMethodCollection: IPaymentMethod[],
    ...paymentMethodsToCheck: (IPaymentMethod | null | undefined)[]
  ): IPaymentMethod[] {
    const paymentMethods: IPaymentMethod[] = paymentMethodsToCheck.filter(isPresent);
    if (paymentMethods.length > 0) {
      const paymentMethodCollectionIdentifiers = paymentMethodCollection.map(
        paymentMethodItem => getPaymentMethodIdentifier(paymentMethodItem)!
      );
      const paymentMethodsToAdd = paymentMethods.filter(paymentMethodItem => {
        const paymentMethodIdentifier = getPaymentMethodIdentifier(paymentMethodItem);
        if (paymentMethodIdentifier == null || paymentMethodCollectionIdentifiers.includes(paymentMethodIdentifier)) {
          return false;
        }
        paymentMethodCollectionIdentifiers.push(paymentMethodIdentifier);
        return true;
      });
      return [...paymentMethodsToAdd, ...paymentMethodCollection];
    }
    return paymentMethodCollection;
  }
}
