import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPaymentMethod, PaymentMethod } from '../payment-method.model';

import { PaymentMethodService } from './payment-method.service';

describe('Service Tests', () => {
  describe('PaymentMethod Service', () => {
    let service: PaymentMethodService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentMethod;
    let expectedResult: IPaymentMethod | IPaymentMethod[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PaymentMethodService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        isActive: false,
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

      it('should create a PaymentMethod', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PaymentMethod()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentMethod', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            isActive: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PaymentMethod', () => {
        const patchObject = Object.assign({}, new PaymentMethod());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PaymentMethod', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            isActive: true,
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

      it('should delete a PaymentMethod', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPaymentMethodToCollectionIfMissing', () => {
        it('should add a PaymentMethod to an empty array', () => {
          const paymentMethod: IPaymentMethod = { id: 123 };
          expectedResult = service.addPaymentMethodToCollectionIfMissing([], paymentMethod);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paymentMethod);
        });

        it('should not add a PaymentMethod to an array that contains it', () => {
          const paymentMethod: IPaymentMethod = { id: 123 };
          const paymentMethodCollection: IPaymentMethod[] = [
            {
              ...paymentMethod,
            },
            { id: 456 },
          ];
          expectedResult = service.addPaymentMethodToCollectionIfMissing(paymentMethodCollection, paymentMethod);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PaymentMethod to an array that doesn't contain it", () => {
          const paymentMethod: IPaymentMethod = { id: 123 };
          const paymentMethodCollection: IPaymentMethod[] = [{ id: 456 }];
          expectedResult = service.addPaymentMethodToCollectionIfMissing(paymentMethodCollection, paymentMethod);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paymentMethod);
        });

        it('should add only unique PaymentMethod to an array', () => {
          const paymentMethodArray: IPaymentMethod[] = [{ id: 123 }, { id: 456 }, { id: 35922 }];
          const paymentMethodCollection: IPaymentMethod[] = [{ id: 123 }];
          expectedResult = service.addPaymentMethodToCollectionIfMissing(paymentMethodCollection, ...paymentMethodArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const paymentMethod: IPaymentMethod = { id: 123 };
          const paymentMethod2: IPaymentMethod = { id: 456 };
          expectedResult = service.addPaymentMethodToCollectionIfMissing([], paymentMethod, paymentMethod2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paymentMethod);
          expect(expectedResult).toContain(paymentMethod2);
        });

        it('should accept null and undefined values', () => {
          const paymentMethod: IPaymentMethod = { id: 123 };
          expectedResult = service.addPaymentMethodToCollectionIfMissing([], null, paymentMethod, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paymentMethod);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
