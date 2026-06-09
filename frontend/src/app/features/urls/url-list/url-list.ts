import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UrlService } from '../../../core/services/url.service';
import { UrlResponse } from '../../../models/url-response';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-url-list',
  imports: [CommonModule, DatePipe],
  templateUrl: './url-list.html',
  styleUrl: './url-list.scss'
})
export class UrlListComponent
implements OnInit {

  urls: UrlResponse[] = [];

  constructor(
    private urlService: UrlService
  ) {}

  ngOnInit(): void {

    this.loadUrls();
  }

  refresh(): void {

    this.loadUrls();
  }

  loadUrls(): void {

    this.urlService
      .getAllUrls()
      .subscribe({

        next: response => {

          this.urls = response;
        }
      });
  }

  copyUrl(
    shortCode: string
  ): void {

    navigator.clipboard
      .writeText(
        'http://localhost:8080/'
        + shortCode
      );

    alert(
      'URL copied'
    );
  }
}