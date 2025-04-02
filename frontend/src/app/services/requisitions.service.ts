import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RequisitionsService {

  private username = "";

  constructor(){
    const claimsString = sessionStorage.getItem('id_token_claims_obj');
    if(claimsString){
      const claims = JSON.parse(claimsString);
      this.username = claims.sub;
    }
  }

  async get<T>(apiUrl:string): Promise<T>{
    const token = sessionStorage.getItem('access_token'); // ou localStorage, dependendo de onde você salva
    
    const response = await fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}` // <-- aqui vai o token
      }
    });
  
    if (!response.ok) {

      throw new Error(`Erro na requisição GET ${apiUrl}: ${response.status}`);
      
    }
    return await response.json();
  }

  async post<T>(apiUrl:string, body:any): Promise<T>{
    const token = sessionStorage.getItem('access_token'); // ou localStorage, dependendo de onde você salva
    
    const response = await fetch(apiUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}` // <-- aqui vai o token
      },
      body: JSON.stringify(body)
    });
  
    if (!response.ok) {
      const errorBody = await response.text();
      
      throw new Error(errorBody);
    }
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json() as T;
    } else {
      return await response.text() as T;
    }
  }
}
