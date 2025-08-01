import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Tecnicos } from './tecnicos';

describe('Tecnicos', () => {
  let component: Tecnicos;
  let fixture: ComponentFixture<Tecnicos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Tecnicos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Tecnicos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
