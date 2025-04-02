import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService, AuthConfig } from 'angular-oauth2-oidc';

const authConfig: AuthConfig = {
  issuer: 'http://localhost:8080', // Servidor de autenticação
  redirectUri: 'http://localhost:4200/login', // URL do frontend Angular
  clientId: 'pushitdown', // ID do cliente registrado no servidor OAuth2
  responseType: 'code', // Authorization Code Flow
  scope: 'openid profile', // Escopos necessários
  showDebugInformation: true,
  strictDiscoveryDocumentValidation: false,
  tokenEndpoint: 'http://localhost:8080/oauth2/token',
  loginUrl: 'http://localhost:8080/oauth2/authorize',
  requireHttps: false,
  logoutUrl: 'http://localhost:8080/logout',
  postLogoutRedirectUri: 'http://localhost:4200/login'
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  redirectUrl: string | null = null; // Armazena a URL original

  constructor(private oauthService: OAuthService, private router: Router) {
    this.oauthService.configure(authConfig);
    
    this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      if (this.isAuthenticated()) {
        console.log('✅ Token obtido com sucesso:', this.oauthService.getAccessToken());
        this.redirectAfterLogin(); // Redireciona após login bem-sucedido
      } else {
        console.log('❌ Falha ao obter o token.');
      }
    });
  }

  login() {
    this.oauthService.initLoginFlow();
  }

  logout() {
    this.oauthService.logOut();
    
  }

  getToken() {
    return this.oauthService.getAccessToken();
  }
  
  getUserInfo(): any {
    return this.oauthService.getIdentityClaims();
  }

  isTokenExpired(token: string): boolean{
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const now = Math.floor(Date.now() / 1000); // em segundos
      return payload.exp < now;
    } 
    catch (e) {
      return true; // se não conseguir decodificar, considera expirado
    }
  }

  async refreshToken(): Promise<void> {
    const refreshToken = sessionStorage.getItem('refresh_token');
  
    const body = new URLSearchParams();
    body.set('grant_type', 'refresh_token');
    body.set('refresh_token', refreshToken!);
    body.set('client_id', 'pushitdown');
  
    const response = await fetch('http://localhost:8080/oauth2/token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: body.toString()
    });
  
    if (!response.ok) {
      throw new Error('Erro ao renovar token');
    }
  
    const data = await response.json();
    sessionStorage.setItem('access_token', data.access_token);
    sessionStorage.setItem('refresh_token', data.refresh_token);
  }

  findWhenExpires(): Date {
    const expiresAt = this.oauthService.getAccessTokenExpiration(); // number
    return new Date(expiresAt); // converte para Date
  }

  isAuthenticated(): boolean {
    console.log(`Token expires at ${this.findWhenExpires().toISOString()}`);
    const token = this.oauthService.getAccessToken();
    const isValid = !this.isTokenExpired(token);
    // const isValid = this.oauthService.hasValidAccessToken();
    console.log('🔍 Verificando token:', isValid ? '✅ Válido' : '❌ Inválido');
    return isValid;
  }

  redirectAfterLogin() {
  if (this.redirectUrl) {
    this.router.navigateByUrl(this.redirectUrl);
    this.redirectUrl = null; // Limpa a URL armazenada
  } else {
    this.router.navigate(['/']); // Redireciona para a home se não houver URL armazenada
  }
  }
}
