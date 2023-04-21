import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { User } from '../models';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  jwt = ''

  constructor(private httpClient: HttpClient, private router : Router, private snackBar : MatSnackBar) {}

  
  authenticate(){
    const jwt = localStorage.getItem('jwt')
    console.log("authenticating...")
    const headers = new HttpHeaders()
    .set('Authorization', `Bearer ${jwt}`)
    lastValueFrom(this.httpClient.get('/api/loadpage', {headers: headers})).then( () => console.log("authenticated"))
      .catch(error => {
        console.error(error, "authentication failed")
        this.router.navigate(['/login'])
      })
  }


  getJwtToken(username: string, password: string) {
    const credentials  = {
      "email" : username,
      "password" : password
    }
    return lastValueFrom(this.httpClient.post<any>('/api/auth/login', credentials))

  }


  register(user : User) : Promise<any> {
    return lastValueFrom(this.httpClient.post<any>('/api/auth/register', user))
  }

}
