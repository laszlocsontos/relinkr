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
                <b-dropdown-item @click="onPeriodChange('TODAY')" :active="isPeriodActive('TODAY')">Today</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('YESTERDAY')" :active="isPeriodActive('YESTERDAY')">Yesterday</b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="onPeriodChange('THIS_WEEK')" :active="isPeriodActive('THIS_WEEK')">This Week</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('PAST_WEEK')" :active="isPeriodActive('PAST_WEEK')">Past Week</b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="onPeriodChange('THIS_MONTH')" :active="isPeriodActive('THIS_MONTH')">This Month</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('PAST_MONTH')" :active="isPeriodActive('PAST_MONTH')">Past Month</b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="onPeriodChange('THIS_YEAR')" :active="isPeriodActive('THIS_YEAR')">This Year</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('PAST_YEAR')" :active="isPeriodActive('PAST_YEAR')">Past Year</b-dropdown-item>
              </b-dropdown>
            </div>
            <b-card-body>
              <line-chart :chart-data="linksStats"></line-chart>
            </b-card-body>
            <em slot="footer">Total Links: <strong>{{linksCount}}</strong></em>
          </b-card>
          <b-card
              border-variant="primary"
              header-bg-variant="primary"
              header-text-variant="white"
              align="center">
            <div slot="header">
              <b-dropdown text="Clicks" split dropright variant="primary">
                <b-dropdown-item @click="onPeriodChange('TODAY')" :active="isPeriodActive('TODAY')">Today</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('YESTERDAY')" :active="isPeriodActive('YESTERDAY')">Yesterday</b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="onPeriodChange('THIS_WEEK')" :active="isPeriodActive('THIS_WEEK')">This Week</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('PAST_WEEK')" :active="isPeriodActive('PAST_WEEK')">Past Week</b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="onPeriodChange('THIS_MONTH')" :active="isPeriodActive('THIS_MONTH')">This Month</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('PAST_MONTH')" :active="isPeriodActive('PAST_MONTH')">Past Month</b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="onPeriodChange('THIS_YEAR')" :active="isPeriodActive('THIS_YEAR')">This Year</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('PAST_YEAR')" :active="isPeriodActive('PAST_YEAR')">Past Year</b-dropdown-item>
              </b-dropdown>
            </div>
            <b-card-body>
              <line-chart :chart-data="clicksStats"></line-chart>
            </b-card-body>
            <em slot="footer">Total Clicks: <strong>{{clicksCount}}</strong></em>
          </b-card>
          <b-card
              border-variant="primary"
              header-bg-variant="primary"
              header-text-variant="white"
              align="center">
            <div slot="header">
              <b-dropdown text="Visitors" split dropright variant="primary">
                <b-dropdown-item @click="onPeriodChange('TODAY')" :active="isPeriodActive('TODAY')">Today</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('YESTERDAY')" :active="isPeriodActive('YESTERDAY')">Yesterday</b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="onPeriodChange('THIS_WEEK')" :active="isPeriodActive('THIS_WEEK')">This Week</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('PAST_WEEK')" :active="isPeriodActive('PAST_WEEK')">Past Week</b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="onPeriodChange('THIS_MONTH')" :active="isPeriodActive('THIS_MONTH')">This Month</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('PAST_MONTH')" :active="isPeriodActive('PAST_MONTH')">Past Month</b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="onPeriodChange('THIS_YEAR')" :active="isPeriodActive('THIS_YEAR')">This Year</b-dropdown-item>
                <b-dropdown-item @click="onPeriodChange('PAST_YEAR')" :active="isPeriodActive('PAST_YEAR')">Past Year</b-dropdown-item>
              </b-dropdown>
            </div>
            <b-card-body>
              <doughnut-chart :chart-data="visitorsStats"></doughnut-chart>
            </b-card-body>
            <em slot="footer">Unique Visitors: <strong>{{visitorsCount}}</strong></em>
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

  import {mapActions, mapGetters, mapState} from 'vuex';

  export default {
    name: 'home',
    components: {
      DoughnutChart, LineChart, PageTemplate
    },
    mounted() {
      this.fetchAllStats('THIS_WEEK');
    },
    computed: {
      ...mapGetters('stats', {
        linksStats: 'getLinksStats',
        clicksStats: 'getClicksStats',
        visitorsStats: 'getVisitorsStats'
      }),
      ...mapState('stats', ['period', 'linksCount', 'clicksCount', 'visitorsCount'])
    },
    methods: {
      ...mapActions('stats', ['fetchStats']),
      isPeriodActive(period) {
        return (this.period === period);
      },
      onPeriodChange(newPeriod) {
        this.fetchAllStats(newPeriod);
      },
      fetchAllStats(period) {
        this.fetchStats({statType: 'links', period: period});
        this.fetchStats({statType: 'clicks', period: period});
        this.fetchStats({statType: 'visitors', period: period});
      }
    }
  }
</script>
