import { Component, OnInit, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TimerComponent } from "../../components/timer/timer.component";
import { ImageCardComponent } from "../../components/image-card/image-card.component";
import { SlideviewComponent } from "../../components/slideview/slideview.component";
import { HeaderComponent } from "../../components/header/header.component";
import { AuthService } from '../../services/auth.service';
import { TimerService } from '../../services/timer.service';

@Component({
    selector: 'app-home',
    standalone:true,
    imports: [TimerComponent, ImageCardComponent, SlideviewComponent, HeaderComponent],
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent{
    @ViewChild('imagecard_pernalonga') imageCardPernalonga!: ImageCardComponent;
    @ViewChild('imagecard_picapau') imageCardPicapau!: ImageCardComponent;
    username:string = "";


    constructor(private authService:AuthService, private timerService:TimerService){
        this.username = this.authService.getUserInfo()?.sub;
    }

    trocarImagem(){
        this.imageCardPernalonga.toggleImage();
        this.imageCardPicapau.toggleImage();
    }
}
