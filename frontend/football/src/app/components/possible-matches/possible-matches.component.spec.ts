import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PossibleMatchesComponent } from './possible-matches.component';

describe('PossibleMatchesComponent', () => {
  let component: PossibleMatchesComponent;
  let fixture: ComponentFixture<PossibleMatchesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PossibleMatchesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PossibleMatchesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
