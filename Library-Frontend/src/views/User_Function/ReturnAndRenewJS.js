import axios from 'axios'
import currentUserData from './CurrentUserData'
import {getCurrentTime} from './CurrentUserData'

var config = require('../../../config')

var frontendUrl = 'https://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'https://' + config.dev.backendHost + ':' + config.dev.backendPort

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
      itemReservationStatus: [{text: 'Select One', value: null}, 'All', 'Valid', 'Expired'],
      selectedItemReservationStatus: null,
      items: [],
      //current user data
      currentUserId: currentUserData.id,
      //Item Reservation data
      itemReservationList: [],
      itemReservationDisplay: [],
      selectedItemReservation: [],
      renewEndDate: '',
      renewEndTime: '',
      //Error data
      error: '',
      //drop down text
      dropDownTitle: "All",
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
     * Transfer to Date Selection Panel
     * @param itemReservation
     */
    handleRenewChooseDate(itemReservation) {
      //do something
      if (itemReservation.length > 0) {
        this.$bvModal.show('renewItem');
      } else {
        this.toastMessage('No Selected Item Reservation')
      }
    },

    /**
     * Confirm renew an item to a new end date/time
     * @param pid
     * @param itemReservation
     * @param endDate
     * @param endTime
     */
    handleRenewConfirm(pid, itemReservation, endDate, endTime) {
      const form_data = new FormData()
      form_data.append('pid', parseInt(this.currentUserId))
      form_data.append('itemId', parseInt(itemReservation[0].itemId))
      form_data.append('itemReservationId', parseInt(itemReservation[0].reservationId))
      form_data.append('endDate', endDate)
      form_data.append('endTime', endTime)
      AXIOS.put('/itemReservations/renew', form_data, {})
        .then(response => {
          this.refreshMyItem()

          this.$bvModal.msgBoxOk(`Success Renew To: ${this.renewEndDate} ${this.renewEndTime}`)
            .then(value => {
            })
        })
        .catch(error => {
          this.toastMessage("Renew fail! Please check time validity")
        })
    },
    /**
     * Return an item
     * @param itemReservation
     */
    returnItem(itemReservation) {
      if (itemReservation.length > 0) {
        const form_data = new FormData()
        form_data.append('pid', parseInt(this.currentUserId))
        form_data.append('itemId', parseInt(itemReservation[0].itemId))
        form_data.append('itemReservationId', parseInt(itemReservation[0].reservationId))
        this.refreshMyItem()
        form_data.append('endDate', this.date)
        form_data.append('endTime', this.time)
        AXIOS.put('/itemReservations/return', form_data, {})
          .then(response => {
            this.refreshMyItem()
            this.$bvModal.msgBoxOk(`Success Return ${itemReservation[0].itemName}`)
          })
          .catch(error => {
            console.log(this.date+" "+this.time)
            this.toastMessage("Fail to return item!")
          })
      } else {
        this.toastMessage('No Selected Item Reservation')
      }
    },
    /**
     * Show items reservation by its status
     * If its end date and time> current date and time, it's valid
     * If its end date and time=< current date and time, it's expired
     * Or show all of them
     * @param selectedItemReservation
     */
    showItemReservation(selectedItemReservation) {
      let currentDate = this.date
      let currentTime = this.time
      console.log(currentDate,currentTime)
      let param = {
        pid: parseInt(this.currentUserId)
      }
      this.itemReservationDisplay = this.itemReservationList

      if (selectedItemReservation == "All") {
        var allList = []
        for (var index in this.itemReservationDisplay) {
          allList.push(this.itemReservationDisplay[index])
        }
        this.itemReservationDisplay = allList
      } else if (selectedItemReservation == "Valid") {
        var validList = []
        for (var index in this.itemReservationDisplay) {
          if (this.itemReservationDisplay[index].endDate > currentDate) {
            validList.push(this.itemReservationDisplay[index])
          } else if (this.itemReservationDisplay[index].endDate == currentDate && this.itemReservationDisplay[index].endTime >= currentTime) {
            validList.push(this.itemReservationDisplay[index])
          }
        }
        this.itemReservationDisplay = validList
      } else if (selectedItemReservation == "Expired") {
        var expiredList = []
        for (var index in this.itemReservationDisplay) {
          if (this.itemReservationDisplay[index].endDate < currentDate) {
            expiredList.push(this.itemReservationDisplay[index])
          } else if (this.itemReservationDisplay[index].endDate == currentDate && this.itemReservationDisplay[index].endTime < currentTime) {
            expiredList.push(this.itemReservationDisplay[index])
          }
        }
        this.itemReservationDisplay = expiredList
      }
    },


    linkGen(pageNum) {
      return pageNum === 1 ? '?' : `?page=${pageNum}`
    },
    onRowSelected(items) {
      this.selectedItemReservation = items
    },
    handleCancel() {
      this.$emit('close');
    },
    handleConfirm() {
      console.log(this.selected, this.startTime, this.endTime);
      this.refreshMyItem()
      this.$bvModal.msgBoxOk(`Success Renew: To  ${this.endDate} ${this.endTime}`)
    },
    /**
     * Cancel an item reservation
     * @param itemReservation selected item reservation transfer object
     */
    cancelItemReservation(itemReservation) {
      var currentDate = getCurrentTime()[0]
      var currentTime = getCurrentTime()[1]
      if (itemReservation.length > 0) {
        if (itemReservation[0].startDate <= currentDate) {
          this.toastMessage("Fail to cancel the reservation! Please checkout/return first!")
        } else {
          let param = {
            pid: parseInt(this.currentUserId),
            itemId: parseInt(itemReservation[0].itemId),
            itemReservationId: parseInt(itemReservation[0].reservationId)
          }
          AXIOS.delete('/itemReservations/cancelReservation', {params: param})
            .then(response => {
              this.refreshMyItem()
              this.$bvModal.msgBoxOk(`Success cancel the reservation of ${itemReservation[0].itemName}`)
            })
            .catch(error => {
              this.toastMessage("Fail to cancel the reservation!")
            })
        }

      } else {
        this.toastMessage('No Selected Item Reservation')
      }
    },
    /**
     * Refresh my item display table
     */
    refreshMyItem(){
      this.currentUserId = decodeURIComponent((new RegExp('[?|&]' + "uid" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
      this.itemReservationDisplay = []
      let param = {
        pid: parseInt(this.currentUserId)
      }
      AXIOS.get('/itemReservations/getItemReservationList', {params: param})
        .then(response => {
          for (var index in response.data) {
            if (response.data[index].personDto.id == this.currentUserId) {
              this.itemReservationDisplay.push(
                {
                  reservationId: response.data[index].id,
                  itemId: response.data[index].itemDto.id,
                  itemName: response.data[index].itemDto.name,
                  itemCategory: response.data[index].itemDto.itemCategory,
                  startDate: response.data[index].timeSlotDto.startDate,
                  startTime: response.data[index].timeSlotDto.startTime,
                  endDate: response.data[index].timeSlotDto.endDate,
                  endTime: response.data[index].timeSlotDto.endTime
                }
              )
            }
          }
          this.itemReservationList = this.itemReservationDisplay
          this.selectedItemReservationStatus="All"
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
    this.currentUserId = decodeURIComponent((new RegExp('[?|&]' + "uid" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    this.refreshMyItem()
  }
}
