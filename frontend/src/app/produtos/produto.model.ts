export type TipoProduto = 'FISICO' | 'DIGITAL';

export const TIPOS_PRODUTO: { valor: TipoProduto; rotulo: string }[] = [
  { valor: 'FISICO', rotulo: 'Físico' },
  { valor: 'DIGITAL', rotulo: 'Digital' },
];

export function rotuloTipo(tipo: TipoProduto): string {
  return TIPOS_PRODUTO.find((t) => t.valor === tipo)?.rotulo ?? tipo;
}

/** Espelha ProdutoResponse do backend. Campos específicos vêm apenas para o tipo correspondente. */
export interface Produto {
  id: number;
  tipo: TipoProduto;
  nome: string;
  preco: number;
  pesoKg?: number;
  dimensoesCm?: number;
  tamanhoArquivoMb?: number;
  criadoEm: string;
  atualizadoEm: string;
}

/** Espelha ProdutoRequest do backend. */
export interface ProdutoRequest {
  tipo: TipoProduto;
  nome: string;
  preco: number;
  pesoKg?: number | null;
  dimensoesCm?: number | null;
  tamanhoArquivoMb?: number | null;
}

/** Subconjunto de org.springframework.data.domain.Page usado pela UI. */
export interface Pagina<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface FiltroProdutos {
  tipo?: TipoProduto | '';
  nome?: string;
  page?: number;
  size?: number;
}

/** Erro RFC 7807 devolvido pelo ApiExceptionHandler. */
export interface ProblemaApi {
  title: string;
  detail: string;
  status: number;
  campos?: { campo: string; mensagem: string }[];
}
