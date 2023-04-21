import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ApiService } from "../services/api.service";
import { NavigationService } from "../services/navigation.service";
import { Router } from "@angular/router";

@Component({
    selector: 'update-target-calorie',
    templateUrl: './update-target-calorie.html',
    styleUrls: ['./update-target-calorie.css']
  })
export class UpdateTargetCalorie implements OnInit{

  form! : FormGroup
  target! : number

  constructor(private fb: FormBuilder, private apiService: ApiService, private navService: NavigationService, private router: Router) {}


  ngOnInit(): void {
    this.apiService.getTargetCalorie().then(
      v => {
        this.target = v['target']
        this.form = this.createForm()}
    ).catch(error => {
      console.error(error, 'authentication failed')
      this.router.navigate(['/login'])
    })
      this.form = this.createForm()
  }


  createForm(){
      return this.fb.group({
          calorie : this.fb.control('',[Validators.required, Validators.min(10)]),
        })
  }


  isNumber(){
    if (Number.isNaN(Number(this.form.value['calorie']))) return false
    else return true
  }


  submit(){
      const target: number = this.form.value['calorie']
      this.apiService.updateTargetCalorie(target).then(
        () => this.router.navigate(['/'])
      ).catch(error => console.error('error', error))
  }


  back(){
    this.navService.back()
  }
  
}
