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
    <b-container fluid class="p-4 w-75">
      <b-pagination
          v-model="currentPage"
          :total-rows="totalRows"
          :per-page="perPage"
          aria-controls="my-table"
      ></b-pagination>

      <p class="mt-3">Current Page: {{ currentPage }}</p>

      <b-table
          id="my-table"
          :fields="fields"
          :items="onLoad"
          :per-page="perPage"
          :current-page="currentPage"
          small striped borderless outlined>

        <template slot="showDetails" slot-scope="row">
          <chevrons-down-icon v-b-tooltip.hover title="Show Details" @click="row.toggleDetails" v-if="!row.detailsShowing" class="btn-outline-primary"/>
          <chevrons-up-icon v-b-tooltip.hover title="Hide Details" @click="row.toggleDetails" v-if="row.detailsShowing" class="btn-outline-primary"/>
          <a v-b-tooltip.hover title="View Target" target="_blank" :href="row.item.longUrl">
            <external-link-icon class="btn-outline-primary" />
          </a>
          <archive-icon v-b-tooltip.hover title="Archive Link" class="btn-outline-primary" @click="onArchive(row.item.id)" />
        </template>

        <template slot="shortLink" slot-scope="row">
          {{ row.item._links.shortLink.href }}
        </template>

        <template slot="row-details" slot-scope="row">
          <b-card>
            <b-tabs content-class="mt-3">
              <b-tab title="UTM Parameters" active>
                <b-list-group>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utmParameters.utmSource }}</h5>
                    </div>
                    <small class="text-muted">UTM Source</small>
                  </b-list-group-item>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utmParameters.utmMedium }}</h5>
                    </div>
                    <small class="text-muted">UTM Medium</small>
                  </b-list-group-item>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utmParameters.utmCampaign }}</h5>
                    </div>
                    <small class="text-muted">UTM Campaign</small>
                  </b-list-group-item>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utmParameters.utmTerm }}</h5>
                    </div>
                    <small class="text-muted">UTM Term</small>
                  </b-list-group-item>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utmParameters.utmContent }}</h5>
                    </div>
                    <small class="text-muted">UTM Content</small>
                  </b-list-group-item>
                </b-list-group>
              </b-tab>
              <b-tab title="Clicks">
                <line-chart :chart-data="clicksData" :chartId="'clicks-chart-' + row.item.id" height=150></line-chart>
              </b-tab>
              <b-tab title="Visitors">
                <doughnut-chart :chart-data="visitorsData" :chartId="'visitors-chart-' +row.item.id" height=150></doughnut-chart>
              </b-tab>
            </b-tabs>
          </b-card>
        </template>
      </b-table>
    </b-container>
  </PageTemplate>
</template>

<script>
import { ExternalLinkIcon, ArchiveIcon, ChevronsDownIcon, ChevronsUpIcon } from 'vue-feather-icons'
import DoughnutChart from '@/components/DoughnutChart.js'
import LineChart from '@/components/LineChart.js'
import PageTemplate from '@/components/PageTemplate.vue'

import { mapGetters } from 'vuex';
import { mapActions } from 'vuex';

export default {
  components: {
    ExternalLinkIcon, ArchiveIcon, ChevronsDownIcon, ChevronsUpIcon,
    DoughnutChart, LineChart, PageTemplate
  },
  data() {
    return {
      currentPage: 1,
      fields: {
        showDetails: {
          label: '',
          sortable: false
        },
        shortLink: {
          label: 'Link',
          sortable: false
        },
        createdDate: {
          label: 'Created At',
          sortable: true
        },
        clicks: {
          label: 'Clicks',
          sortable: true
        },
        visitors: {
          label: 'Unique Visitors',
          sortable: true
        },
        controls: {
          label: '',
          sortable: false
        }
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
  methods: {
    ...mapActions('link', ['fetchLinks', 'archiveLink']),
    onLoad(ctx, callback) {
      // eslint-disable-next-line
      console.log("onLoad", ctx, callback);
      this.fetchLinks({page: ctx.currentPage, callback: callback});
    },
    onArchive(id) {
      // eslint-disable-next-line
      console.log("onArchive", id);
      this.archiveLink({id: id, callback: () => this.$refs.table.refresh() });
    }
  },
  computed: {
    ...mapGetters('link', ['totalRows', 'perPage']),
    rows() {
      return this.items.length
    }
  }
}
</script>
