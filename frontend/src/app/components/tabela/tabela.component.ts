import { Component, Inject, OnInit } from '@angular/core';
import { Session, SessionsService } from '../../services/sessions.service';

@Component({
    selector: 'app-tabela',
    standalone:true,
    imports: [],
    templateUrl: './tabela.component.html',
    styleUrl: './tabela.component.css'
})
export class TabelaComponent{
  sessions: Session[] = [];
  // sessionsService = Inject(SessionsService);
  
  constructor(private sessionsService:SessionsService){
    console.log("LOG");
    sessionsService.getAllSessions().then((sessions:Session[]) => {
      this.sessions = sessions;
      
    });
  }

  click(){
    console.log(this.sessions);
  }

}
