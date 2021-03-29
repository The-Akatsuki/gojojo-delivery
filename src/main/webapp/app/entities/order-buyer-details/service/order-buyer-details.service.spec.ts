import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrderBuyerDetails, OrderBuyerDetails } from '../order-buyer-details.model';

import { OrderBuyerDetailsService } from './order-buyer-details.service';

describe('Service Tests', () => {
  describe('OrderBuyerDetails Service', () => {
    let service: OrderBuyerDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrderBuyerDetails;
    let expectedResult: IOrderBuyerDetails | IOrderBuyerDetails[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrderBuyerDetailsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        mobile: 'AAAAAAA',
        alternateMobile: 'AAAAAAA',
        email: 'AAAAAAA',
        addressline1: 'AAAAAAA',
        addressline2: 'AAAAAAA',
        pincode: 'AAAAAAA',
        city: 'AAAAAAA',
        state: 'AAAAAAA',
        country: 'AAAAAAA',
        companyName: 'AAAAAAA',
        isBillingSame: false,
        billAddressline1: 'AAAAAAA',
        billAddressline2: 'AAAAAAA',
        billPincode: 'AAAAAAA',
        billCity: 'AAAAAAA',
        billState: 'AAAAAAA',
        billCountry: 'AAAAAAA',
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

      it('should create a OrderBuyerDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OrderBuyerDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrderBuyerDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            mobile: 'BBBBBB',
            alternateMobile: 'BBBBBB',
            email: 'BBBBBB',
            addressline1: 'BBBBBB',
            addressline2: 'BBBBBB',
            pincode: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            country: 'BBBBBB',
            companyName: 'BBBBBB',
            isBillingSame: true,
            billAddressline1: 'BBBBBB',
            billAddressline2: 'BBBBBB',
            billPincode: 'BBBBBB',
            billCity: 'BBBBBB',
            billState: 'BBBBBB',
            billCountry: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OrderBuyerDetails', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            mobile: 'BBBBBB',
            alternateMobile: 'BBBBBB',
            addressline1: 'BBBBBB',
            pincode: 'BBBBBB',
            city: 'BBBBBB',
            country: 'BBBBBB',
            billCountry: 'BBBBBB',
          },
          new OrderBuyerDetails()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OrderBuyerDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            mobile: 'BBBBBB',
            alternateMobile: 'BBBBBB',
            email: 'BBBBBB',
            addressline1: 'BBBBBB',
            addressline2: 'BBBBBB',
            pincode: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            country: 'BBBBBB',
            companyName: 'BBBBBB',
            isBillingSame: true,
            billAddressline1: 'BBBBBB',
            billAddressline2: 'BBBBBB',
            billPincode: 'BBBBBB',
            billCity: 'BBBBBB',
            billState: 'BBBBBB',
            billCountry: 'BBBBBB',
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

      it('should delete a OrderBuyerDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrderBuyerDetailsToCollectionIfMissing', () => {
        it('should add a OrderBuyerDetails to an empty array', () => {
          const orderBuyerDetails: IOrderBuyerDetails = { id: 123 };
          expectedResult = service.addOrderBuyerDetailsToCollectionIfMissing([], orderBuyerDetails);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(orderBuyerDetails);
        });

        it('should not add a OrderBuyerDetails to an array that contains it', () => {
          const orderBuyerDetails: IOrderBuyerDetails = { id: 123 };
          const orderBuyerDetailsCollection: IOrderBuyerDetails[] = [
            {
              ...orderBuyerDetails,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrderBuyerDetailsToCollectionIfMissing(orderBuyerDetailsCollection, orderBuyerDetails);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrderBuyerDetails to an array that doesn't contain it", () => {
          const orderBuyerDetails: IOrderBuyerDetails = { id: 123 };
          const orderBuyerDetailsCollection: IOrderBuyerDetails[] = [{ id: 456 }];
          expectedResult = service.addOrderBuyerDetailsToCollectionIfMissing(orderBuyerDetailsCollection, orderBuyerDetails);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(orderBuyerDetails);
        });

        it('should add only unique OrderBuyerDetails to an array', () => {
          const orderBuyerDetailsArray: IOrderBuyerDetails[] = [{ id: 123 }, { id: 456 }, { id: 70262 }];
          const orderBuyerDetailsCollection: IOrderBuyerDetails[] = [{ id: 123 }];
          expectedResult = service.addOrderBuyerDetailsToCollectionIfMissing(orderBuyerDetailsCollection, ...orderBuyerDetailsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const orderBuyerDetails: IOrderBuyerDetails = { id: 123 };
          const orderBuyerDetails2: IOrderBuyerDetails = { id: 456 };
          expectedResult = service.addOrderBuyerDetailsToCollectionIfMissing([], orderBuyerDetails, orderBuyerDetails2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(orderBuyerDetails);
          expect(expectedResult).toContain(orderBuyerDetails2);
        });

        it('should accept null and undefined values', () => {
          const orderBuyerDetails: IOrderBuyerDetails = { id: 123 };
          expectedResult = service.addOrderBuyerDetailsToCollectionIfMissing([], null, orderBuyerDetails, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(orderBuyerDetails);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
