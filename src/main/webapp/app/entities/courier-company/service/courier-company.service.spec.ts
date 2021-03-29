import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICourierCompany, CourierCompany } from '../courier-company.model';

import { CourierCompanyService } from './courier-company.service';

describe('Service Tests', () => {
  describe('CourierCompany Service', () => {
    let service: CourierCompanyService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourierCompany;
    let expectedResult: ICourierCompany | ICourierCompany[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourierCompanyService);
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
        image: 'AAAAAAA',
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

      it('should create a CourierCompany', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CourierCompany()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CourierCompany', () => {
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
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CourierCompany', () => {
        const patchObject = Object.assign(
          {
            alternateMobile: 'BBBBBB',
            email: 'BBBBBB',
            pincode: 'BBBBBB',
            city: 'BBBBBB',
          },
          new CourierCompany()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CourierCompany', () => {
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
            image: 'BBBBBB',
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

      it('should delete a CourierCompany', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourierCompanyToCollectionIfMissing', () => {
        it('should add a CourierCompany to an empty array', () => {
          const courierCompany: ICourierCompany = { id: 123 };
          expectedResult = service.addCourierCompanyToCollectionIfMissing([], courierCompany);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courierCompany);
        });

        it('should not add a CourierCompany to an array that contains it', () => {
          const courierCompany: ICourierCompany = { id: 123 };
          const courierCompanyCollection: ICourierCompany[] = [
            {
              ...courierCompany,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourierCompanyToCollectionIfMissing(courierCompanyCollection, courierCompany);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CourierCompany to an array that doesn't contain it", () => {
          const courierCompany: ICourierCompany = { id: 123 };
          const courierCompanyCollection: ICourierCompany[] = [{ id: 456 }];
          expectedResult = service.addCourierCompanyToCollectionIfMissing(courierCompanyCollection, courierCompany);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courierCompany);
        });

        it('should add only unique CourierCompany to an array', () => {
          const courierCompanyArray: ICourierCompany[] = [{ id: 123 }, { id: 456 }, { id: 14268 }];
          const courierCompanyCollection: ICourierCompany[] = [{ id: 123 }];
          expectedResult = service.addCourierCompanyToCollectionIfMissing(courierCompanyCollection, ...courierCompanyArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const courierCompany: ICourierCompany = { id: 123 };
          const courierCompany2: ICourierCompany = { id: 456 };
          expectedResult = service.addCourierCompanyToCollectionIfMissing([], courierCompany, courierCompany2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courierCompany);
          expect(expectedResult).toContain(courierCompany2);
        });

        it('should accept null and undefined values', () => {
          const courierCompany: ICourierCompany = { id: 123 };
          expectedResult = service.addCourierCompanyToCollectionIfMissing([], null, courierCompany, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courierCompany);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
