import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IShipmentActivity, ShipmentActivity } from '../shipment-activity.model';

import { ShipmentActivityService } from './shipment-activity.service';

describe('Service Tests', () => {
  describe('ShipmentActivity Service', () => {
    let service: ShipmentActivityService;
    let httpMock: HttpTestingController;
    let elemDefault: IShipmentActivity;
    let expectedResult: IShipmentActivity | IShipmentActivity[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ShipmentActivityService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        status: 'AAAAAAA',
        pincode: 'AAAAAAA',
        location: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ShipmentActivity', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ShipmentActivity()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ShipmentActivity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            pincode: 'BBBBBB',
            location: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ShipmentActivity', () => {
        const patchObject = Object.assign(
          {
            location: 'BBBBBB',
          },
          new ShipmentActivity()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ShipmentActivity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            pincode: 'BBBBBB',
            location: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ShipmentActivity', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addShipmentActivityToCollectionIfMissing', () => {
        it('should add a ShipmentActivity to an empty array', () => {
          const shipmentActivity: IShipmentActivity = { id: 123 };
          expectedResult = service.addShipmentActivityToCollectionIfMissing([], shipmentActivity);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(shipmentActivity);
        });

        it('should not add a ShipmentActivity to an array that contains it', () => {
          const shipmentActivity: IShipmentActivity = { id: 123 };
          const shipmentActivityCollection: IShipmentActivity[] = [
            {
              ...shipmentActivity,
            },
            { id: 456 },
          ];
          expectedResult = service.addShipmentActivityToCollectionIfMissing(shipmentActivityCollection, shipmentActivity);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ShipmentActivity to an array that doesn't contain it", () => {
          const shipmentActivity: IShipmentActivity = { id: 123 };
          const shipmentActivityCollection: IShipmentActivity[] = [{ id: 456 }];
          expectedResult = service.addShipmentActivityToCollectionIfMissing(shipmentActivityCollection, shipmentActivity);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(shipmentActivity);
        });

        it('should add only unique ShipmentActivity to an array', () => {
          const shipmentActivityArray: IShipmentActivity[] = [{ id: 123 }, { id: 456 }, { id: 53562 }];
          const shipmentActivityCollection: IShipmentActivity[] = [{ id: 123 }];
          expectedResult = service.addShipmentActivityToCollectionIfMissing(shipmentActivityCollection, ...shipmentActivityArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const shipmentActivity: IShipmentActivity = { id: 123 };
          const shipmentActivity2: IShipmentActivity = { id: 456 };
          expectedResult = service.addShipmentActivityToCollectionIfMissing([], shipmentActivity, shipmentActivity2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(shipmentActivity);
          expect(expectedResult).toContain(shipmentActivity2);
        });

        it('should accept null and undefined values', () => {
          const shipmentActivity: IShipmentActivity = { id: 123 };
          expectedResult = service.addShipmentActivityToCollectionIfMissing([], null, shipmentActivity, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(shipmentActivity);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
