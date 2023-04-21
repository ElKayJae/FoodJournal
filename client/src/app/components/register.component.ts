import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { User } from '../models';
import { NavigationService } from '../services/navigation.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit{

  form! : FormGroup
  hide = true
  submitted = false

  
  constructor(private fb : FormBuilder, private authService : 
    AuthService, private router : Router, private snackBar : MatSnackBar,
    private navService : NavigationService){}


  ngOnInit(): void {
    this.form = this.createForm()
    this.submitted = false
  }


  createForm(){
    return this.fb.group({
      name : this.fb.control('',[Validators.required]),
      email : this.fb.control('',[Validators.required, Validators.email]),
      password: this.fb.control('',[Validators.required]),
      target: this.fb.control('', [Validators.required, Validators.min(10)])
    })
  }


  register(){
    this.submitted = true
    const user : User = {
      "name" : this.form.value['name'],
      "email" : this.form.value['email'],
      "password" : this.form.value['password'],
      "target" : this.form.value['target']
    }
    this.authService.register(user).then(
      token => {
        this.authService.jwt = token['token']
        localStorage.setItem('jwt', token['token'])}
        ).then(() => {
          this.router.navigate([''])
          this.snackBar.open(`logged in as ${user.email}`, 'OK',{duration : 2000})
        })
        .catch(error => {
          console.error(error)
          this.snackBar.open(error['error']['message'], 'DISMISS', {duration: 2000})
        })
  }


  getErrorMessage() {
    if (this.form.get('email')?.hasError('required')) {
      return 'You must enter a value';
    }
    return this.form.get('email')?.hasError('email') ? 'Not a valid email' : '';
  }


  isNumber(){
    if (Number.isNaN(Number(this.form.value['target']))) return false
    else return true
  }


  back(){
    this.navService.back()
  }
  
}


