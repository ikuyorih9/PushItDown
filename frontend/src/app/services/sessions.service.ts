import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SessionState } from 'http2';
import { Observable } from 'rxjs';
import { RequisitionsService } from './requisitions.service';

export interface Session{
  data: string;
  hora: string;
  tipo: string;
}

export interface DaySessions{
  data: string;
  horas: number;
}

@Injectable({
  providedIn: 'root'
})
export class SessionsService {
  private username = "";

  constructor(private requisitionService:RequisitionsService){
    const claimsString = sessionStorage.getItem('id_token_claims_obj');
    if(claimsString){
      const claims = JSON.parse(claimsString);
      this.username = claims.sub;
    }
  }

  async getAllSessions(): Promise<Session[]> {
    const apiUrl = `http://localhost:8080/home/${this.username}/registros`;
  
    const token = sessionStorage.getItem('access_token'); // ou localStorage, dependendo de onde você salva
    const response = await fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}` // <-- aqui vai o token
      }
    });
  
    if (!response.ok) {
      if (response.status === 401) {
        // Token inválido ou expirado
        console.warn('Usuário não autenticado');
      }
      throw new Error(`Erro ao buscar sessões: ${response.status}`);
      
    }
    return await response.json();
  }

  async getDaySessions(): Promise<DaySessions[]> {
    const apiUrl = `http://localhost:8080/home/${this.username}/expedientes`;
  
    const token = sessionStorage.getItem('access_token'); // ou localStorage, dependendo de onde você salva
    const response = await fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}` // <-- aqui vai o token
      }
    });
  
    if (!response.ok) {
      if (response.status === 401) {
        // Token inválido ou expirado
        console.warn('Usuário não autenticado');
      }
      throw new Error(`Erro ao buscar sessões: ${response.status}`);
      
    }
    return await response.json();
  }

  async getFilteredAllSessions(data:Date, tipo:string, periodo:number): Promise<Session[]>{
    let apiUrl = `http://localhost:8080/home/${this.username}/registros`
    let params = [];

    if (data != null) {
      params.push(`data=${data}`);
    }

    if (tipo != null) {
      params.push(`tipo=${tipo}`);
    }

    if (periodo != null) {
      params.push(`periodo=${periodo}`);
    }

    if (params.length > 0) {
      apiUrl += '?' + params.join('&');
    }
    return this.requisitionService.get<Session[]>(apiUrl);
  }

  async getFilteredExpedientes(mes:Date, periodo:number): Promise<DaySessions[]>{
    let apiUrl = `http://localhost:8080/home/${this.username}/expedientes`
    let params = [];

    if (mes != null) {
      params.push(`data=${mes}`);
    }

    if (periodo != null) {
      params.push(`periodo=${periodo}`);
    }

    if (params.length > 0) {
      apiUrl += '?' + params.join('&');
    }
    return this.requisitionService.get<DaySessions[]>(apiUrl);
  }
}
