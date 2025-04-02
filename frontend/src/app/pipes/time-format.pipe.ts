import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'timeFormat' })
export class TimeFormatPipe implements PipeTransform {
  transform(time: number): string {
    let seconds = Math.floor(time / 1000);
    let h = Math.floor(seconds / 3600);
    let m = Math.floor((seconds % 3600) / 60);
    let s = Math.floor(seconds % 60);

    return (
      (h < 10 ? "0" : "") + h + ":" +
      (m < 10 ? "0" : "") + m + ":" +
      (s < 10 ? "0" : "") + s
    );
  }
}
