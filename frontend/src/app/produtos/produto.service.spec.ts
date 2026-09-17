import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { Pagina, Produto } from './produto.model';
import { ProdutoService } from './produto.service';

describe('ProdutoService', () => {
  let service: ProdutoService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ProdutoService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('listar envia paginação e ignora filtros vazios', () => {
    service.listar({ nome: '  ', tipo: '' }).subscribe();

    const req = http.expectOne((r) => r.url === '/api/produtos');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('page')).toBe('0');
    expect(req.request.params.get('size')).toBe('10');
    expect(req.request.params.has('nome')).toBeFalse();
    expect(req.request.params.has('tipo')).toBeFalse();
    req.flush({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 10 } as Pagina<Produto>);
  });

  it('listar envia filtros preenchidos', () => {
    service.listar({ nome: ' java ', tipo: 'DIGITAL', page: 2, size: 5 }).subscribe();

    const req = http.expectOne((r) => r.url === '/api/produtos');
    expect(req.request.params.get('nome')).toBe('java');
    expect(req.request.params.get('tipo')).toBe('DIGITAL');
    expect(req.request.params.get('page')).toBe('2');
    expect(req.request.params.get('size')).toBe('5');
    req.flush({ content: [], totalElements: 0, totalPages: 0, number: 2, size: 5 });
  });

  it('criar faz POST com o corpo', () => {
    const corpo = { tipo: 'DIGITAL' as const, nome: 'E-book', preco: 10, tamanhoArquivoMb: 2 };
    let resposta: Produto | undefined;
    service.criar(corpo).subscribe((p) => (resposta = p));

    const req = http.expectOne('/api/produtos');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(corpo);
    req.flush({ id: 1, ...corpo, criadoEm: '', atualizadoEm: '' });
    expect(resposta?.id).toBe(1);
  });

  it('atualizar e excluir usam o id na URL', () => {
    service.atualizar(7, { tipo: 'FISICO', nome: 'X', preco: 1, pesoKg: 1, dimensoesCm: 1 }).subscribe();
    expect(http.expectOne('/api/produtos/7').request.method).toBe('PUT');

    service.excluir(7).subscribe();
    expect(http.expectOne('/api/produtos/7').request.method).toBe('DELETE');
  });
});
