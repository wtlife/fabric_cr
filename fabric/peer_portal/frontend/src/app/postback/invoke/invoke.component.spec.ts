import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InvokeComponent } from './invoke.component';

describe('InvokeComponent', () => {
  let component: InvokeComponent;
  let fixture: ComponentFixture<InvokeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InvokeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InvokeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
