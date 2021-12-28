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
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
      <b-button v-on:click="checkout(selectedItems)" variant="primary">Borrow</b-button>
      <b-button v-on:click="handleReserveChooseDate(selectedItems)" variant="primary">Reserve</b-button>
    </div>
    <!--Setting of the date pickers, used to select a date, are shown when reserving an item-->
    <b-modal id="date-modal" title="Choose Date"
             @ok="handleReserveConfirm(currentUserId,selectedItems,itemReservationStartDate,itemReservationEndDate)">
      <div>
        <b-form>
          <b-form-group
            label="Start Date"
            label-for="datepicker1"
          >
            <b-form-datepicker id="datepicker1" v-model="itemReservationStartDate" class="mb-2"
                               locale="en"></b-form-datepicker>
          </b-form-group>

          <b-form-group
            label="End Date"
            label-for="datepicker2"
          >
            <b-form-datepicker id="datepicker2" v-model="itemReservationEndDate" class="mb-2"
                               locale="en"></b-form-datepicker>
          </b-form-group>
        </b-form>
      </div>
    </b-modal>
  </div>
</template>

<script src="./BorrowJS.js">
</script>

<style scoped>

</style>
