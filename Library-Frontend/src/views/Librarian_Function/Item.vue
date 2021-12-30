<template>
  <div>
    <!--    Input boxes and buttons in the top of the page-->
    <b-form inline>
      <!--      input of item name-->
      <b-form-group id="input-group-3" label="Name:" style="margin: 0 20px;" label-for="input-1">
        <b-form-input
          id="input-1"
          class="mb-2 mr-sm-2 mb-sm-0"
          placeholder="Book Name"
          v-model="itemName"
        ></b-form-input>
      </b-form-group>
      <!--input of item category-->
      <b-form-group id="input-group-3" label="Item Category" label-for="input-3">
        <b-form-select
          id="input-3"
          v-model="form.ItemCategory"
          :options="ItemCategory"
          required
        ></b-form-select>
      </b-form-group>
      <!--button of finding a item-->
      <b-button v-on:click="findItems(itemName,form.ItemCategory)" variant="primary" style="margin-left: 30px;"
                type="reset">Search
      </b-button>
      <!--button to show all items-->
      <b-button v-on:click="showAllItems" variant="primary" style="margin-left: 30px;" type="reset">Show All</b-button>
    </b-form>
    <!--    Setting of bootstrap table in the middle of the page-->
    <div class="table" style="overflow: auto;height: 51vh;">
      <b-table style="margin-top: 40px;" hover :items="itemList"
               @row-selected="onRowSelected"
               select-mode="single"
               selectable>
      </b-table>
    </div>

    <div style="display: flex;justify-content: flex-end;">
      <b-pagination
        v-model="currentPage"
        :total-rows="rows"
        :per-page="perPage"
        aria-controls="my-table"
      ></b-pagination>
    </div>
    <!--Setting of buttons in the bottom of the page-->
    <div class="btns" style="text-align: right;border-top: #e7e7e7 1px solid;padding-top: 20px">

      <b-button v-on:click="handleCreateItemStep1()" variant="primary">Create New Item</b-button>
      <b-button v-on:click="handleUpdateItemNameStep1(selectedItems)" variant="primary">Update Name</b-button>
      <b-button v-on:click="viewItemReservation(selectedItems)" variant="primary">View Reservation</b-button>
      <b-button v-on:click="deleteItem(selectedItems)" variant="primary">Delete</b-button>
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
    </div>
    <!--Setting of panel of creating an item-->
    <b-modal id="createNewItem" title="Create New Item"
             @ok="handleCreateItemStep2(itemName,itemItemCategory)">
      <div>
        <b-form>
          <b-form-group
            label="Item Name"
            label-for="newItemName"
          >
            <b-form-input
              id="newItemName"
              placeholder="Item Name"
              v-model="itemName"
            ></b-form-input>
          </b-form-group>

          <b-form-group
            label="Item Category"
            label-for="newItemCategory"
          >
            <b-form-select
              id="input-3"
              v-model="itemItemCategory"
              :options="ItemCategoryForCreate"
              required
            ></b-form-select>
          </b-form-group>
        </b-form>
      </div>
    </b-modal>
    <!--Setting of panel of updating an item-->
    <b-modal id="handleUpdateItemName" title="Update Item"
             @ok="handleUpdateItemNameStep2(selectedItems,itemName)">
      <div>
        <b-form>
          <b-form-group
            label="New Item Name"
            label-for="newItemName"
          >
            <b-form-input
              id="newItemName"
              placeholder="Item Name"
              v-model="itemName"
            ></b-form-input>
          </b-form-group>
        </b-form>
      </div>
    </b-modal>
    <!--    Setting of panel of viewing item reservations of an item-->
    <b-modal id="viewItemReservation" title="View Item Reservation of" size="xl">
      <div>Number of Item Reservation Record: {{ numberOfItemReservation }}</div>
      <div>Item Id: {{ selectedItems.length > 0 ? selectedItems[0].id : null }}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
        Item Name: {{ selectedItems.length > 0 ? selectedItems[0].name : null }}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
        Item Category: {{ selectedItems.length > 0 ? selectedItems[0].itemCategory : null }}
      </div>
      <div>
        <b-table style="margin-top: 40px;" hover :items="itemReservationDisplay"
                 @row-selected="onRowSelected"
                 select-mode="single"
                 selectable>
        </b-table>
      </div>
    </b-modal>
  </div>
</template>

<script src="./LibrarianItem_JS.js">
</script>

<style scoped>

</style>
