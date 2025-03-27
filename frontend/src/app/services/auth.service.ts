import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService, AuthConfig } from 'angular-oauth2-oidc';

const authConfig: AuthConfig = {
  issuer: 'http://auth-server.local:9090', // Servidor de autenticação
  redirectUri: 'http://localhost:4200/login', // URL do frontend Angular
  clientId: 'pushitdown', // ID do cliente registrado no servidor OAuth2
  responseType: 'code', // Authorization Code Flow
  scope: 'openid profile', // Escopos necessários
  showDebugInformation: true,
  strictDiscoveryDocumentValidation: false,
  tokenEndpoint: 'http://auth-server.local:9090/oauth2/token',
  loginUrl: 'http://auth-server.local:9090/oauth2/authorize',
  requireHttps: false
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  redirectUrl: string | null = null; // Armazena a URL original
  constructor(private oauthService: OAuthService, private router: Router) {
    console.log("LOG");
    this.oauthService.configure(authConfig);
    this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      if (this.oauthService.hasValidAccessToken()) {
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

  isAuthenticated(): boolean {
    const isValid = this.oauthService.hasValidAccessToken();
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
