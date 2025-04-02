import { Component, Inject, NgModule, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { NgModel } from '@angular/forms';
import { Router } from '@angular/router';
import { HeaderComponent } from "../../components/header/header.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [HeaderComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  // authService = Inject(AuthService);
  constructor(private authService: AuthService, private router: Router) {}
  ngOnInit(): void {
    if(this.authService.isAuthenticated()){
      console.log("AUTENTICADO");
      // this.authService.redirectAfterLogin();
    }
  }

  login() {
    this.authService.login();
  }

  signin(){
    this.router.navigate(['signin']);
  }
}
