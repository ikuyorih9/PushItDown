import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SlidelogsComponent } from './slidelogs.component';

describe('SlidelogsComponent', () => {
  let component: SlidelogsComponent;
  let fixture: ComponentFixture<SlidelogsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SlidelogsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SlidelogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
