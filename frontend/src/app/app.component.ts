import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TimerComponent } from "./components/timer/timer.component";
import { ImageCardComponent } from "./components/image-card/image-card.component";
import { SlideviewComponent } from "./components/slideview/slideview.component";
import { HeaderComponent } from "./components/header/header.component";
import { RouterModule } from '@angular/router';
import { HomeComponent } from "./pages/home/home.component";

@Component({
    selector: 'app-root',
    standalone:true,
    imports: [RouterModule, RouterOutlet, HeaderComponent],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
