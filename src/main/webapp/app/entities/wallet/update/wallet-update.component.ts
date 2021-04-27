import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWallet, Wallet } from '../wallet.model';
import { WalletService } from '../service/wallet.service';

@Component({
  selector: 'jhi-wallet-update',
  templateUrl: './wallet-update.component.html',
})
export class WalletUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    usableBalance: [],
    totalBalance: [],
    balanceOnHold: [],
  });

  constructor(protected walletService: WalletService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wallet }) => {
      this.updateForm(wallet);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wallet = this.createFromForm();
    if (wallet.id !== undefined) {
      this.subscribeToSaveResponse(this.walletService.update(wallet));
    } else {
      this.subscribeToSaveResponse(this.walletService.create(wallet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWallet>>): void {
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

  protected updateForm(wallet: IWallet): void {
    this.editForm.patchValue({
      id: wallet.id,
      usableBalance: wallet.usableBalance,
      totalBalance: wallet.totalBalance,
      balanceOnHold: wallet.balanceOnHold,
    });
  }

  protected createFromForm(): IWallet {
    return {
      ...new Wallet(),
      id: this.editForm.get(['id'])!.value,
      usableBalance: this.editForm.get(['usableBalance'])!.value,
      totalBalance: this.editForm.get(['totalBalance'])!.value,
      balanceOnHold: this.editForm.get(['balanceOnHold'])!.value,
    };
  }
}
