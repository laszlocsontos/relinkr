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
          :total-rows="rows"
          :per-page="perPage"
          aria-controls="my-table"
          v-on:change="requestLinks"
      ></b-pagination>

      <p class="mt-3">Current Page: {{ currentPage }}</p>

      <b-table
          id="my-table"
          :fields="fields"
          :items="items"
          :per-page="perPage"
          :current-page="currentPage"
          small striped borderless outlined>

        <template slot="show_details" slot-scope="row">
          <chevrons-down-icon v-b-tooltip.hover title="Show Details" @click="row.toggleDetails" v-if="!row.detailsShowing" class="btn-outline-primary"/>
          <chevrons-up-icon v-b-tooltip.hover title="Hide Details" @click="row.toggleDetails" v-if="row.detailsShowing" class="btn-outline-primary"/>
          <a v-b-tooltip.hover title="View Target" target="_blank" :href="row.item.short_link">
            <external-link-icon class="btn-outline-primary" />
          </a>
          <trash-2-icon v-b-tooltip.hover title="Delete Link" class="btn-outline-primary" />
        </template>

        <template slot="row-details" slot-scope="row">
          <b-card>
            <b-tabs content-class="mt-3">
              <b-tab title="UTM Parameters" active>
                <b-list-group>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utm_parameters.utm_source }}</h5>
                    </div>
                    <small class="text-muted">UTM Source</small>
                  </b-list-group-item>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utm_parameters.utm_medium }}</h5>
                    </div>
                    <small class="text-muted">UTM Medium</small>
                  </b-list-group-item>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utm_parameters.utm_campaign }}</h5>
                    </div>
                    <small class="text-muted">UTM Campaign</small>
                  </b-list-group-item>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utm_parameters.utm_term }}</h5>
                    </div>
                    <small class="text-muted">UTM Term</small>
                  </b-list-group-item>
                  <b-list-group-item href="#" disabled class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1">{{ row.item.utm_parameters.utm_content }}</h5>
                    </div>
                    <small class="text-muted">UTM Content</small>
                  </b-list-group-item>
                </b-list-group>
              </b-tab>
              <b-tab title="Clicks">
                <line-chart :chart-data="clicksData" :chartId="'clicks-chart-' + row.item.id" height="150"></line-chart>
              </b-tab>
              <b-tab title="Visitors">
                <doughnut-chart :chart-data="visitorsData" :chartId="'visitors-chart-' +row.item.id" height="150"></doughnut-chart>
              </b-tab>
            </b-tabs>
          </b-card>
        </template>
      </b-table>
    </b-container>
  </PageTemplate>
</template>

<script>
import { ExternalLinkIcon, Trash2Icon, ChevronsDownIcon, ChevronsUpIcon } from 'vue-feather-icons'
import DoughnutChart from '@/components/DoughnutChart.js'
import LineChart from '@/components/LineChart.js'
import PageTemplate from '@/components/PageTemplate.vue'

import { mapState } from 'vuex';
import { mapActions } from 'vuex';

export default {
  components: {
    ExternalLinkIcon, Trash2Icon, ChevronsDownIcon, ChevronsUpIcon,
    DoughnutChart, LineChart, PageTemplate
  },
  data() {
    return {
      perPage: 10,
      currentPage: 1,
      fields: {
        show_details: {
          label: '',
          sortable: false
        },
        short_link: {
          label: 'Link',
          sortable: false
        },
        created_at: {
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
      items: [
        { id: 1, short_link: "https://rlr.li/xyf98vad8", created_at: '3 days ago', clicks: 2, visitors: 1, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 2, short_link: "https://rlr.li/8sadf8asd", created_at: '3 days ago', clicks: 34, visitors: 3, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 3, short_link: "https://rlr.li/adg89dafg", created_at: '3 days ago', clicks: 2, visitors: 5, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 4, short_link: "https://rlr.li/fknsasdaf", created_at: '3 days ago', clicks: 345, visitors: 3, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 5, short_link: "https://rlr.li/dffdgsad8", created_at: '3 days ago', clicks: 35, visitors: 7, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 6, short_link: "https://rlr.li/fd87asdzf", created_at: '3 days ago', clicks: 210, visitors: 9, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 7, short_link: "https://rlr.li/asdpf89ff", created_at: '3 days ago', clicks: 2, visitors: 3, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 8, short_link: "https://rlr.li/asdfds877", created_at: '3 days ago', clicks: 2, visitors: 2, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 9, short_link: "https://rlr.li/xyf98vad8", created_at: '3 days ago', clicks: 2, visitors: 3, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 10, short_link: "https://rlr.li/8sadf8asd", created_at: '3 days ago', clicks: 34, visitors: 3, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 11, short_link: "https://rlr.li/adg89dafg", created_at: '3 days ago', clicks: 2, visitors: 1, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 12, short_link: "https://rlr.li/fknsasdaf", created_at: '3 days ago', clicks: 345, visitors: 3, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 13, short_link: "https://rlr.li/dffdgsad8", created_at: '3 days ago', clicks: 35, visitors: 56, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 14, short_link: "https://rlr.li/fd87asdzf", created_at: '3 days ago', clicks: 210, visitors: 3, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 15, short_link: "https://rlr.li/sdfdf9dfs", created_at: '3 days ago', clicks: 2, visitors: 4, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } },
        { id: 16, short_link: "https://rlr.li/asdfds877", created_at: '3 days ago', clicks: 2, visitors: 3, utm_parameters: { utm_source: "Facebook", utm_medium: "email", utm_campaign: "SBCC", utm_term: "Term1", utm_content: "Content1" } }
      ],
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
    ...mapActions('link', ['fetchLinks']),
    requestLinks(event) {
      // eslint-disable-next-line
      console.log("requestLinks", event);
      this.fetchLinks({page: event});
    }
  },
  computed: {
    ...mapState('link', ['links', 'page']),
    rows() {
      return this.items.length
    }
  }
}
</script>
