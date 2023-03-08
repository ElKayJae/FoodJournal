import { Component, ElementRef, HostListener, ViewChild } from '@angular/core';
import { WebcamInitError, WebcamImage, WebcamUtil, WebcamComponent } from 'ngx-webcam';
import { Observable, Subject } from 'rxjs';
import { TempStorageService } from '../services/temp-storage.service';

@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.css']
})
export class ImageUploadComponent {

  @ViewChild(WebcamComponent)
  webcam!: WebcamComponent

  // toggle webcam on/off
  showWebcam = false;
  showFile = true;
  retake = false;
  multipleWebcamsAvailable = false;
  
  webCamWidth! :number
  errors: WebcamInitError[] = []
  // latest snapshot
  trigger = new Subject<void>()
  nextWebcam = new Subject<boolean|string>()
  
  //holds the image displayed on the page
  url : any

  constructor(private tempStorage: TempStorageService){
    this.setWebcamWidth()
  }

  ngOnInit(): void {
  }

  @HostListener('window:resize', ['$event'])
  setWebcamWidth(){
    const screenWidth = window.innerWidth
    if (screenWidth - 56 < 500 )
      this.webCamWidth = screenWidth -56
      else this.webCamWidth = 500
  }


  selectImage(event: any){
    this.showWebcam = false
    this.showFile = true
    this.retake = false
    console.log(event)
    const reader = new FileReader()
    //event.target.files is an attribute of event object
    const imageBlob = event.target.files[0]
    this.tempStorage.setImage(imageBlob)
    reader.readAsDataURL(imageBlob)
    console.log(imageBlob)
    reader.onload = () => {
      this.url = reader.result
    }
    event.target.value = ""
  }

  showCamera(){
    this.showWebcam = true
    this.showFile = false
    this.retake = false
  }

  snap(): void {
    this.showWebcam = false
    this.retake = true
    this.trigger.next()
  }

  closeWebcam(): void {
    this.showWebcam = !this.showWebcam
    this.retake =  false
    this.showFile = true
  }

  handleInitError(error: WebcamInitError): void {
    this.errors.push(error);
  }

  showNextWebcam(directionOrDeviceId: boolean|string): void {
    this.nextWebcam.next(directionOrDeviceId)
  }

  capture(webcamImage: WebcamImage): void {
    console.info('received webcam image', webcamImage)
    this.tempStorage.setImage(this.dataURItoBlob(webcamImage.imageAsDataUrl))
    this.url = webcamImage.imageAsDataUrl

    this.showFile = true
  }

  public get nextWebcamObservable(): Observable<boolean|string> {
    return this.nextWebcam.asObservable()
  }

  dataURItoBlob(dataURI: string) {
    // convert base64 to raw binary data held in a string
    var byteString = atob(dataURI.split(',')[1]);

    // separate out the mime component
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]

    // write the bytes of the string to an ArrayBuffer
    var ab = new ArrayBuffer(byteString.length)
    var ia = new Uint8Array(ab);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    return new Blob([ab], {type: mimeString})
  }


}
