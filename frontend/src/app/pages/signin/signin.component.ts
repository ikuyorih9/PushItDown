import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RequisitionsService } from '../../services/requisitions.service';
import { HeaderComponent } from '../../components/header/header.component';

@Component({
  selector: 'app-signin',
  imports: [ReactiveFormsModule, HeaderComponent],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css'
})
export class SigninComponent {

  profileForm = new FormGroup({
    username: new FormControl(''),
    nome: new FormControl(''),
    senha: new FormControl('')
  });

  constructor(private requisitionService: RequisitionsService){}

  submit(){
    const apiUrl = "http://localhost:8080/auth/cadastrar";
    const usuario = {
      username: this.profileForm.value.username,
      nome: this.profileForm.value.nome,
      senha: this.profileForm.value.senha
    }
    this.requisitionService.post<any>(apiUrl, usuario)
    .then((response:string)=>{
      alert(response);
    })
    .catch((error:Error)=>{
      alert(error.message)
    });
  }
}
