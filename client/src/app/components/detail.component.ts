import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
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
    const day_id = this.activatedRoute.snapshot.params['id']
    this.day_string = this.activatedRoute.snapshot.params['day']
    this.day = new Date(this.day_string)
    this.apiService.getMealsByDayId(day_id).then(v => {
      this.meal_list = v as Meal[]
    })
  }


  deleteMeal(meal: Meal| undefined) {
    const dialogRef = this.dialog.open(DialogConfirmation, {
      width:'250px', data:{meal: meal}})
      dialogRef.afterClosed().subscribe(result => {
        if (result) console.log(meal?.meal_id, meal?.category) 
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
