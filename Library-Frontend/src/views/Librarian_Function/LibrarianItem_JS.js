import axios from 'axios'

var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: {'Access-Control-Allow-Origin': frontendUrl}
})
export default {
  name: 'Items',
  data() {
    return {
      items: [],
      //data used to create an item
      createItemInput: {
        name: '',
        ItemCategory: '',
      },
      updateItemInput: {
        name: '',
        id: '',
      },
      errorItem: '',
      response: [],
      ItemCategory: [{text: 'Select One', value: null}, 'Book', 'Movie', 'MusicAlbum', 'Newspaper', 'Archive'],
    }
  },
  methods: {
    gotoEvent() {
      this.$router.push('/librarianEvent');
    },
    /**
     * Create a new item
     * @param itemName item's name
     * @param itemCategory item's category
     */
    createItem: function (itemName, itemCategory) {
      AXIOS.post('/items/createItem?name=' + itemName + '&itemCategory=' + itemCategory)
        .then(response => {
          this.items.push(response.data)
        })
        .catch(e => {
          this.errorItem = e
        })
    },
    /**
     * Update of an item's name
     * @param itemId id of the item you want to update
     * @param itemName new name of the item updated to
     */
    updateItem: function (itemId, itemName) {
      AXIOS.put('/items/updateItem?id=' + itemId + '&name=' + itemName)
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
          this.errorItem = e
        })
    },
    /**
     * Delete an item by its id
     * @param itemId id of the item
     */
    deleteItem: function (itemId) {
      AXIOS.delete('/items/deleteItem?id=' + itemId)
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
          this.errorItem = e
        })
    }, handleCancel() {
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
