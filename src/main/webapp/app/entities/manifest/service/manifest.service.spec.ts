import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IManifest, Manifest } from '../manifest.model';

import { ManifestService } from './manifest.service';

describe('Service Tests', () => {
  describe('Manifest Service', () => {
    let service: ManifestService;
    let httpMock: HttpTestingController;
    let elemDefault: IManifest;
    let expectedResult: IManifest | IManifest[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ManifestService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        manifestId: 'AAAAAAA',
        awb: 'AAAAAAA',
        awbAssignedDate: currentDate,
        pickupException: 'AAAAAAA',
        remarks: 'AAAAAAA',
        pickupReferenceNumber: 'AAAAAAA',
        status: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            awbAssignedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Manifest', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            awbAssignedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            awbAssignedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Manifest()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Manifest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            manifestId: 'BBBBBB',
            awb: 'BBBBBB',
            awbAssignedDate: currentDate.format(DATE_TIME_FORMAT),
            pickupException: 'BBBBBB',
            remarks: 'BBBBBB',
            pickupReferenceNumber: 'BBBBBB',
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            awbAssignedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Manifest', () => {
        const patchObject = Object.assign(
          {
            manifestId: 'BBBBBB',
            awb: 'BBBBBB',
            awbAssignedDate: currentDate.format(DATE_TIME_FORMAT),
            pickupException: 'BBBBBB',
            remarks: 'BBBBBB',
          },
          new Manifest()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            awbAssignedDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Manifest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            manifestId: 'BBBBBB',
            awb: 'BBBBBB',
            awbAssignedDate: currentDate.format(DATE_TIME_FORMAT),
            pickupException: 'BBBBBB',
            remarks: 'BBBBBB',
            pickupReferenceNumber: 'BBBBBB',
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            awbAssignedDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Manifest', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addManifestToCollectionIfMissing', () => {
        it('should add a Manifest to an empty array', () => {
          const manifest: IManifest = { id: 123 };
          expectedResult = service.addManifestToCollectionIfMissing([], manifest);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(manifest);
        });

        it('should not add a Manifest to an array that contains it', () => {
          const manifest: IManifest = { id: 123 };
          const manifestCollection: IManifest[] = [
            {
              ...manifest,
            },
            { id: 456 },
          ];
          expectedResult = service.addManifestToCollectionIfMissing(manifestCollection, manifest);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Manifest to an array that doesn't contain it", () => {
          const manifest: IManifest = { id: 123 };
          const manifestCollection: IManifest[] = [{ id: 456 }];
          expectedResult = service.addManifestToCollectionIfMissing(manifestCollection, manifest);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(manifest);
        });

        it('should add only unique Manifest to an array', () => {
          const manifestArray: IManifest[] = [{ id: 123 }, { id: 456 }, { id: 3781 }];
          const manifestCollection: IManifest[] = [{ id: 123 }];
          expectedResult = service.addManifestToCollectionIfMissing(manifestCollection, ...manifestArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const manifest: IManifest = { id: 123 };
          const manifest2: IManifest = { id: 456 };
          expectedResult = service.addManifestToCollectionIfMissing([], manifest, manifest2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(manifest);
          expect(expectedResult).toContain(manifest2);
        });

        it('should accept null and undefined values', () => {
          const manifest: IManifest = { id: 123 };
          expectedResult = service.addManifestToCollectionIfMissing([], null, manifest, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(manifest);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
