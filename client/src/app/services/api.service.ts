import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom, Observable } from 'rxjs';
import { Day, Meal } from '../models';
import { TempStorageService } from './temp-storage.service';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient, private tempStorage: TempStorageService) { }

  searchFoodData(query: string){

    const queryParams =  new HttpParams()
      .set('query', query)

    const jwt = localStorage.getItem('jwt')
    const headers = new HttpHeaders()
    .set('Authorization', `Bearer ${jwt}`)

    return lastValueFrom(
      this.httpClient.get('/api/search', {params: queryParams, headers: headers}))
  }

  searchDay(day: string) : Promise<any> {
    const jwt = localStorage.getItem('jwt')
    const headers = new HttpHeaders()
    .set('Authorization', `Bearer ${jwt}`)
    const queryParams =  new HttpParams()
    .set('date', day)

    return lastValueFrom(
      this.httpClient.get('/api/searchday', {params: queryParams, headers: headers}))
  }

  addMeal(meal: Meal, day_id: string){

    const jwt = localStorage.getItem('jwt')
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${jwt}`)
    const queryParams =  new HttpParams()
      .set('day_id', day_id)

    let formdata = new FormData()
    formdata.set('image', this.tempStorage.imageBlob)

    return lastValueFrom(
      this.httpClient.post<any>('/api/uploadimage', formdata, { headers: headers})).then(
        data => {
          console.log(data)
          meal.meal_id = data['meal_id']
          meal.imageurl = data['image_url']
        }).then(() => 
        lastValueFrom(this.httpClient.post('/api/addmeal', meal, {headers : headers, params: queryParams}))
       )
  }

  getDays(): Promise<Day[]> {
    const jwt = localStorage.getItem('jwt')
    const headers = new HttpHeaders()
    .set('Authorization', `Bearer ${jwt}`)

    return lastValueFrom(
      this.httpClient.get<Day[]>('/api/getdays', {headers: headers})
      )
  }

  getMealsByDayId(day_id: string){
    const jwt = localStorage.getItem('jwt')
    const headers = new HttpHeaders()
    .set('Authorization', `Bearer ${jwt}`)

    const queryParams =  new HttpParams()
    .set('day_id', day_id)

    return lastValueFrom(
      this.httpClient.get<Meal[]>('/api/getmeals' , {headers: headers, params: queryParams})
    )
  }
}
