import { Injectable } from '@angular/core';
import { RequisitionsService } from './requisitions.service';


@Injectable({
  providedIn: 'root'
})
export class TimerService {

  private username = "";
  totalHoras:number = 0;

  constructor(private requisitionService:RequisitionsService){
    const claimsString = sessionStorage.getItem('id_token_claims_obj');
    if(claimsString){
      const claims = JSON.parse(claimsString);
      this.username = claims.sub;
    }
  }

  async getTotalHoras(): Promise<number>{
    const apiUrl = `http://localhost:8080/home/${this.username}/total-horas`;
    return this.requisitionService.get<number>(apiUrl);
  }

  async entrance(): Promise<string>{
    const apiUrl = `http://localhost:8080/home/${this.username}/entrance`;
    return this.requisitionService.post<string>(apiUrl, null);
  }

  async exit(): Promise<string>{
    const apiUrl = `http://localhost:8080/home/${this.username}/exit`;
    return this.requisitionService.post<string>(apiUrl, null);
  }
}
