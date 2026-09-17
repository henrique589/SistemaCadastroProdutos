import { Injectable, signal } from '@angular/core';

export interface Notificacao {
  tipo: 'success' | 'danger' | 'warning';
  mensagem: string;
}

/** Fila mínima de mensagens exibidas pelo AppComponent (substitui o JOptionPane do sistema original). */
@Injectable({ providedIn: 'root' })
export class NotificacaoService {
  readonly atual = signal<Notificacao | null>(null);
  private timer?: ReturnType<typeof setTimeout>;

  sucesso(mensagem: string): void {
    this.mostrar({ tipo: 'success', mensagem });
  }

  erro(mensagem: string): void {
    this.mostrar({ tipo: 'danger', mensagem });
  }

  fechar(): void {
    clearTimeout(this.timer);
    this.atual.set(null);
  }

  private mostrar(n: Notificacao): void {
    clearTimeout(this.timer);
    this.atual.set(n);
    this.timer = setTimeout(() => this.atual.set(null), 5000);
  }
}
