import { Component } from '@angular/core';
import { TabelaComponent } from '../../components/tabela/tabela.component';
import { SlidelogsComponent } from "../../components/slidelogs/slidelogs.component";
import { HeaderComponent } from "../../components/header/header.component";

@Component({
    selector: 'app-historico',
    standalone:true,
    imports: [SlidelogsComponent, HeaderComponent],
    templateUrl: './historico.component.html',
    styleUrl: './historico.component.css'
})
export class HistoricoComponent {

}
