import borrowing from "./Borrow.vue";
import returnAndRenew from "./ReturnAndRenew.vue"
import event from "./Event.vue";
import eventRegistration from "./EventRegisteration.vue"
import updateAccount from "./updateAccount.vue"
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
    borrowing,
    returnAndRenew,
    event,
    eventRegistration,
    updateAccount,
  },
  //Setup of buttons
  data() {
    return {
      //Setting of buttons in the user home page
      userHomeButtonSetup:[
        {
          name:'Borrow',
          code:'borrowing'
        },
        {
          name:'My Items',
          code:'returnAndRenew'
        },
        {
          name:'Event',
          code:'event'
        },
        {
          name:'My Events',
          code:'eventRegistration'
        },
        {
          name: 'Update Account',
          code: 'updateAccount'
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
      currentUserId:decodeURIComponent((new RegExp('[?|&]' + "uid" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null,
      currentUsername:''
    }
  },
  methods:{
    execCommand(item){
      if(item.code=='exit'){
        this.currentUserId=""
        window.location.href = frontendUrl + '/#/'
      }
      this.modelTitle = item.name;
      this.curView = item.code;
      this.$bvModal.show('modal-1');
    }
  },
  created:function (){
    this.currentUserId=decodeURIComponent((new RegExp('[?|&]' + "uid" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    // this.currentUserId=currentUserData.id
    AXIOS.get('/businessHours/getCurrentTime')
      .then(response => {
        this.currentDateAndTime = response.data
      })
      .catch(e => {
        this.error=e.message
        console.log(this.error)
      })

    AXIOS.get('/users/getUserById',{params:{uid:this.currentUserId}})
      .then(response => {
        this.currentUsername = response.data.onlineAccountDto.username
      })
      .catch(e => {
        this.error=e.message
        console.log(this.error)
      })
  }
}
