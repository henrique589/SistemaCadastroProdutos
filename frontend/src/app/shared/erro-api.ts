import { HttpErrorResponse } from '@angular/common/http';

import { ProblemaApi } from '../produtos/produto.model';

/** Extrai uma mensagem legível de um erro HTTP (ProblemDetail ou falha de rede). */
export function mensagemDeErro(erro: unknown): string {
  if (erro instanceof HttpErrorResponse) {
    if (erro.status === 0) {
      return 'Não foi possível conectar ao servidor. Verifique se a API está no ar.';
    }
    const problema = erro.error as ProblemaApi | undefined;
    if (problema?.campos?.length) {
      return problema.campos.map((c) => `${c.campo}: ${c.mensagem}`).join('; ');
    }
    if (problema?.detail) {
      return problema.detail;
    }
    return `Erro ${erro.status} ao comunicar com a API.`;
  }
  return 'Ocorreu um erro inesperado.';
}
