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
  name: "bookList",
  data() {
    return {
      //current user data
      currentUserId:'',
      //UI set up data
      perPage: 3,
      currentPage: 1,
      form: {
        isInLibrary: '',
        Name: '',
        ItemCategory: null,
        isReserved: null
      },
      ItemCategory: [{text: 'Select One', value: null}, 'All','Book', 'Movie', 'MusicAlbum', 'Newspaper', 'Archive'],
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
      date:'',
      time:'',
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
      let onlyCategory = {
        itemCategory: itemCategory
      }
      let onlyName = {
        name:name
      }
      //Case when both name and item category are null
      if(name.length==0 && itemCategory==null){
        return
        //Case when name is null
      }else if(name.length==0){
        //Display all
        if(itemCategory=="All" || itemCategory == null){
          this.refreshItem()
        }else{
          //Find item only by category
          AXIOS.get('items/findItemByItemCategory', {params: onlyCategory})
            .then(response => {
              this.itemList = response.data
            })
            .catch(e => {
              this.errorItem = e
            })
        }
        //Case when name is not null
      }else {
        if (itemCategory == "All" || itemCategory == null) {
          //Find item only by its name
          AXIOS.get('items/findItemByName', {params: onlyName})
            .then(response => {
              this.itemList = response.data
            })
            .catch(e => {
              this.errorItem = e
            })
        } else {
          //Find item by name and category
          AXIOS.get('items/findItem', {params: nameAndCategory})
            .then(response => {
              this.itemList = response.data
            })
            .catch(e => {
              this.errorItem = e
            })
        }
      }
      this.itemName=''
      this.form.ItemCategory=null

    },
    /**
     * Display all items in the "Borrow" panel
     */
    showAllItems: function () {
      this.refreshItem()
    },
    /**
     * Check out an item(Directly borrow it)
     * @param item
     */
    checkout: function (item) {
      if (item.length > 0) {
        const form_data = new FormData()
        // console.log(this.date + this.time)
        // console.log(this.currentUserId)
        form_data.append('pid', parseInt(this.currentUserId))
        form_data.append('itemId', parseInt(item[0].id))
        form_data.append('startDate', this.date)
        form_data.append('startTime', this.time)
        AXIOS.post('/itemReservations/checkout', form_data, {})
          .then(response => {
            this.$bvModal.msgBoxOk(`Success checkout: ${item[0].name}`)
            this.refreshItem()
          })
          .catch(error => {
            var errorMsg = error.message
            if (errorMsg === "Request failed with status code 500")
              this.errorItem = errorMsg
            this.toastMessage("Fail to borrow the item!")
          })

      } else {
        this.toastMessage('No Selected Items')
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
        this.toastMessage("No Selected Item")
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
          this.refreshItem()
          this.$bvModal.msgBoxOk(`Success Reserve: ${startDate} ${this.zeroClock} To ${endDate} ${this.zeroClock}`)
        })
        .catch(error => {
          var errorMsg = error.message
          if (errorMsg === "Request failed with status code 500")
            this.errorItem = errorMsg
          this.toastMessage("Item not available at this timeslot!")
        })


    },
    /**
     * Refresh Item Display Table
     */
    refreshItem(){
      AXIOS.get('/items/itemList')
        .then(response => {
          this.itemList = response.data
        })
        .catch(e => {
          this.errorItem = e
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
    this.currentUserId = decodeURIComponent((new RegExp('[?|&]' + "uid" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    this.refreshItem()
  }

}
