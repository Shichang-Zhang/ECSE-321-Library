import axios from 'axios'
import currentUserData from './CurrentUserData'
import {getCurrentTime} from './CurrentUserData'

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
      currentUserId: currentUserData.id,
      //event data
      eventList: [],
      eventDisplay: [],
      selectedEvent: [],
      //eventSearch Data
      eventSearchName: '',
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
    handleConfirm() {
      //do something
      if (this.selected.length > 0) {
        this.$bvModal.msgBoxOk(`${this.operate} Success:'${JSON.stringify(this.selected)}`)
          .then(value => {
            this.$emit('close');
          })
          .catch(err => {
            // An error occurred
          })
      } else {
        this.$bvToast.toast('No Selected Items', {
          title: 'Tips',
          autoHideDelay: 3000,
          variant: 'warning',
          solid: true,
          appendToast: false
        });
      }
    },
    /**
     * Register an event
     * @param event
     */
    register: function (event) {
      if (event.length > 0) {
        const form_data = new FormData()
        form_data.append('pid', parseInt(this.currentUserId))
        form_data.append('eid', parseInt(event[0].eventId))
        AXIOS.post('/eventRegistrations/attend', form_data, {})
          .then(response => {
            //this.eventList = response.data
            this.$bvModal.msgBoxOk(`Success Register: ${event[0].name}`)
          })
          .catch(error => {
            var errorMsg = error.message
            if (errorMsg === "Request failed with status code 500")
              this.errorItem = errorMsg
            this.$bvToast.toast("Fail to register the event!", {
              title: 'Tips',
              autoHideDelay: 2000,
              variant: 'warning',
              solid: true,
              appendToast: false
            });
          })

      } else {
        this.$bvToast.toast('No Selected Events', {
          title: 'Tips',
          autoHideDelay: 2000,
          variant: 'warning',
          solid: true,
          appendToast: false
        });
      }
    },
    /**
     * Find events by name
     * @param name name of the events
     */
    findEventsByName: function (name) {
      let param = {
        name: name
      }
      AXIOS.get('/events/findEventByName', {params: param})
        .then(response => {
          this.eventDisplay = []
          for (var index in response.data) {
            this.eventDisplay.push(
              {
                eventId: response.data[index].id,
                name: response.data[index].name,
                startDate: response.data[index].timeSlotDto.startDate,
                startTime: response.data[index].timeSlotDto.startTime,
                endDate: response.data[index].timeSlotDto.endDate,
                endTime: response.data[index].timeSlotDto.endTime,
              }
            )
          }
        })
        .catch(error => {
          var errorMsg = error.message
          if (errorMsg === "Request failed with status code 500")
            this.errorItem = errorMsg
          this.$bvToast.toast("Invalid empty!", {
            title: 'Tips',
            autoHideDelay: 2000,
            variant: 'warning',
            solid: true,
            appendToast: false
          });
        })
    },
    /**
     * Display all items in the "Borrow" panel
     */
    showAllEvents: function () {
      this.eventDisplay = this.eventList
    }
  },
  created: function () {
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
            }
          )
        }
        this.eventList = this.eventDisplay
      })
      .catch(e => {
        this.errorItem = e
      })
  }
}
