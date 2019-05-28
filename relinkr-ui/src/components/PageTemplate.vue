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
  <b-container fluid class="p-0">
    <b-navbar toggleable="lg" type="dark" variant="primary" sticky>
      <b-navbar-brand href="#">reLinkR</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item to="/dashboard">Dashboard</b-nav-item>
          <b-nav-item to="/links">Links</b-nav-item>
          <b-nav-item to="/stats">Stats</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-form>
            <b-button size="sm" class="my-2 my-sm-0" v-b-modal.new-link-dialog>New Link</b-button>
          </b-nav-form>

          <b-nav-item-dropdown right>
            <!-- Using 'button-content' slot -->
            <template slot="button-content"><user-icon class="custom-class"></user-icon></template>
            <b-dropdown-item v-b-modal.user-profile-dialog @click="fetchProfile">Profile</b-dropdown-item>
            <b-dropdown-item @click="logout">Sign Out</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>

    <!-- New Link Dialog -->
    <b-modal
        id="new-link-dialog"
        title="Add New Link"
        ok-title="Save"
        @show="onShowNewLink"
        @cancel="onCancelNewLink"
        @ok="onSaveNewLink">
      <b-form-group
          id="long-url-group"
          description="Let's shorten something"
          label="Long Url"
          label-for="long-url">
        <b-form-input id="long-url" trim v-model="newLinkDialog.link.longUrl"></b-form-input>
      </b-form-group>
      <b-form-group
          id="utm-parameters-group"
          description="Let's spice it up for tracking"
          label="UTM Parameters"
          label-for="input-1">
        <b-form-input id="utm-source" placeholder="UTM Source" trim size="sm" class="my-2" v-model="newLinkDialog.link.utmParameters.utmSource"></b-form-input>
        <b-form-input id="utm-medium" placeholder="UTM Medium" trim size="sm" class="my-2" v-model="newLinkDialog.link.utmParameters.utmMedium"></b-form-input>
        <b-form-input id="utm-campaign" placeholder="UTM Campaign" trim size="sm" class="my-2" v-model="newLinkDialog.link.utmParameters.utmCampaign"></b-form-input>
        <b-form-input id="utm-term" placeholder="UTM Term" trim size="sm" class="my-2" v-model="newLinkDialog.link.utmParameters.utmTerm"></b-form-input>
        <b-form-input id="utm-content" placeholder="UTM Content" trim size="sm" class="my-2" v-model="newLinkDialog.link.utmParameters.utmContent"></b-form-input>
      </b-form-group>
      <b-alert variant="danger" :show="newLinkDialog.errors.length > 0">
        <ul>
          <li v-for="error in newLinkDialog.errors">{{ error }}</li>
        </ul>
      </b-alert>
    </b-modal>

    <!-- User Profile Dialog -->
    <b-modal id="user-profile-dialog" title="Laszlo's Profile" ok-only>
      <b-form-group label-cols="6" label-cols-lg="3" label-size="sm" label="User ID" label-for="user-id">
        <b-link v-bind:href="profileUrl">{{ userProfileId }}</b-link>
      </b-form-group>
      <b-form-group label-cols="6" label-cols-lg="3" label-size="sm" label="Full Name" label-for="user-full-name">
        <b-form-input id="user-full-name" size="sm" data-lpignore="true" v-model="fullName" disabled></b-form-input>
      </b-form-group>
      <b-form-group label-cols="6" label-cols-lg="3" label-size="sm" label="Profile Type" label-for="user-profile-type">
        <b-form-input id="user-profile-type" size="sm" data-lpignore="true" v-model="userProfileType" disabled></b-form-input>
      </b-form-group>
      <b-form-group label-cols="6" label-cols-lg="3" label-size="sm" label="API Key" label-for="user-api-key">
        <b-button id="user-api-key" size="sm" variant="danger">Reset</b-button>
      </b-form-group>
    </b-modal>

    <!-- Selected Menu's Screen -->
    <slot/>
  </b-container>
</template>

<script>
  import { UserIcon } from 'vue-feather-icons'
  import { mapState } from 'vuex';
  import { mapActions } from 'vuex';

  export default {
    name: 'PageTemplate',
    components: {
      UserIcon
    },
    data() {
      return {
        newLinkDialog: {
          errors: [],
          link: {
            longUrl: "",
            utmParameters: {
              utmSource: "",
              utmMedium: "",
              utmCampaign: "",
              utmTerm: "",
              utmContent: ""
            }
          }
        }
      }
    },
    methods: {
      ...mapActions('auth', ['logout']),
      ...mapActions('profile', ['fetchProfile']),
      ...mapActions('link', ['saveLink']),
      onSaveLinkSucceeded() {
        this.$bvModal.hide("new-link-dialog");
      },
      onSaveLinkFailed(error) {
        this.newLinkDialog.errors.push(error);
      },
      onShowNewLink() {
        this._clearNewLinkDialog();
      },
      onCancelNewLink() {
        this._clearNewLinkDialog();
      },
      onSaveNewLink() {
        this.saveLink({
          link: this.newLinkDialog.link,
          successCallback: this.onSaveLinkSucceeded,
          failureCallback: this.onSaveLinkFailed
        });
      },
      _clearNewLinkDialog() {
        this.newLinkDialog.errors = [];
        this.newLinkDialog.link = {
          longUrl: "",
          utmParameters: {
            utmSource: "",
            utmMedium: "",
            utmCampaign: "",
            utmTerm: "",
            utmContent: ""
          }
        };
      }
    },
    computed: {
      ...mapState('profile', ['userProfileId', 'userProfileType', 'fullName', 'pictureUrl', 'profileUrl'])
    }
  }
</script>
