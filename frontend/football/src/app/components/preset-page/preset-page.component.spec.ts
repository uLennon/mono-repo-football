import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PresetPageComponent } from './preset-page.component';

describe('PresetPageComponent', () => {
  let component: PresetPageComponent;
  let fixture: ComponentFixture<PresetPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PresetPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PresetPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
