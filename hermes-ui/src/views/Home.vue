<template>
  <div class="home">
    <b-alert show>Default Alert</b-alert>
    <img alt="Vue logo" src="../assets/logo.png">
    <airplay-icon class="custom-class"></airplay-icon>
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import { AirplayIcon } from 'vue-feather-icons'

export default {
  name: 'home',
  components: {
    HelloWorld, AirplayIcon
  }
}
</script>
