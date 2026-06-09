import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UrlListComponent } from '../url-list/url-list';

import { UrlService } from '../../../core/services/url.service';

import { CreateUrlRequest } from '../../../models/create-url-request';
import { CreateUrlResponse } from '../../../models/create-url-response';
import { Output, EventEmitter } from '@angular/core';
import { ViewChild } from '@angular/core';
import { AnalyticsComponent } from '../analytics/analytics';

@Component({
  selector: 'app-url-create',
  imports: [
    CommonModule,
    FormsModule,
    UrlListComponent,
    AnalyticsComponent
  ],
  templateUrl: './url-create.html',
  styleUrl: './url-create.scss'
})
export class UrlCreateComponent {

  originalUrl = '';
  generatedUrl = '';
  customAlias = '';
  expiryDate = '';

  @Output()
  urlCreated = new EventEmitter<void>();

  @ViewChild(UrlListComponent)
  urlListComponent!:
    UrlListComponent;

  constructor(
    private urlService: UrlService
  ) {}

  generateUrl(): void {

    const request: CreateUrlRequest = {

        originalUrl:
          this.originalUrl,

        customAlias:
          this.customAlias,

        expiryDate:
          this.expiryDate || undefined
      };

    this.urlService
      .createUrl(request)
      .subscribe({
        next: (response: CreateUrlResponse) => {

          this.generatedUrl = response.shortUrl;

          this.urlListComponent.refresh();
        },

        error: err => {
          alert(err.error);
        }
      });
  }
}