<template>
  <PageTemplate>
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
              <line-chart :chart-data="linksData"></line-chart>
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
              <line-chart :chart-data="clicksData"></line-chart>
            </b-card-body>
            <em slot="footer">Total Clicks: <strong>259</strong></em>
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
              <doughnut-chart :chart-data="visitorsData"></doughnut-chart>
            </b-card-body>
            <em slot="footer">Unique Visitors: <strong>50</strong></em>
          </b-card>
        </b-card-group>
      </b-row>
      <b-row>
      </b-row>

    </b-container>
  </PageTemplate>
</template>

<script>
import DoughnutChart from '@/components/DoughnutChart.js'
import LineChart from '@/components/LineChart.js'
import PageTemplate from '@/components/PageTemplate.vue'

export default {
  name: 'home',
  components: {
    DoughnutChart, LineChart, PageTemplate
  },
  data() {
    return {
      linksData: {
        datasets: [{
          label: "# of links",
          data: [1, 2, 3, 2, 4, 2, 6]
        }],
        labels: ['2018-03-06', '2018-03-07', '2018-03-08', '2018-03-09', '2018-03-10', '2018-03-11', '2018-03-12'],
      },
      clicksData: {
        datasets: [{
          label: "# of clicks",
          data: [10, 20, 35, 20, 42, 25, 80]
        }],
        labels: ['2018-03-06', '2018-03-07', '2018-03-08', '2018-03-09', '2018-03-10', '2018-03-11', '2018-03-12'],
      },
      visitorsData: {
        datasets: [{
          data: [10, 30]
        }],

        // These labels appear in the legend and in the tooltips when hovering different arcs
        labels: [
          'New',
          'Returning'
        ]
      }
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
