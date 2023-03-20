import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit{

  form! : FormGroup
  hide = true

  ngOnInit(): void {
    this.form = this.createForm()
  }

  constructor(private fb : FormBuilder, private authService : AuthService, private router : Router, private snackBar : MatSnackBar){}

  createForm(){
    return this.fb.group({
      name : this.fb.control('',[Validators.required]),
      email : this.fb.control('',[Validators.required, Validators.email]),
      password: this.fb.control('',[Validators.required])
    })
  }

  register(){
    this.authService.register(this.form.value['name'], this.form.value['email'], this.form.value['password']).then(
      token => {
        this.authService.jwt = token['token']
        localStorage.setItem('jwt', token['token'])}
        ).then(() => this.router.navigate(['']))
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
  
}

