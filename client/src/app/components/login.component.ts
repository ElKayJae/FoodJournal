import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form! : FormGroup
  hide = true

  ngOnInit(): void {
    this.form = this.createForm()
  }


  constructor(private fb : FormBuilder, private authService : AuthService){}

  createForm(){
    return this.fb.group({
      email : this.fb.control('',[Validators.required, Validators.email]),
      password: this.fb.control('',[Validators.required])
    })
  }


  login(){
    this.authService.getJwtToken(this.form.value['email'], this.form.value['password'])
    
  }


  getErrorMessage() {
    if (this.form.get('email')?.hasError('required')) {
      return 'You must enter a value';
    }

    return this.form.get('email')?.hasError('email') ? 'Not a valid email' : '';
  }
  
  
}
