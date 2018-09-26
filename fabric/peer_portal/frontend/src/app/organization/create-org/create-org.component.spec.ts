import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateOrgComponent } from './create-org.component';

describe('CreateOrgComponent', () => {
  let component: CreateOrgComponent;
  let fixture: ComponentFixture<CreateOrgComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateOrgComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateOrgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
