import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { mensagemDeErro } from '../../shared/erro-api';
import { NotificacaoService } from '../../shared/notificacao.service';
import { Produto, ProdutoRequest, TIPOS_PRODUTO, TipoProduto } from '../produto.model';
import { ProdutoService } from '../produto.service';

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './produto-form.component.html',
})
export class ProdutoFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(ProdutoService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notificacao = inject(NotificacaoService);

  readonly tipos = TIPOS_PRODUTO;
  readonly id = signal<number | null>(null);
  readonly carregando = signal(false);
  readonly salvando = signal(false);
  readonly erroApi = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    tipo: this.fb.nonNullable.control<TipoProduto>('FISICO', Validators.required),
    nome: ['', [Validators.required, Validators.maxLength(120)]],
    preco: this.fb.control<number | null>(null, [Validators.required, Validators.min(0)]),
    pesoKg: this.fb.control<number | null>(null, [Validators.min(0)]),
    dimensoesCm: this.fb.control<number | null>(null, [Validators.min(0)]),
    tamanhoArquivoMb: this.fb.control<number | null>(null, [Validators.min(0)]),
  });

  get editando(): boolean {
    return this.id() !== null;
  }

  get tipo(): TipoProduto {
    return this.form.controls.tipo.value;
  }

  ngOnInit(): void {
    // Os campos obrigatórios dependem do tipo escolhido (mesma regra do domínio no backend).
    this.form.controls.tipo.valueChanges.subscribe((tipo) => this.aplicarRegrasDoTipo(tipo));
    this.aplicarRegrasDoTipo(this.tipo);

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.id.set(Number(idParam));
      this.carregarProduto(Number(idParam));
    }
  }

  salvar(): void {
    this.erroApi.set(null);
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const request = this.montarRequest();
    const chamada = this.editando
      ? this.service.atualizar(this.id()!, request)
      : this.service.criar(request);

    this.salvando.set(true);
    chamada.subscribe({
      next: (produto) => {
        this.salvando.set(false);
        this.notificacao.sucesso(
          this.editando ? `Produto "${produto.nome}" atualizado.` : `Produto "${produto.nome}" cadastrado.`,
        );
        this.router.navigate(['/produtos']);
      },
      error: (erro) => {
        this.salvando.set(false);
        this.erroApi.set(mensagemDeErro(erro));
      },
    });
  }

  invalido(campo: keyof typeof this.form.controls): boolean {
    const c = this.form.controls[campo];
    return c.invalid && (c.touched || c.dirty);
  }

  private carregarProduto(id: number): void {
    this.carregando.set(true);
    this.service.buscar(id).subscribe({
      next: (produto) => {
        this.preencher(produto);
        this.carregando.set(false);
      },
      error: (erro) => {
        this.carregando.set(false);
        this.notificacao.erro(mensagemDeErro(erro));
        this.router.navigate(['/produtos']);
      },
    });
  }

  private preencher(produto: Produto): void {
    this.form.patchValue({
      tipo: produto.tipo,
      nome: produto.nome,
      preco: produto.preco,
      pesoKg: produto.pesoKg ?? null,
      dimensoesCm: produto.dimensoesCm ?? null,
      tamanhoArquivoMb: produto.tamanhoArquivoMb ?? null,
    });
    // O backend não permite trocar o tipo de um produto existente.
    this.form.controls.tipo.disable({ emitEvent: false });
  }

  private aplicarRegrasDoTipo(tipo: TipoProduto): void {
    const { pesoKg, dimensoesCm, tamanhoArquivoMb } = this.form.controls;
    const fisicos = [pesoKg, dimensoesCm];
    const digitais = [tamanhoArquivoMb];

    const ativar = tipo === 'FISICO' ? fisicos : digitais;
    const desativar = tipo === 'FISICO' ? digitais : fisicos;

    ativar.forEach((c) => {
      c.setValidators([Validators.required, Validators.min(0)]);
      c.enable({ emitEvent: false });
      c.updateValueAndValidity({ emitEvent: false });
    });
    desativar.forEach((c) => {
      c.reset(null, { emitEvent: false });
      c.disable({ emitEvent: false });
    });
  }

  private montarRequest(): ProdutoRequest {
    // getRawValue inclui o "tipo", mesmo desabilitado na edição.
    const v = this.form.getRawValue();
    const base: ProdutoRequest = { tipo: v.tipo, nome: v.nome.trim(), preco: v.preco! };
    return v.tipo === 'FISICO'
      ? { ...base, pesoKg: v.pesoKg, dimensoesCm: v.dimensoesCm }
      : { ...base, tamanhoArquivoMb: v.tamanhoArquivoMb };
  }
}
