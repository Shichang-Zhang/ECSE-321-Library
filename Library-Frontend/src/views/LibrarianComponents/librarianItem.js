import axios from 'axios'
var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})
function ItemDto (name, id, type) {
this.name=name
this.id=id
this.type=type
}
 export default{
  name: 'Items',
  data () {
    return {
      items: [],
       input1: {
        name: '',
		    ItemCategory: '',
      },
        input2: {
		    id: '',
      },
      input3: {
        name: '',
		    id: '',
      },
      errorItem: '',
	    response: [],
	  ItemCategory: [{ text: 'Select One', value: null }, 'Book', 'Movie', 'MusicAlbum', 'Newspaper', 'Archive'],
    }
  },
	methods:{
   gotoEvent(){
   this.$router.push('/librarianEvent');
   },
   gotoItem(){
   this.$router.push('/librarianItem');
   },
  createItem: function (itemName,itemType) {
      AXIOS.post('/items/createItem?name='+itemName+'&itemCategory='+itemType)
        .then(response => {
            this.items.push(response.data)
        })
        .catch(e => {
          this.errorItem= e
        })
  },
    updateItem: function (itemid,itemName) {
      AXIOS.put('/items/updateItem?id='+itemid+'&name='+itemName)
        .then(response => {
          AXIOS.get('/items/itemList')
            .then(response => {
              this.items = response.data
            })
            .catch(e => {
              this.errorItem = e
            })
        })
        .catch(e => {
          this.errorItem= e
        })
  },
    deleteItem: function (itemid) {
      AXIOS.delete('/items/deleteItem?id='+itemid)
        .then(response => {
          AXIOS.get('/items/itemList')
            .then(response => {
              this.items = response.data
            })
            .catch(e => {
              this.errorItem = e
            })
        })
        .catch(e => {
          this.errorItem= e
        })
  },  handleCancel(){
          this.$emit('close');
        },
 },

 created: function () {
        AXIOS.get('/items/itemList')
        .then(response => {
          this.items = response.data
        })
        .catch(e => {
          this.errorItem = e
        })

  }


 }
