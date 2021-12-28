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
      dropDownTitle: "All"
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
        this.$bvModal.show('date-modal');
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
     * Confirm renew an item to a new end date/time
     * @param startDate
     * @param endDate
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


          this.$bvModal.msgBoxOk(`Success Renew To: ${this.renewEndDate} ${this.renewEndTime}`)
            .then(value => {
              this.$emit('close');
            })

        })
        .catch(error => {
          var errorMsg = error.message
          if (errorMsg === "Request failed with status code 500")
            this.errorItem = errorMsg
          this.$bvToast.toast("Renew fail! Please check time", {
            title: 'Tips',
            autoHideDelay: 2000,
            variant: 'warning',
            solid: true,
            appendToast: false
          });
        })


    },
    /**
     * Return an item
     * @param itemReservation
     */
    returnItem(itemReservation) {
      if (itemReservation.length > 0) {
        const form_data = new FormData()
        var date = getCurrentTime()[0]
        var time = getCurrentTime()[1]
        form_data.append('pid', parseInt(this.currentUserId))
        form_data.append('itemId', parseInt(itemReservation[0].itemId))
        form_data.append('itemReservationId', parseInt(itemReservation[0].reservationId))
        form_data.append('endDate', date)
        form_data.append('endTime', time)
        AXIOS.put('/itemReservations/return', form_data, {})
          .then(response => {
            this.$bvModal.msgBoxOk(`Success Return ${itemReservation[0].itemName}`)
              .then(value => {
                this.$emit('close');
              })
              .catch(err => {
                // An error occurred
              })
            // setTimeout(() => { this.$emit('close'); }, 2000)
          })
          .catch(error => {
            var errorMsg = error.message
            if (errorMsg === "Request failed with status code 500")
              this.errorItem = errorMsg
            this.$bvToast.toast("Fail to return the item!", {
              title: 'Tips',
              autoHideDelay: 2000,
              variant: 'warning',
              solid: true,
              appendToast: false
            });
          })
      } else {
        this.$bvToast.toast('No Selected Items', {
          title: 'Tips',
          autoHideDelay: 2000,
          variant: 'warning',
          solid: true,
          appendToast: false
        });
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
      var currentDate = getCurrentTime()[0]
      var currentTime = getCurrentTime()[1]
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
      this.$bvModal.msgBoxOk(`Success Renew: To  ${this.endDate} ${this.endTime}`)
        .then(value => {
          this.$emit('close');
        })
        .catch(err => {
          // An error occurred
        })
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
          this.$bvToast.toast("Fail to cancel the reservation! Please checkout/return first!", {
            title: 'Tips',
            autoHideDelay: 2000,
            variant: 'warning',
            solid: true,
            appendToast: false
          });
        } else {
          let param = {
            pid: parseInt(this.currentUserId),
            itemId: parseInt(itemReservation[0].itemId),
            itemReservationId: parseInt(itemReservation[0].reservationId)
          }
          AXIOS.delete('/itemReservations/cancelReservation', {params: param})
            .then(response => {
              this.$bvModal.msgBoxOk(`Success cancel the reservation of ${itemReservation[0].itemName}`)
                .then(value => {
                  this.$emit('close');
                })
                .catch(err => {
                  // An error occurred
                })

            })
            .catch(error => {
              var errorMsg = error.message
              if (errorMsg === "Request failed with status code 500")
                this.errorItem = errorMsg
              this.$bvToast.toast("Fail to cancel the reservation!", {
                title: 'Tips',
                autoHideDelay: 2000,
                variant: 'warning',
                solid: true,
                appendToast: false
              });
            })
        }

      } else {
        this.$bvToast.toast('No Selected Item Reservation', {
          title: 'Tips',
          autoHideDelay: 2000,
          variant: 'warning',
          solid: true,
          appendToast: false
        });
      }
    }
  },
  created: function () {
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
      })
      .catch(e => {
        this.error = e
      })
  }
}
