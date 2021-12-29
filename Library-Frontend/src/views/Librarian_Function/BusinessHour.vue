<template>
  <div class="Start">
    <!-- input boxes in the top of the page    -->
<!--    <b-form inline>-->

<!--    </b-form>-->
    <!--    Setting of the bootstrap table in the middle of the page-->
    <div class="table" style="overflow: auto;height: 55vh;">
      <b-table style="margin-top: 40px;" hover :items="businessHours"
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
    <div class="btns" style="text-align: right;border-top: #e7e7e7 1px solid;padding-top: 20px">
      <b-button variant="primary" @click="handleCreateBusinessHourStep1" type="reset">Create</b-button>
      <b-button variant="primary" @click="handleUpdateBusinessHourStep1(selectedBusinessHours)"  type="reset">Update</b-button>
      <b-button variant="outline-primary" @click="handleCancel">Close</b-button>
    </div>

    <!--Setting of panel of creating new business hour-->
    <b-modal id="createNewBusinessHour" title="Create New Business Hour"
             @ok="handleCreateBusinessHourStep2(businessHourDayOfWeek,businessHourStartTime,businessHourEndTime)">
      <div>
        <b-form>
          <b-form-group
            label="Day Of Week"
            label-for="businessHourDayOfWeek"
          >
            <b-form-select
              id="businessHourDayOfWeek"
              v-model="businessHourDayOfWeek"
              :options="dayOfWeekOptions"
              required
            ></b-form-select>
          </b-form-group>

          <b-form-group
            label="Start Time"
            label-for="businessHourStartTime"
          >
            <b-form-timepicker id="businessHourStartTime" v-model="businessHourStartTime" class="mb-2" locale="en"></b-form-timepicker>
          </b-form-group>

          <b-form-group
            label="End Time"
            label-for="businessHourEndTime"
          >
            <b-form-timepicker id="businessHourEndTime" v-model="businessHourEndTime" class="mb-2" locale="en"></b-form-timepicker>
          </b-form-group>
        </b-form>
      </div>
    </b-modal>
    <!--Setting of panel of updating business hour-->
    <b-modal id="updateBusinessHour" title="Update Business Hour"
             @ok="handleUpdateBusinessHourStep2(selectedBusinessHours,businessHourStartTime,businessHourEndTime)">
      <div>
        <b-form>
          <b-form-group
            label="Updated Day Of Week:"
            label-for="businessHourDayOfWeek"
          >
            {{selectedBusinessHours.length>0?selectedBusinessHours[0].dayOfWeek:null}}
          </b-form-group>

          <b-form-group
            label="Start Time"
            label-for="businessHourStartTime"
          >
            <b-form-timepicker id="businessHourStartTime" v-model="businessHourStartTime" class="mb-2" locale="en"></b-form-timepicker>
          </b-form-group>

          <b-form-group
            label="End Time"
            label-for="businessHourEndTime"
          >
            <b-form-timepicker id="businessHourEndTime" v-model="businessHourEndTime" class="mb-2" locale="en"></b-form-timepicker>
          </b-form-group>
        </b-form>
      </div>
    </b-modal>
  </div>
</template>
<script src="./LibrarianBusinessHour_JS.js">
</script>
<style scoped>
</style>
