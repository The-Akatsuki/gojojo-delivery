import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShipmentActivity, getShipmentActivityIdentifier } from '../shipment-activity.model';

export type EntityResponseType = HttpResponse<IShipmentActivity>;
export type EntityArrayResponseType = HttpResponse<IShipmentActivity[]>;

@Injectable({ providedIn: 'root' })
export class ShipmentActivityService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/shipment-activities');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(shipmentActivity: IShipmentActivity): Observable<EntityResponseType> {
    return this.http.post<IShipmentActivity>(this.resourceUrl, shipmentActivity, { observe: 'response' });
  }

  update(shipmentActivity: IShipmentActivity): Observable<EntityResponseType> {
    return this.http.put<IShipmentActivity>(
      `${this.resourceUrl}/${getShipmentActivityIdentifier(shipmentActivity) as number}`,
      shipmentActivity,
      { observe: 'response' }
    );
  }

  partialUpdate(shipmentActivity: IShipmentActivity): Observable<EntityResponseType> {
    return this.http.patch<IShipmentActivity>(
      `${this.resourceUrl}/${getShipmentActivityIdentifier(shipmentActivity) as number}`,
      shipmentActivity,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShipmentActivity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShipmentActivity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addShipmentActivityToCollectionIfMissing(
    shipmentActivityCollection: IShipmentActivity[],
    ...shipmentActivitiesToCheck: (IShipmentActivity | null | undefined)[]
  ): IShipmentActivity[] {
    const shipmentActivities: IShipmentActivity[] = shipmentActivitiesToCheck.filter(isPresent);
    if (shipmentActivities.length > 0) {
      const shipmentActivityCollectionIdentifiers = shipmentActivityCollection.map(
        shipmentActivityItem => getShipmentActivityIdentifier(shipmentActivityItem)!
      );
      const shipmentActivitiesToAdd = shipmentActivities.filter(shipmentActivityItem => {
        const shipmentActivityIdentifier = getShipmentActivityIdentifier(shipmentActivityItem);
        if (shipmentActivityIdentifier == null || shipmentActivityCollectionIdentifiers.includes(shipmentActivityIdentifier)) {
          return false;
        }
        shipmentActivityCollectionIdentifiers.push(shipmentActivityIdentifier);
        return true;
      });
      return [...shipmentActivitiesToAdd, ...shipmentActivityCollection];
    }
    return shipmentActivityCollection;
  }
}
