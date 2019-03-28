<template>
  <div class="home">
    <b-alert show>Default Alert</b-alert>
    <img alt="Vue logo" src="../assets/logo.png">
    <airplay-icon class="custom-class"></airplay-icon>
    <b-button variant="success" @click="fillData()">Button</b-button>
    <line-chart :chart-data="datacollection"></line-chart>
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
// @ is an alias to /src
import { AirplayIcon } from 'vue-feather-icons'
import HelloWorld from '@/components/HelloWorld.vue'
import LineChart from '@/components/LineChart.js'


export default {
  name: 'home',
  components: {
    HelloWorld, AirplayIcon, LineChart
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
            backgroundColor: '#f87979',
            data: [this.getRandomInt(), this.getRandomInt()]
          }, {
            label: 'Data One',
            backgroundColor: '#f87979',
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
