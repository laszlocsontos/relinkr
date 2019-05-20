<template>
  <b-container fluid class="vh-100 d-flex flex-column" style="max-width: 600px;">
    <b-row align-v="center" class="flex-grow-1">
      <b-col align-self="center">
        <b-card
            border-variant="primary"
            header="Log in"
            header-bg-variant="primary"
            header-text-variant="white"
            align="center">

          <b-row>
            <b-col>
              <b-button :href="oauth2InitUrl('google')" variant="danger" size="lg" style="width: 170px;">
                <span class="px-1"><strong>G</strong></span>
                <span class="px-1">Google</span>
              </b-button>
            </b-col>
            <b-col>
              <b-button :href="oauth2InitUrl('facebook')" variant="primary" size="lg" style="width: 170px;">
                <span class="px-1">
                  <facebook-icon class="custom-class"></facebook-icon>
                </span>
                <span class="px-1">Facebook</span>
              </b-button>
            </b-col>
          </b-row>
          <b-row>
          </b-row>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  import { FacebookIcon } from 'vue-feather-icons'
  import { API_BASE_URL, OAUTH2_INIT_ENDPOINT } from '@/config.js'

  export default {
    components: {
      FacebookIcon
    },
    methods: {
      oauth2InitUrl(registrationId) {
        return `${API_BASE_URL}/${OAUTH2_INIT_ENDPOINT}/${registrationId}`;
      }
    }
  };
</script>
