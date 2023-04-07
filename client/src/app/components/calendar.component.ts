import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Calendar, CalendarOptions, DateSelectArg, EventApi, EventClickArg, EventInput, EventSourceFuncArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { Day } from '../models';
import { ApiService } from '../services/api.service';
import { AuthService } from '../services/auth.service';
import { NavigationService } from '../services/navigation.service';


@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit, AfterViewInit{

  target! : number

  @ViewChild('calendar')
  calender!: Calendar
  
  constructor(private router: Router, private authService : AuthService, private navService: NavigationService, private apiService: ApiService) {
  }

  ngOnInit(): void {
    this.apiService.getTargetCalorie().then(
      v => this.target = v['target']
    ).catch(error => {
      console.error(error, "authentication failed")
      this.router.navigate(['/login'])
    })
  }

  ngAfterViewInit(): void {
    console.log("afterViewInit",this.calender)
  }

  currentEvents: EventApi[] = [];

  calendarOptions: CalendarOptions = {
    plugins: [      
      dayGridPlugin,
      interactionPlugin
    ],

    initialView: 'dayGridMonth',
    // events: this.events, // alternatively, use the `events` setting to fetch from a feed
    events: this.loadEvents.bind(this), // alternatively, use the `events` setting to fetch from a feed
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
      console.log("event>>>>", clickInfo.event.startStr)
      console.info("EventClick", clickInfo.event.extendedProps)
      this.router.navigate(['/detail' ,  clickInfo.event.startStr ,clickInfo.event.extendedProps['day_id']])
  }

  // handleEvents(events: EventApi[]) {
  //   this.currentEvents = events;
  //   this.changeDetector.detectChanges();
  //   this.currentEvents.forEach( e => console.log( e.start))
  // }

  loadEvents(args: EventSourceFuncArg) : Promise<EventInput[]> {
    return new Promise<EventInput[]> ((resolve) => {
      this.apiService.getDays(args.startStr, args.endStr).then(days => {
        var events: EventInput[] = [];
        if (days[0] != null) days.forEach( day => {
          var colorCode = "#3F51B5"
          if (Number(day.calories) > this.target) colorCode = "#FF4081"
          events.push({
            day_id: day.day_id,
            calories: day.calories,
            date: day.date,
            color: colorCode,
          })
          resolve(events)
        })
      }).catch(error => console.error(error))
    })
  }


  logout(){
    this.navService.logout()
  }

}
