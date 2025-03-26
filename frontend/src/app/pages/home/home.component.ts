import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TimerComponent } from "../../components/timer/timer.component";
import { ImageCardComponent } from "../../components/image-card/image-card.component";
import { SlideviewComponent } from "../../components/slideview/slideview.component";
import { HeaderComponent } from "../../components/header/header.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [TimerComponent, ImageCardComponent, SlideviewComponent, HeaderComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
