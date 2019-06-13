<!--
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->

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
              <line-chart :chart-data="linksStats"></line-chart>
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
              <line-chart :chart-data="clicksStats"></line-chart>
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
              <doughnut-chart :chart-data="visitorsStats"></doughnut-chart>
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
  import DoughnutChart from '@/components/DoughnutChart.js';
  import LineChart from '@/components/LineChart.js';
  import PageTemplate from '@/components/PageTemplate.vue';

import { mapActions } from 'vuex';
import { mapGetters } from 'vuex';

  export default {
    name: 'home',
    components: {
      DoughnutChart, LineChart, PageTemplate
    },
    mounted() {
      this.fetchStats('links');
      this.fetchStats('clicks');
      this.fetchStats('visitors');
    },
    computed: {
      ...mapGetters('link', ['linksStats', 'clicksStats', 'visitorsStats'])
    },
    methods: {
      ...mapActions('link', ['fetchStats'])
    }
  }
</script>
