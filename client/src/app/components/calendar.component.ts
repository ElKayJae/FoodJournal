import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Calendar, CalendarOptions, DateSelectArg, EventApi, EventClickArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit, AfterViewInit{

  

  @ViewChild('calendar')
  calender!: Calendar
  
  constructor(private changeDetector: ChangeDetectorRef, private router: Router, private authService : AuthService) {
  }

  ngOnInit(): void {
    this.authService.authenticate()
  }

  ngAfterViewInit(): void {
    console.log("afterViewInit",this.calender)
  }

  events = [
    { id:'1', date: '2023-02-01',  color: "#3F51B5",  calories: 1659},
    { id:'2', date: '2023-02-03',  color: "#FF4081",  calories: 1659},
    { id:'3', date: '2023-02-04',  color: "#3F51B5",  calories: 1659},
    { id:'4', date: '2023-02-05',  color: "#3F51B5",  calories: 1659},
    { id:'5', date: '2023-02-06',  color: "#3F51B5",  calories: 1659},
    { id:'6', date: '2023-02-07',  color: "#3F51B5",  calories: 1659},
    { id:'7', date: '2023-02-08',  color: "#3F51B5",  calories: 1659},
    { id:'8', date: '2023-02-09',  color: "#FF4081",  calories: 1659},
    { id:'9', date: '2023-02-10',  color: "#3F51B5",  calories: 1659},
    { id:'10', date: '2023-02-02',  color: "#3F51B5",  calories: 2655 },

  ]

  currentEvents: EventApi[] = [];

  calendarOptions: CalendarOptions = {
    plugins: [      
      dayGridPlugin,
      interactionPlugin
    ],

    initialView: 'dayGridMonth',
    events: this.events, // alternatively, use the `events` setting to fetch from a feed
    weekends: true,
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    eventColor: 'green',
    eventDisplay: 'block',
    eventTextColor: 'white',
    contentHeight: 400, 
    dateClick: this.dateClicked.bind(this),
    // select: this.handleDateSelect.bind(this),
    eventClick: this.handleEventClick.bind(this),
    // eventsSet: this.handleEvents.bind(this),

  };

  // eventSet(){
  //   // console.log("eventSet")
  // }
  
  dateClicked(clickInfo: any){
    console.log("date clicked", clickInfo)
    this.router.navigate(['/upload' , clickInfo.dateStr])

  }

  // handleDateSelect(selectInfo: DateSelectArg) {
  //   const title = prompt('Please enter a new title for your event');
  //   const calendarApi = selectInfo.view.calendar;

  //   calendarApi.unselect(); // clear date selection

  //   if (title) {
  //     const event = {
  //       id: this.createEventId(),
  //       title,
  //       start: selectInfo.startStr,
  //       end: selectInfo.endStr,
  //       allDay: selectInfo.allDay
  //     }
  //     calendarApi.addEvent(event);
  //     const events : EventApi[] = calendarApi.getEvents()
  //     console.log('date select')
  //     events.forEach( e => console.log( e.title))
  //   }
  // }

  handleEventClick(clickInfo: EventClickArg) {
      // clickInfo.event.remove();
      console.info("EventClick", clickInfo.event.extendedProps)
      console.info("EventClick", clickInfo.event.id)
      this.router.navigate(['/detail' , clickInfo.event.id])
    
  }

  // handleEvents(events: EventApi[]) {
  //   this.currentEvents = events;
  //   this.changeDetector.detectChanges();
  //   this.currentEvents.forEach( e => console.log( e.start))
  // }

  createEventId(){
    return "fsdf12"
  }

}
