import { Component, Input } from '@angular/core';
import { trigger, transition, style, animate } from '@angular/animations';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-image-card',
    standalone:true,
    imports: [CommonModule],
    animations: [
      trigger('fadeAnimation', [
        transition(':enter', [
          style({ opacity: 0 }),
          animate('1s ease-in', style({ opacity: 1 })),
        ]),
        transition(':leave', [
          animate('1s ease-out', style({ opacity: 0 })),
        ]),
      ]),
    ],
    templateUrl: './image-card.component.html',
    styleUrl: './image-card.component.css'
})
export class ImageCardComponent {
  @Input() imgSrc: string = "";
  @Input() imgSrcAlternative: string = this.imgSrc;
  
  @Input() imgAlt: string = "";
  @Input() imgAltAlternative: string = this.imgAlt;

  @Input() caption: string = "";
  @Input() captionAlternative: string = this.caption;

  currentImage = sessionStorage.getItem("homeImages") ? Number(sessionStorage.getItem("homeImages")) : 0;
  
  toggleImage(){
    this.currentImage = this.currentImage === 0 ? 1 : 0;
    sessionStorage.setItem("homeImages", String(this.currentImage));
  }
}
