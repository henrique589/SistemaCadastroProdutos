import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router, convertToParamMap, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';

import { HttpErrorResponse } from '@angular/common/http';
import { Produto } from '../produto.model';
import { ProdutoService } from '../produto.service';
import { ProdutoFormComponent } from './produto-form.component';

describe('ProdutoFormComponent', () => {
  let fixture: ComponentFixture<ProdutoFormComponent>;
  let component: ProdutoFormComponent;
  let service: jasmine.SpyObj<ProdutoService>;
  let router: Router;

  function configurar(idNaRota: string | null): void {
    service = jasmine.createSpyObj<ProdutoService>('ProdutoService', ['buscar', 'criar', 'atualizar']);
    TestBed.configureTestingModule({
      imports: [ProdutoFormComponent],
      providers: [
        provideRouter([]),
        { provide: ProdutoService, useValue: service },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: convertToParamMap(idNaRota ? { id: idNaRota } : {}) } },
        },
      ],
    });
    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.resolveTo(true);
    fixture = TestBed.createComponent(ProdutoFormComponent);
    component = fixture.componentInstance;
  }

  describe('novo produto', () => {
    beforeEach(() => {
      configurar(null);
      fixture.detectChanges();
    });

    it('começa como FISICO com campos de digital desabilitados', () => {
      expect(component.tipo).toBe('FISICO');
      expect(component.form.controls.pesoKg.enabled).toBeTrue();
      expect(component.form.controls.tamanhoArquivoMb.disabled).toBeTrue();
    });

    it('ao trocar para DIGITAL alterna os campos específicos', () => {
      component.form.controls.tipo.setValue('DIGITAL');
      expect(component.form.controls.tamanhoArquivoMb.enabled).toBeTrue();
      expect(component.form.controls.pesoKg.disabled).toBeTrue();
    });

    it('não envia formulário inválido', () => {
      component.salvar();
      expect(service.criar).not.toHaveBeenCalled();
      expect(component.form.controls.nome.touched).toBeTrue();
    });

    it('envia apenas os campos do tipo escolhido e navega para a lista', () => {
      service.criar.and.returnValue(of({ id: 1, nome: 'E-book' } as Produto));
      component.form.controls.tipo.setValue('DIGITAL');
      component.form.patchValue({ nome: '  E-book ', preco: 19.9, tamanhoArquivoMb: 3 });

      component.salvar();

      expect(service.criar).toHaveBeenCalledWith({
        tipo: 'DIGITAL',
        nome: 'E-book',
        preco: 19.9,
        tamanhoArquivoMb: 3,
      });
      expect(router.navigate).toHaveBeenCalledWith(['/produtos']);
    });

    it('mostra a mensagem do ProblemDetail quando a API rejeita', () => {
      service.criar.and.returnValue(
        throwError(() => new HttpErrorResponse({ status: 400, error: { detail: 'O campo \'pesoKg\' deve ser preenchido.' } })),
      );
      component.form.patchValue({ nome: 'Mesa', preco: 100, pesoKg: 10, dimensoesCm: 120 });

      component.salvar();

      expect(component.erroApi()).toContain('pesoKg');
      expect(router.navigate).not.toHaveBeenCalled();
    });
  });

  describe('edição', () => {
    const existente: Produto = {
      id: 5, tipo: 'DIGITAL', nome: 'Curso', preco: 199.9, tamanhoArquivoMb: 2048,
      criadoEm: '', atualizadoEm: '',
    };

    beforeEach(() => {
      configurar('5');
      service.buscar.and.returnValue(of(existente));
      fixture.detectChanges(); // dispara ngOnInit -> buscar(5)
    });

    it('carrega o produto, bloqueia o tipo e envia PUT', () => {
      expect(service.buscar).toHaveBeenCalledWith(5);

      expect(component.editando).toBeTrue();
      expect(component.form.controls.tipo.disabled).toBeTrue();
      expect(component.form.controls.nome.value).toBe('Curso');

      service.atualizar.and.returnValue(of(existente));
      component.form.controls.nome.setValue('Curso Java');
      component.salvar();

      expect(service.atualizar).toHaveBeenCalledWith(5, jasmine.objectContaining({ tipo: 'DIGITAL', nome: 'Curso Java' }));
    });
  });
});
