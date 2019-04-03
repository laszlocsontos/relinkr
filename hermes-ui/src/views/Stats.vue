<template>
  <b-container fluid class="p-1 w-75">
      <b-card-group class="p-3" deck>
        <b-card
            border-variant="primary"
            header-bg-variant="primary"
            header-text-variant="white"
            wid>

          <div slot="header" align="center">Date Range</div>

          <!-- Start Date -->
          <b-row>
            <b-col sm="4" class="my-1">
              <label for="start-date">Start Date:</label>
            </b-col>
            <b-col sm="6">
              <datepicker id="start-date" input-class="form-control form-control-sm"/>
            </b-col>
          </b-row>

          <!-- End Date -->
          <b-row>
            <b-col sm="4" class="my-1">
              <label for="end-date">End Date:</label>
            </b-col>
            <b-col sm="6">
              <datepicker id="end-date" input-class="form-control form-control-sm"/>
            </b-col>
          </b-row>
        </b-card>

        <b-card
            border-variant="primary"
            header-bg-variant="primary"
            header-text-variant="white">

          <div slot="header" align="center">Long URL</div>

          <!-- Domain -->
          <b-row>
            <b-col sm="4" class="my-1">
              <label for="domain">Domain:</label>
            </b-col>
            <b-col sm="6">
              <b-form-input id="domain" size="sm"></b-form-input>
            </b-col>
          </b-row>

          <!-- Path -->
          <b-row>
            <b-col sm="4" class="my-1">
              <label for="path">Path:</label>
            </b-col>
            <b-col sm="6">
              <b-form-input id="path" size="sm"></b-form-input>
            </b-col>
          </b-row>
        </b-card>
        <b-card
            border-variant="primary"
            header-bg-variant="primary"
            header-text-variant="white"
            header="UTM Parameters">

          <div slot="header" align="center">UTM Parameters</div>

          <!-- Source -->
          <b-row>
            <b-col sm="4" class="my-1">
              <label for="utm-source">Source:</label>
            </b-col>
            <b-col sm="6">
              <b-form-input id="utm-source" size="sm"></b-form-input>
            </b-col>
          </b-row>

          <!-- Medium -->
          <b-row>
            <b-col sm="4" class="my-1">
              <label for="utm-medium">Medium:</label>
            </b-col>
            <b-col sm="6">
              <b-form-input id="utm-medium" size="sm"></b-form-input>
            </b-col>
          </b-row>

          <!-- Campaign -->
          <b-row>
            <b-col sm="4" class="my-1">
              <label for="utm-campaign">Campaign:</label>
            </b-col>
            <b-col sm="6">
              <b-form-input id="utm-campaign" size="sm"></b-form-input>
            </b-col>
          </b-row>

          <!-- Term -->
          <b-row>
            <b-col sm="4" class="my-1">
              <label for="utm-term">Term:</label>
            </b-col>
            <b-col sm="6">
              <b-form-input id="utm-term" size="sm"></b-form-input>
            </b-col>
          </b-row>

          <!-- Content -->
          <b-row>
            <b-col sm="4" class="my-1">
              <label for="utm-content">Content:</label>
            </b-col>
            <b-col sm="6">
              <b-form-input id="utm-content" size="sm"></b-form-input>
            </b-col>
          </b-row>
        </b-card>
      </b-card-group>
      <div class="p-3">
        <b-card
            border-variant="primary"
            header-bg-variant="primary"
            header-text-variant="white"
            header="Aggregated Clicks"
            align="center">
          <line-chart :chart-data="clicksData" height="150"></line-chart>
        </b-card>
      </div>
  </b-container>
</template>

<script>
import Datepicker from 'vuejs-datepicker';
import LineChart from '@/components/LineChart.js'

export default {
  components: {
    Datepicker, LineChart
  },
  data() {
    return {
      clicksData: {
        datasets: [{
          label: "# of clicks",
          data: [10, 20, 35, 20, 42, 25, 80]
        }],
        labels: ['2018-03-06', '2018-03-07', '2018-03-08', '2018-03-09', '2018-03-10',
          '2018-03-11', '2018-03-12'],
      }
    }
  }
}
</script>
