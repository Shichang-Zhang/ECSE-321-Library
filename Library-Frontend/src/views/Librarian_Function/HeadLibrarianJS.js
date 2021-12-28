import event from "./Event"
import item from "./Item"
import businessHour from "./BusinessHour"
import employment from "./Employment";
import user from "./LibrarianUser"

import axios from 'axios'

var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})

export default {
  //Note: the name in components should be associated with import and code data()
  //or use :
  name: "index",
  components: {
    event,
    item,
    businessHour,
    employment,
    user
  },
  //Setup of buttons
  data() {
    return {
      //Set of buttons in head librarian's home page
      headLibrarianHomeSetup: [
        {
          name: 'Event',
          code: 'event'
        },
        {
          name: 'Item',
          code: 'item'
        },
        {
          name: 'BusinessHour',
          code: 'businessHour'
        },
        {
          name: 'Employment',
          code: 'employment'
        },
        {
          name: 'User',
          code: 'user'
        },
        {
          name: 'Exit',
          code: 'exit'
        }
      ],
      curView: '',
      modelTitle: '',
      currentDateAndTime: '',
      error: '',
      currentLibrarianId: decodeURIComponent((new RegExp('[?|&]' + "id" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null,
      currentLibrarianName: '',
    }

  },
  methods: {
    execCommand(item) {
      if (item.code == 'exit') {
        window.location.href = frontendUrl + '/#/'
      }
      this.modelTitle = item.name;
      this.curView = item.code;
      this.$bvModal.show('modal-1');
    }
  },
  created: function () {
    this.currentLibrarianId=decodeURIComponent((new RegExp('[?|&]' + "id" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null,
    AXIOS.get('/librarians/getLibrarianById', {params: {id: this.currentLibrarianId}})
      .then(response => {
        this.currentLibrarianName = response.data.name
      })
      .catch(e => {
        this.error = e.message
        console.log(this.error)
      })

    console.log(this.currentLibrarianId + "!!! ")
    AXIOS.get('/businessHours/getCurrentTime')
      .then(response => {
        this.currentDateAndTime = response.data
      })
      .catch(e => {
        this.error = e.message
        console.log(this.error)
      })
  }
}
