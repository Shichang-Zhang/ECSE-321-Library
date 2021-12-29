import axios from 'axios'
import librarianEvent_JS from "./LibrarianEvent_JS";

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
      ItemCategoryForCreate: [{text: 'Select One', value: null}, 'Book', 'Movie', 'MusicAlbum', 'Newspaper', 'Archive'],
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
     * Display all items in the Item table
     */
    showAllItems: function () {
      this.refreshItem()
    },
    /**
     * Refresh Item Display Table
     */
    refreshItem(){
      this.itemList=[]
      this.itemName=''
      this.itemItemCategory=''
      this.itemId=''
      AXIOS.get('/items/itemList')
        .then(response => {
          this.itemList = response.data
        })
        .catch(e => {
          this.errorItem = e
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
    handleCreateItemStep1(){
      this.$bvModal.show('createNewItem');
    },
    handleCreateItemStep2(itemName,itemCategory){
      AXIOS.post('/items/createItem?name=' + itemName + '&itemCategory=' + itemCategory)
        .then(response => {
          this.refreshItem()
          this.toastMessage("Create successfully")
        })
        .catch(e => {
          if(itemCategory){
            this.toastMessage("Fail to create")
          }else{
            this.toastMessage("Please select an item category")
          }
        })
    },
    handleUpdateItemNameStep1(selectedItems){
      if(selectedItems.length>0){
        this.$bvModal.show('handleUpdateItemName');
      }else{
        this.toastMessage("No Selected Item")
      }
    },
    handleUpdateItemNameStep2(selectedItems,itemName){
      console.log(selectedItems[0].itemId)
      AXIOS.put('/items/updateItem?id=' +selectedItems[0].id  + '&name=' + itemName)
        .then(response => {
          this.refreshItem()
          this.toastMessage("Update successfully")
        })
        .catch(e => {
          this.toastMessage("Fail to update")
        })
    },
    deleteItem(selectedItems){
      if(selectedItems.length>0){
        AXIOS.delete('/items/deleteItem?id=' + selectedItems[0].id)
          .then(response => {
            this.refreshItem()
            this.toastMessage("Delete successfully")
          })
          .catch(e => {
            this.toastMessage("Fail to delete")
          })
      }else{
        this.toastMessage("No Selected Item")
      }

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
    this.refreshItem()
  }

}
