import { Routes } from "@angular/router";
import { HomeComponent } from "./pages/home/home.component";
import { HistoricoComponent } from "./pages/historico/historico.component";
import { AuthGuard } from "./auth.guard";
import { LoginComponent } from "./pages/login/login.component";
import { LogoutComponent } from "./pages/logout/logout.component";
import { SigninComponent } from "./pages/signin/signin.component";

export const routeConfig: Routes = [
    {
      path: '',
      component: HomeComponent,
      title: 'Home',
      canActivate: [AuthGuard]
    },
    {
      path:'historico',
      component: HistoricoComponent,
      title: 'Historico',
      canActivate: [AuthGuard]
    },
    {
      path:'login',
      component: LoginComponent,
      title: 'Login',
    },
    {
      path:'logout',
      component: LogoutComponent,
      title: 'Logout'
    },
    {
      path:'signin',
      component: SigninComponent,
      title: 'Cadastro'
    },
    { 
      path: '**', 
      redirectTo: '' } // Redireciona para home se a rota não existir
  ];