import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FoodData } from '../models';
import { ApiService } from '../services/api.service';
import { TempStorageService } from '../services/temp-storage.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  
  form!: FormGroup
  foodList: FoodData[] = []
  searchText = "Calorie Search"
  
  constructor( private fb: FormBuilder, private apiService: ApiService, private tempStorage: TempStorageService, private snackbar: MatSnackBar){}


  ngOnInit(): void {
    this.form = this.createForm()
    
  }
  createForm(){
  
    return this.fb.group({
      search : this.fb.control(''),
    })
  }

  clearValue(controlName: string){
    this.form.get(controlName)?.reset()
  }
  
  searchFood(){
    const query = this.form.value['search']
    this.apiService.searchFoodData(query).then(
        v => {
          console.log(v)
          const result = v as FoodData[]
          if ( result.length <= 0 ) 
            this.snackbar.open('Food not found, please try another name.', 'OK')
          result.forEach(f => this.foodList.push(f))
          this.tempStorage.setfoodList(this.foodList)
          this.searchText = "Search more items"
        }).then(() => this.clearValue('search'))
        .catch( error => console.error(error))
  }

  removeFood(i :number){
    this.foodList.splice(i,1)
    this.tempStorage.setfoodList(this.foodList)
  }
}
