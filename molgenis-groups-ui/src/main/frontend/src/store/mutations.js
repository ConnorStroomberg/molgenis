// @flow
import type { State, Repository } from '../flow.types'
export const SET_ERROR = '__SET_ERROR__'
export const SET_REPOSITORIES = '__SET_REPOSITORIES__'

export default {
  [SET_ERROR] (state: State, error: any) {
    state.error = error
  },
  [SET_REPOSITORIES] (state: State, repositories: Array<Repository>) {
    state.repositories = repositories
  }
}
