import axios from 'axios'

var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})

export default {
  name: "bookList",
  data() {
    return {
      //UI set up data
      perPage: 3,
      currentPage: 1,

      form: {
        isInLibrary: '',
        Name: '',
        ItemCategory: null,
        isReserved: null
      },
      ItemCategory: [{text: 'Select One', value: null}, 'Book', 'Movie', 'MusicAlbum', 'Newspaper', 'Archive'],
      items: [],
      //current user data
      currentLibrarianId: '',
      //event data
      eventList: [],
      eventDisplay: [],
      selectedEvent: [],
      //event search
      eventSearchName:'',
      //create event data
      newEventName:'',
      newEventStartDate:'',
      newEventStartTime:'',
      newEventEndDate:'',
      newEventEndTime:'',
      //event registration display
      eventRegistrationDisplay:[],
      numberOfAttendee:'',
      //error data
      error: '',
    }
  },
  computed: {
    rows() {
      return this.items.length
    }
  },
  methods: {
    //page setup methods
    linkGen(pageNum) {
      return pageNum === 1 ? '?' : `?page=${pageNum}`
    },
    onRowSelected(items) {
      this.selectedEvent = items;
    },
    handleCancel() {
      this.$emit('close');
    },
    /**
     * Step 1 of create a new Event:
     * The panel of inputting name, start date, start time, end date, end time shows up
     */
    handleCreateStep1(){
        this.$bvModal.show('createNewEventPanel');
    },
    /**
     *
     * Step 2 of create a new Event:
     * Click OK to create
     *
     * @param name name of the event
     * @param startTime start time of the event
     * @param startDate start date of the event
     * @param endTime end time of the event
     * @param endDate end date of the event
     */
    handleCreateStep2(name, startTime, startDate, endTime, endDate){
      AXIOS.post('/events/createEvent?name=' + name + '&startDate=' + startDate + '&endDate=' + endDate + '&startTime=' + startTime + '&endTime=' + endTime)
        .then(response => {
          this.refreshEvent()
          this.toastMessage("Create successfully")
        })
        .catch(e => {
          this.toastMessage("Fail to create, please check name and time slot validity")
        })
    },
    /**
     * Step 1 of updating event name
     * Select an event and click update, the panel of inputting new name of the event shows up
     * @param selectedEvent The event to be updated
     */
    handleUpdateNameStep1(selectedEvent){
      console.log(selectedEvent.length)
      if(selectedEvent.length>0){
        this.$bvModal.show('updateNamePanel');
      }else{
        this.toastMessage("No Selected Event")
      }
    },
    /**
     * Step 2 of updating event name
     * Click OK to update
     * @param selectedEvent the event we want to update
     * @param name new name the event updates to
     */
    handleUpdateNameStep2(selectedEvent,name){
      const form_data=new FormData()
      form_data.append('id',selectedEvent[0].eventId)
      form_data.append('name',name)
      AXIOS.put('/events/updateEventName/',form_data,{})
        .then(response => {
          this.refreshEvent()
          this.toastMessage("Update successfully")
        })
        .catch(e => {
          this.toastMessage("Fail to update name!")
        })
    },
    /**
     * Step 1 of updating the time slot of an event
     * @param selectedEvent the event we want to update
     */
    handleUpdateTimeSlotStep1(selectedEvent){
      console.log(selectedEvent.length)
      if(selectedEvent.length>0){
        this.$bvModal.show('updateTimePanel');
      }else{
        this.toastMessage("No Selected Event")
      }
    },
    /**
     * Step 2 of updating the time slot of an event
     * @param selectedEvent the event we want to update
     * @param startTime start time of the event
     * @param startDate start date of the event
     * @param endTime end time of the event
     * @param endDate end date of the event
     */
    handleUpdateTimeSlotStep2(selectedEvent,startDate,startTime,endDate,endTime){
      const form_data=new FormData()
      form_data.append('id',selectedEvent[0].eventId)
      form_data.append('startDate',startDate)
      form_data.append('endDate',endDate)
      form_data.append('startTime',startTime)
      form_data.append('endTime',endTime)
      AXIOS.put('/events/updateEventTimeSlot/',form_data,{})
        .then(response => {
          this.refreshEvent()
          this.toastMessage("Update successfully")
        })
        .catch(e => {
          this.toastMessage("Fail to update event, please check time slot validity")
        })

    },
    /**
     * Delete Event
     * @param selectedEvent the event we want to delete
     */
    deleteEvent(selectedEvent){
      if(selectedEvent.length>0){
        AXIOS.delete('/events/deleteEvent?id=' + selectedEvent[0].eventId)
          .then(response => {
            this.refreshEvent()
            this.toastMessage("Delete successfully")
          })
          .catch(e => {
            this.toastMessage("Fail to delete!")
          })
      }else{
        this.toastMessage("No Selected Event")
      }
    },
    /**
     * Find event by name
     * @param name name of the event
     */
    findEventsByName(name){
      if(name.length==0){
        this.refreshEvent()
        return
      }
      this.eventDisplay=[]
      AXIOS.get('events/findEventByName?name='+name)
        .then(response => {
          for (var index in response.data) {
            this.eventDisplay.push(
              {
                eventId: response.data[index].id,
                name: response.data[index].name,
                startDate: response.data[index].timeSlotDto.startDate,
                startTime: response.data[index].timeSlotDto.startTime,
                endDate: response.data[index].timeSlotDto.endDate,
                endTime: response.data[index].timeSlotDto.endTime,
                // eventRegistrationNumber:
              }
            )
          }
        })
        .catch(e => {
          this.errorItem = e
        })

    },
    viewEventRegistrationStep1(selectedEvent){
      if(selectedEvent.length>0){
        this.eventRegistrationDisplay=[]
        AXIOS.get('eventRegistrations/getPersonByEvent?eid='+selectedEvent[0].eventId)
          .then(response => {
            for (var index in response.data) {
              this.eventRegistrationDisplay.push(
                {
                  userId: response.data[index].id,
                  userName: response.data[index].name,
                  userAddress: response.data[index].address
                }
              )
            }
          })
          .catch(e => {
            this.errorItem = e
          })
        AXIOS.get('eventRegistrations/getParticipantsNumber?eid='+selectedEvent[0].eventId)
          .then(response => {
            this.numberOfAttendee=response.data
          })
          .catch(e => {
            this.errorItem = e
          })
        this.$bvModal.show("viewAttendees")
      }else{
        this.toastMessage("No Selected Event")
      }
    },
    /**
     * Refresh event in display table
     */
    refreshEvent(){
      this.newEventName=''
      this.newEventStartDate=''
      this.newEventStartTime=''
      this.newEventEndDate=''
      this.newEventEndTime=''
      this.eventDisplay = []
      AXIOS.get('/events/eventList')
        .then(response => {
          for (var index in response.data) {
            this.eventDisplay.push(
              {
                eventId: response.data[index].id,
                name: response.data[index].name,
                startDate: response.data[index].timeSlotDto.startDate,
                startTime: response.data[index].timeSlotDto.startTime,
                endDate: response.data[index].timeSlotDto.endDate,
                endTime: response.data[index].timeSlotDto.endTime,
                // eventRegistrationNumber:
              }
            )
          }
          this.eventList = this.eventDisplay
        })
        .catch(e => {
          this.errorItem = e
        })
    },
    toastMessage(content){
      this.$bvToast.toast(content, {
        title: 'Tips',
        autoHideDelay: 2000,
        variant: 'warning',
        solid: true,
        appendToast: false
      });
    }
  },
  created: function () {
    this.currentLibrarianId = decodeURIComponent((new RegExp('[?|&]' + "id" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    this.refreshEvent()
  }
}
