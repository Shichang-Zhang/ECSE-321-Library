<template>
  <div class="Start">
    <!-- input boxes in the top of the page    -->
    <b-form inline>
      <b-form-group id="input-group-3" style="margin: 0px;" label-for="input-1">
        <b-form-input
          id="input-1"
          class="mb-2 mr-sm-2 mb-sm-0"
          placeholder="DayOfWeek"
          v-model="businessHourDayOfWeek"
        ></b-form-input>
        <b-form-timepicker size="sm" v-model="businessHourStartTime" placeholder="StartDate"></b-form-timepicker>
        <b-form-timepicker size="sm" v-model="businessHourEndTime" placeholder="EndDate"></b-form-timepicker>
      </b-form-group>
    </b-form>
    <!--    Setting of the bootstrap table in the middle of the page-->
    <div class="table" style="overflow: auto;height: 45vh;">
      <b-table style="margin-top: 10px;" hover :items="businessHours"
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
    <!-- Buttons in the bottom of the page-->
    <div class="btns" style="text-align: right;border-top: #e7e7e7 1px solid;padding-top: 10px">
      <b-button v-on:click="createBusinessHour(businessHourDayOfWeek,businessHourStartTime,businessHourEndTime)"
                variant="primary" style="margin-left: 10px;" type="reset">create
      </b-button>
      <b-button v-on:click="updateBusinessHour(businessHourDayOfWeek,businessHourStartTime,businessHourEndTime)"
                variant="primary" style="margin-left: 10px;margin-right: 10px" type="reset">update
      </b-button>
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
    </div>
  </div>
</template>
<script src="./LibrarianBusinessHour_JS.js">
</script>
<style scoped>
</style>
