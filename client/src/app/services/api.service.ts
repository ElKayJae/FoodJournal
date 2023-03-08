import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient) { }

  searchFoodData(query: string){

    const queryParams =  new HttpParams()
      .set('query', query) 

    return lastValueFrom(
      this.httpClient.get('/api/search', {params: queryParams}))
  }
}
