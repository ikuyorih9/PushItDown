import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TimeFormatPipe } from '../../pipes/time-format.pipe';
import { RequisitionsService } from '../../services/requisitions.service';
import { TimerService } from '../../services/timer.service';
@Component({
    selector: 'app-timer',
    standalone:true,
    imports: [TimeFormatPipe],
    templateUrl: './timer.component.html',
    styleUrl: './timer.component.css'
})
export class TimerComponent implements OnInit{
  time: number = 0;
  started:boolean = false;
  buttonLabel:string = "Entrar";
  interval: ReturnType<typeof setInterval> | null = null;
  
  @Output() trocarImagem = new EventEmitter<void>();

  constructor(private timerService:TimerService){}

  ngOnInit(): void {
    this.started = localStorage.getItem("started") === "true";
    const time = localStorage.getItem("time"); 

    if(this.started && time !=null){
      this.time = Number(time);
    }
    else{
      this.timerService.getTotalHoras().then((initTime:number)=>{
        this.time = initTime;
      });
    }

    this.started === true ? this.startClock() : this.stopClock();
    
    this.buttonLabel = this.started ? "Sair" : "Entrar";
  }

  pushitdown(){
    this.trocarImagem.emit();
    if(!this.started){
      this.timerService.entrance().then((response:string)=>{
        console.log(response);
      });
      this.startClock();
      this.buttonLabel = "Sair";
    }
    else{
      this.timerService.exit().then((response:string)=>{
        console.log(response);
      });
      this.stopClock();
      this.buttonLabel = "Entrar";
    }

    this.started = !this.started;
    localStorage.setItem("started", String(this.started));
  }

  startClock(){
    let init = Date.now() - this.time;
    this.interval = setInterval(()=>{
      this.time = Date.now() - init;
      localStorage.setItem("time", String(this.time));
    }, 10);
  }

  stopClock(){
    if (this.interval !== null) {
      clearInterval(this.interval);
      this.interval = null;
    }
    localStorage.removeItem("time");
    localStorage.removeItem("started");
  }

}
