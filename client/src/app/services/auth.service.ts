import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  jwt = ''

  constructor(private httpClient: HttpClient, private router : Router) {}

  authenticate(){
    const jwt = localStorage.getItem('jwt')
    console.log('jwt in authentication', jwt)
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${jwt}`)
    lastValueFrom(this.httpClient.get('/api/loadpage', {headers: headers}))
      .catch(error => {
        console.error(error)
        this.router.navigate(['/login'])
      })
  }

  getJwtToken(username: string, password: string) {
    const credentials  = {
      "email" : username,
      "password" : password
    }
    lastValueFrom(this.httpClient.post<any>('/api/auth/authenticate', credentials)).then(
      token => {
        console.log(token)
        this.jwt = token['token']
        localStorage.setItem('jwt', this.jwt)}
        ).catch(error => {
          console.error(error)})
  }
}
