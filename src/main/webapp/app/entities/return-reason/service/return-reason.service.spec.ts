import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReturnReason, ReturnReason } from '../return-reason.model';

import { ReturnReasonService } from './return-reason.service';

describe('Service Tests', () => {
  describe('ReturnReason Service', () => {
    let service: ReturnReasonService;
    let httpMock: HttpTestingController;
    let elemDefault: IReturnReason;
    let expectedResult: IReturnReason | IReturnReason[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ReturnReasonService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        comment: 'AAAAAAA',
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

      it('should create a ReturnReason', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ReturnReason()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ReturnReason', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            comment: 'BBBBBB',
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

      it('should partial update a ReturnReason', () => {
        const patchObject = Object.assign({}, new ReturnReason());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ReturnReason', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            comment: 'BBBBBB',
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

      it('should delete a ReturnReason', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addReturnReasonToCollectionIfMissing', () => {
        it('should add a ReturnReason to an empty array', () => {
          const returnReason: IReturnReason = { id: 123 };
          expectedResult = service.addReturnReasonToCollectionIfMissing([], returnReason);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(returnReason);
        });

        it('should not add a ReturnReason to an array that contains it', () => {
          const returnReason: IReturnReason = { id: 123 };
          const returnReasonCollection: IReturnReason[] = [
            {
              ...returnReason,
            },
            { id: 456 },
          ];
          expectedResult = service.addReturnReasonToCollectionIfMissing(returnReasonCollection, returnReason);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ReturnReason to an array that doesn't contain it", () => {
          const returnReason: IReturnReason = { id: 123 };
          const returnReasonCollection: IReturnReason[] = [{ id: 456 }];
          expectedResult = service.addReturnReasonToCollectionIfMissing(returnReasonCollection, returnReason);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(returnReason);
        });

        it('should add only unique ReturnReason to an array', () => {
          const returnReasonArray: IReturnReason[] = [{ id: 123 }, { id: 456 }, { id: 60639 }];
          const returnReasonCollection: IReturnReason[] = [{ id: 123 }];
          expectedResult = service.addReturnReasonToCollectionIfMissing(returnReasonCollection, ...returnReasonArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const returnReason: IReturnReason = { id: 123 };
          const returnReason2: IReturnReason = { id: 456 };
          expectedResult = service.addReturnReasonToCollectionIfMissing([], returnReason, returnReason2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(returnReason);
          expect(expectedResult).toContain(returnReason2);
        });

        it('should accept null and undefined values', () => {
          const returnReason: IReturnReason = { id: 123 };
          expectedResult = service.addReturnReasonToCollectionIfMissing([], null, returnReason, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(returnReason);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
