import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { CreateUrlRequest } from '../../models/create-url-request';
import { CreateUrlResponse } from '../../models/create-url-response';
import { UrlResponse } from '../../models/url-response';

@Injectable({
  providedIn: 'root'
})
export class UrlService {

  private readonly apiUrl =
    'http://localhost:8080/api/urls';

  constructor(
    private http: HttpClient
  ) {}

  createUrl(
    request: CreateUrlRequest
  ): Observable<CreateUrlResponse> {

    return this.http.post<CreateUrlResponse>(
      this.apiUrl,
      request
    );
  }

  getAllUrls():
    Observable<UrlResponse[]> {

    return this.http.get<UrlResponse[]>(
      this.apiUrl
    );
  }
}