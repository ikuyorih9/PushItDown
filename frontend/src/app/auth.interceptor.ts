import { Injectable, Injector, inject } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpInterceptorFn, HttpHandlerFn } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    console.log("INTERCEPTOR!!!!!")
    const injector = inject(Injector); // ✅ Inject the Injector instead of AuthService
    const authService = injector.get(AuthService); // ✅ Retrieve AuthService dynamically
    const token = authService.token;

    if (token) {
        req = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
        });
    }

    return next(req);
};