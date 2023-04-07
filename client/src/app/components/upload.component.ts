import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FoodData, Meal } from '../models';
import { ApiService } from '../services/api.service';
import { AuthService } from '../services/auth.service';
import { NavigationService } from '../services/navigation.service';
import { TempStorageService } from '../services/temp-storage.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit{

  day!: Date
  imageBlob! : Blob
  form!: FormGroup
  url : any
  foodList: FoodData[] = this.tempStorage.foodList
  day_id!: string

  constructor(private activatedRoute: ActivatedRoute, private fb: FormBuilder, 
    private tempStorage: TempStorageService, private authService: AuthService,
    private navService: NavigationService, private apiService: ApiService,
    private router: Router){}

  ngOnInit(): void {
    this.authService.authenticate()
    const dayString = this.activatedRoute.snapshot.params['day']
    this.day = new Date(dayString)
    this.apiService.searchDay(dayString).then(v => {
      this.day_id = v['day_id']
      console.log("day_id: "+this.day_id)
    })
    this.form = this.createForm()
    
  }


  createForm(){
    const date = new Date()
    let timeString: string = date.getHours()%12 + ':'+  date.getMinutes()
    if(date.getHours()> 12) timeString += ' PM' ; else timeString += ' AM'

    return this.fb.group({
      meal : this.fb.control('', [Validators.required, Validators.minLength(3)]),
      time : this.fb.control(timeString),
    })
  }


  process(){
    const configTime = this.processTime()
    console.log("submit form")
    const meal = {
      foodlist : this.tempStorage.foodList,
      timestamp : configTime,
      category : this.form.value['meal']
    }
    this.apiService.addMeal(meal, this.day_id)
      .then(() => {
        this.router.navigate(['/'])
        this.tempStorage.setImage(null)
        this.tempStorage.setfoodList([])
      })
  }


  //gets all the date input and converts to date object
  processTime(){
    let splitString = this.form.value['time'].split(/:| /)
    const period = splitString.pop()
    let numArray : number[] = splitString.map((v:string) => Number(v))
    if( period === 'PM' ) numArray[0] += 12
    const configTime = new Date(this.day.getFullYear(), this.day.getMonth(), 
                        this.day.getDate(), numArray[0], numArray[1])

    return configTime
  }

  
  submitDisabled(){
    if (this.tempStorage.foodList)
       return this.tempStorage.foodList.length <= 0
    return true
  }
  
  back(){
    this.navService.back()
  }

  logout(){
    this.navService.logout()
  }
}
