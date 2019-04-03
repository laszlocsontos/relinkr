<template>
  <b-container fluid class="p-1 w-75">
    <b-row>
      <b-card-group deck class="p-3 m-1">
        <b-card
            border-variant="primary"
            header-bg-variant="primary"
            header-text-variant="white"
            align="center">
          <div slot="header">
            <b-dropdown text="Links" split dropright variant="primary">
              <b-dropdown-item active>Today</b-dropdown-item>
              <b-dropdown-item>Yesterday</b-dropdown-item>
              <b-dropdown-divider></b-dropdown-divider>
              <b-dropdown-item>Last 7 days</b-dropdown-item>
              <b-dropdown-item>Last 14 days</b-dropdown-item>
              <b-dropdown-item>Last 28 days</b-dropdown-item>
              <b-dropdown-divider></b-dropdown-divider>
              <b-dropdown-item>Mar 2019</b-dropdown-item>
              <b-dropdown-item>Feb 2019</b-dropdown-item>
              <b-dropdown-item>Jan 2019</b-dropdown-item>
            </b-dropdown>
          </div>
          <b-card-body>
            <line-chart :chart-data="datacollection"></line-chart>
          </b-card-body>
          <em slot="footer">Total Links: <strong>26</strong></em>
        </b-card>
        <b-card
            border-variant="primary"
            header-bg-variant="primary"
            header-text-variant="white"
            align="center">
          <div slot="header">
            <b-dropdown text="Clicks" split dropright variant="primary">
              <b-dropdown-item active>Today</b-dropdown-item>
              <b-dropdown-item>Yesterday</b-dropdown-item>
              <b-dropdown-divider></b-dropdown-divider>
              <b-dropdown-item>Last 7 days</b-dropdown-item>
              <b-dropdown-item>Last 14 days</b-dropdown-item>
              <b-dropdown-item>Last 28 days</b-dropdown-item>
              <b-dropdown-divider></b-dropdown-divider>
              <b-dropdown-item>Mar 2019</b-dropdown-item>
              <b-dropdown-item>Feb 2019</b-dropdown-item>
              <b-dropdown-item>Jan 2019</b-dropdown-item>
            </b-dropdown>
          </div>
          <b-card-body>
            <line-chart :chart-data="datacollection"></line-chart>
          </b-card-body>
          <em slot="footer">Total Clicks: <strong>26</strong></em>
        </b-card>
        <b-card
            border-variant="primary"
            header-bg-variant="primary"
            header-text-variant="white"
            align="center">
          <div slot="header">
            <b-dropdown text="Visitors" split dropright variant="primary">
              <b-dropdown-item active>Today</b-dropdown-item>
              <b-dropdown-item>Yesterday</b-dropdown-item>
              <b-dropdown-divider></b-dropdown-divider>
              <b-dropdown-item>Last 7 days</b-dropdown-item>
              <b-dropdown-item>Last 14 days</b-dropdown-item>
              <b-dropdown-item>Last 28 days</b-dropdown-item>
              <b-dropdown-divider></b-dropdown-divider>
              <b-dropdown-item>Mar 2019</b-dropdown-item>
              <b-dropdown-item>Feb 2019</b-dropdown-item>
              <b-dropdown-item>Jan 2019</b-dropdown-item>
            </b-dropdown>
          </div>
          <b-card-body>
            <line-chart :chart-data="datacollection"></line-chart>
          </b-card-body>
          <em slot="footer">Unique Visitors: <strong>26</strong></em>
        </b-card>
      </b-card-group>
    </b-row>
    <b-row>
    </b-row>

  </b-container>
</template>

<script>
  // @ is an alias to /src
  import LineChart from '@/components/LineChart.js'
  import BRow from "bootstrap-vue/src/components/layout/row";

  export default {
    name: 'home',
    components: {
      BRow,
      LineChart
    },
    data() {
      return {
        datacollection: null
      }
    },
    mounted() {
      this.fillData()
    },
    methods: {
      fillData() {
        this.datacollection = {
          labels: [this.getRandomInt(), this.getRandomInt()],
          datasets: [
            {
              label: 'Data One',
              data: [this.getRandomInt(), this.getRandomInt()]
            }, {
              label: 'Data One',
              data: [this.getRandomInt(), this.getRandomInt()]
            }
          ]
        }
      },
      getRandomInt() {
        return Math.floor(Math.random() * (50 - 5 + 1)) + 5
      }
    }
  }
</script>
