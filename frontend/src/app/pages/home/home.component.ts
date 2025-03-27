import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TimerComponent } from "../../components/timer/timer.component";
import { ImageCardComponent } from "../../components/image-card/image-card.component";
import { SlideviewComponent } from "../../components/slideview/slideview.component";
import { HeaderComponent } from "../../components/header/header.component";
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-home',
    standalone:true,
    imports: [TimerComponent, ImageCardComponent, SlideviewComponent],
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent {
    username:string = "";

    constructor(private authService:AuthService){
        this.username = this.authService.getUserInfo()?.sub;
    }
}
