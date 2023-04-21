import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { DialogData, Meal } from '../models';
import { ApiService } from '../services/api.service';
import { AuthService } from '../services/auth.service';
import { NavigationService } from '../services/navigation.service';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit{

  day!: Date
  imageBlob! : Blob
  url : any
  day_id!: string
  meal_list!: Meal[]
  day_string!: string

  constructor(private activatedRoute: ActivatedRoute, private authService: AuthService,
    private navService: NavigationService, private apiService: ApiService,
    private router: Router, private dialog: MatDialog){}


  ngOnInit(): void {
    this.authService.authenticate()
    this.day_id = this.activatedRoute.snapshot.params['id']
    this.day_string = this.activatedRoute.snapshot.params['day']
    this.day = new Date(this.day_string)
    this.getMeals()
  }


  getMeals(){
    this.apiService.getMealsByDayId(this.day_id).then(v => {
      this.meal_list = v as Meal[]
    }).then(() => {
      if (this.meal_list.length == 0){
        this.apiService.deleteDay(this.day_id)
        this.router.navigate(['/'])
      }
    })
  }


  deleteMeal(meal: Meal| undefined) {
    const dialogRef = this.dialog.open(DialogConfirmation, {
      width:'250px', data:{meal: meal}})
      dialogRef.afterClosed().subscribe(result => {
        if (result && meal?.meal_id && meal?.calories){
          console.log(meal.calories)
          this.apiService.deleteMeal(meal.meal_id, meal.calories, this.day_id).then(
            () => this.getMeals())
          .catch(error => console.error("error" , error))
        }
        else console.log("cancelled")
      });
  }
  

  upload(){
    this.router.navigate(['/upload' , this.day_string])
  }


  back(){
    this.navService.back()
  }


  logout(){
    this.navService.logout()
  }

}


@Component({
  selector: 'delete-meal-dialog',
  templateUrl: 'delete-meal-dialog.html',
})
export class DialogConfirmation {
  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {}
}
