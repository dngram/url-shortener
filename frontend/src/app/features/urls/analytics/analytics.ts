import {
  Component,
  OnInit
}
from '@angular/core';

import {
  CommonModule
}
from '@angular/common';

import {
  UrlService
}
from '../../../core/services/url.service';

import {
  AnalyticsResponse
}
from '../../../models/analytics-response';

@Component({
  selector: 'app-analytics',
  imports: [CommonModule],
  templateUrl:
    './analytics.html',
  styleUrl:
    './analytics.scss'
})
export class AnalyticsComponent
implements OnInit {

  analytics?:
    AnalyticsResponse;

  constructor(
    private urlService:
    UrlService
  ) {}

  ngOnInit(): void {

    this.urlService
      .getAnalytics()
      .subscribe({

        next: response => {

          this.analytics =
            response;
        }
      });
  }
}