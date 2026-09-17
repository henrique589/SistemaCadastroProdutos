import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { mensagemDeErro } from '../../shared/erro-api';
import { NotificacaoService } from '../../shared/notificacao.service';
import { FiltroProdutos, Pagina, Produto, TIPOS_PRODUTO, TipoProduto, rotuloTipo } from '../produto.model';
import { ProdutoService } from '../produto.service';

@Component({
  selector: 'app-produto-lista',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './produto-lista.component.html',
})
export class ProdutoListaComponent implements OnInit {
  private readonly service = inject(ProdutoService);
  private readonly notificacao = inject(NotificacaoService);

  readonly tipos = TIPOS_PRODUTO;
  readonly rotuloTipo = rotuloTipo;

  readonly pagina = signal<Pagina<Produto> | null>(null);
  readonly carregando = signal(false);
  readonly excluindoId = signal<number | null>(null);

  filtro: FiltroProdutos = { tipo: '', nome: '', page: 0, size: 10 };

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando.set(true);
    this.service.listar(this.filtro).subscribe({
      next: (pagina) => {
        this.pagina.set(pagina);
        this.carregando.set(false);
      },
      error: (erro) => {
        this.carregando.set(false);
        this.notificacao.erro(mensagemDeErro(erro));
      },
    });
  }

  filtrar(): void {
    this.filtro.page = 0;
    this.carregar();
  }

  limparFiltro(): void {
    this.filtro = { tipo: '', nome: '', page: 0, size: this.filtro.size };
    this.carregar();
  }

  irParaPagina(numero: number): void {
    const total = this.pagina()?.totalPages ?? 0;
    if (numero < 0 || numero >= total) {
      return;
    }
    this.filtro.page = numero;
    this.carregar();
  }

  excluir(produto: Produto): void {
    if (!confirm(`Excluir o produto "${produto.nome}"?`)) {
      return;
    }
    this.excluindoId.set(produto.id);
    this.service.excluir(produto.id).subscribe({
      next: () => {
        this.excluindoId.set(null);
        this.notificacao.sucesso(`Produto "${produto.nome}" excluído.`);
        // Se era o último item da página, volta uma página.
        const p = this.pagina();
        if (p && p.content.length === 1 && p.number > 0) {
          this.filtro.page = p.number - 1;
        }
        this.carregar();
      },
      error: (erro) => {
        this.excluindoId.set(null);
        this.notificacao.erro(mensagemDeErro(erro));
      },
    });
  }

  /** Texto do atributo específico de cada tipo, para a coluna "Detalhes". */
  detalhes(produto: Produto): string {
    if (produto.tipo === 'FISICO') {
      return `${produto.pesoKg} kg · ${produto.dimensoesCm} cm`;
    }
    return `${produto.tamanhoArquivoMb} MB`;
  }

  classeTipo(tipo: TipoProduto): string {
    return tipo === 'FISICO' ? 'text-bg-primary' : 'text-bg-success';
  }
}
