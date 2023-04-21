import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form! : FormGroup
  hide = true
  submitted = false
  jwt = ''

  ngOnInit(): void {
    this.form = this.createForm()
    this.submitted = false
  }


  constructor(private fb : FormBuilder, private authService : AuthService, private router : Router, private snackBar : MatSnackBar){}

  createForm(){
    return this.fb.group({
      email : this.fb.control('',[Validators.required, Validators.email]),
      password: this.fb.control('',[Validators.required])
    })
  }


  login(){
    this.submitted = true
    this.authService.getJwtToken(this.form.value['email'], this.form.value['password'])
    .then(
      token => {
        this.jwt = token['token']
        localStorage.setItem('jwt', this.jwt)}
        ).then(() => 
        {
          this.router.navigate([''])
          this.snackBar.open(`logged in as ${this.form.value['email']}`, 'OK',{duration : 2000})

        })
        .catch(error => {
          console.error(error)
          this.submitted = false
          this.snackBar.open(`incorrect credentials`,'DISMISS',{duration : 2000} )}
          )
    
  }


  getErrorMessage() {
    if (this.form.get('email')?.hasError('required')) {
      return 'You must enter a value';
    }

    return this.form.get('email')?.hasError('email') ? 'Not a valid email' : '';
  }
  
}
