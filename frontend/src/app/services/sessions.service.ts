import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SessionState } from 'http2';
import { Observable } from 'rxjs';

export interface Session{
  data: string;
  hora: string;
  tipo: string;
}

@Injectable({
  providedIn: 'root'
})
export class SessionsService {
  private username:string = "ikuyorih9";
  private apiUrl = `https://client.local/home/${this.username}/expedientes`;

  async getAllSessions(): Promise<Session[]>{
    const data = await fetch(this.apiUrl);
    return (await data.json()) ?? [];
  }
}
