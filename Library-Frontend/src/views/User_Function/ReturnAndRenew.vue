<template>
  <div>
    <!--    Setting of selecting form in the top of the page-->
    <b-form inline style="margin: 15px 0">
      <b-form-group id="input-group-3" label="Item Reservation Status:" label-for="input-1">
        <b-form-select
          id="input-3"
          v-model="selectedItemReservationStatus"
          :options="itemReservationStatus"
          required
        ></b-form-select>

      </b-form-group>

      <b-button v-on:click="showItemReservation(selectedItemReservationStatus)" variant="primary" style="margin: 0 10px">Show</b-button>

    </b-form>
    <!--    Setting of the bootstrap table in the middle of the page-->
    <div class="table" style="overflow: auto;height: 49vh;">
      <b-table hover :items="itemReservationDisplay"
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
    <!--Setting of the buttons in the bottom of the page-->
    <div class="btns" style="text-align: right;padding-top: 10px">
      <b-button variant="primary" @click="handleRenewChooseDate(selectedItemReservation)" style="margin-left: 30px;">Renew</b-button>
      <b-button v-on:click="returnItem(selectedItemReservation)" variant="primary">Return</b-button>
      <b-button v-on:click="cancelItemReservation(selectedItemReservation)" variant="primary">Cancel</b-button>
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
    </div>
    <!--Setting of the date and time pickers, which are used to select time and date when renewing an item-->
    <b-modal id="renewItem" title="Choose Date"
             @ok="handleRenewConfirm(currentUserId,selectedItemReservation,renewEndDate,renewEndTime)">
      <div>
        <b-form>
          <b-form-group
            label="Extended End Date："
            label-for="datepicker1"
          >
            <b-form-datepicker id="datepicker1" v-model="renewEndDate" class="mb-2" locale="en"></b-form-datepicker>
          </b-form-group>
          <b-form-group
            label="Extended End Time:"
            label-for="endTime"
          >
            <b-form-timepicker v-model="renewEndTime" locale="en" id="endTime" :hour12="false"></b-form-timepicker>
          </b-form-group>
        </b-form>
      </div>
    </b-modal>
  </div>
</template>

<script src="./ReturnAndRenewJS.js">
</script>

<style scoped>
</style>
