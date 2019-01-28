import Vue from 'vue'
import MolgenisSiteMenu from './MolgenisSiteMenu.vue'
import MolgenisFooter from './MolgenisFooter.vue'

Vue.config.productionTip = false

new Vue({
  render: createElement => createElement(MolgenisSiteMenu)
}).$mount('#molgenis-site-menu') // id is set by molgenis-header.ftl

new Vue({
  render: createElement => createElement(MolgenisFooter)
}).$mount('#molgenis-footer') // id is set by molgenis-footer.ftl
