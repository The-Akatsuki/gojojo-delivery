import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IManifest, getManifestIdentifier } from '../manifest.model';

export type EntityResponseType = HttpResponse<IManifest>;
export type EntityArrayResponseType = HttpResponse<IManifest[]>;

@Injectable({ providedIn: 'root' })
export class ManifestService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/manifests');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(manifest: IManifest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manifest);
    return this.http
      .post<IManifest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(manifest: IManifest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manifest);
    return this.http
      .put<IManifest>(`${this.resourceUrl}/${getManifestIdentifier(manifest) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(manifest: IManifest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manifest);
    return this.http
      .patch<IManifest>(`${this.resourceUrl}/${getManifestIdentifier(manifest) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IManifest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IManifest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addManifestToCollectionIfMissing(manifestCollection: IManifest[], ...manifestsToCheck: (IManifest | null | undefined)[]): IManifest[] {
    const manifests: IManifest[] = manifestsToCheck.filter(isPresent);
    if (manifests.length > 0) {
      const manifestCollectionIdentifiers = manifestCollection.map(manifestItem => getManifestIdentifier(manifestItem)!);
      const manifestsToAdd = manifests.filter(manifestItem => {
        const manifestIdentifier = getManifestIdentifier(manifestItem);
        if (manifestIdentifier == null || manifestCollectionIdentifiers.includes(manifestIdentifier)) {
          return false;
        }
        manifestCollectionIdentifiers.push(manifestIdentifier);
        return true;
      });
      return [...manifestsToAdd, ...manifestCollection];
    }
    return manifestCollection;
  }

  protected convertDateFromClient(manifest: IManifest): IManifest {
    return Object.assign({}, manifest, {
      awbAssignedDate: manifest.awbAssignedDate?.isValid() ? manifest.awbAssignedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.awbAssignedDate = res.body.awbAssignedDate ? dayjs(res.body.awbAssignedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((manifest: IManifest) => {
        manifest.awbAssignedDate = manifest.awbAssignedDate ? dayjs(manifest.awbAssignedDate) : undefined;
      });
    }
    return res;
  }
}
