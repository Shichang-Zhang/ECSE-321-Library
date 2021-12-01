import event from "../LibrarianComponents/Event"
import item from "../LibrarianComponents/Item"
import businessHour from "../LibrarianComponents/BusinessHour"
import employment from "../LibrarianComponents/Employment";
import user from "../LibrarianComponents/User"
import currentLibrarianData from "../librarian-side/LibrarianMenu"

import axios from 'axios'
var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  //Note: the name in components should be associated with import and code data()
  //or use :
  name: "index",
  components:{
    event,
    item,
    businessHour,
    employment,
    user
  },
  //Setup of buttons
  data() {
    return {
      headLibrarianHomeSetup:[
        {
          name:'Event',
          code:'event'
        },
        {
          name:'Item',
          code:'item'
        },
        {
          name:'BusinessHour',
          code:'businessHour'
        },
        {
          name:'Employment',
          code:'employment'
        },
        {
          name:'User',
          code:'user'
        },
        {
          name:'Exit',
          code:'exit'
        }
      ],
      NormalLibrarianHomeSetup:[
        {
          name:'Event',
          code:'event'
        },
        {
          name:'Item',
          code:'item'
        },
        {
          name:'User',
          code:'user'
        },
        {
          name:'Exit',
          code:'exit'
        }
      ],
      curView:'',
      modelTitle:'',
      currentDateAndTime:'',
      error:'',
      currentLibrarianId:decodeURIComponent((new RegExp('[?|&]' + "id" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null,
      currentLibrarianName:'',
    }

  },
  methods:{
    execCommand(item){
      if(item.code=='exit'){
        window.location.href = frontendUrl + '/#/'
      }
      this.modelTitle = item.name;
      this.curView = item.code;
      this.$bvModal.show('modal-1');
    }
  },
  created:function (){

    AXIOS.get('/librarians/getLibrarianById',{params:{id:this.currentLibrarianId}})
      .then(response => {
        this.currentLibrarianName = response.data.name
      })
      .catch(e => {
        this.error=e.message
        console.log(this.error)
      })

    console.log(this.currentLibrarianId+"!!! ")
    AXIOS.get('/businessHours/getCurrentTime')
      .then(response => {
        this.currentDateAndTime = response.data
      })
      .catch(e => {
        this.error=e.message
        console.log(this.error)
      })
  }
}
