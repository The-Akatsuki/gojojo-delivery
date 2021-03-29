import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { OrderType } from 'app/entities/enumerations/order-type.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { IOrder, Order } from '../order.model';

import { OrderService } from './order.service';

describe('Service Tests', () => {
  describe('Order Service', () => {
    let service: OrderService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrder;
    let expectedResult: IOrder | IOrder[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrderService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        orderId: 'AAAAAAA',
        referenceId: 'AAAAAAA',
        orderType: OrderType.ForwardOrder,
        orderStatus: OrderStatus.Processing,
        orderDate: currentDate,
        channel: 'AAAAAAA',
        subtotal: 0,
        hasShippingCharges: false,
        shipping: 0,
        hasGiftwrapCharges: false,
        giftwrap: 0,
        hasTransactionCharges: false,
        transaction: 0,
        hasDiscount: false,
        discount: 0,
        weight: 0,
        weightCharges: 0,
        excessWeightCharges: 0,
        totalFreightCharges: 0,
        length: 0,
        width: 0,
        height: 0,
        resellerName: 'AAAAAAA',
        gstin: 'AAAAAAA',
        courier: 'AAAAAAA',
        awb: 'AAAAAAA',
        manifestId: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            orderDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Order', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            orderDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            orderDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Order()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Order', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            orderId: 'BBBBBB',
            referenceId: 'BBBBBB',
            orderType: 'BBBBBB',
            orderStatus: 'BBBBBB',
            orderDate: currentDate.format(DATE_TIME_FORMAT),
            channel: 'BBBBBB',
            subtotal: 1,
            hasShippingCharges: true,
            shipping: 1,
            hasGiftwrapCharges: true,
            giftwrap: 1,
            hasTransactionCharges: true,
            transaction: 1,
            hasDiscount: true,
            discount: 1,
            weight: 1,
            weightCharges: 1,
            excessWeightCharges: 1,
            totalFreightCharges: 1,
            length: 1,
            width: 1,
            height: 1,
            resellerName: 'BBBBBB',
            gstin: 'BBBBBB',
            courier: 'BBBBBB',
            awb: 'BBBBBB',
            manifestId: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            orderDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Order', () => {
        const patchObject = Object.assign(
          {
            orderType: 'BBBBBB',
            orderStatus: 'BBBBBB',
            orderDate: currentDate.format(DATE_TIME_FORMAT),
            channel: 'BBBBBB',
            subtotal: 1,
            hasShippingCharges: true,
            hasGiftwrapCharges: true,
            giftwrap: 1,
            hasTransactionCharges: true,
            transaction: 1,
            hasDiscount: true,
            weight: 1,
            excessWeightCharges: 1,
            length: 1,
            width: 1,
            resellerName: 'BBBBBB',
            manifestId: 'BBBBBB',
          },
          new Order()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            orderDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Order', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            orderId: 'BBBBBB',
            referenceId: 'BBBBBB',
            orderType: 'BBBBBB',
            orderStatus: 'BBBBBB',
            orderDate: currentDate.format(DATE_TIME_FORMAT),
            channel: 'BBBBBB',
            subtotal: 1,
            hasShippingCharges: true,
            shipping: 1,
            hasGiftwrapCharges: true,
            giftwrap: 1,
            hasTransactionCharges: true,
            transaction: 1,
            hasDiscount: true,
            discount: 1,
            weight: 1,
            weightCharges: 1,
            excessWeightCharges: 1,
            totalFreightCharges: 1,
            length: 1,
            width: 1,
            height: 1,
            resellerName: 'BBBBBB',
            gstin: 'BBBBBB',
            courier: 'BBBBBB',
            awb: 'BBBBBB',
            manifestId: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            orderDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Order', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrderToCollectionIfMissing', () => {
        it('should add a Order to an empty array', () => {
          const order: IOrder = { id: 123 };
          expectedResult = service.addOrderToCollectionIfMissing([], order);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(order);
        });

        it('should not add a Order to an array that contains it', () => {
          const order: IOrder = { id: 123 };
          const orderCollection: IOrder[] = [
            {
              ...order,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrderToCollectionIfMissing(orderCollection, order);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Order to an array that doesn't contain it", () => {
          const order: IOrder = { id: 123 };
          const orderCollection: IOrder[] = [{ id: 456 }];
          expectedResult = service.addOrderToCollectionIfMissing(orderCollection, order);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(order);
        });

        it('should add only unique Order to an array', () => {
          const orderArray: IOrder[] = [{ id: 123 }, { id: 456 }, { id: 49948 }];
          const orderCollection: IOrder[] = [{ id: 123 }];
          expectedResult = service.addOrderToCollectionIfMissing(orderCollection, ...orderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const order: IOrder = { id: 123 };
          const order2: IOrder = { id: 456 };
          expectedResult = service.addOrderToCollectionIfMissing([], order, order2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(order);
          expect(expectedResult).toContain(order2);
        });

        it('should accept null and undefined values', () => {
          const order: IOrder = { id: 123 };
          expectedResult = service.addOrderToCollectionIfMissing([], null, order, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(order);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
