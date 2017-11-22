// @flow
import type {State} from '../flow.types'

export const INITIAL_STATE = window.__INITIAL_STATE__ || {}

const state: State = {
  error: undefined,
  repositories: [],
  groupOwnerOptions: []
}

export default state
