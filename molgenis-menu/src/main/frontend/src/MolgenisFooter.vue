<template>
  <div id="molgenis-footer">
    <FooterComponent :molgenis-footer="molgenisFooter"/>
  </div>
</template>

<script>
import Vue from 'vue'
import FooterComponent from '../node_modules/@molgenis/molgenis-ui-context/src/components/FooterComponent'

export default Vue.extend({
  name: 'molgenis-footer',
  components: {
    FooterComponent
  },
  data () {
    return {
      molgenisFooter: window.molgenisFooter
    }
  }
})

</script>
