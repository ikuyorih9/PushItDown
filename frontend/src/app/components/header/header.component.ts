import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HEADER_BUTTONS, HeaderButtons } from '../../header.buttons';
import { AuthService } from '../../services/auth.service';
import { LoginComponent } from '../../pages/login/login.component';
import { RequisitionsService } from '../../services/requisitions.service';
import { TimerService } from '../../services/timer.service';

@Component({
    selector: 'app-header',
    standalone:true,
    imports: [RouterLink],
    templateUrl: './header.component.html',
    styleUrl: './header.component.css'
})
export class HeaderComponent {
    @Input() bInfo:HeaderButtons[] = HEADER_BUTTONS;

    constructor(){}

    logout(){
        console.log("SAIR");
    }
}
