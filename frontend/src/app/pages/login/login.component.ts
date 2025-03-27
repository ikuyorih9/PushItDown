import { Component, Inject, NgModule, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { NgModel } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  // authService = Inject(AuthService);
  constructor(private authService: AuthService) {}
  ngOnInit(): void {
    if(this.authService.isAuthenticated()){
      console.log("AUTENTICADO");
      this.authService.redirectAfterLogin();
    }
  }

  login() {
    this.authService.login();
  }

  logout() {
    this.authService.logout();
  }

  // getData() {
  //   this.http.get('http://localhost:8081/api/protected-endpoint').subscribe(
  //     (data) => console.log(data),
  //     (error) => console.error(error)
  //   );
  // }
}
