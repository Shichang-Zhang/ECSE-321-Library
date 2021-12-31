<template>
  <div>
    <b-form inline style="margin: 15px 0">
      <b-form-select
        id="input-3"
        v-model="selectedItemReservationStatus"
        :options="itemReservationStatus"
        required
      ></b-form-select>
      <b-button v-on:click="showItemReservation(selectedItemReservationStatus)" variant="primary" style="margin: 0 10px">Show</b-button>

    </b-form>
    <!--    Setting of the table of Bootstrap table in the middle of the page-->
    <div class="table" style="overflow: auto;height: 49vh;">
      <b-table hover :items="eventRegistrationDisplay"
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
    <div class="btns" style="text-align: right;margin-top: 10px">
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
      <b-button @click="unregister(selectedEventRegistration)" variant="primary">Unregister</b-button>
    </div>

  </div>
</template>

<script src="./EventRegistrationJS.js">
</script>

<style scoped>
</style>
