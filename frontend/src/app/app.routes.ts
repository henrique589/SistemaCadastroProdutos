import { Routes } from '@angular/router';

import { ProdutoFormComponent } from './produtos/produto-form/produto-form.component';
import { ProdutoListaComponent } from './produtos/produto-lista/produto-lista.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'produtos' },
  { path: 'produtos', component: ProdutoListaComponent, title: 'Produtos' },
  { path: 'produtos/novo', component: ProdutoFormComponent, title: 'Novo produto' },
  { path: 'produtos/:id/editar', component: ProdutoFormComponent, title: 'Editar produto' },
  { path: '**', redirectTo: 'produtos' },
];
