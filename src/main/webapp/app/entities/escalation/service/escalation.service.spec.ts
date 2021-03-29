import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEscalation, Escalation } from '../escalation.model';

import { EscalationService } from './escalation.service';

describe('Service Tests', () => {
  describe('Escalation Service', () => {
    let service: EscalationService;
    let httpMock: HttpTestingController;
    let elemDefault: IEscalation;
    let expectedResult: IEscalation | IEscalation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EscalationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remark: 'AAAAAAA',
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

      it('should create a Escalation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Escalation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Escalation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remark: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Escalation', () => {
        const patchObject = Object.assign(
          {
            remark: 'BBBBBB',
          },
          new Escalation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Escalation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remark: 'BBBBBB',
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

      it('should delete a Escalation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEscalationToCollectionIfMissing', () => {
        it('should add a Escalation to an empty array', () => {
          const escalation: IEscalation = { id: 123 };
          expectedResult = service.addEscalationToCollectionIfMissing([], escalation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(escalation);
        });

        it('should not add a Escalation to an array that contains it', () => {
          const escalation: IEscalation = { id: 123 };
          const escalationCollection: IEscalation[] = [
            {
              ...escalation,
            },
            { id: 456 },
          ];
          expectedResult = service.addEscalationToCollectionIfMissing(escalationCollection, escalation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Escalation to an array that doesn't contain it", () => {
          const escalation: IEscalation = { id: 123 };
          const escalationCollection: IEscalation[] = [{ id: 456 }];
          expectedResult = service.addEscalationToCollectionIfMissing(escalationCollection, escalation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(escalation);
        });

        it('should add only unique Escalation to an array', () => {
          const escalationArray: IEscalation[] = [{ id: 123 }, { id: 456 }, { id: 29095 }];
          const escalationCollection: IEscalation[] = [{ id: 123 }];
          expectedResult = service.addEscalationToCollectionIfMissing(escalationCollection, ...escalationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const escalation: IEscalation = { id: 123 };
          const escalation2: IEscalation = { id: 456 };
          expectedResult = service.addEscalationToCollectionIfMissing([], escalation, escalation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(escalation);
          expect(expectedResult).toContain(escalation2);
        });

        it('should accept null and undefined values', () => {
          const escalation: IEscalation = { id: 123 };
          expectedResult = service.addEscalationToCollectionIfMissing([], null, escalation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(escalation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
