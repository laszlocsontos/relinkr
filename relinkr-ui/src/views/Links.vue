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
          id="linksTable"
          ref="linksTable"
          :fields="fields"
          :items="onLoad"
          :per-page="perPage"
          :current-page="currentPage"
          small striped borderless outlined>

        <template slot="showDetails" slot-scope="row">
          <chevrons-down-icon v-b-tooltip.hover title="Show Details" @click="row.toggleDetails"
                              v-if="!row.detailsShowing" class="btn-outline-primary"/>
          <chevrons-up-icon v-b-tooltip.hover title="Hide Details" @click="row.toggleDetails"
                            v-if="row.detailsShowing" class="btn-outline-primary"/>
          <a v-b-tooltip.hover title="View Target" target="_blank" :href="row.item.targetUrl">
            <external-link-icon class="btn-outline-primary"/>
          </a>
          <archive-icon v-b-tooltip.hover title="Archive Link" class="btn-outline-primary"
                        v-if="hasNextStatus(row.item.id, 'ARCHIVED')"
                        @click="onSetNextStatus(row.item.id, 'ARCHIVED')"/>
          <arrow-up-circle-icon v-b-tooltip.hover title="Activate Link" class="btn-outline-primary"
                                v-if="hasNextStatus(row.item.id, 'ACTIVE')"
                                @click="onSetNextStatus(row.item.id, 'ACTIVE')"/>
        </template>

        <template slot="shortLink" slot-scope="row">
          {{ row.item._links.shortLink.href }}
        </template>

        <template slot="row-details" slot-scope="row">
          <b-card>
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
          </b-card>
        </template>
      </b-table>
    </b-container>
  </PageTemplate>
</template>

<script>
  import {
    ArchiveIcon,
    ArrowUpCircleIcon,
    ChevronsDownIcon,
    ChevronsUpIcon,
    ExternalLinkIcon
  } from 'vue-feather-icons';
  import PageTemplate from '@/components/PageTemplate.vue';

  import {mapActions, mapGetters} from 'vuex';

  export default {
    components: {
      ArrowUpCircleIcon, ExternalLinkIcon, ArchiveIcon, ChevronsDownIcon, ChevronsUpIcon,
      PageTemplate
    },
    mounted() {
      this.$root.$on("refreshLinks", () => this._refresh());
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
          controls: {
            label: '',
            sortable: false
          }
        }
      }
    },
    methods: {
      ...mapActions('link', ['fetchLinks', 'setNextStatus']),
      onLoad(ctx, callback) {
        this.fetchLinks({page: ctx.currentPage, callback: callback});
      },
      onSetNextStatus(id, nextStatus) {
        this.setNextStatus({
          id: id,
          nextStatus: nextStatus,
          refreshCallback: () => this._refresh()
        });
      },
      _refresh() {
        this.$refs.linksTable.refresh();
      }
    },
    computed: {
      ...mapGetters('link', ['totalRows', 'perPage', 'hasNextStatus']),
      rows() {
        return this.items.length;
      }
    }
  }
</script>
