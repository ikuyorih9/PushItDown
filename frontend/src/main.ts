import { bootstrapApplication, provideProtractorTestingSupport } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import {routeConfig} from './app/routes';
import { OAuthModule } from 'angular-oauth2-oidc';
import { importProvidersFrom } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';

bootstrapApplication(AppComponent, {
  providers: [
    
    provideProtractorTestingSupport(), 
    provideRouter(routeConfig),
    provideHttpClient(),
    importProvidersFrom(OAuthModule.forRoot())
  ],
}).catch((err:any) => console.error(err));
