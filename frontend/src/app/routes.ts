import { Routes } from "@angular/router";
import { HomeComponent } from "./pages/home/home.component";
import { HistoricoComponent } from "./pages/historico/historico.component";

export const routeConfig: Routes = [
    {
      path: '',
      component: HomeComponent,
      title: 'Home',
    },
    {
        path:'historico',
        component: HistoricoComponent,
        title: 'Historico',
    }
  ];