import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrder, Order } from '../order.model';
import { OrderService } from '../service/order.service';
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

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;

  buyerDetailsCollection: IOrderBuyerDetails[] = [];
  productsSharedCollection: IProduct[] = [];
  paymentMethodsSharedCollection: IPaymentMethod[] = [];
  pickupAddressesSharedCollection: IPickupAddress[] = [];
  walletsSharedCollection: IWallet[] = [];

  editForm = this.fb.group({
    id: [],
    orderNumber: [],
    referenceId: [],
    orderType: [],
    orderStatus: [],
    orderDate: [],
    channel: [],
    subtotal: [],
    hasShippingCharges: [],
    shipping: [],
    hasGiftwrapCharges: [],
    giftwrap: [],
    hasTransactionCharges: [],
    transaction: [],
    hasDiscount: [],
    discount: [],
    weight: [],
    weightCharges: [],
    excessWeightCharges: [],
    totalFreightCharges: [],
    length: [],
    width: [],
    height: [],
    resellerName: [],
    gstin: [],
    courier: [],
    awb: [],
    manifestId: [],
    buyerDetails: [],
    products: [],
    payment: [],
    pickupaddress: [],
    order: [],
  });

  constructor(
    protected orderService: OrderService,
    protected orderBuyerDetailsService: OrderBuyerDetailsService,
    protected productService: ProductService,
    protected paymentMethodService: PaymentMethodService,
    protected pickupAddressService: PickupAddressService,
    protected walletService: WalletService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      if (order.id === undefined) {
        const today = dayjs().startOf('day');
        order.orderDate = today;
      }

      this.updateForm(order);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.createFromForm();
    if (order.id !== undefined) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  trackOrderBuyerDetailsById(index: number, item: IOrderBuyerDetails): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  trackPaymentMethodById(index: number, item: IPaymentMethod): number {
    return item.id!;
  }

  trackPickupAddressById(index: number, item: IPickupAddress): number {
    return item.id!;
  }

  trackWalletById(index: number, item: IWallet): number {
    return item.id!;
  }

  getSelectedProduct(option: IProduct, selectedVals?: IProduct[]): IProduct {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(order: IOrder): void {
    this.editForm.patchValue({
      id: order.id,
      orderNumber: order.orderNumber,
      referenceId: order.referenceId,
      orderType: order.orderType,
      orderStatus: order.orderStatus,
      orderDate: order.orderDate ? order.orderDate.format(DATE_TIME_FORMAT) : null,
      channel: order.channel,
      subtotal: order.subtotal,
      hasShippingCharges: order.hasShippingCharges,
      shipping: order.shipping,
      hasGiftwrapCharges: order.hasGiftwrapCharges,
      giftwrap: order.giftwrap,
      hasTransactionCharges: order.hasTransactionCharges,
      transaction: order.transaction,
      hasDiscount: order.hasDiscount,
      discount: order.discount,
      weight: order.weight,
      weightCharges: order.weightCharges,
      excessWeightCharges: order.excessWeightCharges,
      totalFreightCharges: order.totalFreightCharges,
      length: order.length,
      width: order.width,
      height: order.height,
      resellerName: order.resellerName,
      gstin: order.gstin,
      courier: order.courier,
      awb: order.awb,
      manifestId: order.manifestId,
      buyerDetails: order.buyerDetails,
      products: order.products,
      payment: order.payment,
      pickupaddress: order.pickupaddress,
      order: order.order,
    });

    this.buyerDetailsCollection = this.orderBuyerDetailsService.addOrderBuyerDetailsToCollectionIfMissing(
      this.buyerDetailsCollection,
      order.buyerDetails
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      ...(order.products ?? [])
    );
    this.paymentMethodsSharedCollection = this.paymentMethodService.addPaymentMethodToCollectionIfMissing(
      this.paymentMethodsSharedCollection,
      order.payment
    );
    this.pickupAddressesSharedCollection = this.pickupAddressService.addPickupAddressToCollectionIfMissing(
      this.pickupAddressesSharedCollection,
      order.pickupaddress
    );
    this.walletsSharedCollection = this.walletService.addWalletToCollectionIfMissing(this.walletsSharedCollection, order.order);
  }

  protected loadRelationshipsOptions(): void {
    this.orderBuyerDetailsService
      .query({ filter: 'order-is-null' })
      .pipe(map((res: HttpResponse<IOrderBuyerDetails[]>) => res.body ?? []))
      .pipe(
        map((orderBuyerDetails: IOrderBuyerDetails[]) =>
          this.orderBuyerDetailsService.addOrderBuyerDetailsToCollectionIfMissing(
            orderBuyerDetails,
            this.editForm.get('buyerDetails')!.value
          )
        )
      )
      .subscribe((orderBuyerDetails: IOrderBuyerDetails[]) => (this.buyerDetailsCollection = orderBuyerDetails));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing(products, ...(this.editForm.get('products')!.value ?? []))
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.paymentMethodService
      .query()
      .pipe(map((res: HttpResponse<IPaymentMethod[]>) => res.body ?? []))
      .pipe(
        map((paymentMethods: IPaymentMethod[]) =>
          this.paymentMethodService.addPaymentMethodToCollectionIfMissing(paymentMethods, this.editForm.get('payment')!.value)
        )
      )
      .subscribe((paymentMethods: IPaymentMethod[]) => (this.paymentMethodsSharedCollection = paymentMethods));

    this.pickupAddressService
      .query()
      .pipe(map((res: HttpResponse<IPickupAddress[]>) => res.body ?? []))
      .pipe(
        map((pickupAddresses: IPickupAddress[]) =>
          this.pickupAddressService.addPickupAddressToCollectionIfMissing(pickupAddresses, this.editForm.get('pickupaddress')!.value)
        )
      )
      .subscribe((pickupAddresses: IPickupAddress[]) => (this.pickupAddressesSharedCollection = pickupAddresses));

    this.walletService
      .query()
      .pipe(map((res: HttpResponse<IWallet[]>) => res.body ?? []))
      .pipe(map((wallets: IWallet[]) => this.walletService.addWalletToCollectionIfMissing(wallets, this.editForm.get('order')!.value)))
      .subscribe((wallets: IWallet[]) => (this.walletsSharedCollection = wallets));
  }

  protected createFromForm(): IOrder {
    return {
      ...new Order(),
      id: this.editForm.get(['id'])!.value,
      orderNumber: this.editForm.get(['orderNumber'])!.value,
      referenceId: this.editForm.get(['referenceId'])!.value,
      orderType: this.editForm.get(['orderType'])!.value,
      orderStatus: this.editForm.get(['orderStatus'])!.value,
      orderDate: this.editForm.get(['orderDate'])!.value ? dayjs(this.editForm.get(['orderDate'])!.value, DATE_TIME_FORMAT) : undefined,
      channel: this.editForm.get(['channel'])!.value,
      subtotal: this.editForm.get(['subtotal'])!.value,
      hasShippingCharges: this.editForm.get(['hasShippingCharges'])!.value,
      shipping: this.editForm.get(['shipping'])!.value,
      hasGiftwrapCharges: this.editForm.get(['hasGiftwrapCharges'])!.value,
      giftwrap: this.editForm.get(['giftwrap'])!.value,
      hasTransactionCharges: this.editForm.get(['hasTransactionCharges'])!.value,
      transaction: this.editForm.get(['transaction'])!.value,
      hasDiscount: this.editForm.get(['hasDiscount'])!.value,
      discount: this.editForm.get(['discount'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      weightCharges: this.editForm.get(['weightCharges'])!.value,
      excessWeightCharges: this.editForm.get(['excessWeightCharges'])!.value,
      totalFreightCharges: this.editForm.get(['totalFreightCharges'])!.value,
      length: this.editForm.get(['length'])!.value,
      width: this.editForm.get(['width'])!.value,
      height: this.editForm.get(['height'])!.value,
      resellerName: this.editForm.get(['resellerName'])!.value,
      gstin: this.editForm.get(['gstin'])!.value,
      courier: this.editForm.get(['courier'])!.value,
      awb: this.editForm.get(['awb'])!.value,
      manifestId: this.editForm.get(['manifestId'])!.value,
      buyerDetails: this.editForm.get(['buyerDetails'])!.value,
      products: this.editForm.get(['products'])!.value,
      payment: this.editForm.get(['payment'])!.value,
      pickupaddress: this.editForm.get(['pickupaddress'])!.value,
      order: this.editForm.get(['order'])!.value,
    };
  }
}
