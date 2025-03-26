import { Component } from '@angular/core';
import { TabelaComponent } from '../../components/tabela/tabela.component';

@Component({
  selector: 'app-historico',
  standalone: true,
  imports: [TabelaComponent],
  templateUrl: './historico.component.html',
  styleUrl: './historico.component.css'
})
export class HistoricoComponent {

}
