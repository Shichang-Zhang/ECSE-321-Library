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
  name: "reservation",
  data() {
    return {
      //UI set up data
      perPage: 3,
      currentPage: 1,
      selected: [],
      form: {
        isInLibrary: '',
        Name: '',
        ItemCategory: null,
        isReserved: null
      },
      items: [],
      //current user data
      currentUserId:'',
      //Event Registration data
      eventRegistrationList: [],
      eventRegistrationDisplay: [],
      selectedEventRegistration: [],
      //Event Registration Search Data
      selectedEventRegistrationStatus:'',
      eventRegistrationStatusOption:[{text: 'Select One', value: null}, 'All', 'Valid', 'Expired'],
      //Error data
      error: '',
      //time data
      time:'',
      date:''
    }

  },
  computed: {
    rows() {
      return this.items.length
    }
  },
  methods: {
    /**
     * Unregister an event
     * @param eventRegistration
     */
    unregister(eventRegistration) {
      if (eventRegistration.length > 0) {
        let param = {
          pid: parseInt(this.currentUserId),
          eid: parseInt(eventRegistration[0].eventId)
        }
        AXIOS.delete('/eventRegistrations/cancel', {params: param})
          .then(response => {
            this.$bvModal.msgBoxOk(`Success Unregister ${eventRegistration[0].eventName}`)
              .then(value => {
                this.refreshEventRegistration()
              })
              .catch(err => {
                // An error occurred
              })

          })
          .catch(error => {
            var errorMsg = error.message
            if (errorMsg === "Request failed with status code 500")
              this.errorItem = errorMsg
            this.$bvToast.toast("Fail to Unregister the event!", {
              title: 'Tips',
              autoHideDelay: 2000,
              variant: 'warning',
              solid: true,
              appendToast: false
            });
          })
      } else {
        this.$bvToast.toast('No Selected Event Registration', {
          title: 'Tips',
          autoHideDelay: 2000,
          variant: 'warning',
          solid: true,
          appendToast: false
        });
      }
    },
    searchEventRegistrationByStatus(selectedEventRegistrationStatus){
      let currentDate = this.date
      let currentTime = this.time
      if (selectedEventRegistrationStatus == "All") {
        this.refreshEventRegistration()
      } else if (selectedEventRegistrationStatus == "Valid") {
        var validList = []
        for (var index in this.eventRegistrationList) {
          if (this.eventRegistrationList[index].endDate > currentDate) {
            validList.push(this.eventRegistrationList[index])
          } else if (this.eventRegistrationList[index].endDate == currentDate && this.eventRegistrationList[index].endTime >= currentTime) {
            validList.push(this.eventRegistrationList[index])
          }
        }
        this.eventRegistrationDisplay = validList
      } else if (selectedEventRegistrationStatus == "Expired") {
        var expiredList = []
        for (var index in this.eventRegistrationList) {
          if (this.eventRegistrationList[index].endDate < currentDate) {
            expiredList.push(this.eventRegistrationList[index])
          } else if (this.eventRegistrationList[index].endDate == currentDate && this.eventRegistrationList[index].endTime < currentTime) {
            expiredList.push(this.eventRegistrationList[index])
          }
        }
        this.eventRegistrationDisplay = expiredList
      }
    },
    /**
     * refresh item registration in the display table
     */
    refreshEventRegistration(){
      let param = {
        pid: parseInt(this.currentUserId)
      }
      AXIOS.get('/eventRegistrations/getEventRegistrationList/', {params: param})
        .then(response => {
          this.eventRegistrationDisplay = []
          for (var index in response.data) {
            if (response.data[index].personDto.id == this.currentUserId) {
              this.eventRegistrationDisplay.push(
                {
                  eventRegistrationId: response.data[index].id,
                  eventId: response.data[index].eventDto.id,
                  eventName: response.data[index].eventDto.name,
                  startDate: response.data[index].eventDto.timeSlotDto.startDate,
                  startTime: response.data[index].eventDto.timeSlotDto.startTime,
                  endDate: response.data[index].eventDto.timeSlotDto.endDate,
                  endTime: response.data[index].eventDto.timeSlotDto.endTime,
                }
              )
            }
          }
          this.eventRegistrationList = this.eventRegistrationDisplay
        })
        .catch(e => {
          this.error = e
        })
      //Refresh time
      AXIOS.get('/businessHours/getCurrentTime')
        .then(response => {
          this.time=response.data
        })
        .catch(error => {
          console.log(error)
        })
      AXIOS.get('/businessHours/getCurrentDate')
        .then(response => {
          this.date=response.data
        })
        .catch(error => {
          console.log(error)
        })
    },
    linkGen(pageNum) {
      return pageNum === 1 ? '?' : `?page=${pageNum}`
    },
    onRowSelected(items) {
      this.selectedEventRegistration = items
    },
    handleCancel() {
      this.$emit('close');
    }
  },

  created: function () {
    this.currentUserId = decodeURIComponent((new RegExp('[?|&]' + "uid" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    this.refreshEventRegistration()
    this.eventRegistrationList=this.eventRegistrationDisplay
  }
}
