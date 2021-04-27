jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WalletService } from '../service/wallet.service';
import { IWallet, Wallet } from '../wallet.model';

import { WalletUpdateComponent } from './wallet-update.component';

describe('Component Tests', () => {
  describe('Wallet Management Update Component', () => {
    let comp: WalletUpdateComponent;
    let fixture: ComponentFixture<WalletUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let walletService: WalletService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WalletUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WalletUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WalletUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      walletService = TestBed.inject(WalletService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const wallet: IWallet = { id: 456 };

        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(wallet));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wallet = { id: 123 };
        spyOn(walletService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wallet }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(walletService.update).toHaveBeenCalledWith(wallet);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wallet = new Wallet();
        spyOn(walletService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wallet }));
        saveSubject.complete();

        // THEN
        expect(walletService.create).toHaveBeenCalledWith(wallet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wallet = { id: 123 };
        spyOn(walletService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wallet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(walletService.update).toHaveBeenCalledWith(wallet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
