import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom, Observable } from 'rxjs';
import { Meal } from '../models';
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


  addMeal(meal: Meal){

    const jwt = localStorage.getItem('jwt')
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${jwt}`)
    let formdata = new FormData()
    formdata.set('image', this.tempStorage.imageBlob)
    console.log(formdata)
    console.log("inside add meal>>>>>>>>>>>>>>>>>>", this.tempStorage.imageBlob)

    lastValueFrom(
      this.httpClient.post<any>('/api/uploadimage', formdata, { headers: headers})).then(
        data => {
          console.log(data)
          meal.meal_id = data['meal_id']
          meal.imageurl = data['image_url']
          console.log(meal.imageurl)
        }).then(() => lastValueFrom(this.httpClient.post<any>('/api/addmeal', meal, {headers : headers})))



      
    
  }
}
