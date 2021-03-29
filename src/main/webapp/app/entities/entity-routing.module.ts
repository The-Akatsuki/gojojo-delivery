import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'order',
        data: { pageTitle: 'gojojoDeliveryApp.order.home.title' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'gojojoDeliveryApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'payment-method',
        data: { pageTitle: 'gojojoDeliveryApp.paymentMethod.home.title' },
        loadChildren: () => import('./payment-method/payment-method.module').then(m => m.PaymentMethodModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'gojojoDeliveryApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'order-buyer-details',
        data: { pageTitle: 'gojojoDeliveryApp.orderBuyerDetails.home.title' },
        loadChildren: () => import('./order-buyer-details/order-buyer-details.module').then(m => m.OrderBuyerDetailsModule),
      },
      {
        path: 'pin-codes',
        data: { pageTitle: 'gojojoDeliveryApp.pinCodes.home.title' },
        loadChildren: () => import('./pin-codes/pin-codes.module').then(m => m.PinCodesModule),
      },
      {
        path: 'pickup-address',
        data: { pageTitle: 'gojojoDeliveryApp.pickupAddress.home.title' },
        loadChildren: () => import('./pickup-address/pickup-address.module').then(m => m.PickupAddressModule),
      },
      {
        path: 'manifest',
        data: { pageTitle: 'gojojoDeliveryApp.manifest.home.title' },
        loadChildren: () => import('./manifest/manifest.module').then(m => m.ManifestModule),
      },
      {
        path: 'escalation',
        data: { pageTitle: 'gojojoDeliveryApp.escalation.home.title' },
        loadChildren: () => import('./escalation/escalation.module').then(m => m.EscalationModule),
      },
      {
        path: 'courier-company',
        data: { pageTitle: 'gojojoDeliveryApp.courierCompany.home.title' },
        loadChildren: () => import('./courier-company/courier-company.module').then(m => m.CourierCompanyModule),
      },
      {
        path: 'reason-escalation',
        data: { pageTitle: 'gojojoDeliveryApp.reasonEscalation.home.title' },
        loadChildren: () => import('./reason-escalation/reason-escalation.module').then(m => m.ReasonEscalationModule),
      },
      {
        path: 'return-reason',
        data: { pageTitle: 'gojojoDeliveryApp.returnReason.home.title' },
        loadChildren: () => import('./return-reason/return-reason.module').then(m => m.ReturnReasonModule),
      },
      {
        path: 'return-label',
        data: { pageTitle: 'gojojoDeliveryApp.returnLabel.home.title' },
        loadChildren: () => import('./return-label/return-label.module').then(m => m.ReturnLabelModule),
      },
      {
        path: 'shipment-activity',
        data: { pageTitle: 'gojojoDeliveryApp.shipmentActivity.home.title' },
        loadChildren: () => import('./shipment-activity/shipment-activity.module').then(m => m.ShipmentActivityModule),
      },
      {
        path: 'wallet',
        data: { pageTitle: 'gojojoDeliveryApp.wallet.home.title' },
        loadChildren: () => import('./wallet/wallet.module').then(m => m.WalletModule),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'gojojoDeliveryApp.transaction.home.title' },
        loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
