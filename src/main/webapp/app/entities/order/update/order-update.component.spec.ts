jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrderService } from '../service/order.service';
import { IOrder, Order } from '../order.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/service/payment-method.service';
import { IPickupAddress } from 'app/entities/pickup-address/pickup-address.model';
import { PickupAddressService } from 'app/entities/pickup-address/service/pickup-address.service';

import { OrderUpdateComponent } from './order-update.component';

describe('Component Tests', () => {
  describe('Order Management Update Component', () => {
    let comp: OrderUpdateComponent;
    let fixture: ComponentFixture<OrderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orderService: OrderService;
    let productService: ProductService;
    let paymentMethodService: PaymentMethodService;
    let pickupAddressService: PickupAddressService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      orderService = TestBed.inject(OrderService);
      productService = TestBed.inject(ProductService);
      paymentMethodService = TestBed.inject(PaymentMethodService);
      pickupAddressService = TestBed.inject(PickupAddressService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Product query and add missing value', () => {
        const order: IOrder = { id: 456 };
        const product: IProduct = { id: 96360 };
        order.product = product;

        const productCollection: IProduct[] = [{ id: 61547 }];
        spyOn(productService, 'query').and.returnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [product];
        const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
        spyOn(productService, 'addProductToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ order });
        comp.ngOnInit();

        expect(productService.query).toHaveBeenCalled();
        expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
        expect(comp.productsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PaymentMethod query and add missing value', () => {
        const order: IOrder = { id: 456 };
        const payment: IPaymentMethod = { id: 80077 };
        order.payment = payment;

        const paymentMethodCollection: IPaymentMethod[] = [{ id: 29939 }];
        spyOn(paymentMethodService, 'query').and.returnValue(of(new HttpResponse({ body: paymentMethodCollection })));
        const additionalPaymentMethods = [payment];
        const expectedCollection: IPaymentMethod[] = [...additionalPaymentMethods, ...paymentMethodCollection];
        spyOn(paymentMethodService, 'addPaymentMethodToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ order });
        comp.ngOnInit();

        expect(paymentMethodService.query).toHaveBeenCalled();
        expect(paymentMethodService.addPaymentMethodToCollectionIfMissing).toHaveBeenCalledWith(
          paymentMethodCollection,
          ...additionalPaymentMethods
        );
        expect(comp.paymentMethodsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PickupAddress query and add missing value', () => {
        const order: IOrder = { id: 456 };
        const pickupaddress: IPickupAddress = { id: 17935 };
        order.pickupaddress = pickupaddress;

        const pickupAddressCollection: IPickupAddress[] = [{ id: 87953 }];
        spyOn(pickupAddressService, 'query').and.returnValue(of(new HttpResponse({ body: pickupAddressCollection })));
        const additionalPickupAddresses = [pickupaddress];
        const expectedCollection: IPickupAddress[] = [...additionalPickupAddresses, ...pickupAddressCollection];
        spyOn(pickupAddressService, 'addPickupAddressToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ order });
        comp.ngOnInit();

        expect(pickupAddressService.query).toHaveBeenCalled();
        expect(pickupAddressService.addPickupAddressToCollectionIfMissing).toHaveBeenCalledWith(
          pickupAddressCollection,
          ...additionalPickupAddresses
        );
        expect(comp.pickupAddressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const order: IOrder = { id: 456 };
        const product: IProduct = { id: 61371 };
        order.product = product;
        const payment: IPaymentMethod = { id: 36148 };
        order.payment = payment;
        const pickupaddress: IPickupAddress = { id: 67206 };
        order.pickupaddress = pickupaddress;

        activatedRoute.data = of({ order });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(order));
        expect(comp.productsSharedCollection).toContain(product);
        expect(comp.paymentMethodsSharedCollection).toContain(payment);
        expect(comp.pickupAddressesSharedCollection).toContain(pickupaddress);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const order = { id: 123 };
        spyOn(orderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ order });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: order }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(orderService.update).toHaveBeenCalledWith(order);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const order = new Order();
        spyOn(orderService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ order });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: order }));
        saveSubject.complete();

        // THEN
        expect(orderService.create).toHaveBeenCalledWith(order);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const order = { id: 123 };
        spyOn(orderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ order });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(orderService.update).toHaveBeenCalledWith(order);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProductById', () => {
        it('Should return tracked Product primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProductById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPaymentMethodById', () => {
        it('Should return tracked PaymentMethod primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaymentMethodById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPickupAddressById', () => {
        it('Should return tracked PickupAddress primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPickupAddressById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
