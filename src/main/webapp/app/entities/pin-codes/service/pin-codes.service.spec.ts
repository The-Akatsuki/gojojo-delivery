import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPinCodes, PinCodes } from '../pin-codes.model';

import { PinCodesService } from './pin-codes.service';

describe('Service Tests', () => {
  describe('PinCodes Service', () => {
    let service: PinCodesService;
    let httpMock: HttpTestingController;
    let elemDefault: IPinCodes;
    let expectedResult: IPinCodes | IPinCodes[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PinCodesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        pin: 'AAAAAAA',
        city: 'AAAAAAA',
        state: 'AAAAAAA',
        country: 'AAAAAAA',
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

      it('should create a PinCodes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PinCodes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PinCodes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pin: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            country: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PinCodes', () => {
        const patchObject = Object.assign(
          {
            city: 'BBBBBB',
            state: 'BBBBBB',
            country: 'BBBBBB',
          },
          new PinCodes()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PinCodes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pin: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            country: 'BBBBBB',
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

      it('should delete a PinCodes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPinCodesToCollectionIfMissing', () => {
        it('should add a PinCodes to an empty array', () => {
          const pinCodes: IPinCodes = { id: 123 };
          expectedResult = service.addPinCodesToCollectionIfMissing([], pinCodes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pinCodes);
        });

        it('should not add a PinCodes to an array that contains it', () => {
          const pinCodes: IPinCodes = { id: 123 };
          const pinCodesCollection: IPinCodes[] = [
            {
              ...pinCodes,
            },
            { id: 456 },
          ];
          expectedResult = service.addPinCodesToCollectionIfMissing(pinCodesCollection, pinCodes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PinCodes to an array that doesn't contain it", () => {
          const pinCodes: IPinCodes = { id: 123 };
          const pinCodesCollection: IPinCodes[] = [{ id: 456 }];
          expectedResult = service.addPinCodesToCollectionIfMissing(pinCodesCollection, pinCodes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pinCodes);
        });

        it('should add only unique PinCodes to an array', () => {
          const pinCodesArray: IPinCodes[] = [{ id: 123 }, { id: 456 }, { id: 38699 }];
          const pinCodesCollection: IPinCodes[] = [{ id: 123 }];
          expectedResult = service.addPinCodesToCollectionIfMissing(pinCodesCollection, ...pinCodesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const pinCodes: IPinCodes = { id: 123 };
          const pinCodes2: IPinCodes = { id: 456 };
          expectedResult = service.addPinCodesToCollectionIfMissing([], pinCodes, pinCodes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pinCodes);
          expect(expectedResult).toContain(pinCodes2);
        });

        it('should accept null and undefined values', () => {
          const pinCodes: IPinCodes = { id: 123 };
          expectedResult = service.addPinCodesToCollectionIfMissing([], null, pinCodes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pinCodes);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
