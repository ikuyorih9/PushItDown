import { Component, ViewChild } from '@angular/core';
import { TabelaComponent } from "../tabela/tabela.component";
import { SLIDELOGS_FRAMES } from '../../slidelogs.frames';
import { FormsModule } from '@angular/forms';

interface FiltrosAll {
  data?: Date;
  tipo?: string;
  periodo?: number;
}

interface FiltrosDay {
  mes?: Date;
  periodo?: number;
}

@Component({
  selector: 'app-slidelogs',
  imports: [TabelaComponent, FormsModule],
  templateUrl: './slidelogs.component.html',
  styleUrl: './slidelogs.component.css'
})
export class SlidelogsComponent {
  @ViewChild('tabela') tabelaComponent!: TabelaComponent;

  frames = SLIDELOGS_FRAMES;
  index = Math.floor(Math.random() * this.frames.length);
  filtros: { [key: string]: any } = {}; // ou defina os campos esperados

  next(){
    this.index = (this.index + 1) % this.frames.length;
  }
  
  prev(){
    this.index = (this.index - 1 + this.frames.length) % this.frames.length;
  }

  applyFilters(){
    if(this.frames[this.index].tableType === 'all'){
      const data:Date = this.filtros['data'] ? this.filtros['data'] : null;
      const tipo:string = this.filtros['tipo'] ? this.filtros['tipo'].toUpperCase(): null;
      const periodo:number = this.filtros['periodo'] ? this.filtros['periodo'] : null;

      this.tabelaComponent.applyFilters({data: data, tipo: tipo, periodo: periodo});
    }
    else if (this.frames[this.index].tableType === 'days'){
      const mes = this.filtros['mes'] ? `${this.filtros['mes']}-01` : null;
      const periodo : number = this.filtros['periodo'] ? this.filtros['periodo'] : null;

      this.tabelaComponent.applyFilters({mes: mes, periodo: periodo});
    }
    
  }

  resetFilters(){
    this.tabelaComponent.resetFilters();
  }
}
