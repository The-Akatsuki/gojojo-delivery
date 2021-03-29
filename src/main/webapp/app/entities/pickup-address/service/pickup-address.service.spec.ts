import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPickupAddress, PickupAddress } from '../pickup-address.model';

import { PickupAddressService } from './pickup-address.service';

describe('Service Tests', () => {
  describe('PickupAddress Service', () => {
    let service: PickupAddressService;
    let httpMock: HttpTestingController;
    let elemDefault: IPickupAddress;
    let expectedResult: IPickupAddress | IPickupAddress[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PickupAddressService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nickName: 'AAAAAAA',
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
        isMobileVerified: false,
        otp: 'AAAAAAA',
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

      it('should create a PickupAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PickupAddress()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PickupAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nickName: 'BBBBBB',
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
            isMobileVerified: true,
            otp: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PickupAddress', () => {
        const patchObject = Object.assign(
          {
            mobile: 'BBBBBB',
            alternateMobile: 'BBBBBB',
            email: 'BBBBBB',
            state: 'BBBBBB',
            country: 'BBBBBB',
            companyName: 'BBBBBB',
            otp: 'BBBBBB',
          },
          new PickupAddress()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PickupAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nickName: 'BBBBBB',
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
            isMobileVerified: true,
            otp: 'BBBBBB',
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

      it('should delete a PickupAddress', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPickupAddressToCollectionIfMissing', () => {
        it('should add a PickupAddress to an empty array', () => {
          const pickupAddress: IPickupAddress = { id: 123 };
          expectedResult = service.addPickupAddressToCollectionIfMissing([], pickupAddress);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pickupAddress);
        });

        it('should not add a PickupAddress to an array that contains it', () => {
          const pickupAddress: IPickupAddress = { id: 123 };
          const pickupAddressCollection: IPickupAddress[] = [
            {
              ...pickupAddress,
            },
            { id: 456 },
          ];
          expectedResult = service.addPickupAddressToCollectionIfMissing(pickupAddressCollection, pickupAddress);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PickupAddress to an array that doesn't contain it", () => {
          const pickupAddress: IPickupAddress = { id: 123 };
          const pickupAddressCollection: IPickupAddress[] = [{ id: 456 }];
          expectedResult = service.addPickupAddressToCollectionIfMissing(pickupAddressCollection, pickupAddress);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pickupAddress);
        });

        it('should add only unique PickupAddress to an array', () => {
          const pickupAddressArray: IPickupAddress[] = [{ id: 123 }, { id: 456 }, { id: 44956 }];
          const pickupAddressCollection: IPickupAddress[] = [{ id: 123 }];
          expectedResult = service.addPickupAddressToCollectionIfMissing(pickupAddressCollection, ...pickupAddressArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const pickupAddress: IPickupAddress = { id: 123 };
          const pickupAddress2: IPickupAddress = { id: 456 };
          expectedResult = service.addPickupAddressToCollectionIfMissing([], pickupAddress, pickupAddress2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pickupAddress);
          expect(expectedResult).toContain(pickupAddress2);
        });

        it('should accept null and undefined values', () => {
          const pickupAddress: IPickupAddress = { id: 123 };
          expectedResult = service.addPickupAddressToCollectionIfMissing([], null, pickupAddress, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pickupAddress);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
