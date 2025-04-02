import { Component, Inject, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { DaySessions, Session, SessionsService } from '../../services/sessions.service';
import { TimeFormatPipe } from '../../pipes/time-format.pipe';
@Component({
    selector: 'app-tabela',
    standalone:true,
    imports: [TimeFormatPipe],
    templateUrl: './tabela.component.html',
    styleUrl: './tabela.component.css'
})
export class TabelaComponent implements OnInit{
  @Input() tableType:string = '';
  @Input() tableColumns: string[] = [];

  sessions: Session[] = [];
  daySessions: DaySessions[] = [];
  
  constructor(private sessionsService:SessionsService){}

  ngOnInit(): void {
    this.sessionsService.getAllSessions().then((sessions:Session[]) => {
      this.sessions = sessions;
    });

    this.sessionsService.getDaySessions().then((daySessions:DaySessions[]) => {
      this.daySessions = daySessions;
    });

  }

  applyFilters(filtros : any){
    if(this.tableType === "all"){
      const data = filtros.data;
      const tipo = filtros.tipo;
      const periodo = filtros.periodo;

      this.sessionsService.getFilteredAllSessions(data,tipo,periodo).then((sessions:Session[]) =>{
        this.sessions = sessions;
      });
    }
    else if(this.tableType === "days"){
      const mes = filtros.mes;
      const periodo = filtros.periodo;
      this.sessionsService.getFilteredExpedientes(mes,periodo).then((daySessions:DaySessions[])=>{
        this.daySessions = daySessions;
      });
    }
  }

  resetFilters(){
    this.sessionsService.getAllSessions().then((sessions:Session[]) => {
      this.sessions = sessions;
    });

    this.sessionsService.getDaySessions().then((daySessions:DaySessions[]) => {
      this.daySessions = daySessions;
    });
  }
}
