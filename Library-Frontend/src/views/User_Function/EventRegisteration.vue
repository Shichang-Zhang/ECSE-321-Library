<template>
  <div>
    <b-form inline style="margin: 15px 0">
      <b-form-group id="input-group-3" label="Event Registration Status: " label-for="input-1">
        <b-form-select
          id="input-3"
          v-model="selectedEventRegistrationStatus"
          :options="eventRegistrationStatusOption"
          required
        ></b-form-select>
        </b-form-group>

      <b-button v-on:click="searchEventRegistrationByStatus(selectedEventRegistrationStatus)" variant="primary" style="margin: 0 10px">Show</b-button>

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
