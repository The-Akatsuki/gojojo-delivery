jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrderService } from '../service/order.service';
import { IOrder, Order } from '../order.model';
import { IOrderBuyerDetails } from 'app/entities/order-buyer-details/order-buyer-details.model';
import { OrderBuyerDetailsService } from 'app/entities/order-buyer-details/service/order-buyer-details.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/service/payment-method.service';
import { IPickupAddress } from 'app/entities/pickup-address/pickup-address.model';
import { PickupAddressService } from 'app/entities/pickup-address/service/pickup-address.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';

import { OrderUpdateComponent } from './order-update.component';

describe('Component Tests', () => {
  describe('Order Management Update Component', () => {
    let comp: OrderUpdateComponent;
    let fixture: ComponentFixture<OrderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orderService: OrderService;
    let orderBuyerDetailsService: OrderBuyerDetailsService;
    let productService: ProductService;
    let paymentMethodService: PaymentMethodService;
    let pickupAddressService: PickupAddressService;
    let walletService: WalletService;

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
      orderBuyerDetailsService = TestBed.inject(OrderBuyerDetailsService);
      productService = TestBed.inject(ProductService);
      paymentMethodService = TestBed.inject(PaymentMethodService);
      pickupAddressService = TestBed.inject(PickupAddressService);
      walletService = TestBed.inject(WalletService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call buyerDetails query and add missing value', () => {
        const order: IOrder = { id: 456 };
        const buyerDetails: IOrderBuyerDetails = { id: 4806 };
        order.buyerDetails = buyerDetails;

        const buyerDetailsCollection: IOrderBuyerDetails[] = [{ id: 34143 }];
        spyOn(orderBuyerDetailsService, 'query').and.returnValue(of(new HttpResponse({ body: buyerDetailsCollection })));
        const expectedCollection: IOrderBuyerDetails[] = [buyerDetails, ...buyerDetailsCollection];
        spyOn(orderBuyerDetailsService, 'addOrderBuyerDetailsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ order });
        comp.ngOnInit();

        expect(orderBuyerDetailsService.query).toHaveBeenCalled();
        expect(orderBuyerDetailsService.addOrderBuyerDetailsToCollectionIfMissing).toHaveBeenCalledWith(
          buyerDetailsCollection,
          buyerDetails
        );
        expect(comp.buyerDetailsCollection).toEqual(expectedCollection);
      });

      it('Should call Product query and add missing value', () => {
        const order: IOrder = { id: 456 };
        const products: IProduct[] = [{ id: 17748 }];
        order.products = products;

        const productCollection: IProduct[] = [{ id: 91872 }];
        spyOn(productService, 'query').and.returnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [...products];
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
        const payment: IPaymentMethod = { id: 21239 };
        order.payment = payment;

        const paymentMethodCollection: IPaymentMethod[] = [{ id: 95552 }];
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
        const pickupaddress: IPickupAddress = { id: 2772 };
        order.pickupaddress = pickupaddress;

        const pickupAddressCollection: IPickupAddress[] = [{ id: 58130 }];
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

      it('Should call Wallet query and add missing value', () => {
        const order: IOrder = { id: 456 };
        const order: IWallet = { id: 59936 };
        order.order = order;

        const walletCollection: IWallet[] = [{ id: 55472 }];
        spyOn(walletService, 'query').and.returnValue(of(new HttpResponse({ body: walletCollection })));
        const additionalWallets = [order];
        const expectedCollection: IWallet[] = [...additionalWallets, ...walletCollection];
        spyOn(walletService, 'addWalletToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ order });
        comp.ngOnInit();

        expect(walletService.query).toHaveBeenCalled();
        expect(walletService.addWalletToCollectionIfMissing).toHaveBeenCalledWith(walletCollection, ...additionalWallets);
        expect(comp.walletsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const order: IOrder = { id: 456 };
        const buyerDetails: IOrderBuyerDetails = { id: 44256 };
        order.buyerDetails = buyerDetails;
        const products: IProduct = { id: 31243 };
        order.products = [products];
        const payment: IPaymentMethod = { id: 95613 };
        order.payment = payment;
        const pickupaddress: IPickupAddress = { id: 24517 };
        order.pickupaddress = pickupaddress;
        const order: IWallet = { id: 75059 };
        order.order = order;

        activatedRoute.data = of({ order });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(order));
        expect(comp.buyerDetailsCollection).toContain(buyerDetails);
        expect(comp.productsSharedCollection).toContain(products);
        expect(comp.paymentMethodsSharedCollection).toContain(payment);
        expect(comp.pickupAddressesSharedCollection).toContain(pickupaddress);
        expect(comp.walletsSharedCollection).toContain(order);
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
      describe('trackOrderBuyerDetailsById', () => {
        it('Should return tracked OrderBuyerDetails primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrderBuyerDetailsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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

      describe('trackWalletById', () => {
        it('Should return tracked Wallet primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWalletById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedProduct', () => {
        it('Should return option if no Product is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedProduct(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Product for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedProduct(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Product is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedProduct(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
