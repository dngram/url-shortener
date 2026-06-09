import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UrlCreate } from './url-create';

describe('UrlCreate', () => {
  let component: UrlCreate;
  let fixture: ComponentFixture<UrlCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UrlCreate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UrlCreate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
