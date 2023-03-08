import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FullCalendarModule } from '@fullcalendar/angular';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { CalendarComponent } from './components/calendar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DetailComponent } from './components/detail.component';
import { UploadComponent } from './components/upload.component';
import { MaterialModule } from './material.module';
import { NgxMatTimepickerModule } from 'ngx-mat-timepicker';
import { SearchComponent } from './components/search.component';
import { ImageUploadComponent } from './components/image-upload.component';
import { WebcamModule } from 'ngx-webcam';
import { LoginComponent } from './components/login.component';


const appRoutes: Routes = [
  { path: '', component: CalendarComponent},
  { path: 'login', component: LoginComponent},
  { path: 'upload/:day', component: UploadComponent},
  { path: 'detail/:id', component: DetailComponent},
  { path: '**', redirectTo: 'create', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    CalendarComponent,
    UploadComponent,
    DetailComponent,
    SearchComponent,
    ImageUploadComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    FullCalendarModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, {useHash: true}),
    BrowserAnimationsModule,
    MaterialModule,
    NgxMatTimepickerModule,
    FormsModule,
    WebcamModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
