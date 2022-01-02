import axios from 'axios'
var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})


export default {
  currentLibrarianId: decodeURIComponent((new RegExp('[?|&]' + "id" + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null,
  data() {
    return {
      form: {
        id: '',
        headLibrarian: '',
        error:''
      },
      show: true,
    }
  },
  methods: {
    /**
     * submit the data to signUp
     * @param event
     */
    onSubmit(event) {
      event.preventDefault()
      this.logIn(this.form.id)
    },

    /**
     * reset the form
     * @param event
     */
    onReset(event) {
      event.preventDefault()
      // Reset our form values
      this.form.id = ''
      this.error=''
      // Trick to reset/clear native browser form validation state
      this.show = false
      this.$nextTick(() => {
        this.show = true
      })
    },
    /**
     * librarian logs in with its id
     * @param id id of the librarian
     */
    logIn:function(id){
      const form_data=new FormData()
      form_data.append('id',id)
      AXIOS.get('/librarians/getLibrarianById?id='+id)//'/librarians/librarianList',form_data,{})
        .then(response => {
          this.form.error=''
          // this.$router.push('/user-side?uid='+response.data.id)
          if(response.data.headLibrarian){
            window.location.href = frontendUrl + '/#/headLibrarian?id=' + response.data.id
          }else{
            window.location.href = frontendUrl + '/#/librarian?id=' + response.data.id
          }
        })
        .catch(e => {
          this.form.error = "invalid id"
        })
    },
    gotoStart(){
      this.$router.push('/');
    }
  },
  created:function (){

  }

}
