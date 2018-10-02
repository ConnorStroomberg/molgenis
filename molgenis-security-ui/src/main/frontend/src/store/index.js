// @flow
import Vue from 'vue'
import Vuex from 'vuex'

import security from '@/security/store'

Vue.use(Vuex)
export default new Vuex.Store({
  modules: {
    security
  },
  strict: process.env.NODE_ENV !== 'production'
})
