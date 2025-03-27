import { Component, Input, input } from '@angular/core';
import { QUOTES_DATA } from '../../quotes';

@Component({
    selector: 'app-slideview',
    standalone:true,
    imports: [],
    templateUrl: './slideview.component.html',
    styleUrl: './slideview.component.css'
})
export class SlideviewComponent {
  quotes = QUOTES_DATA;

  index = Math.floor(Math.random() * this.quotes.length);

  next(){
    this.index = (this.index + 1) % this.quotes.length;
  }
  
  prev(){
    this.index = (this.index - 1 + this.quotes.length) % this.quotes.length;
  }
}
