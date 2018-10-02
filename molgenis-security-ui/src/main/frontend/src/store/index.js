// @flow
import Vue from 'vue'
import Vuex from 'vuex'

import security from '@/security/store'
import data from '@/data/store'

Vue.use(Vuex)
export default new Vuex.Store({
  modules: {
    security,
    data
  },
  strict: process.env.NODE_ENV !== 'production'
})
