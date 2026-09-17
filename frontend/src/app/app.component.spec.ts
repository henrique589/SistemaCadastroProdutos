import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AppComponent } from './app.component';
import { NotificacaoService } from './shared/notificacao.service';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [provideRouter([]), provideHttpClient()],
    }).compileComponents();
  });

  it('renderiza o título da aplicação', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const el: HTMLElement = fixture.nativeElement;
    expect(el.querySelector('.navbar-brand')?.textContent).toContain('Cadastro de Produtos');
  });

  it('exibe a notificação atual', () => {
    const fixture = TestBed.createComponent(AppComponent);
    TestBed.inject(NotificacaoService).sucesso('Salvo!');
    fixture.detectChanges();
    const alerta = (fixture.nativeElement as HTMLElement).querySelector('.alert-success');
    expect(alerta?.textContent).toContain('Salvo!');
  });
});
