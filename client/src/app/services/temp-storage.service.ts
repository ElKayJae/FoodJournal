import { Injectable } from '@angular/core';
import { FoodData } from '../models';

@Injectable({
  providedIn: 'root'
})
export class TempStorageService {

  foodList!: FoodData[]
  imageBlob!: Blob
  
  constructor() { }

  setfoodList(foodList :FoodData[]){
    this.foodList = foodList
  }

  setImage(image: Blob){
    this.imageBlob = image
  }
}
