// @flow
import type {State} from '../flow.types'
export const SET_ERROR = '__SET_ERROR__'

export default {
  [SET_ERROR] (state: State, error: any) {
    state.error = error
  }
}
