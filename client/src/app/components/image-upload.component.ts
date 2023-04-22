import { Component, HostListener, ViewChild } from '@angular/core';
import { WebcamInitError, WebcamImage, WebcamComponent } from 'ngx-webcam';
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
    this.tempStorage.setImage(null)
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
    var imageBlob = event.target.files[0]
    this.tempStorage.setImage(imageBlob)
    reader.readAsDataURL(imageBlob)
    console.log(imageBlob)
    reader.onloadend = () => {
      const base64data = reader.result
      if (null != base64data)
        this.resizeDataUrl(base64data?.toString())
    };
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
    this.showWebcam = false
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
    this.resizeDataUrl(webcamImage.imageAsDataUrl)
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


  resizeDataUrl(dataURL: string){
    const image = new Image()
    console.log("resizing")
    image.src = dataURL
    image.onload = () => {
      const canvas = document.createElement('canvas')
      const MAX_WIDTH = 600
      const MAX_HEIGHT = 600
      let width = image.width
      let height = image.height

      if (width > height) {
        if (width > MAX_WIDTH) {
          height *= MAX_WIDTH / width
          width = MAX_WIDTH
        }
      } else {
        if (height > MAX_HEIGHT) {
          width *= MAX_HEIGHT / height
          height = MAX_HEIGHT
        }
      }
      canvas.width = width
      canvas.height = height
      const ctx = canvas.getContext('2d');
      ctx?.drawImage(image, 0, 0, width, height)
      this.url = canvas.toDataURL('image/jpeg')
      canvas.toBlob(blob =>{
        console.log("canvas blob: ", blob)
        this.tempStorage.setImage(blob)
      })
    };
  }
}
