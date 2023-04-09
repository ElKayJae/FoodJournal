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

  setHeaders(){
    const jwt = localStorage.getItem('jwt')
    const headers = new HttpHeaders()
    .set('Authorization', `Bearer ${jwt}`)

    return headers
  }

  
  searchFoodData(query: string){
    const queryParams =  new HttpParams()
      .set('query', query)

    return lastValueFrom(
      this.httpClient.get('/api/search', {params: queryParams, headers: this.setHeaders()}))
  }


  searchDay(day: string) : Promise<any> {
    const queryParams =  new HttpParams()
    .set('date', day)

    return lastValueFrom(
      this.httpClient.get('/api/searchday', {params: queryParams, headers: this.setHeaders()}))
  }


  getTargetCalorie(){
    return lastValueFrom(
      this.httpClient.get<any>('/api/target', {headers: this.setHeaders()}))
  }


  addMeal(meal: Meal, day_id: string){
    const queryParams =  new HttpParams()
      .set('day_id', day_id)

    let formdata = new FormData()
    if (this.tempStorage.imageBlob != null)
      formdata.set('image', this.tempStorage.imageBlob)

    return lastValueFrom(
      this.httpClient.post<any>('/api/uploadimage', formdata, {headers: this.setHeaders()})).then(
        data => {
          console.log(data)
          meal.meal_id = data['meal_id']
          meal.imageurl = data['image_url']
        }).then(() => 
        lastValueFrom(this.httpClient.post('/api/addmeal', meal, {headers: this.setHeaders(), params: queryParams}))
       )
  }

  
  deleteMeal(meal_id: string, calories: number, day_id: string){
    console.log(meal_id)
    const queryParams =  new HttpParams()
    .set('meal_id', meal_id)
    .set('calories', calories)
    .set('day_id', day_id)

    return lastValueFrom(
      this.httpClient.delete('/api/deletemeal', {headers: this.setHeaders(), params: queryParams})
    )
  }


  deleteDay(day_id: string){
    const queryParams =  new HttpParams()
    .set('day_id', day_id)

    return lastValueFrom(
      this.httpClient.delete('/api/deleteday', {headers: this.setHeaders(), params: queryParams})
    )
  }


  getDays(startStr : string, endStr : string): Promise<Day[]> {
    const queryParams =  new HttpParams()
    .set('start', startStr)
    .set('end', endStr)
    return lastValueFrom(
      this.httpClient.get<Day[]>('/api/getdays', {headers: this.setHeaders(), params: queryParams})
      )
  }


  getMealsByDayId(day_id: string){
    const queryParams =  new HttpParams()
    .set('day_id', day_id)

    return lastValueFrom(
      this.httpClient.get<Meal[]>('/api/getmeals' , {headers: this.setHeaders(), params: queryParams})
    )
  }
}
