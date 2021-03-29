import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPickupAddress } from '../pickup-address.model';
import { PickupAddressService } from '../service/pickup-address.service';
import { PickupAddressDeleteDialogComponent } from '../delete/pickup-address-delete-dialog.component';

@Component({
  selector: 'jhi-pickup-address',
  templateUrl: './pickup-address.component.html',
})
export class PickupAddressComponent implements OnInit {
  pickupAddresses?: IPickupAddress[];
  isLoading = false;

  constructor(protected pickupAddressService: PickupAddressService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pickupAddressService.query().subscribe(
      (res: HttpResponse<IPickupAddress[]>) => {
        this.isLoading = false;
        this.pickupAddresses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPickupAddress): number {
    return item.id!;
  }

  delete(pickupAddress: IPickupAddress): void {
    const modalRef = this.modalService.open(PickupAddressDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pickupAddress = pickupAddress;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
