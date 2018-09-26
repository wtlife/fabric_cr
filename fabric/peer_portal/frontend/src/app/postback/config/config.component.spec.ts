import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PostbackConfigComponent } from './config.component';

describe('ConfigComponent', () => {
  let component: PostbackConfigComponent;
  let fixture: ComponentFixture<PostbackConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PostbackConfigComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PostbackConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
