import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReasonEscalation, ReasonEscalation } from '../reason-escalation.model';

import { ReasonEscalationService } from './reason-escalation.service';

describe('Service Tests', () => {
  describe('ReasonEscalation Service', () => {
    let service: ReasonEscalationService;
    let httpMock: HttpTestingController;
    let elemDefault: IReasonEscalation;
    let expectedResult: IReasonEscalation | IReasonEscalation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ReasonEscalationService);
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

      it('should create a ReasonEscalation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ReasonEscalation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ReasonEscalation', () => {
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

      it('should partial update a ReasonEscalation', () => {
        const patchObject = Object.assign({}, new ReasonEscalation());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ReasonEscalation', () => {
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

      it('should delete a ReasonEscalation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addReasonEscalationToCollectionIfMissing', () => {
        it('should add a ReasonEscalation to an empty array', () => {
          const reasonEscalation: IReasonEscalation = { id: 123 };
          expectedResult = service.addReasonEscalationToCollectionIfMissing([], reasonEscalation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(reasonEscalation);
        });

        it('should not add a ReasonEscalation to an array that contains it', () => {
          const reasonEscalation: IReasonEscalation = { id: 123 };
          const reasonEscalationCollection: IReasonEscalation[] = [
            {
              ...reasonEscalation,
            },
            { id: 456 },
          ];
          expectedResult = service.addReasonEscalationToCollectionIfMissing(reasonEscalationCollection, reasonEscalation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ReasonEscalation to an array that doesn't contain it", () => {
          const reasonEscalation: IReasonEscalation = { id: 123 };
          const reasonEscalationCollection: IReasonEscalation[] = [{ id: 456 }];
          expectedResult = service.addReasonEscalationToCollectionIfMissing(reasonEscalationCollection, reasonEscalation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(reasonEscalation);
        });

        it('should add only unique ReasonEscalation to an array', () => {
          const reasonEscalationArray: IReasonEscalation[] = [{ id: 123 }, { id: 456 }, { id: 26026 }];
          const reasonEscalationCollection: IReasonEscalation[] = [{ id: 123 }];
          expectedResult = service.addReasonEscalationToCollectionIfMissing(reasonEscalationCollection, ...reasonEscalationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const reasonEscalation: IReasonEscalation = { id: 123 };
          const reasonEscalation2: IReasonEscalation = { id: 456 };
          expectedResult = service.addReasonEscalationToCollectionIfMissing([], reasonEscalation, reasonEscalation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(reasonEscalation);
          expect(expectedResult).toContain(reasonEscalation2);
        });

        it('should accept null and undefined values', () => {
          const reasonEscalation: IReasonEscalation = { id: 123 };
          expectedResult = service.addReasonEscalationToCollectionIfMissing([], null, reasonEscalation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(reasonEscalation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
