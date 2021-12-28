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
      //current user data
      currentUserId: currentUserData.id,
      //UI set up data
      perPage: 3,
      currentPage: 1,
      form: {
        isInLibrary: '',
        Name: '',
        ItemCategory: null,
        isReserved: null
      },
      ItemCategory: [{text: 'Select One', value: null}, 'All', 'Book', 'Movie', 'MusicAlbum', 'Newspaper', 'Archive'],
      //item data
      items: [],
      response: [],
      itemList: [],
      selectedItems: [],
      itemId: '',
      itemName: '',
      itemItemCategory: '',
      itemInLibrary: '',
      errorItem: '',
      //item reservation data
      itemReservation: [],
      itemReservationId: '',
      itemReservationStartDate: '',
      itemReservationEndDate: '',
      //time data
      zeroClock: '00:00:00',

    }
  },
  computed: {
    rows() {
      return this.items.length
    }
  },
  methods: {
    /**
     * Search items by name/itemCategory in "Borrow" Panel
     * @param name
     * @param itemCategory
     */
    findItems: function (name, itemCategory) {
      let nameAndCategory = {
        name: name,
        itemCategory: itemCategory
      }
      let category = {
        itemCategory: itemCategory
      }
      if (name) {
        AXIOS.get('items/findItem', {params: nameAndCategory})
          .then(response => {
            this.itemList = response.data
          })
          .catch(e => {
            this.errorItem = e
          })
      } else {
        AXIOS.get('items/findItemByItemCategory', {params: category})
          .then(response => {
            this.itemList = response.data
          })
          .catch(e => {
            this.errorItem = e
          })
      }

    },
    /**
     * Display all items in the "Borrow" panel
     */
    showAllItems: function () {
      AXIOS.get('/items/itemList')
        .then(response => {
          this.itemList = response.data
        })
        .catch(e => {
          this.errorItem = e
        })
    },
    /**
     * Check out an item(Directly borrow it)
     * @param item
     */
    checkout: function (item) {
      if (item.length > 0) {
        const form_data = new FormData()
        var date = getCurrentTime()[0]
        var time = getCurrentTime()[1]
        console.log(date + time)
        console.log(this.currentUserId)
        form_data.append('pid', parseInt(this.currentUserId))
        form_data.append('itemId', parseInt(item[0].id))
        form_data.append('startDate', date)
        form_data.append('startTime', time)
        AXIOS.post('/itemReservations/checkout', form_data, {})
          .then(response => {
            this.itemReservationList = response.data
            this.$bvModal.msgBoxOk(`Success checkout: ${item[0].name}`)
            AXIOS.get('/items/itemList')
              .then(response => {
                this.itemList = response.data
              })
              .catch(e => {
                this.errorItem = e
              })
          })
          .catch(error => {
            var errorMsg = error.message
            if (errorMsg === "Request failed with status code 500")
              this.errorItem = errorMsg
            this.$bvToast.toast("Fail to borrow the item!", {
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
     * Transfer to Date Selection Panel
     * @param item
     */
    handleReserveChooseDate(item) {
      //do something
      if (item.length > 0) {
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
     * Confirm reserve an item after Date Selection
     * @param startDate
     * @param endDate
     */
    handleReserveConfirm(pid, item, startDate, endDate) {
      const form_data = new FormData()
      form_data.append('pid', parseInt(this.currentUserId))
      form_data.append('itemId', parseInt(item[0].id))
      form_data.append('startDate', startDate)
      form_data.append('startTime', this.zeroClock)
      form_data.append('endDate', endDate)
      form_data.append('endTime', this.zeroClock)
      AXIOS.post('/itemReservations/reserve', form_data, {})
        .then(response => {
          this.itemReservationList = response.data
          this.$bvModal.msgBoxOk(`Success Reserve: ${startDate} ${this.zeroClock} To ${endDate} ${this.zeroClock}`)
        })
        .catch(error => {
          var errorMsg = error.message
          if (errorMsg === "Request failed with status code 500")
            this.errorItem = errorMsg
          this.$bvToast.toast("Item not available at this timeslot!", {
            title: 'Tips',
            autoHideDelay: 2000,
            variant: 'warning',
            solid: true,
            appendToast: false
          });
        })


    },
    linkGen(pageNum) {
      return pageNum === 1 ? '?' : `?page=${pageNum}`
    },
    onRowSelected(items) {
      this.selectedItems = items
    },
    /**
     * Close a panel
     */
    handleCancel() {
      this.$emit('close');
    }
  }
  ,
  created: function () {
    AXIOS.get('/items/itemList')
      .then(response => {
        this.itemList = response.data
      })
      .catch(e => {
        this.errorItem = e
      })
  }

}
