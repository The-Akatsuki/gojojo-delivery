import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReturnLabel, ReturnLabel } from '../return-label.model';

import { ReturnLabelService } from './return-label.service';

describe('Service Tests', () => {
  describe('ReturnLabel Service', () => {
    let service: ReturnLabelService;
    let httpMock: HttpTestingController;
    let elemDefault: IReturnLabel;
    let expectedResult: IReturnLabel | IReturnLabel[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ReturnLabelService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
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

      it('should create a ReturnLabel', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ReturnLabel()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ReturnLabel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ReturnLabel', () => {
        const patchObject = Object.assign({}, new ReturnLabel());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ReturnLabel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
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

      it('should delete a ReturnLabel', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addReturnLabelToCollectionIfMissing', () => {
        it('should add a ReturnLabel to an empty array', () => {
          const returnLabel: IReturnLabel = { id: 123 };
          expectedResult = service.addReturnLabelToCollectionIfMissing([], returnLabel);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(returnLabel);
        });

        it('should not add a ReturnLabel to an array that contains it', () => {
          const returnLabel: IReturnLabel = { id: 123 };
          const returnLabelCollection: IReturnLabel[] = [
            {
              ...returnLabel,
            },
            { id: 456 },
          ];
          expectedResult = service.addReturnLabelToCollectionIfMissing(returnLabelCollection, returnLabel);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ReturnLabel to an array that doesn't contain it", () => {
          const returnLabel: IReturnLabel = { id: 123 };
          const returnLabelCollection: IReturnLabel[] = [{ id: 456 }];
          expectedResult = service.addReturnLabelToCollectionIfMissing(returnLabelCollection, returnLabel);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(returnLabel);
        });

        it('should add only unique ReturnLabel to an array', () => {
          const returnLabelArray: IReturnLabel[] = [{ id: 123 }, { id: 456 }, { id: 98116 }];
          const returnLabelCollection: IReturnLabel[] = [{ id: 123 }];
          expectedResult = service.addReturnLabelToCollectionIfMissing(returnLabelCollection, ...returnLabelArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const returnLabel: IReturnLabel = { id: 123 };
          const returnLabel2: IReturnLabel = { id: 456 };
          expectedResult = service.addReturnLabelToCollectionIfMissing([], returnLabel, returnLabel2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(returnLabel);
          expect(expectedResult).toContain(returnLabel2);
        });

        it('should accept null and undefined values', () => {
          const returnLabel: IReturnLabel = { id: 123 };
          expectedResult = service.addReturnLabelToCollectionIfMissing([], null, returnLabel, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(returnLabel);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
